package services;

import java.util.ArrayList;

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

import com.sun.media.jfxmedia.Media;

import beans.Kategorija;
import beans.Oglas;
import beans.Recenzija;
import beans.User;
import beans.Prodavac;
import beans.Kupac;

import dao.AdDAO;
import dao.CategoryDAO;
import dao.UserDAO;

@Path("/customer")
public class CustomerService {

	@Context
	ServletContext ctx;
	
	
	@POST
	@Path("/editReviewAd/{naslovRecenzije}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> editReviewAd(Recenzija recenzija, @PathParam("naslovRecenzije")String naslovRecenzije,
			@Context HttpServletRequest request){
		
		System.out.println(recenzija);
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		Oglas ad = null;
		//oglasi.json
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");
		for (Oglas o : ads.getAds().values()) {
			if(o.getNaziv().equals(recenzija.getOglas()))
			{
				ad = o;
				for (Recenzija r : o.getRecenzije()) {
					if(r.getNaslov().equals(naslovRecenzije)){
						r.setOglas(recenzija.getOglas());
						r.setRecenzent(recenzija.getRecenzent());
						r.setNaslov(recenzija.getNaslov());
						r.setSadrzaj(recenzija.getSadrzaj());
						r.setSlika(recenzija.getSlika());
						r.setOglasTacan(recenzija.isOglasTacan());
						r.setDogovor(recenzija.isDogovor());
					}
				}
			}
		}
		ads.saveAds();
		ctx.setAttribute("adDAO", ads);
		
		//kategorije.json
		CategoryDAO cats = (CategoryDAO) ctx.getAttribute("categoryDAO");
		Kategorija k = cats.find(ad.getKategorija());		
		for (Oglas o : k.getOglasi()) {
			if(o.getNaziv().equals(recenzija.getOglas()))
			{
				for (Recenzija r : o.getRecenzije()) {
					if(r.getNaslov().equals(naslovRecenzije)){
						r.setOglas(recenzija.getOglas());
						r.setRecenzent(recenzija.getRecenzent());
						r.setNaslov(recenzija.getNaslov());
						r.setSadrzaj(recenzija.getSadrzaj());
						r.setSlika(recenzija.getSlika());
						r.setOglasTacan(recenzija.isOglasTacan());
						r.setDogovor(recenzija.isDogovor());
					}
				}
			}
		}
		cats.saveCategories();
		ctx.setAttribute("categoryDAO", cats);
		
		//prodavci.json i kupci.json
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		for (User u : users.getUsers().values()) {
			
			//prodavci
			if(u.getUloga().equals("prodavac")){
				for (Oglas o : ((Prodavac)u).getObjavljeni()) {
					if(o.getNaziv().equals(recenzija.getOglas())){						
						for (Recenzija r : o.getRecenzije()) {
							if(r.getNaslov().equals(naslovRecenzije)){
								r.setOglas(recenzija.getOglas());
								r.setRecenzent(recenzija.getRecenzent());
								r.setNaslov(recenzija.getNaslov());
								r.setSadrzaj(recenzija.getSadrzaj());
								r.setSlika(recenzija.getSlika());
								r.setOglasTacan(recenzija.isOglasTacan());
								r.setDogovor(recenzija.isDogovor());
							}
						}
					}
				}
				for (Oglas o : ((Prodavac)u).getIsporuceni()) {
					if(o.getNaziv().equals(recenzija.getOglas()))
					{
						for (Recenzija r : o.getRecenzije()) {
							if(r.getNaslov().equals(naslovRecenzije)){
								r.setOglas(recenzija.getOglas());
								r.setRecenzent(recenzija.getRecenzent());
								r.setNaslov(recenzija.getNaslov());
								r.setSadrzaj(recenzija.getSadrzaj());
								r.setSlika(recenzija.getSlika());
								r.setOglasTacan(recenzija.isOglasTacan());
								r.setDogovor(recenzija.isDogovor());
							}
						}
					}
				}
			}
			
			//kupci
			if(u.getUloga().equals("kupac")){
				for (Oglas o : ((Kupac)u).getPoruceni()) {
					if(o.getNaziv().equals(recenzija.getOglas())){
						for (Recenzija r : o.getRecenzije()) {
							if(r.getNaslov().equals(naslovRecenzije)){
								r.setOglas(recenzija.getOglas());
								r.setRecenzent(recenzija.getRecenzent());
								r.setNaslov(recenzija.getNaslov());
								r.setSadrzaj(recenzija.getSadrzaj());
								r.setSlika(recenzija.getSlika());
								r.setOglasTacan(recenzija.isOglasTacan());
								r.setDogovor(recenzija.isDogovor());
							}
						}
					}
				}
				for (Oglas o : ((Kupac)u).getDostavljeni()) {
					if(o.getNaziv().equals(recenzija.getOglas())){
						for (Recenzija r : o.getRecenzije()) {
							if(r.getNaslov().equals(naslovRecenzije)){
								r.setOglas(recenzija.getOglas());
								r.setRecenzent(recenzija.getRecenzent());
								r.setNaslov(recenzija.getNaslov());
								r.setSadrzaj(recenzija.getSadrzaj());
								r.setSlika(recenzija.getSlika());
								r.setOglasTacan(recenzija.isOglasTacan());
								r.setDogovor(recenzija.isDogovor());
							}
							
						}
					}
					
					
					
				}
				for (Oglas o : ((Kupac)u).getOmiljeni()) {
					if(o.getNaziv().equals(recenzija.getOglas())){
						for (Recenzija r : o.getRecenzije()) {
							if(r.getNaslov().equals(naslovRecenzije)){
								r.setOglas(recenzija.getOglas());
								r.setRecenzent(recenzija.getRecenzent());
								r.setNaslov(recenzija.getNaslov());
								r.setSadrzaj(recenzija.getSadrzaj());
								r.setSlika(recenzija.getSlika());
								r.setOglasTacan(recenzija.isOglasTacan());
								r.setDogovor(recenzija.isDogovor());
							}
						}
					}
				}
			}
		}
		User user = (User) request.getSession().getAttribute("ulogovan");
		User ulogovan = users.findUsername(user.getUsername());
		if(ulogovan != null){
			if(ulogovan.getUloga().equals("kupac")){
				for (Oglas oglas2 : ((Kupac)ulogovan).getDostavljeni()) {
					if(oglas2.isAktivan())
						retVal.add(oglas2);
				}
			}
		}
		users.saveUsers();
		ctx.setAttribute("userDAO", users);
		return retVal;
	}
	
