package server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

object MyServer {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("Server")

    val routes = NewpageRouter.route ~ HomepageRouter.route

    val host = "0.0.0.0"
    val port = sys.env.getOrElse("PORT", "8080").toInt

    Http().bindAndHandle(routes, host, port)
  }
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
