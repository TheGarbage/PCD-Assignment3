package actors

import akka.actor.typed.{ActorRef, Behavior, Terminated}
import akka.actor.typed.scaladsl.Behaviors
import resources.{Common, Ranking, View}

import java.io.File

object StartActor {
  def apply(startTime: Long, ranking: Ranking, view: View, directory: File, rankingActor: ActorRef[String], countersActor: ActorRef[Int]): Behavior[String] = Behaviors.setup { ctx =>
    val child = ctx.spawn(DirectoryActor(directory, rankingActor, countersActor), "Directory")
    ctx.watch(child)
    Behaviors.receiveSignal({ case (_, Terminated(_)) =>
      view.setFinish(if(ranking.isEmpty)
        "No java files in the directory"
      else
        "Time to finish: " + (System.currentTimeMillis() - startTime) + "ms"
      )
      Behaviors.stopped
    })
  }
}