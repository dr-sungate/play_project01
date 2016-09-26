package utilities.api

import play.api.i18n.Messages.Implicits._


sealed trait ResponseCode

object ResponseCode {

  case object Success extends ResponseCode
  case object Error extends ResponseCode
 
  def valueOf(value: String): ResponseCode = value match {
    case SuccessCode => Success
    case ErrorCode    => Error
     case _ => throw new IllegalArgumentException("")
  }
  
  val SuccessCode = "success"
  val ErrorCode = "error"
  
  val codeSeq = Seq(SuccessCode, ErrorCode)

}
