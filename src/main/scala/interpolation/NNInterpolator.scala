package interpolation

class NNInterpolator(width: Int, height: Int, sample: TemperaturesData, scale: ColorScale)
  extends InterpolatorInterface(width, height, sample, scale) {

  override val name: String = "NN"

  def interpolateTemperature(mlToInterpolate: MapLocation): Temperature = {
    sample.map( (point: Point) => (point.location.calcDistance(mlToInterpolate), point.temperature))
      .minBy(_._1)
      ._2
  }
}