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


class AgenciesDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[AgenciesRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val agenciesquery = TableQuery[Agencies]
  private val applicationagencyquery = TableQuery[ApplicationAgency]
  private val applicationquery = TableQuery[Applications]
 

  def all(): Future[List[AgenciesRow]] = {
    dbConfig.db.run(agenciesquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord)).sortBy(c => c.id.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(agenciesquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord)).length.result)
  }

   def paginglist(page: Int, offset: Int, urlquery: Map[String, String]): Future[(Int, Seq[((AgenciesRow, ApplicationAgencyRow),ApplicationsRow)])] = {
    var joinquery = (for{
      agencylist <- filterQueryAgency(agenciesquery, applicationagencyquery,urlquery) join applicationagencyquery on (_.id === _.agencyId) join applicationquery on (_._2.applicationId === _.id)
    }yield (agencylist)
    ).sortBy(agencylist => agencylist._1._1.id.desc)

    val pagelistsql = (for {
        count <- joinquery.length.result
        joinquerylist <- joinquery.drop((page-1) * offset).take(offset).result
    }yield (count, joinquerylist)
    )
    dbConfig.db.run(pagelistsql.transactionally)
  }

  def paginglist(page: Int, offset: Int, urlquery: Map[String, String], applicationId: Long): Future[(Int, Seq[((AgenciesRow, ApplicationAgencyRow),ApplicationsRow)])] = {
    var joinquery = (for{
      agencylist <- filterQueryAgency(agenciesquery, applicationagencyquery,urlquery) join applicationagencyquery.filter(_.applicationId === applicationId) on (_.id === _.agencyId) join applicationquery on (_._2.applicationId === _.id)
    }yield (agencylist)
    ).sortBy(agencylist => agencylist._1._1.id.desc)

    val pagelistsql = (for {
        count <- joinquery.length.result
        joinquerylist <- joinquery.drop((page-1) * offset).take(offset).result
    }yield (count, joinquerylist)
    )
    dbConfig.db.run(pagelistsql.transactionally)
  }

  def findById(id: Long): Future[Option[AgenciesRow]] = {
    dbConfig.db.run(agenciesquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord && n.id === id)).result.headOption)
  }

  def findByIdWithApplicationId(id: Long, applicationId: Long): Future[Option[AgenciesRow]] = {
    var subquery = applicationagencyquery.filter(_.applicationId === applicationId)
    dbConfig.db.run(agenciesquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord && n.id === id)).filter(_.id in subquery.map(_.agencyId)).result.headOption)
  }
  
  def isBelongApplication(id: Long, applicationId: Long): Future[Boolean] ={
    findByIdWithApplicationId(id,applicationId).map{ agency =>
      agency match{
        case Some(agency) => true
        case _ => false
      }
    }
  }

  def create(agency: AgenciesRow): Future[Int] = {
    val c = agency.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(agenciesquery += c)
  }

  def create(agenciesApplicationAgency: AgenciesApplicationAgencyRow): Future[Long] = {
      val agencynew = new AgenciesRow(0, agenciesApplicationAgency.agencyName, agenciesApplicationAgency.`type`, agenciesApplicationAgency.status, agenciesApplicationAgency.memo, agenciesApplicationAgency.isDisabled, agenciesApplicationAgency.timezoneId, agenciesApplicationAgency.updater, new Date, new Date)
      val action =
      (for {
       newId <- (agenciesquery returning agenciesquery.map(_.id) += agencynew)
       // その結果を使って更新
       _ <- applicationagencyquery += new ApplicationAgencyRow(agenciesApplicationAgency.applicationAgency.applicationId, newId, agenciesApplicationAgency.updater, new Date)
     } yield (newId) )

     dbConfig.db.run(action.transactionally)
  }

  def update(agency: AgenciesRow): Future[Unit] = {
    dbConfig.db.run(agenciesquery.filter(_.id === agency.id).update(agency).map(_ => ()))
  }

  def update_mappinged(agency: AgenciesRow): Future[Int] = {
     dbConfig.db.run(agenciesquery.filter(_.id === agency.id).map(
         c => (
            c.agencyName,
            c.`type`,
            c.status,
            c.memo,
            c.isDisabled,
            c.timezoneId,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          agency.agencyName,
          agency.`type`,
          agency.status,
          agency.memo,
          agency.isDisabled,
          agency.timezoneId,
          agency.updater,
          new Date
        )
      )
    )
  }

  def update_mappinged(agenciesApplicationAgency: AgenciesApplicationAgencyRow): Future[Int] = {
     var subquery = applicationagencyquery.filter(_.applicationId === agenciesApplicationAgency.applicationAgency.applicationId)
     dbConfig.db.run(agenciesquery.filter(_.id === agenciesApplicationAgency.id).filter(_.id in subquery.map(_.agencyId)).map(
         c => (
            c.agencyName,
            c.`type`,
            c.status,
            c.memo,
            c.isDisabled,
            c.timezoneId,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          agenciesApplicationAgency.agencyName,
          agenciesApplicationAgency.`type`,
          agenciesApplicationAgency.status,
          agenciesApplicationAgency.memo,
          agenciesApplicationAgency.isDisabled,
          agenciesApplicationAgency.timezoneId,
          agenciesApplicationAgency.updater,
          new Date
        )
      )
    )
  }

  def delete(id: Long): Future[Int] = dbConfig.db.run(agenciesquery.filter(_.id === id).delete)

  def disable(id: Long, updater: Int, applicationId: Long): Future[Int] = {
     var subquery = applicationagencyquery.filter(_.applicationId === applicationId)
     dbConfig.db.run(agenciesquery.filter(_.id ===id).filter(_.id in subquery.map(_.agencyId)).map(
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
  
  def filterQueryAgency(tablequery:TableQuery[Agencies], subtablequery:TableQuery[ApplicationAgency], urlquery: Map[String, String]) = {
    var returnquery = tablequery.filter(_.isDisabled === RecordValid.IsEnabledRecord)
    urlquery.foreach { querytuple =>
        querytuple._1 match{
          case "agencyName" => if(!StringHelper.trim(querytuple._2).isEmpty()) returnquery = returnquery.filter(_.agencyName like ("%" + StringHelper.trim(querytuple._2) + "%").bind) 
          case "type" =>  if(querytuple._2 != "-1") returnquery = returnquery.filter(_.`type` === StringHelper.trim(querytuple._2).bind) 
          case "status" =>  if(querytuple._2 != "-1") returnquery = returnquery.filter(_.status === StringHelper.trim(querytuple._2).bind) 
          case "timezoneId" =>  if(querytuple._2 != "-1") returnquery = returnquery.filter(_.timezoneId === querytuple._2.toInt.bind) 
          case "applicationId" =>
            var subquery = subtablequery.filter(_.applicationId > 0.toLong)
            querytuple._1 match{
              case "applicationId" => if(querytuple._2 != "-1") subquery = subquery.filter(_.applicationId === querytuple._2.toLong.bind) 
               case _ => 
            }
            returnquery = returnquery.filter(_.id in subquery.map(_.agencyId))
          case _ => 
        }
    }
    returnquery
  }
 
  def findByNameList(name: String, applicationId: Long): Future[Seq[String]] = {
    var subquery = applicationagencyquery.filter(_.applicationId === applicationId)
    val query = (for {
      agency <- agenciesquery.filter(n => (n.isDisabled === false) && ( (n.agencyName like ("%" + name + "%")) || ( n.agencyName like "%" + (StringHelper.convertHiraganaToKatakana(name) + "%")) || ( n.agencyName like "%" + (StringHelper.convertKatakanaToHiragana(name) + "%")))).filter(_.id in subquery.map(_.agencyId)).sortBy(c => c.id.desc)
    } yield (agency.agencyName))
    dbConfig.db.run(query.result.map(rows => rows.map{case (agencyName) => agencyName}))
  }
  def checkExists(id: Long): Future[Boolean] = {
    this.findById(id).flatMap(record =>
      record match {
        case Some(agency) => Future(true)
        case None =>  Future(false)
      }
    )
   }
  def getValidListForSelectOption(applicationId: Long): Future[Seq[(String,String)]] = {
    var subquery = applicationagencyquery.filter(_.applicationId === applicationId)
    val query = (for {
      agency <- agenciesquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord)).filter(_.id in subquery.map(_.agencyId)).sortBy(c => c.id.desc)
    } yield (agency.id, agency.agencyName))
    
    dbConfig.db.run(query.result.map(rows => rows.map{case (id, agencyName) => (id.toString, agencyName)}))
  }

  def getValidListMap(applicationId: Long): Future[Seq[Map[String,String]]] = {
    var subquery = applicationagencyquery.filter(_.applicationId === applicationId)
    val query = (for {
      agency <- agenciesquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord)).filter(_.id in subquery.map(_.agencyId)).sortBy(c => c.id.desc)
    } yield (agency.id, agency.agencyName))
    
    dbConfig.db.run(query.result.map(rows => rows.map{case (id, agencyName) => Map("id"->id.toString, "agencyName" -> agencyName)}))
  }

}

