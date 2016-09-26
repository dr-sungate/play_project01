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

class AgenciesForm @Inject()(val messagesApi: MessagesApi)(implicit ctx: ExecutionContext) extends I18nSupport{
    val form = Form(
    mapping(
      "id" -> optional(longNumber),
      "agencyName" -> text.verifying(Messages("error.required", "代理店名"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 255),{_.length <= 255 })
                            .verifying(Messages("error.minLength", 4),{_.length >=4}),
       "type" -> optional(text.verifying(Messages("error.required", "種別"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 50),{_.length <= 50 })),
       "status" -> text.verifying(Messages("error.required", "ステータス"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 50),{_.length <= 50 }),
       "memo" -> optional(text.verifying(Messages("error.maxLength", 5000),{_.length <= 5000 })
                            .verifying(Messages("error.minLength", 4),{_.length >=4})),
        "timezoneId" -> optional(number),
        "updater" -> optional(number)
 
      )
      (Agenciesapply)(Agenciesunapply)
  )
  private def Agenciesapply(
      id: Option[Long],
      agencyName:  String,
      `type`:  Option[String],
      status: String,
      memo: Option[String], 
      timezoneId: Option[Int],
      updater: Option[Int]
      ) = new AgenciesRow(id.getOrElse(0), agencyName, `type`, status, memo, Option(RecordValid.IsEnabledRecord), timezoneId, updater, new Date, new Date)
  private def Agenciesunapply(n: AgenciesRow) = Some(
      (Option(n.id), n.agencyName, n.`type`, n.status, n.memo, n.timezoneId, n.updater)
      )

}