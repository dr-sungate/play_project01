package forms

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration

import javax.inject.Inject
import models.TablesExtend._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n.I18nSupport
import play.api.i18n.Lang
import play.api.i18n.Messages
import play.api.i18n.MessagesApi
import services.dao._
import utilities._
import utilities.valid._


class AgenciesSearchForm @Inject()(val messagesApi: MessagesApi)(implicit ctx: ExecutionContext) extends I18nSupport{
    val form = Form(
    mapping(
      "applicationId" -> optional(longNumber),
      "agencyName" -> optional(text.verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
      "type" -> optional(text.verifying(Messages("error.required", "種別"), {!_.isEmpty}).verifying(Messages("error.maxLength", 50),{_.length <= 50 })),
      "status" -> optional(text.verifying(Messages("error.required", "ステータス"), {!_.isEmpty}).verifying(Messages("error.maxLength", 50),{_.length <= 50 })),
      "timezoneId" -> optional(number)
      )
      (agenciessearchapply)(agenciessearchunapply)
  )
  private def agenciessearchapply(
      applicationId: Option[Long], 
      agencyName: Option[String], 
      `type`:  Option[String],
      status:  Option[String],
      timezoneId: Option[Int]
       ) = new AgenciesSearchRow(applicationId, agencyName, `type`, status, timezoneId)
  private def agenciessearchunapply(n: AgenciesSearchRow) = Some(
      n.applicationId, n.agencyName, n.`type`, n.status, n.timezoneId
      )
}