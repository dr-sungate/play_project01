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

class BillingDestinationsDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) extends BaseDAO[BillingDestinationsRow, Long]{
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val billingdestinationsquery = TableQuery[BillingDestinations]
  private val billingaddressesquery = TableQuery[BillingAddresses]
  private val contractbillingdestinationquery = TableQuery[ContractBillingDestination]
  private val contractquery = TableQuery[Contracts]

  def all(): Future[List[BillingDestinationsRow]] = {
    dbConfig.db.run(billingdestinationsquery.sortBy(c => c.id.desc).result).map(_.toList)
  }

  def count(): Future[Int] = {
    dbConfig.db.run(billingdestinationsquery.length.result)
  }

 
  def findById(id: Long): Future[Option[BillingDestinationsRow]] = {
    dbConfig.db.run(billingdestinationsquery.filter(_.id === id).result.headOption)
  }

  def findByAgencyId(id: Long): Future[List[BillingDestinationsRow]] = {
    dbConfig.db.run(billingdestinationsquery.filter(_.agencyId === id).sortBy(c => c.id.desc).result).map(_.toList)
  }

  def findByAgencyIdWithAddress(id: Long): Future[Seq[(BillingDestinationsRow, BillingAddressesRow)]] = {
   var joinquery = (for{
      joinlist <- billingdestinationsquery.filter(_.agencyId === id) join billingaddressesquery on (_.id === _.billingDestinationId) 
    }yield (joinlist)
    ).sortBy(joinlist => joinlist._1.id.desc).result
    dbConfig.db.run(joinquery.transactionally)
  }

  def findContractBillingDestinationByClientId(clientId: Long): Future[Option[ContractBillingDestinationRow]] = {
    dbConfig.db.run(contractbillingdestinationquery.filter(_.contractId in contractquery.filter(_.clientId === clientId).map(_.id)).result.headOption)
  }

  def findByContractIdWithAddress(contractId: Long): Future[Option[(BillingDestinationsRow, BillingAddressesRow)]] = {
    var joinquery = (for{
      joinlist <- billingdestinationsquery.filter(_.id in contractbillingdestinationquery.filter(_.contractId === contractId).map(_.billingDestinationId)) join billingaddressesquery on (_.id === _.billingDestinationId) 
    }yield (joinlist)
    ).result.headOption

    dbConfig.db.run(joinquery.transactionally)
  }

  def findByClientIdWithAddress(clientId: Long): Future[Option[(BillingDestinationsRow, BillingAddressesRow)]] = {
    var joinquery = (for{
      joinlist <- billingdestinationsquery.filter(_.id in contractbillingdestinationquery.filter(_.contractId in contractquery.filter(_.clientId === clientId).map(_.id)).map(_.billingDestinationId)) join billingaddressesquery on (_.id === _.billingDestinationId) 
    }yield (joinlist)
    ).result.headOption

    dbConfig.db.run(joinquery.transactionally)
  }

  def create(billingdestination: BillingDestinationsRow): Future[Int] = {
    val c = billingdestination.copy(
        createdDate = new Date,
        updatedDate = new Date
    )
    dbConfig.db.run(billingdestinationsquery += c)
  }

  def create(billingdestinationbillingaddress: BillingDestinationsBillingAddressesRow): Future[Long] = {
      val billingdestinationnew = new BillingDestinationsRow(0, billingdestinationbillingaddress.agencyId, billingdestinationbillingaddress.billingDestinationName, billingdestinationbillingaddress.invoiceIssueType, billingdestinationbillingaddress.dueDateMonth, billingdestinationbillingaddress.dueDateDay, billingdestinationbillingaddress.closingDate, billingdestinationbillingaddress.pcaCode, billingdestinationbillingaddress.pcaCommonName, billingdestinationbillingaddress.memo, billingdestinationbillingaddress.updater, new Date, new Date)
      val action =
      (for {
       newId <- (billingdestinationsquery returning billingdestinationsquery.map(_.id) += billingdestinationnew)
       // その結果を使って更新
       _ <- billingaddressesquery += new BillingAddressesRow(newId, billingdestinationbillingaddress.billingAddresses.company, billingdestinationbillingaddress.billingAddresses.postCode, billingdestinationbillingaddress.billingAddresses.prefecture, billingdestinationbillingaddress.billingAddresses.city, billingdestinationbillingaddress.billingAddresses.address1, billingdestinationbillingaddress.billingAddresses.address2, billingdestinationbillingaddress.billingAddresses.address3, billingdestinationbillingaddress.billingAddresses.depertment, billingdestinationbillingaddress.billingAddresses.staff, billingdestinationbillingaddress.billingAddresses.staffEmail, billingdestinationbillingaddress.billingAddresses.phone, billingdestinationbillingaddress.billingAddresses.fax, billingdestinationbillingaddress.billingAddresses.updater,new Date,new Date)
     } yield (newId) )

     dbConfig.db.run(action.transactionally)
  }

