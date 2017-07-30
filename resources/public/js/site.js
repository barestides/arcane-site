$(document).ready(function() {
    $('#email-toggle').change(function() {
	$('#email-box').toggle();
    });
});

$(function () {
    $('.review-btn').click (function (e){
	var data =  $(this).closest ('form') .serializeArray();
	data.push ({name: this.name, value: this.value});
	console.log (data);
	$.ajax ({
	    type: $ (this).closest('form').attr ('method'),
	    url: $ (this).closest('form').attr ('action'),
	    data: data,
	    success: function (result){
		alert (result);
	    }
	});
	e.preventDefault ();
    });
});

$(function () {
    $('.app-form').submit
    (function (e) {
	e.preventDefault();
	var url = $(this).attr('action');
	var method = $(this).attr('method');
	$.ajax({
	    type: method,
	    url: url,
	    data: $(this).serializeArray(),
	    success: function(response) {
		$('#app-page').hide();
		$('#app-success').removeClass('hidden');
	},
	error: function(response) {
	    console.log (response);
	    $('#form-errors').removeClass('hidden');
	    var $errorlist = $('#form-errors ul');
	    $.each(response.responseJSON,
		   function (i, val) {
		       var listitem = '<li>' + val + '</li>';
		       $errorlist.append(listitem);
		   });
	}
    });
});
});
