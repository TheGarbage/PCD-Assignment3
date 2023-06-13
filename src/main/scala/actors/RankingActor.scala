package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import resources.{Common, Ranking, View}
object RankingActor{
  def apply(ranking: Ranking, view: View, time: Long = System.currentTimeMillis()): Behavior[Option[String]] = Behaviors.receive{ (_, msg) => msg match {
    case None => Behaviors.same
    case Some(file) => val (updatedRanking, bool) = ranking.put(file)
      if (bool && System.currentTimeMillis() - time > Common.MILLISECOND_UPDATE) {
        view.setRankingText(updatedRanking.makeRankingText())
        RankingActor(updatedRanking, view)
      } else {
        RankingActor(updatedRanking, view, time)
      }
  }
  }
}