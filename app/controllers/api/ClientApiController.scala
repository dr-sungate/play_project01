package controllers.api

import javax.inject.Inject

import play.api.mvc._
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.libs.json.{Json, Writes}
import play.api.Logger
import play.api.i18n.MessagesApi

import models.TablesExtend._
import models.JsonResponse._
import services.{Oauth2DataHandler, MyTokenEndpoint}
import services.dao._
import forms._

import scala.concurrent.Future
import scalaoauth2.provider._
import scalaoauth2.provider.OAuth2ProviderActionBuilders._

import play.api.libs.json._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration

import utilities._
import utilities.api._

class ClientpiController @Inject()(oauth2DataHandler: Oauth2DataHandler,
    clientsDao: ClientsDAO, clientsDto: ClientsDTO, agenciesDao: AgenciesDAO, applicationsDao: ApplicationsDAO,  clientsContractsApiRequestForm: ClientsContractsApiRequestForm, val messagesApi: MessagesApi) extends Controller with OAuth2Provider {
  
  def view(clientId: Long) =  Action.async { implicit request =>
    authorize(oauth2DataHandler) { authInfo =>
      val accessuser = authInfo.user 
      if((!accessuser.clientId.isEmpty && accessuser.clientId.getOrElse(0) != clientId)){
        Future(Ok(Json.toJson(clientsDto.responseError("許可されてない閲覧"))))
      }else{
        accessuser.agencyId match{
          case Some(agencyId) => 
            clientsDao.findByIdWithAppAgencyId(clientId, accessuser.applicationId, agencyId).flatMap { client => 
              client match{
                case Some(client) => clientsDto.responseSuccess(client).map{case res => Ok(Json.toJson(res))}
                case _ => Future(Ok(Json.toJson(clientsDto.responseError("存在しないユーザー"))))
              }
              
            }
          case _ =>
            clientsDao.findByIdWithAppId(clientId, accessuser.applicationId).flatMap { client => 
              client match{
                case Some(client) => clientsDto.responseSuccess(client).map{case res => Ok(Json.toJson(res))}
                case _ => Future(Ok(Json.toJson(clientsDto.responseError("存在しないユーザー"))))
              }
              
            }
         }
       }
     
    }
  }
  def lists =  Action.async { implicit request =>
    authorize(oauth2DataHandler) { authInfo =>
      val accessuser = authInfo.user 
      
      if(!accessuser.clientId.isEmpty){
        clientsDao.findByIdWithAppId(accessuser.clientId.getOrElse(0), accessuser.applicationId).flatMap { client => 
          client match{
            case Some(client) => clientsDto.responseSuccess(client).map{case res => Ok(Json.toJson(res))}
            case _ => Future(Ok(Json.toJson(clientsDto.responseError("存在しないユーザー"))))
          }
        }
      }else{
        accessuser.agencyId match{
          case Some(agencyId) => 
            clientsDao.findByAppIdAgencyId(accessuser.applicationId, agencyId).flatMap { clients => 
               clientsDto.responseSuccess(clients).map{case res => Ok(Json.toJson(res))}
            }
          case _ => 
            clientsDao.findByAppId(accessuser.applicationId).flatMap { clients => 
              clientsDto.responseSuccess(clients).map{case res => Ok(Json.toJson(res))}
          }
        }
      }
     
    }
  }
  
  def create =  Action.async { implicit request =>
    authorize(oauth2DataHandler) { authInfo =>
      val accessuser = authInfo.user 
      
      if(!accessuser.clientId.isEmpty){
        Future(Ok(Json.toJson(clientsDto.responseError("許可されてない登録-clientID"))))
      }else{
        var formbindrequest = clientsContractsApiRequestForm.form.bindFromRequest
        //AgencyID がセットされている接続ユーザーの場合はバリデーションセット
        if(!accessuser.agencyId.isEmpty){
          formbindrequest = clientsContractsApiRequestForm.form_noagencyid.bindFromRequest
        }
        formbindrequest.fold(
            formWithErrors => {
                 Logger.debug(formWithErrors.errors.map(formError => ValidationHelper.formErrorToMap(formError, messagesApi)).toSeq.toString())
                 Future(Ok(Json.toJson(clientsDto.responseError("バリデーションエラー", Option(formWithErrors.errors.map(formError => ValidationHelper.formErrorToMap(formError, messagesApi)).toSeq)))))
             },
            clientcontract => {
              if(accessuser.agencyId.isEmpty && Await.result(agenciesDao.isBelongApplication(clientcontract.agencyId,  accessuser.applicationId), Duration.Inf) == false){
                 Future(Ok(Json.toJson(clientsDto.responseError("バリデーションエラー",  Option(Seq(Map("agencyId"-> "存在しない代理店")))))))
              }else{
                clientsDao.create(clientcontract, accessuser.applicationId, accessuser.agencyId).flatMap(cnt =>
                  if (cnt != 0) {
                    Future(Ok(Json.toJson(clientsDto.responseSuccess("登録成功", Option(Seq(Map("clientId"-> cnt.toString())))))))
                  }else{
                    Future(Ok(Json.toJson(clientsDto.responseError("登録エラー"))))
                  }
                )
              }
            }
        )
      }
    }
  }
  def update =  Action.async { implicit request =>
    authorize(oauth2DataHandler) { authInfo =>
      val accessuser = authInfo.user 
      
      if(!accessuser.clientId.isEmpty){
        Future(Ok(Json.toJson(clientsDto.responseError("許可されてない更新-clientID"))))
      }else{
        clientsContractsApiRequestForm.form_update.bindFromRequest.fold(
            formWithErrors => {
                 Logger.debug(formWithErrors.errors.map(formError => ValidationHelper.formErrorToMap(formError, messagesApi)).toSeq.toString())
                 Future(Ok(Json.toJson(clientsDto.responseError("バリデーションエラー", Option(formWithErrors.errors.map(formError => ValidationHelper.formErrorToMap(formError, messagesApi)).toSeq)))))
             },
            clientcontract => {
              if(Await.result(clientsDao.isBelongApplicationAgency(clientcontract.id, accessuser.applicationId, accessuser.agencyId), Duration.Inf) == false){
                 Future(Ok(Json.toJson(clientsDto.responseError("バリデーションエラー",  Option(Seq(Map("id"-> "存在しないor更新許可されていないユーザー")))))))
              }else{
                clientsDao.update_mappinged(clientcontract, accessuser.applicationId).flatMap(cnt =>
                  if (cnt != 0) {
                    Future(Ok(Json.toJson(clientsDto.responseSuccess("更新成功", Option(Seq(Map("clientId"->clientcontract.id.toString())))))))
                  }else{
                    Future(Ok(Json.toJson(clientsDto.responseError("更新エラー"))))
                  }
                )
              }
            }
        )
      }
    }
  }
}