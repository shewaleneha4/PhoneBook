package com.squareone.phonebook.impl.repository



import akka.Done
import akka.actor.Status.{Failure, Success}
import com.datastax.driver.core.{PreparedStatement, Row}
import com.lightbend.lagom.scaladsl.api.transport.BadRequest
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import com.squareone.phonebook.api.request.phonebookRequest
import com.squareone.phonebook.api.response.phonebookResponse
import com.squareone.phonebook.impl.repository.PhonebookRepository
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}

class PhonebookRepository(session: CassandraSession)(implicit ec: ExecutionContext) {

  private val logger = LoggerFactory.getLogger(classOf[PhonebookRepository])


  private def createContactPrepareStatement() = {
    lazy val numberOfAttribute = 4
    session.prepare(
      s"""
         |INSERT INTO phone_book (
         |id,
         |name,
         |surname,
         |number)
         |VALUES (${List.fill(numberOfAttribute)("?").mkString(",")})
      """.stripMargin
    )
  }

  private def getContactPrepareStatement() = {
    session.prepare(
      s"""
         |SELECT
         |id,
         |name,
         |surname,
         |number
         |FROM phone_book WHERE id = ?
      """.stripMargin
    )
  }

  private def updateContactPrepareStatement(): Future[PreparedStatement] = {
    session.prepare(
      s"""
         |UPDATE phone_book
         |SET
         |name=?,
         |surname=?,
         |number=?
         |WHERE id=?
      """.stripMargin
    )
  }

  private def deleteContactPrepareStatement(): Future[PreparedStatement] = {
    session.prepare(
      s"""
         |DELETE FROM phone_book
         |WHERE id=?
      """.stripMargin

    )
  }



  def createContactRepo(req: phonebookRequest) = {
    createContactPrepareStatement().flatMap(stmt => {
      session.executeWrite(stmt.bind(
        req.id,
        req.name,
        req.surname,
        req.number
      ))
    })
  }

  def getContactRepo(id: String) = {
    getContactPrepareStatement().flatMap(stmt => {
      session.selectOne(stmt.bind(id))
        .map(row =>
          row match {
            case Some(res) => phonebookResponse(res)
            case None => throw BadRequest("Record Not Found ")


          })
    })
  }

  def updateContactRepo(req: phonebookRequest) = {
    updateContactPrepareStatement().flatMap(stmt => {
      session.executeWrite(stmt.bind(
        req.id,
        req.name,
        req.surname,
        req.number
      ))
    })
  }




  def deleteContactRepo(id:String)={
    deleteContactPrepareStatement().flatMap(stmt =>
      session.executeWrite(stmt.bind(id))
    )
  }
}

