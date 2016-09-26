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

class ContractDetailController @Inject()(addToken: CSRFAddToken, checkToken: CSRFCheck, cache: CacheApi, cached: Cached, val userAccountService: UserAccountServiceLike, contractDetailsDao: ContractDetailsDAO, contractDetailPricesDao: ContractDetailPricesDAO, contractsDao: ContractsDAO, clientsDao: ClientsDAO, agenciesDao: AgenciesDAO, applicationsDao: ApplicationsDAO, defaultProductsDAO: DefaultProductsDAO, ContractDetailsContractDetailPricesForm:ContractDetailsContractDetailPricesForm, val messagesApi: MessagesApi) extends Controller with AuthActionBuilders with AuthElement with AuthConfigAdminImpl with I18nSupport  {
  
  val UserAccountSv = userAccountService
  
  val pageOffset = PageNation.defaultMaxPageNum
  
  implicit val defaultproductWrites = new Writes[DefaultProductsRow] {
  def writes(defaultproduct: DefaultProductsRow) = Json.obj(
    "id" -> defaultproduct.id,
    "agencyId" -> defaultproduct.agencyId,
    "productName" -> defaultproduct.productName,
    "productType" -> defaultproduct.productType,
    "price" -> defaultproduct.price  )
  }
  
 
  def add(applicationId: Long, agencyId: Long, clientId: Long) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false || Await.result(clientsDao.checkExists(clientId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else if(Await.result(clientsDao.isBelongApplicationAgency(clientId, applicationId, Option(agencyId)), Duration.Inf) == false){
         Future(Status(404)(views.html.errors.error404notfound("no user")))
      }else{
        val currentuser: User = request.user
        val contractDetailOptions = for {
            contract <- contractsDao.findByClientId(clientId)
            defaultproductlist <- defaultProductsDAO.getValidListForSelectOption(agencyId)
        } yield (contract, defaultproductlist)
        contractDetailOptions.map { case (contract, defaultproductlist) =>
          contract match {
            case Some(contract) => Ok(views.html.contractdetail.regist("", applicationId, agencyId, clientId, contract.id, ContractDetailsContractDetailPricesForm.form, defaultproductlist, currentuser))
            case _ => (Status(404)(views.html.errors.error404notfound("no found")))
          }
        }
      }
    }
  }

