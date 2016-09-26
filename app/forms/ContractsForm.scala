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

class ContractsForm @Inject()(val messagesApi: MessagesApi)(implicit ctx: ExecutionContext) extends I18nSupport{
    val form = Form(
    mapping(
      "id" -> optional(longNumber),
      "clientId" -> longNumber,
      "registedDate" -> optional(date("yyyy-MM-dd")),
      "activatedDate" -> optional(date("yyyy-MM-dd")),
      "closeDate" -> optional(date("yyyy-MM-dd")),
      "invoiceIssueType" -> optional(text.verifying(Messages("error.maxLength", 100),{_.length <= 100 })),
      "memo" -> optional(text.verifying(Messages("error.maxLength", 1000),{_.length <= 1000 })),
      "status" -> text.verifying(Messages("error.required", "ステータス"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 50),{_.length <= 50 }),
      "updater" -> optional(number)
 
      )
      (Contractsapply)(Contractsunapply)
  )
  private def Contractsapply(
      id: Option[Long],
      clientId: Long,
      registedDate:  Option[Date],
      activatedDate:  Option[Date],
      closeDate:  Option[Date],
      invoiceIssueType:  Option[String],
      memo:  Option[String],
      status: String,
      updater: Option[Int]
      ) = new ContractsRow(id.getOrElse(0), clientId, registedDate, activatedDate, closeDate, invoiceIssueType, memo, status, Option(RecordValid.IsEnabledRecord), updater, new Date, new Date)
  private def Contractsunapply(n: ContractsRow) = Some(
      (Option(n.id), n.clientId, n.registedDate, n.activatedDate, n.closeDate, n.invoiceIssueType, n.memo, n.status, n.updater)
      )

}