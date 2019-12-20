var user;
var users;
var kategorije;
function naSesiji() {
	$.get({
		url: 'rest/users/getUser',
		contentType: 'application/json',
		success: function(u){
			user = u;
			if(u == null)
				sessionStorage.setItem("ulogovan", null);
			else
				sessionStorage.setItem("ulogovan", JSON.stringify(u));
			
			HomePage();
		},
	});
}
function Odjavi(){
	return function(){
		
		$.get({
			url: 'rest/users/logout',
			contentType: 'application/json',
			success: function() {
				console.log("Izlogovali ste se!");
				sessionStorage.setItem("ulogovan", null);
				HomePage();
				showAd();
				$('.featured-categories').show();
				$('#navbarDropdownCategories').show();
			}
		});
	}
}

function getUsers() {
	$.get({
		url: 'rest/users/getUsers',
		contentType: 'application/json',
		success: function(retVal) {
			users = retVal;	
			console.log(users);
			showUsers(retVal);
		}
	});
}

function getSearchUsers() {
	var ime = $('#nameUserSearch').val();
	var grad =  $('#cityUserSearch').val();
	console.log("usao u getSearchUsers");
	console.log(ime);
	console.log(grad);
	if(ime == "" || ime == null)
		ime = "---";
	console.log(ime);
	$.get({
		url: 'rest/users/getSearchUsers/' + ime + '/' + grad,
		contentType: 'application/json',
		success: function(retVal) {
			showUsers(retVal);			
		}
	});
}

function profile(){
	user = sessionStorage.getItem("ulogovan");
	$('#informations').html("");
	
	$('#informations').append("<h2 class=\"text-center\"> " + JSON.parse(user).ime + " " + JSON.parse(user).prezime + "</h2>");
	if(JSON.parse(user).uloga == "prodavac"){
		$('#informations').append("<h5 class=\"text-center\">- seller -</h5>");
	}
	if(JSON.parse(user).uloga == "kupac"){
		$('#informations').append("<h5 class=\"text-center\">- customer -</h5>");
	}
	$('#informations').append("<p class=\"text-center\">" + JSON.parse(user).username + "</p>");
	$('#informations').append("<p class=\"text-center\">" + JSON.parse(user).telefon + "</p>");
	$('#informations').append("<p class=\"text-center\">" + JSON.parse(user).grad + "</p>");
	if(JSON.parse(user).uloga != "administrator")
		$('#informations').append("<p class=\"text-center\">report (" + JSON.parse(user).report + ")</p>");
}

function HomePage(){
	$('#searchMenu').hide();
	$('#searchUserMenu').hide();
	$('#SearchUserButton').hide();
	$('#SearchButton').show();
	user = sessionStorage.getItem("ulogovan");
	$('#UserTable').hide();	
	$('#description').html("All Categories");
	$('#options').show();
	$('#profile').hide();
	$('#categoriesTable').hide();
	if(user == null || user == "null")
	{
		$('#navbarDropdownCategories').show();
		$('#addAd').hide();
		$('#navbarDropdownProfile').hide();
		$('#contact').hide();
		$('#navbarDropdownProfile').hide();
		$('#addCategory').hide();
		$('#singup').show();
		$('#login').show();
		$('#profile').hide();
		console.log('usao u user null');
		$('#editCategories').hide();
	}
	else if(JSON.parse(user).uloga == "kupac")
	{
		$('#navbarDropdownCategories').show();
		console.log('usao u ulogu kupac');
		$('#addAd').hide();
		$('#addCategory').hide();
		$('#singup').hide();
		$('#login').hide();
		$('#profile').hide();
		$('#contact').show();
		$('#editCategories').hide();
		

	}	
	else if(JSON.parse(user).uloga == "administrator"){
		$('#navbarDropdownCategories').show();
		$('#addAd').hide();
		$('#singup').hide();
		$('#login').hide();
		$('#profile').hide();
		$('#contact').show();
		$('#contact').html("Users");
		$('#addCategory').show();
		$('#editCategories').show();
		
			
	}
	else if(JSON.parse(user).uloga == "prodavac"){
		console.log('usao u ulogu prodavac');
		$('#navbarDropdownCategories').show();
		$('#addAd').show();
		$('#singup').hide();
		$('#login').hide();
		$('#profile').hide();
		$('#contact').show();
		$('#addCategory').hide();
		$('#editCategories').hide();
		$('#options').hide();
	}
	

}

