package com.santa.database

import cats.effect.{IO, Resource}
import com.santa.config.config.DatabaseConfig
import doobie.hikari.HikariTransactor

import scala.concurrent.ExecutionContext

object Database {
  def transactor(config: DatabaseConfig, executionContext: ExecutionContext): Resource[IO, HikariTransactor[IO]] = {
    println("config is", config)
    HikariTransactor.newHikariTransactor[IO](
      config.driver,
      config.url,
      config.user,
      config.password,
      executionContext
    )
  }

  def initialize(transactor: HikariTransactor[IO]): IO[Unit] = {
    transactor.configure { dataSource =>
      IO {
        println("successfully connected to db")
        ()
      }
    }
  }
}