package interpolation

import scala.math.{pow, exp}

class BarnesInterpolator(width: Int, height: Int, sample: TemperaturesData, scale: ColorScale, radius: Double)
  extends InterpolatorInterface(width, height, sample, scale) {

  override val name: String = "Barnes"

  def interpolateTemperature(mlToInterpolate: MapLocation): Temperature = {

    def weight(ml1: MapLocation, ml2: MapLocation, gamma: Double): Double = {
      val dist = ml1.calcDistance(ml2)
      if (dist > radius) {
        0.0
      } else {
        exp(-pow(dist, 2)/(radius*gamma))
      }
    }

    def firstPass(mlToInterpolate: MapLocation): Temperature = {
      val numerAndDenum: (Double, Double) = sample
        .map( (point: Point) => (weight(point.location, mlToInterpolate, 1.0), point.temperature))
        .foldLeft((0,0): (Double, Double))( (acc, wat) => (acc._1 + wat._1*wat._2, acc._2 + wat._1))
      numerAndDenum._1/numerAndDenum._2
    }

    def secondPass(mlToInterpolate: MapLocation): Temperature = {
      val numerAndDenum: (Double, Double) = sample
        .map( (point: Point) => (weight(point.location, mlToInterpolate, 0.3), point.temperature - firstPass(point.location)))
        .foldLeft((0,0): (Double, Double))( (acc, wat) => (acc._1 + wat._1*wat._2, acc._2 + wat._1))
      numerAndDenum._1/numerAndDenum._2
    }

    firstPass(mlToInterpolate) + secondPass(mlToInterpolate)
  }
}
