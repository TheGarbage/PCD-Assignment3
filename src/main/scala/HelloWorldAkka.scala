import akka.actor.typed.{ActorSystem, Behavior, Terminated}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}

import java.io.File
import scala.annotation.tailrec
import scala.io.Source

object FileActor {
  def apply(file: File): Behavior[File] = Behaviors.setup { _ =>
    val source = Source.fromFile(file)
    source.close()
    Behaviors.stopped
  }
}

object Common {
  def makeChild(name: String, behavior: Behavior[File], ctx: ActorContext[File]): Int = {
    val child = ctx.spawn(behavior, name.replaceAll("[^a-zA-Z0-9]", ""))
    ctx.watch(child)
    1
  }
}

object WaiterActor {
  def apply(count: Int = 0): Behavior[File] = Behaviors.receiveSignal[File]{ case (_, Terminated(_)) =>
      count match {
        case 1 => Behaviors.stopped
        case c => WaiterActor(c - 1)
      }
    }
}

object DirectoryActor {
  def scanDirectory(directory: File, ctx: ActorContext[File]): Int = {
    @tailrec
    def scanFiles(files: List[File], count: Int = 0): Int = files match {
      case Nil => count
      case head :: tail =>
        head match {
          case file: File if file.isFile && file.getName.endsWith(".java") =>
            scanFiles(tail, count + Common.makeChild(file.getAbsolutePath, FileActor(file), ctx))
          case subDirectory: File if subDirectory.isDirectory && subDirectory.listFiles().nonEmpty =>
            scanFiles(tail, count + Common.makeChild(subDirectory.getAbsolutePath, DirectoryActor(subDirectory), ctx))
          case _ =>
            scanFiles(tail, count)
        }
    }

    scanFiles(directory.listFiles().toList)
  }

  def apply(directory: File): Behavior[File] = Behaviors.setup[File] { ctx =>
    WaiterActor(scanDirectory(directory, ctx))
  }
}

object BootActor{
  def apply(startTime: Long, directory: File): Behavior[File] = Behaviors.setup{ ctx =>
    ctx.log.info("Started!")
    Common.makeChild("Main", DirectoryActor(directory), ctx)
    Behaviors.receiveSignal({ case (_, Terminated(_)) =>
      ctx.log.info("Time to finish: " + (System.currentTimeMillis() - startTime) + "ms")
      Behaviors.stopped
    })
  }
}

object HelloWorldAkkaTyped extends App {
  val directory: File = new File("C:\\Users\\gugli\\Downloads\\TestFolder")
  val directoryFiles: Array[File] = directory.listFiles()
  val finalMessage: String = if (directoryFiles == null) {
    "Invalid directory selected"
  } else if (directoryFiles.isEmpty) {
    "The selected directory is empty"
  //} else if (wrapperImpl.sizeClassificationListIsEmpty()) {
  //  "No java files in the directory"
  } else {
    val system: ActorSystem[File] = ActorSystem(BootActor(System.currentTimeMillis(), directory), name = "Boot")
    system ! new File("C:\\Users\\gugli\\Downloads\\TestFolder")
    "Processing"
  }
  println(finalMessage)
}