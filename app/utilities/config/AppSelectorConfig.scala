package utilities.config

import scala.concurrent.duration._

object AppSelectorConfig {
  val AppSelectorCacheTime: Duration = 1.days
  val AppSelectorCacheKey = "appselector"
  val AppSelectorCookieKey = "appselectorid"
  val AppSelectorCookieExpires = 60*60*24*7

}