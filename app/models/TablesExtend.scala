package models

import java.sql.Timestamp
import java.util.Date
import org.joda.time.{DateTime,LocalDate,LocalTime,DateTimeZone}
import java.util.UUID


// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object TablesExtend extends {
  val profile = slick.driver.MySQLDriver
} with TablesExtend

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait TablesExtend {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  import slick.collection.heterogeneous._
  import slick.collection.heterogeneous.syntax._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  
  implicit def javaDateMapper = MappedColumnType.base[Date, Timestamp](
      dt => new Timestamp(dt.getTime),
      ts => new Date(ts.getTime)
  )

  implicit def javaLocalDateMapper = MappedColumnType.base[LocalDate, java.sql.Date](
      ld  => new java.sql.Date(ld.toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis),
      d => new LocalDate(d.getTime)
  )

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(AccessTokens.schema, Accounts.schema, Agencies.schema, ApplicationAgency.schema, Applications.schema, AuthCodes.schema, BillingAddresses.schema, BillingDestinations.schema, Clients.schema, ContractAddresses.schema, ContractBillingDestination.schema, ContractDetailBillings.schema, ContractDetailPrices.schema, ContractDetails.schema, ContractDetailSegmentLogs.schema, ContractDetailSegments.schema, Contracts.schema, DefaultProducts.schema, DefaultProductSegments.schema, InvoiceAddresses.schema, InvoiceDetails.schema, InvoiceProcessdates.schema, Invoices.schema, LoginHistories.schema, OauthClients.schema, OauthUsers.schema, PaymentHistories.schema, PlayEvolutions.schema, TaxRates.schema, Timezones.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /**
   * Add case class for view forms
   */
  case class OauthClientsSearchRow(oauthUserGuid: Option[String] = None, oauthUserName: Option[String] = None, agencyId: Option[Long] = None, clientName: Option[String] = None)
  case class OauthUsersSearchRow(agencyId: Option[Long] = None, name: Option[String] = None, clientName: Option[String] = None)
  case class AgenciesApplicationAgencyRow(id: Long, agencyName: String, `type`: Option[String] = None, status: String, memo: Option[String] = None, isDisabled: Option[Boolean] = None, timezoneId: Option[Int] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date, applicationAgency: ApplicationAgencyRow)
  case class AgenciesSearchRow(applicationId: Option[Long] = None, agencyName: Option[String] = None, `type`: Option[String] = None, status: Option[String] = None, timezoneId: Option[Int] = None)
  case class BillingDestinationsBillingAddressesRow(id: Long, agencyId: Long, billingDestinationName: Option[String] = None, invoiceIssueType: Option[String] = None, dueDateMonth: Option[String] = None, dueDateDay: Option[Int] = None, closingDate: Option[Int] = None, pcaCode: Option[String] = None, pcaCommonName: Option[String] = None, memo: Option[String] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date, billingAddresses: BillingAddressesRow)
  case class ClientsContractsRow(id: Long, applicationId: Long, agencyId: Long, appClientId: String, name: Option[String] = None, email: Option[String] = None, role: Option[String] = None, appCreatedDate: Option[Date] = None, lastLoginDate: Option[Date] = None, lastLoginIpaddress: Option[String] = None, loginCount: Option[Int] = None, memo: Option[String] = None, status: String, isDisabled: Option[Boolean] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date, contract: ContractsRow, contractAddress: ContractAddressesRow)
  case class ClientsSearchRow(agencyId: Option[Long] = None, appClientId: Option[String] = None, searchWord: Option[String] = None, role: Option[String] = None, clientStatus: Option[String] = None, contractStatus: Option[String] = None)
  case class ContractDetailsContractDetailPricesRow(id: Long, contractId: Long, defaultProductId: Option[Long] = None, appProductId: Option[String] = None, productName: Option[String] = None, productType: Option[String] = None, accountName: Option[String] = None, details: Option[String] = None, registedDate: Option[Date] = None, closeDate: Option[LocalDate] = None, closeScheduledDate: Option[LocalDate] = None, closeReason: Option[String] = None, status: String, memo: Option[String] = None, isDisabled: Option[Boolean] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date, contractDetailPrice: ContractDetailPricesRow)
 
  /** Entity class storing rows of table AccessTokens
   *  @param accessToken Database column access_token SqlType(VARCHAR), PrimaryKey, Length(255,true)
   *  @param refreshToken Database column refresh_token SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param oauthUserGuid Database column oauth_user_guid SqlType(BINARY)
   *  @param oauthClientId Database column oauth_client_id SqlType(BINARY)
   *  @param redirectUri Database column redirect_uri SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param scope Database column scope SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param expiresIn Database column expires_in SqlType(INT)
   *  @param createdDate Database column created_date SqlType(DATETIME) */
  case class AccessTokensRow(accessToken: String, refreshToken: Option[String] = None, oauthUserGuid: UUID, oauthClientId: UUID, redirectUri: Option[String] = None, scope: Option[String] = None, expiresIn: Int, createdDate: Date)
  /** GetResult implicit for fetching AccessTokensRow objects using plain SQL queries */
  implicit def GetResultAccessTokensRow(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[UUID], e3: GR[Int], e4: GR[Date]): GR[AccessTokensRow] = GR{
    prs => import prs._
    AccessTokensRow.tupled((<<[String], <<?[String], <<[UUID], <<[UUID], <<?[String], <<?[String], <<[Int], <<[Date]))
  }
  /** Table description of table access_tokens. Objects of this class serve as prototypes for rows in queries. */
  class AccessTokens(_tableTag: Tag) extends Table[AccessTokensRow](_tableTag, "access_tokens") {
    def * = (accessToken, refreshToken, oauthUserGuid, oauthClientId, redirectUri, scope, expiresIn, createdDate) <> (AccessTokensRow.tupled, AccessTokensRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(accessToken), refreshToken, Rep.Some(oauthUserGuid), Rep.Some(oauthClientId), redirectUri, scope, Rep.Some(expiresIn), Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> AccessTokensRow.tupled((_1.get, _2, _3.get, _4.get, _5, _6, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column access_token SqlType(VARCHAR), PrimaryKey, Length(255,true) */
    val accessToken: Rep[String] = column[String]("access_token", O.PrimaryKey, O.Length(255,varying=true))
    /** Database column refresh_token SqlType(VARCHAR), Length(255,true), Default(None) */
    val refreshToken: Rep[Option[String]] = column[Option[String]]("refresh_token", O.Length(255,varying=true), O.Default(None))
    /** Database column oauth_user_guid SqlType(BINARY) */
    val oauthUserGuid: Rep[UUID] = column[UUID]("oauth_user_guid")
    /** Database column oauth_client_id SqlType(BINARY) */
    val oauthClientId: Rep[UUID] = column[UUID]("oauth_client_id")
    /** Database column redirect_uri SqlType(VARCHAR), Length(255,true), Default(None) */
    val redirectUri: Rep[Option[String]] = column[Option[String]]("redirect_uri", O.Length(255,varying=true), O.Default(None))
    /** Database column scope SqlType(VARCHAR), Length(255,true), Default(None) */
    val scope: Rep[Option[String]] = column[Option[String]]("scope", O.Length(255,varying=true), O.Default(None))
    /** Database column expires_in SqlType(INT) */
    val expiresIn: Rep[Int] = column[Int]("expires_in")
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")

    /** Index over (oauthClientId) (database name oauth_client_id_index) */
    val index1 = index("oauth_client_id_index", oauthClientId)
    /** Index over (oauthUserGuid) (database name oauth_user_guid_index) */
    val index2 = index("oauth_user_guid_index", oauthUserGuid)
    /** Index over (refreshToken) (database name refresh_token_id_index) */
    val index3 = index("refresh_token_id_index", refreshToken)
  }
  /** Collection-like TableQuery object for table AccessTokens */
  lazy val AccessTokens = new TableQuery(tag => new AccessTokens(tag))

  /** Entity class storing rows of table Accounts
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param loginId Database column login_id SqlType(VARCHAR), Length(255,true)
   *  @param password Database column password SqlType(VARCHAR), Length(255,true)
   *  @param name Database column name SqlType(VARCHAR), Length(255,true)
   *  @param email Database column email SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param role Database column role SqlType(VARCHAR), Length(100,true)
   *  @param isDisabled Database column is_disabled SqlType(TINYINT), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class AccountsRow(id: Int, loginId: String, password: String, name: String, email: Option[String] = None, role: String, isDisabled: Option[Boolean] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching AccountsRow objects using plain SQL queries */
  implicit def GetResultAccountsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[Boolean]], e4: GR[Option[Int]], e5: GR[Date]): GR[AccountsRow] = GR{
    prs => import prs._
    AccountsRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<?[String], <<[String], <<?[Boolean], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table accounts. Objects of this class serve as prototypes for rows in queries. */
  class Accounts(_tableTag: Tag) extends Table[AccountsRow](_tableTag, "accounts") {
    def * = (id, loginId, password, name, email, role, isDisabled, updater, createdDate, updatedDate) <> (AccountsRow.tupled, AccountsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(loginId), Rep.Some(password), Rep.Some(name), email, Rep.Some(role), isDisabled, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> AccountsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6.get, _7, _8, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column login_id SqlType(VARCHAR), Length(255,true) */
    val loginId: Rep[String] = column[String]("login_id", O.Length(255,varying=true))
    /** Database column password SqlType(VARCHAR), Length(255,true) */
    val password: Rep[String] = column[String]("password", O.Length(255,varying=true))
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column email SqlType(VARCHAR), Length(255,true), Default(None) */
    val email: Rep[Option[String]] = column[Option[String]]("email", O.Length(255,varying=true), O.Default(None))
    /** Database column role SqlType(VARCHAR), Length(100,true) */
    val role: Rep[String] = column[String]("role", O.Length(100,varying=true))
    /** Database column is_disabled SqlType(TINYINT), Default(None) */
    val isDisabled: Rep[Option[Boolean]] = column[Option[Boolean]]("is_disabled", O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")

    /** Index over (isDisabled) (database name is_disabled_index) */
    val index1 = index("is_disabled_index", isDisabled)
    /** Index over (loginId) (database name login_id_index) */
    val index2 = index("login_id_index", loginId)
  }
  /** Collection-like TableQuery object for table Accounts */
  lazy val Accounts = new TableQuery(tag => new Accounts(tag))

  /** Entity class storing rows of table Agencies
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param agencyName Database column agency_name SqlType(VARCHAR), Length(255,true)
   *  @param `type` Database column type SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param status Database column status SqlType(VARCHAR), Length(100,true)
   *  @param memo Database column memo SqlType(TEXT), Default(None)
   *  @param isDisabled Database column is_disabled SqlType(TINYINT), Default(None)
   *  @param timezoneId Database column timezone_id SqlType(INT), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class AgenciesRow(id: Long, agencyName: String, `type`: Option[String] = None, status: String, memo: Option[String] = None, isDisabled: Option[Boolean] = None, timezoneId: Option[Int] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching AgenciesRow objects using plain SQL queries */
  implicit def GetResultAgenciesRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[Boolean]], e4: GR[Option[Int]], e5: GR[Date]): GR[AgenciesRow] = GR{
    prs => import prs._
    AgenciesRow.tupled((<<[Long], <<[String], <<?[String], <<[String], <<?[String], <<?[Boolean], <<?[Int], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table agencies. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class Agencies(_tableTag: Tag) extends Table[AgenciesRow](_tableTag, "agencies") {
    def * = (id, agencyName, `type`, status, memo, isDisabled, timezoneId, updater, createdDate, updatedDate) <> (AgenciesRow.tupled, AgenciesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(agencyName), `type`, Rep.Some(status), memo, isDisabled, timezoneId, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> AgenciesRow.tupled((_1.get, _2.get, _3, _4.get, _5, _6, _7, _8, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column agency_name SqlType(VARCHAR), Length(255,true) */
    val agencyName: Rep[String] = column[String]("agency_name", O.Length(255,varying=true))
    /** Database column type SqlType(VARCHAR), Length(100,true), Default(None)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[Option[String]] = column[Option[String]]("type", O.Length(100,varying=true), O.Default(None))
    /** Database column status SqlType(VARCHAR), Length(100,true) */
    val status: Rep[String] = column[String]("status", O.Length(100,varying=true))
    /** Database column memo SqlType(TEXT), Default(None) */
    val memo: Rep[Option[String]] = column[Option[String]]("memo", O.Default(None))
    /** Database column is_disabled SqlType(TINYINT), Default(None) */
    val isDisabled: Rep[Option[Boolean]] = column[Option[Boolean]]("is_disabled", O.Default(None))
    /** Database column timezone_id SqlType(INT), Default(None) */
    val timezoneId: Rep[Option[Int]] = column[Option[Int]]("timezone_id", O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")

    /** Index over (isDisabled) (database name is_disabled_index) */
    val index1 = index("is_disabled_index", isDisabled)
    /** Index over (status) (database name status_index) */
    val index2 = index("status_index", status)
  }
  /** Collection-like TableQuery object for table Agencies */
  lazy val Agencies = new TableQuery(tag => new Agencies(tag))

  /** Entity class storing rows of table ApplicationAgency
   *  @param applicationId Database column application_id SqlType(BIGINT)
   *  @param agencyId Database column agency_id SqlType(BIGINT)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME) */
  case class ApplicationAgencyRow(applicationId: Long, agencyId: Long, updater: Option[Int] = None, createdDate: Date)
  /** GetResult implicit for fetching ApplicationAgencyRow objects using plain SQL queries */
  implicit def GetResultApplicationAgencyRow(implicit e0: GR[Long], e1: GR[Option[Int]], e2: GR[Date]): GR[ApplicationAgencyRow] = GR{
    prs => import prs._
    ApplicationAgencyRow.tupled((<<[Long], <<[Long], <<?[Int], <<[Date]))
  }
  /** Table description of table application_agency. Objects of this class serve as prototypes for rows in queries. */
  class ApplicationAgency(_tableTag: Tag) extends Table[ApplicationAgencyRow](_tableTag, "application_agency") {
    def * = (applicationId, agencyId, updater, createdDate) <> (ApplicationAgencyRow.tupled, ApplicationAgencyRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(applicationId), Rep.Some(agencyId), updater, Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> ApplicationAgencyRow.tupled((_1.get, _2.get, _3, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column application_id SqlType(BIGINT) */
    val applicationId: Rep[Long] = column[Long]("application_id")
    /** Database column agency_id SqlType(BIGINT) */
    val agencyId: Rep[Long] = column[Long]("agency_id")
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")

    /** Primary key of ApplicationAgency (database name application_agency_PK) */
    val pk = primaryKey("application_agency_PK", (applicationId, agencyId))
  }
  /** Collection-like TableQuery object for table ApplicationAgency */
  lazy val ApplicationAgency = new TableQuery(tag => new ApplicationAgency(tag))

  /** Entity class storing rows of table Applications
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param appName Database column app_name SqlType(VARCHAR), Length(255,true)
   *  @param url Database column url SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param serverIps Database column server_ips SqlType(TEXT), Default(None)
   *  @param status Database column status SqlType(VARCHAR), Length(100,true)
   *  @param isDisabled Database column is_disabled SqlType(TINYINT), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class ApplicationsRow(id: Long, appName: String, url: Option[String] = None, serverIps: Option[String] = None, status: String, isDisabled: Option[Boolean] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching ApplicationsRow objects using plain SQL queries */
  implicit def GetResultApplicationsRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[Boolean]], e4: GR[Option[Int]], e5: GR[Date]): GR[ApplicationsRow] = GR{
    prs => import prs._
    ApplicationsRow.tupled((<<[Long], <<[String], <<?[String], <<?[String], <<[String], <<?[Boolean], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table applications. Objects of this class serve as prototypes for rows in queries. */
  class Applications(_tableTag: Tag) extends Table[ApplicationsRow](_tableTag, "applications") {
    def * = (id, appName, url, serverIps, status, isDisabled, updater, createdDate, updatedDate) <> (ApplicationsRow.tupled, ApplicationsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(appName), url, serverIps, Rep.Some(status), isDisabled, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> ApplicationsRow.tupled((_1.get, _2.get, _3, _4, _5.get, _6, _7, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column app_name SqlType(VARCHAR), Length(255,true) */
    val appName: Rep[String] = column[String]("app_name", O.Length(255,varying=true))
    /** Database column url SqlType(VARCHAR), Length(255,true), Default(None) */
    val url: Rep[Option[String]] = column[Option[String]]("url", O.Length(255,varying=true), O.Default(None))
    /** Database column server_ips SqlType(TEXT), Default(None) */
    val serverIps: Rep[Option[String]] = column[Option[String]]("server_ips", O.Default(None))
    /** Database column status SqlType(VARCHAR), Length(100,true) */
    val status: Rep[String] = column[String]("status", O.Length(100,varying=true))
    /** Database column is_disabled SqlType(TINYINT), Default(None) */
    val isDisabled: Rep[Option[Boolean]] = column[Option[Boolean]]("is_disabled", O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")

    /** Index over (isDisabled) (database name is_disabled_index) */
    val index1 = index("is_disabled_index", isDisabled)
    /** Index over (status) (database name status_index) */
    val index2 = index("status_index", status)
  }
  /** Collection-like TableQuery object for table Applications */
  lazy val Applications = new TableQuery(tag => new Applications(tag))

  /** Entity class storing rows of table AuthCodes
   *  @param authorizationCode Database column authorization_code SqlType(VARCHAR), PrimaryKey, Length(255,true)
   *  @param oauthUserGuid Database column oauth_user_guid SqlType(BINARY)
   *  @param oauthClientId Database column oauth_client_id SqlType(BINARY)
   *  @param redirectUri Database column redirect_uri SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param scope Database column scope SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param expiresIn Database column expires_in SqlType(INT)
   *  @param createdDate Database column created_date SqlType(DATETIME) */
  case class AuthCodesRow(authorizationCode: String, oauthUserGuid: UUID, oauthClientId: UUID, redirectUri: Option[String] = None, scope: Option[String] = None, expiresIn: Int, createdDate: Date)
  /** GetResult implicit for fetching AuthCodesRow objects using plain SQL queries */
  implicit def GetResultAuthCodesRow(implicit e0: GR[String], e1: GR[UUID], e2: GR[Option[String]], e3: GR[Int], e4: GR[Date]): GR[AuthCodesRow] = GR{
    prs => import prs._
    AuthCodesRow.tupled((<<[String], <<[UUID], <<[UUID], <<?[String], <<?[String], <<[Int], <<[Date]))
  }
  /** Table description of table auth_codes. Objects of this class serve as prototypes for rows in queries. */
  class AuthCodes(_tableTag: Tag) extends Table[AuthCodesRow](_tableTag, "auth_codes") {
    def * = (authorizationCode, oauthUserGuid, oauthClientId, redirectUri, scope, expiresIn, createdDate) <> (AuthCodesRow.tupled, AuthCodesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(authorizationCode), Rep.Some(oauthUserGuid), Rep.Some(oauthClientId), redirectUri, scope, Rep.Some(expiresIn), Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> AuthCodesRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column authorization_code SqlType(VARCHAR), PrimaryKey, Length(255,true) */
    val authorizationCode: Rep[String] = column[String]("authorization_code", O.PrimaryKey, O.Length(255,varying=true))
    /** Database column oauth_user_guid SqlType(BINARY) */
    val oauthUserGuid: Rep[UUID] = column[UUID]("oauth_user_guid")
    /** Database column oauth_client_id SqlType(BINARY) */
    val oauthClientId: Rep[UUID] = column[UUID]("oauth_client_id")
    /** Database column redirect_uri SqlType(VARCHAR), Length(255,true), Default(None) */
    val redirectUri: Rep[Option[String]] = column[Option[String]]("redirect_uri", O.Length(255,varying=true), O.Default(None))
    /** Database column scope SqlType(VARCHAR), Length(255,true), Default(None) */
    val scope: Rep[Option[String]] = column[Option[String]]("scope", O.Length(255,varying=true), O.Default(None))
    /** Database column expires_in SqlType(INT) */
    val expiresIn: Rep[Int] = column[Int]("expires_in")
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")

    /** Index over (oauthClientId) (database name oauth_client_id_index) */
    val index1 = index("oauth_client_id_index", oauthClientId)
    /** Index over (oauthUserGuid) (database name oauth_user_guid_index) */
    val index2 = index("oauth_user_guid_index", oauthUserGuid)
  }
  /** Collection-like TableQuery object for table AuthCodes */
  lazy val AuthCodes = new TableQuery(tag => new AuthCodes(tag))

  /** Entity class storing rows of table BillingAddresses
   *  @param billingDestinationId Database column billing_destination_id SqlType(BIGINT), PrimaryKey
   *  @param company Database column company SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param postCode Database column post_code SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param prefecture Database column prefecture SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param city Database column city SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param address1 Database column address1 SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param address2 Database column address2 SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param address3 Database column address3 SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param depertment Database column depertment SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param staff Database column staff SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param staffEmail Database column staff_email SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param phone Database column phone SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param fax Database column fax SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class BillingAddressesRow(billingDestinationId: Long, company: Option[String] = None, postCode: Option[String] = None, prefecture: Option[String] = None, city: Option[String] = None, address1: Option[String] = None, address2: Option[String] = None, address3: Option[String] = None, depertment: Option[String] = None, staff: Option[String] = None, staffEmail: Option[String] = None, phone: Option[String] = None, fax: Option[String] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching BillingAddressesRow objects using plain SQL queries */
  implicit def GetResultBillingAddressesRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[Int]], e3: GR[Date]): GR[BillingAddressesRow] = GR{
    prs => import prs._
    BillingAddressesRow.tupled((<<[Long], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table billing_addresses. Objects of this class serve as prototypes for rows in queries. */
  class BillingAddresses(_tableTag: Tag) extends Table[BillingAddressesRow](_tableTag, "billing_addresses") {
    def * = (billingDestinationId, company, postCode, prefecture, city, address1, address2, address3, depertment, staff, staffEmail, phone, fax, updater, createdDate, updatedDate) <> (BillingAddressesRow.tupled, BillingAddressesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(billingDestinationId), company, postCode, prefecture, city, address1, address2, address3, depertment, staff, staffEmail, phone, fax, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> BillingAddressesRow.tupled((_1.get, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15.get, _16.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column billing_destination_id SqlType(BIGINT), PrimaryKey */
    val billingDestinationId: Rep[Long] = column[Long]("billing_destination_id", O.PrimaryKey)
    /** Database column company SqlType(VARCHAR), Length(255,true), Default(None) */
    val company: Rep[Option[String]] = column[Option[String]]("company", O.Length(255,varying=true), O.Default(None))
    /** Database column post_code SqlType(VARCHAR), Length(255,true), Default(None) */
    val postCode: Rep[Option[String]] = column[Option[String]]("post_code", O.Length(255,varying=true), O.Default(None))
    /** Database column prefecture SqlType(VARCHAR), Length(255,true), Default(None) */
    val prefecture: Rep[Option[String]] = column[Option[String]]("prefecture", O.Length(255,varying=true), O.Default(None))
    /** Database column city SqlType(VARCHAR), Length(255,true), Default(None) */
    val city: Rep[Option[String]] = column[Option[String]]("city", O.Length(255,varying=true), O.Default(None))
    /** Database column address1 SqlType(VARCHAR), Length(255,true), Default(None) */
    val address1: Rep[Option[String]] = column[Option[String]]("address1", O.Length(255,varying=true), O.Default(None))
    /** Database column address2 SqlType(VARCHAR), Length(255,true), Default(None) */
    val address2: Rep[Option[String]] = column[Option[String]]("address2", O.Length(255,varying=true), O.Default(None))
    /** Database column address3 SqlType(VARCHAR), Length(255,true), Default(None) */
    val address3: Rep[Option[String]] = column[Option[String]]("address3", O.Length(255,varying=true), O.Default(None))
    /** Database column depertment SqlType(VARCHAR), Length(255,true), Default(None) */
    val depertment: Rep[Option[String]] = column[Option[String]]("depertment", O.Length(255,varying=true), O.Default(None))
    /** Database column staff SqlType(VARCHAR), Length(255,true), Default(None) */
    val staff: Rep[Option[String]] = column[Option[String]]("staff", O.Length(255,varying=true), O.Default(None))
    /** Database column staff_email SqlType(VARCHAR), Length(255,true), Default(None) */
    val staffEmail: Rep[Option[String]] = column[Option[String]]("staff_email", O.Length(255,varying=true), O.Default(None))
    /** Database column phone SqlType(VARCHAR), Length(255,true), Default(None) */
    val phone: Rep[Option[String]] = column[Option[String]]("phone", O.Length(255,varying=true), O.Default(None))
    /** Database column fax SqlType(VARCHAR), Length(255,true), Default(None) */
    val fax: Rep[Option[String]] = column[Option[String]]("fax", O.Length(255,varying=true), O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")
  }
  /** Collection-like TableQuery object for table BillingAddresses */
  lazy val BillingAddresses = new TableQuery(tag => new BillingAddresses(tag))

  /** Entity class storing rows of table BillingDestinations
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param agencyId Database column agency_id SqlType(BIGINT)
   *  @param billingDestinationName Database column billing_destination_name SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param invoiceIssueType Database column invoice_issue_type SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param dueDateMonth Database column due_date_month SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param dueDateDay Database column due_date_day SqlType(INT), Default(None)
   *  @param closingDate Database column closing_date SqlType(INT), Default(None)
   *  @param pcaCode Database column pca_code SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param pcaCommonName Database column pca_common_name SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param memo Database column memo SqlType(TEXT), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class BillingDestinationsRow(id: Long, agencyId: Long, billingDestinationName: Option[String] = None, invoiceIssueType: Option[String] = None, dueDateMonth: Option[String] = None, dueDateDay: Option[Int] = None, closingDate: Option[Int] = None, pcaCode: Option[String] = None, pcaCommonName: Option[String] = None, memo: Option[String] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching BillingDestinationsRow objects using plain SQL queries */
  implicit def GetResultBillingDestinationsRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[Int]], e3: GR[Date]): GR[BillingDestinationsRow] = GR{
    prs => import prs._
    BillingDestinationsRow.tupled((<<[Long], <<[Long], <<?[String], <<?[String], <<?[String], <<?[Int], <<?[Int], <<?[String], <<?[String], <<?[String], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table billing_destinations. Objects of this class serve as prototypes for rows in queries. */
  class BillingDestinations(_tableTag: Tag) extends Table[BillingDestinationsRow](_tableTag, "billing_destinations") {
    def * = (id, agencyId, billingDestinationName, invoiceIssueType, dueDateMonth, dueDateDay, closingDate, pcaCode, pcaCommonName, memo, updater, createdDate, updatedDate) <> (BillingDestinationsRow.tupled, BillingDestinationsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(agencyId), billingDestinationName, invoiceIssueType, dueDateMonth, dueDateDay, closingDate, pcaCode, pcaCommonName, memo, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> BillingDestinationsRow.tupled((_1.get, _2.get, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12.get, _13.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column agency_id SqlType(BIGINT) */
    val agencyId: Rep[Long] = column[Long]("agency_id")
    /** Database column billing_destination_name SqlType(VARCHAR), Length(255,true), Default(None) */
    val billingDestinationName: Rep[Option[String]] = column[Option[String]]("billing_destination_name", O.Length(255,varying=true), O.Default(None))
    /** Database column invoice_issue_type SqlType(VARCHAR), Length(100,true), Default(None) */
    val invoiceIssueType: Rep[Option[String]] = column[Option[String]]("invoice_issue_type", O.Length(100,varying=true), O.Default(None))
    /** Database column due_date_month SqlType(VARCHAR), Length(100,true), Default(None) */
    val dueDateMonth: Rep[Option[String]] = column[Option[String]]("due_date_month", O.Length(100,varying=true), O.Default(None))
    /** Database column due_date_day SqlType(INT), Default(None) */
    val dueDateDay: Rep[Option[Int]] = column[Option[Int]]("due_date_day", O.Default(None))
    /** Database column closing_date SqlType(INT), Default(None) */
    val closingDate: Rep[Option[Int]] = column[Option[Int]]("closing_date", O.Default(None))
    /** Database column pca_code SqlType(VARCHAR), Length(255,true), Default(None) */
    val pcaCode: Rep[Option[String]] = column[Option[String]]("pca_code", O.Length(255,varying=true), O.Default(None))
    /** Database column pca_common_name SqlType(VARCHAR), Length(255,true), Default(None) */
    val pcaCommonName: Rep[Option[String]] = column[Option[String]]("pca_common_name", O.Length(255,varying=true), O.Default(None))
    /** Database column memo SqlType(TEXT), Default(None) */
    val memo: Rep[Option[String]] = column[Option[String]]("memo", O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")

    /** Index over (agencyId) (database name agency_id_index) */
    val index1 = index("agency_id_index", agencyId)
  }
  /** Collection-like TableQuery object for table BillingDestinations */
  lazy val BillingDestinations = new TableQuery(tag => new BillingDestinations(tag))

  /** Entity class storing rows of table Clients
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param applicationId Database column application_id SqlType(BIGINT)
   *  @param agencyId Database column agency_id SqlType(BIGINT)
   *  @param appClientId Database column app_client_id SqlType(VARCHAR), Length(255,true)
   *  @param name Database column name SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param email Database column email SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param role Database column role SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param appCreatedDate Database column app_created_date SqlType(DATETIME), Default(None)
   *  @param lastLoginDate Database column last_login_date SqlType(DATETIME), Default(None)
   *  @param lastLoginIpaddress Database column last_login_ipaddress SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param loginCount Database column login_count SqlType(INT), Default(None)
   *  @param memo Database column memo SqlType(TEXT), Default(None)
   *  @param status Database column status SqlType(VARCHAR), Length(100,true)
   *  @param isDisabled Database column is_disabled SqlType(TINYINT), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class ClientsRow(id: Long, applicationId: Long, agencyId: Long, appClientId: String, name: Option[String] = None, email: Option[String] = None, role: Option[String] = None, appCreatedDate: Option[Date] = None, lastLoginDate: Option[Date] = None, lastLoginIpaddress: Option[String] = None, loginCount: Option[Int] = None, memo: Option[String] = None, status: String, isDisabled: Option[Boolean] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching ClientsRow objects using plain SQL queries */
  implicit def GetResultClientsRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[Date]], e4: GR[Option[Int]], e5: GR[Option[Boolean]], e6: GR[Date]): GR[ClientsRow] = GR{
    prs => import prs._
    ClientsRow.tupled((<<[Long], <<[Long], <<[Long], <<[String], <<?[String], <<?[String], <<?[String], <<?[Date], <<?[Date], <<?[String], <<?[Int], <<?[String], <<[String], <<?[Boolean], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table clients. Objects of this class serve as prototypes for rows in queries. */
  class Clients(_tableTag: Tag) extends Table[ClientsRow](_tableTag, "clients") {
    def * = (id, applicationId, agencyId, appClientId, name, email, role, appCreatedDate, lastLoginDate, lastLoginIpaddress, loginCount, memo, status, isDisabled, updater, createdDate, updatedDate) <> (ClientsRow.tupled, ClientsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(applicationId), Rep.Some(agencyId), Rep.Some(appClientId), name, email, role, appCreatedDate, lastLoginDate, lastLoginIpaddress, loginCount, memo, Rep.Some(status), isDisabled, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> ClientsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8, _9, _10, _11, _12, _13.get, _14, _15, _16.get, _17.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column application_id SqlType(BIGINT) */
    val applicationId: Rep[Long] = column[Long]("application_id")
    /** Database column agency_id SqlType(BIGINT) */
    val agencyId: Rep[Long] = column[Long]("agency_id")
    /** Database column app_client_id SqlType(VARCHAR), Length(255,true) */
    val appClientId: Rep[String] = column[String]("app_client_id", O.Length(255,varying=true))
    /** Database column name SqlType(VARCHAR), Length(255,true), Default(None) */
    val name: Rep[Option[String]] = column[Option[String]]("name", O.Length(255,varying=true), O.Default(None))
    /** Database column email SqlType(VARCHAR), Length(255,true), Default(None) */
    val email: Rep[Option[String]] = column[Option[String]]("email", O.Length(255,varying=true), O.Default(None))
    /** Database column role SqlType(VARCHAR), Length(255,true), Default(None) */
    val role: Rep[Option[String]] = column[Option[String]]("role", O.Length(255,varying=true), O.Default(None))
    /** Database column app_created_date SqlType(DATETIME), Default(None) */
    val appCreatedDate: Rep[Option[Date]] = column[Option[Date]]("app_created_date", O.Default(None))
    /** Database column last_login_date SqlType(DATETIME), Default(None) */
    val lastLoginDate: Rep[Option[Date]] = column[Option[Date]]("last_login_date", O.Default(None))
    /** Database column last_login_ipaddress SqlType(VARCHAR), Length(255,true), Default(None) */
    val lastLoginIpaddress: Rep[Option[String]] = column[Option[String]]("last_login_ipaddress", O.Length(255,varying=true), O.Default(None))
    /** Database column login_count SqlType(INT), Default(None) */
    val loginCount: Rep[Option[Int]] = column[Option[Int]]("login_count", O.Default(None))
    /** Database column memo SqlType(TEXT), Default(None) */
    val memo: Rep[Option[String]] = column[Option[String]]("memo", O.Default(None))
    /** Database column status SqlType(VARCHAR), Length(100,true) */
    val status: Rep[String] = column[String]("status", O.Length(100,varying=true))
    /** Database column is_disabled SqlType(TINYINT), Default(None) */
    val isDisabled: Rep[Option[Boolean]] = column[Option[Boolean]]("is_disabled", O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")

    /** Index over (agencyId) (database name agency_id_index) */
    val index1 = index("agency_id_index", agencyId)
    /** Index over (appClientId) (database name app_client_id_index) */
    val index2 = index("app_client_id_index", appClientId)
    /** Index over (applicationId) (database name application_id_index) */
    val index3 = index("application_id_index", applicationId)
    /** Index over (isDisabled) (database name is_disabled_index) */
    val index4 = index("is_disabled_index", isDisabled)
    /** Index over (status) (database name status_index) */
    val index5 = index("status_index", status)
  }
  /** Collection-like TableQuery object for table Clients */
  lazy val Clients = new TableQuery(tag => new Clients(tag))

  /** Entity class storing rows of table ContractAddresses
   *  @param contractId Database column contract_id SqlType(BIGINT), PrimaryKey
   *  @param company Database column company SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param postCode Database column post_code SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param prefecture Database column prefecture SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param city Database column city SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param address1 Database column address1 SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param address2 Database column address2 SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param address3 Database column address3 SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param depertment Database column depertment SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param staff Database column staff SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param staffEmail Database column staff_email SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param phone Database column phone SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param fax Database column fax SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class ContractAddressesRow(contractId: Long, company: Option[String] = None, postCode: Option[String] = None, prefecture: Option[String] = None, city: Option[String] = None, address1: Option[String] = None, address2: Option[String] = None, address3: Option[String] = None, depertment: Option[String] = None, staff: Option[String] = None, staffEmail: Option[String] = None, phone: Option[String] = None, fax: Option[String] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching ContractAddressesRow objects using plain SQL queries */
  implicit def GetResultContractAddressesRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[Int]], e3: GR[Date]): GR[ContractAddressesRow] = GR{
    prs => import prs._
    ContractAddressesRow.tupled((<<[Long], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table contract_addresses. Objects of this class serve as prototypes for rows in queries. */
  class ContractAddresses(_tableTag: Tag) extends Table[ContractAddressesRow](_tableTag, "contract_addresses") {
    def * = (contractId, company, postCode, prefecture, city, address1, address2, address3, depertment, staff, staffEmail, phone, fax, updater, createdDate, updatedDate) <> (ContractAddressesRow.tupled, ContractAddressesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(contractId), company, postCode, prefecture, city, address1, address2, address3, depertment, staff, staffEmail, phone, fax, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> ContractAddressesRow.tupled((_1.get, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15.get, _16.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column contract_id SqlType(BIGINT), PrimaryKey */
    val contractId: Rep[Long] = column[Long]("contract_id", O.PrimaryKey)
    /** Database column company SqlType(VARCHAR), Length(255,true), Default(None) */
    val company: Rep[Option[String]] = column[Option[String]]("company", O.Length(255,varying=true), O.Default(None))
    /** Database column post_code SqlType(VARCHAR), Length(255,true), Default(None) */
    val postCode: Rep[Option[String]] = column[Option[String]]("post_code", O.Length(255,varying=true), O.Default(None))
    /** Database column prefecture SqlType(VARCHAR), Length(255,true), Default(None) */
    val prefecture: Rep[Option[String]] = column[Option[String]]("prefecture", O.Length(255,varying=true), O.Default(None))
    /** Database column city SqlType(VARCHAR), Length(255,true), Default(None) */
    val city: Rep[Option[String]] = column[Option[String]]("city", O.Length(255,varying=true), O.Default(None))
    /** Database column address1 SqlType(VARCHAR), Length(255,true), Default(None) */
    val address1: Rep[Option[String]] = column[Option[String]]("address1", O.Length(255,varying=true), O.Default(None))
    /** Database column address2 SqlType(VARCHAR), Length(255,true), Default(None) */
    val address2: Rep[Option[String]] = column[Option[String]]("address2", O.Length(255,varying=true), O.Default(None))
    /** Database column address3 SqlType(VARCHAR), Length(255,true), Default(None) */
    val address3: Rep[Option[String]] = column[Option[String]]("address3", O.Length(255,varying=true), O.Default(None))
    /** Database column depertment SqlType(VARCHAR), Length(255,true), Default(None) */
    val depertment: Rep[Option[String]] = column[Option[String]]("depertment", O.Length(255,varying=true), O.Default(None))
    /** Database column staff SqlType(VARCHAR), Length(255,true), Default(None) */
    val staff: Rep[Option[String]] = column[Option[String]]("staff", O.Length(255,varying=true), O.Default(None))
    /** Database column staff_email SqlType(VARCHAR), Length(255,true), Default(None) */
    val staffEmail: Rep[Option[String]] = column[Option[String]]("staff_email", O.Length(255,varying=true), O.Default(None))
    /** Database column phone SqlType(VARCHAR), Length(255,true), Default(None) */
    val phone: Rep[Option[String]] = column[Option[String]]("phone", O.Length(255,varying=true), O.Default(None))
    /** Database column fax SqlType(VARCHAR), Length(255,true), Default(None) */
    val fax: Rep[Option[String]] = column[Option[String]]("fax", O.Length(255,varying=true), O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")
  }
  /** Collection-like TableQuery object for table ContractAddresses */
  lazy val ContractAddresses = new TableQuery(tag => new ContractAddresses(tag))

  /** Entity class storing rows of table ContractBillingDestination
   *  @param contractId Database column contract_id SqlType(BIGINT)
   *  @param billingDestinationId Database column billing_destination_id SqlType(BIGINT)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME) */
  case class ContractBillingDestinationRow(contractId: Long, billingDestinationId: Long, updater: Option[Int] = None, createdDate: Date)
  /** GetResult implicit for fetching ContractBillingDestinationRow objects using plain SQL queries */
  implicit def GetResultContractBillingDestinationRow(implicit e0: GR[Long], e1: GR[Option[Int]], e2: GR[Date]): GR[ContractBillingDestinationRow] = GR{
    prs => import prs._
    ContractBillingDestinationRow.tupled((<<[Long], <<[Long], <<?[Int], <<[Date]))
  }
  /** Table description of table contract_billing_destination. Objects of this class serve as prototypes for rows in queries. */
  class ContractBillingDestination(_tableTag: Tag) extends Table[ContractBillingDestinationRow](_tableTag, "contract_billing_destination") {
    def * = (contractId, billingDestinationId, updater, createdDate) <> (ContractBillingDestinationRow.tupled, ContractBillingDestinationRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(contractId), Rep.Some(billingDestinationId), updater, Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> ContractBillingDestinationRow.tupled((_1.get, _2.get, _3, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column contract_id SqlType(BIGINT) */
    val contractId: Rep[Long] = column[Long]("contract_id")
    /** Database column billing_destination_id SqlType(BIGINT) */
    val billingDestinationId: Rep[Long] = column[Long]("billing_destination_id")
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")

    /** Primary key of ContractBillingDestination (database name contract_billing_destination_PK) */
    val pk = primaryKey("contract_billing_destination_PK", (contractId, billingDestinationId))
  }
  /** Collection-like TableQuery object for table ContractBillingDestination */
  lazy val ContractBillingDestination = new TableQuery(tag => new ContractBillingDestination(tag))

  /** Entity class storing rows of table ContractDetailBillings
   *  @param contractDetailId Database column contract_detail_id SqlType(BIGINT), PrimaryKey
   *  @param activatedDate Database column activated_date SqlType(DATETIME), Default(None)
   *  @param billingType Database column billing_type SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param billingTerm Database column billing_term SqlType(INT), Default(None)
   *  @param firstBillingDate Database column first_billing_date SqlType(DATE), Default(None)
   *  @param lastBillingDate Database column last_billing_date SqlType(DATE), Default(None)
   *  @param nextBillingDate Database column next_billing_date SqlType(DATE), Default(None)
   *  @param billingSkip Database column billing_skip SqlType(TINYINT), Default(None)
   *  @param paymentType Database column payment_type SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param invoiceTermType Database column invoice_term_type SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class ContractDetailBillingsRow(contractDetailId: Long, activatedDate: Option[Date] = None, billingType: Option[String] = None, billingTerm: Option[Int] = None, firstBillingDate: Option[LocalDate] = None, lastBillingDate: Option[LocalDate] = None, nextBillingDate: Option[LocalDate] = None, billingSkip: Option[Boolean] = None, paymentType: Option[String] = None, invoiceTermType: Option[String] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching ContractDetailBillingsRow objects using plain SQL queries */
  implicit def GetResultContractDetailBillingsRow(implicit e0: GR[Long], e1: GR[Option[Date]], e2: GR[Option[String]], e3: GR[Option[Int]], e4: GR[Option[LocalDate]], e5: GR[Option[Boolean]], e6: GR[Date]): GR[ContractDetailBillingsRow] = GR{
    prs => import prs._
    ContractDetailBillingsRow.tupled((<<[Long], <<?[Date], <<?[String], <<?[Int], <<?[LocalDate], <<?[LocalDate], <<?[LocalDate], <<?[Boolean], <<?[String], <<?[String], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table contract_detail_billings. Objects of this class serve as prototypes for rows in queries. */
  class ContractDetailBillings(_tableTag: Tag) extends Table[ContractDetailBillingsRow](_tableTag, "contract_detail_billings") {
    def * = (contractDetailId, activatedDate, billingType, billingTerm, firstBillingDate, lastBillingDate, nextBillingDate, billingSkip, paymentType, invoiceTermType, updater, createdDate, updatedDate) <> (ContractDetailBillingsRow.tupled, ContractDetailBillingsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(contractDetailId), activatedDate, billingType, billingTerm, firstBillingDate, lastBillingDate, nextBillingDate, billingSkip, paymentType, invoiceTermType, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> ContractDetailBillingsRow.tupled((_1.get, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12.get, _13.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column contract_detail_id SqlType(BIGINT), PrimaryKey */
    val contractDetailId: Rep[Long] = column[Long]("contract_detail_id", O.PrimaryKey)
    /** Database column activated_date SqlType(DATETIME), Default(None) */
    val activatedDate: Rep[Option[Date]] = column[Option[Date]]("activated_date", O.Default(None))
    /** Database column billing_type SqlType(VARCHAR), Length(100,true), Default(None) */
    val billingType: Rep[Option[String]] = column[Option[String]]("billing_type", O.Length(100,varying=true), O.Default(None))
    /** Database column billing_term SqlType(INT), Default(None) */
    val billingTerm: Rep[Option[Int]] = column[Option[Int]]("billing_term", O.Default(None))
    /** Database column first_billing_date SqlType(DATE), Default(None) */
    val firstBillingDate: Rep[Option[LocalDate]] = column[Option[LocalDate]]("first_billing_date", O.Default(None))
    /** Database column last_billing_date SqlType(DATE), Default(None) */
    val lastBillingDate: Rep[Option[LocalDate]] = column[Option[LocalDate]]("last_billing_date", O.Default(None))
    /** Database column next_billing_date SqlType(DATE), Default(None) */
    val nextBillingDate: Rep[Option[LocalDate]] = column[Option[LocalDate]]("next_billing_date", O.Default(None))
    /** Database column billing_skip SqlType(TINYINT), Default(None) */
    val billingSkip: Rep[Option[Boolean]] = column[Option[Boolean]]("billing_skip", O.Default(None))
    /** Database column payment_type SqlType(VARCHAR), Length(100,true), Default(None) */
    val paymentType: Rep[Option[String]] = column[Option[String]]("payment_type", O.Length(100,varying=true), O.Default(None))
    /** Database column invoice_term_type SqlType(VARCHAR), Length(100,true), Default(None) */
    val invoiceTermType: Rep[Option[String]] = column[Option[String]]("invoice_term_type", O.Length(100,varying=true), O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")
  }
  /** Collection-like TableQuery object for table ContractDetailBillings */
  lazy val ContractDetailBillings = new TableQuery(tag => new ContractDetailBillings(tag))

  /** Entity class storing rows of table ContractDetailPrices
   *  @param contractDetailId Database column contract_detail_id SqlType(BIGINT), PrimaryKey
   *  @param unitPrice Database column unit_price SqlType(DECIMAL), Default(None)
   *  @param quantity Database column quantity SqlType(INT), Default(None)
   *  @param discount Database column discount SqlType(DECIMAL), Default(None)
   *  @param discountRate Database column discount_rate SqlType(DECIMAL), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class ContractDetailPricesRow(contractDetailId: Long, unitPrice: Option[scala.math.BigDecimal] = None, quantity: Option[Int] = None, discount: Option[scala.math.BigDecimal] = None, discountRate: Option[scala.math.BigDecimal] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching ContractDetailPricesRow objects using plain SQL queries */
  implicit def GetResultContractDetailPricesRow(implicit e0: GR[Long], e1: GR[Option[scala.math.BigDecimal]], e2: GR[Option[Int]], e3: GR[Date]): GR[ContractDetailPricesRow] = GR{
    prs => import prs._
    ContractDetailPricesRow.tupled((<<[Long], <<?[scala.math.BigDecimal], <<?[Int], <<?[scala.math.BigDecimal], <<?[scala.math.BigDecimal], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table contract_detail_prices. Objects of this class serve as prototypes for rows in queries. */
  class ContractDetailPrices(_tableTag: Tag) extends Table[ContractDetailPricesRow](_tableTag, "contract_detail_prices") {
    def * = (contractDetailId, unitPrice, quantity, discount, discountRate, updater, createdDate, updatedDate) <> (ContractDetailPricesRow.tupled, ContractDetailPricesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(contractDetailId), unitPrice, quantity, discount, discountRate, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> ContractDetailPricesRow.tupled((_1.get, _2, _3, _4, _5, _6, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column contract_detail_id SqlType(BIGINT), PrimaryKey */
    val contractDetailId: Rep[Long] = column[Long]("contract_detail_id", O.PrimaryKey)
    /** Database column unit_price SqlType(DECIMAL), Default(None) */
    val unitPrice: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("unit_price", O.Default(None))
    /** Database column quantity SqlType(INT), Default(None) */
    val quantity: Rep[Option[Int]] = column[Option[Int]]("quantity", O.Default(None))
    /** Database column discount SqlType(DECIMAL), Default(None) */
    val discount: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("discount", O.Default(None))
    /** Database column discount_rate SqlType(DECIMAL), Default(None) */
    val discountRate: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("discount_rate", O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")
  }
  /** Collection-like TableQuery object for table ContractDetailPrices */
  lazy val ContractDetailPrices = new TableQuery(tag => new ContractDetailPrices(tag))

  /** Entity class storing rows of table ContractDetails
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param contractId Database column contract_id SqlType(BIGINT)
   *  @param defaultProductId Database column default_product_id SqlType(BIGINT), Default(None)
   *  @param appProductId Database column app_product_id SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param productName Database column product_name SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param productType Database column product_type SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param accountName Database column account_name SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param details Database column details SqlType(TEXT), Default(None)
   *  @param registedDate Database column registed_date SqlType(DATETIME), Default(None)
   *  @param closeDate Database column close_date SqlType(DATE), Default(None)
   *  @param closeScheduledDate Database column close_scheduled_date SqlType(DATE), Default(None)
   *  @param closeReason Database column close_reason SqlType(TEXT), Default(None)
   *  @param status Database column status SqlType(VARCHAR), Length(100,true)
   *  @param memo Database column memo SqlType(TEXT), Default(None)
   *  @param isDisabled Database column is_disabled SqlType(TINYINT), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class ContractDetailsRow(id: Long, contractId: Long, defaultProductId: Option[Long] = None, appProductId: Option[String] = None, productName: Option[String] = None, productType: Option[String] = None, accountName: Option[String] = None, details: Option[String] = None, registedDate: Option[Date] = None, closeDate: Option[LocalDate] = None, closeScheduledDate: Option[LocalDate] = None, closeReason: Option[String] = None, status: String, memo: Option[String] = None, isDisabled: Option[Boolean] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching ContractDetailsRow objects using plain SQL queries */
  implicit def GetResultContractDetailsRow(implicit e0: GR[Long], e1: GR[Option[Long]], e2: GR[Option[String]], e3: GR[Option[Date]], e4: GR[Option[LocalDate]], e5: GR[String], e6: GR[Option[Boolean]], e7: GR[Option[Int]], e8: GR[Date]): GR[ContractDetailsRow] = GR{
    prs => import prs._
    ContractDetailsRow.tupled((<<[Long], <<[Long], <<?[Long], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[Date], <<?[LocalDate], <<?[LocalDate], <<?[String], <<[String], <<?[String], <<?[Boolean], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table contract_details. Objects of this class serve as prototypes for rows in queries. */
  class ContractDetails(_tableTag: Tag) extends Table[ContractDetailsRow](_tableTag, "contract_details") {
    def * = (id, contractId, defaultProductId, appProductId, productName, productType, accountName, details, registedDate, closeDate, closeScheduledDate, closeReason, status, memo, isDisabled, updater, createdDate, updatedDate) <> (ContractDetailsRow.tupled, ContractDetailsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(contractId), defaultProductId, appProductId, productName, productType, accountName, details, registedDate, closeDate, closeScheduledDate, closeReason, Rep.Some(status), memo, isDisabled, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> ContractDetailsRow.tupled((_1.get, _2.get, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13.get, _14, _15, _16, _17.get, _18.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column contract_id SqlType(BIGINT) */
    val contractId: Rep[Long] = column[Long]("contract_id")
    /** Database column default_product_id SqlType(BIGINT), Default(None) */
    val defaultProductId: Rep[Option[Long]] = column[Option[Long]]("default_product_id", O.Default(None))
    /** Database column app_product_id SqlType(VARCHAR), Length(255,true), Default(None) */
    val appProductId: Rep[Option[String]] = column[Option[String]]("app_product_id", O.Length(255,varying=true), O.Default(None))
    /** Database column product_name SqlType(VARCHAR), Length(255,true), Default(None) */
    val productName: Rep[Option[String]] = column[Option[String]]("product_name", O.Length(255,varying=true), O.Default(None))
    /** Database column product_type SqlType(VARCHAR), Length(255,true), Default(None) */
    val productType: Rep[Option[String]] = column[Option[String]]("product_type", O.Length(255,varying=true), O.Default(None))
    /** Database column account_name SqlType(VARCHAR), Length(255,true), Default(None) */
    val accountName: Rep[Option[String]] = column[Option[String]]("account_name", O.Length(255,varying=true), O.Default(None))
    /** Database column details SqlType(TEXT), Default(None) */
    val details: Rep[Option[String]] = column[Option[String]]("details", O.Default(None))
    /** Database column registed_date SqlType(DATETIME), Default(None) */
    val registedDate: Rep[Option[Date]] = column[Option[Date]]("registed_date", O.Default(None))
    /** Database column close_date SqlType(DATE), Default(None) */
    val closeDate: Rep[Option[LocalDate]] = column[Option[LocalDate]]("close_date", O.Default(None))
    /** Database column close_scheduled_date SqlType(DATE), Default(None) */
    val closeScheduledDate: Rep[Option[LocalDate]] = column[Option[LocalDate]]("close_scheduled_date", O.Default(None))
    /** Database column close_reason SqlType(TEXT), Default(None) */
    val closeReason: Rep[Option[String]] = column[Option[String]]("close_reason", O.Default(None))
    /** Database column status SqlType(VARCHAR), Length(100,true) */
    val status: Rep[String] = column[String]("status", O.Length(100,varying=true))
    /** Database column memo SqlType(TEXT), Default(None) */
    val memo: Rep[Option[String]] = column[Option[String]]("memo", O.Default(None))
    /** Database column is_disabled SqlType(TINYINT), Default(None) */
    val isDisabled: Rep[Option[Boolean]] = column[Option[Boolean]]("is_disabled", O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")

    /** Index over (contractId) (database name contract_id_index) */
    val index1 = index("contract_id_index", contractId)
    /** Index over (isDisabled) (database name is_disabled_index) */
    val index2 = index("is_disabled_index", isDisabled)
    /** Index over (status) (database name status_index) */
    val index3 = index("status_index", status)
  }
  /** Collection-like TableQuery object for table ContractDetails */
  lazy val ContractDetails = new TableQuery(tag => new ContractDetails(tag))

  /** Entity class storing rows of table ContractDetailSegmentLogs
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param clientId Database column client_id SqlType(BIGINT)
   *  @param contractDetailId Database column contract_detail_id SqlType(BIGINT)
   *  @param unitLog Database column unit_log SqlType(DECIMAL), Default(None)
   *  @param accountingFrom Database column accounting_from SqlType(DATETIME), Default(None)
   *  @param accountingTo Database column accounting_to SqlType(DATETIME), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME) */
  case class ContractDetailSegmentLogsRow(id: Long, clientId: Long, contractDetailId: Long, unitLog: Option[scala.math.BigDecimal] = None, accountingFrom: Option[Date] = None, accountingTo: Option[Date] = None, createdDate: Date)
  /** GetResult implicit for fetching ContractDetailSegmentLogsRow objects using plain SQL queries */
  implicit def GetResultContractDetailSegmentLogsRow(implicit e0: GR[Long], e1: GR[Option[scala.math.BigDecimal]], e2: GR[Option[Date]], e3: GR[Date]): GR[ContractDetailSegmentLogsRow] = GR{
    prs => import prs._
    ContractDetailSegmentLogsRow.tupled((<<[Long], <<[Long], <<[Long], <<?[scala.math.BigDecimal], <<?[Date], <<?[Date], <<[Date]))
  }
  /** Table description of table contract_detail_segment_logs. Objects of this class serve as prototypes for rows in queries. */
  class ContractDetailSegmentLogs(_tableTag: Tag) extends Table[ContractDetailSegmentLogsRow](_tableTag, "contract_detail_segment_logs") {
    def * = (id, clientId, contractDetailId, unitLog, accountingFrom, accountingTo, createdDate) <> (ContractDetailSegmentLogsRow.tupled, ContractDetailSegmentLogsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(clientId), Rep.Some(contractDetailId), unitLog, accountingFrom, accountingTo, Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> ContractDetailSegmentLogsRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column client_id SqlType(BIGINT) */
    val clientId: Rep[Long] = column[Long]("client_id")
    /** Database column contract_detail_id SqlType(BIGINT) */
    val contractDetailId: Rep[Long] = column[Long]("contract_detail_id")
    /** Database column unit_log SqlType(DECIMAL), Default(None) */
    val unitLog: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("unit_log", O.Default(None))
    /** Database column accounting_from SqlType(DATETIME), Default(None) */
    val accountingFrom: Rep[Option[Date]] = column[Option[Date]]("accounting_from", O.Default(None))
    /** Database column accounting_to SqlType(DATETIME), Default(None) */
    val accountingTo: Rep[Option[Date]] = column[Option[Date]]("accounting_to", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")

    /** Index over (clientId) (database name client_id_index) */
    val index1 = index("client_id_index", clientId)
    /** Index over (contractDetailId) (database name contract_detail_id_index) */
    val index2 = index("contract_detail_id_index", contractDetailId)
  }
  /** Collection-like TableQuery object for table ContractDetailSegmentLogs */
  lazy val ContractDetailSegmentLogs = new TableQuery(tag => new ContractDetailSegmentLogs(tag))

  /** Entity class storing rows of table ContractDetailSegments
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param contractDetailId Database column contract_detail_id SqlType(BIGINT)
   *  @param unit Database column unit SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param unitValue Database column unit_value SqlType(DECIMAL), Default(None)
   *  @param price Database column price SqlType(DECIMAL), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class ContractDetailSegmentsRow(id: Long, contractDetailId: Long, unit: Option[String] = None, unitValue: Option[scala.math.BigDecimal] = None, price: Option[scala.math.BigDecimal] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching ContractDetailSegmentsRow objects using plain SQL queries */
  implicit def GetResultContractDetailSegmentsRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[scala.math.BigDecimal]], e3: GR[Option[Int]], e4: GR[Date]): GR[ContractDetailSegmentsRow] = GR{
    prs => import prs._
    ContractDetailSegmentsRow.tupled((<<[Long], <<[Long], <<?[String], <<?[scala.math.BigDecimal], <<?[scala.math.BigDecimal], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table contract_detail_segments. Objects of this class serve as prototypes for rows in queries. */
  class ContractDetailSegments(_tableTag: Tag) extends Table[ContractDetailSegmentsRow](_tableTag, "contract_detail_segments") {
    def * = (id, contractDetailId, unit, unitValue, price, updater, createdDate, updatedDate) <> (ContractDetailSegmentsRow.tupled, ContractDetailSegmentsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(contractDetailId), unit, unitValue, price, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> ContractDetailSegmentsRow.tupled((_1.get, _2.get, _3, _4, _5, _6, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column contract_detail_id SqlType(BIGINT) */
    val contractDetailId: Rep[Long] = column[Long]("contract_detail_id")
    /** Database column unit SqlType(VARCHAR), Length(255,true), Default(None) */
    val unit: Rep[Option[String]] = column[Option[String]]("unit", O.Length(255,varying=true), O.Default(None))
    /** Database column unit_value SqlType(DECIMAL), Default(None) */
    val unitValue: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("unit_value", O.Default(None))
    /** Database column price SqlType(DECIMAL), Default(None) */
    val price: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("price", O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")

    /** Index over (contractDetailId) (database name contract_detail_id_index) */
    val index1 = index("contract_detail_id_index", contractDetailId)
  }
  /** Collection-like TableQuery object for table ContractDetailSegments */
  lazy val ContractDetailSegments = new TableQuery(tag => new ContractDetailSegments(tag))

  /** Entity class storing rows of table Contracts
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param clientId Database column client_id SqlType(BIGINT)
   *  @param registedDate Database column registed_date SqlType(DATETIME), Default(None)
   *  @param activatedDate Database column activated_date SqlType(DATETIME), Default(None)
   *  @param closeDate Database column close_date SqlType(DATETIME), Default(None)
   *  @param invoiceIssueType Database column invoice_issue_type SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param memo Database column memo SqlType(TEXT), Default(None)
   *  @param status Database column status SqlType(VARCHAR), Length(100,true)
   *  @param isDisabled Database column is_disabled SqlType(TINYINT), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class ContractsRow(id: Long, clientId: Long, registedDate: Option[Date] = None, activatedDate: Option[Date] = None, closeDate: Option[Date] = None, invoiceIssueType: Option[String] = None, memo: Option[String] = None, status: String, isDisabled: Option[Boolean] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching ContractsRow objects using plain SQL queries */
  implicit def GetResultContractsRow(implicit e0: GR[Long], e1: GR[Option[Date]], e2: GR[Option[String]], e3: GR[String], e4: GR[Option[Boolean]], e5: GR[Option[Int]], e6: GR[Date]): GR[ContractsRow] = GR{
    prs => import prs._
    ContractsRow.tupled((<<[Long], <<[Long], <<?[Date], <<?[Date], <<?[Date], <<?[String], <<?[String], <<[String], <<?[Boolean], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table contracts. Objects of this class serve as prototypes for rows in queries. */
  class Contracts(_tableTag: Tag) extends Table[ContractsRow](_tableTag, "contracts") {
    def * = (id, clientId, registedDate, activatedDate, closeDate, invoiceIssueType, memo, status, isDisabled, updater, createdDate, updatedDate) <> (ContractsRow.tupled, ContractsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(clientId), registedDate, activatedDate, closeDate, invoiceIssueType, memo, Rep.Some(status), isDisabled, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> ContractsRow.tupled((_1.get, _2.get, _3, _4, _5, _6, _7, _8.get, _9, _10, _11.get, _12.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column client_id SqlType(BIGINT) */
    val clientId: Rep[Long] = column[Long]("client_id")
    /** Database column registed_date SqlType(DATETIME), Default(None) */
    val registedDate: Rep[Option[Date]] = column[Option[Date]]("registed_date", O.Default(None))
    /** Database column activated_date SqlType(DATETIME), Default(None) */
    val activatedDate: Rep[Option[Date]] = column[Option[Date]]("activated_date", O.Default(None))
    /** Database column close_date SqlType(DATETIME), Default(None) */
    val closeDate: Rep[Option[Date]] = column[Option[Date]]("close_date", O.Default(None))
    /** Database column invoice_issue_type SqlType(VARCHAR), Length(100,true), Default(None) */
    val invoiceIssueType: Rep[Option[String]] = column[Option[String]]("invoice_issue_type", O.Length(100,varying=true), O.Default(None))
    /** Database column memo SqlType(TEXT), Default(None) */
    val memo: Rep[Option[String]] = column[Option[String]]("memo", O.Default(None))
    /** Database column status SqlType(VARCHAR), Length(100,true) */
    val status: Rep[String] = column[String]("status", O.Length(100,varying=true))
    /** Database column is_disabled SqlType(TINYINT), Default(None) */
    val isDisabled: Rep[Option[Boolean]] = column[Option[Boolean]]("is_disabled", O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")

    /** Index over (clientId) (database name client_id_index) */
    val index1 = index("client_id_index", clientId)
    /** Index over (isDisabled) (database name is_disabled_index) */
    val index2 = index("is_disabled_index", isDisabled)
    /** Index over (status) (database name status_index) */
    val index3 = index("status_index", status)
  }
  /** Collection-like TableQuery object for table Contracts */
  lazy val Contracts = new TableQuery(tag => new Contracts(tag))

  /** Entity class storing rows of table DefaultProducts
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param agencyId Database column agency_id SqlType(BIGINT)
   *  @param productName Database column product_name SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param productType Database column product_type SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param price Database column price SqlType(DECIMAL), Default(None)
   *  @param memo Database column memo SqlType(TEXT), Default(None)
   *  @param isDisabled Database column is_disabled SqlType(TINYINT), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class DefaultProductsRow(id: Long, agencyId: Long, productName: Option[String] = None, productType: Option[String] = None, price: Option[scala.math.BigDecimal] = None, memo: Option[String] = None, isDisabled: Option[Boolean] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching DefaultProductsRow objects using plain SQL queries */
  implicit def GetResultDefaultProductsRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[scala.math.BigDecimal]], e3: GR[Option[Boolean]], e4: GR[Option[Int]], e5: GR[Date]): GR[DefaultProductsRow] = GR{
    prs => import prs._
    DefaultProductsRow.tupled((<<[Long], <<[Long], <<?[String], <<?[String], <<?[scala.math.BigDecimal], <<?[String], <<?[Boolean], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table default_products. Objects of this class serve as prototypes for rows in queries. */
  class DefaultProducts(_tableTag: Tag) extends Table[DefaultProductsRow](_tableTag, "default_products") {
    def * = (id, agencyId, productName, productType, price, memo, isDisabled, updater, createdDate, updatedDate) <> (DefaultProductsRow.tupled, DefaultProductsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(agencyId), productName, productType, price, memo, isDisabled, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> DefaultProductsRow.tupled((_1.get, _2.get, _3, _4, _5, _6, _7, _8, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column agency_id SqlType(BIGINT) */
    val agencyId: Rep[Long] = column[Long]("agency_id")
    /** Database column product_name SqlType(VARCHAR), Length(255,true), Default(None) */
    val productName: Rep[Option[String]] = column[Option[String]]("product_name", O.Length(255,varying=true), O.Default(None))
    /** Database column product_type SqlType(VARCHAR), Length(255,true), Default(None) */
    val productType: Rep[Option[String]] = column[Option[String]]("product_type", O.Length(255,varying=true), O.Default(None))
    /** Database column price SqlType(DECIMAL), Default(None) */
    val price: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("price", O.Default(None))
    /** Database column memo SqlType(TEXT), Default(None) */
    val memo: Rep[Option[String]] = column[Option[String]]("memo", O.Default(None))
    /** Database column is_disabled SqlType(TINYINT), Default(None) */
    val isDisabled: Rep[Option[Boolean]] = column[Option[Boolean]]("is_disabled", O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")

    /** Index over (agencyId) (database name agency_id_index) */
    val index1 = index("agency_id_index", agencyId)
    /** Index over (isDisabled) (database name is_disabled_index) */
    val index2 = index("is_disabled_index", isDisabled)
  }
  /** Collection-like TableQuery object for table DefaultProducts */
  lazy val DefaultProducts = new TableQuery(tag => new DefaultProducts(tag))

  /** Entity class storing rows of table DefaultProductSegments
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param defaultProductId Database column default_product_id SqlType(BIGINT)
   *  @param unit Database column unit SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param unitValue Database column unit_value SqlType(DECIMAL), Default(None)
   *  @param price Database column price SqlType(DECIMAL), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class DefaultProductSegmentsRow(id: Long, defaultProductId: Long, unit: Option[String] = None, unitValue: Option[scala.math.BigDecimal] = None, price: Option[scala.math.BigDecimal] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching DefaultProductSegmentsRow objects using plain SQL queries */
  implicit def GetResultDefaultProductSegmentsRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[scala.math.BigDecimal]], e3: GR[Option[Int]], e4: GR[Date]): GR[DefaultProductSegmentsRow] = GR{
    prs => import prs._
    DefaultProductSegmentsRow.tupled((<<[Long], <<[Long], <<?[String], <<?[scala.math.BigDecimal], <<?[scala.math.BigDecimal], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table default_product_segments. Objects of this class serve as prototypes for rows in queries. */
  class DefaultProductSegments(_tableTag: Tag) extends Table[DefaultProductSegmentsRow](_tableTag, "default_product_segments") {
    def * = (id, defaultProductId, unit, unitValue, price, updater, createdDate, updatedDate) <> (DefaultProductSegmentsRow.tupled, DefaultProductSegmentsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(defaultProductId), unit, unitValue, price, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> DefaultProductSegmentsRow.tupled((_1.get, _2.get, _3, _4, _5, _6, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column default_product_id SqlType(BIGINT) */
    val defaultProductId: Rep[Long] = column[Long]("default_product_id")
    /** Database column unit SqlType(VARCHAR), Length(255,true), Default(None) */
    val unit: Rep[Option[String]] = column[Option[String]]("unit", O.Length(255,varying=true), O.Default(None))
    /** Database column unit_value SqlType(DECIMAL), Default(None) */
    val unitValue: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("unit_value", O.Default(None))
    /** Database column price SqlType(DECIMAL), Default(None) */
    val price: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("price", O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")

    /** Index over (defaultProductId) (database name default_product_id_index) */
    val index1 = index("default_product_id_index", defaultProductId)
  }
  /** Collection-like TableQuery object for table DefaultProductSegments */
  lazy val DefaultProductSegments = new TableQuery(tag => new DefaultProductSegments(tag))

  /** Entity class storing rows of table InvoiceAddresses
   *  @param invoiceId Database column invoice_id SqlType(BIGINT), PrimaryKey, Default(0)
   *  @param company Database column company SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param postCode Database column post_code SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param prefecture Database column prefecture SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param city Database column city SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param address1 Database column address1 SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param address2 Database column address2 SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param address3 Database column address3 SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param depertment Database column depertment SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param staff Database column staff SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param staffEmail Database column staff_email SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param phone Database column phone SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param fax Database column fax SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class InvoiceAddressesRow(invoiceId: Long = 0L, company: Option[String] = None, postCode: Option[String] = None, prefecture: Option[String] = None, city: Option[String] = None, address1: Option[String] = None, address2: Option[String] = None, address3: Option[String] = None, depertment: Option[String] = None, staff: Option[String] = None, staffEmail: Option[String] = None, phone: Option[String] = None, fax: Option[String] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching InvoiceAddressesRow objects using plain SQL queries */
  implicit def GetResultInvoiceAddressesRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[Int]], e3: GR[Date]): GR[InvoiceAddressesRow] = GR{
    prs => import prs._
    InvoiceAddressesRow.tupled((<<[Long], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table invoice_addresses. Objects of this class serve as prototypes for rows in queries. */
  class InvoiceAddresses(_tableTag: Tag) extends Table[InvoiceAddressesRow](_tableTag, "invoice_addresses") {
    def * = (invoiceId, company, postCode, prefecture, city, address1, address2, address3, depertment, staff, staffEmail, phone, fax, updater, createdDate, updatedDate) <> (InvoiceAddressesRow.tupled, InvoiceAddressesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(invoiceId), company, postCode, prefecture, city, address1, address2, address3, depertment, staff, staffEmail, phone, fax, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> InvoiceAddressesRow.tupled((_1.get, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15.get, _16.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column invoice_id SqlType(BIGINT), PrimaryKey, Default(0) */
    val invoiceId: Rep[Long] = column[Long]("invoice_id", O.PrimaryKey, O.Default(0L))
    /** Database column company SqlType(VARCHAR), Length(255,true), Default(None) */
    val company: Rep[Option[String]] = column[Option[String]]("company", O.Length(255,varying=true), O.Default(None))
    /** Database column post_code SqlType(VARCHAR), Length(255,true), Default(None) */
    val postCode: Rep[Option[String]] = column[Option[String]]("post_code", O.Length(255,varying=true), O.Default(None))
    /** Database column prefecture SqlType(VARCHAR), Length(255,true), Default(None) */
    val prefecture: Rep[Option[String]] = column[Option[String]]("prefecture", O.Length(255,varying=true), O.Default(None))
    /** Database column city SqlType(VARCHAR), Length(255,true), Default(None) */
    val city: Rep[Option[String]] = column[Option[String]]("city", O.Length(255,varying=true), O.Default(None))
    /** Database column address1 SqlType(VARCHAR), Length(255,true), Default(None) */
    val address1: Rep[Option[String]] = column[Option[String]]("address1", O.Length(255,varying=true), O.Default(None))
    /** Database column address2 SqlType(VARCHAR), Length(255,true), Default(None) */
    val address2: Rep[Option[String]] = column[Option[String]]("address2", O.Length(255,varying=true), O.Default(None))
    /** Database column address3 SqlType(VARCHAR), Length(255,true), Default(None) */
    val address3: Rep[Option[String]] = column[Option[String]]("address3", O.Length(255,varying=true), O.Default(None))
    /** Database column depertment SqlType(VARCHAR), Length(255,true), Default(None) */
    val depertment: Rep[Option[String]] = column[Option[String]]("depertment", O.Length(255,varying=true), O.Default(None))
    /** Database column staff SqlType(VARCHAR), Length(255,true), Default(None) */
    val staff: Rep[Option[String]] = column[Option[String]]("staff", O.Length(255,varying=true), O.Default(None))
    /** Database column staff_email SqlType(VARCHAR), Length(255,true), Default(None) */
    val staffEmail: Rep[Option[String]] = column[Option[String]]("staff_email", O.Length(255,varying=true), O.Default(None))
    /** Database column phone SqlType(VARCHAR), Length(255,true), Default(None) */
    val phone: Rep[Option[String]] = column[Option[String]]("phone", O.Length(255,varying=true), O.Default(None))
    /** Database column fax SqlType(VARCHAR), Length(255,true), Default(None) */
    val fax: Rep[Option[String]] = column[Option[String]]("fax", O.Length(255,varying=true), O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")
  }
  /** Collection-like TableQuery object for table InvoiceAddresses */
  lazy val InvoiceAddresses = new TableQuery(tag => new InvoiceAddresses(tag))

  /** Entity class storing rows of table InvoiceDetails
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param invoiceId Database column invoice_id SqlType(BIGINT)
   *  @param unitPrice Database column unit_price SqlType(DECIMAL), Default(None)
   *  @param quantity Database column quantity SqlType(INT), Default(None)
   *  @param productName Database column product_name SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param productType Database column product_type SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param accountName Database column account_name SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param details Database column details SqlType(TEXT), Default(None)
   *  @param billTermFrom Database column bill_term_from SqlType(DATE), Default(None)
   *  @param billTermTo Database column bill_term_to SqlType(DATE), Default(None)
   *  @param memo Database column memo SqlType(TEXT), Default(None)
   *  @param isDisabled Database column is_disabled SqlType(TINYINT), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class InvoiceDetailsRow(id: Long, invoiceId: Long, unitPrice: Option[scala.math.BigDecimal] = None, quantity: Option[Int] = None, productName: Option[String] = None, productType: Option[String] = None, accountName: Option[String] = None, details: Option[String] = None, billTermFrom: Option[LocalDate] = None, billTermTo: Option[LocalDate] = None, memo: Option[String] = None, isDisabled: Option[Boolean] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching InvoiceDetailsRow objects using plain SQL queries */
  implicit def GetResultInvoiceDetailsRow(implicit e0: GR[Long], e1: GR[Option[scala.math.BigDecimal]], e2: GR[Option[Int]], e3: GR[Option[String]], e4: GR[Option[LocalDate]], e5: GR[Option[Boolean]], e6: GR[Date]): GR[InvoiceDetailsRow] = GR{
    prs => import prs._
    InvoiceDetailsRow.tupled((<<[Long], <<[Long], <<?[scala.math.BigDecimal], <<?[Int], <<?[String], <<?[String], <<?[String], <<?[String], <<?[LocalDate], <<?[LocalDate], <<?[String], <<?[Boolean], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table invoice_details. Objects of this class serve as prototypes for rows in queries. */
  class InvoiceDetails(_tableTag: Tag) extends Table[InvoiceDetailsRow](_tableTag, "invoice_details") {
    def * = (id, invoiceId, unitPrice, quantity, productName, productType, accountName, details, billTermFrom, billTermTo, memo, isDisabled, updater, createdDate, updatedDate) <> (InvoiceDetailsRow.tupled, InvoiceDetailsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(invoiceId), unitPrice, quantity, productName, productType, accountName, details, billTermFrom, billTermTo, memo, isDisabled, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> InvoiceDetailsRow.tupled((_1.get, _2.get, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14.get, _15.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column invoice_id SqlType(BIGINT) */
    val invoiceId: Rep[Long] = column[Long]("invoice_id")
    /** Database column unit_price SqlType(DECIMAL), Default(None) */
    val unitPrice: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("unit_price", O.Default(None))
    /** Database column quantity SqlType(INT), Default(None) */
    val quantity: Rep[Option[Int]] = column[Option[Int]]("quantity", O.Default(None))
    /** Database column product_name SqlType(VARCHAR), Length(255,true), Default(None) */
    val productName: Rep[Option[String]] = column[Option[String]]("product_name", O.Length(255,varying=true), O.Default(None))
    /** Database column product_type SqlType(VARCHAR), Length(255,true), Default(None) */
    val productType: Rep[Option[String]] = column[Option[String]]("product_type", O.Length(255,varying=true), O.Default(None))
    /** Database column account_name SqlType(VARCHAR), Length(255,true), Default(None) */
    val accountName: Rep[Option[String]] = column[Option[String]]("account_name", O.Length(255,varying=true), O.Default(None))
    /** Database column details SqlType(TEXT), Default(None) */
    val details: Rep[Option[String]] = column[Option[String]]("details", O.Default(None))
    /** Database column bill_term_from SqlType(DATE), Default(None) */
    val billTermFrom: Rep[Option[LocalDate]] = column[Option[LocalDate]]("bill_term_from", O.Default(None))
    /** Database column bill_term_to SqlType(DATE), Default(None) */
    val billTermTo: Rep[Option[LocalDate]] = column[Option[LocalDate]]("bill_term_to", O.Default(None))
    /** Database column memo SqlType(TEXT), Default(None) */
    val memo: Rep[Option[String]] = column[Option[String]]("memo", O.Default(None))
    /** Database column is_disabled SqlType(TINYINT), Default(None) */
    val isDisabled: Rep[Option[Boolean]] = column[Option[Boolean]]("is_disabled", O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")

    /** Index over (invoiceId) (database name invoice_id_index) */
    val index1 = index("invoice_id_index", invoiceId)
    /** Index over (isDisabled) (database name is_disabled_index) */
    val index2 = index("is_disabled_index", isDisabled)
  }
  /** Collection-like TableQuery object for table InvoiceDetails */
  lazy val InvoiceDetails = new TableQuery(tag => new InvoiceDetails(tag))

  /** Entity class storing rows of table InvoiceProcessdates
   *  @param invoiceId Database column invoice_id SqlType(BIGINT), PrimaryKey
   *  @param issueDate Database column issue_date SqlType(DATE), Default(None)
   *  @param applyDate Database column apply_date SqlType(DATE), Default(None)
   *  @param payedDate Database column payed_date SqlType(DATE), Default(None)
   *  @param sentDate Database column sent_date SqlType(DATE), Default(None)
   *  @param cancelDate Database column cancel_date SqlType(DATE), Default(None)
   *  @param paymentDueDate Database column payment_due_date SqlType(DATE), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class InvoiceProcessdatesRow(invoiceId: Long, issueDate: Option[LocalDate] = None, applyDate: Option[LocalDate] = None, payedDate: Option[LocalDate] = None, sentDate: Option[LocalDate] = None, cancelDate: Option[LocalDate] = None, paymentDueDate: Option[LocalDate] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching InvoiceProcessdatesRow objects using plain SQL queries */
  implicit def GetResultInvoiceProcessdatesRow(implicit e0: GR[Long], e1: GR[Option[LocalDate]], e2: GR[Option[Int]], e3: GR[Date]): GR[InvoiceProcessdatesRow] = GR{
    prs => import prs._
    InvoiceProcessdatesRow.tupled((<<[Long], <<?[LocalDate], <<?[LocalDate], <<?[LocalDate], <<?[LocalDate], <<?[LocalDate], <<?[LocalDate], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table invoice_processdates. Objects of this class serve as prototypes for rows in queries. */
  class InvoiceProcessdates(_tableTag: Tag) extends Table[InvoiceProcessdatesRow](_tableTag, "invoice_processdates") {
    def * = (invoiceId, issueDate, applyDate, payedDate, sentDate, cancelDate, paymentDueDate, updater, createdDate, updatedDate) <> (InvoiceProcessdatesRow.tupled, InvoiceProcessdatesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(invoiceId), issueDate, applyDate, payedDate, sentDate, cancelDate, paymentDueDate, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> InvoiceProcessdatesRow.tupled((_1.get, _2, _3, _4, _5, _6, _7, _8, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column invoice_id SqlType(BIGINT), PrimaryKey */
    val invoiceId: Rep[Long] = column[Long]("invoice_id", O.PrimaryKey)
    /** Database column issue_date SqlType(DATE), Default(None) */
    val issueDate: Rep[Option[LocalDate]] = column[Option[LocalDate]]("issue_date", O.Default(None))
    /** Database column apply_date SqlType(DATE), Default(None) */
    val applyDate: Rep[Option[LocalDate]] = column[Option[LocalDate]]("apply_date", O.Default(None))
    /** Database column payed_date SqlType(DATE), Default(None) */
    val payedDate: Rep[Option[LocalDate]] = column[Option[LocalDate]]("payed_date", O.Default(None))
    /** Database column sent_date SqlType(DATE), Default(None) */
    val sentDate: Rep[Option[LocalDate]] = column[Option[LocalDate]]("sent_date", O.Default(None))
    /** Database column cancel_date SqlType(DATE), Default(None) */
    val cancelDate: Rep[Option[LocalDate]] = column[Option[LocalDate]]("cancel_date", O.Default(None))
    /** Database column payment_due_date SqlType(DATE), Default(None) */
    val paymentDueDate: Rep[Option[LocalDate]] = column[Option[LocalDate]]("payment_due_date", O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")
  }
  /** Collection-like TableQuery object for table InvoiceProcessdates */
  lazy val InvoiceProcessdates = new TableQuery(tag => new InvoiceProcessdates(tag))

  /** Entity class storing rows of table Invoices
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param invoicecViewId Database column invoicec_view_id SqlType(VARCHAR), Length(255,true)
   *  @param applicationId Database column application_id SqlType(BIGINT)
   *  @param agencyId Database column agency_id SqlType(BIGINT)
   *  @param billingDestinationId Database column billing_destination_id SqlType(BIGINT)
   *  @param invoicecTo Database column invoicec_to SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param invoicecSubject Database column invoicec_subject SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param invoicecTotal Database column invoicec_total SqlType(DECIMAL), Default(None)
   *  @param invoicecTaxedTotal Database column invoicec_taxed_total SqlType(DECIMAL), Default(None)
   *  @param invoicecTaxrate Database column invoicec_taxrate SqlType(DECIMAL), Default(None)
   *  @param pcaCode Database column pca_code SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param pcaCommonName Database column pca_common_name SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param memo Database column memo SqlType(TEXT), Default(None)
   *  @param status Database column status SqlType(VARCHAR), Length(100,true)
   *  @param cancelStatus Database column cancel_status SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param isDisabled Database column is_disabled SqlType(TINYINT), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class InvoicesRow(id: Long, invoicecViewId: String, applicationId: Long, agencyId: Long, billingDestinationId: Long, invoicecTo: Option[String] = None, invoicecSubject: Option[String] = None, invoicecTotal: Option[scala.math.BigDecimal] = None, invoicecTaxedTotal: Option[scala.math.BigDecimal] = None, invoicecTaxrate: Option[scala.math.BigDecimal] = None, pcaCode: Option[String] = None, pcaCommonName: Option[String] = None, memo: Option[String] = None, status: String, cancelStatus: Option[String] = None, isDisabled: Option[Boolean] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching InvoicesRow objects using plain SQL queries */
  implicit def GetResultInvoicesRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[scala.math.BigDecimal]], e4: GR[Option[Boolean]], e5: GR[Option[Int]], e6: GR[Date]): GR[InvoicesRow] = GR{
    prs => import prs._
    InvoicesRow.tupled((<<[Long], <<[String], <<[Long], <<[Long], <<[Long], <<?[String], <<?[String], <<?[scala.math.BigDecimal], <<?[scala.math.BigDecimal], <<?[scala.math.BigDecimal], <<?[String], <<?[String], <<?[String], <<[String], <<?[String], <<?[Boolean], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table invoices. Objects of this class serve as prototypes for rows in queries. */
  class Invoices(_tableTag: Tag) extends Table[InvoicesRow](_tableTag, "invoices") {
    def * = (id, invoicecViewId, applicationId, agencyId, billingDestinationId, invoicecTo, invoicecSubject, invoicecTotal, invoicecTaxedTotal, invoicecTaxrate, pcaCode, pcaCommonName, memo, status, cancelStatus, isDisabled, updater, createdDate, updatedDate) <> (InvoicesRow.tupled, InvoicesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(invoicecViewId), Rep.Some(applicationId), Rep.Some(agencyId), Rep.Some(billingDestinationId), invoicecTo, invoicecSubject, invoicecTotal, invoicecTaxedTotal, invoicecTaxrate, pcaCode, pcaCommonName, memo, Rep.Some(status), cancelStatus, isDisabled, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> InvoicesRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7, _8, _9, _10, _11, _12, _13, _14.get, _15, _16, _17, _18.get, _19.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column invoicec_view_id SqlType(VARCHAR), Length(255,true) */
    val invoicecViewId: Rep[String] = column[String]("invoicec_view_id", O.Length(255,varying=true))
    /** Database column application_id SqlType(BIGINT) */
    val applicationId: Rep[Long] = column[Long]("application_id")
    /** Database column agency_id SqlType(BIGINT) */
    val agencyId: Rep[Long] = column[Long]("agency_id")
    /** Database column billing_destination_id SqlType(BIGINT) */
    val billingDestinationId: Rep[Long] = column[Long]("billing_destination_id")
    /** Database column invoicec_to SqlType(VARCHAR), Length(255,true), Default(None) */
    val invoicecTo: Rep[Option[String]] = column[Option[String]]("invoicec_to", O.Length(255,varying=true), O.Default(None))
    /** Database column invoicec_subject SqlType(VARCHAR), Length(255,true), Default(None) */
    val invoicecSubject: Rep[Option[String]] = column[Option[String]]("invoicec_subject", O.Length(255,varying=true), O.Default(None))
    /** Database column invoicec_total SqlType(DECIMAL), Default(None) */
    val invoicecTotal: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("invoicec_total", O.Default(None))
    /** Database column invoicec_taxed_total SqlType(DECIMAL), Default(None) */
    val invoicecTaxedTotal: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("invoicec_taxed_total", O.Default(None))
    /** Database column invoicec_taxrate SqlType(DECIMAL), Default(None) */
    val invoicecTaxrate: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("invoicec_taxrate", O.Default(None))
    /** Database column pca_code SqlType(VARCHAR), Length(255,true), Default(None) */
    val pcaCode: Rep[Option[String]] = column[Option[String]]("pca_code", O.Length(255,varying=true), O.Default(None))
    /** Database column pca_common_name SqlType(VARCHAR), Length(255,true), Default(None) */
    val pcaCommonName: Rep[Option[String]] = column[Option[String]]("pca_common_name", O.Length(255,varying=true), O.Default(None))
    /** Database column memo SqlType(TEXT), Default(None) */
    val memo: Rep[Option[String]] = column[Option[String]]("memo", O.Default(None))
    /** Database column status SqlType(VARCHAR), Length(100,true) */
    val status: Rep[String] = column[String]("status", O.Length(100,varying=true))
    /** Database column cancel_status SqlType(VARCHAR), Length(100,true), Default(None) */
    val cancelStatus: Rep[Option[String]] = column[Option[String]]("cancel_status", O.Length(100,varying=true), O.Default(None))
    /** Database column is_disabled SqlType(TINYINT), Default(None) */
    val isDisabled: Rep[Option[Boolean]] = column[Option[Boolean]]("is_disabled", O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")

    /** Index over (agencyId) (database name agency_id_index) */
    val index1 = index("agency_id_index", agencyId)
    /** Index over (applicationId) (database name application_id_index) */
    val index2 = index("application_id_index", applicationId)
    /** Index over (billingDestinationId) (database name billing_destination_id_index) */
    val index3 = index("billing_destination_id_index", billingDestinationId)
    /** Index over (cancelStatus) (database name cancel_status_index) */
    val index4 = index("cancel_status_index", cancelStatus)
    /** Index over (invoicecViewId) (database name invoicec_view_id_index) */
    val index5 = index("invoicec_view_id_index", invoicecViewId)
    /** Index over (isDisabled) (database name is_disabled_index) */
    val index6 = index("is_disabled_index", isDisabled)
    /** Index over (status) (database name status_index) */
    val index7 = index("status_index", status)
  }
  /** Collection-like TableQuery object for table Invoices */
  lazy val Invoices = new TableQuery(tag => new Invoices(tag))

  /** Entity class storing rows of table LoginHistories
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param accountId Database column account_id SqlType(INT), Default(None)
   *  @param inputLoginId Database column input_login_id SqlType(TEXT), Default(None)
   *  @param userAgent Database column user_agent SqlType(TEXT), Default(None)
   *  @param ipaddress Database column ipaddress SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param isLogined Database column is_logined SqlType(TINYINT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME) */
  case class LoginHistoriesRow(id: Long, accountId: Option[Int] = None, inputLoginId: Option[String] = None, userAgent: Option[String] = None, ipaddress: Option[String] = None, isLogined: Option[Boolean] = None, createdDate: Date)
  /** GetResult implicit for fetching LoginHistoriesRow objects using plain SQL queries */
  implicit def GetResultLoginHistoriesRow(implicit e0: GR[Long], e1: GR[Option[Int]], e2: GR[Option[String]], e3: GR[Option[Boolean]], e4: GR[Date]): GR[LoginHistoriesRow] = GR{
    prs => import prs._
    LoginHistoriesRow.tupled((<<[Long], <<?[Int], <<?[String], <<?[String], <<?[String], <<?[Boolean], <<[Date]))
  }
  /** Table description of table login_histories. Objects of this class serve as prototypes for rows in queries. */
  class LoginHistories(_tableTag: Tag) extends Table[LoginHistoriesRow](_tableTag, "login_histories") {
    def * = (id, accountId, inputLoginId, userAgent, ipaddress, isLogined, createdDate) <> (LoginHistoriesRow.tupled, LoginHistoriesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), accountId, inputLoginId, userAgent, ipaddress, isLogined, Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> LoginHistoriesRow.tupled((_1.get, _2, _3, _4, _5, _6, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column account_id SqlType(INT), Default(None) */
    val accountId: Rep[Option[Int]] = column[Option[Int]]("account_id", O.Default(None))
    /** Database column input_login_id SqlType(TEXT), Default(None) */
    val inputLoginId: Rep[Option[String]] = column[Option[String]]("input_login_id", O.Default(None))
    /** Database column user_agent SqlType(TEXT), Default(None) */
    val userAgent: Rep[Option[String]] = column[Option[String]]("user_agent", O.Default(None))
    /** Database column ipaddress SqlType(VARCHAR), Length(255,true), Default(None) */
    val ipaddress: Rep[Option[String]] = column[Option[String]]("ipaddress", O.Length(255,varying=true), O.Default(None))
    /** Database column is_logined SqlType(TINYINT), Default(None) */
    val isLogined: Rep[Option[Boolean]] = column[Option[Boolean]]("is_logined", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")

    /** Index over (accountId) (database name account_id_index) */
    val index1 = index("account_id_index", accountId)
  }
  /** Collection-like TableQuery object for table LoginHistories */
  lazy val LoginHistories = new TableQuery(tag => new LoginHistories(tag))

  /** Entity class storing rows of table OauthClients
   *  @param oauthClientId Database column oauth_client_id SqlType(BINARY), PrimaryKey
   *  @param oauthUserGuid Database column oauth_user_guid SqlType(BINARY)
   *  @param clientSecret Database column client_secret SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param redirectUri Database column redirect_uri SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param grantType Database column grant_type SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param expiresIn Database column expires_in SqlType(INT)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class OauthClientsRow(oauthClientId: UUID, oauthUserGuid: UUID, clientSecret: Option[String] = None, redirectUri: Option[String] = None, grantType: Option[String] = None, expiresIn: Int, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching OauthClientsRow objects using plain SQL queries */
  implicit def GetResultOauthClientsRow(implicit e0: GR[UUID], e1: GR[Option[String]], e2: GR[Int], e3: GR[Option[Int]], e4: GR[Date]): GR[OauthClientsRow] = GR{
    prs => import prs._
    OauthClientsRow.tupled((<<[UUID], <<[UUID], <<?[String], <<?[String], <<?[String], <<[Int], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table oauth_clients. Objects of this class serve as prototypes for rows in queries. */
  class OauthClients(_tableTag: Tag) extends Table[OauthClientsRow](_tableTag, "oauth_clients") {
    def * = (oauthClientId, oauthUserGuid, clientSecret, redirectUri, grantType, expiresIn, updater, createdDate, updatedDate) <> (OauthClientsRow.tupled, OauthClientsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(oauthClientId), Rep.Some(oauthUserGuid), clientSecret, redirectUri, grantType, Rep.Some(expiresIn), updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> OauthClientsRow.tupled((_1.get, _2.get, _3, _4, _5, _6.get, _7, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column oauth_client_id SqlType(BINARY), PrimaryKey */
    val oauthClientId: Rep[UUID] = column[UUID]("oauth_client_id", O.PrimaryKey)
    /** Database column oauth_user_guid SqlType(BINARY) */
    val oauthUserGuid: Rep[UUID] = column[UUID]("oauth_user_guid")
    /** Database column client_secret SqlType(VARCHAR), Length(255,true), Default(None) */
    val clientSecret: Rep[Option[String]] = column[Option[String]]("client_secret", O.Length(255,varying=true), O.Default(None))
    /** Database column redirect_uri SqlType(VARCHAR), Length(255,true), Default(None) */
    val redirectUri: Rep[Option[String]] = column[Option[String]]("redirect_uri", O.Length(255,varying=true), O.Default(None))
    /** Database column grant_type SqlType(VARCHAR), Length(255,true), Default(None) */
    val grantType: Rep[Option[String]] = column[Option[String]]("grant_type", O.Length(255,varying=true), O.Default(None))
    /** Database column expires_in SqlType(INT) */
    val expiresIn: Rep[Int] = column[Int]("expires_in")
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")

    /** Index over (oauthUserGuid) (database name oauth_user_guid_index) */
    val index1 = index("oauth_user_guid_index", oauthUserGuid)
  }
  /** Collection-like TableQuery object for table OauthClients */
  lazy val OauthClients = new TableQuery(tag => new OauthClients(tag))

  /** Entity class storing rows of table OauthUsers
   *  @param guid Database column guid SqlType(BINARY), PrimaryKey
   *  @param applicationId Database column application_id SqlType(BIGINT)
   *  @param agencyId Database column agency_id SqlType(BIGINT), Default(None)
   *  @param clientId Database column client_id SqlType(BIGINT), Default(None)
   *  @param name Database column name SqlType(VARCHAR), Length(255,true)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class OauthUsersRow(guid: UUID, applicationId: Long, agencyId: Option[Long] = None, clientId: Option[Long] = None, name: String, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching OauthUsersRow objects using plain SQL queries */
  implicit def GetResultOauthUsersRow(implicit e0: GR[UUID], e1: GR[Long], e2: GR[Option[Long]], e3: GR[String], e4: GR[Option[Int]], e5: GR[Date]): GR[OauthUsersRow] = GR{
    prs => import prs._
    OauthUsersRow.tupled((<<[UUID], <<[Long], <<?[Long], <<?[Long], <<[String], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table oauth_users. Objects of this class serve as prototypes for rows in queries. */
  class OauthUsers(_tableTag: Tag) extends Table[OauthUsersRow](_tableTag, "oauth_users") {
    def * = (guid, applicationId, agencyId, clientId, name, updater, createdDate, updatedDate) <> (OauthUsersRow.tupled, OauthUsersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(guid), Rep.Some(applicationId), agencyId, clientId, Rep.Some(name), updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> OauthUsersRow.tupled((_1.get, _2.get, _3, _4, _5.get, _6, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column guid SqlType(BINARY), PrimaryKey */
    val guid: Rep[UUID] = column[UUID]("guid", O.PrimaryKey)
    /** Database column application_id SqlType(BIGINT) */
    val applicationId: Rep[Long] = column[Long]("application_id")
    /** Database column agency_id SqlType(BIGINT), Default(None) */
    val agencyId: Rep[Option[Long]] = column[Option[Long]]("agency_id", O.Default(None))
    /** Database column client_id SqlType(BIGINT), Default(None) */
    val clientId: Rep[Option[Long]] = column[Option[Long]]("client_id", O.Default(None))
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")

    /** Index over (agencyId) (database name agency_id_index) */
    val index1 = index("agency_id_index", agencyId)
    /** Index over (applicationId) (database name application_id_index) */
    val index2 = index("application_id_index", applicationId)
    /** Index over (clientId) (database name client_id_index) */
    val index3 = index("client_id_index", clientId)
  }
  /** Collection-like TableQuery object for table OauthUsers */
  lazy val OauthUsers = new TableQuery(tag => new OauthUsers(tag))

  /** Entity class storing rows of table PaymentHistories
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param clientId Database column client_id SqlType(BIGINT)
   *  @param contractDetailId Database column contract_detail_id SqlType(BIGINT), Default(None)
   *  @param appProductId Database column app_product_id SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param productName Database column product_name SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param price Database column price SqlType(DECIMAL), Default(None)
   *  @param taxRate Database column tax_rate SqlType(DECIMAL), Default(None)
   *  @param taxedPrice Database column taxed_price SqlType(DECIMAL), Default(None)
   *  @param paymentDate Database column payment_date SqlType(DATETIME), Default(None)
   *  @param paymentIssueType Database column payment_issue_type SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param paymentStatus Database column payment_status SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param accountingFrom Database column accounting_from SqlType(DATETIME), Default(None)
   *  @param accountingTo Database column accounting_to SqlType(DATETIME), Default(None)
   *  @param details Database column details SqlType(TEXT), Default(None)
   *  @param updater Database column updater SqlType(INT), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME)
   *  @param updatedDate Database column updated_date SqlType(TIMESTAMP) */
  case class PaymentHistoriesRow(id: Long, clientId: Long, contractDetailId: Option[Long] = None, appProductId: Option[String] = None, productName: Option[String] = None, price: Option[scala.math.BigDecimal] = None, taxRate: Option[scala.math.BigDecimal] = None, taxedPrice: Option[scala.math.BigDecimal] = None, paymentDate: Option[Date] = None, paymentIssueType: Option[String] = None, paymentStatus: Option[String] = None, accountingFrom: Option[Date] = None, accountingTo: Option[Date] = None, details: Option[String] = None, updater: Option[Int] = None, createdDate: Date, updatedDate: Date)
  /** GetResult implicit for fetching PaymentHistoriesRow objects using plain SQL queries */
  implicit def GetResultPaymentHistoriesRow(implicit e0: GR[Long], e1: GR[Option[Long]], e2: GR[Option[String]], e3: GR[Option[scala.math.BigDecimal]], e4: GR[Option[Date]], e5: GR[Option[Int]], e6: GR[Date]): GR[PaymentHistoriesRow] = GR{
    prs => import prs._
    PaymentHistoriesRow.tupled((<<[Long], <<[Long], <<?[Long], <<?[String], <<?[String], <<?[scala.math.BigDecimal], <<?[scala.math.BigDecimal], <<?[scala.math.BigDecimal], <<?[Date], <<?[String], <<?[String], <<?[Date], <<?[Date], <<?[String], <<?[Int], <<[Date], <<[Date]))
  }
  /** Table description of table payment_histories. Objects of this class serve as prototypes for rows in queries. */
  class PaymentHistories(_tableTag: Tag) extends Table[PaymentHistoriesRow](_tableTag, "payment_histories") {
    def * = (id, clientId, contractDetailId, appProductId, productName, price, taxRate, taxedPrice, paymentDate, paymentIssueType, paymentStatus, accountingFrom, accountingTo, details, updater, createdDate, updatedDate) <> (PaymentHistoriesRow.tupled, PaymentHistoriesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(clientId), contractDetailId, appProductId, productName, price, taxRate, taxedPrice, paymentDate, paymentIssueType, paymentStatus, accountingFrom, accountingTo, details, updater, Rep.Some(createdDate), Rep.Some(updatedDate)).shaped.<>({r=>import r._; _1.map(_=> PaymentHistoriesRow.tupled((_1.get, _2.get, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16.get, _17.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column client_id SqlType(BIGINT) */
    val clientId: Rep[Long] = column[Long]("client_id")
    /** Database column contract_detail_id SqlType(BIGINT), Default(None) */
    val contractDetailId: Rep[Option[Long]] = column[Option[Long]]("contract_detail_id", O.Default(None))
    /** Database column app_product_id SqlType(VARCHAR), Length(255,true), Default(None) */
    val appProductId: Rep[Option[String]] = column[Option[String]]("app_product_id", O.Length(255,varying=true), O.Default(None))
    /** Database column product_name SqlType(VARCHAR), Length(255,true), Default(None) */
    val productName: Rep[Option[String]] = column[Option[String]]("product_name", O.Length(255,varying=true), O.Default(None))
    /** Database column price SqlType(DECIMAL), Default(None) */
    val price: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("price", O.Default(None))
    /** Database column tax_rate SqlType(DECIMAL), Default(None) */
    val taxRate: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("tax_rate", O.Default(None))
    /** Database column taxed_price SqlType(DECIMAL), Default(None) */
    val taxedPrice: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("taxed_price", O.Default(None))
    /** Database column payment_date SqlType(DATETIME), Default(None) */
    val paymentDate: Rep[Option[Date]] = column[Option[Date]]("payment_date", O.Default(None))
    /** Database column payment_issue_type SqlType(VARCHAR), Length(100,true), Default(None) */
    val paymentIssueType: Rep[Option[String]] = column[Option[String]]("payment_issue_type", O.Length(100,varying=true), O.Default(None))
    /** Database column payment_status SqlType(VARCHAR), Length(100,true), Default(None) */
    val paymentStatus: Rep[Option[String]] = column[Option[String]]("payment_status", O.Length(100,varying=true), O.Default(None))
    /** Database column accounting_from SqlType(DATETIME), Default(None) */
    val accountingFrom: Rep[Option[Date]] = column[Option[Date]]("accounting_from", O.Default(None))
    /** Database column accounting_to SqlType(DATETIME), Default(None) */
    val accountingTo: Rep[Option[Date]] = column[Option[Date]]("accounting_to", O.Default(None))
    /** Database column details SqlType(TEXT), Default(None) */
    val details: Rep[Option[String]] = column[Option[String]]("details", O.Default(None))
    /** Database column updater SqlType(INT), Default(None) */
    val updater: Rep[Option[Int]] = column[Option[Int]]("updater", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")
    /** Database column updated_date SqlType(TIMESTAMP) */
    val updatedDate: Rep[Date] = column[Date]("updated_date")
  }
  /** Collection-like TableQuery object for table PaymentHistories */
  lazy val PaymentHistories = new TableQuery(tag => new PaymentHistories(tag))

  /** Entity class storing rows of table PlayEvolutions
   *  @param id Database column id SqlType(INT), PrimaryKey
   *  @param hash Database column hash SqlType(VARCHAR), Length(255,true)
   *  @param appliedAt Database column applied_at SqlType(TIMESTAMP)
   *  @param applyScript Database column apply_script SqlType(MEDIUMTEXT), Length(16777215,true), Default(None)
   *  @param revertScript Database column revert_script SqlType(MEDIUMTEXT), Length(16777215,true), Default(None)
   *  @param state Database column state SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param lastProblem Database column last_problem SqlType(MEDIUMTEXT), Length(16777215,true), Default(None) */
  case class PlayEvolutionsRow(id: Int, hash: String, appliedAt: java.sql.Timestamp, applyScript: Option[String] = None, revertScript: Option[String] = None, state: Option[String] = None, lastProblem: Option[String] = None)
  /** GetResult implicit for fetching PlayEvolutionsRow objects using plain SQL queries */
  implicit def GetResultPlayEvolutionsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Option[String]]): GR[PlayEvolutionsRow] = GR{
    prs => import prs._
    PlayEvolutionsRow.tupled((<<[Int], <<[String], <<[java.sql.Timestamp], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table play_evolutions. Objects of this class serve as prototypes for rows in queries. */
  class PlayEvolutions(_tableTag: Tag) extends Table[PlayEvolutionsRow](_tableTag, "play_evolutions") {
    def * = (id, hash, appliedAt, applyScript, revertScript, state, lastProblem) <> (PlayEvolutionsRow.tupled, PlayEvolutionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(hash), Rep.Some(appliedAt), applyScript, revertScript, state, lastProblem).shaped.<>({r=>import r._; _1.map(_=> PlayEvolutionsRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column hash SqlType(VARCHAR), Length(255,true) */
    val hash: Rep[String] = column[String]("hash", O.Length(255,varying=true))
    /** Database column applied_at SqlType(TIMESTAMP) */
    val appliedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("applied_at")
    /** Database column apply_script SqlType(MEDIUMTEXT), Length(16777215,true), Default(None) */
    val applyScript: Rep[Option[String]] = column[Option[String]]("apply_script", O.Length(16777215,varying=true), O.Default(None))
    /** Database column revert_script SqlType(MEDIUMTEXT), Length(16777215,true), Default(None) */
    val revertScript: Rep[Option[String]] = column[Option[String]]("revert_script", O.Length(16777215,varying=true), O.Default(None))
    /** Database column state SqlType(VARCHAR), Length(255,true), Default(None) */
    val state: Rep[Option[String]] = column[Option[String]]("state", O.Length(255,varying=true), O.Default(None))
    /** Database column last_problem SqlType(MEDIUMTEXT), Length(16777215,true), Default(None) */
    val lastProblem: Rep[Option[String]] = column[Option[String]]("last_problem", O.Length(16777215,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table PlayEvolutions */
  lazy val PlayEvolutions = new TableQuery(tag => new PlayEvolutions(tag))

  /** Entity class storing rows of table TaxRates
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param taxRate Database column tax_rate SqlType(DECIMAL), Default(None)
   *  @param startFrom Database column start_from SqlType(DATETIME), Default(None)
   *  @param createdDate Database column created_date SqlType(DATETIME) */
  case class TaxRatesRow(id: Int, taxRate: Option[scala.math.BigDecimal] = None, startFrom: Option[Date] = None, createdDate: Date)
  /** GetResult implicit for fetching TaxRatesRow objects using plain SQL queries */
  implicit def GetResultTaxRatesRow(implicit e0: GR[Int], e1: GR[Option[scala.math.BigDecimal]], e2: GR[Option[Date]], e3: GR[Date]): GR[TaxRatesRow] = GR{
    prs => import prs._
    TaxRatesRow.tupled((<<[Int], <<?[scala.math.BigDecimal], <<?[Date], <<[Date]))
  }
  /** Table description of table tax_rates. Objects of this class serve as prototypes for rows in queries. */
  class TaxRates(_tableTag: Tag) extends Table[TaxRatesRow](_tableTag, "tax_rates") {
    def * = (id, taxRate, startFrom, createdDate) <> (TaxRatesRow.tupled, TaxRatesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), taxRate, startFrom, Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> TaxRatesRow.tupled((_1.get, _2, _3, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column tax_rate SqlType(DECIMAL), Default(None) */
    val taxRate: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("tax_rate", O.Default(None))
    /** Database column start_from SqlType(DATETIME), Default(None) */
    val startFrom: Rep[Option[Date]] = column[Option[Date]]("start_from", O.Default(None))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")

    /** Index over (startFrom) (database name start_from_index) */
    val index1 = index("start_from_index", startFrom)
  }
  /** Collection-like TableQuery object for table TaxRates */
  lazy val TaxRates = new TableQuery(tag => new TaxRates(tag))

  /** Entity class storing rows of table Timezones
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param timezoneValue Database column timezone_value SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param cities Database column cities SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param gmt Database column gmt SqlType(VARCHAR), Length(100,true)
   *  @param createdDate Database column created_date SqlType(DATETIME) */
  case class TimezonesRow(id: Int, timezoneValue: Option[String] = None, cities: Option[String] = None, gmt: String, createdDate: Date)
  /** GetResult implicit for fetching TimezonesRow objects using plain SQL queries */
  implicit def GetResultTimezonesRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[String], e3: GR[Date]): GR[TimezonesRow] = GR{
    prs => import prs._
    TimezonesRow.tupled((<<[Int], <<?[String], <<?[String], <<[String], <<[Date]))
  }
  /** Table description of table timezones. Objects of this class serve as prototypes for rows in queries. */
  class Timezones(_tableTag: Tag) extends Table[TimezonesRow](_tableTag, "timezones") {
    def * = (id, timezoneValue, cities, gmt, createdDate) <> (TimezonesRow.tupled, TimezonesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), timezoneValue, cities, Rep.Some(gmt), Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> TimezonesRow.tupled((_1.get, _2, _3, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column timezone_value SqlType(VARCHAR), Length(255,true), Default(None) */
    val timezoneValue: Rep[Option[String]] = column[Option[String]]("timezone_value", O.Length(255,varying=true), O.Default(None))
    /** Database column cities SqlType(VARCHAR), Length(255,true), Default(None) */
    val cities: Rep[Option[String]] = column[Option[String]]("cities", O.Length(255,varying=true), O.Default(None))
    /** Database column gmt SqlType(VARCHAR), Length(100,true) */
    val gmt: Rep[String] = column[String]("gmt", O.Length(100,varying=true))
    /** Database column created_date SqlType(DATETIME) */
    val createdDate: Rep[Date] = column[Date]("created_date")

    /** Index over (timezoneValue) (database name timezone_value_index) */
    val index1 = index("timezone_value_index", timezoneValue)
  }
  /** Collection-like TableQuery object for table Timezones */
  lazy val Timezones = new TableQuery(tag => new Timezones(tag))
}

