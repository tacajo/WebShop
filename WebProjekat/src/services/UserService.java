package services;

import java.util.ArrayList;

import java.util.Collections;
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

import beans.Administrator;
import beans.Kategorija;
import beans.Kupac;
import beans.Oglas;
import beans.Poruka;
import beans.Prodavac;
import beans.User;
import dao.AdDAO;
import dao.CategoryDAO;
import dao.UserDAO;


@Path("/users")
public class UserService {

	@Context
	ServletContext ctx;
	
	@PostConstruct 
	public void init() { 
		if (ctx.getAttribute("userDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("userDAO", new UserDAO(contextPath));	
		}
		if(ctx.getAttribute("adDAO") == null){
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("adDAO", new AdDAO(contextPath));
		}
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User ulogujSe(Kupac u, @Context HttpServletRequest request)
	{
		System.out.println("usao u ulogujSe()");
		UserDAO users= (UserDAO) ctx.getAttribute("userDAO");
		User user=users.find(u.getUsername(), u.getPassword());
	
		
		if(user==null)
		{
			return null;
		}
		
		ctx.setAttribute("userDAO", users);	
		request.getSession().setAttribute("ulogovan", user);
		
		return user;
	}
	
	@GET
	@Path("/getUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User userNaSesiji(@Context HttpServletRequest request)
	{
		
		User user = (User) request.getSession().getAttribute("ulogovan");
		
		return user;
	}
	
	
	
	@POST
	@Path("/registracija")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registruj(Kupac k, @Context HttpServletRequest request){
			
		System.out.println("usao u registruj()");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		User userReg = users.findUsername(k.getUsername());
		
		if(userReg != null)
			return Response.status(400).build();
		
		users.getUsers().put(k.getUsername(), k);
		ctx.setAttribute("userDAO", users);
		System.out.println("registrovan admin" + k);
		users.saveUsers();
		return Response.status(200).build();
	}
	
	@GET
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void logout(@Context HttpServletRequest request){
		
		request.getSession().invalidate();
	}
	
	
	@GET
	@Path("/getUsers")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> getUsers(@Context HttpServletRequest request)
	{
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		ArrayList<User> retVal = new ArrayList<User>();
		
		for (User u : users.getUsers().values()) {
				retVal.add(u);
		}
		return retVal;
	}
	
	
	
	
	@GET
	@Path("/adsSession")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> adsSession(@Context HttpServletRequest request)
	{
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		User user = (User) request.getSession().getAttribute("ulogovan");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		User u = users.findUsername(user.getUsername());
	
		if(user != null){
			if(user.getUloga().equals("prodavac")){
				for (Oglas oglas : ((Prodavac)u).getObjavljeni()) {
					if(oglas.isAktivan())
						retVal.add(oglas);
				}
			}
		}
		
		
		return retVal;
		
	}
	
	@POST
	@Path("/sendMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int posaljiPoruku(Poruka poruka){

		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		int retVal = 0;
		User u1 = users.findUsername(poruka.getPrimalac());
		if(u1 != null){
			u1.getPrimljenePoruke().add(poruka);
			retVal = 1;
			
		}
		
		if(poruka.getPosiljalac().equals("automatic message"))
			retVal = 3;
		else {
			User u2 = users.findUsername(poruka.getPosiljalac());
			if(u2 != null){
				u2.getPoslatePoruke().add(poruka);
				retVal = 2;
			}
		}
		
		
		ctx.setAttribute("userDAO", users);
		users.saveUsers();
		return retVal;
	}
	
	
	@GET
	@Path("/getSentMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Poruka> getPoslatePoruke(@Context HttpServletRequest request){
		ArrayList<Poruka> retVal = new ArrayList<Poruka>();
		
		User user = (User) request.getSession().getAttribute("ulogovan");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		User u = users.findUsername(user.getUsername());
		if(user != null){
			for (Poruka poruka : u.getPoslatePoruke()) {
				if(poruka.isAktivan()){
					retVal.add(poruka);
				}
			}
		}
		return retVal;
		
	}
	
	@GET
	@Path("/getReceivedMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Poruka> getPrimljenePoruke(@Context HttpServletRequest request){
		ArrayList<Poruka> retVal = new ArrayList<Poruka>();
		
		User user = (User) request.getSession().getAttribute("ulogovan");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		User u = users.findUsername(user.getUsername());
		if(user != null){
			for (Poruka poruka : u.getPrimljenePoruke()) {
				if(poruka.isAktivan()){
					retVal.add(poruka);
				}
			}
		}
		return retVal;
		
	}
	
	@PUT
	@Path("/deleteSentMessage/{naziv}/{sadrzaj}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Poruka> deleteSentMessage(@PathParam("naziv")String naziv,@PathParam("sadrzaj")String sadrzaj,
			@Context HttpServletRequest request){
		
		ArrayList<Poruka> retVal = new ArrayList<Poruka>();

		User user = (User) request.getSession().getAttribute("ulogovan");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		User u = users.findUsername(user.getUsername());
		
		if(user!= null){
			for (Poruka poruka : u.getPoslatePoruke()) {
				if(poruka.getNaslovPoruke().equals(naziv) && poruka.getSadrzaj().equals(sadrzaj))
					poruka.setAktivan(false);
				else if(poruka.isAktivan()) 
					retVal.add(poruka);
			}
		}
		users.saveUsers();
		ctx.setAttribute("userDAO", users);
		return retVal;
	}
	
	@PUT
	@Path("/deleteReceivedMessage/{naziv}/{sadrzaj}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Poruka> deleteReceivedMessage(@PathParam("naziv")String naziv,@PathParam("sadrzaj")String sadrzaj,
			@Context HttpServletRequest request){
		ArrayList<Poruka> retVal = new ArrayList<Poruka>();
		
		User user = (User) request.getSession().getAttribute("ulogovan");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		User u = users.findUsername(user.getUsername());
		
		for (Poruka poruka : u.getPrimljenePoruke()) {
			if(poruka.getNaslovPoruke().equals(naziv) && poruka.getSadrzaj().equals(sadrzaj)){
				poruka.setAktivan(false);
			}	
			else if(poruka.isAktivan())
				retVal.add(poruka);
		}
		
		users.saveUsers();
		ctx.setAttribute("userDAO", users);
		
		return retVal;
	}
	
	@PUT
	@Path("editMessage/{naslov}/{sadrzaj}/{noviNaslov}/{noviSadrzaj}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Poruka> editMessage(@PathParam("naslov")String naslov, @PathParam("sadrzaj")String sadrzaj,
			@PathParam("noviNaslov")String noviNaslov, @PathParam("noviSadrzaj")String noviSadrzaj, @Context HttpServletRequest request){
		
		User user = (User) request.getSession().getAttribute("ulogovan");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		ArrayList<Poruka> retVal = new ArrayList<Poruka>();
		
		User posiljalac = users.findUsername(user.getUsername());
		String usernamePrimalac = null;
		System.out.println("usao u editMessage()");
		for (Poruka poruka : posiljalac.getPoslatePoruke()) {
			if(poruka.getNaslovPoruke().equals(naslov) && poruka.getSadrzaj().equals(sadrzaj)){
				poruka.setNaslovPoruke(noviNaslov);
				poruka.setSadrzaj(noviSadrzaj);
				retVal.add(poruka);
				usernamePrimalac = poruka.getPrimalac();
			}else if(poruka.isAktivan())
				retVal.add(poruka);
			
		}
		
		User primalac = users.findUsername(usernamePrimalac);
		
		for (Poruka poruka : primalac.getPrimljenePoruke()) {
			if(poruka.getNaslovPoruke().equals(naslov) && poruka.getSadrzaj().equals(sadrzaj)){
				poruka.setNaslovPoruke(noviNaslov);
				poruka.setSadrzaj(noviSadrzaj);
			}
		}
		
		users.saveUsers();
		ctx.setAttribute("userDAO", users);
		return retVal;
	}
	
	
	
	
	
	@GET
	@Path("/getAdsUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<String> getAdsUser(){
		ArrayList<String> retVal = new ArrayList<String>();
		
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		for (User u : users.getUsers().values()) {
			if(!retVal.contains(u.getGrad()))
					retVal.add(u.getGrad());
		}
		return retVal;
	}
	
	@GET
	@Path("/getSearchUsers/{ime}/{grad}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<User> getSearchUsers(@PathParam("ime")String ime, @PathParam("grad")String grad,
			@Context HttpServletRequest request){
		ArrayList<User> retVal = new ArrayList<User>();
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		User u = (User) request.getSession().getAttribute("ulogovan");
		
		
		if(!ime.equals("---") && !grad.equals("Choose...") ){
			for (User user : users.getUsers().values()) {
				if(user.getIme().equals(ime) && user.getGrad().equals(grad) && !user.getUsername().equals(u.getUsername()))
					retVal.add(user);
			}
		}else if(!ime.equals("---")  && grad.equals("Choose...") ){
			for (User user : users.getUsers().values()) {
				if(user.getIme().equals(ime) && !user.getUsername().equals(u.getUsername()))
					retVal.add(user);
			}
		}
		else if(!grad.equals("Choose...")  && ime.equals("---") ){
			for (User user : users.getUsers().values()) {
				if(user.getGrad().equals(grad) && !user.getUsername().equals(u.getUsername()))
					retVal.add(user);
			}
		}
		
		return retVal;
	}

	@GET
	@Path("/search/{ime}/{minPrice}/{maxPrice}/{grad}/{minDate}/{maxDate}/{status}/{minLike}/{maxLike}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> searchAd(@PathParam("ime")String ime, @PathParam("minPrice")String minPrice,
			@PathParam("maxPrice")String maxPrice, @PathParam("grad")String grad, @PathParam("minDate")String minDate, 
			@PathParam("maxDate")String maxDate, @PathParam("status")String status,
			@PathParam("minLike")String minLike, @PathParam("maxLike")String maxLike){
		
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");
		for (Oglas ad : ads.getAds().values()) {
			retVal.add(ad);
		}
		
		ArrayList<Oglas> temp = new ArrayList<Oglas>();
		if(!status.equals("Choose...")){
			
			if(status.equals("active")){
				for (Oglas oglas : retVal) {
					if(oglas.getStatus().equals("aktivan") && oglas.isAktivan())
						temp.add(oglas);
				}
			}else if(status.equals("in realization")){
				for (Oglas oglas : retVal) {
					if(oglas.getStatus().equals("u realizaciji") && oglas.isAktivan())
						temp.add(oglas);
				}
			}else if(status.equals("delivered")){
				for (Oglas oglas : retVal) {
					if(oglas.getStatus().equals("dostavljen") && oglas.isAktivan())
						temp.add(oglas);
				}
			}
			retVal.clear();
			for (Oglas oglas : temp) {
				retVal.add(oglas);
			}
		}
		
		if(!ime.equals("null")){
			String ime1 = ime.toUpperCase();
			temp.clear();
			for (Oglas oglas : retVal) {
				if(oglas.getNaziv().contains(ime1) && oglas.isAktivan())
					temp.add(oglas);
				
			}
			retVal.clear();
			for (Oglas oglas : temp) {
				retVal.add(oglas);
			}
		
		}
		
		if(!grad.equals("Choose...")){
			temp.clear();
			for (Oglas oglas : retVal) {
				if(oglas.getGrad().equals(grad) && oglas.isAktivan())
					temp.add(oglas);
			}
			retVal.clear();
			for (Oglas oglas : temp) {
				retVal.add(oglas);
			}
		}
		if(!minPrice.equals("null")){
			temp.clear();
			int price = Integer.parseInt(minPrice);
			for (Oglas oglas : retVal) {
				if(oglas.getCena() >= price && oglas.isAktivan())
					temp.add(oglas);
			}
			retVal.clear();
			for (Oglas oglas : temp) {
				retVal.add(oglas);
			}
		}
		if(!maxPrice.equals("null")){
			temp.clear();
			int price = Integer.parseInt(maxPrice);
			for (Oglas oglas : retVal) {
				if(oglas.getCena() <= price && oglas.isAktivan())
					temp.add(oglas);
			}
			retVal.clear();
			for (Oglas oglas : temp) {
				retVal.add(oglas);
			}
		}
		if(!minDate.equals("null") && !minDate.equals("NaN")){
			temp.clear();
			System.out.println("usao u min date");
			long date = Long.parseLong(minDate);
			for (Oglas oglas : retVal) {
				if(oglas.getDatumIsticanja() > date && oglas.isAktivan())
					temp.add(oglas);
			}
			retVal.clear();
			for (Oglas oglas : temp) {
				retVal.add(oglas);
			}
		}
		if(!maxDate.equals("null") &&  !maxDate.equals("NaN")){
			temp.clear();
			System.out.println("usao u max date");
			long date = Long.parseLong(maxDate);
			for (Oglas oglas : retVal) {
				if(oglas.getDatumIsticanja() < date && oglas.isAktivan())
					temp.add(oglas);
			}
			retVal.clear();
			for (Oglas oglas : temp) {
				retVal.add(oglas);
			}
		}
		if(!minLike.equals("null")){
			temp.clear();
			int likes = Integer.parseInt(minLike);
			for (Oglas oglas : retVal) {
				if(oglas.getLajk() >= likes && oglas.isAktivan())
					temp.add(oglas);
			}
			retVal.clear();
			for (Oglas oglas : temp) {
				retVal.add(oglas);
			}
		}
		if(!maxLike.equals("null")){
			temp.clear();
			int likes = Integer.parseInt(maxLike);
			for (Oglas oglas : retVal) {
				if(oglas.getLajk() <= likes && oglas.isAktivan())
					temp.add(oglas);
			}
			retVal.clear();
			for (Oglas oglas : temp) {
				retVal.add(oglas);
			}
		}
		System.out.println("usao u search");
		return retVal;
	}
	
	@GET
	@Path("/getPopular")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> popularAds(){
		ArrayList<Oglas> sortAds = new ArrayList<Oglas>();
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");
		ArrayList<Oglas> allAds = new ArrayList<Oglas>();
		for (Oglas oglas : ads.getAds().values()) {
			allAds.add(oglas);
		}
		
		Collections.sort(allAds);
		int i = 0;
		for (Oglas oglas : allAds) {
			if(oglas.getStatus().equals("aktivan") && oglas.isAktivan()){
				if(i < 10){
					sortAds.add(oglas);
					i++;
				}
			}
		}
		
		return sortAds;
	}
	
}
