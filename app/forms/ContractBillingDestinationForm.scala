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

class ContractBillingDestinationForm @Inject()(val messagesApi: MessagesApi)(implicit ctx: ExecutionContext) extends I18nSupport{
  val form = Form(
    mapping(
      "contractId" -> longNumber,
      "billingDestinationId" -> longNumber.verifying(Messages("error.required.selected"), {!_.isNaN()}),
      "updater" -> optional(number)
      )
      (ContractBillingDestinationapply)(ContractBillingDestinationunapply)
  )
  private def ContractBillingDestinationapply(
      contractId: Long,
      billingDestinationId: Long,
      updater: Option[Int]
    ) = new ContractBillingDestinationRow(contractId, billingDestinationId, updater, new Date)
  private def ContractBillingDestinationunapply(n: ContractBillingDestinationRow) = Some(
      (n.contractId, n.billingDestinationId, n.updater)
    )
  def createContractBillingDestinationRow(contractId: Long): ContractBillingDestinationRow = {
      new ContractBillingDestinationRow(contractId, 0, Option(0), new Date)
  }

}