package com.squareone.phonebookstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.squareone.phonebookstream.api.PhonebookStreamService
import com.squareone.phonebook.api.PhonebookService
import com.softwaremill.macwire._

class PhonebookStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new PhonebookStreamApplication(context) {
      override def serviceLocator: NoServiceLocator.type = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new PhonebookStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[PhonebookStreamService])
}

abstract class PhonebookStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[PhonebookStreamService](wire[PhonebookStreamServiceImpl])

  // Bind the PhonebookService client
  lazy val phonebookService: PhonebookService = serviceClient.implement[PhonebookService]
}
