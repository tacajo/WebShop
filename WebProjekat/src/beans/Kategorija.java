package beans;

import java.util.ArrayList;

public class Kategorija {

	public String naziv;
	public String opis;
	public ArrayList<Oglas> oglasi;
	public boolean active;
	
	public Kategorija() {
		// TODO Auto-generated constructor stub
		oglasi = new ArrayList<Oglas>();
		active = true;
	}
	

	@Override
	public String toString() {
		return "Kategorija [naziv=" + naziv + ", opis=" + opis + ", oglasi=" + oglasi + "]";
	}


	public Kategorija(String naziv, String opis, ArrayList<Oglas> oglasi) {
		super();
		this.naziv = naziv;
		this.opis = opis;
		this.oglasi = oglasi;
	}


	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public ArrayList<Oglas> getOglasi() {
		return oglasi;
	}

	public void setOglasi(ArrayList<Oglas> oglasi) {
		this.oglasi = oglasi;
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	

}