	@DELETE
	@Path("/deleteReview/{naslovRecenzije}/{oglas}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public  ArrayList<Oglas> deleteReview(@PathParam("naslovRecenzije")String naslovRecenzije,
			@PathParam("oglas")String oglas ,@Context HttpServletRequest request){
		
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		User user = (User) request.getSession().getAttribute("ulogovan");
		
		Oglas ad = null;
		//oglasi.json
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");
		for (Oglas o : ads.getAds().values()) {
			if(o.getNaziv().equals(oglas))
			{
				ad = o;
				for (Recenzija r : o.getRecenzije()) {
					if(r.getNaslov().equals(naslovRecenzije) && r.getRecenzent().equals(user.getUsername()))
						r.setAktivan(false);
				}
			}
		}
		ads.saveAds();
		ctx.setAttribute("adDAO", ads);
		
		
		//kategorije.json
		CategoryDAO cats = (CategoryDAO) ctx.getAttribute("categoryDAO");
		Kategorija k = cats.find(ad.getKategorija());		
		for (Oglas o : k.getOglasi()) {
			if(o.getNaziv().equals(oglas))
			{
				for (Recenzija r : o.getRecenzije()) {
					if(r.getNaslov().equals(naslovRecenzije) && r.getRecenzent().equals(user.getUsername()))
						r.setAktivan(false);
				}
			}
		}
		cats.saveCategories();
		ctx.setAttribute("categoryDAO", cats);
		
		//prodavci.json i kupci.json
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		for (User u : users.getUsers().values()) {
			
			//prodavci
			if(u.getUloga().equals("prodavac")){
				for (Oglas o : ((Prodavac)u).getObjavljeni()) {
					if(o.getNaziv().equals(oglas))
					{
						for (Recenzija r : o.getRecenzije()) {
							if(r.getNaslov().equals(naslovRecenzije) && r.getRecenzent().equals(user.getUsername()))
								r.setAktivan(false);
						}
					}
				}
				for (Oglas o : ((Prodavac)u).getIsporuceni()) {
					if(o.getNaziv().equals(oglas))
					{
						for (Recenzija r : o.getRecenzije()) {
							if(r.getNaslov().equals(naslovRecenzije) && r.getRecenzent().equals(user.getUsername()))
								r.setAktivan(false);
						}
					}
				}
			}
			
			//kupci
			if(u.getUloga().equals("kupac")){
				for (Oglas o : ((Kupac)u).getPoruceni()) {
					if(o.getNaziv().equals(oglas))
					{
						for (Recenzija r : o.getRecenzije()) {
							if(r.getNaslov().equals(naslovRecenzije) && r.getRecenzent().equals(user.getUsername()))
								r.setAktivan(false);
						}
					}
				}
				for (Oglas o : ((Kupac)u).getDostavljeni()) {
					if(o.getNaziv().equals(oglas))
					{
						for (Recenzija r : o.getRecenzije()) {
							if(r.getNaslov().equals(naslovRecenzije) && r.getRecenzent().equals(user.getUsername())){
								r.setAktivan(false);
							}
						}
					}
					
				}
				for (Oglas o : ((Kupac)u).getOmiljeni()) {
					if(o.getNaziv().equals(oglas))
					{
						for (Recenzija r : o.getRecenzije()) {
							if(r.getNaslov().equals(naslovRecenzije) && r.getRecenzent().equals(user.getUsername()))
								r.setAktivan(false);
						}
					}
				}
			}
		}
		
		User ulogovan = users.findUsername(user.getUsername());
		if(ulogovan != null){
			if(ulogovan.getUloga().equals("kupac")){
				for (Oglas oglas2 : ((Kupac)ulogovan).getDostavljeni()) {
					if(oglas2.isAktivan())
						retVal.add(oglas2);
				}
			}
		}
		users.saveUsers();
		ctx.setAttribute("userDAO", users);
		return retVal;
	}
	
