package utilities.status

import play.api.i18n.Messages.Implicits._


sealed trait AgencyStatus

object AgencyStatus {

  case object Active extends AgencyStatus
  case object Stopped extends AgencyStatus
  case object InActive extends AgencyStatus

  def valueOf(value: String): AgencyStatus = value match {
    case ActiveStatus => Active
    case StoppedStatus    => Stopped
    case InActiveStatus    => InActive
     case _ => throw new IllegalArgumentException("")
  }
  
  val ActiveStatus = "Active"
  val StoppedStatus = "Stopped"
  val InActiveStatus = "InActive"
 
  val statusSeq = Seq(ActiveStatus, StoppedStatus, InActiveStatus)

}
