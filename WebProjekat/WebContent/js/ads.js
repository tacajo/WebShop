var today;
var ads;
function dodajOglas(newAd){
	console.log("usao u dodajOglas");

	console.log(newAd.kategorija);
	$.ajax({
		type: 'post',
		url: 'rest/ad/newAd',
		contentType: 'application/json',
		data: JSON.stringify({
			"naziv" : newAd.naziv,
			"kategorija" : newAd.kategorija,
			"cena" : newAd.cena,
			"opis" : newAd.opis,
			"slika" : newAd.slika, 
			"datumIsticanja" : newAd.datumIsticanja,
			"grad" : newAd.grad,
			"prodavac" : newAd.prodavac}),
		success: function(ret){
			if(ret == 0){
				$('#error').text('Ad with this name already exists.');
				$('#error').css({"color": "red", "font-size": "12px"});
				$('#error').show().delay(3000).fadeOut();
			
			}else{
				$('#nameAd').val("");
				$('#priceAd').val("");
				$('#descriptionAd').val("");
				$('#cityAd').val("");
				$('#dateAd').empty();
				$('#imgUpload').val("");
				$('.ShowImage').attr('src', "#").width(5).height(5);
				
				$('#addAdTarget').modal('hide');
				getAdsSession();
				getAdsCity();
			}
				
			
		}
		
		
	});
	
}

function readURL(input) {
	console.log("usao u readURL");
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('.ShowImage')
                .attr('src', e.target.result)
                .width(150)
                .height(200);
            
        };

        reader.readAsDataURL(input.files[0]);
    }
   console.log(reader);
}
function izmeniOglas(ad, naziv, action){	

	if(naziv != null){
		$.ajax({
			type: 'post',
			url: 'rest/ad/editAd/' + naziv,
			contentType: 'application/json',
			data: JSON.stringify({
				"naziv" : ad.naziv,
				"kategorija" : ad.kategorija,
				"cena" : ad.cena,
				"opis" : ad.opis,
				"slika" : ad.slika,
				"datumIsticanja" : ad.datumIsticanja,
				"grad" : ad.grad}),
			success: function(retVal){
				console.log("usao u succes izmeniOglas");
				if(retVal == null){
					$('#error').text('Ad with this name already exists.');
					$('#error').css({"color": "red", "font-size": "12px"});
					$('#error').show().delay(3000).fadeOut();
				
				}else{
					$('#nameAd').val("");
					$('#priceAd').val("");
					$('#descriptionAd').val("");
					$('#cityAd').val("");
					$('#dateAd').val("yyyy-MM-dd");
					$('#imgUpload').val("");
					$('.ShowImage').attr('src', "#").width(5).height(5);
					
					$('#addAdTarget').modal('hide');
					showProduct(retVal, 1);
					$('#addAdTarget').modal('hide');
					if(JSON.parse(user).uloga == "administrator"){
						getAllAdminAds();
						
					}
				}
				if(action == 1){
					sendMessage("automatic message");
					getCustomerOfAd(naziv);			 
					showProduct(retVal, 1);
				}	
					
				
			}
			
			
		});
	}else{
		console.log("nije usao u izmeni oglas")
	}
}

function uploadImage(newAd, naziv, action) {
	console.log("usao u upload Image");
	let file; 
	console.log(($('#imgUpload')).val());
	
	if(($('#imgUpload'))[0].files.length > 0) {
		file = ($('#imgUpload'))[0].files[0];
		console.log(file);
		
		var formData = new FormData();
		formData.append("fileToUpload", file);
		formData.append("name", file.name);
		
		$.ajax({
			url: "rest/ad/uploadImage",
			type: 'post',
			data: formData, 
			processData: false,
			contentType: false,
			success: function(response) {
				console.log("usao u success uploada");
				newAd.slika = "/images/" + ($('#imgUpload'))[0].files[0].name;
				if(naziv === 1)
					dodajOglas(newAd);
				else if(naziv != 1)
				{
					izmeniOglas(newAd, naziv, action);
					console.log(newAd);
				}
			}
		})
		
	}
	if(naziv != 1 && naziv != 2){
		izmeniOglas(newAd, naziv, action);
		console.log("izasao iz uploada");
	}
}

