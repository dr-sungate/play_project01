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

class BillingDestinationsBillingAaddressesForm @Inject()(val messagesApi: MessagesApi)(implicit ctx: ExecutionContext) extends I18nSupport{
    val form = Form(
    mapping(
      "id" -> optional(longNumber),
      "agencyId" -> longNumber,
      "billingDestinationName" -> text.verifying(Messages("error.required", "請求先名"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 255),{_.length <= 255 })
                            .verifying(Messages("error.minLength", 4),{_.length >=4}),
      "invoiceIssueType" -> text.verifying(Messages("error.required", "請求書発行種別"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 100),{_.length <= 100 }),
      "dueDateMonth" -> optional(text.verifying(Messages("error.maxLength", 100),{_.length <= 100 })),
      "dueDateDay" -> optional(number),
      "closingDate" -> optional(number),
      "pcaCode" -> optional(text.verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
      "pcaCommonName" -> optional(text.verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
      "memo" -> optional(text.verifying(Messages("error.maxLength", 5000),{_.length <= 5000 })
                            .verifying(Messages("error.minLength", 4),{_.length >=4})),
      "updater" -> optional(number),
      "billingAddresses" -> mapping(
        "billingDestinationId" -> optional(longNumber),
        "company" -> text.verifying(Messages("error.required", "会社名"), {!_.isEmpty})
                                  .verifying(Messages("error.maxLength", 255),{_.length <= 255 }),
        "postCode" -> optional(text.verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
        "prefecture" -> optional(text.verifying(Messages("error.required", "都道府県"), {!_.isEmpty})
                                  .verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
        "city" -> optional(text.verifying(Messages("error.required", "市区町村"), {!_.isEmpty})
                                  .verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
        "address1" -> optional(text.verifying(Messages("error.required", "住所1"), {!_.isEmpty})
                                  .verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
        "address2" -> optional(text.verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
        "address3" -> optional(text.verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
        "depertment" -> optional(text.verifying(Messages("error.required", "部署"), {!_.isEmpty})
                                  .verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
        "staff" -> optional(text.verifying(Messages("error.required", "担当者"), {!_.isEmpty})
                                  .verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
        "staffEmail" -> optional(text.verifying(ValidationHelper.emailAddress)),
        "phone" -> optional(text.verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
        "fax" -> optional(text.verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
        "updater" -> optional(number)
      )(BillingAaddressesapply)(BillingAaddressesunapply)
 
      )
      (BillingDestinationsBillingAaddressesapply)(BillingDestinationsBillingAaddressesunapply)
  )
  private def BillingDestinationsBillingAaddressesapply(
      id: Option[Long],
      agencyId: Long,
      billingDestinationName:  String,
      invoiceIssueType:  String,
      dueDateMonth:  Option[String],
      dueDateDay:  Option[Int],
      closingDate:  Option[Int],
      pcaCode: Option[String],
      pcaCommonName: Option[String],
      memo: Option[String], 
      updater: Option[Int],
      billingAddresses:BillingAddressesRow
      ) = new BillingDestinationsBillingAddressesRow(id.getOrElse(0), agencyId, Option(billingDestinationName), Option(invoiceIssueType), dueDateMonth, dueDateDay, closingDate, pcaCode, pcaCommonName, memo, updater, new Date, new Date, billingAddresses)
  private def BillingDestinationsBillingAaddressesunapply(n: BillingDestinationsBillingAddressesRow) = Some(
      (Option(n.id), n.agencyId, n.billingDestinationName.getOrElse(""), n.invoiceIssueType.getOrElse(""), n.dueDateMonth, n.dueDateDay, n.closingDate, n.pcaCode, n.pcaCommonName, n.memo, n.updater, n.billingAddresses)
      )
  private def BillingAaddressesapply(
      billingDestinationId: Option[Long],
      company:  String,
      postCode:  Option[String],
      prefecture:  Option[String],
      city:  Option[String],
      address1:  Option[String],
      address2:  Option[String],
      address3:  Option[String],
      depertment:  Option[String],
      staff:  Option[String],
      staffEmail:  Option[String],
      phone:  Option[String],
      fax:  Option[String],
      updater: Option[Int]
        ) = new BillingAddressesRow(billingDestinationId.getOrElse(0), Option(company), postCode, prefecture, city, address1, address2, address3, depertment, staff, staffEmail, phone, fax, updater,new Date,new Date)
  private def BillingAaddressesunapply(n: BillingAddressesRow) = Some(
      (Option(n.billingDestinationId), n.company.getOrElse(""), n.postCode, n.prefecture, n.city, n.address1, n.address2, n.address3, n.depertment, n.staff, n.staffEmail, n.phone, n.fax , n.updater)
      )

  def createBillingDestinationsBillingAaddressesRow(billingDestination: BillingDestinationsRow, billingAddress: BillingAddressesRow): BillingDestinationsBillingAddressesRow = {
      new BillingDestinationsBillingAddressesRow(billingDestination.id, billingDestination.agencyId, billingDestination.billingDestinationName, billingDestination.invoiceIssueType, billingDestination.dueDateMonth, billingDestination.dueDateDay, billingDestination.closingDate, billingDestination.pcaCode, billingDestination.pcaCommonName, billingDestination.memo, billingDestination.updater, billingDestination.createdDate, billingDestination.updatedDate, billingAddress)
  }

}