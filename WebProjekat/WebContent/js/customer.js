var prodavacReview;
function deleteReview(naslov, oglas) {
	
	$.ajax({
		type: 'delete',
		url: 'rest/customer/deleteReview/' + naslov + '/' + oglas,
		contentType: 'application/json',
		success: function(retVal) {
			console.log("obrisan review");
			$('#exampleModal').modal('hide');
			showProduct(retVal, 6);
		}
	});
}
function addReview(review) {
	$.ajax({
		type: 'post',
		url: 'rest/ad/ReviewAd',
		contentType: 'application/json',
		data: JSON.stringify({
			"oglas" : review.oglas,
			"recenzent" : review.recenzent,
			"naslov" : review.naslov,
			"sadrzaj" : review.sadrzaj,
			"slika" : review.slika,
			"oglasTacan" : review.oglasTacan,
			"dogovor" : review.dogovor	
		}),
		success: function(retVal) {
			showProduct(retVal, 6);
			$('#reviewModal').modal('hide');
			$('#productName').text(review.oglas);
			$('#titleMessage').val("New Review");
			$('#message-text').val("You have new Review on your ad: " + review.oglas + ", by: " + review.recenzent + ".");
			sellmenOfAd(review.oglas);
			$('#reviewAdName').text("");
			$('#reviewTitle').val("");
			$('#reviewContent').val("");
			$('#checkbox1').prop('checked',false);
			$('#checkbox2').prop('checked',false);
			$('.ShowImage').attr('src', '#').width(5).height(5);
			$('#imgUpload').val("");
			
		}
	});
}
function sellmenOfAd(oglas) {
	$.get({
		url: 'rest/seller/sellmenOfAd/' + oglas,
		contentType: 'application/json',
		success: function(p) {
			$('#recipient-name').val(p.username);	
			sendMessage("automatic message");
		}
	});
	
	
}

function uploadImage2(review, naziv) {
	let file; 

	
	if(($('#imgUpload2'))[0].files.length > 0) {
		file = ($('#imgUpload2'))[0].files[0];
		console.log("usao u if imageUpload");
		var formData = new FormData();
		formData.append("fileToUpload", file);
		formData.append("name", file.name);
		
		console.log("usao u upload2");
		
		$.ajax({
			url: "rest/ad/uploadImage",
			type: 'post',
			data: formData, 
			processData: false,
			contentType: false,
			success: function(response) {
				console.log("usao u success uploada");
				review.slika = "/images/" + ($('#imgUpload2'))[0].files[0].name;
				console.log(review);
				
				if(naziv == 2){
					console.log(naziv + "+++ add review");
					addReview(review);
				}
				else if(naziv == 3){
					console.log(naziv + "+++ reviewSeller");
					reviewSeller(review);
				}
				else{
					console.log(naziv + "+++ editReview");
					editReview(review, naziv);
				}
			}
		});
		
	}else {
		console.log(naziv + "  nije usao u if upload Image");
		if(naziv == 2)
			addReview(review);
		else if(naziv == 3 )
			reviewSeller(review);
		else if(naziv != 1 && naziv != 2 && naziv != 3)
			editReview(review, naziv);
	}
}
function editReview(review, nazivRecenzije) {
	console.log(review);
	console.log(nazivRecenzije);
	
	$.ajax({
		type: 'post',
		url: 'rest/customer/editReviewAd/' + nazivRecenzije,
		contentType: 'application/json',
		data: JSON.stringify({
			"oglas" : review.oglas,
			"recenzent" : review.recenzent,
			"naslov" : review.naslov,
			"sadrzaj" : review.sadrzaj,
			"slika" : review.slika,
			"oglasTacan" : review.oglasTacan,
			"dogovor" : review.dogovor	
		}),
		success: function(retVal) {
			$('#exampleModal').modal('hide');
			$('#reviewModal').modal('hide');
			$('#reviewAdName').text("");
			$('#reviewTitle').val("");
			$('#reviewContent').val("");
			$('#checkbox1').prop('checked',false);
			$('#checkbox2').prop('checked',false);
			$('.ShowImage').attr('src', '#').width(5).height(5);
			$('#imgUpload').val("");
			showProduct(retVal, 6);
			$('#productName').text(review.oglas);
			$('#titleMessage').val("Edited Review");
			$('#message-text').val("The review on your ad: " + review.oglas + " is edited.");
			sellmenOfAd(review.oglas);
		}
		
	});
}

