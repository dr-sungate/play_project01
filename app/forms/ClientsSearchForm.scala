package forms

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


class ClientsSearchForm @Inject()(val messagesApi: MessagesApi)(implicit ctx: ExecutionContext) extends I18nSupport{
    val form = Form(
    mapping(
      "agencyId" -> optional(longNumber),
      "appClientId" -> optional(text.verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
      "searchWord" -> optional(text.verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
      "role" -> optional(text.verifying(Messages("error.maxLength", 100),{_.length <= 100 })),
      "clientStatus" -> optional(text.verifying(Messages("error.maxLength", 50),{_.length <= 50 })),
      "contractStatus" -> optional(text.verifying(Messages("error.maxLength", 50),{_.length <= 50 }))
     )
      (clientssearchapply)(clientsearchunapply)
  )
  private def clientssearchapply(
      agencyId: Option[Long], 
      appClientId: Option[String], 
      searchWord: Option[String], 
      role:  Option[String],
      clientStatus:  Option[String],
      contractStatus:  Option[String]
       ) = new ClientsSearchRow(agencyId, appClientId, searchWord, role, clientStatus, contractStatus)
  private def clientsearchunapply(n: ClientsSearchRow) = Some(
      n.agencyId, n.appClientId, n.searchWord, n.role, n.clientStatus, n.contractStatus
      )
}