package es1.resources

case class Counters(countersVector: Vector[Int], maxl: Int, ni: Int, count: Int) {
  def increment(lines: Int): Counters = {
    val updatedRankingList =
      if (lines < maxl)
        countersVector.updated(lines / (maxl / (ni - 1)), countersVector(lines / (maxl / (ni - 1))) + 1)
      else
        countersVector.updated(ni - 1, countersVector(ni - 1) + 1)
    Counters(updatedRankingList, maxl, ni, count + 1)
  }

  def makeCountersText(): String = {
    val textArray =
      if (ni < maxl) {
        (0 until ni - 1).map { i =>
          s"- Interval [ ${maxl / (ni - 1) * i} - ${maxl / (ni - 1) * (i + 1) - 1} ]:    ${countersVector(i)}\n"
        }
      } else {
        (0 until ni).map { i =>
          val counter = if (i == maxl) s"more than $maxl" else i.toString
          s" - $counter row(s): =   ${countersVector(i)}\n"
        }
      }
    textArray.mkString("")
  }
}

object Counters {
  def apply(maxl: Int, ni: Int): Counters =
    Counters(Vector.fill(ni)(0), maxl, ni, 0)
}