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

class InvoicesDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[InvoicesRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val invoicesquery = TableQuery[Invoices]
 

  def all(): Future[List[InvoicesRow]] = {
    dbConfig.db.run(invoicesquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord)).sortBy(c => c.id.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(invoicesquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord)).length.result)
  }

 
  def findById(id: Long): Future[Option[InvoicesRow]] = {
    dbConfig.db.run(invoicesquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord && n.id === id)).result.headOption)
  }

  def create(invoice: InvoicesRow): Future[Int] = {
    val c = invoice.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(invoicesquery += c)
  }

  def update(invoice: InvoicesRow): Future[Unit] = {
    dbConfig.db.run(invoicesquery.filter(_.id === invoice.id).update(invoice).map(_ => ()))
  }

  def update_mappinged(invoice: InvoicesRow): Future[Int] = {
     dbConfig.db.run(invoicesquery.filter(_.id === invoice.id).map(
         c => (
            c.invoicecViewId,
            c.applicationId,
            c.agencyId,
            c.billingDestinationId,
            c.invoicecTo,
            c.invoicecSubject,
            c.invoicecTotal,
            c.invoicecTaxedTotal,
            c.invoicecTaxrate,
            c.pcaCode,
            c.pcaCommonName,
            c.memo,
            c.status,
            c.isDisabled,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          invoice.invoicecViewId,
          invoice.applicationId,
          invoice.agencyId,
          invoice.billingDestinationId,
          invoice.invoicecTo,
          invoice.invoicecSubject,
          invoice.invoicecTotal,
          invoice.invoicecTaxedTotal,
          invoice.invoicecTaxrate,
          invoice.pcaCode,
          invoice.pcaCommonName,
          invoice.memo,
          invoice.status,
          invoice.isDisabled,
          invoice.updater,
          new Date
        )
      )
    )
  }
  
  def delete(id: Long): Future[Int] = dbConfig.db.run(invoicesquery.filter(_.id === id).delete)

 

}

