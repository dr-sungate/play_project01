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

class DefaultProductsForm @Inject()(val messagesApi: MessagesApi)(implicit ctx: ExecutionContext) extends I18nSupport{
    val form = Form(
    mapping(
      "id" -> optional(longNumber),
      "agencyId" -> longNumber,
      "productName" -> text.verifying(Messages("error.required", "商品名"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 255),{_.length <= 255 })
                            .verifying(Messages("error.minLength", 4),{_.length >=4}),
      "productType" -> text.verifying(Messages("error.required", "商品種別"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 100),{_.length <= 100 }),
      "price" -> bigDecimal(30, 2),
      "memo" -> optional(text.verifying(Messages("error.maxLength", 5000),{_.length <= 5000 })
                            .verifying(Messages("error.minLength", 4),{_.length >=4})),
      "updater" -> optional(number)
 
      )
      (Defaultproductsapply)(Defaultproductsunapply)
  )
  private def Defaultproductsapply(
      id: Option[Long],
      agencyId: Long,
      productName:  String,
      productType:  String,
      price: BigDecimal,
      memo:  Option[String],
      updater: Option[Int]
      ) = new DefaultProductsRow(id.getOrElse(0), agencyId, Option(productName), Option(productType), Option(price), memo, Option(RecordValid.IsEnabledRecord), updater, new Date, new Date)
  private def Defaultproductsunapply(n: DefaultProductsRow) = Some(
      (Option(n.id), n.agencyId, n.productName.getOrElse(""), n.productType.getOrElse(""), n.price.getOrElse(BigDecimal(0)), n.memo, n.updater)
      )
}