function getAds() {
	$.get({
		url: 'rest/ad/getAds',
		contentType: 'application/json',
		success: function(retVal){
			ads=retVal;
			showProduct(ads, 0);
		}
	});
}



function previewButton(ad) {
	user = sessionStorage.getItem("ulogovan");
	$('#previewProduct .col-md-5').html('<img src=".'+ ad.slika  + '">');	
	$('.product-description p').text(ad.opis);				
	$('#productName').text(ad.naziv);
	$('.price').text("US $" + ad.cena);
	if(ad.status=="aktivan")
		$('#productStatus').text("active");
	else if(ad.status=="u realizaciji")
		$('#productStatus').text("in realization");
	else if(ad.status=="dostavljen")
		$('#productStatus').text("delivered");
	$('#city').html("<b>City:</b> " + ad.grad);
	$('#category').html("<b>Category:</b> " + ad.kategorija);
	$('#likes').html("<i class=\"fa fa-thumbs-up\"></i>" + ad.lajk + "<i class=\"fa fa-thumbs-down\"></i>"+ ad.dislajk)
	$('#seller').html("<b>Sellman:</b> "+ ad.prodavac);
	let date1 = new Date(ad.datumPostavljanja);
	let date2 = new Date(ad.datumIsticanja);
	$('#date1').html("<b>Date of posting:</b> " + date1.toISOString().substr(0, 10));
	$('#date2').html("<b>Date of Expiration:</b> " + date2.toISOString().substr(0, 10));
	
	
	
	if(user == null || user == "null"){
		$('#previewSendMessage').hide();
	}else if(JSON.parse(user).uloga == "kupac"){
		$('#previewSendMessage').show();
	} else if(JSON.parse(user).uloga == "prodavac"){
		$('#previewSendMessage').hide();
	} else if(JSON.parse(user).uloga == "administrator"){
		$('#previewSendMessage').show();
	} 
	
	$('#previewSendMessage').click(function(){
		$('#sendMessageButton').show();
		$('#editMessageButton').hide();
		$('#MessageModalLabel').text("New Message");	
		$('#recipient-name').val(ad.prodavac);
		$('#recipient-name').attr('disabled','disabled');
	});
	console.log(ad.recenzije);
	
	user = sessionStorage.getItem("ulogovan");
	$('#ReviewContainer').html("<h6>Product Reviews</h6><hr>");
	$('#previewReviewSeller').click(function() {
		//$('#exampleModal').modal('hide');
		$('#AddReviewButton').show();
		$('#EditReviewButton').hide();
		$('#reviewSellerName').text(ad.prodavac);
		$('#reviewAdName').text(ad.naziv);
		$('#reviewAdName').hide();
		prodavacReview = "";
		prodavacReview = ad.prodavac;
		
		let likeSellerButton = $('<a href="#"  title="Like Seller" style="float: right; margin-left: 10px; margin-right: 10px; font-size: 20px"><i class="far fa-thumbs-up"></i></a>');
		let dislikeSellerButton = $('<a href="#"  title="Dislike Seller" style="float: right; margin-left: 10px; margin-right: 10px; font-size: 20px"><i class="far fa-thumbs-down"></i></a>');
		$('#reviewSellerName').append(likeSellerButton).append(dislikeSellerButton);
		likeSellerButton.click(function() {
			likeSeller();
		});
		dislikeSellerButton.click(function() {
			dislikeSeller();
		});
	
	});
	
	
	for(let recenzija of ad.recenzije){
		if(recenzija.aktivan){
		console.log(recenzija);
		 
		
		
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
		
		if(user != "null"){
			
		if(JSON.parse(user).username == recenzija.recenzent){

			let deleteReviewbutton = $('<button class="float-md-right" class="card-link" title="Delete Review"><i class="fas fa-trash-alt"></i></button>');
			let editReviewbutton = $('<button class="float-md-right" class="card-link" title="Edit Review" data-toggle="modal" data-target="#reviewModal"><i class="fas fa-pencil-alt"></i></button>');
			footer.append(deleteReviewbutton).append(editReviewbutton);
			
			editReviewbutton.click(function() {
				$('#reviewAdName').text(recenzija.oglas);
				$('#AddReviewButton').hide();
				$('#EditReviewButton').show();
				$('#reviewTitle').val(recenzija.naslov);
				$('#reviewContent').val(recenzija.sadrzaj);
				
				if(recenzija.oglasTacan)
					$('#checkbox1').prop( "checked", true );
				else
					$('#checkbox1').prop('checked',false);
				
				if(recenzija.dogovor)
					$('#checkbox2').prop( "checked", true );
				else
					$('#checkbox2').prop('checked',false);
				if(recenzija.slika != "")
					$('.ShowImage').attr('src', '.' + recenzija.slika).width(150).height(200);
				else
					$('.ShowImage').attr('src', '#').width(10).height(10);
				
				$('#EditReviewButton').click(function(){
					var review = new Object();
					review.oglas = $('#reviewAdName').text();
					user = sessionStorage.getItem("ulogovan");
					review.recenzent = JSON.parse(user).username;
					review.naslov = $('#reviewTitle').val();  
					review.sadrzaj = $('#reviewContent').val(); 
					review.slika = recenzija.slika;
					if($('#checkbox1').prop('checked'))
						review.oglasTacan = true;
					else
						review.oglasTacan = false;
					if($('#checkbox2').prop('checked'))
						review.dogovor = true;
					else
						review.dogovor = false;
					
					uploadImage2(review, recenzija.naslov);
				});
				$('#modalCloseReview').click(function(){
					review = null;
				});
			});
			
			deleteReviewbutton.click(function() {
				deleteReview(recenzija.naslov, recenzija.oglas);
			})
		
		}
		}
		
		reviewCart.append(reviewHeader).append(reviewBody).append(footer);
		$('#ReviewContainer').append(reviewCart);
		
		
		}
	}
}

