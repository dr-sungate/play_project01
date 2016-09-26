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

class InvoiceAddressesDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[InvoiceAddressesRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val invoiceaddressesquery = TableQuery[InvoiceAddresses]
 

  def all(): Future[List[InvoiceAddressesRow]] = {
    dbConfig.db.run(invoiceaddressesquery.sortBy(c => c.invoiceId.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(invoiceaddressesquery.length.result)
  }

 
  def findById(id: Long): Future[Option[InvoiceAddressesRow]] = {
    dbConfig.db.run(invoiceaddressesquery.filter(_.invoiceId === id).result.headOption)
  }

  def create(invoiceaddress: InvoiceAddressesRow): Future[Int] = {
    val c = invoiceaddress.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(invoiceaddressesquery += c)
  }

  def update(invoiceaddress: InvoiceAddressesRow): Future[Unit] = {
    dbConfig.db.run(invoiceaddressesquery.filter(_.invoiceId === invoiceaddress.invoiceId).update(invoiceaddress).map(_ => ()))
  }

  def update_mappinged(invoiceaddress: InvoiceAddressesRow): Future[Int] = {
     dbConfig.db.run(invoiceaddressesquery.filter(_.invoiceId === invoiceaddress.invoiceId).map(
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
          invoiceaddress.company,
          invoiceaddress.postCode,
          invoiceaddress.prefecture,
          invoiceaddress.city,
          invoiceaddress.address1,
          invoiceaddress.address2,
          invoiceaddress.address3,
          invoiceaddress.depertment,
          invoiceaddress.staff,
          invoiceaddress.staffEmail,
          invoiceaddress.phone,
          invoiceaddress.fax,
          invoiceaddress.updater,
          new Date
        )
      )
    )
  }
  
  def delete(id: Long): Future[Int] = dbConfig.db.run(invoiceaddressesquery.filter(_.invoiceId === id).delete)

 

}

