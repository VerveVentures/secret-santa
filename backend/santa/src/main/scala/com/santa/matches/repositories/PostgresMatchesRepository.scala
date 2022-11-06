package com.santa.matches.repositories

import cats.effect.IO
import com.santa.matches.models.{Match, MatchNotFoundError}
import doobie.implicits.toSqlInterpolator
import doobie.util.transactor.Transactor
import doobie.implicits._
import doobie.util.log.LogHandler

class PostgresMatchesRepository(transactor: Transactor[IO]) extends MatchesRepository {
  private val simpleLogHandler = LogHandler.jdkLogHandler
  override def create(input: Match): IO[Match] = {
    sql"INSERT INTO matches (id, session_id, giver, receiver) VALUES (${input.id}, ${input.sessionId}, ${input.giver}, ${input.receiver})"
      .updateWithLogHandler(simpleLogHandler).run.transact(transactor).map(x => {
      input
    })
  }

  override def getMatches(sessionId: String): IO[List[Match]] = {
    sql"SELECT id, session_id, giver, receiver FROM matches".query[Match].stream.compile.toList.transact(transactor)
  }

  override def get(id: String): IO[Either[MatchNotFoundError.type, Match]] = {
    sql"SELECT id, session_id, giver, receiver FROM matches WHERE id = $id".query[Match].option.transact(transactor).map {
      case Some(todo) => Right(todo)
      case None => Left(MatchNotFoundError)
    }
  }

  override def deleteMatch(id: String): IO[Either[MatchNotFoundError.type, Boolean]] = {
    sql"DELETE FROM matches WHERE id = $id".update.run.transact(transactor).map { affectedRows =>
      if (affectedRows == 1) {
        Right(true)
      } else {
        Left(MatchNotFoundError)
      }
    }
  }
}



