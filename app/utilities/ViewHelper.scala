package utilities

import play.twirl.api.HtmlFormat
import java.util.Date

import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi

 import play.api.Logger
 import play.api.mvc.RequestHeader

object ViewHelper {
  def nl2br(inputstr: Option[String], withescape:Boolean = true):String ={
    inputstr match{
      case Some(str) => 
        if (withescape) 
          HtmlFormat.escape(str).toString.replaceAll("(\r\n|\r|\n)","<br/>")
        else
          str.replaceAll("(\r\n|\r|\n)","<br/>")
      case _ => ""
    }
    
  }
  
  def dateFormat(inputdata: Option[Date], formatstr: String):String = {
    inputdata match{
      case Some(date) => formatstr format date
      case _ => ""
    }
  }

  def dateFormat(inputdata: Date, formatstr: String):String = {
    formatstr format inputdata
  }

  def currencyFormat(inputdata: Option[scala.math.BigDecimal]):String = {
    val formatter = java.text.NumberFormat.getCurrencyInstance
    inputdata match{
      case Some(inputdata) => formatter.format(inputdata)
      case _ => ""
    }
  }

  def currencyFormat(inputdata: scala.math.BigDecimal):String = {
    val formatter = java.text.NumberFormat.getCurrencyInstance
    formatter.format(inputdata)
  }

  def makeOptionSeq(optionvalue:Seq[String], messagekeyname:String, messages:play.api.i18n.Messages) ={
    this.makeSeqWithMessageApi(optionvalue, messagekeyname, messages).toSeq.sortBy(_._1)
  }
  def makeOptionMap(optionvalue:Seq[String], messagekeyname:String, messages:play.api.i18n.Messages) ={
    this.makeSeqWithMessageApi(optionvalue, messagekeyname, messages)
  }
  def makeOptionMap(optionvalue:Seq[String], messagekeyname:String, messages:play.api.i18n.MessagesApi) ={
    this.makeSeqWithMessageApi(optionvalue, messagekeyname, messages)
  }
  def makeSeqWithMessageApi( s:Seq[String] , messagekeyname:String, messages:play.api.i18n.Messages) = {
    def count( l:Seq[String], m:Map[String, String] ):Map[String, String] = {
      if( l.isEmpty ){  // lが空Listだったら再帰終了
        m
      }else {
        val str = messages(messagekeyname + '.' + l.head )
        count( l.tail, m ++ Map( l.head -> str ) )
      }
    }
    
     count( s, Map.empty[String,String] )
  }  
  def makeSeqWithMessageApi( s:Seq[String] , messagekeyname:String, messages:play.api.i18n.MessagesApi) = {
    def count( l:Seq[String], m:Map[String, String] ):Map[String, String] = {
      if( l.isEmpty ){  // lが空Listだったら再帰終了
        m
      }else {
        val str = messages(messagekeyname + '.' + l.head )
        count( l.tail, m ++ Map( l.head -> str ) )
      }
    }
    
     count( s, Map.empty[String,String] )
  }

  def getOptionView(optionval:String, maplist:Map[String, String]):String = {
      maplist.get(optionval) match{ 
        case Some(value) => value
        case None => ""
        }
  }


  def addRequestQuery(url: play.api.mvc.Call, request: RequestHeader):String ={
    url + "?" + request.queryString.map {
      case (k,v) => (k + "=" + v.mkString)
    }.mkString("&")
  }
  
  def removeListExceptTarget(list: Seq[(String, String)], target: String): Seq[(String, String)] = {
    list.filter{case (key, value) => key == target}
  }
}