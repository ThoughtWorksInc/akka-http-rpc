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
trait RpcSupport extends autowire.Server[ujson.Value, upickle.default.Reader, upickle.default.Writer] {

  implicit private def upickleJsValueMarshaller = {
    Marshaller.StringMarshaller.wrap[ujson.Value, MessageEntity](MediaTypes.`application/json`)(_.toString)
  }

  implicit private def upickleJsValueUnmarshaller = {
    Unmarshaller.stringUnmarshaller.map(ujson.read(_))
  }

  final def write[Result](r: Result)(implicit writer: upickle.default.Writer[Result]) = {
    upickle.default.writeJs(r)
  }

  final def read[Result](p: ujson.Value)(implicit reader: upickle.default.Reader[Result]) = {
    upickle.default.read(p)
  }

  final def rpc(packagePrefix: String*)(routes: PartialFunction[Request, Future[ujson.Value]]) = {
    path(Segments) { segments =>
      entity(as[ujson.Value]) {
        case ujson.Obj(keyValuePairs) =>
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
