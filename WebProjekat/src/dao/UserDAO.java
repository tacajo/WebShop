package dao;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Administrator;
import beans.Kupac;
import beans.Prodavac;
import beans.User;

public class UserDAO {

	private HashMap<String, User> users;
	private String contextPath;
	
	public UserDAO(){
		users=new HashMap<String, User>();
		loadUsers();
		
	}
	public UserDAO(String contextPath){
		users=new HashMap<String, User>();
		this.contextPath = contextPath;
		loadUsers();
	}
	@Override
	public String toString() {
		return "UserDAO [users=" + users + "]";
	}

	public HashMap<String, User> getUsers() {
		return users;
	}

	public void setUsers(HashMap<String, User> users) {
		this.users = users;
	}
	
	public User find(String username, String password)
	{
		for(User user:users.values())
		{
			if(user.getUsername().equals(username))
			{
				if(user.getPassword().equals(password))
					return user;
				else
					return null;
			}
		}
		
		return null;
	}
	public User findUsername(String username)
	{
		for(User user:users.values())
		{
			if(user.getUsername().equals(username))
			{
				return user;
			}
		}
		
		return null;
	}
	public User registruj(Kupac kupac)
	{
		
		
		for(User user:users.values())
		{
			if(user.getUsername().equals(kupac.getUsername()))
			{
				return user;
			}
		}
		
		return null;
	}
	public void saveUsers(){
		ObjectMapper objectMapper = new ObjectMapper();
		
		ArrayList<Kupac> kupci = new ArrayList<Kupac>();
		ArrayList<Prodavac> prodavci = new ArrayList<Prodavac>();
		ArrayList<Administrator> admini = new ArrayList<Administrator>();
		
		for (User user: this.users.values()) {
			if (user.getUloga().equals("kupac")) {
				kupci.add( (Kupac)user );
			} else if(user.getUloga().equals("prodavac")){
				prodavci.add((Prodavac)user);
			} else if(user.getUloga().equals("administrator")){
				admini.add((Administrator)user);
			}
		}
		
		File file1 = new File(this.contextPath + "/kupci.json");
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(file1, kupci);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		File file2 = new File(this.contextPath + "/prodavci.json");
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(file2, prodavci);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File file3 = new File(this.contextPath + "/admini.json");
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(file3, admini);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadUsers(){
		users.clear();
		
		ObjectMapper objectMapper = new ObjectMapper();
		String line;
		
		ArrayList<Kupac> kupci = null;
		ArrayList<Prodavac> prodavci = null;
		ArrayList<Administrator> administratori = null;
		
		String kupac = "";
		String prodavac= "";
		String admin = "";
		
		File file1 = new File(this.contextPath + "/kupci.json");
		File file2 = new File(this.contextPath + "/prodavci.json");
		File file3 = new File(this.contextPath + "/admini.json");
		
		try(BufferedReader br = new BufferedReader(new FileReader(file1))){
			while ((line = br.readLine()) != null) {
				kupac += line;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
		try {
			kupci = objectMapper.readValue(kupac, new TypeReference<ArrayList<Kupac>>() {});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Kupac k: kupci) {
			this.users.put(k.getUsername(), k);
			
		}
		
		line="";
		
		try(BufferedReader br = new BufferedReader(new FileReader(file2))){
			while ((line = br.readLine()) != null) {
				prodavac += line;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
			try {
				prodavci = objectMapper.readValue(prodavac, new TypeReference<ArrayList<Prodavac>>() {});
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		for(Prodavac p: prodavci) {
			this.users.put(p.getUsername(), p);
			
		}
		
		line="";
		
		try(BufferedReader br = new BufferedReader(new FileReader(file3))){
			while ((line = br.readLine()) != null) {
				admin += line;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
			try {
				administratori = objectMapper.readValue(admin, new TypeReference<ArrayList<Administrator>>() {});
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		for(Administrator a: administratori) {
			this.users.put(a.getUsername(), a);
			
		}
	}
	
	
	
	 
}