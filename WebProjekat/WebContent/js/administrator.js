
function kategorijaTabela(k){
	console.log("usao u kategorijaTablea()");
	var br = 1;
	//$('#CategoriesTable').show();
	for ( let category of k) {
		if(category.active == true)
		{
			$('#categoriesBody').append("<tr><th scope=\"row\">"+ br++ + "</th><td>"+category.naziv + "</td><td>" + category.opis
			+ "</td><td></td><td><a href=\"#\" data-toggle=\"modal\" data-target=\"#addCategoryTarget\" id=\"editCategory1\"><i class=\"fas fa-pencil-alt\"></i></td>" +
			"</td><td></td><td><a href=\"#\" id=\"deleteCategory\"><i class=\"fas fa-trash\"></i></td></tr>");
			
		}			
	}
}
function deleteCategory(naziv){
	
		
    $.ajax({
        type: 'delete',
        url: 'rest/category/deleteCategory/' + naziv,
        contentType: 'application/json',
        success: function(categories){
            $('#categoriesTable tbody').html('');
            kategorijaTabela(categories);
            kategorije = categories;
            preuzmiKategorije();
        }
    });
    
}
function editCategory(naziv) {	
		console.log("usao  u fun editCategory(naziv)");
		let noviNaziv = $('#nameCategory').val();
		let noviOpis = 	$('#descriptionCategory').val();
		$.ajax({
			type: 'put',
			url: 'rest/category/editCategory/'+ naziv + "/" + noviNaziv + "/" + noviOpis ,
			contentType: 'application/json',
			success: function(categories){				
				console.log(categories);
				noviNaslov = "";
				noviOpis="";
				$('#categoriesTable tbody').html('');
				$('#nameCategory').val("");
				$('#descriptionCategory').val("");
				if(categories != null){
					kategorijaTabela(categories);
					kategorije = categories;
				}
				preuzmiKategorije();
			}
		
		});
	
	
}
function preuzmiKategorije() {
	
	
	$.get({
		url: 'rest/category/getCategories',
		contentType: 'application/json',
		success: function(categories){
			kategorije = categories;
			$('#CategoriesOptions').html("");
			let all = $('<a class="dropdown-item" href="#" id="AllCategoris">All</a>');
			
			let popular = $('<a class="dropdown-item" href="#" id="PopularAds">Popular Ads<i class="fas fa-star"></i></a><div class="dropdown-divider"></div>');
			$('#CategoriesOptions').append(all).append(popular);
			
			popular.click(function() {
				$('#description').text("Popular");
				getPopular();
			});
			
			//sve kategorija u dropMenu
			all.click(function(){
				$('#description').text("All Categories");
				showAd();
			});
			
			var br = 1;
			for(let category of categories){
				let categoryItem = $('<a class="dropdown-item" href="#" id="viewProfile">'+ category.naziv + '</a>')
				
					
				$('#CategoriesOptions').append(categoryItem);
				
				if(br++ == 1){
					$('#selectCategory').html("");
					$('#selectCategory').append('<option  selected value="'+ category.naziv + '">'+ category.naziv +'</option>');
	
				}
				else
					$('#selectCategory').append('<option value="'+ category.naziv + '">' + category.naziv +'</option>');
			
				
				//kad se klikne na kategoriju iz dropMenu
				categoryItem.click(function(){
					$('#description').text(category.naziv);
					getAdsByCategories(category.naziv);
				})
					
			}	
		}
	});
}
function changeRole(newRole, username) {
	console.log("usao u changeRole(newRole, username) " + username +  "+++" + newRole);
	if(newRole != null && username != null){
		console.log(newRole);
		console.log(username);
		$.ajax({
			type: 'put',
			url: 'rest/admin/changeRole/' + username + '/' + newRole,
			contentType: 'application/json',
			success: function(retVal) {
				console.log("uspesno promenjena uloga");
				$('#changeRole').modal('hide');
				showUsers(retVal);
			}
		});
	}
}

