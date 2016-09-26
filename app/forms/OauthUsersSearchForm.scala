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

class OauthUsersSearchForm @Inject()(val messagesApi: MessagesApi) extends I18nSupport{
    val form = Form(
    mapping(
       "agencyId" -> optional(longNumber),
      "name" -> optional(text.verifying(Messages("error.maxLength", 255),{_.length <= 255 })),
      "clientName" -> optional(text.verifying(Messages("error.maxLength", 255),{_.length <= 255 }))
      )
      (oauthusersearchapply)(oauthusersearchunapply)
  )
  private def oauthusersearchapply(
      agencyId: Option[Long], 
      name: Option[String], 
      clientName:  Option[String]
       ) = new OauthUsersSearchRow(agencyId, name, clientName)
  private def oauthusersearchunapply(n: OauthUsersSearchRow) = Some(
      (n.agencyId, n.name, n.clientName)
      )

}