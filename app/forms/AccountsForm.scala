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

class AccountsForm @Inject()(val messagesApi: MessagesApi,accountsDao: AccountsDAO)(implicit ctx: ExecutionContext) extends I18nSupport{
    val formnew = Form(
    mapping(
      "id" -> optional(number),
      "loginId" -> text.verifying(Messages("error.required", "ログインID"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 100),{_.length <= 100 })
                            .verifying(Messages("error.minLength", 6),{_.length >=6})
                            .verifying(Messages("error.duplicate", "ログインID"),fields => fields match {
                              case loginId => 
                                Await.result(ValidationHelper.isExistsLoginId(loginId, accountsDao), Duration.Inf)
                              }),
      "password" -> text.verifying(Messages("error.required", "パスワード"), {!_.isEmpty})
                                  .verifying(Messages("error.maxLength", 50),{_.length <= 50 })
                            .verifying(Messages("error.minLength", 12),{_.length >=12})
                            .verifying(ValidationHelper.isBothAlphabetNumeric),
      "name" -> text.verifying(Messages("error.required", "名前"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 255),{_.length <= 255 })
                            .verifying(Messages("error.minLength", 4),{_.length >=4}),
      "email" -> optional(text.verifying(ValidationHelper.emailAddress)),
      "role" -> text.verifying(Messages("error.maxLength", 50),{_.length <= 50 })

      )
      (Accountsapply)(Accountsunapply)
  )
    val formedit = Form(
    mapping(
      "id" -> optional(number),
      "loginId" -> text.verifying(Messages("error.required", "ログインID"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 100),{_.length <= 100 })
                            .verifying(Messages("error.minLength", 6),{_.length >=6}),
      "password" -> text.verifying(Messages("error.maxLength", 50),{_.length <= 50 })
                            .verifying(Messages("error.minLength", 12),{e => e.isEmpty || e.length >=12})
                            .verifying(ValidationHelper.isBothAlphabetNumeric),
      "name" -> text.verifying(Messages("error.required", "名前"), {!_.isEmpty})
                            .verifying(Messages("error.maxLength", 255),{_.length <= 255 })
                            .verifying(Messages("error.minLength", 4),{_.length >=4}),
      "email" -> optional(email),
      "role" -> text.verifying(Messages("error.maxLength", 50),{_.length <= 50 })

      )
      (Accountsapply)(Accountsunapply)
  )
  private def Accountsapply(
      id: Option[Int],
      loginId:  String,
      password:  String,
      name: String, 
      email:  Option[String],
      role: String
       ) = new AccountsRow(id.getOrElse(0), loginId, password, name, email, role, Option(false), Option(0), new Date, new Date)
  private def Accountsunapply(n: AccountsRow) = Some(
      (Option(n.id), n.loginId, n.password, n.name, n.email, n.role)
      )

}