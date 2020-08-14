package server

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes, _}
import interpolation._
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.io.File

import server.Routes.complete

import scala.math.pow

object Handler {

  val defaultScale: ColorScale = List (
    ColorMap(0.0, Color (255, 0, 0) ),
    ColorMap(0.25, Color (255, 255, 0) ),
    ColorMap(0.5, Color (0, 255, 0) ),
    ColorMap(0.75, Color (0, 255, 255) ),
    ColorMap(1.0, Color (0, 0, 255) ),
  )

  def response(body: Body) = {

    val interpolatorType = body.interpolatorType.get
    val width = body.data.width
    val height = body.data.height
    val sample = body.data.sample
    val scale = if (body.scale == None) defaultScale else body.scale.get

    val interpolator: Option[InterpolatorInterface] = interpolatorType match {
      case "barnes" => Some(new BarnesInterpolator(
        width, height, sample, defaultScale,
        math.min(width/2, height/2)
      ))
      case "idw" => Some(new IDWInterpolator(width, height, sample, scale, 2))
      case "nn" => Some(new NNInterpolator(width, height, sample, scale))
      case "rbf" => Some(new RBFInterpolator(width, height, sample, scale, r => 1/(pow(r, 0.5))))
      case "wrapper" => Some(new WrapperInterpolator(width, height, sample, scale, 3))

      // error case
      case _ => None
    }

    if (interpolator == None) {
      complete(
        HttpResponse(
          StatusCodes.BadRequest,
          entity = "Request formatted incorrectly"
        )
      )
    } else {
      val img: BufferedImage = interpolator.get.interpolateImage
      ImageIO.write(img, "png", new File(s"${interpolator.get.name}_img.png"))
      complete(
        HttpEntity.fromFile(
          ContentTypes.`application/octet-stream`,
          new File(s"${interpolator.get.name}_img.png")
        )
      )
    }
  }
}
