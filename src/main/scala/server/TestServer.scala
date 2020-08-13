package server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._

object TestServer {

  val testContent =
    """
      |<html>
      | <head>Test</head>
      | <body>
      |   Test HTML content served by Akka HTTP on Heroku
      | </body>
      |</html>
      |""".stripMargin

  val route = get {
     complete(
       HttpEntity(
         ContentTypes.`text/html(UTF-8)`,
         testContent
       )
     )
  }

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("TestServer")

    val host = "0.0.0.0"
    val port = sys.env.getOrElse("PORT", "1234").toInt

    Http().bindAndHandle(route, host, port)
  }
}
