package es1.actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import es1.resources.{Counters, Ranking, View}

object RenderActor{
  def apply(view: View) : Behavior[Ranking | Counters | String] = Behaviors.receive { (_, msg) => msg match {
    case msg: Ranking => view.setRankingText(msg.makeRankingText())
    case msg: Counters => view.setIntervalsText(msg.makeCountersText())
    case msg: String => view.setFinish(msg)
  }
    Behaviors.same
  }
}