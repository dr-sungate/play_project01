$(document).ready ->
	oauthclientoptions = new OauthClientOptions()
	oauthclientoptions.makeoption()

				
	$('#agencyId').change ->
		oauthclientoptions = new OauthClientOptions()
		oauthclientoptions.makeoption()



class OauthClientOptions
	constructor: () ->
	
	makeoption: () ->
		$('#clientId').find('option').remove().end().append('<option value=""></option>')
		if $('#agencyId').val()
			$.ajax
				async:     true
				url: $('#clientId').attr('data-uri') + "&agencyId=" + $('#agencyId').val()
				dataType: "json"
				type: "GET",
				context:    this
				success: (data) ->
					if data != null
						for obj in data
							$('#clientId').append($("<option>").val(obj["id"]).text(obj["name"]))
						setselected($('#clientId').attr('data-initvalue'))
			
			
						
				
	setselected =  (value) ->
		$('#clientId').val(value)
		
		
window.OauthClientOptions = window.OauthClientOptions || OauthClientOptions