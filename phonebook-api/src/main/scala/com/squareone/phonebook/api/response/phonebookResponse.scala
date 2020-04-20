package com.squareone.phonebook.api.response

import play.api.libs.json.Json
import com.datastax.driver.core.Row
import play.api.libs.json.Json.format

case class phonebookResponse(id:String, name:String, surname:String, number:String)

object phonebookResponse{
  def apply(row: Row) : phonebookResponse = phonebookResponse.getcontact(row)

  def getcontact(row: Row) =
    phonebookResponse(
      id= row.getString("id"),
      name=row.getString("name"),
      surname = row.getString("surname"),
      number = row.getString("number")
    )

  implicit lazy val jsonphonebookResponse= Json.format[phonebookResponse]
}


