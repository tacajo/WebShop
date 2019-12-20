package services;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Kupac;
import beans.Oglas;
import beans.Prodavac;
import beans.Recenzija;
import beans.User;
import dao.AdDAO;
import dao.UserDAO;

@Path("/seller")
public class SellerServise {

	@Context
	ServletContext ctx;

	@GET
	@Path("/getSellerReviews")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Recenzija> getSellerReviews(@Context HttpServletRequest request){
		ArrayList<Recenzija> retVal = new ArrayList<Recenzija>();
		
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		User u = (User) request.getSession().getAttribute("ulogovan");
		
		User user = users.findUsername(u.getUsername());
		
		if(user.getUloga().equals("prodavac")){
			for (Recenzija recenzija : ((Prodavac)user).getRecenzije()) {
				retVal.add(recenzija);
			}
		}
		
		return retVal;
	}
	
	@PUT
	@Path("/likeSeller/{prodavac}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public int likeSeller(@PathParam("prodavac")String prodavac, @Context HttpServletRequest request){
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");		
		User user = users.findUsername(prodavac);
		User u = (User) request.getSession().getAttribute("ulogovan");
		User ulogovan = users.findUsername(u.getUsername());
		
		int retVal = 0;
		if(ulogovan.getUloga().equals("kupac")){
			for (String likedSeller : ((Kupac)ulogovan).getLikedDislikedSeller()) {
				if(likedSeller.equals(prodavac))
					return retVal;
			}
		}else
			return retVal;
		
		
		if(user.getUloga().equals("prodavac")){
			((Prodavac)user).setlajk(((Prodavac)user).getlajk() + 1);
			retVal++;
			((Kupac)ulogovan).getLikedDislikedSeller().add(prodavac);
		}
		
		
		users.saveUsers();
		ctx.setAttribute("userDAO", users);
		return retVal;
	}
	
	@PUT
	@Path("/dislikeSeller/{prodavac}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public int dislikeSeller(@PathParam("prodavac")String prodavac, @Context HttpServletRequest request){
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");		
		User user = users.findUsername(prodavac);
		User u = (User) request.getSession().getAttribute("ulogovan");
		User ulogovan = users.findUsername(u.getUsername());
		
		int retVal = 0;
		if(ulogovan.getUloga().equals("kupac")){
			for (String likedSeller : ((Kupac)ulogovan).getLikedDislikedSeller()) {
				if(likedSeller.equals(prodavac))
					return retVal;
			}
		}else
			return retVal;
		
		
		if(user.getUloga().equals("prodavac")){
			((Prodavac)user).setDislajk(((Prodavac)user).getDislajk() + 1);
			retVal++;
			((Kupac)ulogovan).getLikedDislikedSeller().add(prodavac);
		}
		
		
		users.saveUsers();
		ctx.setAttribute("userDAO", users);
		return retVal;
	}
	
	@GET
	@Path("/getSellerLikes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int getSellerLikes(@Context HttpServletRequest request){
		int retVal = 0;
		
		User u = (User) request.getSession().getAttribute("ulogovan");
		UserDAO users= (UserDAO) ctx.getAttribute("userDAO");
		User user = users.findUsername(u.getUsername());
		
		if(user.getUloga().equals("prodavac"))
			retVal = ((Prodavac)user).getlajk();
		
		return retVal;
	}
	
	@GET
	@Path("/getSellerDislikes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int getSellerDislikes(@Context HttpServletRequest request){
		int retVal = 0;
		
		User u = (User) request.getSession().getAttribute("ulogovan");
		UserDAO users= (UserDAO) ctx.getAttribute("userDAO");
		User user = users.findUsername(u.getUsername());
		
		if(user.getUloga().equals("prodavac"))
			retVal = ((Prodavac)user).getDislajk();
		
		return retVal;
	}
	
	@PUT
	@Path("/suspiciousUser/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> suspiciousUser(@PathParam("username")String username){
		System.out.println("suspiciousUser");
		UserDAO users  = (UserDAO) ctx.getAttribute("userDAO");		
		User user = users.findUsername(username);
		ArrayList<User> retVal = new  ArrayList<User>();
		
		user.setReport((user).getReport() + 1);
		
		
		users.saveUsers();
		ctx.setAttribute("userDAO", users);
		for (User user2 : users.getUsers().values()) {
			retVal.add(user2);
		}

		return retVal;
	}
	
	@GET
	@Path("/activeAdsSession")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> activeAdsSession(@Context HttpServletRequest request)
	{
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		User user = (User) request.getSession().getAttribute("ulogovan");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		for (User u : users.getUsers().values()) {
			if(u.getUsername().equals(user.getUsername())){
				for (Oglas oglas : ((Prodavac)u).getObjavljeni()) {
					if(oglas.getStatus().equals("aktivan") && oglas.isAktivan()){
						retVal.add(oglas);
						System.out.println("ACTIVE  " +oglas.getNaziv());
					}
						
				}
			}
		}
		return retVal;
		
	}
	
	@GET
	@Path("/inRealizationAdsSession")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> inRealizationAdsSession(@Context HttpServletRequest request)
	{
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		User user = (User) request.getSession().getAttribute("ulogovan");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		User u = users.findUsername(user.getUsername());
		
		if(u.getUloga().equals("prodavac")){
			for (Oglas oglas : ((Prodavac)u).getObjavljeni()) {
				if(oglas.getStatus().equals("u realizaciji") && oglas.isAktivan()){
					retVal.add(oglas);
					System.out.println("REALIZACIJA  " + oglas.getNaziv());
				}
					
			}
		}
		
		
		return retVal;
		
	}
	
	@GET
	@Path("/deliveredAdsSession")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> deliveredAdsSession(@Context HttpServletRequest request)
	{
		System.out.println("usao u deliveredAdsSession");
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		User user = (User) request.getSession().getAttribute("ulogovan");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		User u = users.findUsername(user.getUsername());
		
		if(u.getUloga().equals("prodavac")){
			for (Oglas oglas : ((Prodavac)u).getObjavljeni()) {
				System.out.println("usao prodavca get delivered");
				if(oglas.getStatus().equals("dostavljen") && oglas.isAktivan()){
					retVal.add(oglas);
					System.out.println("DOSTAVLJENO:" +  oglas.getNaziv());
				}
					
			}
		}
		
		return retVal;
		
	}
	
	@GET
	@Path("/sellmenOfAd/{oglas}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User sellmenOfAd(@PathParam("oglas")String oglas){
		String p = "";
		User u = null;
		System.out.println("usao u sellmenOfAd");
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");
		Oglas ad = ads.find(oglas);
		if(ad == null)
			return null;
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		User ret = users.findUsername(ad.getProdavac());
		
		return ret;
	}
}
