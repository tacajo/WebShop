package beans;

import java.util.ArrayList;

public class Kupac extends User{
	public ArrayList<Oglas> poruceni;
	public ArrayList<Oglas> dostavljeni;
	public ArrayList<Oglas> omiljeni;
	public ArrayList<String> likedDislikedAd;
	public ArrayList<String> likedDislikedSeller;
	
	public Kupac() {
		super();
		this.setUloga("kupac");
		poruceni = new ArrayList<>();
		dostavljeni = new ArrayList<>();
		omiljeni = new ArrayList<>();
		likedDislikedAd = new ArrayList<String>();
		likedDislikedSeller = new ArrayList<String>();
	}

	@Override
	public String toString() {
		return "Kupac [poruceni=" + poruceni + ", dostavljeni=" + dostavljeni + ", omiljeni=" + omiljeni
				+ ", likedDislikedAd=" + likedDislikedAd + ", likedDislikedSeller=" + likedDislikedSeller
				+ ", username=" + username + ", password=" + password + ", ime=" + ime + ", prezime=" + prezime
				+ ", uloga=" + uloga + ", telefon=" + telefon + ", grad=" + grad + ", email=" + email + ", datum="
				+ datum + ", poslatePoruke=" + poslatePoruke + ", primljenePoruke=" + primljenePoruke + "]";
	}







	public ArrayList<Oglas> getPoruceni() {
		return poruceni;
	}

	public void setPoruceni(ArrayList<Oglas> poruceni) {
		this.poruceni = poruceni;
	}

	public ArrayList<Oglas> getDostavljeni() {
		return dostavljeni;
	}

	public void setDostavljeni(ArrayList<Oglas> dostavljeni) {
		this.dostavljeni = dostavljeni;
	}

	public ArrayList<Oglas> getOmiljeni() {
		return omiljeni;
	}

	public void setOmiljeni(ArrayList<Oglas> omiljeni) {
		this.omiljeni = omiljeni;
	}

	public Kupac(ArrayList<Oglas> poruceni, ArrayList<Oglas> dostavljeni, ArrayList<Oglas> omiljeni) {
		super();
		this.poruceni = poruceni;
		this.dostavljeni = dostavljeni;
		this.omiljeni = omiljeni;
	}

	public ArrayList<String> getLikedDislikedAd() {
		return likedDislikedAd;
	}

	public void setLikedDislikedAd(ArrayList<String> likedDislikedAd) {
		this.likedDislikedAd = likedDislikedAd;
	}

	public ArrayList<String> getLikedDislikedSeller() {
		return likedDislikedSeller;
	}

	public void setLikedDislikedSeller(ArrayList<String> likedDislikedSeller) {
		this.likedDislikedSeller = likedDislikedSeller;
	}
	

}
