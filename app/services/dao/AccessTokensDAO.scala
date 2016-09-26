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

class AccessTokensDAO @Inject()(dbConfigProvider: DatabaseConfigProvider)  extends BaseDAO[AccessTokensRow, String]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val accesstokenquery = TableQuery[AccessTokens]
  private val oauthuserquery = TableQuery[OauthUsers]
 

  

  def all(): Future[List[AccessTokensRow]] = {
    dbConfig.db.run(accesstokenquery.sortBy(c => c.accessToken.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(accesstokenquery.length.result)
  }

  def findById(token: String): Future[Option[AccessTokensRow]] = {
    dbConfig.db.run(accesstokenquery.filter(_.accessToken === token).result.headOption)
  }

  def findByAccessToken(token: String): Future[Option[AccessTokensRow]] = {
    findById(token)
  }

  def findByRefreshToken(token: String): Future[Option[AccessTokensRow]] = {
    dbConfig.db.run(accesstokenquery.filter(_.refreshToken === token).result.headOption)
  }

  def findByToken(userGuid: UUID, clientId: UUID): Future[Option[AccessTokensRow]] = {
    dbConfig.db.run(accesstokenquery.filter(a => a.oauthUserGuid === userGuid && a.oauthClientId === clientId).result.headOption)
  }

  def create(account: AccessTokensRow): Future[Int] = {
    val c = account.copy(
        createdDate = new Date
    )
    dbConfig.db.run(accesstokenquery += c)
  }

  def update(accesstoken: AccessTokensRow): Future[Unit] = {
    dbConfig.db.run(accesstokenquery.filter(_.accessToken === accesstoken.accessToken).update(accesstoken).map(_ => ()))
  }

  
  def deleteExistingAndCreate(accessToken: AccessTokensRow, userGuid: UUID, clientId: UUID): Future[Int] = {
    val action =
    (for {
     _ <- accesstokenquery.filter(a => a.oauthClientId === clientId && a.oauthUserGuid === userGuid).delete
     newId <- accesstokenquery += accessToken
    } yield (newId) )

    dbConfig.db.run(action.transactionally)
  }
  
  def getWithOauthUserByRefreshToken(refreshToken: String): Future[Option[(AccessTokensRow, OauthUsersRow)]] ={
    var joinquery = (for{
      tokenlist <- accesstokenquery.filter(_.refreshToken === refreshToken) join oauthuserquery on (_.oauthUserGuid === _.guid) 
    }yield (tokenlist)
    ).result.headOption
    dbConfig.db.run(joinquery.transactionally)
    
  }

  def getWithOauthUserByAccessToken(accessToken: String): Future[Option[(AccessTokensRow, OauthUsersRow)]] ={
    var joinquery = (for{
      tokenlist <- accesstokenquery.filter(_.accessToken === accessToken) join oauthuserquery on (_.oauthUserGuid === _.guid) 
    }yield (tokenlist)
    ).result.headOption
    dbConfig.db.run(joinquery.transactionally)
    
  }
}

