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

public class CategoryDAO {

	private HashMap<String, Kategorija> categories;
	private String contextPath;
	
	public CategoryDAO(String ctxPath) {
		categories = new HashMap<String, Kategorija>();
		contextPath = ctxPath;
		loadCategories();
		// TODO Auto-generated constructor stub
	}
	
	public HashMap<String, Kategorija> getCategories() {
		return categories;
	}

	public void setCategories(HashMap<String, Kategorija> categories) {
		this.categories = categories;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
	public Kategorija find(String name){
		
		for (Kategorija k : categories.values()) {
			if(k.getNaziv().equals(name)){
				return k;
			}
				
		}
		
		return null;
	}
	public void saveCategories(){
		System.out.println("usao u save categories");
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		File file = new File(this.contextPath + "/kategorije.json");
		
		
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, categories.values());
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
	
	public void loadCategories(){
		categories.clear();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		File file = new File(this.contextPath + "/kategorije.json");
		String line = "";
		String category = "";
		ArrayList<Kategorija> categories1 = new ArrayList<Kategorija>();
		try(BufferedReader br = new BufferedReader(new FileReader(file))){
			while ((line = br.readLine()) != null) {
				category += line;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			categories1 = objectMapper.readValue(category, new TypeReference<ArrayList<Kategorija>>() {});
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
		
		for (Kategorija k : categories1) {
			categories.put(k.getNaziv(), k);
		}
		
	}

}
