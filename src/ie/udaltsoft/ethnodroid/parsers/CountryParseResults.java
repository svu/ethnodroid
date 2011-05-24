package ie.udaltsoft.ethnodroid.parsers;

import java.io.Serializable;
import java.util.ArrayList;

public class CountryParseResults extends WebPageParserResults implements
		Serializable {
	
	public static class LanguageInfo implements Serializable {

		private static final long serialVersionUID = 1L;

		private String isoCode;
		private String name;

		public void setIsoCode(String isoCode) {
			this.isoCode = isoCode;
		}

		public String getIsoCode() {
			return isoCode;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public String toString() {
			return name;
		}
	};

	private static final long serialVersionUID = 1L;

	private String countryName;

	private ArrayList<LanguageInfo> languages = new ArrayList<LanguageInfo>();

	public void setLanguages(ArrayList<LanguageInfo> languages) {
		this.languages = languages;
	}

	public ArrayList<LanguageInfo> getLanguages() {
		return languages;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryName() {
		return countryName;
	}

}