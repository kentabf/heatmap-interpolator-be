package server

import akka.http.scaladsl.server.StandardRoute
import interpolation.BarnesInterpolator
import scala.math.min

class BarnesHandler(body: Body) extends HandlerInterface(body) {

  import Handler._

  val width = body.data.width
  val height = body.data.height
  val radius = min(width/2, height/2)

  def interpolate: StandardRoute = {
    val interpolator: BarnesInterpolator =
      new BarnesInterpolator(width, height, body.data.sample, body.scale.getOrElse(defaultScale), radius)
    val img = interpolator.interpolateImage
    returnImage(img)
  }
}