$(document).ready(function() {
    preuzmiKategorije();
   
    console.log("usao u administartor.js");
    
    //opcija za brisanje kategorija
	$('#modalClose').click(function(){
		$('#nameCategory').val("");
		$('#descriptionCategory').val("");

	});
    
    //opcija za dodavanje kategorija
	$('#addCategory').click(function(){
		$('#AddCategorybutton').show();
		$('#EditCategorybutton').hide();
		$('#nameCategory').val("");
		$('#descriptionCategory').val("");
		$('#tittleCategory').text("Add category");
    });
    
    
    //dodavanje kategorija
    $('#AddCategorybutton').click(function(event){
    	event.preventDefault();
        var naziv = $('#nameCategory').val();
        var opis = $('#descriptionCategory').val();
        console.log(naziv);
        console.log(opis);
        
        if(!naziv || naziv == "" || !opis || opis == "")
    	{
        	console.log("prazna polja za addCategory");
        	$('#error1').text('All fields are required.');
			$('#error1').css({"color": "red", "font-size": "12px"});
			$('#error1').show().delay(3000).fadeOut();
    	}else{
    		$.post({
                url: 'rest/category/newCategory',
                contentType: 'application/json',
                data: JSON.stringify({
                    "naziv": naziv,
                    "opis": opis}),
                success: function(categories){
                    if(categories == null)
                    {
                        console.log("usao u if");
                        $('#error1').text('Category with this name already exists.');
                        $('#error1').css({"color": "red", "font-size": "12px"});
                        $('#error1').show().delay(1000).fadeOut();
                    }
                    else{
                        $('#CategoriesOptions').append('<a class="dropdown-item" href="#" id="">'+ naziv + '</a>');
                        kategorije = categories;
                        preuzmiKategorije();
                        $('#addCategoryTarget').modal('hide');
                        
                    }
                    
                }

            });
    		
    	}
        
        
        

    });

    $('#editCategories').click( function() {
		$('#AddCategorybutton').hide();
		$('#EditCategorybutton').show();
		$('#tittleCategory').text("Edit category");
		console.log("usao u editCategories()");
		$('#profile').hide();
		$('#UserTable').hide();
		$('.featured-categories').hide();
		$('#options').hide();
		
		$('#CategoriesTable').show();
		$('#categoriesTable').show();
		$('#categoriesTable tbody').html("");
		kategorijaTabela(kategorije);	
    });
    
    $('.table tbody').on('click', '#editCategory1', function(){
    	console.log("usao u editCategory");
		var currow = $(this).closest('tr');	
		let naziv = currow.find('td:eq(0)').text();
		let opis = currow.find('td:eq(1)').text();
		$('#nameCategory').val(naziv);
		$('#sendMessageButton').show();
		$('#editMessageButton').hide();
		$('#descriptionCategory').val(opis);
		console.log(naziv + "+++++++" + opis);
		$('#EditCategorybutton').click(function(){
			console.log("usao u EditCategorybutton");
			editCategory(naziv);
			naziv = null;
			opis = null;
		});
		
	});
	
	$('.table tbody').on('click', '#deleteCategory', function(){
		var currow = $(this).closest('tr');
		var naziv = currow.find('td:eq(0)').text();
		deleteCategory(naziv);
		
	});
	
	$('.table tbody').on('click', '#editUser', function() {
		var currow = $(this).closest('tr');
		var username = currow.find('td:eq(2)').text();
		var role = currow.find('td:eq(3)').text();
		console.log(username+ " + "+ role);
		
		if(role == "customer"){			
			$('#selectRole').html("");
			$('#selectRole').append('<option>seller</option>').append('<option>administrator</option>');
		}else if(role == "seller"){
			$('#selectRole').html("");
			$('#selectRole').append('<option>customer</option>').append('<option>administrator</option>');
		}
		
		$('#changeRoleButton').click(function() {
			var newRole = $('#selectRole').val();
			
			if(newRole == "customer")
				newRole = "kupac";
			else if(newRole == "seller")
				newRole = "prodavac";
			
			console.log(newRole + " + " + username);
			
			changeRole(newRole, username);
			newRole = null;
			username = null;
		});
	});
	
	
});