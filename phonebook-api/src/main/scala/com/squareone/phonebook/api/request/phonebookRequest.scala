package com.squareone.phonebook.api.request

import play.api.libs.json.Json
import com.datastax.driver.core.Row
import play.api.libs.json.Json.format


case class phonebookRequest(id:String, name:String, surname:String, number:String)

object phonebookRequest {

  def apply(row : Row) : phonebookRequest = phonebookRequest.createContact(row)

  def createContact(row : Row) =
    phonebookRequest(
      id=row.getString("id"),
      name = row.getString("name"),
      surname = row.getString("surname"),
      number = row.getString("number")
    )
  implicit lazy val jsonphonebookRequest = Json.format[phonebookRequest]


}
