package com.santa.sessions.repositories
import cats.effect.IO
import com.santa.sessions.models.{Session, SessionNotFoundError}
import doobie.implicits.toSqlInterpolator
import doobie.util.log.LogHandler
import doobie.util.transactor.Transactor
import doobie.implicits._

class PostgresSessionsRepository(transactor: Transactor[IO]) extends SessionsRepository {

  private val simpleLogHandler = LogHandler.jdkLogHandler

  override def create(input: Session): IO[Session] = {
    sql"INSERT INTO sessions (id, name, passphrase, session_scrambled, emailsSent) VALUES (${input.id}, ${input.name}, ${input.passphrase}, ${input.sessionScrambled}, ${input.emailsSent})"
      .updateWithLogHandler(simpleLogHandler).run.transact(transactor).map(_ => {
      input
    })
  }

  override def getSessions: IO[List[Session]] = {
    sql"SELECT id, name, passphrase, session_scrambled, emailsSent FROM sessions".query[Session].stream.compile.toList.transact(transactor)
  }


  override def get(id: String): IO[Either[SessionNotFoundError.type, Session]] = {
    sql"SELECT id, session_id, name, email, participates, comment FROM sessions WHERE id = $id".query[Session].option.transact(transactor).map {
      case Some(session) => Right(session)
      case None => Left(SessionNotFoundError)
    }
  }

  override def deleteSession(id: String): IO[Either[SessionNotFoundError.type, Boolean]] = {
    sql"DELETE FROM sessions WHERE id = $id".update.run.transact(transactor).map { affectedRows =>
      if (affectedRows == 1) {
        Right(true)
      } else {
        Left(SessionNotFoundError)
      }
    }
  }


  override def updateSession(id: String, session: Session): IO[Either[SessionNotFoundError.type, Session]] = {
    sql"UPDATE session SET name = ${session.name}, passphrase = ${session.passphrase}, session_scrambled = ${session.sessionScrambled}, emailsSent = ${session.emailsSent} WHERE id = $id".update.run.transact(transactor).map { affectedRows =>
      if (affectedRows == 1) {
        Right(session)
      } else {
        Left(SessionNotFoundError)
      }
    }
  }
}
