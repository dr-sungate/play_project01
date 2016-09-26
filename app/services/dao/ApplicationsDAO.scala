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

class ApplicationsDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[ApplicationsRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val applicationsquery = TableQuery[Applications]
  private val applicationagencyquery = TableQuery[ApplicationAgency]
  private val agenciesquery = TableQuery[Agencies]
  
  def all(): Future[List[ApplicationsRow]] = {
    dbConfig.db.run(applicationsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord)).sortBy(c => c.id.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(applicationsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord)).length.result)
  }

  def paginglist(page: Int, offset: Int): Future[(Int, Seq[ApplicationsRow])] = {
    val pagelistsql = (for {
        count <- applicationsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord)).sortBy(c => c.id.desc).length.result
        applications <- applicationsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord)).sortBy(c => c.id.desc).drop((page-1) * offset).take(offset).result
    }yield (count, applications)
    )
    dbConfig.db.run(pagelistsql.transactionally)
  }
 
  def findById(id: Long): Future[Option[ApplicationsRow]] = {
    dbConfig.db.run(applicationsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord && n.id === id)).result.headOption)
  }

  def create(application: ApplicationsRow): Future[Int] = {
    val c = application.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(applicationsquery += c)
  }

  def update(application: ApplicationsRow): Future[Unit] = {
    dbConfig.db.run(applicationsquery.filter(_.id === application.id).update(application).map(_ => ()))
  }

  def update_mappinged(application: ApplicationsRow): Future[Int] = {
     dbConfig.db.run(applicationsquery.filter(_.id === application.id).map(
         c => (
            c.appName,
            c.url,
            c.status,
            c.serverIps,
            c.isDisabled,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          application.appName,
          application.url,
          application.status,
          application.serverIps,
          application.isDisabled,
          application.updater,
          new Date
        )
      )
    )
  }
  
  def delete(id: Long): Future[Int] = dbConfig.db.run(applicationsquery.filter(_.id === id).delete)
  
  def disable(id: Long, updater: Int): Future[Int] = {
     dbConfig.db.run(applicationsquery.filter(_.id ===id).map(
         c => (
            c.isDisabled,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          Option(RecordValid.IsDisabledRecord),
          Option(updater),
          new Date
        )
      )
    )
  }

  def getValidListForSelectOption(): Future[Seq[(String,String)]] = {
    val query = (for {
      application <- applicationsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord)).sortBy(c => c.id.desc)
    } yield (application.id, application.appName))
    
    dbConfig.db.run(query.result.map(rows => rows.map{case (id, appName) => (id.toString, appName)}))
  }

  def getValidListMap(): Future[Seq[Map[String,String]]] = {
    val query = (for {
      application <- applicationsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord)).sortBy(c => c.id.desc)
    } yield (application.id, application.appName))
    
    dbConfig.db.run(query.result.map(rows => rows.map{case (id, appName) => Map("id"->id.toString, "appname" -> appName)}))
  }

  def getValidListMap(applicationId: Long): Future[Seq[Map[String,String]]] = {
    val query = (for {
      application <- applicationsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord && n.id === applicationId)).sortBy(c => c.id.desc)
    } yield (application.id, application.appName))
    
    dbConfig.db.run(query.result.map(rows => rows.map{case (id, appName) => Map("id"->id.toString, "appname" -> appName)}))
  }

  def getValidListWithAgencyMap(): Future[Seq[Map[String,String]]] = {
    val query = (for {
      application <- applicationsquery.filter(_.isDisabled === RecordValid.IsEnabledRecord).filter(_.id in applicationagencyquery.filter(_.agencyId in agenciesquery.filter(_.isDisabled === RecordValid.IsEnabledRecord).map(_.id)).map(_.applicationId)).sortBy(c => c.id.desc)
    } yield (application.id, application.appName))
    
    dbConfig.db.run(query.result.map(rows => rows.map{case (id, appName) => Map("id"->id.toString, "appname" -> appName)}))
  }

  def getValidListWithAgencyMap(applicationId: Long): Future[Seq[Map[String,String]]] = {
    val query = (for {
      application <- applicationsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord && n.id === applicationId)).filter(_.id in applicationagencyquery.filter(_.agencyId in agenciesquery.filter(_.isDisabled === RecordValid.IsEnabledRecord).map(_.id)).map(_.applicationId)).sortBy(c => c.id.desc)
    } yield (application.id, application.appName))
    
    dbConfig.db.run(query.result.map(rows => rows.map{case (id, appName) => Map("id"->id.toString, "appname" -> appName)}))
  }

   def findByNameList(name: String): Future[Seq[String]] = {
    val query = (for {
      application <- applicationsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord) && ( (n.appName like (name + "%")) || ( n.appName like (StringHelper.convertHiraganaToKatakana(name) + "%")) || ( n.appName like (StringHelper.convertKatakanaToHiragana(name) + "%")))).sortBy(c => c.id.desc)
    } yield (application.appName))
    dbConfig.db.run(query.result.map(rows => rows.map{case (appName) => appName}))
  }

  def getAppNameById(id: Long): Future[Option[String]] = {
     val query = (for {
      application <- applicationsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord && n.id === id)).sortBy(c => c.id.desc)
    } yield (application.appName))
   dbConfig.db.run(query.result.headOption.map(rows => rows.map{case (appName) => appName}))
  }

  def checkExists(id: Long): Future[Boolean] = {
    this.findById(id).flatMap(record =>
      record match {
        case Some(application) => Future(true)
        case None =>  Future(false)
      }
    )
   }
}

