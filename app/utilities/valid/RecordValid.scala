package utilities.valid

import play.api.i18n.Messages.Implicits._


sealed trait RecordValid

object RecordValid {

  case object Disabled extends RecordValid
  case object Enabled extends RecordValid

  def valueOf(value: Boolean): RecordValid = value match {
    case IsDisabledRecord => Disabled
    case IsEnabledRecord    => Enabled
     case _ => throw new IllegalArgumentException("")
  }
  
  val IsDisabledRecord = true
  val IsEnabledRecord = false
 
  val statusSeq = Seq(IsDisabledRecord, IsEnabledRecord)

}
