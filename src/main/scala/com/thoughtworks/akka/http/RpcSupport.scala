package com.thoughtworks.akka.http

import akka.http.scaladsl.model.{MediaTypes, _}
import akka.http.scaladsl.server.Directives._
import com.thoughtworks.Extractor._

import scala.concurrent.Future
import akka.http.scaladsl.marshalling.Marshaller
import akka.http.scaladsl.unmarshalling.Unmarshaller

/**
  * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
trait RpcSupport extends autowire.Server[upickle.Js.Value, upickle.default.Reader, upickle.default.Writer] {

  implicit private def upickleJsValueMarshaller = {
    Marshaller.StringMarshaller.wrap[upickle.Js.Value, MessageEntity](MediaTypes.`application/json`)(_.toString)
  }

  implicit private def upickleJsValueUnmarshaller = {
    Unmarshaller.stringUnmarshaller.map(upickle.json.read)
  }

  final def write[Result](r: Result)(implicit writer: upickle.default.Writer[Result]) = {
    writer.write(r)
  }

  final def read[Result](p: upickle.Js.Value)(implicit reader: upickle.default.Reader[Result]) = {
    reader.read(p)
  }

  final def rpc(packagePrefix: String*)(routes: PartialFunction[Request, Future[upickle.Js.Value]]) = {
    path(Segments) { segments =>
      entity(as[upickle.Js.Value]) {
        case upickle.Js.Obj(keyValuePairs@_*) =>
          autowire.Core.Request(packagePrefix ++ segments, keyValuePairs.toMap) match {
            case routes.extract(response) =>
              complete {
                response
              }
            case _ =>
              reject
          }
        case _ =>
          complete(HttpResponse(StatusCodes.BadRequest))
      }
    }
  }

}

object RpcSupport extends RpcSupport
