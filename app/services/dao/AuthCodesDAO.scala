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

class AuthCodesDAO @Inject()(dbConfigProvider: DatabaseConfigProvider)  extends BaseDAO[AuthCodesRow, String]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val authcodequery = TableQuery[AuthCodes]
  private val oauthuserquery = TableQuery[OauthUsers]

  def all(): Future[List[AuthCodesRow]] = {
    dbConfig.db.run(authcodequery.sortBy(c => c.authorizationCode.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(authcodequery.length.result)
  }

  def findById(code: String): Future[Option[AuthCodesRow]] = {
    dbConfig.db.run(authcodequery.filter(_.authorizationCode === code).result.headOption)
  }

  def findByAuthorizationCode(code: String): Future[Option[AuthCodesRow]] = {
    findById(code)
  }

  def findByGUID(userGuid: UUID, clientId: UUID): Future[Option[AuthCodesRow]] = {
    dbConfig.db.run(authcodequery.filter(a => a.oauthClientId === clientId && a.oauthUserGuid === userGuid).result.headOption)
  }

  def create(account: AuthCodesRow): Future[Int] = {
    val c = account.copy(
        createdDate = new Date
    )
    dbConfig.db.run(authcodequery += c)
  }

  def update(authcode: AuthCodesRow): Future[Unit] = {
    dbConfig.db.run(authcodequery.filter(_.authorizationCode === authcode.authorizationCode).update(authcode).map(_ => ()))
  }

  
 def delete(code: String): Future[Int] = dbConfig.db.run(authcodequery.filter(_.authorizationCode === code).delete)

 def deleteExistingAndCreate(authcode: AuthCodesRow, userGuid: UUID, clientId: UUID): Future[Int] = {
    val action =
    (for {
     _ <- authcodequery.filter(a => a.oauthClientId === clientId && a.oauthUserGuid === userGuid).delete
     newId <- authcodequery += authcode
  } yield (newId) )

   dbConfig.db.run(action.transactionally)
  }
 
  def getWithOauthUserByCode(code: String): Future[Option[(AuthCodesRow, OauthUsersRow)]] ={
    var joinquery = (for{
      tokenlist <- authcodequery.filter(_.authorizationCode === code) join oauthuserquery on (_.oauthUserGuid === _.guid) 
    }yield (tokenlist)
    ).result.headOption
    dbConfig.db.run(joinquery.transactionally)
    
  }
}

