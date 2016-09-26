package forms

import java.util.Date


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

class ApplicationsForm @Inject()(val messagesApi: MessagesApi)(implicit ctx: ExecutionContext) extends I18nSupport{
    val form = Form(
    mapping(
      "id" -> optional(longNumber),
      "appName" -> text.verifying(Messages("error.required", "APPサービス名"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 255),{_.length <= 255 })
                            .verifying(Messages("error.minLength", 4),{_.length >=4}),
      "url" -> optional(text.verifying(Messages("error.maxLength", 255),{_.length <= 255 })
                            .verifying(Messages("error.minLength", 4),{_.length >=4})),
      "serverIps" -> optional(text.verifying(Messages("error.maxLength", 1000),{_.length <= 1000 })
                            .verifying(Messages("error.minLength", 4),{_.length >=4})),
      "status" -> text.verifying(Messages("error.required", "ステータス"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 50),{_.length <= 50 }),
      "updater" -> optional(number)
 
      )
      (Applicationsapply)(Applicationsunapply)
  )
  private def Applicationsapply(
      id: Option[Long],
      appName:  String,
      url:  Option[String],
      serverIps: Option[String], 
      status: String,
      updater: Option[Int]
      ) = new ApplicationsRow(id.getOrElse(0), appName, url, serverIps, status, Option(RecordValid.IsEnabledRecord), updater, new Date, new Date)
  private def Applicationsunapply(n: ApplicationsRow) = Some(
      (Option(n.id), n.appName, n.url, n.serverIps, n.status, n.updater)
      )

}