function getAllAdminAds() {
	$.get({
		url: 'rest/admin/getAllAdminAds',
		contentType: 'application/json',
		success: function(retVal){
			showProduct(retVal, 0);
		}
	});
}

function getAdsByCategories(naziv) {
	
	console.log("usao u getAdsByCategories");
	$.get({
		url: 'rest/ad/getAdsByCategory/' + naziv,
		contentType: 'application/json',
		success: function(retVal){
			$('.featured-categories').show();
			$('#SearchButton').attr('disabled', false);
			$('#searchUserMenu').hide();
			$('#SearchUserButton').hide();
			$('#SearchButton').show();
			$('#UserTable').hide();	
			$('#profile').hide();
			$('#categoriesTable').hide();
			showProduct(retVal, 0);
		}
	});
}
function getAdmins(naziv) {
	$.get({
		url: 'rest/admin/getAdmins',
		contentType: 'application/json',
		success: function(retVal) {
			for (let a of retVal) {
				$('#recipient-name').val(a.username);
				console.log("usao u getAdmins  :" + a.username);
				if(naziv != null){
					$('#message-text').val("Ad " + naziv + " is deleted.");
					$('#titleMessage').val("DELETED AD");
					sendMessage(0);
				}
			}
		}
	});
}

function deleteAd(naziv, action){
	console.log("usao u deleteAd");
	let role;
	
	$.ajax({
		type: 'put',
		url: 'rest/ad/deleteAd/' + naziv,
		contentType: 'application/json',
		success: function(retVal){
			if(retVal == null)
				alert("This ad cannot be deleted.");
			else{
				
				if(action == 1){
					sendMessage("automatic message");
					getCustomerOfAd(naziv);
					showAd();
					showProduct(retVal, 1);
				}else{
					showProduct(retVal, 1);
					getAdmins(naziv);
				}	
			}
				
		}
	
	});
}

