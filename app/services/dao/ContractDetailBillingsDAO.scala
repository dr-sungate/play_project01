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

class ContractDetailBillingsDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[ContractDetailBillingsRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val contractdetailbillingbillingsquery = TableQuery[ContractDetailBillings]
 

  def all(): Future[List[ContractDetailBillingsRow]] = {
    dbConfig.db.run(contractdetailbillingbillingsquery.sortBy(c => c.contractDetailId.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(contractdetailbillingbillingsquery.length.result)
  }

 
  def findById(id: Long): Future[Option[ContractDetailBillingsRow]] = {
    dbConfig.db.run(contractdetailbillingbillingsquery.filter(_.contractDetailId === id).result.headOption)
  }

  def create(contractdetailbilling: ContractDetailBillingsRow): Future[Int] = {
    val c = contractdetailbilling.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(contractdetailbillingbillingsquery += c)
  }

  def update(contractdetailbilling: ContractDetailBillingsRow): Future[Unit] = {
    dbConfig.db.run(contractdetailbillingbillingsquery.filter(_.contractDetailId === contractdetailbilling.contractDetailId).update(contractdetailbilling).map(_ => ()))
  }

  def update_mappinged(contractdetailbilling: ContractDetailBillingsRow): Future[Int] = {
     dbConfig.db.run(contractdetailbillingbillingsquery.filter(_.contractDetailId === contractdetailbilling.contractDetailId).map(
         c => (
            c.activatedDate,
            c.billingType,
            c.billingTerm,
            c.firstBillingDate,
            c.lastBillingDate,
            c.nextBillingDate,
            c.billingSkip,
            c.paymentType,
            c.invoiceTermType,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          contractdetailbilling.activatedDate,
          contractdetailbilling.billingType,
          contractdetailbilling.billingTerm,
          contractdetailbilling.firstBillingDate,
          contractdetailbilling.lastBillingDate,
          contractdetailbilling.nextBillingDate,
          contractdetailbilling.billingSkip,
          contractdetailbilling.paymentType,
          contractdetailbilling.invoiceTermType,
          contractdetailbilling.updater,
          new Date
        )
      )
    )
  }
  
  def delete(id: Long): Future[Int] = dbConfig.db.run(contractdetailbillingbillingsquery.filter(_.contractDetailId === id).delete)

 

}

