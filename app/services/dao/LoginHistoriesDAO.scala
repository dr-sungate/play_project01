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

class LoginHistoriesDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[LoginHistoriesRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val loginhistoriesquery = TableQuery[LoginHistories]
 

  def all(): Future[List[LoginHistoriesRow]] = {
    dbConfig.db.run(loginhistoriesquery.sortBy(c => c.id.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(loginhistoriesquery.length.result)
  }

 
  def findById(id: Long): Future[Option[LoginHistoriesRow]] = {
    dbConfig.db.run(loginhistoriesquery.filter(_.id === id).result.headOption)
  }

  def findByAccountId(accountId: Int): Future[List[LoginHistoriesRow]] = {
    dbConfig.db.run(loginhistoriesquery.filter(_.accountId === accountId).sortBy(c => c.createdDate.desc).result).map(_.toList)
  }
  
  def paginglistByAccountId(accountId: Int, page: Int, offset: Int): Future[(Int, Seq[LoginHistoriesRow])] = {
    val pagelistsql = (for {
        count <- loginhistoriesquery.filter(_.accountId === accountId).length.result
        loginhistories <- loginhistoriesquery.filter(_.accountId === accountId).sortBy(c => c.createdDate.desc).drop((page-1) * offset).take(offset).result
    }yield (count, loginhistories)
    )
    dbConfig.db.run(pagelistsql.transactionally)
  }

  def create(loginhistory: LoginHistoriesRow): Future[Int] = {
    val c = loginhistory.copy(
        createdDate = new Date
    )
    dbConfig.db.run(loginhistoriesquery += c)
  }

  def update(loginhistory: LoginHistoriesRow): Future[Unit] = {
    dbConfig.db.run(loginhistoriesquery.filter(_.id === loginhistory.id).update(loginhistory).map(_ => ()))
  }
  
  def delete(id: Long): Future[Int] = dbConfig.db.run(loginhistoriesquery.filter(_.id === id).delete)

  def rotateOld(borderdate: Date): Future[Int] = dbConfig.db.run(loginhistoriesquery.filter(_.createdDate < borderdate).delete)

}