function editAdButton(ad, action) {
	$('#selectCategory').attr('disabled', 'disabled');
	$('#tittleAds').text("Edit Ad");
	$('#editAdbutton').show();
	$('#addAdbutton').hide();				
	$('#selectCategory').val( ad.kategorija);
	$('#nameAd').val(ad.naziv);
	$('#priceAd').val(ad.cena);
	$('#descriptionAd').val(ad.opis);
	$('#cityAd').val(ad.grad);
	let date = new Date(ad.datumIsticanja);
	$('#dateAd').val(date.toISOString().substr(0, 10));
	$('.ShowImage').attr('src', "." + ad.slika).width(150).height(200);
	let naziv = ad.naziv;
	$('#editAdbutton').click(function(){
		console.log(naziv + "+++ edited ad");
		event.preventDefault();
		var newAd = new Object();
		newAd.slika = ad.slika;
		modalAd(newAd, naziv, action);
		naziv = null;
	});
	$('#closeAdd').click(function() {
		naziv = null;
	})
}
function getCustomerOfAd(naziv) {
	
	$.get({
		url: 'rest/ad/getCustomerofAd/' + naziv,
		contentType: 'application/json',
		success: function(retVal) {
			if(retVal != null){
				console.log(retVal);
				$('#recipient-name').val(retVal.username);
				sendMessage("automatic message");
			}
		}
	});
}

