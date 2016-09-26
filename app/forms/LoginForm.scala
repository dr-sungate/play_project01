package forms

import java.util.Date
import play.api.data.Form
import play.api.data.Forms._
import models.TablesExtend._
import play.api.data.validation.Constraints._
import play.api.i18n.{I18nSupport, MessagesApi, Messages, Lang}
import javax.inject.Inject
import utilities._

class LoginForm @Inject()(val messagesApi: MessagesApi) extends I18nSupport{
  val loginForm = Form(
    mapping(
      "loginId" -> text.verifying(Messages("loginId または password　が違います"), {!_.isEmpty}),
      "password" -> text.verifying(Messages("loginId または password　が違います"), {!_.isEmpty})
    )(loginormapply)(loginormunapply)
  )
  private def loginormapply(
      loginId: String, 
      password:  String
       ) = new AccountsRow(0, loginId, password, null, null,null, Option(false), Option(0), new Date, new Date)
  private def loginormunapply(n: AccountsRow) = Some(
      (n.loginId, n.password)
      )

}