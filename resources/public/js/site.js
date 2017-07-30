$(document).ready(function() {
    $('#email-toggle').change(function() {
	$('#email-box').toggle();
    });
});

$(function () {
    $('.review-btn').click (function (e){
	e.preventDefault ();
	var data =  $(this).closest ('form') .serializeArray();
	var $app_element = $(this).closest ('.application');
	data.push ({name: this.name, value: this.value});
	$.ajax ({
	    type: $ (this).closest('form').attr ('method'),
	    url: $ (this).closest('form').attr ('action'),
	    data: data,
	    success: function (result){
		$app_element.hide();
	    },
	    error: function (result){
		//jinkies maybe add actual error handling here
		//it doesn't really matter, as this is only for staff
		console.log(result);
	    }
	});
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
		$('#app-success').removeClass('hidden');},
	    error: function(response) {
		console.log (response);
		$('#form-errors').removeClass('hidden');
		var $errorlist = $('#form-errors ul');
		$.each(response.responseJSON,
		       function (i, val) {
			   var listitem = '<li>' + val + '</li>';
			   $errorlist.append(listitem);
		       });}
	});
    });
});
