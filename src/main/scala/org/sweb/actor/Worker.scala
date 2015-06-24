package org.sweb.actor

import akka.actor.Status.Success
import akka.actor._
import akka.actor.Actor._
import akka.routing._

import org.sweb.protocol.{Request, Response}
import scala.concurrent.Future

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

class Manager(nworkers: Int) extends Actor {

  var router = context.actorOf(SmallestMailboxPool(nworkers).props(Worker.props()), "router")

  override def receive: Actor.Receive = {
    case Initial() => {
      println("Manager: Initial")
      router ! Broadcast(Initial())
      context.become(work)
    }
  }

  def work: Actor.Receive = {
    case reqMessage: RequestMessage => {
      println("Manager: readMessage")
      router ! reqMessage
    }
    case resp: RequestFinished => {
      println("Manager: RequestFinished")
      context.parent ! resp
    }
  }

}

object Manager {
  def props(nworkers: Int): Props = Props(new Manager(nworkers))
}

class Worker() extends Actor {

  override def receive: Receive = {
    case Initial() => {
      println("Worker: Initial")
      context.become(work)
    }
  }

  def work: Actor.Receive = {
    case RequestMessage(transform, func, write, buffer) => {
      println("Worker: RequestMessage")
      val request = transform(buffer)
      println("Worker: transform finished")
      func(request) onSuccess {
        case resp: Response => {
          sender() ! RequestFinished(resp, write)
          println("Worker: sent back request finished message")
        }
      }
    }
  }

}


object Worker {
  def props(): Props = Props(new Worker())
}


object Main {

  def main(args: Array[String]): Unit = {
  }

}