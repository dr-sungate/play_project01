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

class DefaultProductSegmentsDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[DefaultProductSegmentsRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val defaultproductsegmentsquery = TableQuery[DefaultProductSegments]
 

  def all(): Future[List[DefaultProductSegmentsRow]] = {
    dbConfig.db.run(defaultproductsegmentsquery.sortBy(c => c.id.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(defaultproductsegmentsquery.length.result)
  }

 
  def findById(id: Long): Future[Option[DefaultProductSegmentsRow]] = {
    dbConfig.db.run(defaultproductsegmentsquery.filter(_.id === id).result.headOption)
  }

  def create(defaultproductsegment: DefaultProductSegmentsRow): Future[Int] = {
    val c = defaultproductsegment.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(defaultproductsegmentsquery += c)
  }

  def update(defaultproductsegment: DefaultProductSegmentsRow): Future[Unit] = {
    dbConfig.db.run(defaultproductsegmentsquery.filter(_.id === defaultproductsegment.id).update(defaultproductsegment).map(_ => ()))
  }

  def update_mappinged(defaultproductsegment: DefaultProductSegmentsRow): Future[Int] = {
     dbConfig.db.run(defaultproductsegmentsquery.filter(_.id === defaultproductsegment.id).map(
         c => (
            c.defaultProductId,
            c.unit,
            c.unitValue,
            c.price,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          defaultproductsegment.defaultProductId,
          defaultproductsegment.unit,
          defaultproductsegment.unitValue,
          defaultproductsegment.price,
          defaultproductsegment.updater,
          new Date
        )
      )
    )
  }
  
  def delete(id: Long): Future[Int] = dbConfig.db.run(defaultproductsegmentsquery.filter(_.id === id).delete)

 

}

