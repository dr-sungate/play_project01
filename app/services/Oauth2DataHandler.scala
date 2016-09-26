package services

import javax.inject.Inject

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

import scalaoauth2.provider._
import scalaoauth2.provider.OAuth2ProviderActionBuilders._

import services.dao._
import models.TablesExtend._
import utilities.oauth.GrantType

import java.util.Date
import org.mindrot.jbcrypt.BCrypt
import java.util.UUID

class Oauth2DataHandler @Inject()(oauthClientsDAO: OauthClientsDAO, oauthUsersDAO: OauthUsersDAO, accessTokensDAO: AccessTokensDAO, authCodesDAO: AuthCodesDAO) extends DataHandler[OauthUsersRow] {

  import scala.concurrent.ExecutionContext.Implicits.global
  
  override def validateClient(request: AuthorizationRequest): Future[Boolean] = {
    request.clientCredential.map { clientCredential =>
   
       oauthClientsDAO.validate(java.util.UUID.fromString(clientCredential.clientId), clientCredential.clientSecret, request.grantType).flatMap(cnt =>
         if (cnt > 0) Future.successful(true)
         else Future.successful(false)
       )
    }.getOrElse(Future.successful(false))
   }

  override def getStoredAccessToken(authInfo: AuthInfo[OauthUsersRow]): Future[Option[scalaoauth2.provider.AccessToken]] = {
   accessTokensDAO.findByToken(authInfo.user.guid, java.util.UUID.fromString(authInfo.clientId.getOrElse(""))).map { optToken =>
      optToken.map { token =>
        scalaoauth2.provider.AccessToken(token.accessToken, token.refreshToken, token.scope, Option(token.expiresIn.toLong), token.createdDate)
      }
    }
     
   }

  def getStoredAuthCode(authInfo: AuthInfo[OauthUsersRow]): Future[Option[Oauth2DataHandlerObj.AuthorizationCode]] = {
   authCodesDAO.findByGUID(authInfo.user.guid, java.util.UUID.fromString(authInfo.clientId.getOrElse(""))).map { optCode =>
      optCode.map { code =>
        Oauth2DataHandlerObj.AuthorizationCode(code.authorizationCode, code.scope, Option(code.expiresIn.toLong), code.createdDate)
      }
    }
     
   }

  override def createAccessToken(authInfo: AuthInfo[OauthUsersRow]): Future[scalaoauth2.provider.AccessToken] = {
    val accessTokenExpiresIn =  60L * 60L
    val refreshToken = Some(Crypto.generateToken)
    val accessToken = Crypto.generateToken
    val now = new Date
    
    val tokenObject = new AccessTokensRow(accessToken, refreshToken, authInfo.user.guid, java.util.UUID.fromString(authInfo.clientId.getOrElse("")), authInfo.redirectUri, authInfo.scope, accessTokenExpiresIn.toInt, new Date)
    accessTokensDAO.deleteExistingAndCreate(tokenObject, authInfo.user.guid, java.util.UUID.fromString(authInfo.clientId.getOrElse("")))
    Future.successful(scalaoauth2.provider.AccessToken(accessToken, refreshToken, authInfo.scope, Option(accessTokenExpiresIn), now))
   }

  def createAuthCode(authInfo: AuthInfo[OauthUsersRow]): Future[Oauth2DataHandlerObj.AuthorizationCode] = {
    val AuthCodeExpiresIn =  60L * 60L
    val authorizationCode = Crypto.generateToken
    val now = new Date
    
    val codeObject = new AuthCodesRow(authorizationCode, authInfo.user.guid, java.util.UUID.fromString(authInfo.clientId.getOrElse("")), authInfo.redirectUri, authInfo.scope, AuthCodeExpiresIn.toInt, new Date)
    authCodesDAO.deleteExistingAndCreate(codeObject, authInfo.user.guid, java.util.UUID.fromString(authInfo.clientId.getOrElse("")))
    Future.successful(Oauth2DataHandlerObj.AuthorizationCode(authorizationCode, authInfo.scope, Option(AuthCodeExpiresIn), now))
   }

