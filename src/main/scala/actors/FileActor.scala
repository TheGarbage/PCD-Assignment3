package actors

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import resources.Common

import java.io.File
import scala.io.Source

object FileActor {
  def apply(file: File, rankingActor: ActorRef[Option[String]], countersActor: ActorRef[Option[Int]]): Behavior[File] = Behaviors.setup { _ =>
    val source = Source.fromFile(file)
    val lines = source.getLines().foldLeft(0)((count, _) => count + 1)
    countersActor ! Some(lines)
    val prefix = Common.STRING_PREFIX.take(Common.MAX_DIGITS - lines.toString.length)
    rankingActor ! Some(prefix + lines + lines.toString.length + file.getName)
    source.close()
    Behaviors.stopped
  }
}