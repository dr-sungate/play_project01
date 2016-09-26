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

class ClientsForm @Inject()(val messagesApi: MessagesApi)(implicit ctx: ExecutionContext) extends I18nSupport{
    val form = Form(
    mapping(
      "id" -> optional(longNumber),
      "appClientId" -> text.verifying(Messages("error.required", "アプリ側クライアントID"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 255),{_.length <= 255 }),
      "name" -> text.verifying(Messages("error.required", "クライアント名"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 255),{_.length <= 255 }),
      "email" -> optional(text.verifying(ValidationHelper.emailAddress)),
      "role" -> optional(text.verifying(Messages("error.maxLength", 100),{_.length <= 100 })),
      "appCreatedDate" -> optional(date("yyyy-MM-dd")),
      "memo" -> optional(text.verifying(Messages("error.maxLength", 1000),{_.length <= 1000 })),
      "status" -> text.verifying(Messages("error.required", "ステータス"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 50),{_.length <= 50 }),
      "updater" -> optional(number)
 
      )
      (Clientsapply)(Clientsunapply)
  )
  private def Clientsapply(
      id: Option[Long],
      appClientId:  String,
      name:  String,
      email:  Option[String],
      role:  Option[String],
      appCreatedDate:  Option[Date],
      memo:  Option[String],
      status: String,
      updater: Option[Int]
      ) = new ClientsRow(id.getOrElse(0), 0, 0, appClientId, Some(name), email, role, appCreatedDate, null, Option(""), Option(0), memo, status, Option(RecordValid.IsEnabledRecord), updater, new Date, new Date)
  private def Clientsunapply(n: ClientsRow) = Some(
      (Option(n.id), n.appClientId, n.name.getOrElse(""), n.email, n.role, n.appCreatedDate, n.memo, n.status, n.updater)
      )

}