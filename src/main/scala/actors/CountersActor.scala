package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import resources.{Common, Counters, View}

object CountersActor{
  def apply(counters: Counters, view: View, time: Long = System.currentTimeMillis()): Behavior[Option[Int]] = Behaviors.receive{ (_, msg) => msg match {
    case None => Behaviors.same
    case Some(lines) =>
      val newCounters = counters.increment(lines)
      if (System.currentTimeMillis() - time > Common.MILLISECOND_UPDATE) {
        view.setIntervalsText(newCounters.makeCountersText())
        CountersActor(newCounters, view)
      } else {
        CountersActor(newCounters, view, time)
      }
  }
  }
}