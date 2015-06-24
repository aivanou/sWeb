package org.sweb.util

import java.io.{InputStreamReader, BufferedReader, DataInputStream, DataInput}
import java.nio.ByteBuffer

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream
import sun.nio.ByteBuffered

import scala.util.{Failure, Try}


object BufferUtil {

  def   toBufferedReader(buffer: ByteBuffer): BufferedReader = {
    val bout = new ByteOutputStream()
    while (buffer.hasRemaining)
      bout.write(buffer.get())
    return new BufferedReader(new InputStreamReader(bout.newInputStream()))
  }

  def printReader(reader: BufferedReader): Unit = {
    val line = reader.readLine()
    if (line == null)
      return
    println(line)
    printReader(reader)
  }


  //  def readLine(buffer: ByteBuffer): Option[String] = {
  //    val sb = new StringBuilder()
  //    if (!buffer.hasRemaining)
  //      return None
  //    while (buffer.hasRemaining) {
  //      val bt = buffer.get().toChar
  //      if (bt.equals('\n')) {
  //        return Some(sb.toString)
  //      }
  //      else if (!bt.equals('\r')) {
  //        sb.append(bt.toChar)
  //      }
  //    }
  //    return Some(sb.toString)
  //  }

}
