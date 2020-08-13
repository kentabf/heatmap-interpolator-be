package interpolation

class NNInterpolator(width: Int, height: Int, sample: TemperaturesData, scale: ColorScale)
  extends InterpolatorInterface(width, height, sample, scale) {

  override val name: String = "NN"

  def interpolateTemperature(mlToInterpolate: MapLocation): Temperature = {
    sample.map{ case (ml: MapLocation, t: Temperature) => (ml.calcDistance(mlToInterpolate), t) }
      .minBy(_._1)
      ._2
  }
}