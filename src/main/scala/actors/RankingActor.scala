package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import resources.{Ranking, View}
object RankingActor{
  def apply(ranking: Ranking, view: View): Behavior[String] = Behaviors.receive{ (_, msg) =>
    val (updatedRanking, bool) = ranking.put(msg)
    if(bool)
      view.setRankingText(updatedRanking.makeRankingText())
    RankingActor(updatedRanking, view)
  }
}