package services.dao

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import scala.collection.mutable.ArrayBuffer

import javax.inject.Inject
import javax.inject.Singleton
import models.TablesExtend._
import models.JsonResponse._

import play.api._
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import play.api.i18n.Messages


import play.api.libs.json._
import utilities.api._
import utilities._



import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration


class ClientsDTO @Inject()(clientsDao: ClientsDAO, agenciesDao: AgenciesDAO, applicationsDao: ApplicationsDAO, contractsDao: ContractsDAO, billingDestinationsDao: BillingDestinationsDAO, val messagesApi: MessagesApi) extends BaseDTO[ClientsRow, SucccessClientsResponse] with I18nSupport{
  
  def responseSuccess(client: ClientsRow): Future[SucccessClientsResponse] ={
    this.buildClientsResponseByRecord(client).map{ case clientresponse => 
      new SucccessClientsResponse(ResponseCode.SuccessCode, "成功",  Option(List(clientresponse)), 1)
    }
  }

  def responseSuccess(clientlist: List[ClientsRow]): Future[SucccessClientsResponse] ={
    val clientresponse = clientlist.map( client => 
      Await.result(this.buildClientsResponseByRecord(client).map(clientresponse => clientresponse), Duration.Inf)
    )
    Future(new SucccessClientsResponse(ResponseCode.SuccessCode, "成功", Option(clientresponse), clientresponse.size))
  }
  
  def buildClientsResponseByRecord(client: ClientsRow): Future[ClientsResponse] = {
    var responsequery = (for{
      agency <- agenciesDao.findById(client.agencyId)
      application <- applicationsDao.findById(client.applicationId)
      contractlist <- contractsDao.findByClientIdWithAddress(client.id)
      billingDestinationslist <- billingDestinationsDao.findByClientIdWithAddress(client.id)
    }yield (agency, application, contractlist, billingDestinationslist))
    responsequery.map{ case (agency, application, contractlist, billingDestinationslist) => 
       this.makeClients(client, agency.get, application.get, this.makeContract(contractlist), this.makeBillingDestinations(billingDestinationslist))
    }
  }
  def responseSuccess(csuccessmassage: String, response: Option[Seq[Map[String, String]]]):SuccessResponse ={
    new SuccessResponse(ResponseCode.SuccessCode, csuccessmassage, response)
  }

  def responseError(errormessage: String): ErrorResponse ={
     new ErrorResponse(ResponseCode.ErrorCode, errormessage)
  }
  
  def responseError(errormessage: String, validateError: Option[Seq[Map[String, String]]]): ErrorResponse ={
     new ErrorResponse(ResponseCode.ErrorCode, errormessage, validateError)
  }

  def makeContract(contracttuple: Option[(ContractsRow, ContractAddressesRow)]) : Option[ContractsResponse] ={
    contracttuple match {
      case Some(contracttuple) =>
        Option(new ContractsResponse(Option(utilities.ViewHelper.dateFormat(contracttuple._1.registedDate, "%tY/%<tm/%<td %<tH:%<tM:%<tS")), Option(utilities.ViewHelper.dateFormat(contracttuple._1.activatedDate, "%tY/%<tm/%<td %<tH:%<tM:%<tS")), Option(utilities.ViewHelper.dateFormat(contracttuple._1.closeDate, "%tY/%<tm/%<td %<tH:%<tM:%<tS")), Option(utilities.ViewHelper.getOptionView(contracttuple._1.invoiceIssueType.getOrElse(""),utilities.ViewHelper.makeOptionMap(utilities.`type`.InvoiceIssueType.typeSeq, "view.billingdestination.invoiceissuetype", messagesApi))), contracttuple._1.memo, utilities.ViewHelper.getOptionView(contracttuple._1.status,utilities.ViewHelper.makeOptionMap(utilities.status.ContractStatus.statusSeq, "view.contract.status", messagesApi)), new ContractAddressesResponse(contracttuple._2.company, contracttuple._2.postCode, contracttuple._2.prefecture, contracttuple._2.city, contracttuple._2.address1, contracttuple._2.address2, contracttuple._2.address3, contracttuple._2.depertment, contracttuple._2.staff, contracttuple._2.staffEmail, contracttuple._2.phone, contracttuple._2.fax)))
      case _ => None
    }
  }
  def makeBillingDestinations(billingdestinationstuple:  Option[(BillingDestinationsRow, BillingAddressesRow)]) : Option[BillingDestinationsResponse] ={
    billingdestinationstuple match {
      case Some(billingdestinationstuple) =>
        Option(new BillingDestinationsResponse(billingdestinationstuple._1.billingDestinationName, Option(utilities.ViewHelper.getOptionView(billingdestinationstuple._1.invoiceIssueType.getOrElse(""),utilities.ViewHelper.makeOptionMap(utilities.`type`.InvoiceIssueType.typeSeq, "view.billingdestination.invoiceissuetype", messagesApi))), Option(utilities.ViewHelper.getOptionView(billingdestinationstuple._1.dueDateMonth.getOrElse(""),utilities.ViewHelper.makeOptionMap(utilities.`type`.DueDateMonthType.typeSeq, "view.billingdestination.duedatemonthtype", messagesApi))), Option(utilities.ViewHelper.getOptionView(billingdestinationstuple._1.dueDateDay.getOrElse(-1).toString(),utilities.ViewHelper.makeOptionMap(utilities.`type`.DueDateDayType.typeSeq, "view.billingdestination.duedatedaytype", messagesApi))), Option(utilities.ViewHelper.getOptionView(billingdestinationstuple._1.closingDate.getOrElse(-1).toString(),utilities.ViewHelper.makeOptionMap(utilities.`type`.ClosingDateType.typeSeq, "view.billingdestination.closingdatetype", messagesApi))), billingdestinationstuple._1.pcaCode, billingdestinationstuple._1.pcaCommonName, billingdestinationstuple._1.memo, new BillingAddressesResponse(billingdestinationstuple._2.company, billingdestinationstuple._2.postCode, billingdestinationstuple._2.prefecture, billingdestinationstuple._2.city, billingdestinationstuple._2.address1, billingdestinationstuple._2.address2, billingdestinationstuple._2.address3, billingdestinationstuple._2.depertment, billingdestinationstuple._2.staff, billingdestinationstuple._2.staffEmail, billingdestinationstuple._2.phone, billingdestinationstuple._2.fax)))
      case _ => None
    }
  }
  def makeClients(client: ClientsRow, agency: AgenciesRow, application: ApplicationsRow, contract: Option[ContractsResponse], billingdestination: Option[BillingDestinationsResponse]) : ClientsResponse ={
    new ClientsResponse(application.appName, agency.agencyName, client.appClientId, client.name, client.email, Option(utilities.ViewHelper.getOptionView(client.role.getOrElse(""),utilities.ViewHelper.makeOptionMap(utilities.`type`.ClientRoleType.typeSeq, "view.client.clientroletype", messagesApi))), Option(utilities.ViewHelper.dateFormat(client.appCreatedDate, "%tY/%<tm/%<td %<tH:%<tM:%<tS")), Option(utilities.ViewHelper.dateFormat(client.lastLoginDate, "%tY/%<tm/%<td %<tH:%<tM:%<tS")), client.lastLoginIpaddress, client.loginCount, client.memo, utilities.ViewHelper.getOptionView(client.status,utilities.ViewHelper.makeOptionMap(utilities.status.ClientStatus.statusSeq, "view.client.status", messagesApi)), contract, billingdestination)
  }
}

