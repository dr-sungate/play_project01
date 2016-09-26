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

class DefaultProductsDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[DefaultProductsRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val defaultproductsquery = TableQuery[DefaultProducts]
 

  def all(): Future[List[DefaultProductsRow]] = {
    dbConfig.db.run(defaultproductsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord)).sortBy(c => c.id.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(defaultproductsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord)).length.result)
  }

 
  def findById(id: Long): Future[Option[DefaultProductsRow]] = {
    dbConfig.db.run(defaultproductsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord && n.id === id)).result.headOption)
  }

  def findByIdAndAgencyId(id: Long, agencyId: Long): Future[Option[DefaultProductsRow]] = {
    dbConfig.db.run(defaultproductsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord && n.id === id && n.agencyId === agencyId)).result.headOption)
  }

  def findByAgencyId(id: Long): Future[List[DefaultProductsRow]] = {
    dbConfig.db.run(defaultproductsquery.filter(n => n.agencyId === id && n.isDisabled === RecordValid.IsEnabledRecord).sortBy(c => c.id.desc).result).map(_.toList)
  }

  def create(defaultproduct: DefaultProductsRow): Future[Int] = {
    val c = defaultproduct.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(defaultproductsquery += c)
  }

  def update(defaultproduct: DefaultProductsRow): Future[Unit] = {
    dbConfig.db.run(defaultproductsquery.filter(_.id === defaultproduct.id).update(defaultproduct).map(_ => ()))
  }

  def update_mappinged(defaultproduct: DefaultProductsRow): Future[Int] = {
     dbConfig.db.run(defaultproductsquery.filter(_.id === defaultproduct.id).map(
         c => (
            c.agencyId,
            c.productName,
            c.productType,
            c.price,
            c.memo,
            c.isDisabled,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          defaultproduct.agencyId,
          defaultproduct.productName,
          defaultproduct.productType,
          defaultproduct.price,
          defaultproduct.memo,
          defaultproduct.isDisabled,
          defaultproduct.updater,
          new Date
        )
      )
    )
  }
  
  def delete(id: Long): Future[Int] = dbConfig.db.run(defaultproductsquery.filter(_.id === id).delete)

  def disable(id: Long, updater: Int, agencyId: Long): Future[Int] = {
     dbConfig.db.run(defaultproductsquery.filter(n => n.id ===id && n.agencyId === agencyId).map(
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

  def getValidListMap(agencyId: Long): Future[Seq[Map[String,String]]] = {
     val query = (for {
      defaultproduct <- defaultproductsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord && n.agencyId === agencyId)).sortBy(c => c.id.desc)
    } yield (defaultproduct.id, defaultproduct.productName))
    
    dbConfig.db.run(query.result.map(rows => rows.map{case (id, productName) => Map("id"->id.toString, "productName" -> productName.getOrElse(""))}))
  }

  def getValidListForSelectOption(agencyId: Long): Future[Seq[(String,String)]] = {
    val query = (for {
      defaultproduct <- defaultproductsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord && n.agencyId === agencyId)).sortBy(c => c.id.desc)
    } yield (defaultproduct.id, defaultproduct.productName))
    
    dbConfig.db.run(query.result.map(rows => rows.map{case (id, productName) => (id.toString, productName.getOrElse(""))}))
  }

}

