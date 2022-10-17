package com.santa

import cats.effect._
import cats.implicits._
import com.santa.matches.controllers.MatchesController
import com.santa.participants.controllers.ParticipantsController
import com.santa.sessions.controllers.SessionsController
import org.http4s._
import org.http4s.implicits._
import org.http4s.server._
import org.http4s.server.blaze.BlazeServerBuilder

object App extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val apis = Router(
      "/api" -> allRoutes[IO],
    ).orNotFound

    BlazeServerBuilder[IO](runtime.compute)
      .bindHttp(8080, "localhost")
      .withHttpApp(apis)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }

  def allRoutes[F[_] : Concurrent]: HttpRoutes[F] = {
    SessionsController.sessionRoutes[F] <+> ParticipantsController.participantRoutes[F] <+> MatchesController.matchRoutes[F]
  }
}
