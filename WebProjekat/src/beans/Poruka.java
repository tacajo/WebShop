package beans;

public class Poruka {

	public String nazivOglasa;
	public String posiljalac;
	public String naslovPoruke;
	public String sadrzaj;
	public String datumVreme;
	public boolean aktivan;
	public String primalac;
	
	public Poruka() {
		aktivan = true;
	}

	
	


	@Override
	public String toString() {
		return "Poruka [nazivOglasa=" + nazivOglasa + ", posiljalac=" + posiljalac + ", naslovPoruke=" + naslovPoruke
				+ ", sadrzaj=" + sadrzaj + ", datumVreme=" + datumVreme + ", aktivan=" + aktivan + ", primalac="
				+ primalac + "]";
	}





	public Poruka(String nazivOglasa, String posiljalac, String naslovPoruke, String sadrzaj, String datumVreme) {
		super();
		this.nazivOglasa = nazivOglasa;
		this.posiljalac = posiljalac;
		this.naslovPoruke = naslovPoruke;
		this.sadrzaj = sadrzaj;
		this.datumVreme = datumVreme;
	}

	public String getPrimalac() {
		return primalac;
	}

	public void setPrimalac(String primalac) {
		this.primalac = primalac;
	}

	public String getNazivOglasa() {
		return nazivOglasa;
	}

	public void setNazivOglasa(String nazivOglasa) {
		this.nazivOglasa = nazivOglasa;
	}

	public String getPosiljalac() {
		return posiljalac;
	}


	public void setPosiljalac(String posiljalac) {
		this.posiljalac = posiljalac;
	}


	public String getNaslovPoruke() {
		return naslovPoruke;
	}

	public void setNaslovPoruke(String naslovPoruke) {
		this.naslovPoruke = naslovPoruke;
	}

	public String getSadrzaj() {
		return sadrzaj;
	}

	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}

	public String getDatumVreme() {
		return datumVreme;
	}

	public void setDatumVreme(String datumVreme) {
		this.datumVreme = datumVreme;
	}


	public boolean isAktivan() {
		return aktivan;
	}


	public void setAktivan(boolean aktivan) {
		this.aktivan = aktivan;
	}
	

}
