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

import utilities._
import utilities.valid._

class AccountsDAO @Inject()(dbConfigProvider: DatabaseConfigProvider)  extends BaseDAO[AccountsRow, Int]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val accountquery = TableQuery[Accounts]
 

  def authenticate(loginId: String, password: String): Future[Option[AccountsRow]] = {
    this.findByLoginId(loginId).map( accountOpt =>
      accountOpt.flatMap { accountdata =>
        if (BCrypt.checkpw(password, accountdata.password))
          Some(new AccountsRow(
              accountdata.id,
              accountdata.loginId,
              accountdata.password,
              accountdata.name,
              accountdata.email,
              accountdata.role,
              accountdata.isDisabled,
              accountdata.updater,
              accountdata.createdDate,
              accountdata.updatedDate
              ))
        else
          None
      }
     )
  }
  

  def all(): Future[List[AccountsRow]] = {
    dbConfig.db.run(accountquery.filter(_.isDisabled === RecordValid.IsEnabledRecord).sortBy(c => c.id.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(accountquery.filter(_.isDisabled === RecordValid.IsEnabledRecord).length.result)
  }

  def findByLoginId(loginId: String): Future[Option[AccountsRow]] = {
    dbConfig.db.run(accountquery.filter(account => account.isDisabled === RecordValid.IsEnabledRecord && account.loginId === loginId).result.map(_.headOption))
  }

  def findById(id: Int): Future[Option[AccountsRow]] = {
    dbConfig.db.run(accountquery.filter(account => account.isDisabled === RecordValid.IsEnabledRecord && account.id === id).result.headOption)
  }

  def create(account: AccountsRow): Future[Int] = {
    val c = account.copy(
        password = this.encryptpassword(account.password)
    )
    dbConfig.db.run(accountquery += c)
  }

  def update(account: AccountsRow): Future[Unit] = {
    dbConfig.db.run(accountquery.filter(_.id === account.id).update(account).map(_ => ()))
  }

  def update_mappinged(account: AccountsRow): Future[Int] = {
    Option(account.password) match{
      case Some(s) if ((s == null) || (s.trim.isEmpty)) =>
         dbConfig.db.run(accountquery.filter(_.id === account.id).map(
           c => (
              c.loginId,
              c.name,
              c.email,
              c.role
              )
          ).update(
            (
            account.loginId,
            account.name,
            account.email,
            account.role
            )
          )
        )
        
      case _ =>
         dbConfig.db.run(accountquery.filter(_.id === account.id).map(
           c => (
              c.loginId,
              c.name,
              c.email,
              c.password,
              c.role
              )
          ).update(
            (
            account.loginId,
            account.name,
            account.email,
            this.encryptpassword(account.password),
            account.role
            )
          )
        )
        
    }
  }
  
  def delete(id: Int): Future[Int] = dbConfig.db.run(accountquery.filter(_.id === id).delete)
  
  def disabled(id: Int, updator: Int): Future[Int] = {
        dbConfig.db.run(accountquery.filter(_.id === id).map(
           c => (
              c.isDisabled,
              c.updater,
              c.updatedDate
              )
          ).update(
            (
            Option(true),
            Option(updator),
            new Date
            )
          )
        )
     
  }

  def encryptpassword(password: String):String = {
    return  BCrypt.hashpw(password, BCrypt.gensalt())
  }

}

