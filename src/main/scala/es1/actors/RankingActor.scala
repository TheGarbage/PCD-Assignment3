package es1.actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import es1.resources.{Common, Counters, Ranking, View}
object RankingActor{
  def apply(ranking: Ranking, renderActor: ActorRef[(Option[Ranking],Option[Counters], Option[String])], time: Long = System.currentTimeMillis()): Behavior[Option[String]] = Behaviors.receive{ (_, msg) => msg match {
    case None =>
      renderActor ! (Some(ranking), None, None)
      Behaviors.stopped
    case Some(file) => val (updatedRanking, bool) = ranking.put(file)
      if (bool && System.currentTimeMillis() - time > Common.MILLISECOND_UPDATE) {
        renderActor ! (Some(updatedRanking), None, None)
        RankingActor(updatedRanking, renderActor)
      } else {
        RankingActor(updatedRanking, renderActor, time)
      }
  }
  }
}