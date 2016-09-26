package utilities.`type`

import play.api.i18n.Messages.Implicits._


sealed trait ClientRoleType

object ClientRoleType {

  case object NormalUser extends ClientRoleType
  case object TrialUser extends ClientRoleType
  case object Agency extends ClientRoleType

  def valueOf(value: String): ClientRoleType = value match {
    case NormalUserType => NormalUser
    case TrialUserType    => TrialUser
    case AgencyType    => Agency
     case _ => throw new IllegalArgumentException("")
  }
  
  val NormalUserType = "NormalUser"
  val TrialUserType = "TrialUser"
  val AgencyType = "Agency"
 
  val typeSeq = Seq(NormalUserType, TrialUserType, AgencyType)

}
