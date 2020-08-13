package server

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn

object MyServer {

  val localEnv: Boolean = true

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem(Behaviors.empty, "my-system")
    implicit val executionContext = system.executionContext

    val host = "0.0.0.0"
    val port = sys.env.getOrElse("PORT", "8080").toInt

    val bindingFuture = Http().newServerAt(host, port).bind(Routes.routes)
    if (localEnv) {
      println(s"Server online.\nPress RETURN to stop...")
      StdIn.readLine()
      bindingFuture
        .flatMap(_.unbind())
        .onComplete(_ => system.terminate())
    }
  }
}

object Routes2 {
  val routes = concat(
    path("hello") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
      }
    },
    path("hello2") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello2 to akka-http</h1>"))
      }
    },
  )
}

object NewpageRouter {
  val content =
    """
      |<html>
      | <head>Test Newpage</head>
      | <body>
      |   Test Newpage HTML content served by Akka HTTP on Heroku
      | </body>
      |</html>
      |""".stripMargin

  val route = path("new") {
    get {
      complete(
        HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          content
        )
      )
    }
  }
}

object HomepageRouter {
  val content =
    """
      |<html>
      | <head>Test Homepage</head>
      | <body>
      |   Test Homepage HTML content served by Akka HTTP on Heroku
      | </body>
      |</html>
      |""".stripMargin

  val route = path("home") {
    get {
      complete(
        HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          content
        )
      )
    }
  }
}