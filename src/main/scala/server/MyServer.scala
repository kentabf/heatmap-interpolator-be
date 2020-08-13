package server

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http

import scala.io.StdIn

object MyServer {

  val localEnv: Boolean = true

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem(Behaviors.empty, "my-system")
    implicit val executionContext = system.executionContext

    val host = "0.0.0.0"
    val port = sys.env.getOrElse("PORT", "8080").toInt

    val bindingFuture = Http().newServerAt(host, port).bind(Routes.route)
    if (localEnv) {
      println(s"Server online.\nPress RETURN to stop...")
      StdIn.readLine()
      bindingFuture
        .flatMap(_.unbind())
        .onComplete(_ => system.terminate())
    }
  }
}
