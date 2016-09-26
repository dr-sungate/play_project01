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

class ApplicationAgencyDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[ApplicationAgencyRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val applicationagencyagencyquery = TableQuery[ApplicationAgency]
  
  def all(): Future[List[ApplicationAgencyRow]] = {
    dbConfig.db.run(applicationagencyagencyquery.sortBy(c => (c.applicationId.desc, c.applicationId.desc)).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(applicationagencyagencyquery.length.result)
  }

 
  def findById(id: Long): Future[Option[ApplicationAgencyRow]] = {
    dbConfig.db.run(applicationagencyagencyquery.filter(_.applicationId === id).sortBy(c => (c.applicationId.desc, c.applicationId.desc)).result.headOption)
  }

  def findByApplicationIdhasOne(applicationId: Long): Future[Option[ApplicationAgencyRow]] = {
    dbConfig.db.run(applicationagencyagencyquery.filter(_.applicationId === applicationId).sortBy(c => (c.applicationId.desc, c.applicationId.desc)).result.headOption)
  }

  def findByAgencyIdhasOne(agencyId: Long): Future[Option[ApplicationAgencyRow]] = {
    dbConfig.db.run(applicationagencyagencyquery.filter(_.agencyId === agencyId).sortBy(c => (c.applicationId.desc, c.applicationId.desc)).result.headOption)
  }

  def create(applicationagency: ApplicationAgencyRow): Future[Int] = {
    val c = applicationagency.copy(
        createdDate = new Date
    )
    dbConfig.db.run(applicationagencyagencyquery += c)
  }

  def update(applicationagency: ApplicationAgencyRow): Future[Unit] = {
    dbConfig.db.run(applicationagencyagencyquery.filter(c => (c.applicationId === applicationagency.applicationId && c.agencyId === applicationagency.agencyId)).update(applicationagency).map(_ => ()))
  }

  def delete(id: Long): Future[Int] = dbConfig.db.run(applicationagencyagencyquery.filter(_.applicationId === id).delete)
  
  def deleteByAplicationId(applicationId: Long): Future[Int] = dbConfig.db.run(applicationagencyagencyquery.filter(_.applicationId === applicationId).delete)
  
  def deleteByAgencyId(agencyId: Long): Future[Int] = dbConfig.db.run(applicationagencyagencyquery.filter(_.agencyId === agencyId).delete)
 
 
}

