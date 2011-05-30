package ie.udaltsoft.ethnodroid.parsers.data;


import java.io.Serializable;
import java.util.ArrayList;

public class CountryParseResults extends WebPageParserResults implements
		Serializable {
	
	private static final long serialVersionUID = 1L;

	private String countryName;

	private ArrayList<NamedCode> languages = new ArrayList<NamedCode>();

	public void setLanguages(ArrayList<NamedCode> languages) {
		this.languages = languages;
	}

	public ArrayList<NamedCode> getLanguages() {
		return languages;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryName() {
		return countryName;
	}

}