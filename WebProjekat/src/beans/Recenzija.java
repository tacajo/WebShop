package beans;

public class Recenzija {

	public String oglas;
	public String recenzent;
	public String naslov;
	public String sadrzaj;
	public String slika;
	public boolean oglasTacan;
	public boolean dogovor;
	public boolean aktivan;
	
	
	public Recenzija() {
		aktivan = true;
	}
	

	public Recenzija(String oglas, String recenzent, String naslov, String sadrzaj, String slika, boolean oglasTacan,
			boolean dogovor) {
		super();
		this.oglas = oglas;
		this.recenzent = recenzent;
		this.naslov = naslov;
		this.sadrzaj = sadrzaj;
		this.slika = slika;
		this.oglasTacan = oglasTacan;
		this.dogovor = dogovor;
	}


	@Override
	public String toString() {
		return "Recenzija [oglas=" + oglas + ", recenzent=" + recenzent + ", naslov=" + naslov + ", sadrzaj=" + sadrzaj
				+ ", slika=" + slika + ", oglasTacan=" + oglasTacan + ", dogovor=" + dogovor + "]";
	}


	public String getOglas() {
		return oglas;
	}


	public void setOglas(String oglas) {
		this.oglas = oglas;
	}


	public String getRecenzent() {
		return recenzent;
	}


	public void setRecenzent(String recenzent) {
		this.recenzent = recenzent;
	}


	public String getNaslov() {
		return naslov;
	}


	public void setNaslov(String naslov) {
		this.naslov = naslov;
	}


	public String getSadrzaj() {
		return sadrzaj;
	}


	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}


	public String getSlika() {
		return slika;
	}


	public void setSlika(String slika) {
		this.slika = slika;
	}


	public boolean isOglasTacan() {
		return oglasTacan;
	}


	public void setOglasTacan(boolean oglasTacan) {
		this.oglasTacan = oglasTacan;
	}


	public boolean isDogovor() {
		return dogovor;
	}


	public void setDogovor(boolean dogovor) {
		this.dogovor = dogovor;
	}


	public boolean isAktivan() {
		return aktivan;
	}


	public void setAktivan(boolean aktivan) {
		this.aktivan = aktivan;
	}
	

}
