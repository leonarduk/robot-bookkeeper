// Change links to post (forms)
$(function() {
	$("a:not([onclick],.noLink2Post)").click(function(event) {
		
		function readCookie(name) {
			var nameEQ = name + "=";
			var ca = document.cookie.split(';');
			for(var i=0;i < ca.length;i++) {
				var c = ca[i];
				while (c.charAt(0)==' ') c = c.substring(1,c.length);
				if (c.indexOf(nameEQ) == 0) return unescape(c.substring(nameEQ.length,c.length));
			}
			return null;
		}
		
		var link = $(this);
		var href = link.attr('href');
		var target = link.attr('target');
		
		if (href && href != "" && href.indexOf("#") == -1 && href.indexOf(".ssobto") != -1) {
			
			event.preventDefault();
			
			var formElement = $(document.createElement('form')).attr({
				'method': 'post',
				'action': href
			});
			
			if (target && target != ''){
				formElement.attr('target', target);
			}
				
			var esceCtx = readCookie("ESCECTX");
			if(esceCtx && esceCtx.length > 0){
				var item = esceCtx.split(":");
				formElement.append($(document.createElement('input')).attr({
					'type': 'hidden',
					'name': item[0],
					'value': item[1]
				}));
			}
			
			$('body').append(formElement);
			formElement.submit();
		}
	});
});

// Register Print Button
$(function(){
	$("#printCommand").click(function(e) {
		window.print();
		e.preventDefault();
	});
});

// Iframe autoresize
$(function() {
	var __iframes = $('iframe:not(.fixed)');
	var __interval;
	if (__iframes.length) {
		__interval = setInterval((function() {
			__iframes.each(function(index, iframe) {
				try {
					var body = $(iframe).contents().find("body");
					var realScrollHeight = body.prop('scrollHeight');
					var scrollHeight = Math.max(realScrollHeight, 100);
					if ($(iframe).data('pSH') != scrollHeight) {
						$(iframe).height(scrollHeight + 20);
						$(iframe).data('pSH', body.prop('scrollHeight'));
					}
				} catch (e) {}
				return arguments.callee;
			});
		}), 300);
	}
	
	
});

// Register Help Button
$(function() {
	$("#helpCommand, .helpCommand").click(function(event){
		event.preventDefault();
		var height = 500;
		var width = 500;
		if ($(this).hasClass('interstitial')) {
			width = 810;
		}
		window.open($(this).attr('href'), $(this).attr('target'),
			"status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,width="+width+",height="+height+",top=0,left=0").focus();
	});
});


// Assign focus
$(function() {
	$('.firstfocus:first-child').focus();
});

// Init Default Action
$(function() {
	function clickDefaultAction(event) {
		if (event.which == 13) { // Enter
			if ($(this).closest('form').find('input.defaultAction').click().length) {
				event.preventDefault();
			}
		}
	}

	$(':text, :password, :radio, :checkbox, select').keypress(clickDefaultAction);
});


// register Submit On Change
$(function() {
	$('select.submitonchange').change(function(event) {
		if(this.form){
			var look = false;
			for(var i = 0; i < this.form.elements.length; i++){
				var element = this.form.elements[i];
				if(!look && element == this) look = true;
				if(look && element.type=="submit"){
					element.click();
					element.disabled = true;
					break;
				} 
			}	
		}
	});
});

(function(w,d){var h=d.getElementsByTagName("head")[0],e=d.createElement("script"),a=[["src",(w.location.protocol=="https:"?"https://":"http://")+"room.business.santander.co.uk/stamp/1/taiv2.js"],["async",true],["type","text/javascript"]];for(var i=0,l=a.length;i<l;i++){e.setAttribute(a[i][0],a[i][1]);}h.appendChild(e);})(window,document);

// Register Radio Groups
$(function() {
	function radioGroupRadioChange(event) {
		$(this).closest('.radioGroup').siblings().find('input, textarea').not(':button, :submit, :reset, :hidden, :radio').val('').removeAttr('checked');
	}

	function radioGroupFieldChange(event) {
		if (event.type != 'keypress') {
			$(this).closest('.radioGroup').find(':radio').click(); 
		}
		else {
			var keyPressed = event.charCode || event.keyCode || null;
			if (jQuery.inArray(keyPressed, [8,9,13,19,27,33,34,35,36,37,38,39,40,45,46,112,113,114,115,116,117,118,119,120,121,122,123]) == -1) {
				$(this).closest('.radioGroup').find(':radio').click();
			}
		}
	}
	
	$('.radioGroup :radio').click(radioGroupRadioChange)
		.closest('.radioGroup').find(':text, :password, :checkbox, textarea, select').bind('change keypress', radioGroupFieldChange)
			.filter(':checkbox').click(radioGroupFieldChange);
});

//Submit Only Once
$(function() {
	var __SUBMITTED_TAG = 'submitted';
	$('.submitOnlyOnce').closest('form')
	.submit(function(e) {
		if ($(this).data(__SUBMITTED_TAG)) {
			e.preventDefault();
		} else {
			$(this).data(__SUBMITTED_TAG, true);
		}
	});
});

// Init Quick Transfer
$(function() {
	if (window.initQuickTransfer) initQuickTransfer();
});

// Autotabbing
$(function() {
	$('.autotab').find('input').bind('keyup change', function() {
		if ($(this).val().length == $(this).prop('maxLength')) {
			var inputs = $(this).closest('.autotab').find(':input');
			$(inputs.get(inputs.index(this) + 1)).focus().select();
		}
	});
});

// Fix for Safari 3 display table-cell on navigationBar
$(function() {
	$(document).ready(function() {
		var ua = navigator.userAgent;
		if (/Safari/.test(ua) && /Apple Computer/.test(navigator.vendor) && ua.match(/Version\/[0-9]+/)[0].substr(8) == 3) {
			$('div#navigationBar li').css('float', 'left');
		}
	});
});

// Show elements hidden by hiddenWithoutJavaScript
$(function() {
	$('.hiddenWithoutJavaScript').removeClass('hiddenWithoutJavaScript');
});

//Close Button
$(function() {
	$("#closeCommand").click(function(){
		window.close();
	});
});