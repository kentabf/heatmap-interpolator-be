package interpolation

import scala.math.{Pi, abs, acos, cos, pow, sin, sqrt}

final case class MapLocation(y: Int, x: Int) {
  def calcDistance(other: MapLocation): Double = {
    sqrt(pow(y - other.y, 2) + pow(x - other.x, 2))
  }
}

final case class Point(location: MapLocation, temperature: Temperature)

final case class Color(red: Int, green: Int, blue: Int) {
  def rgbToInt: Int = {
    red*pow(256, 2).toInt + green*256 + blue
  }
}

final case class Pixel(location: MapLocation, color: Color)

final case class ColorMap(temperature: Temperature, color: Color)

final case class EarthLocation(lat: Double, lon: Double) {
  val r = 6371.0

  // lat lon in radians
  val latR = lat * Pi/180
  val lonR = lon * Pi/180

  def isAntipodWith(other: EarthLocation): Boolean = {
    lat + other.lat == 0 &&
      abs(lon - other.lon) == 180
  }

  def distanceWith(other: EarthLocation) = {
    val delta = {
      if (lat == other.lat && lon == other.lon) {
        0
      } else if (isAntipodWith(other)) {
        Pi
      } else {
        acos(sin(latR) * sin(other.latR)
          + cos(latR) * cos(other.latR) * cos(abs(lonR - other.lonR)))
      }
    }
    delta * r
  }
}

