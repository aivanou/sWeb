package org.sweb.server

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels._

import org.sweb.actor.{ProcessRequest, SwebActorSystem}
import org.sweb.protocol._
import org.sweb.util.BufferUtil._

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

class IOLoop(host: String, port: Int, swebSystem: SwebActorSystem) {


  def start(): Unit = {
    val selector = initSelector(host, port)

    while (true) {
      selector.select(10L)
      val set = selector.selectedKeys()
      val it = set.iterator()
      while (it.hasNext) {
        val key = it.next()
        it.remove()
        key match {
          case key if key.isAcceptable => {
            val server: SelectableChannel = key.channel()
            val clientChannel = server.asInstanceOf[ServerSocketChannel].accept()
            clientChannel.configureBlocking(false)
            clientChannel.register(selector, SelectionKey.OP_READ)
          }
          case key if key.isReadable => {
            //            println("IOLoop: key is readable")
            val clientChannel = key.channel().asInstanceOf[SocketChannel]

            val readFunction = () => {
              val buffer = ByteBuffer.allocate(1024)
              clientChannel.read(buffer)
              buffer.flip()
              buffer

            }
            val transformFunction = (buffer: ByteBuffer) => {
              Protocol.transform(buffer)
            }
            val processFunction = (req: Request) => {
              val r = req.asInstanceOf[DummyRequest]
              println("readFunction: ")
              printReader(toBufferedReader(r.buffer))
              println()
              r.buffer.flip()
              Future {
                new DummyResponse()
              }
            }
            val writeFunction = (resp: Response) => {
              val buffer = resp.output()
              clientChannel.register(selector, SelectionKey.OP_WRITE)
              key.attach(buffer)
              buffer.flip()
              buffer
            }
            key.interestOps(key.interestOps() & (~SelectionKey.OP_READ))
            //            clientChannel.register(selector, SelectionKey.OP_CONNECT)
            swebSystem sendMessage ProcessRequest(readFunction, transformFunction, processFunction, writeFunction)
          }
          case key if key.isWritable => {
            val clientChannel = key.channel().asInstanceOf[SocketChannel]
            val clientMessage = key.attachment().asInstanceOf[ByteBuffer]
            clientChannel.write(clientMessage)
            //            clientChannel.register(selector, SelectionKey.OP_READ)
            //clientChannel.close()
          }
        }
      }
    }
  }

  def initSelector(host: String, port: Int): Selector = {
    val serverChannel = ServerSocketChannel.open()
    val selector = Selector.open()
    serverChannel.socket().bind(new InetSocketAddress(10000))
    serverChannel.configureBlocking(false)
    serverChannel.register(selector, SelectionKey.OP_ACCEPT)
    return selector
  }

}
