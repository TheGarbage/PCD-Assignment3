import actors.BootActor
import akka.actor.typed.ActorSystem

import java.io.File

object Main extends App {
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