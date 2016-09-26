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

class InvoiceProcessdatesDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[InvoiceProcessdatesRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val invoiceprocessdatesquery = TableQuery[InvoiceProcessdates]
 

  def all(): Future[List[InvoiceProcessdatesRow]] = {
    dbConfig.db.run(invoiceprocessdatesquery.sortBy(c => c.invoiceId.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(invoiceprocessdatesquery.length.result)
  }

 
  def findById(id: Long): Future[Option[InvoiceProcessdatesRow]] = {
    dbConfig.db.run(invoiceprocessdatesquery.filter(_.invoiceId === id).result.headOption)
  }

  def create(invoiceprocessdate: InvoiceProcessdatesRow): Future[Int] = {
    val c = invoiceprocessdate.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(invoiceprocessdatesquery += c)
  }

  def update(invoiceprocessdate: InvoiceProcessdatesRow): Future[Unit] = {
    dbConfig.db.run(invoiceprocessdatesquery.filter(_.invoiceId === invoiceprocessdate.invoiceId).update(invoiceprocessdate).map(_ => ()))
  }

  def update_mappinged(invoiceprocessdate: InvoiceProcessdatesRow): Future[Int] = {
     dbConfig.db.run(invoiceprocessdatesquery.filter(_.invoiceId === invoiceprocessdate.invoiceId).map(
         c => (
            c.issueDate,
            c.applyDate,
            c.payedDate,
            c.sentDate,
            c.cancelDate,
            c.paymentDueDate,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          invoiceprocessdate.issueDate,
          invoiceprocessdate.applyDate,
          invoiceprocessdate.payedDate,
          invoiceprocessdate.sentDate,
          invoiceprocessdate.cancelDate,
          invoiceprocessdate.paymentDueDate,
          invoiceprocessdate.updater,
          new Date
        )
      )
    )
  }
  
  def delete(id: Long): Future[Int] = dbConfig.db.run(invoiceprocessdatesquery.filter(_.invoiceId === id).delete)

 

}

