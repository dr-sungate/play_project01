package utilities.status

import play.api.i18n.Messages.Implicits._


sealed trait AppStatus

object AppStatus {

  case object Active extends AppStatus
  case object Stopped extends AppStatus
  case object InActive extends AppStatus

  def valueOf(value: String): AppStatus = value match {
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
