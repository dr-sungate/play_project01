package services.dao

import java.util.Calendar
import java.util.Date

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import javax.inject.Inject
import javax.inject.Singleton
import models.TablesExtend._
import play.api._
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import utilities._
import utilities.valid._

class ClientsDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[ClientsRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val clientsquery = TableQuery[Clients]
  private val contractsquery = TableQuery[Contracts]
  private val contractaddressquery = TableQuery[ContractAddresses]
 

  def all(): Future[List[ClientsRow]] = {
    dbConfig.db.run(clientsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord )).sortBy(c => c.id.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(clientsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord )).length.result)
  }

 
  def findById(id: Long): Future[Option[ClientsRow]] = {
    dbConfig.db.run(clientsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord  && n.id === id)).result.headOption)
  }

  def findByIdWithAppAgencyId(clientId: Long, applicationId: Long, agencyId: Long): Future[Option[ClientsRow]] = {
    dbConfig.db.run(clientsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord  && n.id === clientId && n.applicationId === applicationId && n.agencyId === agencyId)).result.headOption)
  }

  def findByIdWithAppId(clientId: Long, applicationId: Long): Future[Option[ClientsRow]] = {
    dbConfig.db.run(clientsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord  && n.id === clientId && n.applicationId === applicationId )).result.headOption)
  }

  def findByAppIdAgencyId(applicationId: Long, agencyId: Long): Future[List[ClientsRow]] = {
    dbConfig.db.run(clientsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord  && n.applicationId === applicationId && n.agencyId === agencyId)).sortBy(c => c.id.desc).result).map(_.toList)
  }
  
  def findByAppId(applicationId: Long): Future[List[ClientsRow]] = {
    dbConfig.db.run(clientsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord  && n.applicationId === applicationId)).sortBy(c => c.id.desc).result).map(_.toList)
  }

  def isBelongApplicationAgency(clientId: Long, applicationId: Long, agencyId: Option[Long]): Future[Boolean] ={
    agencyId match{
      case Some(agencyId) =>
        findByIdWithAppAgencyId(clientId, applicationId, agencyId).map{ client =>
          client match{
            case Some(agency) => true
            case _ => false
          }
        }
      case _ =>
        findByIdWithAppId(clientId, applicationId).map{ client =>
          client match{
            case Some(agency) => true
            case _ => false
          }
        }
    }
  }

  def paginglist(page: Int, offset: Int, urlquery: Map[String, String]): Future[(Int, Seq[((ClientsRow, ContractsRow),ContractAddressesRow)])] = {
    var joinquery = (for{
      agencylist <- filterQueryClient(clientsquery, contractsquery, contractaddressquery,urlquery) join filterQueryContract(contractsquery, urlquery) on (_.id === _.clientId) join contractaddressquery on (_._2.id === _.contractId)
    }yield (agencylist)
    ).sortBy(agencylist => agencylist._1._1.id.desc)

    val pagelistsql = (for {
        count <- joinquery.length.result
        joinquerylist <- joinquery.drop((page-1) * offset).take(offset).result
    }yield (count, joinquerylist)
    )
    dbConfig.db.run(pagelistsql.transactionally)
  }

  def paginglist(page: Int, offset: Int, urlquery: Map[String, String], applicationId: Long): Future[(Int, Seq[((ClientsRow, ContractsRow),ContractAddressesRow)])] = {
    var joinquery = (for{
      agencylist <- filterQueryClient(clientsquery, contractsquery, contractaddressquery,urlquery).filter(_.applicationId === applicationId) join filterQueryContract(contractsquery, urlquery) on (_.id === _.clientId) join contractaddressquery on (_._2.id === _.contractId)
    }yield (agencylist)
    ).sortBy(agencylist => agencylist._1._1.id.desc)

    val pagelistsql = (for {
        count <- joinquery.length.result
        joinquerylist <- joinquery.drop((page-1) * offset).take(offset).result
    }yield (count, joinquerylist)
    )
    dbConfig.db.run(pagelistsql.transactionally)
  }

  def create(client: ClientsRow): Future[Int] = {
    val c = client.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(clientsquery += c)
  }

  def create(clientscontracts: ClientsContractsRow): Future[Long] = {
      val clientnew = new ClientsRow(0, clientscontracts.applicationId, clientscontracts.agencyId, clientscontracts.appClientId, clientscontracts.name, clientscontracts.email, clientscontracts.role, clientscontracts.appCreatedDate, null, Option(""), Option(0), clientscontracts.memo, clientscontracts.status, Option(RecordValid.IsEnabledRecord), clientscontracts.updater, new Date, new Date)
      val action =
      (for {
       newClientId <- (clientsquery returning clientsquery.map(_.id) += clientnew)
       // その結果を使って更新
       newContractId <- contractsquery returning contractsquery.map(_.id) += new ContractsRow(0, newClientId, clientscontracts.contract.registedDate, clientscontracts.contract.activatedDate, clientscontracts.contract.closeDate, clientscontracts.contract.invoiceIssueType, clientscontracts.contract.memo, clientscontracts.contract.status, Option(RecordValid.IsEnabledRecord), clientscontracts.contract.updater, new Date, new Date)
       _ <- contractaddressquery += new ContractAddressesRow(newContractId, clientscontracts.contractAddress.company, clientscontracts.contractAddress.postCode, clientscontracts.contractAddress.prefecture, clientscontracts.contractAddress.city, clientscontracts.contractAddress.address1, clientscontracts.contractAddress.address2, clientscontracts.contractAddress.address3, clientscontracts.contractAddress.depertment, clientscontracts.contractAddress.staff, clientscontracts.contractAddress.staffEmail, clientscontracts.contractAddress.phone, clientscontracts.contractAddress.fax, clientscontracts.contractAddress.updater,new Date,new Date)
     } yield (newContractId))

     dbConfig.db.run(action.transactionally)
  }

  def create(clientscontracts: ClientsContractsRow, applicationId: Long): Future[Long] = {
      val clientnew = new ClientsRow(0, applicationId, clientscontracts.agencyId, clientscontracts.appClientId, clientscontracts.name, clientscontracts.email, clientscontracts.role, clientscontracts.appCreatedDate, null, Option(""), Option(0), clientscontracts.memo, clientscontracts.status, Option(RecordValid.IsEnabledRecord), clientscontracts.updater, new Date, new Date)
      val action =
      (for {
       newClientId <- (clientsquery returning clientsquery.map(_.id) += clientnew)
       // その結果を使って更新
       newContractId <- contractsquery returning contractsquery.map(_.id) += new ContractsRow(0, newClientId, clientscontracts.contract.registedDate, clientscontracts.contract.activatedDate, clientscontracts.contract.closeDate, clientscontracts.contract.invoiceIssueType, clientscontracts.contract.memo, clientscontracts.contract.status, Option(RecordValid.IsEnabledRecord), clientscontracts.contract.updater, new Date, new Date)
       _ <- contractaddressquery += new ContractAddressesRow(newContractId, clientscontracts.contractAddress.company, clientscontracts.contractAddress.postCode, clientscontracts.contractAddress.prefecture, clientscontracts.contractAddress.city, clientscontracts.contractAddress.address1, clientscontracts.contractAddress.address2, clientscontracts.contractAddress.address3, clientscontracts.contractAddress.depertment, clientscontracts.contractAddress.staff, clientscontracts.contractAddress.staffEmail, clientscontracts.contractAddress.phone, clientscontracts.contractAddress.fax, clientscontracts.contractAddress.updater,new Date,new Date)
     } yield (newClientId))

     dbConfig.db.run(action.transactionally)
  }

  def create(clientscontracts: ClientsContractsRow, applicationId: Long, agencyId: Option[Long]): Future[Long] = {
      var insertAgencyId: Long = 0
      agencyId match{
        case Some(agencyId) => insertAgencyId = agencyId
        case _ => insertAgencyId = clientscontracts.agencyId
      }
      val clientnew = new ClientsRow(0, applicationId, insertAgencyId, clientscontracts.appClientId, clientscontracts.name, clientscontracts.email, clientscontracts.role, clientscontracts.appCreatedDate, null, Option(""), Option(0), clientscontracts.memo, clientscontracts.status, Option(RecordValid.IsEnabledRecord), clientscontracts.updater, new Date, new Date)
      val action =
      (for {
       newClientId <- (clientsquery returning clientsquery.map(_.id) += clientnew)
       // その結果を使って更新
       newContractId <- contractsquery returning contractsquery.map(_.id) += new ContractsRow(0, newClientId, clientscontracts.contract.registedDate, clientscontracts.contract.activatedDate, clientscontracts.contract.closeDate, clientscontracts.contract.invoiceIssueType, clientscontracts.contract.memo, clientscontracts.contract.status, Option(RecordValid.IsEnabledRecord), clientscontracts.contract.updater, new Date, new Date)
       _ <- contractaddressquery += new ContractAddressesRow(newContractId, clientscontracts.contractAddress.company, clientscontracts.contractAddress.postCode, clientscontracts.contractAddress.prefecture, clientscontracts.contractAddress.city, clientscontracts.contractAddress.address1, clientscontracts.contractAddress.address2, clientscontracts.contractAddress.address3, clientscontracts.contractAddress.depertment, clientscontracts.contractAddress.staff, clientscontracts.contractAddress.staffEmail, clientscontracts.contractAddress.phone, clientscontracts.contractAddress.fax, clientscontracts.contractAddress.updater,new Date,new Date)
     } yield (newClientId))

     dbConfig.db.run(action.transactionally)
  }

  def update(client: ClientsRow): Future[Unit] = {
    dbConfig.db.run(clientsquery.filter(_.id === client.id).update(client).map(_ => ()))
  }

  def update_mappinged(client: ClientsRow): Future[Int] = {
     dbConfig.db.run(clientsquery.filter(_.id === client.id).map(
         c => (
//            c.applicationId,
//            c.agencyId,
            c.appClientId,
            c.name,
            c.email,
            c.role,
            c.appCreatedDate,
            c.lastLoginDate,
            c.lastLoginIpaddress,
            c.loginCount,
            c.memo,
            c.status,
            c.isDisabled,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
//          client.applicationId,
//          client.agencyId,
          client.appClientId,
          client.name,
          client.email,
          client.role,
          client.appCreatedDate,
          client.lastLoginDate,
          client.lastLoginIpaddress,
          client.loginCount,
          client.memo,
          client.status,
          client.isDisabled,
          client.updater,
          new Date
        )
      )
    )
  }
  
    def update_mappinged(clientsContract: ClientsContractsRow, applicationId: Long): Future[Int] = {
    val action =
    (for {
       updatedClientsId <- clientsquery.filter(s => s.id === clientsContract.id && s.applicationId === applicationId).map(
           c => (
//            c.applicationId,
//            c.agencyId,
            c.appClientId,
            c.name,
            c.email,
            c.role,
            c.appCreatedDate,
            c.lastLoginDate,
            c.lastLoginIpaddress,
            c.loginCount,
            c.memo,
//            c.status,
//            c.isDisabled,
            c.updater,
            c.updatedDate
              )
        ).update(
            (
//          clientsContract.applicationId,
//          clientsContract.agencyId,
          clientsContract.appClientId,
          clientsContract.name,
          clientsContract.email,
          clientsContract.role,
          clientsContract.appCreatedDate,
          clientsContract.lastLoginDate,
          clientsContract.lastLoginIpaddress,
          clientsContract.loginCount,
          clientsContract.memo,
//          clientsContract.status,
//          clientsContract.isDisabled,
          clientsContract.updater,
          new Date
          )
        )
      // その結果を使って更新
       _ <- contractsquery.filter(_.clientId === clientsContract.id).map(
         c => (
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          clientsContract.contract.updater,
          new Date
        )
       ) 
       // その結果を使って更新
       _ <- contractaddressquery.filter(_.contractId in contractsquery.filter(_.clientId === clientsContract.id).map(_.id)).map(
         c => (
            c.company,
            c.postCode,
            c.prefecture,
            c.city,
            c.address1,
            c.address2,
            c.address3,
            c.depertment,
            c.staff,
            c.staffEmail,
            c.phone,
            c.fax,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          clientsContract.contractAddress.company,
          clientsContract.contractAddress.postCode,
          clientsContract.contractAddress.prefecture,
          clientsContract.contractAddress.city,
          clientsContract.contractAddress.address1,
          clientsContract.contractAddress.address2,
          clientsContract.contractAddress.address3,
          clientsContract.contractAddress.depertment,
          clientsContract.contractAddress.staff,
          clientsContract.contractAddress.staffEmail,
          clientsContract.contractAddress.phone,
          clientsContract.contractAddress.fax,
          clientsContract.contractAddress.updater,
          new Date
        )
      )
    } yield (updatedClientsId) )
    
    dbConfig.db.run(action.transactionally)
  }
  
  def delete(id: Long): Future[Int] = dbConfig.db.run(clientsquery.filter(_.id === id).delete)

  def disable(id: Long, updater: Int, applicationId: Long): Future[Int] = {
    val action =
    (for {
       row <-  clientsquery.filter(n => (n.id ===id && n.applicationId === applicationId)).map(
           c => (
            c.isDisabled,
            c.updater,
            c.updatedDate
              )
        ).update(
            (
          Option(RecordValid.IsDisabledRecord),
          Option(updater),
          new Date
          )
        )
       // その結果を使って更新
       _ <- contractsquery.filter(_.clientId === id).map(
         c => (
            c.isDisabled,
            c.updater,
            c.updatedDate
             )
      ).update(
          (
          Option(RecordValid.IsDisabledRecord),
          Option(updater),
          new Date
        )
      )
    } yield (row) )
    
    dbConfig.db.run(action.transactionally)

  }

  def filterQueryClient(tablequery:TableQuery[Clients], submiddletablequery:TableQuery[Contracts], subtablequery:TableQuery[ContractAddresses], urlquery: Map[String, String]) = {
    var returnquery = tablequery.filter(_.isDisabled === RecordValid.IsEnabledRecord)
    urlquery.foreach { querytuple =>
        querytuple._1 match{
          case "agencyId" => if(querytuple._2 != "-1") returnquery = returnquery.filter(_.agencyId === querytuple._2.toLong.bind) 
          case "appClientId" =>  if(!StringHelper.trim(querytuple._2).isEmpty()) returnquery = returnquery.filter(_.appClientId === StringHelper.trim(querytuple._2).bind) 
          case "role" =>  if(querytuple._2 != "-1") returnquery = returnquery.filter(_.role === StringHelper.trim(querytuple._2).bind) 
          case "clientStatus" =>  if(querytuple._2 != "-1") returnquery = returnquery.filter(_.status === StringHelper.trim(querytuple._2).bind) 
          case "searchWord" => 
            if(!StringHelper.trim(querytuple._2).isEmpty()) {
              returnquery = returnquery.filter(c => ((c.name like ("%" + StringHelper.trim(querytuple._2) + "%").bind) || (c.email like ("%" + StringHelper.trim(querytuple._2) + "%").bind))) 
              var subquery = subtablequery.filter(c=> ((c.company like ("%" + StringHelper.trim(querytuple._2) + "%").bind) || (c.staff like ("%" + StringHelper.trim(querytuple._2) + "%").bind)))
              var submiddlequery = submiddletablequery.filter(_.isDisabled === RecordValid.IsEnabledRecord)
              submiddlequery =  submiddlequery.filter(_.id in subquery.map(_.contractId))
              returnquery = returnquery.filter(_.id in submiddlequery.map(_.clientId))
            }
          case _ => 
        }
    }
    returnquery
  }
  
  def filterQueryContract(tablequery:TableQuery[Contracts], urlquery: Map[String, String]) = {
    var returnquery = tablequery.filter(_.isDisabled === RecordValid.IsEnabledRecord)
    urlquery.foreach { querytuple =>
        querytuple._1 match{
          case "contractStatus" =>  if(querytuple._2 != "-1") returnquery = returnquery.filter(_.status === StringHelper.trim(querytuple._2).bind) 
          case _ => 
        }
    }
    returnquery
  }
  def findByNameList(name: String, applicationId: Long): Future[Seq[String]] = {
    val query = (for {
      agency <- clientsquery.filter(n => (n.isDisabled === false) && (n.applicationId === applicationId) && ( (n.name like ("%" + name + "%")) || ( n.name like "%" + (StringHelper.convertHiraganaToKatakana(name) + "%")) || ( n.name like "%" + (StringHelper.convertKatakanaToHiragana(name) + "%")))).sortBy(c => c.id.desc)
    } yield (agency.name))
    dbConfig.db.run(query.result.map(rows => rows.map{case (name) => name.getOrElse("")}))
  }
  def checkExists(id: Long): Future[Boolean] = {
    this.findById(id).flatMap(record =>
      record match {
        case Some(client) => Future(true)
        case None =>  Future(false)
      }
    )
   }
  
  def getValidListMap(applicationId: Long, agencyId: Long): Future[Seq[Map[String,String]]] = {
    val query = (for {
      client <- clientsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord) && (n.applicationId === applicationId) && (n.agencyId === agencyId)).sortBy(c => c.id.desc)
    } yield (client.id, client.name))
    
    dbConfig.db.run(query.result.map(rows => rows.map{case (id, name) => Map("id"->id.toString, "name" -> name.getOrElse(""))}))
  }


}

