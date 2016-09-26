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

class ContractDetailSegmentsDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[ContractDetailSegmentsRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val contractdetailsegmentsegmentsquery = TableQuery[ContractDetailSegments]
 

  def all(): Future[List[ContractDetailSegmentsRow]] = {
    dbConfig.db.run(contractdetailsegmentsegmentsquery.sortBy(c => c.id.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(contractdetailsegmentsegmentsquery.length.result)
  }

 
  def findById(id: Long): Future[Option[ContractDetailSegmentsRow]] = {
    dbConfig.db.run(contractdetailsegmentsegmentsquery.filter(_.id === id).result.headOption)
  }

  def create(contractdetailsegment: ContractDetailSegmentsRow): Future[Int] = {
    val c = contractdetailsegment.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(contractdetailsegmentsegmentsquery += c)
  }

  def update(contractdetailsegment: ContractDetailSegmentsRow): Future[Unit] = {
    dbConfig.db.run(contractdetailsegmentsegmentsquery.filter(_.id === contractdetailsegment.id).update(contractdetailsegment).map(_ => ()))
  }

  def update_mappinged(contractdetailsegment: ContractDetailSegmentsRow): Future[Int] = {
     dbConfig.db.run(contractdetailsegmentsegmentsquery.filter(_.id === contractdetailsegment.id).map(
         c => (
            c.contractDetailId,
            c.unit,
            c.unitValue,
            c.price,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          contractdetailsegment.contractDetailId,
          contractdetailsegment.unit,
          contractdetailsegment.unitValue,
          contractdetailsegment.price,
          contractdetailsegment.updater,
          new Date
        )
      )
    )
  }
  
  def delete(id: Long): Future[Int] = dbConfig.db.run(contractdetailsegmentsegmentsquery.filter(_.id === id).delete)

 

}

