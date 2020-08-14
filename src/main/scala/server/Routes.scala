package server

import java.io.File

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives

object Routes extends Directives with JsonSupport {

  def helper(body: Body ): String = {
    println(body)
    body.toString
  }

  val routes =
    concat(

      path("idw-img") {
        get {
          complete(
            HttpEntity.fromFile(ContentTypes.`application/octet-stream`, new File("IDW_img.png"))
          )
        }
      },

      path("test") {
        post {
          entity(as[Body]) { body =>
            complete(
              HttpEntity(
                ContentTypes.`text/html(UTF-8)`,
                helper(body)
              )
            )
          }
        }
      },

      path("interpolate") {
        post {
          entity(as[Body]) { body =>
            Handler.response(body)
          }
        }
      },
    )
}