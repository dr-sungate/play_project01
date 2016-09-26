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

class ContractDetailsDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[ContractDetailsRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val contractdetailsquery = TableQuery[ContractDetails]
  private val contractdetailpricesquery = TableQuery[ContractDetailPrices]
 

  def all(): Future[List[ContractDetailsRow]] = {
    dbConfig.db.run(contractdetailsquery.filter(_.isDisabled === RecordValid.IsEnabledRecord).sortBy(c => c.id.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(contractdetailsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord)).length.result)
  }

 
  def findById(id: Long): Future[Option[ContractDetailsRow]] = {
    dbConfig.db.run(contractdetailsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord && n.id === id)).result.headOption)
  }

  def findByContractIdWithPrices(contractId: Long): Future[Seq[(ContractDetailsRow, ContractDetailPricesRow)]] = {
   var joinquery = (for{
      joinlist <- contractdetailsquery.filter(n => (n.isDisabled === RecordValid.IsEnabledRecord && n.contractId === contractId)) join contractdetailpricesquery on (_.id === _.contractDetailId) 
    }yield (joinlist)
    ).sortBy(joinlist => joinlist._1.id.desc).result
    dbConfig.db.run(joinquery.transactionally)
  }

  def create(contractdetail: ContractDetailsRow): Future[Int] = {
    val c = contractdetail.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(contractdetailsquery += c)
  }

  def create(contractDetailsContractDetailPrices: ContractDetailsContractDetailPricesRow): Future[Long] = {
      val contractDetailnew = new ContractDetailsRow(0, contractDetailsContractDetailPrices.contractId, contractDetailsContractDetailPrices.defaultProductId, contractDetailsContractDetailPrices.appProductId, contractDetailsContractDetailPrices.productName, contractDetailsContractDetailPrices.productType, contractDetailsContractDetailPrices.accountName, contractDetailsContractDetailPrices.details, contractDetailsContractDetailPrices.registedDate, contractDetailsContractDetailPrices.closeDate, contractDetailsContractDetailPrices.closeScheduledDate, contractDetailsContractDetailPrices.closeReason, contractDetailsContractDetailPrices.status, contractDetailsContractDetailPrices.memo, contractDetailsContractDetailPrices.isDisabled, contractDetailsContractDetailPrices.updater, new Date, new Date)
      val action =
      (for {
       newId <- (contractdetailsquery returning contractdetailsquery.map(_.id) += contractDetailnew)
       // その結果を使って更新
       _ <- contractdetailpricesquery += new ContractDetailPricesRow(newId, contractDetailsContractDetailPrices.contractDetailPrice.unitPrice, contractDetailsContractDetailPrices.contractDetailPrice.quantity, contractDetailsContractDetailPrices.contractDetailPrice.discount, contractDetailsContractDetailPrices.contractDetailPrice.discountRate, contractDetailsContractDetailPrices.contractDetailPrice.updater, new Date, new Date)
     } yield (newId) )

     dbConfig.db.run(action.transactionally)
  }
  def update(contractdetail: ContractDetailsRow): Future[Unit] = {
    dbConfig.db.run(contractdetailsquery.filter(_.id === contractdetail.id).update(contractdetail).map(_ => ()))
  }

  def update_mappinged(contractdetail: ContractDetailsRow): Future[Int] = {
     dbConfig.db.run(contractdetailsquery.filter(_.id === contractdetail.id).map(
         c => (
            c.contractId,
            c.defaultProductId,
            c.appProductId,
            c.productName,
            c.productType,
            c.accountName,
            c.details,
            c.registedDate,
            c.closeDate,
            c.closeScheduledDate,
            c.closeReason,
            c.status,
            c.memo,
            c.isDisabled,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          contractdetail.contractId,
          contractdetail.defaultProductId,
          contractdetail.appProductId,
          contractdetail.productName,
          contractdetail.productType,
          contractdetail.accountName,
          contractdetail.details,
          contractdetail.registedDate,
          contractdetail.closeDate,
          contractdetail.closeScheduledDate,
          contractdetail.closeReason,
          contractdetail.status,
          contractdetail.memo,
          contractdetail.isDisabled,
          contractdetail.updater,
          new Date
        )
      )
    )
  }

  def update_mappinged(contractDetailsContractDetailPrices: ContractDetailsContractDetailPricesRow): Future[Int] = {
    val action =
    (for {
       updatedContractDetailsId <- contractdetailsquery.filter(_.id === contractDetailsContractDetailPrices.id).map(
           c => (
            c.defaultProductId,
            c.appProductId,
            c.productName,
            c.productType,
            c.accountName,
            c.details,
            c.registedDate,
            c.closeDate,
            c.closeScheduledDate,
            c.closeReason,
            c.status,
            c.memo,
            c.updater,
            c.updatedDate
              )
        ).update(
            (
          contractDetailsContractDetailPrices.defaultProductId,
          contractDetailsContractDetailPrices.appProductId,
          contractDetailsContractDetailPrices.productName,
          contractDetailsContractDetailPrices.productType,
          contractDetailsContractDetailPrices.accountName,
          contractDetailsContractDetailPrices.details,
          contractDetailsContractDetailPrices.registedDate,
          contractDetailsContractDetailPrices.closeDate,
          contractDetailsContractDetailPrices.closeScheduledDate,
          contractDetailsContractDetailPrices.closeReason,
          contractDetailsContractDetailPrices.status,
          contractDetailsContractDetailPrices.memo,
          contractDetailsContractDetailPrices.updater,
          new Date
          )
        )
       // その結果を使って更新
       _ <- contractdetailpricesquery.filter(_.contractDetailId === contractDetailsContractDetailPrices.contractDetailPrice.contractDetailId).map(
         p => (
            p.unitPrice,
            p.quantity,
            p.discount,
            p.discountRate,
            p.updater,
            p.updatedDate
            )
      ).update(
          (
          contractDetailsContractDetailPrices.contractDetailPrice.unitPrice,
          contractDetailsContractDetailPrices.contractDetailPrice.quantity,
          contractDetailsContractDetailPrices.contractDetailPrice.discount,
          contractDetailsContractDetailPrices.contractDetailPrice.discountRate,
          contractDetailsContractDetailPrices.contractDetailPrice.updater,
          new Date
        )
      )
    } yield (updatedContractDetailsId) )
    
    dbConfig.db.run(action.transactionally)
  }

 
  def delete(id: Long): Future[Int] = dbConfig.db.run(contractdetailsquery.filter(_.id === id).delete)

  def disabled(id: Long, updater: Int): Future[Int] = {
     dbConfig.db.run(contractdetailsquery.filter(_.id === id).map(
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


}

