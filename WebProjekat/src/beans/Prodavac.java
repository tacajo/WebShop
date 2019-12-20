package beans;

import java.util.ArrayList;

public class Prodavac extends User{
	public ArrayList<Oglas> objavljeni;
	public ArrayList<Oglas> isporuceni;
	public ArrayList<Poruka> poruke; 
	public int lajk;
	public int dislajk;
	public ArrayList<Recenzija> recenzije;
	
	
	public Prodavac() {
		super();
		// TODO Auto-generated constructor stub
		objavljeni = new ArrayList<>();
		isporuceni = new ArrayList<>();
		poruke = new ArrayList<>();
		lajk = 0;
		dislajk = 0;
		recenzije = new ArrayList<>();
		report = 0;
	}

	public Prodavac(ArrayList<Oglas> objavljeni, ArrayList<Oglas> isporuceni, ArrayList<Poruka> poruke, int lajk,
			int dislajk) {
		super();
		this.objavljeni = objavljeni;
		this.isporuceni = isporuceni;
		this.poruke = poruke;
		this.lajk = lajk;
		this.dislajk = dislajk;
	}
	
	

	

	@Override
	public String toString() {
		return "Prodavac [objavljeni=" + objavljeni + ", isporuceni=" + isporuceni + ", poruke=" + poruke + ", lajk="
				+ lajk + ", dislajk=" + dislajk + ", username=" + username + ", password=" + password + ", ime=" + ime
				+ ", prezime=" + prezime + ", uloga=" + uloga + ", telefon=" + telefon + ", grad=" + grad + ", email="
				+ email + ", datum=" + datum + ", poslatePoruke=" + poslatePoruke + ", primljenePoruke="
				+ primljenePoruke + "]";
	}

	public ArrayList<Oglas> getObjavljeni() {
		return objavljeni;
	}

	public void setObjavljeni(ArrayList<Oglas> objavljeni) {
		this.objavljeni = objavljeni;
	}

	public ArrayList<Oglas> getIsporuceni() {
		return isporuceni;
	}

	public void setIsporuceni(ArrayList<Oglas> isporuceni) {
		this.isporuceni = isporuceni;
	}

	public ArrayList<Poruka> getPoruke() {
		return poruke;
	}

	public void setPoruke(ArrayList<Poruka> poruke) {
		this.poruke = poruke;
	}

	public int getlajk() {
		return lajk;
	}

	public void setlajk(int lajk) {
		this.lajk = lajk;
	}

	public int getDislajk() {
		return dislajk;
	}

	public void setDislajk(int dislajk) {
		this.dislajk = dislajk;
	}

	public ArrayList<Recenzija> getRecenzije() {
		return recenzije;
	}

	public void setRecenzije(ArrayList<Recenzija> recenzije) {
		this.recenzije = recenzije;
	}

	
	
}
