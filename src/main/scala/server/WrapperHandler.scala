package server

import akka.http.scaladsl.server.StandardRoute
import interpolation.WrapperInterpolator

class WrapperHandler(body: Body) extends HandlerInterface(body) {

  import Handler._

  val width = body.data.width
  val height = body.data.height

  def interpolate: StandardRoute = {
    val interpolator: WrapperInterpolator =
      new WrapperInterpolator(width, height, body.data.sample, body.scale.getOrElse(defaultScale), 2)
    val img = interpolator.interpolateImage
    returnImage(img)
  }
}
