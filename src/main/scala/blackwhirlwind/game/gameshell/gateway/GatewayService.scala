package blackwhirlwind.game.gameshell.gateway

import grpc.gateway.{GatewayAPI, LoginRequest, LoginResponse}
import org.http4s.Headers

object GatewayService {
  def make[F[_]]: GatewayAPI[F] = new GatewayAPI[F] {
    override def login(request: LoginRequest, ctx: Headers): F[LoginResponse] = ???
  }
}
