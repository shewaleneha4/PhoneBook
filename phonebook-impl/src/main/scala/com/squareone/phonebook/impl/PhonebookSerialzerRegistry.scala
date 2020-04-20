package com.squareone.phonebook.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import com.squareone.phonebook.api.request.phonebookRequest
import com.squareone.phonebook.api.response.phonebookResponse

object PhonebookSerialzerRegistry extends JsonSerializerRegistry {
  override val serializers = Vector(
    JsonSerializer[phonebookRequest],
    JsonSerializer[phonebookResponse]
  )
}