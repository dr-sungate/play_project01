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

class ContractAddressesForm @Inject()(val messagesApi: MessagesApi)(implicit ctx: ExecutionContext) extends I18nSupport{
    val form = Form(
    mapping(
        "contractId" -> optional(longNumber),
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
      )
      (ContractAddressesapply)(ContractAddressesunapply)
  )
  private def ContractAddressesapply(
      contractId: Option[Long],
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
        ) = new ContractAddressesRow(contractId.getOrElse(0), Option(company), postCode, prefecture, city, address1, address2, address3, depertment, staff, staffEmail, phone, fax, updater,new Date,new Date)
  private def ContractAddressesunapply(n: ContractAddressesRow) = Some(
      (Option(n.contractId), n.company.getOrElse(""), n.postCode, n.prefecture, n.city, n.address1, n.address2, n.address3, n.depertment, n.staff, n.staffEmail, n.phone, n.fax , n.updater)
      )

}