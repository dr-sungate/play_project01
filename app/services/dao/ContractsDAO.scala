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

class ContractsDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[ContractsRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val contractsquery = TableQuery[Contracts]
  private val contractaddressquery = TableQuery[ContractAddresses]
 

  def all(): Future[List[ContractsRow]] = {
    dbConfig.db.run(contractsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord )).sortBy(c => c.id.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(contractsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord )).length.result)
  }

 
  def findById(id: Long): Future[Option[ContractsRow]] = {
    dbConfig.db.run(contractsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord && n.id === id)).result.headOption)
  }
  
  def findByClientId(id: Long): Future[Option[ContractsRow]] = {
    dbConfig.db.run(contractsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord && n.clientId === id)).result.headOption)
  }

  def findByClientIdWithAddress(id: Long): Future[Option[(ContractsRow, ContractAddressesRow)]] = {
     var joinquery = (for{
      contractlist <- contractsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord && n.clientId === id))  join contractaddressquery on (_.id === _.contractId)
    }yield (contractlist)
    ).result.headOption
   dbConfig.db.run(joinquery.transactionally)
  }

  def create(contract: ContractsRow): Future[Int] = {
    val c = contract.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(contractsquery += c)
  }

  def update(contract: ContractsRow): Future[Unit] = {
    dbConfig.db.run(contractsquery.filter(_.id === contract.id).update(contract).map(_ => ()))
  }

  def update_mappinged(contract: ContractsRow): Future[Int] = {
     dbConfig.db.run(contractsquery.filter(_.id === contract.id).map(
         c => (
            c.clientId,
            c.registedDate,
            c.activatedDate,
            c.closeDate,
            c.invoiceIssueType,
            c.memo,
            c.status,
            c.isDisabled,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          contract.clientId,
          contract.registedDate,
          contract.activatedDate,
          contract.closeDate,
          contract.invoiceIssueType,
          contract.memo,
          contract.status,
          contract.isDisabled,
          contract.updater,
          new Date
        )
      )
    )
  }
  
  def delete(id: Long): Future[Int] = dbConfig.db.run(contractsquery.filter(_.id === id).delete)

 

}

