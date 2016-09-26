package controllers.admin

import scala.concurrent.Future
import scala.concurrent.Future.{successful => future}

import controllers.auth.AuthConfigAdminImpl
import forms._
import javax.inject.Inject
import jp.t2v.lab.play2.auth.AuthActionBuilders
import models.TablesExtend._
import play.api._
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._
import play.api.mvc.Action
import play.api.mvc.Controller
import play.filters.csrf._
import play.filters.csrf.CSRF.Token
import services.UserAccountServiceLike
import services.dao._
import skinny.util.JSONStringOps._
import views._
import utilities.auth.Role
import utilities.auth.Role._
import java.util.Calendar
import java.util.Date

class TaxRateController @Inject()(addToken: CSRFAddToken, checkToken: CSRFCheck, val userAccountService: UserAccountServiceLike, taxratesDao: TaxRatesDAO, TaxRatesForm:TaxRatesForm, val messagesApi: MessagesApi) extends Controller with AuthActionBuilders with AuthConfigAdminImpl with I18nSupport  {
  val UserAccountSv = userAccountService

  
  /** This result directly redirect to the application home.*/
  val Home = Redirect(controllers.admin.routes.TaxRateController.index)

  
  def index = addToken{
    AuthorizationAction(Administrator).async { implicit request =>

      val taxList = (for {
          alltaxrates <- taxratesDao.all()
          nowrate <- taxratesDao.getNowRateWithDate(new Date)
       } yield (alltaxrates, nowrate))
      taxList.map{case (alltaxrates, nowrate) =>
        Ok(views.html.taxrate.list("", TaxRatesForm.form, alltaxrates, nowrate))
       }
    }
  }
  
  def create = checkToken{
    AuthorizationAction(Administrator).async { implicit request =>
      TaxRatesForm.form.bindFromRequest.fold(
          formWithErrors => {
            val taxList = (for {
                alltaxrates <- taxratesDao.all()
                nowrate <- taxratesDao.getNowRateWithDate(new Date)
             } yield (alltaxrates, nowrate))
            taxList.map{case (alltaxrates, nowrate) =>
              BadRequest(views.html.taxrate.list("登録バリデーションエラーが存在します", formWithErrors, alltaxrates, nowrate))
            }
          },
          taxraterow => {
            taxratesDao.create(taxraterow).flatMap(cnt =>
              if (cnt != 0) {
                 val taxList = (for {
                    alltaxrates <- taxratesDao.all()
                    nowrate <- taxratesDao.getNowRateWithDate(new Date)
                 } yield (alltaxrates, nowrate))
                taxList.map{case (alltaxrates, nowrate) =>
                  Ok(views.html.taxrate.list("登録しました", TaxRatesForm.form, alltaxrates, nowrate))
                }
                
              }else{
                val taxList = (for {
                    alltaxrates <- taxratesDao.all()
                    nowrate <- taxratesDao.getNowRateWithDate(new Date)
                 } yield (alltaxrates, nowrate))
                taxList.map{case (alltaxrates, nowrate) =>
                  BadRequest(views.html.taxrate.list("登録エラーが発生しました", TaxRatesForm.form.fill(taxraterow), alltaxrates, nowrate))
                }
              }
            )
          }
      )
    }
  }
   
  
  def delete(id: Int) = checkToken{
    AuthorizationAction(Administrator).async {implicit request =>
      taxratesDao.delete(id).flatMap(cnt =>
        if (cnt != 0) {
           val taxList = (for {
              alltaxrates <- taxratesDao.all()
              nowrate <- taxratesDao.getNowRateWithDate(new Date)
           } yield (alltaxrates, nowrate))
          taxList.map{case (alltaxrates, nowrate) =>
            Ok(views.html.taxrate.list("削除しました", TaxRatesForm.form, alltaxrates, nowrate))
          }
          
        }else{
          val taxList = (for {
              alltaxrates <- taxratesDao.all()
              nowrate <- taxratesDao.getNowRateWithDate(new Date)
           } yield (alltaxrates, nowrate))
          taxList.map{case (alltaxrates, nowrate) =>
            BadRequest(views.html.taxrate.list("削除エラーが発生しました", TaxRatesForm.form, alltaxrates, nowrate))
          }
        }
      )
    }
  }
    
}