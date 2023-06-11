import akka.actor.typed.{ActorRef, ActorSystem, Behavior, Terminated}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}

import java.io.File
import scala.io.Source

object FileActor {
  def apply(): Behavior[String] = Behaviors.receive { (ctx, msg) =>
    ctx.log.info("Started!")
    val source = Source.fromFile(msg)
    try {
      println(source.getLines().size + " - " + msg)
    } finally {
      source.close()
    }
    Behaviors.stopped
  }
}

// "Actor" module definition
object DirectoryActor{
  // "API", i.e. message that actors should received / send
  def scanDirectory(directory: File, ctx: ActorContext[String]): Int = {
    var count = 0
    if (directory.exists() && directory.isDirectory) {
      directory.listFiles().foreach {
        case file: File if file.isFile && file.getName.endsWith(".java")=>
          val child = ctx.spawn(FileActor(), file.getAbsolutePath.replaceAll("[^a-zA-Z0-9]", ""))
          child ! file.getAbsolutePath
          ctx.watch(child)
          count += 1
        case subDirectory: File if subDirectory.isDirectory => count += scanDirectory(subDirectory, ctx)
        case _ =>
      }
    }
    count
  }

  // Behaviour factory, i.e how the actor react to messages
  def apply(count: Int): Behavior[String] = Behaviors.receive[String]{ (ctx, msg) =>
      ctx.log.info("Started {}!", msg)
      DirectoryActor(scanDirectory(new File(msg), ctx))
    }.receiveSignal { case (_, Terminated(_)) =>
      count match {
        case 1 => Behaviors.stopped
        case c =>
          DirectoryActor(c - 1)
      }
    }
}

object HelloWorldAkkaTyped extends App {
  val system: ActorSystem[String] = ActorSystem(DirectoryActor(0), name = "Main")
  system ! "C:\\Users\\gugli\\Downloads\\TestFolder"
}

