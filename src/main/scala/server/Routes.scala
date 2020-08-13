package server

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, path, concat}

object Routes {
  val routes =
    concat(
      path("hello") {
        get {
         complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
        }
      },
      path("hello2") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello2 to akka-http</h1>"))
        }
      }
    )
}

// For records, just in case

/*
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
 */