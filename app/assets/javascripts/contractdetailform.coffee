$(document).ready ->
	$('#defaultProductId').change ->
		defaultproductoptions = new DefaultProductOptions()
		defaultproductoptions.getdata()



class DefaultProductOptions
	constructor: () ->
	
	getdata: () ->
		if $('#defaultProductId').val()
			$.ajax
				async:     true
				url: $('#defaultProductId').attr('data-uri') + "&defaultProductId=" + $('#defaultProductId').val()
				dataType: "json"
				type: "GET",
				context:    this
				success: (data) ->
					if data != null && data != ""
						$('#productName').val(data["productName"])
						$('#productType').val(data["productType"])
						$('#contractDetailPrice_unitPrice').val(data["price"])

		
window.DefaultProductOptions = window.DefaultProductOptions || DefaultProductOptions