package utilities.`type`

import play.api.i18n.Messages.Implicits._


sealed trait DueDateMonthType

object DueDateMonthType {

  case object NextMonth extends DueDateMonthType
  case object NextNextMonth extends DueDateMonthType
  case object NextNextNextMonth extends DueDateMonthType

  def valueOf(value: String): DueDateMonthType = value match {
    case NextMonthType => NextMonth
    case NextNextMonthType    => NextNextMonth
    case NextNextNextMonthType    => NextNextNextMonth
     case _ => throw new IllegalArgumentException("")
  }
  
  val NextMonthType = "NextMonth"
  val NextNextMonthType = "NextNextMonth"
  val NextNextNextMonthType = "NextNextNextMonth"
 
  val typeSeq = Seq(NextMonthType, NextNextMonthType, NextNextNextMonthType)

}
