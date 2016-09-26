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

class BillingAddressesDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[BillingAddressesRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val billingaddressesquery = TableQuery[BillingAddresses]
 

  def all(): Future[List[BillingAddressesRow]] = {
    dbConfig.db.run(billingaddressesquery.sortBy(c => c.billingDestinationId.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(billingaddressesquery.length.result)
  }

 
  def findById(id: Long): Future[Option[BillingAddressesRow]] = {
    dbConfig.db.run(billingaddressesquery.filter(_.billingDestinationId === id).result.headOption)
  }

  def create(billingaddress: BillingAddressesRow): Future[Int] = {
    val c = billingaddress.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(billingaddressesquery += c)
  }

  def update(billingaddress: BillingAddressesRow): Future[Unit] = {
    dbConfig.db.run(billingaddressesquery.filter(_.billingDestinationId === billingaddress.billingDestinationId).update(billingaddress).map(_ => ()))
  }

  def update_mappinged(billingaddress: BillingAddressesRow): Future[Int] = {
     dbConfig.db.run(billingaddressesquery.filter(_.billingDestinationId === billingaddress.billingDestinationId).map(
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
          billingaddress.company,
          billingaddress.postCode,
          billingaddress.prefecture,
          billingaddress.city,
          billingaddress.address1,
          billingaddress.address2,
          billingaddress.address3,
          billingaddress.depertment,
          billingaddress.staff,
          billingaddress.staffEmail,
          billingaddress.phone,
          billingaddress.fax,
          billingaddress.updater,
          new Date
        )
      )
    )
  }
  
  def delete(id: Long): Future[Int] = dbConfig.db.run(billingaddressesquery.filter(_.billingDestinationId === id).delete)

 

}

