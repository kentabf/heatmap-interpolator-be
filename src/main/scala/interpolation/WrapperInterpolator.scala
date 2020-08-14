package interpolation

import scala.math.pow

class WrapperInterpolator(width: Int, height: Int, sample: TemperaturesData, scale: ColorScale, power: Double)
  extends InterpolatorInterface(width, height, sample, scale) {

  override val name: String = "Wrapper"

  def mapToEarth(ml: MapLocation): EarthLocation = {
    val lat: Double = 90.0 - (ml.i/(height*1.0))*180.0
    val lon: Double = -180.0 + (ml.j/(width*1.0))*360.0
    EarthLocation(lat, lon)
  }
  def earthToMap(el: EarthLocation): MapLocation = {
    val i = ((90 - el.lat)*(height/180)).toInt
    val j = ((el.lon + 180)*(width/360)).toInt
    MapLocation(i, j)
  }

  def interpolateTemperature(mlToInterpolate: MapLocation): Temperature = {
    def getWeighted(dist: Double): Double = {
      1.0 / pow(dist, power)
    }

    val elToInterpolate: EarthLocation = mapToEarth(mlToInterpolate)
    val distances: Iterable[(Double, Temperature)] = sample
      .map((point: Point) => (mapToEarth(point.location), point.temperature))
      .map { case (el: EarthLocation, t: Temperature) => (elToInterpolate.distanceWith(el), t) }
    val lessThan1: Iterable[(Double, Temperature)] = distances.filter(_._1 < 1)
    if (!lessThan1.isEmpty) {
      lessThan1.minBy(_._1)._2
    } else {
      val numerAndDenum: (Double, Double) = distances
        .map { case (dist: Double, t: Temperature) => (getWeighted(dist), t) }
        .foldLeft((0, 0): (Double, Double))((acc, wat) => (acc._1 + wat._1 * wat._2, acc._2 + wat._1))
      numerAndDenum._1 / numerAndDenum._2
    }
  }
}
