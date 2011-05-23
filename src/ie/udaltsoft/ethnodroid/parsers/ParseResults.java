package ie.udaltsoft.ethnodroid.parsers;

import java.io.Serializable;
import java.util.ArrayList;

public class ParseResults implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList<CountryInfo> countries = new ArrayList<CountryInfo>();

	public void setCountries(ArrayList<CountryInfo> countries) {
		this.countries = countries;
	}

	public ArrayList<CountryInfo> getCountries() {
		return countries;
	}

}