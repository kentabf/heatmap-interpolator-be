package server

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import interpolation._
import spray.json._

object InterpolatorType extends Enumeration {
  type InterpolatorType = Value
  val barnes, idw, nn, rbf, wrapper = Value
}

// domain model
// NOTE: several case classes already defined in `interpolation`
final case class Data(width: Int, height: Int, sample: List[Point])
final case class Body(interpolatorType: Option[String], data: Data, scale: Option[ColorScale])

// collect json format instances into a support trait
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
//  implicit val interpolatorTypeFormat = jsonFormat1(InterpolatorType)
  implicit val colorFormat = jsonFormat3(Color)
  implicit val colorMapFormat = jsonFormat2(ColorMap)
  implicit val mapLocationFormat = jsonFormat2(MapLocation)
  implicit val pointFormat = jsonFormat2(Point)
  implicit val dataFormat = jsonFormat3(Data)
  implicit val bodyFormat = jsonFormat3(Body)
}
