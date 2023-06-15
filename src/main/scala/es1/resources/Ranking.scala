package es1.resources

case class Ranking(rankingList: List[String], maxSize: Int) {
  def put(item: String): (Ranking, Boolean) = if (rankingList.isEmpty) {
    (Ranking(item :: rankingList, maxSize), true)
  } else {
    val (before, after) = rankingList.span(_ >= item)
    val toAdd = after.nonEmpty
    if (toAdd) {
      (Ranking( if (rankingList.size == maxSize) {
        before ::: (item :: after.tail)
      } else {
        before ::: (item :: after)
      }, maxSize), toAdd)
    } else
      (this, toAdd)
  }

  def isEmpty: Boolean = rankingList.isEmpty

  def makeRankingText(): String = {
    val textArray = rankingList.zipWithIndex.map { case (item, i) =>
      s"${i + 1}) ${item.substring(58 - item.charAt(Common.MAX_DIGITS).toInt, Common.MAX_DIGITS)} - ${item.split("\\\\").last}\n"
    }
    textArray.mkString
  }

}

object Ranking {
  def apply(maxSize: Int): Ranking = Ranking(Nil, maxSize)
}
