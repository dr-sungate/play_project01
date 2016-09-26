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

class ContractAddressesDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[ContractAddressesRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val contractaddressesquery = TableQuery[ContractAddresses]
  private val contractsquery = TableQuery[Contracts]


  def all(): Future[List[ContractAddressesRow]] = {
    dbConfig.db.run(contractaddressesquery.sortBy(c => c.contractId.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(contractaddressesquery.length.result)
  }

 
  def findById(id: Long): Future[Option[ContractAddressesRow]] = {
    dbConfig.db.run(contractaddressesquery.filter(_.contractId === id).result.headOption)
  }

  def findByClientId(clientId: Long): Future[Option[ContractAddressesRow]] = {
    dbConfig.db.run(contractaddressesquery.filter(_.contractId in contractsquery.filter(_.clientId === clientId).map(_.id)).result.headOption)
  }

  def create(contractaddress: ContractAddressesRow): Future[Int] = {
    val c = contractaddress.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(contractaddressesquery += c)
  }

  def update(contractaddress: ContractAddressesRow): Future[Unit] = {
    dbConfig.db.run(contractaddressesquery.filter(_.contractId === contractaddress.contractId).update(contractaddress).map(_ => ()))
  }

  def update_mappinged(contractaddress: ContractAddressesRow): Future[Int] = {
     dbConfig.db.run(contractaddressesquery.filter(_.contractId === contractaddress.contractId).map(
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
          contractaddress.company,
          contractaddress.postCode,
          contractaddress.prefecture,
          contractaddress.city,
          contractaddress.address1,
          contractaddress.address2,
          contractaddress.address3,
          contractaddress.depertment,
          contractaddress.staff,
          contractaddress.staffEmail,
          contractaddress.phone,
          contractaddress.fax,
          contractaddress.updater,
          new Date
        )
      )
    )
  }
  
  def delete(id: Long): Future[Int] = dbConfig.db.run(contractaddressesquery.filter(_.contractId === id).delete)

 

}