	@POST
	@Path("/reviewSeller/{prodavac}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void reviewSeller(Recenzija recenzija, @PathParam("prodavac")String prodavac){
		
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		User u = users.findUsername(prodavac);
		if(u != null){
			if(u.getUloga().equals("prodavac")){
				((Prodavac)u).getRecenzije().add(recenzija);
				System.out.println("nasao i dodao recenziju");
				System.out.println(((Prodavac)u).getRecenzije().size());
			}
		}
		System.out.println(u.username);
		users.saveUsers();
		ctx.setAttribute("userDAO", users);
	}
	
	@PUT
	@Path("/wishList/{oglas}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int wishList(@PathParam("oglas")String oglas, @Context HttpServletRequest request)
	{
		User user = (User) request.getSession().getAttribute("ulogovan");
		
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");
		Oglas ad = null;
			
		for (Oglas o : ads.getAds().values()) {
			if(o.getNaziv().equals(oglas)){
				ad = o;
				o.setWished(ad.getWished() + 1);
			}
		}
		
		ads.saveAds();
		ctx.setAttribute("adDAO", ads);
		
		User u = users.findUsername(user.getUsername());
		if(u.getUloga().equals("kupac")){
			for (Oglas o : ((Kupac)u).getOmiljeni()) {
				if(o.getNaziv().equals(oglas))
					return 0;
			}
			((Kupac)u).getOmiljeni().add(ad);
		}
		ctx.setAttribute("userDAO", users);
		users.saveUsers();
		
		//kategorije.json
		CategoryDAO cats = (CategoryDAO) ctx.getAttribute("categoryDAO");
		Kategorija k = cats.find(ad.getKategorija());		
		for (Oglas o : k.getOglasi()) {
			if(o.getNaziv().equals(oglas))
				o.setWished(o.getWished() + 1);
		}
		cats.saveCategories();
		ctx.setAttribute("categoryDAO", cats);
		
		
		return 1;
	}

	
	@GET
	@Path("/getWishedAd")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> getWishedAds(@Context HttpServletRequest request){
		
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		User user = (User) request.getSession().getAttribute("ulogovan");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		if(user != null){
			for (User u : users.getUsers().values()) {
				if(u.getUsername().equals(user.getUsername())){
					System.out.println("nasao usera");
					for (Oglas o : ((Kupac)u).getOmiljeni()) {
						if(o.isAktivan() && o.getStatus().equals("aktivan"))
							retVal.add(o);			
					}
					
				}
			}
		}
		
		
		
		return retVal; 
	}
	@PUT
	@Path("/cartList/{oglas}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> cartList(@PathParam("oglas")String oglas, @Context HttpServletRequest request)
	{
		User user = (User) request.getSession().getAttribute("ulogovan");
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");
		String prodavac = "";
		for (User u : users.getUsers().values()) {
			if(user.getUsername().equals(u.getUsername())){
				System.out.println("nasao usera");
				if(u.getUloga().equals("kupac")){
					for (Oglas o : ((Kupac)u).getPoruceni()) {
						if(o.getNaziv().equals(oglas))
							return null;
					}
					for (Oglas o : ads.getAds().values()) {
						if(o.getNaziv().equals(oglas)){
							o.setStatus("u realizaciji");	
							((Kupac)u).getPoruceni().add(o);
							prodavac  = o.getProdavac();
						}else if(o.getStatus().equals("aktivan") && o.isAktivan()){
							retVal.add(o);
						}
							
					}
				}
			}
		}
		
		User p = users.findUsername(prodavac);
		if(p.getUloga().equals("prodavac")){
			for (Oglas oglas2 : ((Prodavac)p).getObjavljeni()) {
				if(oglas2.getNaziv().equals(oglas))
					oglas2.setStatus("u realizaciji");
			}
		}
		ctx.setAttribute("userDAO", users);
		users.saveUsers();
		ctx.setAttribute("adDAO", ads);
		ads.saveAds();
		
		CategoryDAO cats = (CategoryDAO) ctx.getAttribute("categoryDAO");
		for (Kategorija k : cats.getCategories().values()) {
			for (Oglas o : k.getOglasi()) {
				if(o.getNaziv().equals(oglas))
					o.setStatus("dostavljen");
			}
		}
		
		cats.saveCategories();
		ctx.setAttribute("categoryDAO", cats);
		
		return retVal;
	}

	
	@GET
	@Path("/getCartdAd")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> getCartAds(@Context HttpServletRequest request){
		
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		User user = (User) request.getSession().getAttribute("ulogovan");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		for (User u : users.getUsers().values()) {
			if(u.getUsername().equals(user.getUsername())){
				System.out.println("nasao usera");
				for (Oglas o : ((Kupac)u).getPoruceni()) {
					if(o.getStatus().equals("u realizaciji") && o.isAktivan())
						retVal.add(o);			
				}
				
			}
		}
		
		
		return retVal; 
	}
	
	@GET
	@Path("/getCustomerOfAd/{oglas}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Kupac getCustomerOfAd(@PathParam("oglas")String oglas){
		Kupac k = new Kupac();
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		for (User user : users.getUsers().values()) {
			if(user.getUloga().equals("kupac")){
				for (Oglas o : ((Kupac)user).getPoruceni()) {
					if(o.getNaziv().equals(oglas)){
						k = (Kupac) user;
					}	
				}
			}
		}
		
		return k;
	}
	
	@PUT
	@Path("/delivered/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> deliveredAd(@Context HttpServletRequest request, @PathParam("naziv")String naziv){
		
		System.out.println("usao u deliveredAd()");
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		
		//ads.json
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");
		Oglas ad  = ads.find(naziv);		
		ad.setStatus("dostavljen");
		ads.saveAds();
		ctx.setAttribute("adDAO", ads);
		
		
		//staviti dostavljeno u kupac.json
		User user = (User) request.getSession().getAttribute("ulogovan");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");

		User u1 = users.findUsername(user.getUsername());	

		if(u1.getUloga().equals("kupac")){
			System.out.println("usao u usera koji ima taj oglas");
			for (Oglas oglas : ((Kupac)u1).getPoruceni()) {
				if(oglas.getNaziv().equals(naziv))
				{
					System.out.println("+nasao i oglas");
					oglas.setStatus("dostavljen");
					((Kupac)u1).getDostavljeni().add(oglas);
					//((Kupac)u1).getPoruceni().remove(oglas);
				}
				else if(oglas.getStatus().equals("u realizaciji") && oglas.isAktivan())
					retVal.add(oglas);
			}
		}
		
		User u2 = users.findUsername(ad.getProdavac());
		if(u2.getUloga().equals("prodavac")){
			for (Oglas oglas : ((Prodavac)u2).getObjavljeni()) {
				if(oglas.getNaziv().equals(naziv))
				{
					oglas.setStatus("dostavljen");
					((Prodavac)u2).getIsporuceni().add(oglas);
				}
				
			}
		}
		users.saveUsers();
		ctx.setAttribute("userDAO", users);
		
		
			
		//kategorije.json
		CategoryDAO cats = (CategoryDAO) ctx.getAttribute("categoryDAO");
		for (Kategorija k : cats.getCategories().values()) {
			for (Oglas oglas : k.getOglasi()) {
				if(oglas.getNaziv().equals(naziv))
					oglas.setStatus("dostavljen");
			}
		}
		
		cats.saveCategories();
		ctx.setAttribute("categoryDAO", cats);
		
		
		return retVal;
		
	}
	@GET
	@Path("/getCustomerDeliveredAd")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> getCustomerDeliveredAd(@Context HttpServletRequest request){
	
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		User user = (User) request.getSession().getAttribute("ulogovan");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		User u = users.findUsername(user.getUsername());
		
		if(u.getUloga().equals("kupac")){
			for (Oglas oglas : ((Kupac)u).getDostavljeni()) {
				if(oglas.isAktivan() && oglas.getStatus().equals("dostavljen"))
					retVal.add(oglas);
			}
		}
		return retVal;
	}
	
	
}
