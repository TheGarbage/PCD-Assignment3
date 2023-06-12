package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import resources.Ranking
object RankingActor{
  def apply(ranking: Ranking): Behavior[String] = Behaviors.receive{ (_, msg) =>
      val (updatedRanking, bool) = ranking.put(msg)
      RankingActor(updatedRanking)
  }
}