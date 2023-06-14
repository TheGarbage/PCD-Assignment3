package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import resources.{Counters, Ranking, View}

object RenderActor{
  def apply(view: View) : Behavior[(Option[Ranking],Option[Counters], Option[String])] = Behaviors.receive { (_, msg) => msg match {
    case (Some(ranking), _, _) => view.setRankingText(ranking.makeRankingText())
    case (_, Some(counters), _ ) => view.setIntervalsText(counters.makeCountersText())
    case (_, _, Some(text)) => view.setFinish(text)
  }
    Behaviors.same
  }
}