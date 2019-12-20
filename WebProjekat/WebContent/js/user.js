
function getAdsSession() {
	
	console.log("usaou u getAdsSession");
	$.get({
		url: 'rest/users/adsSession',
		contentType: 'application/json',
		success: function(retVal){
			console.log(retVal);
			showProduct(retVal, 0);
			
			$('#description').html("My ads");
		}
	});
}


function deleteSentMessage(naziv, sadrzaj) {

	$.ajax({
		type: 'put',
		url: 'rest/users/deleteSentMessage/' + naziv + '/'+ sadrzaj,
		contentType: 'application/json',
		success: function(retVal) {
			showSentMessage(retVal);
		}
	});
}
function editMessageFun(naslovPoruke, sadrzaj) {

	
	$('#editMessageButton').click(function() {
		var noviNaslov = $('#titleMessage').val();
		var noviSadrzaj = $('#message-text').val();
		
		if(noviNaslov == "" || noviSadrzaj == ""){
			$('#errorMessage').text("All fields are required.");
			$('#errorMessage').show().delay(1000).fadeOut();
			
		}else{
			console.log(noviNaslov);
			console.log("usao u editMessage()");
			$.ajax({
				type: 'put',
				url: 'rest/users/editMessage/' + naslovPoruke + '/' + sadrzaj + '/' + noviNaslov + '/' + noviSadrzaj,
				contentType: 'application/json',
				success: function(retVal) {
					console.log("usao u success editMessage()");
					$('#MessageModal').modal('hide');
					showSentMessage(retVal);
					
				}
			});
		}
	})
	
}
function showSentMessage(poruke) {
	$('#cardMessages').html("");
	for(let poruka of poruke){
		
		let deleteMessageButton = $('<a href="#"  title="Delete Message" style="float: right;"><i class="fas fa-trash-alt"></i></a>');
		let editMessage = $('<a href="#" title="Edit Message" style="float: right;" data-toggle="modal" data-target="#MessageModal"><i class="fas fa-pencil-alt"></i></a>');
		
		
		let cart = $('<div id="messageCard" class="card"></div>');
		let header = $('<div class="card-header"></div>');
		let p = $('<p><b>To:</b> ' + poruka.primalac +'</br><h4>' + poruka.naslovPoruke +'</h4><small class="text-muted">' + poruka.nazivOglasa + '</small></p>');
		header.append(deleteMessageButton).append(editMessage).append(p);
		let body = $('<div class="card-body"><p class="card-text">' + poruka.sadrzaj + '</p></div>');
		let footer =  $('<div class="card-footer text-right"><small class="text-muted">'+ poruka.datumVreme + '</small></div>');
		cart.append(header).append(body).append(footer);
		$('#cardMessages').append(cart);
		
		deleteMessageButton.click(function(){
			deleteSentMessage(poruka.naslovPoruke, poruka.sadrzaj);
		});
		
		editMessage.click(function() {
			
			$('#recipient-name').val(poruka.primalac);
			$('#titleMessage').val(poruka.naslovPoruke);
			$('#message-text').val(poruka.sadrzaj);
			$('#sendMessageButton').hide();
			$('#editMessageButton').show();
			$('#MessageModalLabel').text("Edit Message");
			editMessageFun(poruka.naslovPoruke, poruka.sadrzaj);
		});
	
	}
}
function getSentMessages() {
	
	$.get({
		url: 'rest/users/getSentMessage',
		contentType: 'application/json',
		success: function(retVal) {
			console.log("usao u getSentMessage");
			$('.featured-categories').hide();
			$('.profilMessages').show();
			$('#poruka').text("Sent Message");
			
			showSentMessage(retVal);
		}
	});
}
function deleteReceivedMessage(naziv, sadrzaj) {
	$.ajax({
		type: 'put',
		url: 'rest/users/deleteReceivedMessage/' + naziv + '/'+ sadrzaj,
		contentType: 'application/json',
		success: function(retVal) {
			console.log("usao u deleteRMessages");
			showReceivedMessage(retVal);
		}
	});
}

function showReceivedMessage(poruke) {
	$('#cardMessages').html("");
	user = sessionStorage.getItem("ulogovan");
	for(let poruka of poruke){
		let deleteMessageButton1 = $('<a href="#" title="Delete Message" style="float: right;"><i class="fas fa-trash-alt"></i></a>');
		
		
		
		let cart = $('<div id="messageCard" class="card"></div>');
		let header = $('<div class="card-header"></div>');
		let p = $('<p><b>From:</b> ' + poruka.posiljalac +'</br><h4>' + poruka.naslovPoruke +'</h4><small class="text-muted">' + poruka.nazivOglasa + '</small></p>');
		if(JSON.parse(user).uloga == "prodavac" && poruka.posiljalac != "admin" && poruka.posiljalac != "automatic message"){
			let reply = $('<a href="#" title="Reply" style="float: right;" data-toggle="modal" data-target="#MessageModal"><i class="fas fa-reply"></i></a>');
			header.append(reply).append(deleteMessageButton1).append(p);
			
			reply.click(function() {
				
				$('#sendMessageButton').show();
				$('#editMessageButton').hide();
				$('#MessageModalLabel').text("New Message");	
				$('#recipient-name').val(poruka.posiljalac);
				$('#recipient-name').attr('disabled','disabled');
			})

		}else 
			header.append(deleteMessageButton1).append(p);
		
		let body = $('<div class="card-body"><p class="card-text">' + poruka.sadrzaj + '</p></div>');
		let footer =  $('<div class="card-footer text-right"><small class="text-muted">'+ poruka.datumVreme + '</small></div>');
		cart.append(header).append(body).append(footer);
		$('#cardMessages').append(cart);
		
		deleteMessageButton1.click(function(){
			deleteReceivedMessage(poruka.naslovPoruke, poruka.sadrzaj);
		});
		
	}
}

