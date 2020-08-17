package server

import akka.http.scaladsl.model.{StatusCodes, _}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.StandardRoute

object Endpoint {

  def respond(body: Body): StandardRoute = {

    val interpolatorType = body.interpolatorType.get

    val handler: Option[HandlerInterface] = interpolatorType match {
      case "barnes" => Some(new BarnesHandler(body))
      case "idw" => Some(new IDWHandler(body))
      case "nn" => Some(new NNHandler(body))
      case "rbf" => Some(new RBFHandler(body))
      case "wrapper" => Some(new WrapperHandler(body))

      // error case
      case _ => None
    }

    if (handler.isEmpty) {
      complete(
        HttpResponse(
          StatusCodes.BadRequest,
          entity = "Invalid interpolator type"
        )
      )
    } else {
      handler.get.respond
    }
  }
}
