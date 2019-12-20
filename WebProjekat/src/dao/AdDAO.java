package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Kategorija;
import beans.Oglas;

public class AdDAO {

	public HashMap<String, Oglas> ads;
	public String contextPath;
	
	public AdDAO(String ctxPath) {
		// TODO Auto-generated constructor stub
		ads = new HashMap<String, Oglas>();
		contextPath = ctxPath;
		loadAds();
	}

	public HashMap<String, Oglas> getAds() {
		return ads;
	}

	public void setAds(HashMap<String, Oglas> ads) {
		this.ads = ads;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
	public Oglas find(String name){
		for(Oglas o : ads.values())
		{
			if(o.getNaziv().equals(name))
				return o;
		}
		return null;
	}
	
	public void saveAds(){
		System.out.println("usao u save ads");
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		File file = new File(this.contextPath + "/oglasi.json");
		
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, ads.values());
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void loadAds(){
		ads.clear();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		File file = new File(this.contextPath + "/oglasi.json");
		String line = "";
		String ad = "";
		ArrayList<Oglas> ads1 = new ArrayList<Oglas>();
		try(BufferedReader br = new BufferedReader(new FileReader(file))){
			while ((line = br.readLine()) != null) {
				ad += line;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			ads1 = objectMapper.readValue(ad, new TypeReference<ArrayList<Oglas>>() {});
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		for(Oglas o : ads1){
			ads.put(o.getNaziv(), o);
		}
	}
	


}
