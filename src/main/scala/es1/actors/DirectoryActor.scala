package es1.actors

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}

import java.io.File
import scala.annotation.tailrec

object DirectoryActor {
  def makeChild(behavior: Behavior[File], ctx: ActorContext[File]): Int = {
    val child = ctx.spawnAnonymous(behavior)
    ctx.watch(child)
    1
  }

  def scanDirectory(directory: File, ctx: ActorContext[File], rankingActor: ActorRef[Option[String]], countersActor: ActorRef[Option[Int]]): Int = {
    @tailrec
    def scanFiles(files: List[File], count: Int = 0): Int = files match {
      case Nil => count
      case head :: tail =>
        head match {
          case file: File if file.isFile && file.getName.endsWith(".java") =>
            scanFiles(tail, count + makeChild(FileActor(file, rankingActor, countersActor), ctx))
          case subDirectory: File if subDirectory.isDirectory && subDirectory.listFiles().nonEmpty =>
            scanFiles(tail, count + makeChild(DirectoryActor(subDirectory, rankingActor, countersActor), ctx))
          case _ =>
            scanFiles(tail, count)
        }
    }
    scanFiles(directory.listFiles().toList)
  }

  def apply(directory: File, rankingActor: ActorRef[Option[String]], countersActor: ActorRef[Option[Int]]): Behavior[File] = Behaviors.setup[File] { ctx =>
    WaiterActor(scanDirectory(directory, ctx, rankingActor, countersActor))
  }
}