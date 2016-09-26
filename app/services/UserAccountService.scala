package services

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.google.inject.ImplementedBy
import models.TablesExtend._
import forms._
import services.dao._
import org.apache.commons.codec.digest.DigestUtils

@ImplementedBy(classOf[UserAccountService])
trait UserAccountServiceLike {
  def findById(id: Int): Future[Option[AccountsRow]]
  
  def findByLoginId(loginId: String): Future[Option[AccountsRow]]

  def authenticate(acountdata: AccountsRow): Future[Option[AccountsRow]]
}


class UserAccountService @Inject()(val accountsDAO: AccountsDAO, LoginForm:LoginForm)
  extends UserAccountServiceLike {
  import UserAccountService._

  def findById(id: Int): Future[Option[AccountsRow]] = {
    accountsDAO.findById(id)
  }
  def findByLoginId(loginId: String): Future[Option[AccountsRow]] = {
    accountsDAO.findByLoginId(loginId)
  }

  def authenticate(acountdata: AccountsRow): Future[Option[AccountsRow]] = {
    accountsDAO.authenticate(acountdata.loginId, acountdata.password)
  }
}

object UserAccountService {

  val STRETCH_LOOP_COUNT = 1000

  def hashAndStretch(plain: String, salt: String, loopCnt: Int): String = {
    var hashed: String = ""
    (1 to STRETCH_LOOP_COUNT).foreach(i =>
      hashed = DigestUtils.sha256Hex(hashed + plain + salt)
    )
    hashed
  }

  def createPasswordSalt(): String = {
    DigestUtils.sha256Hex(UUID.randomUUID().toString())
  }
}