  def update(billingdestination: BillingDestinationsRow): Future[Unit] = {
    dbConfig.db.run(billingdestinationsquery.filter(_.id === billingdestination.id).update(billingdestination).map(_ => ()))
  }

  def update_mappinged(billingdestination: BillingDestinationsRow): Future[Int] = {
     dbConfig.db.run(billingdestinationsquery.filter(_.id === billingdestination.id).map(
         c => (
            c.agencyId,
            c.billingDestinationName,
            c.invoiceIssueType,
            c.dueDateMonth,
            c.dueDateDay,
            c.closingDate,
            c.pcaCode,
            c.pcaCommonName,
            c.memo,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          billingdestination.agencyId,
          billingdestination.billingDestinationName,
          billingdestination.invoiceIssueType,
          billingdestination.dueDateMonth,
          billingdestination.dueDateDay,
          billingdestination.closingDate,
          billingdestination.pcaCode,
          billingdestination.pcaCommonName,
          billingdestination.memo,
          billingdestination.updater,
          new Date
        )
      )
    )
  }

  def update_mappinged(billingdestinationbillingaddress: BillingDestinationsBillingAddressesRow): Future[Int] = {
    val action =
    (for {
       updatedBillingDestinationsId <- billingdestinationsquery.filter(_.id === billingdestinationbillingaddress.id).map(
           c => (
            c.agencyId,
            c.billingDestinationName,
            c.invoiceIssueType,
            c.dueDateMonth,
            c.dueDateDay,
            c.closingDate,
            c.pcaCode,
            c.pcaCommonName,
            c.memo,
            c.updater,
            c.updatedDate
              )
        ).update(
            (
          billingdestinationbillingaddress.agencyId,
          billingdestinationbillingaddress.billingDestinationName,
          billingdestinationbillingaddress.invoiceIssueType,
          billingdestinationbillingaddress.dueDateMonth,
          billingdestinationbillingaddress.dueDateDay,
          billingdestinationbillingaddress.closingDate,
          billingdestinationbillingaddress.pcaCode,
          billingdestinationbillingaddress.pcaCommonName,
          billingdestinationbillingaddress.memo,
          billingdestinationbillingaddress.updater,
          new Date
          )
        )
       // その結果を使って更新
       _ <- billingaddressesquery.filter(_.billingDestinationId === billingdestinationbillingaddress.id).map(
         c => (
            c.company,
            c.postCode,
            c.prefecture,
            c.city,
            c.address1,
            c.address2,
            c.address3,
            c.depertment,
            c.staff,
            c.staffEmail,
            c.phone,
            c.fax,
            c.updater,
            c.updatedDate
            )
      ).update(
          (
          billingdestinationbillingaddress.billingAddresses.company,
          billingdestinationbillingaddress.billingAddresses.postCode,
          billingdestinationbillingaddress.billingAddresses.prefecture,
          billingdestinationbillingaddress.billingAddresses.city,
          billingdestinationbillingaddress.billingAddresses.address1,
          billingdestinationbillingaddress.billingAddresses.address2,
          billingdestinationbillingaddress.billingAddresses.address3,
          billingdestinationbillingaddress.billingAddresses.depertment,
          billingdestinationbillingaddress.billingAddresses.staff,
          billingdestinationbillingaddress.billingAddresses.staffEmail,
          billingdestinationbillingaddress.billingAddresses.phone,
          billingdestinationbillingaddress.billingAddresses.fax,
          billingdestinationbillingaddress.billingAddresses.updater,
          new Date
        )
      )
    } yield (updatedBillingDestinationsId) )
    
    dbConfig.db.run(action.transactionally)
  }

  def update_mappinged(contractbillingdestination: ContractBillingDestinationRow): Future[Int] = {
    val action =
    (for {
       _ <- contractbillingdestinationquery.filter(_.contractId === contractbillingdestination.contractId).delete
       // その結果を使って更新
       newRow <- contractbillingdestinationquery += contractbillingdestination
    } yield (newRow) )
    
    dbConfig.db.run(action.transactionally)
  }

    def delete(id: Long): Future[Int] = dbConfig.db.run(
      (billingdestinationsquery.filter(_.id === id).delete
      andThen billingaddressesquery.filter(_.billingDestinationId === id).delete).transactionally
   )

   def delete(id: Long, agencyId: Long): Future[Int] = dbConfig.db.run(
      (billingdestinationsquery.filter(c => (c.id === id && c.agencyId === agencyId)).delete
      andThen billingaddressesquery.filter(_.billingDestinationId === id).delete).transactionally
   )


}