function reviewSeller(review) {
	console.log(prodavacReview);
	
	$.ajax({
		type: 'post',
		url: 'rest/customer/reviewSeller/' + prodavacReview,
		contentType: 'application/json',
		data: JSON.stringify({
			"oglas" : review.oglas,
			"recenzent" : review.recenzent,
			"naslov" : review.naslov,
			"sadrzaj" : review.sadrzaj,
			"slika" : review.slika,
			"oglasTacan" : review.oglasTacan,
			"dogovor" : review.dogovor	
		}),
		success: function() {
			console.log("usao u succes reviewSeller()");
			$('#reviewModal').modal('hide');
			$('#reviewAdName').text("");
			$('#reviewTitle').val("");
			$('#reviewContent').val("");
			$('#checkbox1').prop('checked',false);
			$('#checkbox2').prop('checked',false);
			$('.ShowImage').attr('src', '#').width(5).height(5);
		}
	});
}
function wishList(naziv, error, success){

	console.log("kliknuo srce");
	$.ajax({
		type: 'put',
		url: 'rest/customer/wishList/' + naziv,
		contentType: 'application/json',
		success: function(retVal){
			if(retVal == 0)
			{
				error.text("This ad is already in your Wish List.");
				error.show().delay(1000).fadeOut();
				//alert("This ad is already in Your Wish List.");
			}else{
				success.text("This ad is added to your Wish List.");
				success.show().delay(1000).fadeOut();
			}
		}
		
	});

}
function getWishedList() {
	console.log("usao u getWishedList");
	
	$.get({
		url: 'rest/customer/getWishedAd',
		contentType: 'application/json',
		success: function(retVal){
			console.log(retVal);
			showProduct(retVal, 5);
			$('.fa-heart').css({color:'red'});
			$('#description').html("Wish list <i class=\"far fa-heart\"></i>");
		}
	});
}
function cartList(naziv, error, success) {
	
	console.log("kliknuo korpu");
	$.ajax({
		type: 'put',
		url: 'rest/customer/cartList/' + naziv,
		contentType: 'application/json',
		success: function(retVal){
			console.log(retVal);
			if(retVal == null)
			{
				console.log("usao u if");
				error.text("This ad is already in Your Cart.");
				error.show().delay(1000).fadeOut();
			}else{
				console.log("usao u else");
				success.text("This ad is added to your Cart List.");
				success.show().delay(1000).fadeOut(function() {
					showProduct(retVal, 0);
				});
			}
		}
		
	});
}
function getCartList() {
	
	$.get({
		url: 'rest/customer/getCartdAd',
		contentType: 'application/json',
		success: function(retVal){
			showProduct(retVal, 2);
			//$('.fa-cart-plus').css({color:'#1c8adb'});
			$('#description').html("My orders <i class=\"fas fa-shopping-cart\"></i>");
		}
	});
}

function delivered(naziv, success) {
	$.ajax({
		type: 'put',
		url: 'rest/customer/delivered/' + naziv,
		contentType: 'application/json',
		success: function(retVal){
			success.text("This ad is delivered.");
			success.show().delay(1000).fadeOut(function() {
				showProduct(retVal, 2);
			});
			sendMessage("automatic message");
		}
	});
}

function getDeliveredAdCustomer() {
	
	$.get({
		url: 'rest/customer/getCustomerDeliveredAd',
		contentType: 'application/json',
		success: function(retVal) {
			showProduct(retVal, 6);
			console.log("usao u getDeliveredAdCustomer()");
		}
	});
}
function likeAd(naziv, success, error) {
	$.ajax({
		type: 'put',
		url: 'rest/ad/likeAd/' + naziv,
		contentType: 'application/json',
		success: function(retVal) {
			if(retVal == null){
				error.text("This ad is already liked or disliked.");
				error.show().delay(1000).fadeOut();
			}
			else{
				success.text("You like this Ad.");
				success.show().delay(1000).fadeOut(function() {
					showProduct(retVal, 6);
				});
			}
		}
		
	});
}

function dislikeAd(naziv, success, error) {
	$.ajax({
		type: 'put',
		url: 'rest/ad/dislikeAd/' + naziv,
		contentType: 'application/json',
		success: function(retVal) {
			if(retVal == null){
				error.text("This ad is already liked or disliked.");
				error.show().delay(1000).fadeOut();
			}
			else{
				success.text("You dislike this Ad.");
				success.show().delay(1000).fadeOut(function() {
					showProduct(retVal, 6);
				});
			}
		}
		
	});
}
$(document).ready(function() {
	
	$('#modalCloseReview').click(function(){
			
			$('#reviewAdName').text("");
			$('#reviewTitle').val("");
			$('#reviewContent').val("");
			$('#checkbox1').prop('checked',false);
			$('#checkbox2').prop('checked',false);
			$('.ShowImage').attr('src', '#').width(5).height(5);
		});
	
	$('#AddReviewButton').click(function(){
		let review = null;
		review = new Object();
		review.oglas = $('#reviewAdName').text();
		user = sessionStorage.getItem("ulogovan");
		review.recenzent = JSON.parse(user).username;
		review.naslov = $('#reviewTitle').val();  
		review.sadrzaj = $('#reviewContent').val();
		review.slika="";
		if($('#checkbox1').prop('checked'))
			review.oglasTacan = true;
		else
			review.oglasTacan = false;
		if($('#checkbox2').prop('checked'))
			review.dogovor = true;
		else
			review.dogovor = false;
	
		if(review.naslov == "" || review.sadrzaj == ""){
			
			 $('#errorReview').text("Title and content are required fields.");
			 $('#errorReview').show().delay(3000).fadeOut();
		}else{
			if($('#reviewSellerName').text() != "")
				uploadImage2(review, 3);
			else
				uploadImage2(review, 2);
		}
	});
	
	$('#CartList').click(function(){
		$('.featured-categories').show();
		$('.profilMessages').hide();
		getCartList();
	});
	
	$('#WishList').click(function(){
		$('.featured-categories').show();
		$('.profilMessages').hide();
		getWishedList();
	});
	
});