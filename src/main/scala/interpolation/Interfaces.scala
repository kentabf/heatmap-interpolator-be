package interpolation

import java.awt.image.BufferedImage
import scala.math.floor

abstract class InterpolatorInterface(width: Int, height: Int, sample: TemperaturesData, scale: ColorScale) {
  //Abstract
  val name: String
  def interpolateTemperature(mlToInterpolate: MapLocation): Temperature

  //Implemented
  def interpolateColor(temperature: Temperature): Color = {
    var upper: (Double, Color) = (Double.MaxValue, Color(0, 0, 0))
    var lower: (Double, Color) = (Double.MinValue, Color(0, 0, 0))
    for ( elem <- scale ) {
      val elemTemp = elem._1
      if ( elemTemp >= temperature && elemTemp < upper._1 ) {
        upper = elem
      }
      if ( elemTemp <= temperature && elemTemp > lower._1 ) {
        lower = elem
      }
    }
    if ( upper._1 == lower._1 ) {
      upper._2
    } else {
      val uRatio = (temperature - lower._1)/(upper._1 - lower._1)
      val r = ((upper._2.red - lower._2.red) * uRatio + lower._2.red).round.toInt
      val g = ((upper._2.green - lower._2.green) * uRatio + lower._2.green).round.toInt
      val b = ((upper._2.blue - lower._2.blue) * uRatio + lower._2.blue).round.toInt
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
    val idxArray: Array[Int] = new Array[Int](height*width)
    for (idx <- 0 until idxArray.size) {
      idxArray(idx) = idx
    }
    val img: BufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    idxArray
      .map( (idx: Int) => idxToMapLocation(idx) )
      .map( (ml: MapLocation) => {
        val inSample: TemperaturesData = sample.filter { case (ml2: MapLocation, t: Temperature) => ml == ml2 }
        if (inSample.size > 0) {
          (ml, inSample.head._2)
        } else {
          (ml, interpolateTemperature(ml))
        }
      })
      .map{ case (ml: MapLocation, t: Temperature)  => (ml, interpolateColor(t))}
      .foreach{ case (ml: MapLocation, c: Color) => {
        img.setRGB(ml.j, ml.i, c.rgbToInt)
      }}
    img
  }
}
