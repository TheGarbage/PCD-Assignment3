package resources

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.ActorContext

import java.io.File
import scala.util.Sorting

object Common {
  val STRING_PREFIX = "0000000000"
  val MAX_DIGITS: Int = STRING_PREFIX.length

  def makeChild(name: String, behavior: Behavior[File], ctx: ActorContext[File]): Int = {
    val child = ctx.spawn(behavior, name)
    ctx.watch(child)
    1
  }

  def makeCountersText(counters: Counters, maxl: Int, ni: Int): String = {
    val counterValues = counters.read()
    val textArray =
      if (ni < maxl) {
        (0 until ni - 1).map { i =>
          s"- Interval [ ${maxl / (ni - 1) * i} - ${maxl / (ni - 1) * (i + 1) - 1} ]:    ${counterValues(i)}\n"
        }
      } else {
        (0 until ni).map { i =>
          val counter = if (i == maxl) s"more than $maxl" else i.toString
          s" - $counter row(s): =   ${counterValues(i)}\n"
        }
      }
      textArray.mkString("")
    }

  def makeRankingText(ranking: Ranking, n: Int): String = {
    val list = ranking.read()
    if (list.length < n && list.length != 1)
      Sorting.quickSort(list.toArray)
    val textArray = list.reverse.zipWithIndex.map { case (item, i) =>
      s"${n - i}) ${item.substring(58 - item.charAt(MAX_DIGITS).toInt, MAX_DIGITS)} - ${item.split("\\\\").last}\n"
    }
    textArray.mkString
  }

}