  override def findUser(request: AuthorizationRequest): Future[Option[OauthUsersRow]] = {
      request match {
       case request: PasswordRequest =>
         Future.successful(None)
          //Future.successful(Account.authenticate(request.username, request.password))
         case request: ClientCredentialsRequest =>
          request.clientCredential.map { clientCredential =>
            oauthClientsDAO.findByClientCredentialsReturnOauthUser(
              java.util.UUID.fromString(clientCredential.clientId),
              clientCredential.clientSecret.getOrElse(""),
              GrantType.ClientCredentialsGrantType
            )
          }.getOrElse(Future.successful(None))
        case request: AuthorizationCodeRequest =>
          request.clientCredential.map { clientCredential =>
            oauthClientsDAO.findByClientCredentialsReturnOauthUser(
              java.util.UUID.fromString(clientCredential.clientId),
              clientCredential.clientSecret.getOrElse(""),
              GrantType.AuthorizationCodeGrantType
            )
          }.getOrElse(Future.successful(None))
        case _ =>
          Future.successful(None)
      }
   }

  override def findAuthInfoByRefreshToken(refreshToken: String): Future[Option[AuthInfo[OauthUsersRow]]] = {
    val tokenuser = for {
       tokenusertuple <- accessTokensDAO.getWithOauthUserByRefreshToken(refreshToken)
    } yield (tokenusertuple)
    tokenuser.map { tuple =>
      tuple.map{ tokenusertuple =>
        AuthInfo(tokenusertuple._2, Option(tokenusertuple._1.oauthClientId.toString()), tokenusertuple._1.scope, tokenusertuple._1.redirectUri)
      }
    }
   }


  override def refreshAccessToken(authInfo: AuthInfo[OauthUsersRow], refreshToken: String): Future[scalaoauth2.provider.AccessToken] = {
     createAccessToken(authInfo)
   }

  def refreshAuthCode(authInfo: AuthInfo[OauthUsersRow]): Future[Oauth2DataHandlerObj.AuthorizationCode] = {
     createAuthCode(authInfo)
   }

  override def findAuthInfoByCode(code: String): Future[Option[AuthInfo[OauthUsersRow]]] = {
    val codeuser = for {
       codeusertuple <- authCodesDAO.getWithOauthUserByCode(code)
    } yield (codeusertuple)
    codeuser.map { tuple =>
      tuple.map{ codeusertuple =>
        AuthInfo(codeusertuple._2, Option(codeusertuple._1.oauthClientId.toString()), codeusertuple._1.scope, codeusertuple._1.redirectUri)
      }
    }
  }

  override def deleteAuthCode(code: String): Future[Unit] = {
     Future.successful(authCodesDAO.delete(code))
   }


  override def findAccessToken(token: String): Future[Option[scalaoauth2.provider.AccessToken]] = {
    accessTokensDAO.findByAccessToken(token).map { optToken =>
      optToken.map { token =>
        scalaoauth2.provider.AccessToken(token.accessToken, token.refreshToken, token.scope, Option(token.expiresIn.toLong), token.createdDate)
      }
    }

  }


  override def findAuthInfoByAccessToken(accessToken: scalaoauth2.provider.AccessToken): Future[Option[AuthInfo[OauthUsersRow]]]  ={
    val tokenuser = for {
       tokenusertuple <- accessTokensDAO.getWithOauthUserByAccessToken(accessToken.token)
    } yield (tokenusertuple)
    tokenuser.map { tuple =>
      tuple.map{ tokenusertuple =>
        AuthInfo(tokenusertuple._2, Option(tokenusertuple._1.oauthClientId.toString()), tokenusertuple._1.scope, tokenusertuple._1.redirectUri)
      }
    }
   }
}
object Oauth2DataHandlerObj {
  case class AuthorizationCode(code: String, scope: Option[String], lifeSeconds: Option[Long], createdAt: Date) {
    def isExpired: Boolean = expirationTimeInMilis.exists { expTime =>
      expTime <= System.currentTimeMillis
    }
  
    def expiresIn: Option[Long] = expirationTimeInMilis map { expTime =>
      (expTime - System.currentTimeMillis) / 1000
    }
  
    private def expirationTimeInMilis: Option[Long] = lifeSeconds map { lifeSecs =>
      createdAt.getTime + lifeSecs * 1000
    }
  }

}

object Crypto {
  def generateToken: String = {
    val key = java.util.UUID.randomUUID.toString
    new sun.misc.BASE64Encoder().encode(key.getBytes)
  }
}
