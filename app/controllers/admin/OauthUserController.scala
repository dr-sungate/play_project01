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
import views._
import utilities._
import utilities.auth.Role
import utilities.auth.Role._
import java.util.Calendar
import java.util.UUID

import play.api.libs.json._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration

class OauthUserController @Inject()(addToken: CSRFAddToken, checkToken: CSRFCheck, val userAccountService: UserAccountServiceLike, oauthUsersDao: OauthUsersDAO, clientsDao: ClientsDAO, agenciesDao: AgenciesDAO, applicationsDao: ApplicationsDAO, OauthUsersForm:OauthUsersForm, OauthUsersSearchForm: OauthUsersSearchForm, val messagesApi: MessagesApi) extends Controller with AuthActionBuilders with AuthConfigAdminImpl with I18nSupport  {
  val UserAccountSv = userAccountService
  
  val pageOffset = PageNation.defaultMaxPageNum

  
  /** This result directly redirect to the application home.*/
  val Home = Redirect(controllers.admin.routes.OauthUserController.index(0, 1))

  
  
  def nameautocomplete(applicationId: Long, name: String) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        oauthUsersDao.findByNameList(applicationId, name).map{case oauthuser =>
          Ok(Json.toJson(oauthuser))
        }
      }
    }
  }

  def index(applicationId: Long, page: Int = 1) = addToken{
    AuthorizationAction(Administrator).async { implicit request =>
     if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
       Future(Status(404)(views.html.errors.error404notfound("no found")))
     }else{
       val urlquery: Map[String,String] = request.queryString.map { case (k,v) => k -> v.mkString }
       OauthUsersSearchForm.form.bindFromRequest.fold(
          formWithErrors => {
            Logger.debug(formWithErrors.toString())
           val pagenationOptions = for {
               agencyoptions <- agenciesDao.getValidListForSelectOption(applicationId)
               appoptions <- applicationsDao.getValidListForSelectOption()
            } yield (agencyoptions, appoptions)
      
            pagenationOptions.map{case (agencyoptions, appoptions) =>
               BadRequest(views.html.oauthuser.listerror("エラー", applicationId, formWithErrors, agencyoptions, appoptions))
            }
          },
          oauthclientsearch => {
           val pagenationOptions = for {
               (pagecount,oauthclient) <- oauthUsersDao.paginglist(page, pageOffset, urlquery, applicationId)
               agencyoptions <- agenciesDao.getValidListForSelectOption(applicationId)
               appoptions <- applicationsDao.getValidListForSelectOption()
            } yield ((pagecount,oauthclient), agencyoptions, appoptions)
      
            pagenationOptions.map{case ((pagecount,oauthclient), agencyoptions, appoptions) =>
                Ok(views.html.oauthuser.list("", applicationId, oauthclient.toList, OauthUsersSearchForm.form.fill(oauthclientsearch), agencyoptions, appoptions, new PageNation(page, pagecount, pageOffset)))
            }
          }
        )
      }
    }
  }
  def add(applicationId: Long) = addToken{
    AuthorizationAction(Administrator).async {implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        agenciesDao.getValidListForSelectOption(applicationId).map( agencyoptions =>
          Ok(views.html.oauthuser.regist("", applicationId, OauthUsersForm.form, agencyoptions, currentuser))
        )
      }
    }
  }
  
  def create(applicationId: Long) = checkToken{
    AuthorizationAction(Administrator).async { implicit request =>
       if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        OauthUsersForm.form.bindFromRequest.fold(
          formWithErrors => {
            Logger.debug(formWithErrors.toString())
           agenciesDao.getValidListForSelectOption(applicationId).map( agencyoptions =>
             BadRequest(views.html.oauthuser.regist("エラー", applicationId, formWithErrors, agencyoptions, currentuser))
             )
          },
          oauthuser => {
            oauthUsersDao.create(oauthuser).flatMap(cnt =>
                if (cnt != 0) Future.successful(Redirect(controllers.admin.routes.OauthUserController.index(applicationId, 1)))
                else agenciesDao.getValidListForSelectOption(applicationId).map( agencyoptions  => BadRequest(views.html.oauthuser.edit("エラー", applicationId, OauthUsersForm.form.fill(oauthuser), agencyoptions, currentuser)))
             )
          }
        )
      }
    }
  }
   
  def edit(applicationId: Long, id: String) = addToken{
    AuthorizationAction(Administrator).async {implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        val oauthOptions = for {
            oauthuser <- oauthUsersDao.findById(java.util.UUID.fromString(id))
            agencyoptions <- agenciesDao.getValidListForSelectOption(applicationId)
        } yield (oauthuser, agencyoptions)
        oauthOptions.map { case (oauthuser, agencyoptions) =>
          oauthuser match {
            case Some(oauthuser) => Ok(views.html.oauthuser.edit("GET", applicationId, OauthUsersForm.form.fill(oauthuser), agencyoptions, currentuser))
            case None => (Status(404)(views.html.errors.error404notfound("no found")))
          }
        }
      }
    }
  }

  def update(applicationId: Long) = checkToken {
    AuthorizationAction(Administrator).async { implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        OauthUsersForm.form.bindFromRequest.fold(
            formWithErrors => {
             agenciesDao.getValidListForSelectOption(applicationId).map( agencyoptions =>
                BadRequest(views.html.oauthuser.edit("ERROR", applicationId, formWithErrors, agencyoptions, currentuser))
              )
            },
            oauthuser => {
              oauthUsersDao.update_mappinged(oauthuser).flatMap(cnt =>
                if (cnt != 0) Future.successful(Redirect(controllers.admin.routes.OauthUserController.index(applicationId, 1)))
                else agenciesDao.getValidListForSelectOption(applicationId).map( agencyoptions => BadRequest(views.html.oauthuser.edit("エラー", applicationId, OauthUsersForm.form.fill(oauthuser), agencyoptions, currentuser)))
              )
            }
        )
      }
    }
  }
  
    def delete(applicationId: Long, id: String) = checkToken{
      AuthorizationAction(Administrator).async {implicit request =>
       if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
         Future(Status(404)(views.html.errors.error404notfound("no found")))
       }else{
         oauthUsersDao.delete(java.util.UUID.fromString(id)).flatMap(cnt =>
            if (cnt != 0) Future.successful(Redirect(controllers.admin.routes.OauthUserController.index(applicationId, 1)))
            else Future.successful(Status(500)(views.html.errors.error500internalerror("error")))
          )
        }
      }
    }

  def pagenation(applicationId: Long, currentPageNum: Int, pageName: String) = AuthorizationAction(NormalUser) { implicit request =>
    pageName match {
      case "index" => Redirect(controllers.admin.routes.OauthUserController.index(applicationId, currentPageNum))
      case _       => Redirect(controllers.admin.routes.OauthUserController.index(applicationId, currentPageNum))
    }
  }


}