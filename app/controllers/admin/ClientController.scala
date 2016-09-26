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

class ClientController @Inject()(addToken: CSRFAddToken, checkToken: CSRFCheck, cache: CacheApi, cached: Cached, val userAccountService: UserAccountServiceLike, 
    clientsDao: ClientsDAO, 
    agenciesDao: AgenciesDAO, 
    applicationsDao: ApplicationsDAO, 
    contractsDao: ContractsDAO, 
    contractAddressesDao: ContractAddressesDAO, 
    contractDetailsDao: ContractDetailsDAO,
    contractDetailBillingsDao: ContractDetailBillingsDAO,
    billingDestinationsDao: BillingDestinationsDAO, 
    ClientsForm: ClientsForm, 
    ClientsContractsForm: ClientsContractsForm, 
    ClientsSearchForm: ClientsSearchForm, 
    val messagesApi: MessagesApi) extends Controller with AuthActionBuilders with AuthElement with AuthConfigAdminImpl with I18nSupport  {
  
  val UserAccountSv = userAccountService
  
  val pageOffset = PageNation.defaultMaxPageNum
  
  
  /** This result directly redirect to the agency home.*/
  val Home = Redirect(controllers.admin.routes.ClientController.index(0, 1))

  
  def index(applicationId: Long, page: Int = 1) = addToken{
   AuthorizationAction(NormalUser).async {implicit request =>
     if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
       Future(Status(404)(views.html.errors.error404notfound("no found")))
     }else{
       val urlquery: Map[String,String] = request.queryString.map { case (k,v) => k -> v.mkString }
       ClientsSearchForm.form.bindFromRequest.fold(
          formWithErrors => {
            Logger.debug(formWithErrors.toString())
            val errorptions = for {
               agencyoptions <- agenciesDao.getValidListForSelectOption(applicationId)
               appoptions <- applicationsDao.getValidListForSelectOption()
             } yield (agencyoptions, appoptions)
            errorptions.map{case (agencyoptions, appoptions) => 
               BadRequest(views.html.client.listerror("エラー", applicationId, formWithErrors, agencyoptions, appoptions))
             }
          },
          clientsearch => {
           val pagenationOptions = for {
               (pagecount,clients) <- clientsDao.paginglist(page, pageOffset, urlquery, applicationId)
               agencyoptions <- agenciesDao.getValidListForSelectOption(applicationId)
               appoptions <- applicationsDao.getValidListForSelectOption()
             } yield ((pagecount,clients), agencyoptions, appoptions)
      
            pagenationOptions.map{case ((pagecount,clients), agencyoptions, appoptions) =>
                Ok(views.html.client.list("", applicationId, clients.toList, ClientsSearchForm.form.fill(clientsearch), agencyoptions, appoptions, new PageNation(page, pagecount, pageOffset)))
      
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
            options <- agenciesDao.getValidListForSelectOption(applicationId)
        } yield (options)
  
        optionsList.map{case (options) =>
          Ok(views.html.client.regist("", applicationId, ClientsContractsForm.form, options, currentuser))
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
        ClientsContractsForm.form.bindFromRequest.fold(
            formWithErrors => {
              Logger.debug(formWithErrors.toString())
               val optionsList = for {
                     options <- agenciesDao.getValidListForSelectOption(applicationId)
               } yield (options)
  
               optionsList.map{case (options) =>

                 BadRequest(views.html.client.regist("エラー", applicationId, formWithErrors, options, currentuser))
               }
             },
            clientcontract => {
              clientsDao.create(clientcontract).flatMap(cnt =>
                  //if (cnt != 0) agenciesDao.all().map(agencies => Ok(views.html.client.list("登録しました", agencies)))
                  if (cnt != 0) {
                    Future.successful(Redirect(controllers.admin.routes.ClientController.index(clientcontract.applicationId, 1)))
                  }else{
                    val optionsListerror = for {
                          options <- agenciesDao.getValidListForSelectOption(applicationId)
                     } yield (options)
        
                     optionsListerror.map{case (options) =>
                        BadRequest(views.html.client.regist("エラー", applicationId, ClientsContractsForm.form.fill(clientcontract), options, currentuser))
                     }
                  }
               )
            }
        )
      }
    }
  }
   
  def view(applicationId: Long, agencyId: Long, clientId: Long) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        val clientOptions = for {
            client <- clientsDao.findByIdWithAppAgencyId(clientId, applicationId, agencyId)
            contract <- contractsDao.findByClientId(clientId)
            contractaddress <- contractAddressesDao.findById(contract.map(c => c.id).getOrElse(-1))
            billings <- billingDestinationsDao.findByContractIdWithAddress(contract.map(c => c.id).getOrElse(-1))
            contractdetailwithprices <- contractDetailsDao.findByContractIdWithPrices(contract.map(c => c.id).getOrElse(-1))
         } yield (client, contract, contractaddress, billings, contractdetailwithprices)
        clientOptions.map { case (client, contract, contractaddress, billings, contractdetailwithprices) =>
          client match {
            case Some(client) => Ok(views.html.client.view("GET", applicationId, agencyId, client, contract, contractaddress, billings, contractdetailwithprices, currentuser))
            case _ => (Status(404)(views.html.errors.error404notfound("no found")))
          }
        }
      }
    }
  }

  def edit(applicationId: Long, agencyId: Long, clientId: Long) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
     if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        clientsDao.findByIdWithAppAgencyId(clientId, applicationId, agencyId).flatMap(clientresult =>
          clientresult match {
            case Some(client) => Future(Ok(views.html.client.edit("GET", applicationId, agencyId, ClientsForm.form.fill(client), currentuser)))
            case None => Future.successful(Status(404)(views.html.errors.error404notfound("no found")))
          }
        )
      }
    }
  }

  def update(applicationId: Long, agencyId: Long) = checkToken {
    AuthorizationAction(NormalUser).async { implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        ClientsForm.form.bindFromRequest.fold(
            formWithErrors => {
              Future(BadRequest(views.html.client.edit("ERROR", applicationId, agencyId, formWithErrors, currentuser)))
            },
            client => {
              clientsDao.update_mappinged(client).flatMap(cnt =>
                if (cnt != 0) Future.successful(Redirect(controllers.admin.routes.ClientController.view(applicationId, agencyId, client.id)))
                else applicationsDao.all().map(notifications => BadRequest(views.html.client.edit("エラー", applicationId, agencyId, ClientsForm.form.fill(client), currentuser)))
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
        clientsDao.disable(id, currentuser.id, applicationId).flatMap(cnt =>
          if (cnt != 0) Future.successful(Redirect(controllers.admin.routes.ClientController.index(applicationId, 1)))
          else Future.successful(Status(500)(views.html.errors.error500internalerror("error")))
        )
      }
    }
  }
    
  def nameautocomplete(applicationId: Long, name: String) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      clientsDao.findByNameList(name, applicationId).map{case agency =>
        Ok(Json.toJson(agency))
      }
    }
  }

  def getValidCount = checkToken {
    AuthorizationAction(NormalUser).async { implicit request =>
       clientsDao.count().map{case cuunt =>
        Ok(Json.toJson(cuunt))
      }
     
    }
  }
    def getClientListByAgenyWithSelected(agencyId: Long = 0) = checkToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      if(Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val selectedApplicationId = cache.get[Long](AppSelectorConfig.AppSelectorCacheKey + "-" + request.session.get("uuid"))
         selectedApplicationId match {
          case Some(selectedApplicationId) => 
             clientsDao.getValidListMap(selectedApplicationId, agencyId).map{case list =>
              Ok(Json.toJson(list))
            }
          case _ => Future(Ok(Json.toJson("")))
        }
      }
    }
  }
 
  def pagenation(applicationId: Long, currentPageNum: Int, pageName: String) = AuthorizationAction(NormalUser) { implicit request =>
    pageName match {
      case "index" => Redirect(controllers.admin.routes.ClientController.index(applicationId, currentPageNum))
      case _       => Redirect(controllers.admin.routes.ClientController.index(applicationId, currentPageNum))
    }
  }
}