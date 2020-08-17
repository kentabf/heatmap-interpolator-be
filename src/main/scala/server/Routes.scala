package server

import java.nio.file.{Files, Paths}

import akka.http.scaladsl.model.{HttpEntity, _}
import akka.http.scaladsl.server.Directives

object Routes extends Directives with JsonSupport {

  def testHelper(body: Body ): String = {
    println(body)
    body.toString
  }

  val routes =
    concat(

      path("test-image") {
        get {
          complete{
            val byteArray = Files.readAllBytes(Paths.get("IDW_img.png"))
            HttpResponse(
             StatusCodes.OK,
             entity = HttpEntity(
               ContentType(MediaTypes.`image/png`),
               byteArray
             )
           )
          }
        }
      },

      path("test-body") {
        post {
          entity(as[Body]) { body =>
            complete(
              HttpEntity(
                ContentTypes.`text/html(UTF-8)`,
                testHelper(body)
              )
            )
          }
        }
      },

      path("interpolate") {
        post {
          entity(as[Body]) { body =>
            Endpoint.respond(body)
          }
        }
      },
    )
}