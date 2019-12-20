function getSellerReviews() {
	$.get({
		url: 'rest/seller/getSellerReviews',
		contentType: 'application/json',
		success: function(retVal) {
			showSellerReviews(retVal);
			
		}
	});
}

function showSellerReviews(reviews) {
	console.log(reviews);
	$('#cardMessages').html("");
	for (let recenzija of reviews) {
		let reviewCart = $('<div class="card"></div>');
		let reviewHeader = $(' <div class="card-header"></div>');
		let p = $('<h4>' + recenzija.naslov +'</h4><small class="text-muted">' + recenzija.oglas + '</small>');
		reviewHeader.append(p);
		let reviewBody = $('<div class="card-body"></div>');
		
		let reviewContent;
		if(recenzija.slika == "")
			reviewContent = $('<p class="card-text">' + recenzija.sadrzaj +'</p>');
		else
			reviewContent = $('<p class="card-text">' + recenzija.sadrzaj
				+ '<img style="float: right;" src=".'+ recenzija.slika+ '" height="200px" width="160px"/>' +'</p>');
		
		if(recenzija.oglasTacan)
			reviewContent.append('</br> <input type="checkbox" checked disabled> The ad is correct.');
		else
			reviewContent.append('</br> <input type="checkbox" disabled> The ad is correct.');
		
		if(recenzija.dogovor)
			reviewContent.append('</br> <input type="checkbox" checked disabled> The deal was respected.');
		else
			reviewContent.append('</br> <input type="checkbox" disabled> The deal was respected.');
		reviewBody.append(reviewContent);
		
		let footer =  $('<div class="card-footer"></div>');

		let recenzent = $('<small class="text-muted">Sent by: '+ recenzija.recenzent + '</small>')
		footer.append(recenzent);
		
		reviewCart.append(reviewHeader).append(reviewBody).append(footer);
		
		$('#cardMessages').append(reviewCart);
	}
}

function likeSeller() {
	$.ajax({
		type: 'put',
		url: 'rest/seller/likeSeller/' + prodavacReview,
		contentType: 'application/json',
		success: function(retVal) {
			if(retVal == 0)
				alert("This seller is already liked/disliked.");
			else 
				console.log("lajkovan");
		}
	});
}

function dislikeSeller() {
	$.ajax({
		type: 'put',
		url: 'rest/seller/dislikeSeller/' + prodavacReview,
		contentType: 'application/json',
		success: function(retVal) {
			if(retVal == 0)
				alert("This seller is already liked/disliked.");
			else 
				console.log("lajkovan");
		}
	});
}

function getSellerLike() {
	$.get({
		url: 'rest/seller/getSellerLikes',
		contentType: 'application/json',
		success: function(retVal) {
			$('#likesSeller').html("<i style = \"color: #1c8adb;\" class=\"fas fa-thumbs-up\"></i>");
			$('#likesSeller').append(" " + retVal);
			console.log("usao u getSellerLike +++ "+ retVal);
		}
	});
}

function getSellerDislike() {
	$.get({
		url: 'rest/seller/getSellerDislikes',
		contentType: 'application/json',
		success: function(retVal) {
			$('#dislikesSeller').html("<i style = \"color: #1c8adb;\" class=\"fas fa-thumbs-down\"></i>");
			$('#dislikesSeller').append(" " + retVal);
		}
	});
}

function getActiveAd() {
	console.log("usao u getActiveAd()");
	$.get({
		url: 'rest/seller/activeAdsSession',
		contentType: 'application/json',
		success: function(retVal){
			showProduct(retVal, 1);
			$('#description').html("My Adss <i class=\"fas fa-shopping-cart\"></i> <i class=\"fas fa-chevron-right\"></i> Active Ads");
		}
	});
	
}

function getInRealizationAd() {
	console.log("usao u getInRealizationAd()");
	$.get({
		url: 'rest/seller/inRealizationAdsSession',
		contentType: 'application/json',
		success: function(retVal){
			showProduct(retVal, 0);
			console.log(retVal);
			$('#description').html("My Adss <i class=\"fas fa-shopping-cart\"></i> <i class=\"fas fa-chevron-right\"></i> In Realization Ads");
		}
	});
	
}

function getDeliveredAd() {
	console.log("usao u getDeliveredAd");
	$.get({
		url: 'rest/seller/deliveredAdsSession',
		contentType: 'application/json',
		success: function(retVal){
			console.log(retVal);
			showProduct(retVal, 0);
			$('#description').html("My Adss <i class=\"fas fa-shopping-cart\"></i> <i class=\"fas fa-chevron-right\"></i> Delivered Ads");
		}
	});
}

$(document).ready(function() {
	console.log("usao u seller.js");
	
	$('#ShowReviews').click(function() {
		$('#poruka').text("Reviews");
		$('.profilMessages').show();
		$('.featured-categories').hide();
		 getSellerReviews();
	})
});