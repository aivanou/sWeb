package org.sweb.protocol

import java.io.{BufferedReader, DataInputStream}
import java.nio.{CharBuffer, ByteBuffer}

import com.sun.deploy.net.HttpResponse
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream
import org.sweb.protocol.http.HttpRequest
import org.sweb.util.BufferUtil

abstract class Request(_protocol: String) {
  val protocol = _protocol
}

trait Response {
  def output(): ByteBuffer
}

class DummyRequest(_buffer: ByteBuffer) extends Request {
  val buffer = _buffer
}

class DummyResponse extends Response {
  override def output(): ByteBuffer = {
    return ByteBuffer.wrap("dummy response".getBytes("UTF-8"))
  }
}

object Protocol {

  def transform(buffer: ByteBuffer): Request = {
    val reader = BufferUtil.toBufferedReader(buffer)
    val line = reader.readLine()
    return parseProtocol(line, reader)
  }

  def parseProtocol(line: String, reader: BufferedReader): Request = {
    val (method, path, protocol) = line.split(" +") match {
      case Array(m, p, prot) => (m, p, prot)
      case _ => (null, null, null)
    }
    if (protocol.toLowerCase.startsWith("http")) {
      return HttpRequest(reader, path, method)
    }
    else return new DummyRequest(ByteBuffer.allocate(0))
  }

  def handle(request: Request): Response = {
    request.protocol match {
      case "http" => return processHttpRequest(request.asInstanceOf[HttpRequest]).asInstanceOf[Response]
    }
  }

  def processHttpRequest(request: HttpRequest): HttpResponse = {
    request.method match {
      case "get" => {}
      case "post" => {}
    }
    return null
  }

}
