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

class AgencyController @Inject()(addToken: CSRFAddToken, checkToken: CSRFCheck, cache: CacheApi, cached: Cached, val userAccountService: UserAccountServiceLike, 
    agenciesDao: AgenciesDAO, 
    applicationsDao: ApplicationsDAO, 
    applicationAgencyDao: ApplicationAgencyDAO, 
    timezonesDao: TimezonesDAO,
    billingDestinationsDao: BillingDestinationsDAO,
    defaultProductsDAO: DefaultProductsDAO,
    AgenciesForm:AgenciesForm, 
    AgenciesApplicationAgencyForm:AgenciesApplicationAgencyForm, 
    AgenciesSearchForm:AgenciesSearchForm, 
    val messagesApi: MessagesApi) extends Controller with AuthActionBuilders with AuthElement with AuthConfigAdminImpl with I18nSupport  {
  
  val UserAccountSv = userAccountService
  
  val pageOffset = PageNation.defaultMaxPageNum
  
  
  /** This result directly redirect to the agency home.*/
  val Home = Redirect(controllers.admin.routes.AgencyController.index(0, 1))

  
  def index(applicationId: Long, page: Int = 1) = addToken{
   AuthorizationAction(NormalUser).async {implicit request =>
     if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
       Future(Status(404)(views.html.errors.error404notfound("no found")))
     }else{
       val urlquery: Map[String,String] = request.queryString.map { case (k,v) => k -> v.mkString }
       AgenciesSearchForm.form.bindFromRequest.fold(
          formWithErrors => {
            Logger.debug(formWithErrors.toString())
            applicationsDao.getValidListForSelectOption().map(options => 
               BadRequest(views.html.agency.listerror("エラー", applicationId, formWithErrors, options))
            )
          },
          agencysearch => {
           val pagenationOptions = for {
               (pagecount,agencies) <- agenciesDao.paginglist(page, pageOffset, urlquery, applicationId)
               options <- applicationsDao.getValidListForSelectOption()
               timezoneoption <- timezonesDao.getValidListForSelectOption()
            } yield ((pagecount,agencies), options, timezoneoption)
      
            pagenationOptions.map{case ((pagecount,agencies), options, timezoneoption) =>
                Ok(views.html.agency.list("", applicationId, agencies.toList, AgenciesSearchForm.form.fill(agencysearch), options, timezoneoption, new PageNation(page, pagecount, pageOffset)))
      
            }
          }
       )
     }
   }
  }

  def add(applicationId: Long) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        val optionsList = for {
            options <- applicationsDao.getValidListForSelectOption()
            timezoneoption <- timezonesDao.getValidListForSelectOption()
        } yield (options,timezoneoption)
  
        optionsList.map{case (options,timezoneoption) =>
          Ok(views.html.agency.regist("", applicationId, AgenciesApplicationAgencyForm.form, options, timezoneoption, currentuser))
        }
      }
    }
  }
  
  def create(applicationId: Long) = checkToken{
    AuthorizationAction(NormalUser).async { implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        AgenciesApplicationAgencyForm.form.bindFromRequest.fold(
            formWithErrors => {
              Logger.debug(formWithErrors.toString())
               val optionsList = for {
                    options <- applicationsDao.getValidListForSelectOption()
                     timezoneoption <- timezonesDao.getValidListForSelectOption()
               } yield (options,timezoneoption)
  
               optionsList.map{case (options,timezoneoption) =>
                 BadRequest(views.html.agency.regist("エラー", applicationId, formWithErrors, options, timezoneoption, currentuser))
               }
             },
            agency => {
              agenciesDao.create(agency).flatMap(cnt =>
                  //if (cnt != 0) agenciesDao.all().map(agencies => Ok(views.html.agency.list("登録しました", agencies)))
                  if (cnt != 0) {
                    Future.successful(Redirect(controllers.admin.routes.AgencyController.index(agency.applicationAgency.applicationId, 1)))
                  }else{
                    val optionsListerror = for {
                          options <- applicationsDao.getValidListForSelectOption()
                           timezoneoption <- timezonesDao.getValidListForSelectOption()
                     } yield (options,timezoneoption)
        
                     optionsListerror.map{case (options,timezoneoption) =>
                        BadRequest(views.html.agency.regist("エラー", applicationId, AgenciesApplicationAgencyForm.form.fill(agency), options, timezoneoption, currentuser))
                     }
                  }
               )
            }
        )
      }
    }
  }
   
  def view(applicationId: Long, agencyId: Long) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        val agencyOptions = for {
            agency <- agenciesDao.findByIdWithApplicationId(agencyId, applicationId)
            applicationagency <- applicationAgencyDao.findByAgencyIdhasOne(agencyId)
            billingdestinations <- billingDestinationsDao.findByAgencyId(agencyId)
            defaultproducts <- defaultProductsDAO.findByAgencyId(agencyId)
            options <- applicationsDao.getValidListForSelectOption()
            timezoneoption <- timezonesDao.getValidListForSelectOption()
        } yield (agency, applicationagency, billingdestinations, defaultproducts, options, timezoneoption)
        agencyOptions.map { case (agency, applicationagency, billingdestinations, defaultproducts, options, timezoneoption) =>
          agency match {
            case Some(agency) => Ok(views.html.agency.view("GET", applicationId, agency, billingdestinations, defaultproducts, options, timezoneoption, currentuser))
            case _ => (Status(404)(views.html.errors.error404notfound("no found")))
          }
        }
      }
    }
  }

  def edit(applicationId: Long, agencyId: Long) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        val agencyOptions = for {
            agency <- agenciesDao.findById(agencyId)
            applicationagency <- applicationAgencyDao.findByAgencyIdhasOne(agencyId)
            options <- applicationsDao.getValidListForSelectOption()
            timezoneoption <- timezonesDao.getValidListForSelectOption()
        } yield (agency, applicationagency, options, timezoneoption)
        agencyOptions.map { case (agency, applicationagency, options, timezoneoption) =>
          agency match {
            case Some(agency) => Ok(views.html.agency.edit("GET", applicationId, AgenciesApplicationAgencyForm.form.fill(AgenciesApplicationAgencyForm.createAgenciesApplicationAgencyRow(agency, applicationagency.getOrElse(null))), options, timezoneoption, currentuser))
            case _ => (Status(404)(views.html.errors.error404notfound("no found")))
          }
        }
      }
    }
  }

  def update(applicationId: Long) = checkToken {
    AuthorizationAction(NormalUser).async { implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        AgenciesApplicationAgencyForm.form.bindFromRequest.fold(
            formWithErrors => {
               val optionsList = for {
                    options <- applicationsDao.getValidListForSelectOption()
                     timezoneoption <- timezonesDao.getValidListForSelectOption()
               } yield (options,timezoneoption)
  
               optionsList.map{case (options,timezoneoption) =>
                 BadRequest(views.html.agency.edit("エラー", applicationId, formWithErrors, options, timezoneoption, currentuser))
               }
            },
            agencyapplicationagency => {
              agenciesDao.update_mappinged(agencyapplicationagency).flatMap(cnt =>
                if (cnt != 0){
                  Future.successful(Redirect(controllers.admin.routes.AgencyController.view(agencyapplicationagency.applicationAgency.applicationId, agencyapplicationagency.id)))
                }else{
                    val optionsListerror = for {
                          options <- applicationsDao.getValidListForSelectOption()
                           timezoneoption <- timezonesDao.getValidListForSelectOption()
                     } yield (options,timezoneoption)
        
                     optionsListerror.map{case (options,timezoneoption) =>
                        BadRequest(views.html.agency.edit("エラー", applicationId, AgenciesApplicationAgencyForm.form.fill(agencyapplicationagency), options, timezoneoption, currentuser))
                     }
                 }
              )
            }
        )
      }
    }
  }
  
  def delete(applicationId: Long, id: Long) = checkToken{
    AuthorizationAction(NormalUser).async { implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser = request.user
        cache.remove(FormConfig.FormCacheKey)
        agenciesDao.disable(id, currentuser.id, applicationId).flatMap(cnt =>
          if (cnt != 0) Future.successful(Redirect(controllers.admin.routes.AgencyController.index(applicationId, 1)))
          else Future.successful(Status(500)(views.html.errors.error500internalerror("error")))
        )
      }
    }
  }
    
  def nameautocomplete(applicationId: Long, name: String) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      agenciesDao.findByNameList(name, applicationId).map{case agency =>
        Ok(Json.toJson(agency))
      }
    }
  }

  def getValidCount = checkToken {
    AuthorizationAction(NormalUser).async { implicit request =>
       agenciesDao.count().map{case cuunt =>
        Ok(Json.toJson(cuunt))
      }
     
    }
  }
  
  def pagenation(applicationId: Long, currentPageNum: Int, pageName: String) = AuthorizationAction(NormalUser) { implicit request =>
    pageName match {
      case "index" => Redirect(controllers.admin.routes.AgencyController.index(applicationId, currentPageNum))
      case _       => Redirect(controllers.admin.routes.AgencyController.index(applicationId, currentPageNum))
    }
  }
}