package utilities.status

import play.api.i18n.Messages.Implicits._


sealed trait ContractStatus

object ContractStatus {

  case object Contract extends ContractStatus
  case object Applying extends ContractStatus
  case object Cancelled extends ContractStatus
  case object Cancelling extends ContractStatus

  def valueOf(value: String): ContractStatus = value match {
    case ContractStatus => Contract
    case ApplyingStatus    => Applying
    case CancelledStatus    => Cancelled
    case CancellingStatus    => Cancelling
     case _ => throw new IllegalArgumentException("")
  }
  
  val ContractStatus = "Contract"
  val ApplyingStatus = "Applying"
  val CancelledStatus = "Cancelled"
  val CancellingStatus = "Cancelling"
 
  val statusSeq = Seq(ContractStatus, ApplyingStatus, CancelledStatus, CancellingStatus)

}
