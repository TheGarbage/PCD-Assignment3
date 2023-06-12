package actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{Behavior, Terminated}

import java.io.File
import resources.{Common, Counters, Ranking}

object BootActor{
  def apply(startTime: Long, directory: File): Behavior[File] = Behaviors.setup{ ctx =>
    ctx.log.info("Started!")
    val rankingActor = ctx.spawn(RankingActor(Ranking(10)), "RankingActor")
    val countersActor = ctx.spawn(CountersActor(Counters(100, 6)), "CounterActor")
    Common.makeChild("Directory", DirectoryActor(directory, rankingActor, countersActor), ctx)
    Behaviors.receiveSignal({ case (_, Terminated(_)) =>
      ctx.log.info("Time to finish: " + (System.currentTimeMillis() - startTime) + "ms")
      Behaviors.stopped
    })
  }
}