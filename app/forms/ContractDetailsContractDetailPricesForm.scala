package forms

import java.util.Date
import org.joda.time.LocalDate



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

class ContractDetailsContractDetailPricesForm @Inject()(val messagesApi: MessagesApi)(implicit ctx: ExecutionContext) extends I18nSupport{
    val form = Form(
    mapping(
      "id" -> optional(longNumber),
      "contractId" -> longNumber,
      "defaultProductId" -> optional(longNumber),
      "appProductId" -> optional(text.verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
      "productName" -> text.verifying(Messages("error.required", "商品名"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 255),{_.length <= 255 })
                            .verifying(Messages("error.minLength", 4),{_.length >=4}),
      "productType" -> text.verifying(Messages("error.required", "商品種別"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 100),{_.length <= 100 }),
      "accountName" -> optional(text.verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
      "details" -> optional(text.verifying(Messages("error.maxLength", 1000),{_.length <= 1000 })),
      "registedDate" -> optional(date("yyyy-MM-dd")),
      "closeDate" -> optional(jodaLocalDate("yyyy-MM-dd")),
      "closeScheduledDate" -> optional(jodaLocalDate("yyyy-MM-dd")),
      "closeReason" -> optional(text.verifying(Messages("error.maxLength", 1000),{_.length <= 1000 })),
      "status" -> text.verifying(Messages("error.required", "ステータス"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 50),{_.length <= 50 }),
      "memo" -> optional(text.verifying(Messages("error.maxLength", 5000),{_.length <= 5000 })
                            .verifying(Messages("error.minLength", 4),{_.length >=4})),
      "updater" -> optional(number),
      "contractDetailPrice" -> mapping(
        "contractDetailId" -> optional(longNumber),
        "unitPrice" -> bigDecimal(30, 2),
        "quantity" -> number,
        "discount" -> bigDecimal(30, 2),
        "discountRate" -> bigDecimal(10, 2),
        "updater" -> optional(number)
      )(ContractDetailPricesapply)(ContractDetailPricesunapply)
 
      )
      (ContractDetailsapply)(ContractDetailsunapply)
  )
  private def ContractDetailsapply(
      id: Option[Long],
      contractId: Long,
      defaultProductId:  Option[Long],
      appProductId:  Option[String],
      productName:  String,
      productType:  String,
      accountName:  Option[String],
      details:  Option[String],
      registedDate:   Option[Date],
      closeDate:   Option[LocalDate],
      closeScheduledDate:   Option[LocalDate],
      closeReason:   Option[String],
      status:  String,
      memo: Option[String], 
      updater: Option[Int],
      contractDetailPrice:ContractDetailPricesRow
      ) = new ContractDetailsContractDetailPricesRow(id.getOrElse(0), contractId, defaultProductId, appProductId, Option(productName), Option(productType), accountName, details, registedDate, closeDate, closeScheduledDate, closeReason, status, memo, Option(RecordValid.IsEnabledRecord), updater, new Date, new Date, contractDetailPrice)
  private def ContractDetailsunapply(n: ContractDetailsContractDetailPricesRow) = Some(
      (Option(n.id), n.contractId, n.defaultProductId, n.appProductId, n.productName.getOrElse(""), n.productType.getOrElse(""), n.accountName, n.details, n.registedDate, n.closeDate, n.closeScheduledDate, n.closeReason, n.status, n.memo, n.updater, n.contractDetailPrice)
      )
  private def ContractDetailPricesapply(
      contractDetailId: Option[Long],
      unitPrice:  BigDecimal,
      quantity:  Int,
      discount:  BigDecimal,
      discountRate:  BigDecimal,
      updater: Option[Int]
        ) = new ContractDetailPricesRow(contractDetailId.getOrElse(0), Option(unitPrice), Option(quantity), Option(discount), Option(discountRate), updater,new Date,new Date)
  private def ContractDetailPricesunapply(n: ContractDetailPricesRow) = Some(
      (Option(n.contractDetailId), n.unitPrice.getOrElse(BigDecimal(0)), n.quantity.getOrElse(0), n.discount.getOrElse(BigDecimal(0)), n.discountRate.getOrElse(BigDecimal(0)), n.updater)
      )

  def createContractDetailsContractDetailPricesRow(contractDetail: ContractDetailsRow, contractDetailPrice: ContractDetailPricesRow): ContractDetailsContractDetailPricesRow = {
      new ContractDetailsContractDetailPricesRow(contractDetail.id, contractDetail.contractId, contractDetail.defaultProductId, contractDetail.appProductId, contractDetail.productName, contractDetail.productType, contractDetail.accountName, contractDetail.details, contractDetail.registedDate, contractDetail.closeDate, contractDetail.closeScheduledDate, contractDetail.closeReason, contractDetail.status, contractDetail.memo, contractDetail.isDisabled, contractDetail.updater, contractDetail.createdDate, contractDetail.updatedDate, contractDetailPrice)
  }

}