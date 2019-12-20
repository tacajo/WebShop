package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
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

import beans.Administrator;
import beans.Kupac;
import beans.Oglas;
import beans.Prodavac;
import beans.User;
import dao.AdDAO;
import dao.UserDAO;

@Path("/admin")
public class AdminServices {

	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("adDAO") == null){
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("adDAO", new AdDAO(contextPath));
		}
	}
	
	@PUT
	@Path("/changeRole/{user}/{role}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<User> changeRole(@PathParam("user")String user, @PathParam("role")String role, @Context HttpServletRequest request){
		User ulogovan = (User) request.getSession().getAttribute("ulogovan");
		if(!ulogovan.getUloga().equals("administrator")){
			return null;
		}
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		 ArrayList<User> retVal = new  ArrayList<User>();
		System.out.println(user + "+++++" + role);
		User oldUser = users.findUsername(user);
		System.out.println("change role start");
		if(role.equals("kupac")){
			System.out.println("usao u kupca");
			Kupac newUser = new Kupac();
			newUser.setUsername(oldUser.getUsername());
			newUser.setPassword(oldUser.getPassword());
			newUser.setIme(oldUser.getIme());
			newUser.setPrezime(oldUser.getPrezime());
			newUser.setUloga("kupac");
			newUser.setGrad(oldUser.getGrad());
			newUser.setTelefon(oldUser.getTelefon());
			newUser.setEmail(oldUser.getEmail());
			newUser.setDatum(oldUser.getDatum());
			newUser.setPoslatePoruke(oldUser.getPoslatePoruke());
			newUser.setPrimljenePoruke(oldUser.getPrimljenePoruke());
			users.getUsers().remove(oldUser.getUsername());
			users.getUsers().put(newUser.getUsername(), newUser);
		}else if(role.equals("prodavac")){
			System.out.println("usao u prodavca");
			Prodavac newUser = new Prodavac();
			newUser.setUsername(oldUser.getUsername());
			newUser.setPassword(oldUser.getPassword());
			newUser.setIme(oldUser.getIme());
			newUser.setPrezime(oldUser.getPrezime());
			newUser.setUloga("prodavac");
			newUser.setGrad(oldUser.getGrad());
			newUser.setTelefon(oldUser.getTelefon());
			newUser.setEmail(oldUser.getEmail());
			newUser.setDatum(oldUser.getDatum());
			newUser.setPoslatePoruke(oldUser.getPoslatePoruke());
			newUser.setPrimljenePoruke(oldUser.getPrimljenePoruke());
			users.getUsers().remove(oldUser.getUsername());
			users.getUsers().put(newUser.getUsername(), newUser);
		}else if(role.equals("administrator")){
			System.out.println("usao u admina");
			Administrator newUser = new Administrator();
			newUser.setUsername(oldUser.getUsername());
			newUser.setPassword(oldUser.getPassword());
			newUser.setIme(oldUser.getIme());
			newUser.setPrezime(oldUser.getPrezime());
			newUser.setUloga("administrator");
			newUser.setGrad(oldUser.getGrad());
			newUser.setTelefon(oldUser.getTelefon());
			newUser.setEmail(oldUser.getEmail());
			newUser.setDatum(oldUser.getDatum());
			newUser.setPoslatePoruke(oldUser.getPoslatePoruke());
			newUser.setPrimljenePoruke(oldUser.getPrimljenePoruke());
			users.getUsers().remove(oldUser.getUsername());
			users.getUsers().put(newUser.getUsername(), newUser);
		}
		
		System.out.println("change role end");
		users.saveUsers();
		ctx.setAttribute("userDAO", users);
		for (User user2 : users.getUsers().values()) {
			retVal.add(user2);
		}
		return retVal;
	}
	@GET
	@Path("/getAllAdminAds")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Oglas> getAllAdminAds(){
		
		ArrayList<Oglas> retVal = new ArrayList<Oglas>();
		AdDAO ads = (AdDAO) ctx.getAttribute("adDAO");
		
		for (Oglas oglas : ads.getAds().values()) {
			if(oglas.isAktivan())
				retVal.add(oglas);
		}
		
		
		return retVal;
	}
	
	@PUT
	@Path("/removeBan/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<User> removeBan(@PathParam("user")String username){
		ArrayList<User> retVal = new ArrayList<User>();
		UserDAO users  = (UserDAO) ctx.getAttribute("userDAO");
		
		User u = users.findUsername(username);
		if(u != null){
			if(u.getUloga().equals("prodavac"))
				((Prodavac)u).setReport(0);
		}
		for (User user : users.getUsers().values()) {
			retVal.add(user);
		}
		users.saveUsers();
		ctx.setAttribute("userDAO", users);
		return retVal;
	}
	
	@GET
	@Path("/getAdmins")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> getAdmins(){
		ArrayList<User> retVal = new ArrayList<User>();
		
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		for (User u : users.getUsers().values()) {
			if(u.getUloga().equals("administrator"))
				retVal.add(u);
		}
		return retVal;
	}

}
