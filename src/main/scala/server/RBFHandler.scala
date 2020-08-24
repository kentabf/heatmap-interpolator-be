package server

import akka.http.scaladsl.server.StandardRoute
import interpolation.RBFInterpolator
import math.pow

class RBFHandler(body: Body) extends HandlerInterface(body) {

  import Handler._

  val width = body.data.width
  val height = body.data.height

  def interpolate: StandardRoute = {
    val interpolator: RBFInterpolator =
      new RBFInterpolator(width, height, body.data.sample, body.scale.getOrElse(defaultScale), r => 1/(pow(1+pow(r, 2), 0.5)))
    val img = interpolator.interpolateImage
    returnImage(img)
  }
}