  def create(applicationId: Long, agencyId: Long, clientId: Long) = checkToken{
    AuthorizationAction(NormalUser).async { implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false || Await.result(clientsDao.checkExists(clientId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else if(Await.result(clientsDao.isBelongApplicationAgency(clientId, applicationId, Option(agencyId)), Duration.Inf) == false){
         Future(Status(404)(views.html.errors.error404notfound("no user")))
      }else{
        val currentuser: User = request.user
        ContractDetailsContractDetailPricesForm.form.bindFromRequest.fold(
            formWithErrors => {
              Logger.debug(formWithErrors.toString())
              val contractDetailOptions = for {
                  contract <- contractsDao.findByClientId(clientId)
                  defaultproductlist <- defaultProductsDAO.getValidListForSelectOption(agencyId)
              } yield (contract, defaultproductlist)
              contractDetailOptions.map { case (contract, defaultproductlist) =>
                contract match {
                  case Some(contract) => 
                    BadRequest(views.html.contractdetail.regist("エラー", applicationId, agencyId, clientId, contract.id, formWithErrors, defaultproductlist, currentuser))
                  case _ => (Status(404)(views.html.errors.error404notfound("no found")))
                }
              }
            },
            contractDetailsContractDetailPrice => {
              contractDetailsDao.create(contractDetailsContractDetailPrice).flatMap(cnt =>
                  //if (cnt != 0) agenciesDao.all().map(agencies => Ok(views.html.agency.list("登録しました", agencies)))
                  if (cnt != 0) {
                   Future.successful(Redirect(controllers.admin.routes.ClientController.view(applicationId, agencyId, clientId)))
                  }else{
                    val contractDetailOptions = for {
                        contract <- contractsDao.findByClientId(clientId)
                        defaultproductlist <- defaultProductsDAO.getValidListForSelectOption(agencyId)
                    } yield (contract, defaultproductlist)
                    contractDetailOptions.map { case (contract, defaultproductlist) =>
                      contract match {
                        case Some(contract) => 
                          BadRequest(views.html.contractdetail.regist("エラー", applicationId, agencyId, clientId, contract.id, ContractDetailsContractDetailPricesForm.form.fill(contractDetailsContractDetailPrice), defaultproductlist, currentuser))
                        case _ => (Status(404)(views.html.errors.error404notfound("no found")))
                      }
                    }
                  }
               )
            }
        )
      }
    }
  }
   

  def edit(applicationId: Long, agencyId: Long, clientId: Long, contractDetailId: Long) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false || Await.result(clientsDao.checkExists(clientId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else if(Await.result(clientsDao.isBelongApplicationAgency(clientId, applicationId, Option(agencyId)), Duration.Inf) == false){
         Future(Status(404)(views.html.errors.error404notfound("no user")))
      }else{
        val currentuser: User = request.user
         val contractDetailOptions = for {
            contractdetail <- contractDetailsDao.findById(contractDetailId)
            contractdetailprice <- contractDetailPricesDao.findById(contractDetailId)
            defaultproductlist <- defaultProductsDAO.getValidListForSelectOption(agencyId)
        } yield (contractdetail, contractdetailprice, defaultproductlist)
        contractDetailOptions.map { case (contractdetail, contractdetailprice, defaultproductlist) =>
          contractdetail match {
            case Some(contractdetail) => Ok(views.html.contractdetail.edit("", applicationId, agencyId, clientId, contractdetail.contractId, ContractDetailsContractDetailPricesForm.form.fill(ContractDetailsContractDetailPricesForm.createContractDetailsContractDetailPricesRow(contractdetail, contractdetailprice.getOrElse(null))), defaultproductlist, currentuser))
            case _ => (Status(404)(views.html.errors.error404notfound("no found")))
          }
        }
      }
    }
  }

  def update(applicationId: Long, agencyId: Long, clientId: Long) = checkToken {
    AuthorizationAction(NormalUser).async { implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false || Await.result(clientsDao.checkExists(clientId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else if(Await.result(clientsDao.isBelongApplicationAgency(clientId, applicationId, Option(agencyId)), Duration.Inf) == false){
         Future(Status(404)(views.html.errors.error404notfound("no user")))
      }else{
        val currentuser: User = request.user
        ContractDetailsContractDetailPricesForm.form.bindFromRequest.fold(
            formWithErrors => {
              Logger.debug(formWithErrors.toString())
              val contractDetailOptions = for {
                  contract <- contractsDao.findByClientId(clientId)
                  defaultproductlist <- defaultProductsDAO.getValidListForSelectOption(agencyId)
              } yield (contract, defaultproductlist)
              contractDetailOptions.map { case (contract, defaultproductlist) =>
                contract match {
                  case Some(contract) => 
                    BadRequest(views.html.contractdetail.edit("エラー", applicationId, agencyId, clientId, contract.id, formWithErrors, defaultproductlist, currentuser))
                  case _ => (Status(404)(views.html.errors.error404notfound("no found")))
                }
              }
            },
            contractDetailsContractDetailPrice => {
              contractDetailsDao.update_mappinged(contractDetailsContractDetailPrice).flatMap(cnt =>
                if (cnt != 0){
                  Future.successful(Redirect(controllers.admin.routes.ClientController.view(applicationId, agencyId, clientId)))
                }else{
                  val contractDetailOptions = for {
                      contract <- contractsDao.findByClientId(clientId)
                      defaultproductlist <- defaultProductsDAO.getValidListForSelectOption(agencyId)
                  } yield (contract, defaultproductlist)
                  contractDetailOptions.map { case (contract, defaultproductlist) =>
                    contract match {
                      case Some(contract) => 
                        BadRequest(views.html.contractdetail.edit("エラー", applicationId, agencyId, clientId, contract.id, ContractDetailsContractDetailPricesForm.form.fill(contractDetailsContractDetailPrice), defaultproductlist, currentuser))
                      case _ => (Status(404)(views.html.errors.error404notfound("no found")))
                    }
                  }
                }
              )
            }
        )
      }
    }
  }

  def delete(applicationId: Long, agencyId: Long, clientId: Long, id: Long) = checkToken{
    AuthorizationAction(NormalUser).async { implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false || Await.result(clientsDao.checkExists(clientId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else if(Await.result(clientsDao.isBelongApplicationAgency(clientId, applicationId, Option(agencyId)), Duration.Inf) == false){
         Future(Status(404)(views.html.errors.error404notfound("no user")))
      }else{
        val currentuser = request.user
        contractDetailsDao.disabled(id, currentuser.id).flatMap(cnt =>
          if (cnt != 0) Future.successful(Redirect(controllers.admin.routes.ClientController.view(applicationId, agencyId, clientId)))
          else Future.successful(Status(500)(views.html.errors.error500internalerror("error")))
        )
      }
    }
  }

  def getDefaultProductWithSelected(agencyId: Long, defaultProductId: Long = 0) = checkToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      val selectedApplicationId = cache.get[Long](AppSelectorConfig.AppSelectorCacheKey + "-" + request.session.get("uuid"))
         selectedApplicationId match {
          case Some(selectedApplicationId) => 
            if(Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false){
              Future(Status(404)(views.html.errors.error404notfound("no found")))
            }else if(Await.result(agenciesDao.isBelongApplication(agencyId, selectedApplicationId), Duration.Inf) == false){
               Future(Status(404)(views.html.errors.error404notfound("no agency")))
            }else{
              defaultProductsDAO.findByIdAndAgencyId(defaultProductId, agencyId).flatMap(defaultproduct =>
                defaultproduct match {
                  case Some(defaultproduct) =>  Future(Ok(Json.toJson(defaultproduct)))
                  case None => Future(Ok(Json.toJson("")))
                }
              )
            }
          case _ => Future(Ok(Json.toJson("")))
      }
    }
  }

}