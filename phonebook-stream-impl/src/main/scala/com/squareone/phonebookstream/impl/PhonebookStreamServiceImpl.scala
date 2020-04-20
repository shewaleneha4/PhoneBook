package com.squareone.phonebookstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.squareone.phonebookstream.api.PhonebookStreamService
import com.squareone.phonebook.api.PhonebookService

import scala.concurrent.Future

/**
  * Implementation of the PhonebookStreamService.
  */
class PhonebookStreamServiceImpl(phonebookService: PhonebookService) extends PhonebookStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(phonebookService.hello(_).invoke()))
  }
}
