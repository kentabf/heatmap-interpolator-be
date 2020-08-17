package server

import akka.http.scaladsl.server.StandardRoute
import interpolation.NNInterpolator

class NNHandler(body: Body) extends HandlerInterface(body) {

  import Handler._

  val width = body.data.width
  val height = body.data.height

  def interpolate: StandardRoute = {
    val interpolator: NNInterpolator =
      new NNInterpolator(width, height, body.data.sample, body.scale.getOrElse(defaultScale))
    val img = interpolator.interpolateImage
    returnImage(img)
  }
}
