package utilities.status

import play.api.i18n.Messages.Implicits._


sealed trait ClientStatus

object ClientStatus {

  case object Active extends ClientStatus
  case object Approval extends ClientStatus
  case object Stopped extends ClientStatus
  case object InActive extends ClientStatus

  def valueOf(value: String): ClientStatus = value match {
    case ActiveStatus => Active
    case ApprovalStatus    => Approval
    case StoppedStatus    => Stopped
    case InActiveStatus    => InActive
     case _ => throw new IllegalArgumentException("")
  }
  
  val ActiveStatus = "Active"
  val StoppedStatus = "Stopped"
  val InActiveStatus = "InActive"
  val ApprovalStatus = "Approval"
 
  val statusSeq = Seq(ActiveStatus, ApprovalStatus, StoppedStatus, ApprovalStatus)

}
