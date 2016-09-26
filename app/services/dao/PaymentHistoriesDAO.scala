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

class PaymentHistoriesDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[PaymentHistoriesRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val paymenthistoriesquery = TableQuery[PaymentHistories]
 

  def all(): Future[List[PaymentHistoriesRow]] = {
    dbConfig.db.run(paymenthistoriesquery.sortBy(c => c.id.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(paymenthistoriesquery.length.result)
  }

 
  def findById(id: Long): Future[Option[PaymentHistoriesRow]] = {
    dbConfig.db.run(paymenthistoriesquery.filter(_.id === id).result.headOption)
  }

  def create(paymenthistory: PaymentHistoriesRow): Future[Int] = {
    val c = paymenthistory.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(paymenthistoriesquery += c)
  }

  def update(paymenthistory: PaymentHistoriesRow): Future[Unit] = {
    dbConfig.db.run(paymenthistoriesquery.filter(_.id === paymenthistory.id).update(paymenthistory).map(_ => ()))
  }

  def update_mappinged(paymenthistory: PaymentHistoriesRow): Future[Int] = {
     dbConfig.db.run(paymenthistoriesquery.filter(_.id === paymenthistory.id).map(
         c => (
            c.clientId,
            c.contractDetailId,
            c.appProductId,
            c.productName,
            c.price,
            c.taxRate,
            c.taxedPrice,
            c.paymentDate,
            c.paymentIssueType,
            c.paymentStatus,
            c.accountingFrom,
            c.accountingTo,
            c.details,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          paymenthistory.clientId,
          paymenthistory.contractDetailId,
          paymenthistory.appProductId,
          paymenthistory.productName,
          paymenthistory.price,
          paymenthistory.taxRate,
          paymenthistory.taxedPrice,
          paymenthistory.paymentDate,
          paymenthistory.paymentIssueType,
          paymenthistory.paymentStatus,
          paymenthistory.accountingFrom,
          paymenthistory.accountingTo,
          paymenthistory.details,
          paymenthistory.updater,
          new Date
        )
      )
    )
  }
  
  def delete(id: Long): Future[Int] = dbConfig.db.run(paymenthistoriesquery.filter(_.id === id).delete)

 

}

