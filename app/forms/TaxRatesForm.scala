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

class TaxRatesForm @Inject()(val messagesApi: MessagesApi)(implicit ctx: ExecutionContext) extends I18nSupport{
    val form = Form(
    mapping(
      "id" -> optional(number),
      "taxRate" ->bigDecimal(10, 2),
      "startFrom" -> date("yyyy-MM-dd'T'HH:mm")
 

      )
      (TaxRatesapply)(TaxRatesunapply)
  )
  private def TaxRatesapply(
      id: Option[Int],
      taxRate:  BigDecimal,
      startFrom:  Date
       ) = new TaxRatesRow(id.getOrElse(0), Option(taxRate), Option(startFrom), new Date)
  private def TaxRatesunapply(n: TaxRatesRow) = Some(
      (Option(n.id), n.taxRate.getOrElse(BigDecimal(0)), n.startFrom.getOrElse(new Date))
      )

}