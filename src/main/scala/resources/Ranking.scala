package resources

import scala.util.Sorting

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

  def makeRankingText(): String = {
    if (rankingList.length < maxSize && rankingList.length != 1)
      Sorting.quickSort(rankingList.toArray)
    val textArray = rankingList.reverse.zipWithIndex.map { case (item, i) =>
      s"${maxSize - i}) ${item.substring(58 - item.charAt(Common.MAX_DIGITS).toInt, Common.MAX_DIGITS)} - ${item.split("\\\\").last}\n"
    }
    textArray.mkString
  }

}

object Ranking {
  def apply(maxSize: Int): Ranking = Ranking(Nil, maxSize)
}
