package utilities.`type`

import play.api.i18n.Messages.Implicits._


sealed trait InvoiceIssueType

object InvoiceIssueType {

  case object NotIssue extends InvoiceIssueType
  case object EveryAccount extends InvoiceIssueType
  case object EveryDestination extends InvoiceIssueType

  def valueOf(value: String): InvoiceIssueType = value match {
    case NotIssueType => NotIssue
    case EveryAccountType    => EveryAccount
    case EveryDestinationType    => EveryDestination
     case _ => throw new IllegalArgumentException("")
  }
  
  val NotIssueType = "NotIssue"
  val EveryAccountType = "EveryAccount"
  val EveryDestinationType = "EveryDestination"
 
  val typeSeq = Seq(NotIssueType, EveryAccountType, EveryDestinationType)

}