function getReceivedMessages() {
	$.get({
		url: 'rest/users/getReceivedMessage',
		contentType: 'application/json',
		success: function(retVal) {
			$('#poruka').text("Received Message");
			$('.profilMessages').show();
			$('.featured-categories').hide();
			
			showReceivedMessage(retVal);
		}
	});
}

function sendMessage(posiljalac) {
	console.log("usao u sendMessage")
	var primalac = $('#recipient-name').val();
	var dNow = new Date();
    var localdate= (dNow.getMonth()+1) + '/' + dNow.getDate() + '/' + dNow.getFullYear() + ' ' + dNow.getHours() + ':' + dNow.getMinutes();
    
	user = sessionStorage.getItem("ulogovan");
	var poruka = new Object();
	
	
	poruka.nazivOglasa = $('#productName').text();
	
	if(posiljalac == "automatic message"){
		console.log("usao u automatic message");
		poruka.posiljalac = posiljalac;
	}
	else
		poruka.posiljalac = JSON.parse(user).username;
	
	poruka.naslovPoruke = $('#titleMessage').val();
	poruka.sadrzaj = $('#message-text').val();
	poruka.datumVreme = localdate;
	poruka.primalac = $('#recipient-name').val();

	var primalac =  $('#recipient-name').val();
	console.log(poruka);
	if(poruka.naslovPoruke == "" || poruka.sadrzaj == ""){
		$('#errorMessage').text("All fields are required.");
		$('#errorMessage').show().delay(1000).fadeOut();
	}else {
		console.log(poruka);
		$.ajax({
			type: 'post',
			url: 'rest/users/sendMessage',
			data: JSON.stringify({
				"nazivOglasa": poruka.nazivOglasa,
				"posiljalac": poruka.posiljalac,
				"naslovPoruke": poruka.naslovPoruke,
				"sadrzaj": poruka.sadrzaj,
				"datumVreme": poruka.datumVreme,
				"primalac": poruka.primalac
			}),
			contentType: 'application/json',
			success: function(retVal) {
				console.log("usao u success posalji poruku  " + retVal);
				if(retVal == 0)
				{
					console.log("niej posoapo poruku");
					$('#userTableSuccess').text('This message cannot be sent.');
					$('#userTableSuccess').css({"color": "red", "font-size": "12px"});
					$("#userTableSuccess").show().delay(1000).fadeOut();
					$('#MessageModal').modal('hide');
				}else
				{
					console.log("poslata poruka");
					$('#userTableSuccess').text('The message is sent.');
					$('#userTableSuccess').css({"color": "#7FFF00", "font-size": "12px"});
					$("#userTableSuccess").show().delay(1000).fadeOut();
					$('#MessageModal').modal('hide');
					$('#titleMessage').val("");
					$('#message-text').val("");
					$('#recipient-name').val("");
				}
				
				
			}
		});
	}
	
}


function getPopular() {
	$.get({
		url: 'rest/users/getPopular',
		contentType: 'application/json',
		success: function(retVal) {
			console.log("usao u popular ads");
			showProduct(retVal,0);
			$('#UserTable').hide();
		}
	});
}
$(document).ready(function() {

	$('#active').click(function(){
		console.log("usao u activeAd");
		$('.profilMessages').hide();
		$('.featured-categories').show();
		getActiveAd();
	});
	
	$('#inRealization').click(function(){
		$('.profilMessages').hide();
		console.log("usao u activeAd");
		$('.featured-categories').show();
		getInRealizationAd();
	});
	
	$('#deliveredAd').click(function(){
		console.log("usao u DELIVEREDAd");
		$('.profilMessages').hide();
		$('.featured-categories').show();
		getDeliveredAd();
	});
	
	
	$('#myAds').click(function(){
		$('.profilMessages').hide();
		$('.featured-categories').show();
		getAdsSession();
	});
	
	
	$('#sendMessageButton').click(function(){	
		sendMessage(0); 
	
	});
	
	$('#sentMessages1').click(function(){
		console.log("kliknuo na sentMessages");
		getSentMessages(); 
	
	});
	$('#receivedMessages1').click(function(){
		getReceivedMessages(); 
	
	});
	$('#sentMessages2').click(function(){
		getSentMessages(); 
	
	});
	$('#receivedMessages2').click(function(){
		getReceivedMessages(); 
	
	});
	$('#sentMessages3').click(function(){
		getSentMessages(); 
	
	});
	$('#receivedMessages3').click(function(){
		getReceivedMessages(); 
	
	});
	
	$('#closeMessageButton').click(function(){
		$('#recipient-name').val("");
		$('#titleMessage').val("");
		$('#message-text').val("");
	
	});
	
	$('#deliveredAdCustomer').click(function(){
		$('.profilMessages').hide();
		$('#description').html("Delivered Ads <i class=\"fa fa-check-circle\"></i>");
		getDeliveredAdCustomer();
	});
	
	
		
	
});