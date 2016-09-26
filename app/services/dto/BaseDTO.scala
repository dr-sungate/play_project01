package services.dao

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.libs.json._
import models.JsonResponse._

trait BaseDTO[A,B] {

  def responseSuccess(dbdata: A): Future[B]

  def responseError(errormessage: String): ErrorResponse
  
}