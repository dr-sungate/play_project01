package forms

import java.util.Date
import play.api.data.Form
import play.api.data.Forms._
import models.TablesExtend._
import play.api.data.validation.Constraints._
import play.api.i18n.{I18nSupport, MessagesApi, Messages, Lang}
import javax.inject.Inject
import java.util.UUID
import utilities._

class OauthClientsSearchForm @Inject()(val messagesApi: MessagesApi) extends I18nSupport{
    val form = Form(
    mapping(
      "oauthUserGuid" -> optional(text),
      "oauthUserName" -> optional(text.verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
      "agencyId" -> optional(longNumber),
      "clientName" -> optional(text.verifying(Messages("error.maxLength", 255),{_.length <= 255 }))
      )
      (oauthclientsearchapply)(oauthclientsearchunapply)
  )
  private def oauthclientsearchapply(
      oauthUserGuid: Option[String], 
      oauthUserName:  Option[String],
      agencyId:  Option[Long],
      clientName:  Option[String]
       ) = new OauthClientsSearchRow(oauthUserGuid, oauthUserName, agencyId, clientName)
  private def oauthclientsearchunapply(n: OauthClientsSearchRow) = Some(
      (n.oauthUserGuid, n.oauthUserName, n.agencyId, n.clientName)
      )

}