package es1.actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import es1.resources.{Common, Counters, Ranking, View}

object CountersActor{
  def apply(counters: Counters, renderActor: ActorRef[(Option[Ranking],Option[Counters], Option[String])], time: Long = System.currentTimeMillis()): Behavior[Option[Int]] = Behaviors.receive{ (_, msg) => msg match {
    case None =>
      renderActor ! (None, Some(counters), None)
      Behaviors.stopped
    case Some(lines) =>
      val newCounters = counters.increment(lines)
      if (System.currentTimeMillis() - time > Common.MILLISECOND_UPDATE) {
        renderActor ! (None, Some(counters), None)
        CountersActor(newCounters, renderActor)
      } else {
        CountersActor(newCounters, renderActor, time)
      }
  }
  }
}