function showProduct(oglasi, action) {
	$('#products').html("");
	$('.featured-categories').show();
	$('.profilMessages').hide();
	user = sessionStorage.getItem("ulogovan");
	$('#previewReviewSeller').hide();	
	for(let ad of oglasi){
		let success = $('<p id="success" style = "color: #7FFF00; font-size: 12px;"></p>');
		let error = $('<p id="errorShowProduct" style = "color: red; font-size: 12px;"></p>');
		let previewAd = $('<button  id="preview" class="card-link" title="Preview" data-toggle="modal" data-target="#exampleModal"><i class="fa fa-eye"></i></button>');
		let product = $("<div class=\"card col-4-ld mr-4 mt-4\" style=\"width: 16rem;\">"
				 + "<img src=\"." + ad.slika +"\" class=\"card-img-top\">"
				 + "<div class=\"card-body\">"
				 + "<h5 class=\"card-title text-center\">"+ ad.naziv +"</h5>"
				 + "<p class=\"card-text text-center\">$"+ ad.cena + "</p>"
				 + "</div>"
				 + "</div>");
		
		let footer = $("<div class=\"card-footer\"></div>");
		
		footer.append(previewAd);
		
		if(user == null || user == "null")
		{	  
			previewAd.click( function(){						
				previewButton(ad);
			});
		
			
		}
		else if(JSON.parse(user).uloga == "kupac")
		{
			previewAd.click( function(){
				
				previewButton(ad);
			});
			
			if(ad.aktivan && ad.status == "aktivan"){
			//kad se prikaze wish list ne bude korpa
				if(action != 5 && action != 6){
					let cartAd = $('<button  id="cartButton" class="card-link" title="Add to Cart"><i class="fas fa-cart-plus"></i></button>');
					footer.append(cartAd);
					cartAd.click(function(){
						cartList(ad.naziv, error, success);
					});
				}
				
				//za delivered nema lajk
				if(action != 6){
					let favoriteAd = $('<button  id="favoriteButton" class="card-link" title="Add to Wish List"><i class="fas fa-heart"></i></button>');
					footer.append(favoriteAd);
					
					favoriteAd.click(function() {
						wishList(ad.naziv, error, success);
					});
				}
			}
			
			
			//6 je za delivered ad
			if(action === 6){
				let reviewAdButton = $('<button class="card-link" title="Review Ad" data-toggle="modal" data-target="#reviewModal"><i class="fas fa-comments"></i></button>');
				footer.append(reviewAdButton);
				let likeAdButton = $('<button class="card-link" title="Like Ad"><i class="far fa-thumbs-up"></i></button>');
				footer.append(likeAdButton);
				let dislikeAdButton = $('<button class="card-link" title="Dislike Ad"><i class="far fa-thumbs-down"></i></button>');
				footer.append(dislikeAdButton);
				
				likeAdButton.click(function(){
					$(this).css({color:'blue'});
					likeAd(ad.naziv, success, error);
					
				});
				
				dislikeAdButton.click(function(){
					$(this).css({color:'blue'});
					dislikeAd(ad.naziv, success, error);
				});
				
				let reportAdButton = $('<button id="reportAd" class="card-link" style = "float:right;" title="Report Ad">Report</button>');
				footer.append(reportAdButton);
				
				reportAdButton.click(function() {
					$('#recipient-name').val(ad.prodavac);
					$('#productName').text(ad.naziv);
					$('#titleMessage').val("Warning");
					$('#message-text').val("Your Ad " + ad.naziv + " is reported. Sent by "+JSON.parse(user).username + ".");
					sendMessage("automatic message");
					success.text("Report is sent to " +ad.prodavac +".");
					success.show().delay(1000).fadeOut();
				});
				
				let reportAd2Button = $('<button id="reportAd2" class="card-link" style = "margin-left:0px;"  title="Report">Bad Description</button>');
				footer.append(reportAd2Button);
				
				reportAd2Button.click(function() {
					$('#recipient-name').val(ad.prodavac);
					$('#productName').text(ad.naziv);
					$('#titleMessage').val("Warning");
					$('#message-text').val("Your Ad " + ad.naziv + " has bad description. Sent by "+JSON.parse(user).username + ".");
					sendMessage("automatic message");
					success.text("Report is sent to " +ad.prodavac +".");
					success.show().delay(1000).fadeOut();
				});
				
				reviewAdButton.click(function() {
					$('#reviewAdName').text(ad.naziv);
					$('#AddReviewButton').show();
					$('#EditReviewButton').hide();
					$('#reviewSellerName').text("");
					$('#reviewAdName').show();
				});
				
				$('#previewReviewSeller').show();
				
				
				
			}
			
			
			//dodaje se opcija da bude delivered
			if(action === 2){
				console.log("usao u edit")
				let deliveredAd = $('<button class="card-link" title="Delivered"><i class="fas fa-truck-loading"></i></button>');
				footer.append(deliveredAd);
				
				deliveredAd.click(function() {
					console.log(ad.naziv);
					 $('#recipient-name').val(ad.prodavac);
					 $('#productName').text(ad.naziv);
					 $('#titleMessage').val("Delivered: " + ad.naziv);
					 $('#message-text').val("Dear, your Ad " + ad.naziv + " is delivered. You can see all your delivered ads in option My Ads>Delivered.");
					delivered(ad.naziv, success);
				})
				
			}
			
		}
		else  if(JSON.parse(user).uloga == "prodavac")
		{
			if(action === 1){
				console.log("usao u edit")
				let editAd = $('<button class="card-link" title="Edit Ad" data-toggle="modal" data-target="#addAdTarget"><i class="fas fa-pencil-alt"></i></button>');
				footer.append(editAd);
				editAd.click(function(){
					editAdButton(ad, 0);					
				});
				
				let deleteAdbutton = $('<button class="card-link" title="Delete Ad"><i class="fas fa-trash-alt"></i></button>');
				footer.append(deleteAdbutton);
				deleteAdbutton.click(function(){
					deleteAd(ad.naziv, 0);
				});
			
			
			}
			previewAd.click( function(){
				
				previewButton(ad);
				console.log(ad.naziv);
			});
		}
		else if(JSON.parse(user).uloga == "administrator")
		{
			previewAd.click( function(){
				
				previewButton(ad);
				console.log(ad.naziv);
			});
			
			let editAd = $('<button class="card-link" title="Edit Ad" data-toggle="modal" data-target="#addAdTarget"><i class="fas fa-pencil-alt"></i></button>');
			footer.append(editAd);
			editAd.click(function(){
				 $('#recipient-name').val(ad.prodavac);
				 $('#productName').text(ad.naziv);
				 $('#titleMessage').val("Edited Ad: " + ad.naziv);
				 $('#message-text').val("Your Ad " + ad.naziv + " is edited by administrator.");
				 editAdButton(ad, 1);					
			});
			
			let deleteAdbutton = $('<button class="card-link" title="Delete Ad"><i class="fas fa-trash-alt"></i></button>');
			footer.append(deleteAdbutton);
			deleteAdbutton.click(function(){
				
				 $('#recipient-name').val(ad.prodavac);
				 $('#productName').text(ad.naziv);
				 $('#titleMessage').val("Deleted Ad: " + ad.naziv);
				 $('#message-text').val("Your Ad " + ad.naziv + " is deleted by administrator.");
				deleteAd(ad.naziv, 1);
			});
		}
		footer.append(success).append(error);
		product.append(footer);
		
		$('#products').append(product);	
	}
}

