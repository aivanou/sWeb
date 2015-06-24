package org.sweb.protocol.http

import java.io.{BufferedReader, Reader}
import java.nio.ByteBuffer
import java.nio.charset.Charset

import com.sun.corba.se.impl.ior
import org.sweb.protocol.Request

abstract class HttpRequest(_method: String, _path: String, _headers: Map[String, String]) extends Request("http") {
  val method = _method
  val path = _path
  val headers = _headers
}

class GetRequest(method: String, headers: Map[String, String], path: String) extends HttpRequest(method, path, headers) {}

class PostRequest(method: String, headers: Map[String, String], body: ByteBuffer, path: String, charset: Charset) extends HttpRequest(method, path, headers) {}

class UnknownRequest(method: String) extends HttpRequest(method, null, null) {}

object HttpRequest {

  def apply(reader: BufferedReader, path: String, method: String): HttpRequest = {
    reader.readLine()
    val headers = parseHeaders(reader)
    method.toLowerCase() match {
      case "get" => return GetRequest(method, path, headers)
      case "post" => return PostRequest(method, path, headers, reader)
      case value => return new UnknownRequest(value)
    }
  }

  def parseHeaders(reader: BufferedReader): Map[String, String] = {
    reader.readLine() match {
      case null => return Map[String, String]()
      case line => {
        val (k, v) = parseHeader(line)
        return parseHeaders(reader) + (k -> v)
      }
    }
  }

  def parseHeader(line: String): (String, String) = {
    line.trim().indexOf(':') match {
      case -1 => return (line, "")
      case ind => return (line.substring(0, ind), line.substring(ind))
    }
  }


}

object GetRequest {
  def apply(method: String, path: String, headers: Map[String, String]): GetRequest = {
    return new GetRequest(method, headers, path)
  }
}

object PostRequest {
  def apply(method: String, path: String, headers: Map[String, String], reader: BufferedReader): PostRequest = {
    return new PostRequest(method, headers, parseBody(reader), path, Charset.defaultCharset())
  }

  def parseBody(reader: BufferedReader): ByteBuffer = {
    return ByteBuffer.allocate(1)
  }
}

object UnknownRequest {
  def apply(medhod: String): Unit = {
    return new UnknownRequest(medhod)
  }
}

