$(document).ready(function() {
	
	let datum = new Date().toISOString().split('T')[0];
	
	
	$('#forma').submit(function(event) {
		event.preventDefault();
		let username = $('#username').val();
		let password = $('#password').val();
		let ime =  $('#firstname').val();
		let prezime =  $('#lastname').val();
		let cpassword = $('#confirmpassword').val();
		let telefon = $('#phone').val();
		let grad = $('#city').val();
		let email = $('#email').val();
		let uloga = 'kupac'; 
			
		console.log(ime);
		console.log(prezime);
		console.log(username);
		console.log(email);
		console.log(password);
		console.log(cpassword);
		console.log(telefon);
		console.log(grad);
		console.log(uloga);
		
		if(!username || !password || !ime || !prezime || !telefon || !grad || !email)
		{
			$('#error').text('All fields is required.');
			$('#error').css({"color": "red", "font-size": "12px", "fontweight": 'bold'});
			$('#error').show().delay(3000).fadeOut();
			return;
		}
		
		if(password !== cpassword)
		{
			$('#error').text('Incorrect password');
			$('#error').css({"color": "red", "font-size": "12px", "fontweight": 'bold'});
			$('#error').show().delay(3000).fadeOut();
			return;
		}
		
		$.post({
			url: 'rest/users/registracija',
			data: JSON.stringify({
				"username" : username,
				"password": password,
				"ime": ime,
				"prezime": prezime,
				"email": email,
				"telefon": telefon,
				"grad": grad,
				"uloga": uloga,
				"datum": datum}),
			
			
			contentType: 'application/json',
			success: function(data) {
				console.log(JSON.stringify(data));
				$('#success').text('Successful sing up.');
				$('#success').css({"color": "#7FFF00", "font-size": "12px"});
				$("#success").show().delay(1000).fadeOut(function(){
					
					window.location = "./shop.html";
					
				});
				
			},
			error: function() {
				$('#error').text('User already exists.');
				$('#error').css({"color": "red", "font-size": "12px"});
				$('#error').show().delay(3000).fadeOut();
			}
		});	
		
		
	});
	
});