function modalAd(newAd, naziv, action){
	
	
	newAd.naziv = $('#nameAd').val();
	newAd.cena = $('#priceAd').val();
	newAd.opis = $('#descriptionAd').val();
	newAd.grad = $('#cityAd').val();
	var date = new Date($('#dateAd').val());
	newAd.datumIsticanja = date.getTime();
	newAd.kategorija = $('#selectCategory').val();
	
	console.log(newAd);
	console.log(newAd.naziv);
	if(!newAd.naziv || newAd.naziv == "" || !newAd.cena || newAd.cena == "" || !newAd.opis || newAd.opis == "" || !newAd.grad || newAd.grad == "" ||
				!newAd.datumIsticanja || newAd.datumIsticanja == ""  || !newAd.kategorija || newAd.kategorija == "")
	{
		$('#error').text('All fields are required.');
		$('#error').css({"color": "red", "font-size": "12px"});
		$('#error').show().delay(3000).fadeOut();
	
	}else{
		uploadImage(newAd, naziv, action);
		
	}
	newAd = null;
}
$(document).ready(function(){
	console.log("usao u ads.js");
	user = sessionStorage.getItem("ulogovan");
	
	showAd();
	$('#description').html("<button  id=\"bla\">bla</button>");
	
	
	$('#icon').click(function(){
		user = sessionStorage.getItem("ulogovan");
		
		showAd();
	});
	
	$('#dateAd').attr('min', today);
	
	$('#closeAdd').click(function() {
		$('#nameAd').val("");
		$('#priceAd').val("");
		$('#descriptionAd').val("");
		$('#cityAd').val("");
		$('#dateAd').val("mm/dd/yyyy");
		$('#imgUpload').val("");
		$('.ShowImage').attr('src', "#").width(5).height(5);

	});
	
	$('#addAd').click(function(){
		$('#tittleAds').text("Add Ad");
		$('#editAdbutton').hide();
		$('#addAdbutton').show();
	});
	
	$('#addAd1').click(function(){
		
		$('#selectCategory').attr('disabled', false);
		$('#tittleAds').text("Add Ad");
		$('#editAdbutton').hide();
		$('#addAdbutton').show();
		$('#nameAd').val("");
		$('#priceAd').val("");
		$('#descriptionAd').val("");
		$('#cityAd').val("");
		$('#dateAd').empty();
		$('#imgUpload').val("");
		$('.ShowImage').attr('src', "#").width(5).height(5);
		
	});
	
	$('#addAdbutton').click(function(event){
		event.preventDefault();
		console.log("usao u dodajOglas");
		user = sessionStorage.getItem("ulogovan");
		
		var newAd = new Object();
		newAd.naziv = $('#nameAd').val();
		newAd.cena = $('#priceAd').val();
		newAd.opis = $('#descriptionAd').val();
		newAd.grad = $('#cityAd').val();
		let date = new Date($('#dateAd').val());
		newAd.datumIsticanja = date.getTime();
		newAd.kategorija = $('#selectCategory').val();
		newAd.prodavac = JSON.parse(user).username;
		
		var image  = $('#imgUpload').val();
		
		
		if(!newAd.naziv || newAd.naziv == "" || !newAd.cena || newAd.cena == "" || !newAd.opis || newAd.opis == "" || !newAd.grad || newAd.grad == "" ||
					!newAd.datumIsticanja || newAd.datumIsticanja == "" || !image || image == "" || !newAd.kategorija || newAd.kategorija == "")
		{
			$('#error').text('All fields are required.');
			$('#error').css({"color": "red", "font-size": "12px"});
			$('#error').show().delay(3000).fadeOut();
		
		}else{
			uploadImage(newAd, 1, 0);
			
		}
		
			
	});
	
	
	
	
	
	
});