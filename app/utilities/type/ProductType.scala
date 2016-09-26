package utilities.`type`

import play.api.i18n.Messages.Implicits._


sealed trait ProductType

object ProductType {

  case object InitialCost extends ProductType
  case object MonthlyCost extends ProductType
  case object YearlyCost extends ProductType
  case object AdditionalCost extends ProductType

  def valueOf(value: String): ProductType = value match {
    case InitialCostType => InitialCost
    case MonthlyCostType    => MonthlyCost
    case YearlyCostType    => YearlyCost
    case AdditionalCostType    => AdditionalCost
     case _ => throw new IllegalArgumentException("")
  }
  
  val InitialCostType = "InitialCost"
  val MonthlyCostType = "MonthlyCost"
  val YearlyCostType = "YearlyCost"
  val AdditionalCostType = "AdditionalCost"
 
  val typeSeq = Seq(InitialCostType, MonthlyCostType, YearlyCostType, AdditionalCostType)

}
