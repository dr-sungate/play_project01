package models

import play.api.libs.json.Json
import java.util.Date


object JsonResponse extends {
} with JsonResponse

trait JsonResponse {
  case class ErrorResponse(responsecode:String, messages: String, errors: Option[Seq[Map[String, String]]] = None)
  object ErrorResponse {
    implicit def jsonWrites = Json.writes[ErrorResponse]
    implicit def jsonReads = Json.reads[ErrorResponse]
  }

  case class SuccessResponse(responsecode:String, messages: String, response: Option[Seq[Map[String, String]]] = None)
  object SuccessResponse {
    implicit def jsonWrites = Json.writes[SuccessResponse]
    implicit def jsonReads = Json.reads[SuccessResponse]
  }

  case class ContractAddressesResponse(company: Option[String] = None, postCode: Option[String] = None, prefecture: Option[String] = None, city: Option[String] = None, address1: Option[String] = None, address2: Option[String] = None, address3: Option[String] = None, depertment: Option[String] = None, staff: Option[String] = None, staffEmail: Option[String] = None, phone: Option[String] = None, fax: Option[String] = None)
  object ContractAddressesResponse {
    implicit def jsonWrites = Json.writes[ContractAddressesResponse]
    implicit def jsonReads = Json.reads[ContractAddressesResponse]
  }

  case class ContractsResponse(registedDate: Option[String] = None, activatedDate: Option[String] = None, closeDate: Option[String] = None, invoiceIssueType: Option[String] = None, memo: Option[String] = None, status: String, contractaddress: ContractAddressesResponse)
  object ContractsResponse {
    implicit def jsonWrites = Json.writes[ContractsResponse]
    implicit def jsonReads = Json.reads[ContractsResponse]
  }
  
  case class BillingAddressesResponse(company: Option[String] = None, postCode: Option[String] = None, prefecture: Option[String] = None, city: Option[String] = None, address1: Option[String] = None, address2: Option[String] = None, address3: Option[String] = None, depertment: Option[String] = None, staff: Option[String] = None, staffEmail: Option[String] = None, phone: Option[String] = None, fax: Option[String] = None)
  object BillingAddressesResponse {
    implicit def jsonWrites = Json.writes[BillingAddressesResponse]
    implicit def jsonReads = Json.reads[BillingAddressesResponse]
  }

  case class BillingDestinationsResponse(billingDestinationName: Option[String] = None, invoiceIssueType: Option[String] = None, dueDateMonth: Option[String] = None, dueDateDay: Option[String] = None, closingDate: Option[String] = None, pcaCode: Option[String] = None, pcaCommonName: Option[String] = None, memo: Option[String] = None, billingaddress: BillingAddressesResponse)
  object BillingDestinationsResponse {
    implicit def jsonWrites = Json.writes[BillingDestinationsResponse]
    implicit def jsonReads = Json.reads[BillingDestinationsResponse]
  }
  

  
  case class ClientsResponse(applicationName: String, agencyName: String, appClientId: String, name: Option[String] = None, email: Option[String] = None, role: Option[String] = None, appCreatedDate: Option[String] = None, lastLoginDate: Option[String] = None, lastLoginIpaddress: Option[String] = None, loginCount: Option[Int] = None, memo: Option[String] = None, status: String, contract: Option[ContractsResponse] = None, billingdestination: Option[BillingDestinationsResponse] = None)
  object ClientsResponse {
    implicit def jsonWrites = Json.writes[ClientsResponse]
    implicit def jsonReads = Json.reads[ClientsResponse]
  }

  case class SucccessClientsResponse(responsecode:String, messages: String, contents: Option[List[ClientsResponse]], count: Int)
  object SucccessClientsResponse {
    implicit def jsonWrites = Json.writes[SucccessClientsResponse]
    implicit def jsonReads = Json.reads[SucccessClientsResponse]
  }

}
