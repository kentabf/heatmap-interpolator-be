package interpolation

import java.awt.image.BufferedImage
import scala.math.floor

abstract class InterpolatorInterface(width: Int, height: Int, sample: TemperaturesData, scale: ColorScale) {
  //Abstract
  val name: String
  def interpolateTemperature(mlToInterpolate: MapLocation): Temperature

  //Implemented
  def interpolateColor(temperature: Temperature): Color = {
    var upper: ColorMap = ColorMap(Double.MaxValue, Color(0, 0, 0))
    var lower: ColorMap = ColorMap(Double.MinValue, Color(0, 0, 0))
    for ( colorMap <- scale ) {
      val elemTemp = colorMap.temperature
      if ( elemTemp >= temperature && elemTemp < upper.temperature ) {
        upper = colorMap
      }
      if ( elemTemp <= temperature && elemTemp > lower.temperature ) {
        lower = colorMap
      }
    }
    if ( upper.temperature == lower.temperature ) {
      upper.color
    } else {
      val uRatio = (temperature - lower.temperature)/(upper.temperature - lower.temperature)
      val r = ((upper.color.red - lower.color.red) * uRatio + lower.color.red).round.toInt
      val g = ((upper.color.green - lower.color.green) * uRatio + lower.color.green).round.toInt
      val b = ((upper.color.blue - lower.color.blue) * uRatio + lower.color.blue).round.toInt
      Color(r, g, b)
    }
  }
  def idxToMapLocation(idx: Int): MapLocation = {
    MapLocation(floor(idx/width).toInt, idx % width)
  }
  def mapLocationToIdx(ml: MapLocation): Int = {
    ml.i*width + ml.j
  }
  def interpolateImage: BufferedImage = {
    val img: BufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val idxArray: Array[Int] = new Array[Int](height*width)
    for (idx <- 0 until idxArray.size) {
      idxArray(idx) = idx
    }
    idxArray
      .map( (idx: Int) => idxToMapLocation(idx) )
      .map( (ml: MapLocation) => {
        val inSample: TemperaturesData = sample.filter(ml == _.location)
        if (inSample.size > 0) {
          Point(ml, inSample.head.temperature)
        } else {
          Point(ml, interpolateTemperature(ml))
        }
      })
      .map( point => Pixel(point.location, interpolateColor(point.temperature)))
      .foreach( (pixel: Pixel) => {
        img.setRGB(pixel.location.j, pixel.location.i, pixel.color.rgbToInt)
      })
    img
  }
}
