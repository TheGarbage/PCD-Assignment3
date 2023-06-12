package resources

case class Ranking(rankingList: List[String], maxSize: Int) {
  def put(item: String): (Ranking, Boolean) = rankingList.size match {
    case n if n == maxSize =>
      val (before, after) = rankingList.span(_ < item)
      val toAdd = after.nonEmpty
      if (toAdd) {
        val updatedRankingList = before ::: (item :: after.tail)
        (Ranking(updatedRankingList, maxSize), toAdd)
      } else
        (this, toAdd)
    case _ =>
      val updatedRankingList = item :: rankingList
      (Ranking(updatedRankingList, maxSize), false)
  }

  def read(): List[String] = rankingList

  def isEmpty: Boolean = rankingList.isEmpty
}

object Ranking {
  def apply(maxSize: Int): Ranking = Ranking(Nil, maxSize)
}
