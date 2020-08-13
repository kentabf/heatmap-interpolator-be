package server

import java.io.File

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives

object Routes extends Directives with JsonSupport {

  def helper(body: Body ): String = {
    println(body)
    "abadsfdg"
  }

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
      },
      path("img") {
        get {
          complete(
            HttpEntity.fromFile(ContentTypes.`application/octet-stream`, new File("IDW_img.png"))
          )
        }
      },

      path("testpost") {
        post {
          entity(as[Body]) { body =>
            complete(
              HttpEntity(
                ContentTypes.`application/json`,
                helper(body)
              )
            )
          }
        }
      },
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