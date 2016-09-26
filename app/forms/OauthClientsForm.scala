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


class OauthClientsForm @Inject()(val messagesApi: MessagesApi) extends I18nSupport{
    val form = Form(
    mapping(
      "oauthClientId" -> optional(uuid),
      "oauthUserGuid" -> uuid.verifying(Messages("error.required", "Oauthユーザー"), {_ != null})
                              .verifying(Messages("error.maxLength", 255),{_.toString.length <= 255 })
                              .verifying(Messages("error.minLength", 4),{_.toString.length >=4}),
      "clientSecret" -> text.verifying(Messages("error.required", "シークレット"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 255),{_.length <= 255 })
                            .verifying(Messages("error.minLength", 30),{_.length >=30}),
      "redirectUri" -> text.verifying(Messages("error.required", "リダイレクト先"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 255),{_.length <= 255 })
                            .verifying(Messages("error.minLength", 4),{_.length >=4}),
      "grantType" -> text.verifying(Messages("error.required", "grant type"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 255),{_.length <= 255 })
      )
      (OauthClientsapply)(OauthClientsunapply)
  )
  private def OauthClientsapply(
      oauthClientId: Option[UUID],
      oauthUserGuid: UUID, 
      clientSecret:  String,
      redirectUri:  String,
      grantType:  String
       ) = new OauthClientsRow(oauthClientId.getOrElse(null), oauthUserGuid, Option(clientSecret), Option(redirectUri), Option(grantType), 0, Option(0) ,new Date, new Date)
  private def OauthClientsunapply(n: OauthClientsRow) = Some(
      (Option(n.oauthClientId), n.oauthUserGuid, n.clientSecret.getOrElse(null), n.redirectUri.getOrElse(null), n.grantType.getOrElse(null))
      )

}