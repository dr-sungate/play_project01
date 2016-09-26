package services.dao

import java.util.Calendar
import java.util.Date
import org.mindrot.jbcrypt.BCrypt

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import javax.inject.Inject
import javax.inject.Singleton
import models.TablesExtend._
import play.api._
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider

import play.api.mvc._
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import java.util.UUID

import utilities._
import utilities.valid._

class OauthUsersDAO @Inject()(dbConfigProvider: DatabaseConfigProvider)  extends BaseDAO[OauthUsersRow, UUID]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val oauthuserquery = TableQuery[OauthUsers]
  private val clientsquery = TableQuery[Clients]
   

  def all(): Future[List[OauthUsersRow]] = {
    dbConfig.db.run(oauthuserquery.sortBy(c => c.guid.desc).result).map(_.toList)
  }

  def paginglist(page: Int, offset: Int, urlquery: Map[String, String], applicationId: Long): Future[(Int, Seq[(OauthUsersRow, Option[ClientsRow])])] = {
    var joinquery = (for{
      oauthuserlist <- filterQueryOauthuser(oauthuserquery, clientsquery, urlquery).filter(_.applicationId === applicationId)  joinLeft clientsquery on (_.clientId === _.id) 
    }yield (oauthuserlist)
    ).sortBy(oauthuserlist => oauthuserlist._1.createdDate.desc)

    val pagelistsql = (for {
        count <- joinquery.length.result
        joinquerylist <- joinquery.drop((page-1) * offset).take(offset).result
    }yield (count, joinquerylist)
    )
    dbConfig.db.run(pagelistsql.transactionally)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(oauthuserquery.length.result)
  }

  def findById(id: UUID): Future[Option[OauthUsersRow]] = {
    dbConfig.db.run(oauthuserquery.filter(_.guid === id).result.headOption)
  }

  def create(oauthuser: OauthUsersRow): Future[Int] = {
    val c = oauthuser.copy(
        guid = java.util.UUID.randomUUID
    )
    dbConfig.db.run(oauthuserquery += c)
  }

  def update(oauthuser: OauthUsersRow): Future[Unit] = {
    dbConfig.db.run(oauthuserquery.filter(_.guid === oauthuser.guid).update(oauthuser).map(_ => ()))
  }

  def update_mappinged(oauthuser: OauthUsersRow): Future[Int] = {
     dbConfig.db.run(oauthuserquery.filter(_.guid === oauthuser.guid).map(
       c => (
          c.agencyId,
          c.clientId,
          c.name,
          c.updater,
          c.updatedDate
          )
      ).update(
        (
        oauthuser.agencyId,
        oauthuser.clientId,
        oauthuser.name,
        oauthuser.updater,
        new Date
        )
      )
    )
        
  }
  
  def delete(id: UUID): Future[Int] = dbConfig.db.run(oauthuserquery.filter(_.guid === id).delete)
  
  def filterQueryOauthuser(tablequery:TableQuery[OauthUsers], subtablequery:TableQuery[Clients], urlquery: Map[String, String]) = {
    val dummyid: UUID = null
    var returnquery = tablequery.filterNot(_.guid === dummyid)
    urlquery.foreach { querytuple =>
        querytuple._1 match{
          case "name" => if(!StringHelper.trim(querytuple._2).isEmpty()) returnquery = returnquery.filter(_.name like ("%" + StringHelper.trim(querytuple._2) + "%").bind) 
          case "agencyId" =>  if(querytuple._2 != "-1") returnquery = returnquery.filter(_.agencyId === querytuple._2.toLong.bind) 
          case "clientName" =>
            var subquery =  subtablequery.filter(_.isDisabled === RecordValid.IsEnabledRecord)
            querytuple._1 match{
              case "clientName" =>  if(!StringHelper.trim(querytuple._2).isEmpty()) subquery = subquery.filter(_.name like ("%" + StringHelper.trim(querytuple._2) + "%").bind) 
               case _ => 
            }
            returnquery = returnquery.filter(_.clientId in subquery.map(_.id))
          case _ => 
        }
    }
    returnquery
  }

  def getValidListForSelectOption(applicationId: Long): Future[Seq[(String,String)]] = {
    val query = (for {
      oauthuser <- oauthuserquery.filter(_.applicationId === applicationId).sortBy(c => c.guid.desc)
    } yield (oauthuser.guid, oauthuser.name))
    
    dbConfig.db.run(query.result.map(rows => rows.map{case (guid, name) => (guid.toString, name)}))
  }
  
  def findByNameList(applicationId: Long, name: String): Future[Seq[String]] = {
    val query = (for {
      oauthuser <- oauthuserquery.filter(n => ((n.applicationId === applicationId) && ( (n.name like (name + "%")) || ( n.name like (StringHelper.convertHiraganaToKatakana(name) + "%")) || ( n.name like (StringHelper.convertKatakanaToHiragana(name) + "%"))))).sortBy(c => c.guid.desc)
    } yield (oauthuser.name))
    dbConfig.db.run(query.result.map(rows => rows.map{case (name) => name}))
  }

}

