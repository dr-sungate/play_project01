$(document).ready ->
                    		
	appselect = new AppSelector()
	appselect.initialLoader()
	
	
class AppSelector
	constructor: () ->
	
	setAppSelect: (senddata) ->
		$.ajax
			async:     false
			url: $('div#setappselect').attr('data-uri')
			data: senddata
			type: "PUT",
			success: (data) ->
				appselect = new AppSelector()
				appselect.initialLoader()
				
	initialLoader:  ->
		$.get($('ul#appselector').attr('data-uri'), null, (data)->
			menudata = ""
			for obj in data
				menudata += "<li><a href=\"#\" onclick=\"window.AppSelector.prototype.setAppSelect('applicationId=#{obj["id"]}')\">#{obj["appname"]}</a></li>"
			$('ul#appselector').html(menudata)
		)

		$.ajax(
			async:     true
			url: $('i#currentappselect').attr('data-uri')
			dataType: "json"
			type: "GET",
			context:    this
			success: (data) ->
				$('i#currentappselect').html(data)
		).then ->
	
	
			$.get($('ul#globalnavi-agency').attr('data-uri'), null, (data)->
				menudata = ""
				if data != undefined
					for obj in data
						menudata += "<li class=\"submenu\"><a href=\"/admin/app/#{obj["id"]}/agency\" class=\"small\">#{obj["appname"]}</a></li>"
					$('ul#globalnavi-agency').html(menudata)
			)
			$.get($('ul#globalnavi-client').attr('data-uri'), null, (data)->
				menudata = ""
				if data != undefined
					for obj in data
						menudata += "<li class=\"submenu\"><a href=\"/admin/app/#{obj["id"]}/client\" class=\"small\">#{obj["appname"]}</a></li>"
					$('ul#globalnavi-client').html(menudata)
			)
			$.get($('ul#globalnavi-oauthuser').attr('data-uri'), null, (data)->
				menudata = ""
				if data != undefined
					for obj in data
						menudata += "<li class=\"submenu\"><a href=\"/admin/app/#{obj["id"]}/oauthuser\" class=\"small\">#{obj["appname"]}</a></li>"
					$('ul#globalnavi-oauthuser').html(menudata)
			)
			$.get($('ul#globalnavi-oauthclient').attr('data-uri'), null, (data)->
				menudata = ""
				if data != undefined
					for obj in data
						menudata += "<li class=\"submenu\"><a href=\"/admin/app/#{obj["id"]}/oauthclient\" class=\"small\">#{obj["appname"]}</a></li>"
					$('ul#globalnavi-oauthclient').html(menudata)
			)
		
		
	
	
				

window.AppSelector = window.AppSelector || AppSelector

	