function getAdsCity() {
	$.get({
		url: 'rest/category/getAdsCity',
		contentType: 'application/json',
		success: function(retVal) {
			$('#citySearch').html("<option selected>Choose...</option>");
			for(let city of retVal){
				let cityOption = $('<option>' + city + '</option>');
				$('#citySearch').append(cityOption);
			}
		}
	});
}
function getUserCity() {
	$.get({
		url: 'rest/users/getAdsUser',
		contentType: 'application/json',
		success: function(retVal) {
			$('#cityUserSearch').html("<option selected>Choose...</option>");
			for(let city of retVal){
				let cityOption2 = $('<option>' + city + '</option>');
				$('#cityUserSearch').append(cityOption2);
			}
			 
		}
	});
}

function showUsers(AllUsers) {
	console.log(AllUsers);
	var br = 1;
	user = sessionStorage.getItem("ulogovan");
	$('#userBody').html("");
	$('#sendMessageButton').show();
	$('#editMessageButton').hide();
	if(JSON.parse(user).uloga == "kupac"){
		
		for(let u of AllUsers){
			console.log(u.username);
			if(u.uloga == "prodavac"){
				$('#userBody').append("<tr><th scope=\"row\">"+ br++ + "</th><td>"+u.ime + "</td><td>" + u.prezime + "</td><td>"
						+ u.username +"</td><td>seller</td><td>" + u.grad + "</td><td><a href=\"#\" id=\"sendMessage\" data-toggle=\"modal\" data-target=\"#MessageModal\"><i class=\"fa fa-envelope\"></i></td>"+
						"<td><a href=\"#\" title=\"Suspicious User\" id=\"SuspiciousUser\"><i class=\"fas fa-user-slash\"></i></a></td><td></td></tr>");
			}
			if(u.uloga == "administrator"){
				$('#userBody').append("<tr><th scope=\"row\">"+ br++ + "</th><td>"+u.ime + "</td><td>" + u.prezime + "</td><td>"
						+ u.username +"</td><td>admin</td><td>" + u.grad + "</td><td></td><td></td><td></td></tr>");
			}
			if(u.uloga == "kupac") {
				if(u.username != JSON.parse(user).username){
					$('#userBody').append("<tr><th scope=\"row\">"+ br++ + "</th><td>"+u.ime + "</td><td>" + u.prezime + "</td><td>"
						+ u.username +"</td><td>customer</td><td>" + u.grad + "</td>"+
						"<td></td><td><a href=\"#\" title=\"Suspicious User\" id=\"SuspiciousUser\"><i class=\"fas fa-user-slash\"></i></a></td><td></td></tr>");
				}
			}
		}
	}else if(JSON.parse(user).uloga == "prodavac"){
		
		for(let u of AllUsers){
			if(u.uloga == "administrator"){
				$('#userBody').append("<tr><th scope=\"row\">"+ br++ + "</th><td>"+u.ime + "</td><td>" + u.prezime + "</td><td>"
						+ u.username +"</td><td>admin</td><td>" + u.grad + "</td><td><a href=\"#\" id=\"sendMessage\" data-toggle=\"modal\" data-target=\"#MessageModal\"><i class=\"fa fa-envelope\"></i></td>"+
						"<td></td><td></td></tr>");
			}
			if(u.uloga == "kupac") {
				$('#userBody').append("<tr><th scope=\"row\">"+ br++ + "</th><td>"+u.ime + "</td><td>" + u.prezime + "</td><td>"
						+ u.username +"</td><td>customer</td><td>" + u.grad + "</td><td></td>"+
						"<td><a href=\"#\" title=\"Suspicious User\" id=\"SuspiciousUser\"><i class=\"fas fa-user-slash\"></i></a></td><td></td></tr>");
			}
			if(u.uloga == "prodavac"){
				if(u.username != JSON.parse(user).username){
					$('#userBody').append("<tr><th scope=\"row\">"+ br++ + "</th><td>"+u.ime + "</td><td>" + u.prezime + "</td><td>"
						+ u.username +"</td><td>seller</td><td>" + u.grad + "</td><td></td>"+"" +
								"<td><a href=\"#\" title=\"Suspicious User\" id=\"SuspiciousUser\" ><i class=\"fas fa-user-slash\"></i></a></td><td></td></tr>");
				}
			}
		}
	}else if(JSON.parse(user).uloga == "administrator"){
		
		for(let u of AllUsers){
			if(u.uloga == "kupac") {
				$('#userBody').append("<tr><th scope=\"row\">"+ br++ + "</th><td>"+ u.ime + "</td><td>" + u.prezime + "</td><td>"
						+ u.username +"</td><td>customer</td><td>" + u.grad + "</td><td><a href=\"#\" id=\"sendMessage\" data-toggle=\"modal\" data-target=\"#MessageModal\"><i class=\"fa fa-envelope\"></i></td>"+
						"<td><a href=\"#\" id=\"editUser\" data-toggle=\"modal\" data-target=\"#changeRole\"><i class=\"fas fa-users-cog\"></i></a></td>"+
						"<td><a href=\"#\" title=\"Suspicious User\" id=\"SuspiciousUser\"><i class=\"fas fa-user-slash\"></i></a></td><td></td></tr>");
			}
			if(u.uloga == "prodavac"){
				let row = '<tr><th scope="row">'+ br++ + '</th><td>'+u.ime + '</td><td>' + u.prezime + '</td><td>'
				+ u.username +'</td><td>seller</td><td>' + u.grad + '</td><td><a href="#" title="Send Message" id="sendMessage" data-toggle="modal" data-target="#MessageModal"><i class="fa fa-envelope"></i></td>'+
				'<td><a href="#" title="Change Role" data-toggle="modal" data-target="#changeRole" id="editUser"><i class="fas fa-users-cog"></i></a></td>' +
				'<td><a href="#" title="Suspicious User" id="SuspiciousUser"><i class="fas fa-user-slash"></i></a></td>';
				if(u.report > 3)
					row += '<td><a href="#" title="Remove Suspicious User" id="RemoveBan">Remove ban</a></td></tr>';
				else
					row +='<td></td></tr>';
				$('#userBody ').append(row);
				console.log(u.report);
			}
			if(u.uloga == "administrator"){
				if(u.username != JSON.parse(user).username)
					$('#userBody').append("<tr><th scope=\"row\">"+ br++ + "</th><td>"+u.ime + "</td><td>" + u.prezime + "</td><td>"
						+ u.username +"</td><td>admin</td><td>" + u.grad + "</td><td><a href=\"#\" id=\"sendMessage\" data-toggle=\"modal\" data-target=\"#MessageModal\"><i class=\"fa fa-envelope\"></i></td>"+
						"<td></td>"+
						"<td></td><td></td></tr>");
			}
		}
		
	}
}
function searchAd() {
	var name = $('#nameSearch').val();
	var minPrice = $('#minPriceSearch').val();
	var maxPrice = $('#maxPriceSearch').val();
	var city = $('#citySearch').val();
	var date1 = new Date($('#minDateSearch').val());
	var minDate = date1.getTime();
	var date2 =  new Date($('#maxDateSearch').val());
	var maxDate = date2.getTime();
	var status = $('#statusSearch').val();
	var minLike =  $('#minLikesSearch').val();
	var maxLike =  $('#maxLikesSearch').val();
	
	console.log(minDate + "   " + maxDate);
	if(name == "" || name == null)
		name = "null";
	if(minPrice == "" || minPrice == null)
		minPrice = "null";
	if(maxPrice == "" || maxPrice == null)
		maxPrice = "null";
	if(minDate == "NaN" || minDate == null)
		minDate = "null";
	if(maxDate == "NaN" || maxDate == null)
		maxDate = "null";
	if(minLike == "" || minLike == null)
		minLike = "null";
	if(maxLike == "" || maxLike == null)
		maxLike = "null";
	
	var greska = 0;
	if(minPrice != "null" || maxPrice != "null") {
		if(minPrice < 0 || maxPrice < 0){
			greska = 1;
			$('#errorSearch').text("Value of price must be positive.");
			$('#errorSearch').show().delay(3000).fadeOut();
		}	
		else if(minPrice >= maxPrice){
			greska = 1;
			console.log("usao");
			$('#errorSearch').text("Value of min Price must be less than value of max price.");
			$('#errorSearch').show().delay(3000).fadeOut();
		}
	}
	else if(minLike != "null" || maxLike != "null") {
		if(minLike < 0 || maxLike < 0){
			greska = 1;
			$('#errorSearch').text("Value of likes must be positive.");
			$('#errorSearch').show().delay(3000).fadeOut();
		}	
		else if(minLike >= maxLike){
			greska = 1;;
			$('#errorSearch').text("Value of min Likes must be less than value of max Likes.");
			$('#errorSearch').show().delay(3000).fadeOut();
		}
	}
	else if(minDate != "null" || maxDate != null){
		 if(minDate >= maxDate){
			greska = 1;
			$('#errorSearch').text("Min Date must be less than max Date.");
			$('#errorSearch').show().delay(3000).fadeOut();
		 }
	}
	if(greska == 0){
		$.get({
			url: 'rest/users/search/' + name + '/' + minPrice + '/' + maxPrice + '/' + city + '/' + minDate + '/' + maxDate + '/' + status + '/' + minLike + '/' + maxLike,
			contentType: 'application/json',
			success: function(retVal) {
				console.log(retVal);
				showProduct(retVal, 0);
			}
		});
	}
}
function showAd() {
	user = sessionStorage.getItem("ulogovan");
	if(user != "null" && user != null){
		if(JSON.parse(user).uloga == "administrator"){
			getAllAdminAds();
		}else{
			getAds();
		}
		
	}else
		getAds();
}
$(document).ready(function() {
	naSesiji();
	user = sessionStorage.getItem("ulogovan");

	$('#searchMenu').hide();
	$('#searchUserMenu').hide();
	showAd();
	
	$('#SearchButton').attr('disabled', false);
	$('#SearchUserButton').hide();
	$('#SearchButton').show();
	
	$('#icon, #home').click(function(){
		preuzmiKategorije();
		getAdsCity();
		console.log("usao u icon");
		HomePage();
		showAd();
		$('.featured-categories').show();
		$('#SearchButton').attr('disabled', false);
		$('#searchUserMenu').hide();
		$('#SearchUserButton').hide();
		$('#SearchButton').show();
	});
	
	//registracija
	$('a[href="#singup"]').click(function(){
		window.location="./registracija.html";
	});
	
	//odjava
	$('#logout').click(Odjavi());
	
	//prijava
	$('#login').attr('href', 'index.html');
	
	//kontakt
	$('#contact').click(function() {
		getUserCity();
		$('#searchMenu').hide();
		$('#searchUserMenu').hide();
		$('#SearchUserButton').show();
		$('#SearchButton').hide();
		$('#UserTable').show();
		$('.featured-categories').hide();
		$('#options').hide();
		$('#profile').hide();
		getUsers();

	});
	
	//opcija za profil
	$('#viewProfile').click(function(){
		$('#searchMenu').hide();
		$('#searchUserMenu').hide();
		$('#SearchButton').attr('disabled', 'disabled');
		$('#profile').hide();
		$('#UserTable').hide();
		$('.featured-categories').hide();
		$('#CategoriesTable').hide();
		$('#navbarDropdownCategories').hide();
		$('#options').show();
		profile();
		$('#profile').show();
		$('.profilMessages').hide();
		
		user = sessionStorage.getItem("ulogovan");
		
		if(JSON.parse(user).uloga == "kupac")
		{
			
			 $('#likesProfil').hide();
			 $('#customerOptions').show();
			 $('#sellerOptions').hide();
			 $('#adminOptions').hide();
		}
		else if(JSON.parse(user).uloga == "prodavac")
		{
			getSellerDislike();
			getSellerLike();
			$('#likesProfil').show();
			$('#customerOptions').hide();
			$('#sellerOptions').show();
			$('#adminOptions').hide();
			$('#options').hide();
			if(JSON.parse(user).report > 3){
				$('#addAd1').attr('disabled', 'disabled');
				$('#addAd1').attr('title', "You have a ban.");
				$('#addAd1').attr('class', "btn btn-danger");	
			}else{
				$('#addAd1').attr('disabled', false);
				$('#addAd1').attr('title', "Add new Ad");
				$('#addAd1').attr('class', "btn btn-primary");	
			}
		}
		else if(JSON.parse(user).uloga == "administrator")
		{
			 $('#likesProfil').hide();
			 $('#customerOptions').hide();
			 $('#sellerOptions').hide();
			 $('#adminOptions').show();
		}
		 
		 
	});
	
	$('.table tbody').on('click', '#sendMessage', function(){
		var currow = $(this).closest('tr');
		var naziv = currow.find('td:eq(0)').text();
		var opis = currow.find('td:eq(1)').text();
		var username = currow.find('td:eq(2)').text();
		$('#MessageModalLabel').text("New Message");
		$('#sendMessageButton').show();
		$('#editMessageButton').hide();
		 $('#recipient-name').val(username);
		 $('#recipient-name').attr('disabled','disabled');
		console.log(username);
	});
	
	
	$('.table tbody').on('click', '#SuspiciousUser', function(){		
		var currow = $(this).closest('tr');
		var username = currow.find('td:eq(2)').text();
		console.log(username);
		console.log("rest/seller/suspiciousUser/" + username);
		$.ajax({
			type: 'put',
			url: 'rest/seller/suspiciousUser/' + username,
			contentType: 'application/json',
			success: function(retVal) {
				console.log("ban uradjen");
				showUsers(retVal);
				
			}
		})
		
	});
	$('.table tbody').on('click', '#RemoveBan', function(){		
		var currow = $(this).closest('tr');
		var username = currow.find('td:eq(2)').text();
		console.log(username);
		$.ajax({
			type: 'put',
			url: 'rest/admin/removeBan/' + username,
			contentType: 'application/json',
			success: function(retVal) {
				console.log("ban uklonjen");
				showUsers(retVal);
				
			}
		})
		
	});
	
	
	$('#SearchButton').click(function() {
		$('#searchMenu').slideToggle();
	});
	
	$('#SearchUserButton').click(function() {
		$('#searchUserMenu').slideToggle();
	});
	$('#searchUser').click(function() {
		getSearchUsers();
	});
	
	
	$('#search').click(function() {
		getAdsCity();
		console.log("usao u search1");
		searchAd();
	});
	
});