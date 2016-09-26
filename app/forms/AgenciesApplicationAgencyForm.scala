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

class AgenciesApplicationAgencyForm @Inject()(val messagesApi: MessagesApi)(implicit ctx: ExecutionContext) extends I18nSupport{
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
        "updater" -> optional(number),
        "applicationAgency" -> mapping(
           "applicationId" -> longNumber,
           "agencyId" -> optional(longNumber),
           "updater" -> optional(number)
        )(ApplicationAgencyapply)(ApplicationAgencyunapply)
 
      )
      (AgenciesApplicationAgencyapply)(AgenciesApplicationAgencyunapply)
  )
  private def AgenciesApplicationAgencyapply(
      id: Option[Long],
      agencyName:  String,
      `type`:  Option[String],
      status: String,
      memo: Option[String], 
      timezoneId: Option[Int],
      updater: Option[Int],
      applicationAgency: ApplicationAgencyRow
      ) = new AgenciesApplicationAgencyRow(id.getOrElse(0), agencyName, `type`, status, memo, Option(RecordValid.IsEnabledRecord), timezoneId, updater, new Date, new Date, applicationAgency)
  private def AgenciesApplicationAgencyunapply(n: AgenciesApplicationAgencyRow) = Some(
      (Option(n.id), n.agencyName, n.`type`, n.status, n.memo, n.timezoneId, n.updater, n.applicationAgency)
      )
  private def ApplicationAgencyapply(
      applicationId: Long,
      agencyId: Option[Long],
      updater: Option[Int]
        ) = new ApplicationAgencyRow(applicationId, agencyId.getOrElse(0), updater,new Date)
  private def ApplicationAgencyunapply(n: ApplicationAgencyRow) = Some(
      (n.applicationId, Option(n.agencyId), n.updater)
      )

      def createAgenciesApplicationAgencyRow(agency: AgenciesRow, applicationAgency: ApplicationAgencyRow): AgenciesApplicationAgencyRow = {
      new AgenciesApplicationAgencyRow(agency.id, agency.agencyName, agency.`type`, agency.status, agency.memo, agency.isDisabled, agency.timezoneId, agency.updater, agency.createdDate, agency.updatedDate, applicationAgency)
  }

}