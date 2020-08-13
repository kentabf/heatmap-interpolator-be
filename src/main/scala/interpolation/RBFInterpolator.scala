package interpolation

import breeze.linalg.{DenseMatrix, DenseVector}

class RBFInterpolator(width: Int, height: Int, sample: TemperaturesData, scale: ColorScale, rbf: Double => Double)
  extends InterpolatorInterface(width, height, sample, scale) {

  override val name: String = "RBF"

  // for convenience
  val sampleML: Array[MapLocation] = sample.map(_._1).toArray

  // symmetric matrix A
  val rbfMatData: Array[Array[Double]] = sampleML
    .map( ml1 => sampleML
      .map( ml2 => ml1.calcDistance(ml2) )
      .map( rbf(_) )
    )
  val rbfMat: DenseMatrix[Double] = DenseMatrix(rbfMatData:_*)
  // vector b, the sample temperatures
  val rbfVecData: Array[Temperature] = sample.map(_._2).toArray
  val rbfVec: DenseVector[Temperature] = new DenseVector(rbfVecData)
  // solves Ax=b, where x is the vector of weights
  val x: DenseVector[Double] = rbfMat \ rbfVec

  def interpolateTemperature(mlToInterpolate: MapLocation): Temperature = {
    val vecData: Array[Double] = sampleML.map(mlToInterpolate.calcDistance(_))
      .map(rbf(_))
    val vec: DenseVector[Double] = new DenseVector(vecData)
    vec dot x
  }
}
