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

class OauthClientsDAO @Inject()(dbConfigProvider: DatabaseConfigProvider)  extends BaseDAO[OauthClientsRow, UUID]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val oauthclientquery = TableQuery[OauthClients]
  private val oauthuserquery = TableQuery[OauthUsers]
  private val clientsquery = TableQuery[Clients]
 


  def validate(id: UUID, secret: Option[String], grantType: String): Future[Int] = {
    dbConfig.db.run(oauthclientquery.filter(n => (n.oauthClientId === id) && (n.clientSecret === secret) && (n.grantType === grantType)).length.result)
  }
  

  def all(): Future[List[OauthClientsRow]] = {
    dbConfig.db.run(oauthclientquery.sortBy(c => c.oauthClientId.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(oauthclientquery.length.result)
  }

  def paginglist(page: Int, offset: Int, urlquery: Map[String, String], applicationId: Long): Future[(Int, Seq[(OauthClientsRow, OauthUsersRow)])] = {
    var joinquery = (for{
      oauthclientlist <- filterQueryOauthClient(oauthclientquery, urlquery) join filterQueryOauthUser(oauthuserquery, clientsquery, urlquery, applicationId) on (_.oauthUserGuid === _.guid) 
    }yield (oauthclientlist)
    ).sortBy(oauthclientlist => oauthclientlist._1.createdDate.desc)

    val pagelistsql = (for {
        count <- joinquery.length.result
        joinquerylist <- joinquery.drop((page-1) * offset).take(offset).result
    }yield (count, joinquerylist)
    )
    dbConfig.db.run(pagelistsql.transactionally)
  }

  def findById(id: UUID): Future[Option[OauthClientsRow]] = {
    dbConfig.db.run(oauthclientquery.filter(_.oauthClientId === id).result.headOption)
  }

  def findByClientCredentialsReturnOauthUser(clientId: UUID, clientSecret: String, grandType: String): Future[Option[OauthUsersRow]] = {
    val subquery = oauthclientquery.filter(n => (n.oauthClientId === clientId) && (n.clientSecret === clientSecret) && (n.grantType === grandType))
    dbConfig.db.run(oauthuserquery.filter(_.guid in subquery.map(_.oauthUserGuid)).result.headOption)
  }

  def create(oauthclient: OauthClientsRow): Future[Int] = {
    val c = oauthclient.copy(
        oauthClientId = java.util.UUID.randomUUID,
        expiresIn = 0
    )
    dbConfig.db.run(oauthclientquery += c)
  }

  def update(oauthclient: OauthClientsRow): Future[Unit] = {
    dbConfig.db.run(oauthclientquery.filter(_.oauthClientId === oauthclient.oauthClientId).update(oauthclient).map(_ => ()))
  }

  def update_mappinged(oauthclient: OauthClientsRow): Future[Int] = {
     dbConfig.db.run(oauthclientquery.filter(_.oauthClientId === oauthclient.oauthClientId).map(
       c => (
          c.oauthUserGuid,
          c.clientSecret,
          c.redirectUri,
          c.grantType,
          c.expiresIn
          )
      ).update(
        (
        oauthclient.oauthUserGuid,
        oauthclient.clientSecret,
        oauthclient.redirectUri,
        oauthclient.grantType,
        oauthclient.expiresIn
        )
      )
    )

  }
  
  def delete(id: UUID): Future[Int] = dbConfig.db.run(oauthclientquery.filter(_.oauthClientId === id).delete)

  def filterQueryOauthClient(tablequery:TableQuery[OauthClients], urlquery: Map[String, String]) = {
    val dummyid: UUID = null
    var returnquery = tablequery.filterNot(_.oauthClientId === dummyid)
    urlquery.foreach { querytuple =>
        querytuple._1 match{
          case "oauthUserGuid" => if(!StringHelper.trim(querytuple._2).isEmpty()) returnquery = returnquery.filter(_.oauthUserGuid === java.util.UUID.fromString(querytuple._2).bind) 
          case _ => 
        }
    }
    returnquery
  }

  def filterQueryOauthUser(tablequery:TableQuery[OauthUsers], subtablequery:TableQuery[Clients], urlquery: Map[String, String], applicationId: Long) = {
    var returnquery = tablequery.filter(_.applicationId === applicationId)
    urlquery.foreach { querytuple =>
        querytuple._1 match{
          case "oauthUserName" => if(!StringHelper.trim(querytuple._2).isEmpty()) returnquery = returnquery.filter(_.name like ("%" + StringHelper.trim(querytuple._2) + "%").bind) 
          case "agencyId" => if(querytuple._2 != "-1") returnquery = returnquery.filter(_.agencyId === querytuple._2.toLong.bind) 
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

}

