package com.santa.participants.repositories
import cats.effect.IO
import com.santa.participants.models.{Participant, ParticipantNotFoundError}
import doobie.implicits.toSqlInterpolator
import doobie.util.log.LogHandler
import doobie.util.transactor.Transactor
import doobie.implicits._

class PostgresParticipantsRepository(transactor: Transactor[IO]) extends ParticipantsRepository {

  private val simpleLogHandler = LogHandler.jdkLogHandler
  override def create(input: Participant): IO[Participant] = {
    sql"INSERT INTO participants (id, session_id, first_name, last_name, email, participates, comment) VALUES (${input.id}, ${input.sessionId}, ${input.firstName}, ${input.lastName}, ${input.email}, ${input.participates}, ${input.comment})"
        .updateWithLogHandler(simpleLogHandler).run.transact(transactor).map(_ => {
      input
    })
  }

  override def getParticipants(sessionId: String): IO[List[Participant]] = {
    sql"SELECT id, session_id, first_name, last_name, email, participates, comment FROM participants WHERE session_id = $sessionId".query[Participant].stream.compile.toList.transact(transactor)
  }

  override def getParticipant(id: String): IO[Either[ParticipantNotFoundError.type, Participant]] = {
    sql"SELECT id, session_id, first_name, last_name, email, participates, comment FROM participants WHERE id = $id".query[Participant].option.transact(transactor).map {
      case Some(participant) => Right(participant)
      case None => Left(ParticipantNotFoundError)
    }
  }

  override def deleteParticipant(id: String): IO[Either[ParticipantNotFoundError.type, Boolean]] = {
    sql"DELETE FROM participants WHERE id = $id".update.run.transact(transactor).map { affectedRows =>
      if (affectedRows == 1) {
        Right(true)
      } else {
        Left(ParticipantNotFoundError)
      }
    }
  }

  override def updateParticipant(id: String, participant: Participant): IO[Either[ParticipantNotFoundError.type, Participant]] = {
    sql"UPDATE participants SET first_name = ${participant.firstName},  last_name =${participant.lastName}, email = ${participant.email}, participates = ${participant.participates}, comment = ${participant.comment} WHERE id = $id".update.run.transact(transactor).map { affectedRows =>
      if (affectedRows == 1) {
        Right(participant)
      } else {
        Left(ParticipantNotFoundError)
      }
    }
  }
}
