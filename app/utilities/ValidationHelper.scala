package utilities

import play.api.data.validation.{Valid, ValidationError, Invalid, Constraint}
import services.dao._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import play.Logger

import play.api.i18n.MessagesApi
import play.api.data._

object ValidationHelper {
  
  private val emailRegex = """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r
  private val bothalphabetnumberRegex = """^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])\w{3,}$""".r
  
  def emailAddress: Constraint[String] = Constraint[String]("constraint.custom.email") { e =>
    if ((e == null) || (e.trim.isEmpty)) Valid // use nonEmpty or custom nonEmpty constraints
    else emailRegex.findFirstMatchIn(e)
      .map(_ => Valid)
      .getOrElse(Invalid(ValidationError("error.email")))
  }
  
  def isExistsLoginId(loginId: String, accountsDao: AccountsDAO)(implicit ctx: ExecutionContext):Future[Boolean] = {
    if ((loginId == null) || (loginId.trim.isEmpty)) {
       Future(true)
    }else {
      accountsDao.findByLoginId(loginId).flatMap(record =>
        record match {
          case Some(account) => Future(false)
          case None =>  Future(true)
        }
      )
    }
  }
  def isBothAlphabetNumeric: Constraint[String] = Constraint[String]("constraint.custom.password") { e =>
   //Logger.debug(e)
   //Logger.debug(bothalphabetnumberRegex.findFirstMatchIn(e).toString())
   if ((e == null) || (e.trim.isEmpty)) Valid // use nonEmpty or custom nonEmpty constraints
    else bothalphabetnumberRegex.findFirstMatchIn(e)
      .map(_ => Valid)
      .getOrElse(Invalid(ValidationError("error.bothalphabetnumber")))
      
  }
//  def formError2String(formError: FormError, messagesApi: MessagesApi):String = {
//    formError.key match {
//      case null | "" => messagesApi(formError.message, formError.args:_*)
//      case key => "%s : %s".format(key, messagesApi(formError.message, formError.args:_*))
//    }
//  }
  def formErrorToString(formError: FormError, messagesApi: MessagesApi):String = {
    formError.key match {
      case null | "" => messagesApi(formError.message, formError.args:_*)
      case key => "%s : %s".format(key, messagesApi(formError.message, key))
    }
  }
  def formErrorToMap(formError: FormError, messagesApi: MessagesApi):Map[String, String] = {
    formError.key match {
      case null | "" => Map("" -> messagesApi(formError.message, formError.args:_*))
      case key => Map(key -> messagesApi(formError.message, key))
    }
  }
}