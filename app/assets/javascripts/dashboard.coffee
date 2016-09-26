$(document).ready ->
		
$.ajax
	async:     true
	url: $('div#dashboardappcount').attr('data-uri')
	dataType: "json"
	type: "GET",
	context:    this
	success: (data) ->
		$('#dashboardappcount').html(data)

$.ajax
	async:     true
	url: $('div#dashboardagencycount').attr('data-uri')
	dataType: "json"
	type: "GET",
	context:    this
	success: (data) ->
		$('#dashboardagencycount').html(data)

$.ajax
	async:     true
	url: $('div#dashboardclientcount').attr('data-uri')
	dataType: "json"
	type: "GET",
	context:    this
	success: (data) ->
		$('#dashboardclientcount').html(data)
		
$.ajax
	async:     true
	url: $('div#dashboardinvoicecount').attr('data-uri')
	dataType: "json"
	type: "GET",
	context:    this
	success: (data) ->
		$('#dashboardinvoicecount').html(data)
		