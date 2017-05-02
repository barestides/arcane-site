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
	    type: $ (this).closest('form').attr ("method"),
	    url: $ (this).closest('form').attr ("action"),
	    data: data,
	    success: function (result){
		alert (result);
	    }
	});
	e.preventDefault ();
    });
});
