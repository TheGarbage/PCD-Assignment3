package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import resources.{Counters, View}
object CountersActor{

  def apply(counters: Counters, view: View): Behavior[Int] = Behaviors.receive{ (_, lines) =>
      val newCounters = counters.increment(lines)
      view.setIntervalsText(newCounters.makeCountersText())
      CountersActor(newCounters, view)
    }
}