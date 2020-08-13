package server

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import interpolation.{MapLocation, Temperature}
import spray.json.DefaultJsonProtocol

// domain model
// MapLocation already defined in package `interpolation`
final case class Point(location: MapLocation, temperature: Temperature)
final case class Data(width: Int, height: Int, sample: List[Point])
final case class Body(interpolator: String, data: Data)

// collect json to support trait
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val mapLocationFormat = jsonFormat2(MapLocation)
  implicit val pointFormat = jsonFormat2(Point)
  implicit val dataFormat = jsonFormat3(Data)
  implicit val bodyFormat = jsonFormat2(Body)
}
