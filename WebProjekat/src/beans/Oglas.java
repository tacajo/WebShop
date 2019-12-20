package beans;

import java.util.ArrayList;

import java.util.List;

public class Oglas implements Comparable<Oglas>{

	public String naziv;
	public int cena;
	public String opis;
	public int lajk;
	public int dislajk;
	public String slika;
	public long datumPostavljanja;
	public long datumIsticanja;
	public boolean aktivan;
	public ArrayList<Recenzija> recenzije;
	public String grad;
	public String kategorija;
	public String status;
	public String prodavac;
	public int wished;
	
	
	
	public Oglas(String naziv, int cena, String opis, int lajk, int dislajk, String slika, long datumPostavljanja,
			long datumIsticanja, boolean aktivan, ArrayList<Recenzija> recenzije, String grad, String kategorija) {
		super();
		this.naziv = naziv;
		this.cena = cena;
		this.opis = opis;
		this.lajk = lajk;
		this.dislajk = dislajk;
		this.slika = slika;
		this.datumPostavljanja = datumPostavljanja;
		this.datumIsticanja = datumIsticanja;
		this.aktivan = aktivan;
		this.recenzije = recenzije;
		this.grad = grad;
		this.kategorija = kategorija;
	}


	public Oglas() {
		recenzije = new ArrayList<Recenzija>();
		lajk = 0;
		dislajk = 0;
		aktivan = true;
		status = "aktivan";
		this.setDatumPostavljanja( java.lang.System.currentTimeMillis() );
	}
	
	@Override
	public String toString() {
		return "Oglas [naziv=" + naziv + ", cena=" + cena + ", opis=" + opis + ", lajk=" + lajk + ", dislajk=" + dislajk
				+ ", slika=" + slika + ", datumPostavljanja=" + datumPostavljanja + ", datumIsticanja=" + datumIsticanja
				+ ", aktivan=" + aktivan + ", recenzije=" + recenzije + ", grad=" + grad + ", kategorija=" + kategorija
				+ ", status=" + status + "]";
	}


	public String getKategorija() {
		return kategorija;
	}
	public void setKategorija(String kategorija) {
		this.kategorija = kategorija;
	}

	public String getNaziv() {
		return naziv;
	}


	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}


	public int getCena() {
		return cena;
	}


	public void setCena(int cena) {
		this.cena = cena;
	}


	public String getOpis() {
		return opis;
	}


	public void setOpis(String opis) {
		this.opis = opis;
	}


	public int getLajk() {
		return lajk;
	}


	public void setLajk(int lajk) {
		this.lajk = lajk;
	}


	public int getDislajk() {
		return dislajk;
	}


	public void setDislajk(int dislajk) {
		this.dislajk = dislajk;
	}


	public String getSlika() {
		return slika;
	}


	public void setSlika(String slika) {
		this.slika = slika;
	}


	

	public boolean isAktivan() {
		return aktivan;
	}


	public void setAktivan(boolean aktivan) {
		this.aktivan = aktivan;
	}


	public List<Recenzija> getRecenzije() {
		return recenzije;
	}


	public void setRecenzije(ArrayList<Recenzija> recenzije) {
		this.recenzije = recenzije;
	}


	public String getGrad() {
		return grad;
	}


	public void setGrad(String grad) {
		this.grad = grad;
	}
	
	public long getDatumPostavljanja() {
		return datumPostavljanja;
	}
	
	public void setDatumPostavljanja(long datumPostavljanja) {
		this.datumPostavljanja = datumPostavljanja;
	}
	
	public long getDatumIsticanja() {
		return datumIsticanja;
	}
	
	public void setDatumIsticanja(long datumIsticanja) {
		this.datumIsticanja = datumIsticanja;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getProdavac() {
		return prodavac;
	}


	public void setProdavac(String prodavac) {
		this.prodavac = prodavac;
	}


	public int getWished() {
		return wished;
	}


	public void setWished(int wished) {
		this.wished = wished;
	}
	
	@Override
	public int compareTo(Oglas o) {
		int compareWish = o.getWished();
		return compareWish - this.wished;
	}
	

}
