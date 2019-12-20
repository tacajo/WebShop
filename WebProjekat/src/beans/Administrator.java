package beans;



public class Administrator extends User{

	
	
	public Administrator() {
		// TODO Auto-generated constructor stub
		super();
		
	}

	@Override
	public String toString() {
		return "Administrator [username=" + username + ", password=" + password + ", ime=" + ime + ", prezime="
				+ prezime + ", uloga=" + uloga + ", telefon=" + telefon + ", grad=" + grad + ", email=" + email
				+ ", datum=" + datum + ", poslatePoruke=" + poslatePoruke + ", primljenePoruke=" + primljenePoruke
				+ "]";
	}


	

}
