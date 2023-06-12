package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import resources.Counters
object CountersActor{
  def apply(counters: Counters): Behavior[Int] = Behaviors.receive{ (_, lines) =>
      CountersActor(counters.increment(lines))
    }
}