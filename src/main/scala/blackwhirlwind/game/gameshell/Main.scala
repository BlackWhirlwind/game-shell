package blackwhirlwind.game.gameshell

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple:
  val run = GameShellServer.run[IO]
