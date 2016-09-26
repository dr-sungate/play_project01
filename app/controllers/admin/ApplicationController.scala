package controllers.admin

import scala.concurrent.Future
import scala.concurrent.Future.{successful => future}

import jp.t2v.lab.play2.auth.AuthElement
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
import play.api.cache._
import play.filters.csrf._
import play.filters.csrf.CSRF.Token
import services.UserAccountServiceLike
import services.dao._
import skinny.util.JSONStringOps._
import views._
import utilities.auth.Role
import utilities.auth.Role._
import utilities.config._
import utilities._

import play.api.libs.json._
import jp.t2v.lab.play2.auth.AuthElement
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration



class ApplicationController @Inject()(addToken: CSRFAddToken, checkToken: CSRFCheck, cache: CacheApi, cached: Cached, val userAccountService: UserAccountServiceLike, applicationsDao: ApplicationsDAO, ApplicationsForm:ApplicationsForm, AppSelectorForm: AppSelectorForm, val messagesApi: MessagesApi) extends Controller with AuthActionBuilders with AuthElement with AuthConfigAdminImpl with I18nSupport  {
  
  val UserAccountSv = userAccountService
  
  val pageOffset = PageNation.defaultMaxPageNum
 
  
  /** This result directly redirect to the application home.*/
  val Home = Redirect(controllers.admin.routes.ApplicationController.index(1))

  
  def index(page: Int = 1) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      applicationsDao.paginglist(page, pageOffset).map{case (pagecount,applications) =>
        Ok(views.html.application.list("", applications.toList, new PageNation(page, pagecount, pageOffset)))
      }
   }
  }
  def add = addToken{
    AuthorizationAction(NormalUser) {implicit request =>
      val currentuser: User = request.user
      Ok(views.html.application.regist("", ApplicationsForm.form, currentuser))
    }
  }
  
  def create = checkToken{
    AuthorizationAction(NormalUser).async { implicit request =>
      val currentuser: User = request.user
      ApplicationsForm.form.bindFromRequest.fold(
          formWithErrors => {
            Logger.debug(formWithErrors.toString())
            Future(BadRequest(views.html.application.regist("エラー", formWithErrors, currentuser)))
          },
          application => {
            applicationsDao.create(application).flatMap(cnt =>
                //if (cnt != 0) applicationsDao.all().map(applications => Ok(views.html.application.list("登録しました", applications)))
                if (cnt != 0) Future.successful(Redirect(controllers.admin.routes.ApplicationController.index(1)))
                else applicationsDao.all().map(notifications => BadRequest(views.html.application.edit("エラー", ApplicationsForm.form.fill(application), currentuser)))
             )
          }
      )
    }
  }
   
  def edit(applicationId: Long) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      val currentuser: User = request.user
      applicationsDao.findById(applicationId).flatMap(option =>
        option match {
          case Some(application) => Future(Ok(views.html.application.edit("GET", ApplicationsForm.form.fill(application), currentuser)))
          case None => Future.successful(Status(404)(views.html.errors.error404notfound("no found")))
        }
      )
    }
  }

  def update = checkToken {
    AuthorizationAction(NormalUser).async { implicit request =>
      val currentuser: User = request.user
      ApplicationsForm.form.bindFromRequest.fold(
          formWithErrors => {
            Future(BadRequest(views.html.application.edit("ERROR", formWithErrors, currentuser)))
          },
          application => {
            applicationsDao.update_mappinged(application).flatMap(cnt =>
              if (cnt != 0) Future.successful(Redirect(controllers.admin.routes.ApplicationController.index(1)))
              else applicationsDao.all().map(notifications => BadRequest(views.html.application.edit("エラー", ApplicationsForm.form.fill(application), currentuser)))
            )
          }
      )
    }
  }
  
  def delete(id: Long) = checkToken{
    AuthorizationAction(NormalUser).async { implicit request =>
      val currentuser = request.user
      cache.remove(FormConfig.FormCacheKey)
      applicationsDao.disable(id, currentuser.id).flatMap(cnt =>
        if (cnt != 0) Future.successful(Redirect(controllers.admin.routes.ApplicationController.index(1)))
        else Future.successful(Status(500)(views.html.errors.error500internalerror("error")))
      )
    }
  }
  
  def getAppList = checkToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      applicationsDao.getValidListMap().map{case list =>
        Ok(Json.toJson(list))
      }
    }
  }
  
  def getAppListWithSelected = checkToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      val selectedApplicationId = cache.get[Long](AppSelectorConfig.AppSelectorCacheKey + "-" + request.session.get("uuid"))
       selectedApplicationId match {
        case Some(selectedApplicationId) => 
           applicationsDao.getValidListMap(selectedApplicationId).map{case list =>
            Ok(Json.toJson(list))
          }
        case _ => Future(Ok(Json.toJson("")))
      }
    }
  }
    
  def getAppListWithAgency = checkToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      applicationsDao.getValidListWithAgencyMap().map{case list =>
        Ok(Json.toJson(list))
      }
    }
  }
  
  def getAppListWithAgencyWithSelected = checkToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      val selectedApplicationId = cache.get[Long](AppSelectorConfig.AppSelectorCacheKey + "-" + request.session.get("uuid"))
       selectedApplicationId match {
        case Some(selectedApplicationId) => 
           applicationsDao.getValidListWithAgencyMap(selectedApplicationId).map{case list =>
            Ok(Json.toJson(list))
          }
        case _ => Future(Ok(Json.toJson("")))
      }
    }
  }
    
    
  def getValidCount = checkToken {
    AuthorizationAction(NormalUser).async { implicit request =>
       applicationsDao.count().map{case cuunt =>
        Ok(Json.toJson(cuunt))
      }
     
    }
  }
  

  def pagenation(currentPageNum: Int, pageName: String) = AuthorizationAction(NormalUser) { implicit request =>
    pageName match {
      case "index" => Redirect(controllers.admin.routes.ApplicationController.index(currentPageNum))
      case _       => Redirect(controllers.admin.routes.ApplicationController.index(currentPageNum))
    }
  }
  
  def setApp = checkToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      AppSelectorForm.form.bindFromRequest.fold(
          formWithErrors => {
            Future(Ok(Json.toJson("ng")))
          },
          appselector => {
            if(Await.result(applicationsDao.checkExists(appselector.applicationId), Duration.Inf) == true){
               cache.set(AppSelectorConfig.AppSelectorCacheKey + "-" + request.session.get("uuid"), appselector.applicationId, AppSelectorConfig.AppSelectorCacheTime)
               Future(Ok(Json.toJson("ok")).withCookies(Cookie(AppSelectorConfig.AppSelectorCookieKey , appselector.applicationId.toString(), Option(AppSelectorConfig.AppSelectorCookieExpires))))
            }else{
              Future(Ok(Json.toJson("ng")))
            }
          }
      )
     }
  }
  def getSelectedAppName = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      val selectedApplicationId = cache.get[Long](AppSelectorConfig.AppSelectorCacheKey + "-" + request.session.get("uuid"))
      val cookieselectedApplicationId : String = request.cookies.get(AppSelectorConfig.AppSelectorCookieKey).map(_.value).getOrElse("")
      selectedApplicationId match {
        case None => 
          if(!cookieselectedApplicationId.isEmpty()){
            cache.set(AppSelectorConfig.AppSelectorCacheKey + "-" + request.session.get("uuid"), cookieselectedApplicationId.toLong, AppSelectorConfig.AppSelectorCacheTime)
            applicationsDao.getAppNameById(cookieselectedApplicationId.toLong).map{ case appName =>
              Ok(Json.toJson(appName)).withCookies(Cookie(AppSelectorConfig.AppSelectorCookieKey , cookieselectedApplicationId, Option(AppSelectorConfig.AppSelectorCookieExpires)))
            }
          }else{
            Future(Ok(Json.toJson("選択されてません")))
          }
          
        case Some(selectedApplicationId) => 
          applicationsDao.getAppNameById(selectedApplicationId).map{ case appName =>
            Ok(Json.toJson(appName)).withCookies(Cookie(AppSelectorConfig.AppSelectorCookieKey , selectedApplicationId.toString(), Option(AppSelectorConfig.AppSelectorCookieExpires)))
          }
        case _ => Future(Ok(Json.toJson("nodata")))
      }
    }
  }
}