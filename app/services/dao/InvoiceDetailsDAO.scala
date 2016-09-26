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

class InvoiceDetailsDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[InvoiceDetailsRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val invoicedetailsquery = TableQuery[InvoiceDetails]
 

  def all(): Future[List[InvoiceDetailsRow]] = {
    dbConfig.db.run(invoicedetailsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord)).sortBy(c => c.id.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(invoicedetailsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord)).length.result)
  }

 
  def findById(id: Long): Future[Option[InvoiceDetailsRow]] = {
    dbConfig.db.run(invoicedetailsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord && n.id === id)).result.headOption)
  }

  def create(invoicedetail: InvoiceDetailsRow): Future[Int] = {
    val c = invoicedetail.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(invoicedetailsquery += c)
  }

  def update(invoicedetail: InvoiceDetailsRow): Future[Unit] = {
    dbConfig.db.run(invoicedetailsquery.filter(_.id === invoicedetail.id).update(invoicedetail).map(_ => ()))
  }

  def update_mappinged(invoicedetail: InvoiceDetailsRow): Future[Int] = {
     dbConfig.db.run(invoicedetailsquery.filter(_.id === invoicedetail.id).map(
         c => (
            c.invoiceId,
            c.unitPrice,
            c.quantity,
            c.productName,
            c.productType,
            c.accountName,
            c.details,
            c.billTermFrom,
            c.billTermTo,
            c.memo,
            c.isDisabled,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          invoicedetail.invoiceId,
          invoicedetail.unitPrice,
          invoicedetail.quantity,
          invoicedetail.productName,
          invoicedetail.productType,
          invoicedetail.accountName,
          invoicedetail.details,
          invoicedetail.billTermFrom,
          invoicedetail.billTermTo,
          invoicedetail.memo,
          invoicedetail.isDisabled,
          invoicedetail.updater,
          new Date
        )
      )
    )
  }
  
  def delete(id: Long): Future[Int] = dbConfig.db.run(invoicedetailsquery.filter(_.id === id).delete)

 

}

