package object interpolation {
  type Temperature = Double
  type TemperaturesData = Iterable[(MapLocation, Temperature)]
  type ColorScale = Iterable[(Temperature, Color)]
}
