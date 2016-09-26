package utilities.status

import play.api.i18n.Messages.Implicits._


sealed trait ContractDetailStatus

object ContractDetailStatus {

  case object Contract extends ContractDetailStatus
  case object Applying extends ContractDetailStatus
  case object Cancelled extends ContractDetailStatus
  case object Cancelling extends ContractDetailStatus

  def valueOf(value: String): ContractDetailStatus = value match {
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
