package server

import akka.http.scaladsl.server.StandardRoute
import interpolation.IDWInterpolator

class IDWHandler(body: Body) extends HandlerInterface(body) {

  import Handler._

  val width = body.data.width
  val height = body.data.height

  def interpolate: StandardRoute = {
    val interpolator: IDWInterpolator =
      new IDWInterpolator(width, height, body.data.sample, body.scale.getOrElse(defaultScale), 2)
    val img = interpolator.interpolateImage
    returnImage(img)
  }
}
