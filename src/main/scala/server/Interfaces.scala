package server

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.StandardRoute
import akka.http.scaladsl.server.Directives._
import interpolation.{Color, ColorMap, ColorScale}
import javax.imageio.ImageIO

// Companion object
object Handler{
  val defaultScale: ColorScale = List (
    ColorMap(0.0, Color (255, 0, 0) ),
    ColorMap(0.2, Color (255, 255, 0) ),
    ColorMap(0.4, Color (0, 255, 0) ),
    ColorMap(0.6, Color (0, 255, 255) ),
    ColorMap(0.8, Color (0, 0, 255) ),
    ColorMap(1.0, Color (255, 0, 255) ),
  )

  val defaultScaleMax = 1.0
  val defaultScaleMin = 0.0
}

abstract class HandlerInterface(body: Body) {

  import Handler._

  // Abstract
  def interpolate: StandardRoute

  // Implemented
  // If invalid, the output returns an error string to be returned HTTP status code 400
  def validateInput: Option[String] = {
    val scaleMax = if (body.scale.isEmpty) defaultScaleMax else body.scale.get.maxBy(_.temperature).temperature
    val scaleMin = if (body.scale.isEmpty) defaultScaleMin else body.scale.get.minBy(_.temperature).temperature
    val sampleMax = body.data.sample.maxBy(_.temperature).temperature
    val sampleMin = body.data.sample.minBy(_.temperature).temperature

    if (scaleMax < sampleMax || scaleMin > sampleMin) {
      // Validate that all input temperatures are within scale
      Some("Sample not within scale")
    } else {
      None
    }
  }

  def respond: StandardRoute = {
    val validationErrorString: Option[String] = validateInput

    if (validationErrorString.isEmpty) {
      interpolate
    } else {
      complete(
        HttpResponse(
          StatusCodes.BadRequest,
          entity = validationErrorString.get
        )
      )
    }
  }

  def returnImage(img: BufferedImage): StandardRoute = {
    val baos: ByteArrayOutputStream = new ByteArrayOutputStream()
    ImageIO.write(img, "png", baos)
    complete(
      HttpResponse(
        StatusCodes.OK,
        entity = HttpEntity(
          ContentType(MediaTypes.`image/png`),
          baos.toByteArray
        )
      )
    )
  }

}
