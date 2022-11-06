package com.santa

import cats.effect._
import cats.implicits._
import com.santa.database.Database
import com.santa.matches.controllers.MatchesController
import com.santa.participants.controllers.ParticipantsController
import com.santa.sessions.controllers.SessionsController
import com.santa.config.config.Config
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.http4s._
import org.http4s.implicits._
import org.http4s.server._
import org.http4s.server.blaze.BlazeServerBuilder

object App extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    create()
  }

  def allRoutes[F[_] : Concurrent]: HttpRoutes[F] = {
    SessionsController.sessionRoutes[F] <+> ParticipantsController.participantRoutes[F] <+> MatchesController.matchRoutes[F]
  }

  def create(configFile: String = "application.conf"): IO[ExitCode] = {
    resources(configFile).use(create)
  }

  private def resources(configFile: String): Resource[IO, Resources] = {
    for {
      config <- Config.load(configFile)
      ec <- ExecutionContexts.fixedThreadPool[IO](config.database.threadPoolSize)
      transactor <- Database.transactor(config.database, ec)
    } yield Resources(transactor, config)
  }

  private def create(resources: Resources): IO[ExitCode] = {
    val apis = Router(
      "/api" -> allRoutes[IO],
    ).orNotFound
    for {
      _ <- Database.initialize(resources.transactor)
      exitCode <- BlazeServerBuilder[IO](runtime.compute)
        .bindHttp(8080, "localhost")
        .withHttpApp(apis)
        .resource
        .use(_ => IO.never)
        .as(ExitCode.Success)
    } yield exitCode
  }

  case class Resources(transactor: HikariTransactor[IO], config: Config)
}
