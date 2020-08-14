package interpolation

import scala.math.pow

class IDWInterpolator(width: Int, height: Int, sample: TemperaturesData, scale: ColorScale, power: Double)
  extends InterpolatorInterface(width, height, sample, scale) {

  override val name: String = "IDW"

  def interpolateTemperature(mlToInterpolate: MapLocation): Temperature = {
    def getWeighted(dist: Double): Double = {
      1.0/pow(dist, power)
    }
    val numerAndDenum: (Double, Double) = sample
      .map( (point: Point) => (point.location.calcDistance(mlToInterpolate), point.temperature))
      .map{ case (dist: Double, t: Temperature) => (getWeighted(dist), t) }
      .foldLeft((0, 0): (Double, Double))( (acc, wat) => (acc._1 + wat._1*wat._2, acc._2 + wat._1) )
    // 'wat' stands for 'weight and temperature'
    numerAndDenum._1/numerAndDenum._2
  }
}
