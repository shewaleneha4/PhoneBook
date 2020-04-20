package com.squareone.phonebook.impl

import akka.cluster.sharding.typed.scaladsl.Entity
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.squareone.phonebook.api.PhonebookService
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.softwaremill.macwire._
import com.squareone.phonebook.impl.repository.PhonebookRepository

class PhonebookLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new PhonebookApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new PhonebookApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[PhonebookService])
}

abstract class PhonebookApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[PhonebookService](wire[PhonebookServiceImpl])
  lazy val contactbookService = serviceClient.implement[PhonebookService]
  lazy val lagomRepository = wire[PhonebookRepository]


  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry: JsonSerializerRegistry = PhonebookSerializerRegistry

  // Initialize the sharding of the Aggregate. The following starts the aggregate Behavior under
  // a given sharding entity typeKey.
  clusterSharding.init(
    Entity(PhonebookState.typeKey)(
      entityContext => PhonebookBehavior.create(entityContext)
    )
  )

}
