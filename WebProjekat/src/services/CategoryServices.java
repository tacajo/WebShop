package services;

import java.util.ArrayList;



import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


import beans.Kategorija;
import beans.Oglas;
import dao.AdDAO;
import dao.CategoryDAO;

@Path("/category")
public class CategoryServices {

	
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init(){
		if(ctx.getAttribute("categoryDAO") == null){
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("categoryDAO", new CategoryDAO(contextPath));
		}
		if(ctx.getAttribute("adDAO") == null){
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("adDAO", new AdDAO(contextPath));
		}
	}
	
	
	@POST
	@Path("/newCategory")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Kategorija> newCategory(Kategorija k){
		CategoryDAO categories = (CategoryDAO) ctx.getAttribute("categoryDAO");
		
		Kategorija category = categories.find(k.getNaziv());
		if(category != null)
			return null;
		
		categories.getCategories().put(k.getNaziv(), k);
		ctx.setAttribute("categoryDAO", categories);
		categories.saveCategories();
		ArrayList<Kategorija> retVal = new ArrayList<Kategorija>();
		
		for (Kategorija kategorija : categories.getCategories().values()) {
			retVal.add(kategorija);
		}
		return retVal;
	}
	
	
	@GET
	@Path("/getCategories")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Kategorija> getCategories(){
		CategoryDAO categories = (CategoryDAO) ctx.getAttribute("categoryDAO");
		
		ArrayList<Kategorija> retVal = new ArrayList<Kategorija>();
		
		for (Kategorija kategorija : categories.getCategories().values()) {
			retVal.add(kategorija);
		}
		return retVal;
	}
	
	@DELETE
	@Path("/deleteCategory/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Kategorija> deleteCategory(@PathParam("naziv") String naziv){
		
		CategoryDAO categories = (CategoryDAO) ctx.getAttribute("categoryDAO");
		
		Kategorija category = categories.find(naziv);
		if(category == null)
			return null;
		
		category.setActive(false);
		
		ctx.setAttribute("categoryDAO", categories);
		categories.saveCategories();
		
		ArrayList<Kategorija> retVal = new ArrayList<Kategorija>();
		 
		for (Kategorija kategorija : categories.getCategories().values()) {
			retVal.add(kategorija);
		}
		 
		 return retVal;
	}
	
	
	@PUT
	@Path("/editCategory/{naziv}/{noviNaziv}/{noviOpis}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Kategorija> editCategory(@PathParam("naziv") String naziv,
								@PathParam("noviNaziv") String noviNaziv, @PathParam("noviOpis") String noviOpis){
		
		System.out.println("usao u editCategory " + naziv + "/" + noviNaziv + "/" + noviOpis);
		CategoryDAO categories = (CategoryDAO) ctx.getAttribute("categoryDAO");
		Kategorija category = categories.find(naziv);
		if(category == null)
			return null;
		category.setNaziv(noviNaziv);
		category.setOpis(noviOpis);
		
		categories.saveCategories();
		ctx.setAttribute("categoryDAO", categories);
		
		
		ArrayList<Kategorija> retVal = new ArrayList<Kategorija>();
		 
		for (Kategorija kategorija : categories.getCategories().values()) {
			retVal.add(kategorija);
		}
		 
		 return retVal;
	}
	
	@GET
	@Path("/getAdsCity")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<String> getAdsCity(){
		ArrayList<String> retVal = new ArrayList<String>();
		
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");
		for (Oglas ad : ads.getAds().values()) {
			if(!retVal.contains(ad.getGrad()))
					retVal.add(ad.getGrad());
		}
		return retVal;
	}

}
