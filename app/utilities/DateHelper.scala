package utilities

import java.sql.{Timestamp,Date,Time}
import org.joda.time.{DateTime,LocalDate,LocalTime,DateTimeZone}
import org.joda.time.format._
import java.util.Calendar

object DateHelper {
  val nowDate = new java.util.Date()
  
  def dateTimeToSqlTimestamp: DateTime => Timestamp = { dt => new Timestamp(dt.getMillis) }
  def sqlTimestampToDateTime: Timestamp => DateTime = { ts => new DateTime(ts.getTime) }
  def localDateToSqlDate: LocalDate => Date = { ld => new Date(ld.toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis) }
  def sqlDateToLocalDate: Date => LocalDate = { d  => new LocalDate(d.getTime) }
  def localTimeToSqlTime: LocalTime => Time = { lt => new Time(lt.toDateTimeToday.getMillis) }
  def sqlTimeToLocalTime: Time => LocalTime = { t  => new LocalTime(t, DateTimeZone.UTC) }
  
  def dateToString(date:java.util.Date, format:String = "yyyy-MM-dd HH:mm:ss"):String = {
    import java.text._
    val sdf = new SimpleDateFormat(format)
    sdf.format(date)
  }
  def stringToDate(datestr:String, format:String = "yyyy-MM-dd HH:mm:ss"):java.util.Date = {
    import java.text._
    val sdf = new SimpleDateFormat(format)
    sdf.parse(datestr)
  }
  def getDateBeforeMonth(actionDate: java.util.Date, add_period: Int): java.util.Date = {
    val cl = Calendar.getInstance
    cl.setTime(actionDate)
    cl.add(Calendar.MONTH, add_period)
    cl.getTime
  }
  
  def isDateCompare(targetDate: java.util.Date, checktype: String, compareDate: java.util.Date): Boolean = {
    checktype match {
      case ">" => if(targetDate.compareTo(compareDate) > 0) true else false
      case "<" => if(targetDate.compareTo(compareDate) < 0) true else false
      case ">=" | "=>" => if(targetDate.compareTo(compareDate) >= 0) true else false
      case "<=" | "=<" => if(targetDate.compareTo(compareDate) <= 0) true else false
      case _ => false
    }
   }
}