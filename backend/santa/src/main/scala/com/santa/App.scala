package com.santa

import cats._
import cats.effect._
import cats.implicits._
import com.santa.sessions.models.Session
import org.http4s.circe._
import org.http4s._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.dsl._
import org.http4s.dsl.impl._
import org.http4s.headers._
import org.http4s.implicits._
import org.http4s.server._
import org.http4s.server.blaze.BlazeServerBuilder
import org.typelevel.ci.CIString

import java.util.UUID

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

  def sessionRoutes[F[_] : Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._
    HttpRoutes.of[F] {
      case POST -> Root / "sessions"  => {
        Ok(Session("Verve", UUID.randomUUID().toString).asJson)
      }
    }
  }

  def allRoutes[F[_] : Monad]: HttpRoutes[F] = {
    sessionRoutes[F]
  }
}
