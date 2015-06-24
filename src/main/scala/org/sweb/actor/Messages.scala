package org.sweb.actor

import java.nio.ByteBuffer

import org.sweb.protocol.{Response, Request}

import scala.concurrent.Future


case class RequestMessage(transform: ByteBuffer => Request, process: Request => Future[Response], write: Response => ByteBuffer, buffer: ByteBuffer)

case class RequestFinished(response: Response, write: Response => ByteBuffer)

case class Initial()

case class Start(host: String, port: Int)

case class ProcessRequest(read: () => ByteBuffer,
                          transform: ByteBuffer => Request,
                          process: Request => Future[Response],
                          write: Response => ByteBuffer)
