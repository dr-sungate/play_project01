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

class ContractDetailPricesDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[ContractDetailPricesRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val contractdetailpricepricesquery = TableQuery[ContractDetailPrices]
 

  def all(): Future[List[ContractDetailPricesRow]] = {
    dbConfig.db.run(contractdetailpricepricesquery.sortBy(c => c.contractDetailId.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(contractdetailpricepricesquery.length.result)
  }

 
  def findById(id: Long): Future[Option[ContractDetailPricesRow]] = {
    dbConfig.db.run(contractdetailpricepricesquery.filter(_.contractDetailId === id).result.headOption)
  }

  def create(contractdetailprice: ContractDetailPricesRow): Future[Int] = {
    val c = contractdetailprice.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(contractdetailpricepricesquery += c)
  }

  def update(contractdetailprice: ContractDetailPricesRow): Future[Unit] = {
    dbConfig.db.run(contractdetailpricepricesquery.filter(_.contractDetailId === contractdetailprice.contractDetailId).update(contractdetailprice).map(_ => ()))
  }

  def update_mappinged(contractdetailprice: ContractDetailPricesRow): Future[Int] = {
     dbConfig.db.run(contractdetailpricepricesquery.filter(_.contractDetailId === contractdetailprice.contractDetailId).map(
         c => (
            c.unitPrice,
            c.quantity,
            c.discount,
            c.discountRate,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          contractdetailprice.unitPrice,
          contractdetailprice.quantity,
          contractdetailprice.discount,
          contractdetailprice.discountRate,
          contractdetailprice.updater,
          new Date
        )
      )
    )
  }
  
  def delete(id: Long): Future[Int] = dbConfig.db.run(contractdetailpricepricesquery.filter(_.contractDetailId === id).delete)

 

}

