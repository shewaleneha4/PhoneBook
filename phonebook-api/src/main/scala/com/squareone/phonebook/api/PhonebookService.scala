package com.squareone.phonebook.api


import com.squareone.phonebook.api.request._
import com.squareone.phonebook.api.response._
import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait PhonebookService extends Service{

  def getContact(id:String) : ServiceCall[NotUsed,phonebookResponse]
  def createContact() : ServiceCall[phonebookRequest,Done]
  def updateContact() : ServiceCall[phonebookRequest,Done]
  def deleteContact(id:String) : ServiceCall[NotUsed,Done]

  override final def descriptor: Descriptor = {
    import Service._
    // @formatter:off
    named("phonebook")
      .withCalls(
        restCall(Method.GET, "/api/get/phonebook/id/:id", getContact _),
        restCall(Method.POST,"/api/add/phonebook/",createContact _),
        restCall(Method.PUT,"/api/update/phonebook/id", updateContact _),
        restCall(Method.DELETE,"/api/delete/phonebook/id/:id", deleteContact _)
      )
      .withAutoAcl(true)
    // @formatter:on
  }

}