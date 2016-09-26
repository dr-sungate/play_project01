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
import utilities.auth.Role
import utilities.auth.Role._
import utilities._
import java.util.Calendar
import java.util.UUID

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration

class OauthClientController @Inject()(addToken: CSRFAddToken, checkToken: CSRFCheck, val userAccountService: UserAccountServiceLike, oauthClientsDao: OauthClientsDAO, oauthUsersDao: OauthUsersDAO, clientsDao: ClientsDAO, agenciesDao: AgenciesDAO, applicationsDao: ApplicationsDAO, oauthClientsForm:OauthClientsForm, oauthClientsSearchForm:OauthClientsSearchForm, val messagesApi: MessagesApi) extends Controller with AuthActionBuilders with AuthConfigAdminImpl with I18nSupport  {
  val UserAccountSv = userAccountService

   val pageOffset = PageNation.defaultMaxPageNum
  
  /** This result directly redirect to the application home.*/
  val Home = Redirect(controllers.admin.routes.OauthClientController.index(0, 1))

  
  
  def index(applicationId: Long, page: Int = 1) = addToken{
    AuthorizationAction(Administrator).async { implicit request =>
     if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
       Future(Status(404)(views.html.errors.error404notfound("no found")))
     }else{
       val urlquery: Map[String,String] = request.queryString.map { case (k,v) => k -> v.mkString }
       oauthClientsSearchForm.form.bindFromRequest.fold(
          formWithErrors => {
            Logger.debug(formWithErrors.toString())
           val pagenationOptions = for {
               oauthuseroptions <- oauthUsersDao.getValidListForSelectOption(applicationId)
               agencyoptions <- agenciesDao.getValidListForSelectOption(applicationId)
               appoptions <- applicationsDao.getValidListForSelectOption()
            } yield (oauthuseroptions, agencyoptions, appoptions)
      
            pagenationOptions.map{case (oauthuseroptions, agencyoptions, appoptions) =>
               BadRequest(views.html.oauthclient.listerror("エラー", applicationId, formWithErrors, oauthuseroptions, agencyoptions, appoptions))
            }
          },
          oauthclientsearch => {
           val pagenationOptions = for {
               (pagecount,oauthclient) <- oauthClientsDao.paginglist(page, pageOffset, urlquery, applicationId)
               oauthuseroptions <- oauthUsersDao.getValidListForSelectOption(applicationId)
               agencyoptions <- agenciesDao.getValidListForSelectOption(applicationId)
               appoptions <- applicationsDao.getValidListForSelectOption()
            } yield ((pagecount,oauthclient), oauthuseroptions, agencyoptions, appoptions)
      
            pagenationOptions.map{case ((pagecount,oauthclient), oauthuseroptions, agencyoptions, appoptions) =>
                Ok(views.html.oauthclient.list("", applicationId, oauthclient.toList, oauthClientsSearchForm.form.fill(oauthclientsearch), oauthuseroptions, agencyoptions, appoptions, new PageNation(page, pagecount, pageOffset)))
      
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
        val oauthuserOptions = for {
           options <- oauthUsersDao.getValidListForSelectOption(applicationId)
        } yield (options)
        oauthuserOptions.map { case (options) =>
           Ok(views.html.oauthclient.regist("", applicationId, oauthClientsForm.form, options, currentuser))
        }
      }
    }
  }
  
  def create(applicationId: Long) = checkToken{
    AuthorizationAction(Administrator).async { implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        oauthClientsForm.form.bindFromRequest.fold(
            formWithErrors => {
             oauthUsersDao.getValidListForSelectOption(applicationId).map(options => 
                 BadRequest(views.html.oauthclient.regist("エラー", applicationId, formWithErrors, options, currentuser))
              )
            },
            oauthclient => {
              oauthClientsDao.create(oauthclient).flatMap(cnt =>
                if (cnt != 0) Future.successful(Redirect(controllers.admin.routes.OauthClientController.index(applicationId, 1)))
                else oauthUsersDao.getValidListForSelectOption(applicationId).map( options  => BadRequest(views.html.oauthclient.regist("エラー", applicationId, oauthClientsForm.form.fill(oauthclient), options, currentuser)))
               )
            }
        )
      }
    }
  }
   
  def edit(applicationId: Long, oauthClientId: String) = addToken{
    AuthorizationAction(Administrator).async {implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        val oauthclientOptions = for {
           oauthclient <- oauthClientsDao.findById(java.util.UUID.fromString(oauthClientId))
           options <- oauthUsersDao.getValidListForSelectOption(applicationId)
        } yield (oauthclient, options)
        oauthclientOptions.map { case (oauthclient, options) =>
          oauthclient match {
            case Some(oauthclient) => Ok(views.html.oauthclient.edit("GET", applicationId, oauthClientsForm.form.fill(oauthclient), options, currentuser))
            case _ => (Status(404)(views.html.errors.error404notfound("no found")))
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
        oauthClientsForm.form.bindFromRequest.fold(
          formWithErrors => {
            oauthUsersDao.getValidListForSelectOption(applicationId).map(options => 
              BadRequest(views.html.oauthclient.edit("エラー2", applicationId, formWithErrors, options, currentuser))
            )
          },
          oauthclient => {
            oauthClientsDao.update_mappinged(oauthclient).flatMap(cnt =>
              if (cnt != 0) Future.successful(Redirect(controllers.admin.routes.OauthClientController.index(applicationId, 1)))
              else 
                oauthUsersDao.getValidListForSelectOption(applicationId).map(options => 
                  BadRequest(views.html.oauthclient.edit("エラー3", applicationId, oauthClientsForm.form.fill(oauthclient), options, currentuser))
                )
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
        val currentuser: User = request.user
        oauthClientsDao.delete(java.util.UUID.fromString(id)).flatMap(cnt =>
          if (cnt != 0) Future.successful(Redirect(controllers.admin.routes.OauthClientController.index(applicationId, 1)))
          else Future.successful(Status(500)(views.html.errors.error500internalerror("error")))
        )
      }
    }
  }

  def pagenation(applicationId: Long, currentPageNum: Int, pageName: String) = AuthorizationAction(NormalUser) { implicit request =>
    pageName match {
      case "index" => Redirect(ViewHelper.addRequestQuery(controllers.admin.routes.OauthClientController.index(applicationId: Long,currentPageNum), request))
      case _       => Redirect(ViewHelper.addRequestQuery(controllers.admin.routes.OauthClientController.index(applicationId: Long,currentPageNum), request))
    }
  }


}