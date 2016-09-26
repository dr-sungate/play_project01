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

class TimezonesDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[TimezonesRow, Int]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val timezonesquery = TableQuery[Timezones]
 

  def all(): Future[List[TimezonesRow]] = {
    dbConfig.db.run(timezonesquery.sortBy(c => c.id.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(timezonesquery.length.result)
  }

 
  def findById(id: Int): Future[Option[TimezonesRow]] = {
    dbConfig.db.run(timezonesquery.filter(_.id === id).result.headOption)
  }

  def create(timezone: TimezonesRow): Future[Int] = {
    val c = timezone.copy(
        createdDate = new Date
    )
    dbConfig.db.run(timezonesquery += c)
  }

  def update(timezone: TimezonesRow): Future[Unit] = {
    dbConfig.db.run(timezonesquery.filter(_.id === timezone.id).update(timezone).map(_ => ()))
  }
  
  def delete(id: Int): Future[Int] = dbConfig.db.run(timezonesquery.filter(_.id === id).delete)

 
  def getValidListForSelectOption(): Future[Seq[(String,String)]] = {
    val query = (for {
      timezone <- timezonesquery.sortBy(c => c.id.desc)
    } yield (timezone.id, timezone.cities))
    
    dbConfig.db.run(query.result.map(rows => rows.map{case (id, cities) => (id.toString, cities.getOrElse("-"))}))
  }

}

