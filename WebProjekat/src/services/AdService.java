package services;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataParam;

import beans.Kategorija;
import beans.Kupac;
import beans.Oglas;
import beans.Prodavac;
import beans.Recenzija;
import beans.User;
import dao.AdDAO;
import dao.CategoryDAO;
import dao.UserDAO;


@Path("/ad")
public class AdService {

	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("adDAO") == null){
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("adDAO", new AdDAO(contextPath));
		}
	}

	@POST
	@Path("/newAd")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public int newAd(Oglas oglas,  @Context HttpServletRequest request){
		System.out.println("usao u newAd");
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");
		
		Oglas ad = ads.find(oglas.getNaziv());
		
		if(ad != null)
			return 0;
		
		ads.getAds().put(oglas.getNaziv(), oglas);
		ctx.setAttribute("adDAO", ads);
		ads.saveAds();
		
		CategoryDAO categories = (CategoryDAO) ctx.getAttribute("categoryDAO");
		for (Kategorija k : categories.getCategories().values()) {
			if(k.getNaziv().equals(oglas.getKategorija()))
				k.getOglasi().add(oglas);
		}
		ctx.setAttribute("categoryDAO", categories);
		categories.saveCategories();
		
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		User user = (User) request.getSession().getAttribute("ulogovan");
		
		
		for (User u : users.getUsers().values()) {
			if(u.getUsername().equals(user.getUsername())){
				System.out.println("pronasao prodavca koji dodaje oglas");
				((Prodavac)u).getObjavljeni().add(oglas);
			}
			
		}
		ctx.setAttribute("userDAO", users);
		users.saveUsers();
		return 1;
		
	}
	
	@POST
	@Path("/uploadImage")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadImage(@FormDataParam("fileToUpload") InputStream uploadedInputStream,
			@FormDataParam("name") String name) {
		String fileLocation = ctx.getRealPath("/images/" + name);

		try {
			OutputStream out = new FileOutputStream(new File(
					fileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(fileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return Response.status(200).build();
	}
	
	@GET
	@Path("/getAds")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> oglasi(){
		
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");

		//CategoryDAO categories = (CategoryDAO) ctx.getAttribute("categoryDAO");
		
		for (Oglas oglas : ads.getAds().values()) {
			if(oglas.getStatus().equals("aktivan") &&  oglas.isAktivan())
			{
				retVal.add(oglas);		
			}
			
		}
		
		
		/*
		for (Kategorija k : categories.getCategories().values()) {
			if(k.isActive()){
				for (Oglas oglas : k.getOglasi()) {
					if(!oglas.getStatus().equals("u realizaciji") || !oglas.getStatus().equals("dostavljen") || oglas.isAktivan())
						retVal.add(oglas);
				}
			}
		}*/
		
		System.out.println("usao u getAds()");
		return retVal;
	}
	
	
	
	@GET
	@Path("/getAdsByCategory/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> oglasiPoKategoriji(@PathParam("naziv")String naziv){
		
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		
		
		CategoryDAO categories = (CategoryDAO) ctx.getAttribute("categoryDAO");
		for (Kategorija k : categories.getCategories().values()) {
			if(k.getNaziv().equals(naziv)){
				for (Oglas oglas : k.getOglasi()) {
					if(oglas.isAktivan())
						retVal.add(oglas);
				}
			}
		}
		
		System.out.println("usao u oglasiPoKategoriji()");
		return retVal;
	}
	
	@POST
	@Path("/editAd/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> editAd(@PathParam("naziv")String naziv, Oglas oglas, @Context HttpServletRequest request){
		
		System.out.println("usao u editAd");
		System.out.println(naziv + "/" + oglas.getNaziv());
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		//User user = (User) request.getSession().getAttribute("ulogovan");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		String prodavac = "";
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");
		//promena u oglas.json
		for (Oglas oglas2 : ads.getAds().values()) {
			if(oglas2.getNaziv().equals(naziv)){
				prodavac = oglas2.getProdavac();
				oglas2.setNaziv(oglas.getNaziv());
				oglas2.setCena(oglas.getCena());
				oglas2.setOpis(oglas.getOpis());
				oglas2.setDatumIsticanja(oglas.getDatumIsticanja());
				oglas2.setGrad(oglas.getGrad());
				oglas2.setKategorija(oglas.getKategorija());
				oglas2.setSlika(oglas.getSlika());
			}
			
		}		
		ctx.setAttribute("adDAO", ads);
		ads.saveAds();
		
		//promena u prodavac.json
		User u = users.findUsername(prodavac);
		if(u != null){
		if(u.getUloga().equals("prodavac")){
			for (Oglas oglas1 : ((Prodavac)u).getObjavljeni()) {
				if(oglas1.getNaziv().equals(naziv))
				{
					oglas1.setNaziv(oglas.getNaziv());
					oglas1.setCena(oglas.getCena());
					oglas1.setOpis(oglas.getOpis());
					oglas1.setDatumIsticanja(oglas.getDatumIsticanja());
					oglas1.setGrad(oglas.getGrad());
					oglas1.setKategorija(oglas.getKategorija());
					oglas1.setSlika(oglas.getSlika());
				}
				if(oglas1.isAktivan() && oglas1.getStatus().equals("aktivan"))
					retVal.add(oglas1);
			}
			for (Oglas oglas2 : ((Prodavac)u).getIsporuceni()) {
				if(oglas2.getNaziv().equals(naziv))
				{
					oglas2.setNaziv(oglas.getNaziv());
					oglas2.setCena(oglas.getCena());
					oglas2.setOpis(oglas.getOpis());
					oglas2.setDatumIsticanja(oglas.getDatumIsticanja());
					oglas2.setGrad(oglas.getGrad());
					oglas2.setKategorija(oglas.getKategorija());
					oglas2.setSlika(oglas.getSlika());
				}
				
			}
		}
		}
		//promena u kupci.json
		for (User u1 : users.getUsers().values()) {
			if(u1.getUloga().equals("kupac")){
				for (Oglas oglas1 : ((Kupac)u1).getOmiljeni()) {
					if(oglas1.getNaziv().equals(naziv))
					{
						oglas1.setNaziv(oglas.getNaziv());
						oglas1.setCena(oglas.getCena());
						oglas1.setOpis(oglas.getOpis());
						oglas1.setDatumIsticanja(oglas.getDatumIsticanja());
						oglas1.setGrad(oglas.getGrad());
						oglas1.setKategorija(oglas.getKategorija());
						oglas1.setSlika(oglas.getSlika());
					}
				}
				for (Oglas oglas1 : ((Kupac)u1).getPoruceni()) {
					if(oglas1.getNaziv().equals(naziv))
					{
						oglas1.setNaziv(oglas.getNaziv());
						oglas1.setCena(oglas.getCena());
						oglas1.setOpis(oglas.getOpis());
						oglas1.setDatumIsticanja(oglas.getDatumIsticanja());
						oglas1.setGrad(oglas.getGrad());
						oglas1.setKategorija(oglas.getKategorija());
						oglas1.setSlika(oglas.getSlika());
					}
				}
				for (Oglas oglas1 : ((Kupac)u1).getDostavljeni()) {
					if(oglas1.getNaziv().equals(naziv))
					{
						oglas1.setNaziv(oglas.getNaziv());
						oglas1.setCena(oglas.getCena());
						oglas1.setOpis(oglas.getOpis());
						oglas1.setDatumIsticanja(oglas.getDatumIsticanja());
						oglas1.setGrad(oglas.getGrad());
						oglas1.setKategorija(oglas.getKategorija());
						oglas1.setSlika(oglas.getSlika());
					}
				}
			}
		}
				
		
		ctx.setAttribute("userDAO", users);
		users.saveUsers();
			
		//promena u kategorije.json
		CategoryDAO cats = (CategoryDAO) ctx.getAttribute("categoryDAO");
		
		for (Kategorija kategorija : cats.getCategories().values()) {
			if(kategorija.getNaziv().equals(oglas.getKategorija()))
			{		
				System.out.println("nasao kategoriju");
				for (Oglas oglas2 : kategorija.getOglasi()) {
					if(oglas2.getNaziv().equals(naziv)){
						oglas2.setNaziv(oglas.getNaziv());
						oglas2.setCena(oglas.getCena());
						oglas2.setOpis(oglas.getOpis());
						oglas2.setDatumIsticanja(oglas.getDatumIsticanja());
						oglas2.setGrad(oglas.getGrad());
						oglas2.setKategorija(oglas.getKategorija());
					}
				}
			}
		}
		
		cats.saveCategories();
		ctx.setAttribute("categoryDAO", cats);
		return retVal;
		
	}
	
	
	
	
	
	
	
	@PUT
	@Path("/deleteAd/{naziv}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> deleteAd(@PathParam("naziv")String naziv, @Context HttpServletRequest request){
		
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		User ulogovan = (User) request.getSession().getAttribute("ulogovan");
		User user = users.findUsername(ulogovan.getUsername());
		
		//promena u oglasi.json
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");
		String prodavacUsername = "";
		for (Oglas oglas : ads.getAds().values()) {
			if(oglas.getNaziv().equals(naziv))
			{	
				if(user.getUloga().equals("prodavac")){
					if(oglas.getStatus().equals("u realizaciji") || oglas.getStatus().equals("dostavljen")){
						return null;
					}else{
						oglas.setAktivan(false);			
					}
				}
				if(user.getUloga().equals("administrator"))
					oglas.setAktivan(false);
					
				prodavacUsername = oglas.getProdavac();
			}
		}
		
		ctx.setAttribute("adDAO", ads);
		ads.saveAds();
		
		//promena i prodavci.json
		
		User prodavac = users.findUsername(prodavacUsername);
		
		if(prodavac.getUloga().equals("prodavac")){	
			for (Oglas o :( (Prodavac)prodavac).getObjavljeni()) {
				if(o.getNaziv().equals(naziv)){
					o.setAktivan(false);
				}
				if(o.isAktivan() && o.getStatus().equals("aktivan"))
					retVal.add(o);
				
			}for (Oglas o :( (Prodavac)prodavac).getIsporuceni()) {
				if(o.getNaziv().equals(naziv)){
					o.setAktivan(false);
				}
			}
		}	
		
		//promena u kupci.json
		for (User u : users.getUsers().values()) {
			if(u.getUloga().equals("kupac")){
				for (Oglas oglas : ((Kupac)u).getOmiljeni()) {
					if(oglas.getNaziv().equals(naziv))
						oglas.setAktivan(false);
				}
				for (Oglas oglas : ((Kupac)u).getPoruceni()) {
					if(oglas.getNaziv().equals(naziv))
						oglas.setAktivan(false);
				}
				for (Oglas oglas : ((Kupac)u).getDostavljeni()) {
					if(oglas.getNaziv().equals(naziv))
						oglas.setAktivan(false);
				}
			}
		}
		
		ctx.setAttribute("userDAO", users);
		users.saveUsers();
		
		CategoryDAO cats = (CategoryDAO) ctx.getAttribute("categoryDAO");
		for (Kategorija k : cats.getCategories().values()) {
			for (Oglas oglas : k.getOglasi()) {
				if(oglas.getNaziv().equals(naziv))
					oglas.setAktivan(false);
			}
		}
		
		cats.saveCategories();
		ctx.setAttribute("categoryDAO", cats);
		
		return retVal;
	}
	
	@PUT
	@Path("/likeAd/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> likeAd(@PathParam("naziv")String naziv, @Context HttpServletRequest request){
		
		User user = (User) request.getSession().getAttribute("ulogovan");
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		
		Oglas ad = null;
	
		//prodavci.json i kupci.json
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		User ulogovan = users.findUsername(user.getUsername());
		if(ulogovan != null){
			if(ulogovan.getUloga().equals("kupac"))
			{
				for (String oglas : ((Kupac)ulogovan).getLikedDislikedAd()) {
					if(oglas.equals(naziv))
						return null;
				}
				((Kupac)ulogovan).getLikedDislikedAd().add(naziv);
				request.getSession().setAttribute("ulogovan", ulogovan);
			}
		}
		//oglasi.json
		int like = 0;
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");
		for (Oglas o : ads.getAds().values()) {
			if(o.getNaziv().equals(naziv))
			{	
				like = o.getLajk() + 1;
				o.setLajk(like);
				ad = o;
			}
		}
		ads.saveAds();
		ctx.setAttribute("adDAO", ads);
		
		//prodavci
		User prodavac = users.findUsername(ad.getProdavac());		
		if(prodavac.getUloga().equals("prodavac")){
			for (Oglas o : ((Prodavac)prodavac).getObjavljeni()) {
				if(o.getNaziv().equals(naziv)){
					o.setLajk(like);
				}

			}
			for (Oglas o : ((Prodavac)prodavac).getIsporuceni()) {
				if(o.getNaziv().equals(naziv))
					o.setLajk(like);
			}
		}
		
		for (User u : users.getUsers().values()) {
			
			//kupci
			if(u.getUloga().equals("kupac")){
				for (Oglas o : ((Kupac)u).getPoruceni()) {
					if(o.getNaziv().equals(naziv))
						o.setLajk(like);
				}
				for (Oglas o : ((Kupac)u).getDostavljeni()) {
					if(o.getNaziv().equals(naziv))
						o.setLajk(like);
				}
				for (Oglas o : ((Kupac)u).getOmiljeni()) {
					if(o.getNaziv().equals(naziv))
						o.setLajk(like);
				}
			}
		}
		
		if(ulogovan != null){
			if(ulogovan.getUloga().equals("kupac")){
				for (Oglas oglas : ((Kupac)ulogovan).getDostavljeni()) {
					if(oglas.isAktivan())
						retVal.add(oglas);
				}
			}
		}
		users.saveUsers();
		ctx.setAttribute("userDAO", users);

		
		
		//kategorije.json
		CategoryDAO cats = (CategoryDAO) ctx.getAttribute("categoryDAO");
		Kategorija k = cats.find(ad.getKategorija());		
		for (Oglas o : k.getOglasi()) {
			if(o.getNaziv().equals(naziv))
				o.setLajk(like);
		}
		cats.saveCategories();
		ctx.setAttribute("categoryDAO", cats);
	
		
		return retVal;
	}
	
	@PUT
	@Path("/dislikeAd/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> dislikeAd(@PathParam("naziv")String naziv, @Context HttpServletRequest request){
		
		User user = (User) request.getSession().getAttribute("ulogovan");
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		
		Oglas ad = null;
	
		//prodavci.json i kupci.json
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		User ulogovan = users.findUsername(user.getUsername());
		if(ulogovan != null){
		if(ulogovan.getUloga().equals("kupac"))
		{
			for (String oglas : ((Kupac)ulogovan).getLikedDislikedAd()) {
				if(oglas.equals(naziv))
					return null;
			}
			((Kupac)ulogovan).getLikedDislikedAd().add(naziv);
			request.getSession().setAttribute("ulogovan", ulogovan);
		}
		}
		//oglasi.json
		int dislike = 0;
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");
		for (Oglas o : ads.getAds().values()) {
			if(o.getNaziv().equals(naziv))
			{	
				dislike = o.getDislajk() + 1;
				o.setDislajk(dislike);
				ad = o;
			}
		}
		ads.saveAds();
		ctx.setAttribute("adDAO", ads);
		
		//prodavci
		User prodavac = users.findUsername(ad.getProdavac());		
		if(prodavac.getUloga().equals("prodavac")){
			for (Oglas o : ((Prodavac)prodavac).getObjavljeni()) {
				if(o.getNaziv().equals(naziv)){
					o.setDislajk(dislike);
				}

			}
			for (Oglas o : ((Prodavac)prodavac).getIsporuceni()) {
				if(o.getNaziv().equals(naziv))
					o.setDislajk(dislike);
			}
		}
		
		for (User u : users.getUsers().values()) {
			
			//kupci
			if(u.getUloga().equals("kupac")){
				for (Oglas o : ((Kupac)u).getPoruceni()) {
					if(o.getNaziv().equals(naziv))
						o.setDislajk(dislike);
				}
				for (Oglas o : ((Kupac)u).getDostavljeni()) {
					if(o.getNaziv().equals(naziv))
						o.setDislajk(dislike);
					
				}
				for (Oglas o : ((Kupac)u).getOmiljeni()) {
					if(o.getNaziv().equals(naziv))
						o.setDislajk(dislike);
				}
			}
		}
		
		if(ulogovan != null){
			if(ulogovan.getUloga().equals("kupac")){
				for (Oglas oglas : ((Kupac)ulogovan).getDostavljeni()) {
					if(oglas.isAktivan())
						retVal.add(oglas);
				}
			}
		}
		
		users.saveUsers();
		ctx.setAttribute("userDAO", users);

		
		
		//kategorije.json
		CategoryDAO cats = (CategoryDAO) ctx.getAttribute("categoryDAO");
		Kategorija k = cats.find(ad.getKategorija());		
		for (Oglas o : k.getOglasi()) {
			if(o.getNaziv().equals(naziv))
				o.setDislajk(dislike);
		}
		cats.saveCategories();
		ctx.setAttribute("categoryDAO", cats);
	
		
		return retVal;
	}
	
	
	@POST
	@Path("/ReviewAd")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> reviewAd(Recenzija recenzija, @Context HttpServletRequest request){
		
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		System.out.println("usao u addRview");
		System.out.println(recenzija.getNaslov());
		//kupci
		
		String prodavac = "";
		//prodavci.json i kupci.json
		//oglasi.json
		String kategorija = "";
		ArrayList<Recenzija> listaR = new ArrayList<Recenzija>();
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");
		for (Oglas o2 : ads.getAds().values()) {
			if(o2.getNaziv().equals(recenzija.getOglas()))
			{
				listaR.add(recenzija);
				for (Recenzija recenzija2 : o2.getRecenzije()) {
					listaR.add(recenzija2);
				}
				o2.setRecenzije(listaR);
				kategorija = o2.getKategorija();
				prodavac = o2.getProdavac();
			}
		}
		ads.saveAds();
		ctx.setAttribute("adDAO", ads);
		
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		User ulogovan = (User) request.getSession().getAttribute("ulogovan");
		if(ulogovan != null){
			if(ulogovan.getUloga().equals("kupac")){
				for (Oglas oglas : ((Kupac)ulogovan).getDostavljeni()) {
					if(oglas.getNaziv().equals(recenzija.getOglas())){
						oglas.setRecenzije(listaR);
						System.out.println("dodata recenzija u kupac dostavljeni " + recenzija.getNaslov());
					}
					if(oglas.isAktivan())
						retVal.add(oglas);
				}
			}
		}
	
		
		
		//kategorije.json
		CategoryDAO cats = (CategoryDAO) ctx.getAttribute("categoryDAO");
		Kategorija k = cats.find(kategorija);		
		for (Oglas o3 : k.getOglasi()) {
			if(o3.getNaziv().equals(recenzija.getOglas()))
				o3.setRecenzije(listaR);
		}
		cats.saveCategories();
		ctx.setAttribute("categoryDAO", cats);
		
		
		User p = users.findUsername(prodavac);
		
		if(p != null){
			if(p.getUloga().equals("prodavac")){
				for (Oglas o1 : ((Prodavac)p).getObjavljeni()) {
					if(o1.getNaziv().equals(recenzija.getOglas())){					
						o1.setRecenzije(listaR);
						System.out.println("prodavac objavljeni: + + + +"+ o1.getNaziv() + " *** " + recenzija.getOglas());
					}
				}
				for (Oglas o4 : ((Prodavac)p).getIsporuceni()) {
					if(o4.getNaziv().equals(recenzija.getOglas())){
						o4.setRecenzije(listaR);
					}
				}
			}
		}
		users.saveUsers();
		ctx.setAttribute("userDAO", users);
		return retVal;
	}
	
	@GET
	@Path("/getCustomerofAd/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Kupac getCustomerofAd(@PathParam("naziv")String naziv){
		Kupac k = null;
		System.out.println("usao u getCustomerofAd");
		
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");
		
		Oglas ad = ads.find(naziv);
		if(ad == null)
			return null;
		
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		for (User u : users.getUsers().values()) {
			if(u.getUloga().equals("kupac")){
				for (Oglas o : ((Kupac)u).getPoruceni()) {
					if(o.getNaziv().equals(naziv))
						k = (Kupac) u;
				}
			}
		}
		
		return k;
	}
	
}
