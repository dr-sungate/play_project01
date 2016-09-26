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
import utilities.status._

class ClientsContractsForm @Inject()(val messagesApi: MessagesApi)(implicit ctx: ExecutionContext) extends I18nSupport{
    val form = Form(
    mapping(
      "id" -> optional(longNumber),
      "applicationId" -> longNumber,
      "agencyId" -> longNumber,
      "appClientId" -> text.verifying(Messages("error.required", "アプリ側クライアントID"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 255),{_.length <= 255 }),
      "name" -> text.verifying(Messages("error.required", "クライアント名"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 255),{_.length <= 255 }),
      "email" -> optional(text.verifying(ValidationHelper.emailAddress)),
      "role" -> optional(text.verifying(Messages("error.maxLength", 100),{_.length <= 100 })),
      "appCreatedDate" -> optional(date("yyyy-MM-dd")),
      "memo" -> optional(text.verifying(Messages("error.maxLength", 1000),{_.length <= 1000 })),
      "updater" -> optional(number),
      "contract" -> mapping(
        "id" -> optional(longNumber),
        "clientId" -> optional(longNumber),
        "registedDate" -> optional(date("yyyy-MM-dd")),
        "activatedDate" -> optional(date("yyyy-MM-dd")),
        "closeDate" -> optional(date("yyyy-MM-dd")),
        "invoiceIssueType" -> optional(text.verifying(Messages("error.maxLength", 100),{_.length <= 100 })),
        "memo" -> optional(text.verifying(Messages("error.maxLength", 1000),{_.length <= 1000 })),
        "updater" -> optional(number)
      )(Contractsapply)(Contractsunapply),
      "contractAddress" -> mapping(
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
      )(ContractAddressesapply)(ContractAddressesunapply)
 
      )
      (ClientsContractsapply)(ClientsContractsunapply)
  )
  private def ClientsContractsapply(
      id: Option[Long],
      applicationId: Long,
      agencyId: Long,
      appClientId:  String,
      name:  String,
      email:  Option[String],
      role:  Option[String],
      appCreatedDate:  Option[Date],
      memo:  Option[String],
      updater: Option[Int],
      contract:ContractsRow,
      contractAddress:ContractAddressesRow
      ) = new ClientsContractsRow(id.getOrElse(0), applicationId, agencyId, appClientId, Some(name), email, role, appCreatedDate, null, Option(""), Option(0), memo, ClientStatus.ActiveStatus, Option(RecordValid.IsEnabledRecord), updater, new Date, new Date, contract, contractAddress)
  private def ClientsContractsunapply(n: ClientsContractsRow) = Some(
      (Option(n.id), n.applicationId, n.agencyId, n.appClientId, n.name.getOrElse(""), n.email, n.role, n.appCreatedDate, n.memo, n.updater, n.contract, n.contractAddress)
      )
  private def Contractsapply(
      id: Option[Long],
      clientId: Option[Long],
      registedDate:  Option[Date],
      activatedDate:  Option[Date],
      closeDate:  Option[Date],
      invoiceIssueType:  Option[String],
      memo:  Option[String],
      updater: Option[Int]
      ) = new ContractsRow(id.getOrElse(0), clientId.getOrElse(0), registedDate, activatedDate, closeDate, invoiceIssueType, memo, ContractStatus.ApplyingStatus, Option(RecordValid.IsEnabledRecord), updater, new Date, new Date)
  private def Contractsunapply(n: ContractsRow) = Some(
      (Option(n.id), Option(n.clientId), n.registedDate, n.activatedDate, n.closeDate, n.invoiceIssueType, n.memo, n.updater)
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

  def createClientsContractsRow(billingDestination: BillingDestinationsRow, billingAddress: BillingAddressesRow): BillingDestinationsBillingAddressesRow = {
      new BillingDestinationsBillingAddressesRow(billingDestination.id, billingDestination.agencyId, billingDestination.billingDestinationName, billingDestination.invoiceIssueType, billingDestination.dueDateMonth, billingDestination.dueDateDay, billingDestination.closingDate, billingDestination.pcaCode, billingDestination.pcaCommonName, billingDestination.memo, billingDestination.updater, billingDestination.createdDate, billingDestination.updatedDate, billingAddress)
  }

}