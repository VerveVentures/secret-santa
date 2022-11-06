package com.santa.matches.controllers

import cats.effect._
import cats.implicits._
import com.santa.matches.models.CreateMatchInput
import com.santa.matches.repositories.{MatchesRepository, PostgresMatchesRepository}
import com.santa.matches.services.{MatchesService, MatchesServiceImpl}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._

class MatchesController(matchesService: MatchesService) {

  def matchRoutes: HttpRoutes[IO] = {
    val dsl = Http4sDsl[IO]
    import dsl._

    HttpRoutes.of[IO] {
      case GET -> Root / "matches" / UUIDVar(sessionId)=> {
        for {
          result <- matchesService.getMatches(sessionId.toString)
          result <- Ok(result.asJson)
        } yield {
          result
        }
      }
      case GET -> Root / "match" / UUIDVar(matchId) => {
        for {
          result <- matchesService.get(matchId.toString)
          result <- Ok(result.asJson)
        } yield {
          result
        }
      }
      case req@POST -> Root / "matches" =>
        implicit val sessionsDecoder: EntityDecoder[IO, CreateMatchInput] = jsonOf[IO, CreateMatchInput]
        for {
          createMatchInput <- req.as[CreateMatchInput]
          matching <- matchesService.create(createMatchInput)
          res <- Ok(matching.asJson)
        } yield {
          res
        }
    }
  }
}
