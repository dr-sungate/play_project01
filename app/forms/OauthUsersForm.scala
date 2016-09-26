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

class OauthUsersForm @Inject()(val messagesApi: MessagesApi) extends I18nSupport{
    val form = Form(
    mapping(
      "guid" -> optional(uuid),
      "applicationId" -> longNumber,
      "agencyId" -> optional(longNumber),
      "clientId" -> optional(longNumber),
      "name" -> text.verifying(Messages("error.required", "名前"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 10),{_.length <= 50 })
                            .verifying(Messages("error.minLength", 4),{_.length >=4})
      )
      (OauthUsersapply)(OauthUsersunapply)
  )
  private def OauthUsersapply(
      guid: Option[UUID],
      applicationId: Long,
      agencyId: Option[Long],
      clientId: Option[Long],
      name: String
  ) = new OauthUsersRow(guid.getOrElse(null), applicationId, agencyId, clientId, name, Option(0), new Date, new Date)
  private def OauthUsersunapply(n: OauthUsersRow) = Some(
      (Option(n.guid), n.applicationId, n.agencyId, n.clientId, n.name)
      )

}