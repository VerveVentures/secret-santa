package com.santa.matches.controllers

import cats.effect._
import cats.implicits._
import com.santa.matches.models.CreateMatchInput
import com.santa.matches.repositories.{InMemoryMatchesRepository, MatchesRepository}
import com.santa.matches.services.{MatchesService, MatchesServiceImpl}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._

object MatchesController {

  val matchesRepository: MatchesRepository = new InMemoryMatchesRepository()
  val matchesService: MatchesService = new MatchesServiceImpl(matchesRepository)


  def matchRoutes[F[_] : Concurrent]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "matches" / UUIDVar(sessionId)=> {
        Ok(matchesService.getMatches(sessionId.toString).asJson)
      }
      case GET -> Root / "match" / UUIDVar(matchId) => {
        Ok(matchesService.get(matchId.toString).asJson)
      }
      case req@POST -> Root / "matches" =>
        implicit val sessionsDecoder: EntityDecoder[F, CreateMatchInput] = jsonOf[F, CreateMatchInput]
        for {
          createMatchInput <- req.as[CreateMatchInput]
          matching = matchesService.create(createMatchInput)
          res <- Ok(matching.asJson)
        } yield {
          res
        }
    }
  }
}
