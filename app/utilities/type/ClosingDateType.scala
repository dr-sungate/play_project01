package utilities.`type`

import play.api.i18n.Messages.Implicits._


sealed trait ClosingDateType

object ClosingDateType {

  case object EndOfMonth extends ClosingDateType
  case object day10 extends ClosingDateType
  case object day15 extends ClosingDateType
  case object day20 extends ClosingDateType
  case object day25 extends ClosingDateType

  def valueOf(value: Int): ClosingDateType = value match {
    case `EndOfMonthType` => EndOfMonth
    case `day10Type`    => day10
    case `day15Type`    => day15
    case `day20Type`    => day20
    case `day25Type`    => day25
     case _ => throw new IllegalArgumentException("")
  }
  
  val EndOfMonthType = 0
  val day10Type = 10
  val day15Type = 15
  val day20Type = 20
  val day25Type = 25
 
  val typeSeq = Seq(EndOfMonthType.toString(), day10Type.toString(), day15Type.toString(), day20Type.toString(), day25Type.toString())

}
