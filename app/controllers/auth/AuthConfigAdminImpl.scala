package controllers.auth

import controllers.{BaseAuthConfig, RememberMeTokenAccessor}
import play.api.mvc.RequestHeader
import play.api.mvc.Results._
import scala.concurrent.{Future, ExecutionContext}



trait AuthConfigAdminImpl extends BaseAuthConfig {

  def loginSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext) = Future.successful(Redirect(controllers.admin.routes.DashboardController.index))

  def logoutSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext) = Future.successful(Redirect(controllers.auth.routes.LoginLogoutController.index))

  def authenticationFailed(request: RequestHeader)(implicit ctx: ExecutionContext) = Future.successful(Redirect(controllers.auth.routes.LoginLogoutController.index))
  
  override lazy val tokenAccessor = new RememberMeTokenAccessor(sessionTimeoutInSeconds)

}