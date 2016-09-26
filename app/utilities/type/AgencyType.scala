package utilities.`type`

import play.api.i18n.Messages.Implicits._


sealed trait AgencyType

object AgencyType {

  case object Direct extends AgencyType
  case object Oem extends AgencyType
  case object Agency extends AgencyType

  def valueOf(value: String): AgencyType = value match {
    case DirectType => Direct
    case OemType    => Oem
    case AgencyType    => Agency
     case _ => throw new IllegalArgumentException("")
  }
  
  val DirectType = "Direct"
  val OemType = "Oem"
  val AgencyType = "Agency"
 
  val typeSeq = Seq(DirectType, OemType, AgencyType)

}
