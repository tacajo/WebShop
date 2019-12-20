package beans;

import java.util.ArrayList;

public abstract class User {
	public String username;
	public String password;
	public String ime;
	public String prezime;
	public String uloga;
	public String telefon;
	public String grad;
	public String email;
	public String datum;
	public ArrayList<Poruka> poslatePoruke;
	public ArrayList<Poruka> primljenePoruke;
	public int report;

	
	public User() {
		super();
		// TODO Auto-generated constructor stub
		poslatePoruke =  new ArrayList<Poruka>();
		primljenePoruke = new ArrayList<Poruka>();
		report = 0;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public String getUloga() {
		return uloga;
	}
	public void setUloga(String uloga) {
		this.uloga = uloga;
	}
	public String getTelefon() {
		return telefon;
	}
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	public String getGrad() {
		return grad;
	}
	public void setGrad(String grad) {
		this.grad = grad;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDatum() {
		return datum;
	}
	public void setDatum(String datum) {
		this.datum = datum;
	}
	public ArrayList<Poruka> getPoslatePoruke() {
		return poslatePoruke;
	}
	public void setPoslatePoruke(ArrayList<Poruka> poslatePoruke) {
		this.poslatePoruke = poslatePoruke;
	}
	public ArrayList<Poruka> getPrimljenePoruke() {
		return primljenePoruke;
	}
	public void setPrimljenePoruke(ArrayList<Poruka> primljenePoruke) {
		this.primljenePoruke = primljenePoruke;
	}
	public int getReport() {
		return report;
	}
	public void setReport(int report) {
		this.report = report;
	}
		
	
	
}
