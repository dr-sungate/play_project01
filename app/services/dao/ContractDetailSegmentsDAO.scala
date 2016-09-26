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

class ContractDetailSegmentLogsDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[ContractDetailSegmentLogsRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val contractdetailsegmentlogsegmentsquery = TableQuery[ContractDetailSegmentLogs]
 

  def all(): Future[List[ContractDetailSegmentLogsRow]] = {
    dbConfig.db.run(contractdetailsegmentlogsegmentsquery.sortBy(c => c.id.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(contractdetailsegmentlogsegmentsquery.length.result)
  }

 
  def findById(id: Long): Future[Option[ContractDetailSegmentLogsRow]] = {
    dbConfig.db.run(contractdetailsegmentlogsegmentsquery.filter(_.id === id).result.headOption)
  }

  def create(contractdetailsegmentlog: ContractDetailSegmentLogsRow): Future[Int] = {
    val c = contractdetailsegmentlog.copy(
        createdDate = new Date
    )
    dbConfig.db.run(contractdetailsegmentlogsegmentsquery += c)
  }

  def update(contractdetailsegmentlog: ContractDetailSegmentLogsRow): Future[Unit] = {
    dbConfig.db.run(contractdetailsegmentlogsegmentsquery.filter(_.id === contractdetailsegmentlog.id).update(contractdetailsegmentlog).map(_ => ()))
  }

  def update_mappinged(contractdetailsegmentlog: ContractDetailSegmentLogsRow): Future[Int] = {
     dbConfig.db.run(contractdetailsegmentlogsegmentsquery.filter(_.id === contractdetailsegmentlog.id).map(
         c => (
            c.clientId,
            c.contractDetailId,
            c.unitLog,
            c.accountingFrom,
            c.accountingTo
            )
      ).update(
          (
          contractdetailsegmentlog.clientId,
          contractdetailsegmentlog.contractDetailId,
          contractdetailsegmentlog.unitLog,
          contractdetailsegmentlog.accountingFrom,
          contractdetailsegmentlog.accountingTo
                  )
      )
    )
  }
  
  def delete(id: Long): Future[Int] = dbConfig.db.run(contractdetailsegmentlogsegmentsquery.filter(_.id === id).delete)

 

}

