package resources

case class Counters(countersVector: Vector[Int], maxl: Int, ni: Int, count: Int) {
  def increment(lines: Int): Counters = {
    val updatedRankingList =
      if (lines < maxl)
        countersVector.updated(lines / (maxl / (ni - 1)), countersVector(lines / (maxl / (ni - 1))) + 1)
      else
        countersVector.updated(ni - 1, countersVector(ni - 1) + 1)
    Counters(updatedRankingList, maxl, ni, count + 1)
  }

  def read(): Vector[Int] = countersVector
}

object Counters {
  def apply(maxl: Int, ni: Int): Counters = {
    val rankingList = Vector.fill(ni)(0)
    Counters(rankingList, maxl, ni, 0)
  }
}