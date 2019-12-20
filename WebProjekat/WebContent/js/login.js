$(document).ready(function() {
	
	$('#prijava').submit(function(event){
		event.preventDefault();
		
		let username=$('#username').val();
		let password=$('#password').val();
		console.log(username);
		console.log(password);
		if(!username || !password)
		{
			$('#error').text('All fields is required.');
			$('#error').css({"color": "red", "font-size": "12px"});
			$('#error').show().delay(3000).fadeOut();
			return;
		}
	
		
		$.ajax({
			type: 'post',
			url: 'rest/users/login',
			contentType: 'application/json',
			data: JSON.stringify({
				"username" : username,
				"password": password}),
			success: function(user) {
				console.log(user);
				if(user != null){
					sessionStorage.setItem("ulogovan", JSON.stringify(user));
					$('#success').text('Successful login.');
					$('#success').css({"color": "#7FFF00", "font-size": "12px"});
					$("#success").show().delay(1000).fadeOut(function(){
						window.location = "./shop.html";
					});
				}
				
				else {
					$('#error').text('Incorrect values.');
					$('#error').css({"color": "red", "font-size": "12px"});
					$('#error').show().delay(3000).fadeOut();
				}	
			}
		});
	});
		
	$('#reg').click(function() {
		window.location = "./registracija.html";
	});
	
	
});
