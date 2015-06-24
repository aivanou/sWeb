import java.net.InetSocketAddress
import java.nio._
import java.nio.channels._
import java.util.concurrent.{ExecutorService, Executors}

import org.sweb.actor.SwebActorSystem
import org.sweb.server.IOLoop

import scala.concurrent.{ExecutionContext, Promise}


object Main {

  def main(args: Array[String]): Unit = {
    val swebSystem = new SwebActorSystem()
    val ioLoop = new IOLoop("localhost", 10000, swebSystem)
    swebSystem.start()
    ioLoop.start()
  }

}
