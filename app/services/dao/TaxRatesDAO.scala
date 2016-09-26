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

class TaxRatesDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[TaxRatesRow, Int]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val taxratesquery = TableQuery[TaxRates]
 

  def all(): Future[List[TaxRatesRow]] = {
    dbConfig.db.run(taxratesquery.sortBy(c => c.startFrom.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(taxratesquery.length.result)
  }

 
  def findById(id: Int): Future[Option[TaxRatesRow]] = {
    dbConfig.db.run(taxratesquery.filter(_.id === id).result.headOption)
  }

  def create(taxrate: TaxRatesRow): Future[Int] = {
    val c = taxrate.copy(
        createdDate = new Date
    )
    dbConfig.db.run(taxratesquery += c)
  }

  def update(taxrate: TaxRatesRow): Future[Unit] = {
    dbConfig.db.run(taxratesquery.filter(_.id === taxrate.id).update(taxrate).map(_ => ()))
  }
  
  def delete(id: Int): Future[Int] = dbConfig.db.run(taxratesquery.filter(_.id === id).delete)

  def getNowRateWithDate(targetDate: Date):Future[Option[TaxRatesRow]] = {
    dbConfig.db.run(taxratesquery.filter(_.startFrom <= targetDate).sortBy(_.startFrom.desc).result.headOption)
  }

}

