package com.squareone.phonebook.impl

import com.squareone.phonebook.api.PhonebookService
import akka.Done
import akka.NotUsed
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import scala.concurrent.ExecutionContext
import com.squareone.phonebook.api.request.phonebookRequest
import com.squareone.phonebook.api.response.phonebookResponse
import com.squareone.phonebook.impl.repository.PhonebookRepository


class PhonebookServiceImpl(PhonebookRepository: PhonebookRepository)(
  clusterSharding: ClusterSharding,
  persistentEntityRegistry: PersistentEntityRegistry
)(implicit ec: ExecutionContext)
  extends PhonebookService {



  override def getContact(id: String): ServiceCall[NotUsed, phonebookResponse] = ServiceCall{
    request => PhonebookRepository.getContactRepo(id)
  }



  override def createContact(): ServiceCall[phonebookRequest, Done] =ServiceCall{
    request => PhonebookRepository.createContactRepo(request)
  }


  override def updateContact():ServiceCall[phonebookRequest,Done] =ServiceCall{
    request =>  PhonebookRepository.updateContactRepo(request)
  }

  override def deleteContact(id: String): ServiceCall[NotUsed,Done]=ServiceCall{
    request => PhonebookRepository.deleteContactRepo(id)
  }
}


