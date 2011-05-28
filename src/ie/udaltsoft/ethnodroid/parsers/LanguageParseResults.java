package ie.udaltsoft.ethnodroid.parsers;

import java.io.Serializable;
import java.util.ArrayList;

public class LanguageParseResults extends WebPageParserResults implements
		Serializable {

	public static class CountryInfo implements Serializable {

		private static final long serialVersionUID = 1L;

		private String languageIsoCode;
		private String countryIsoCode;
		private String countryNameText;
		private String populationText;
		private String locationText;
		private String languageMapURLParams;
		private String languageMapText;
		private String alternateNamesText;
		private String dialectsText;
		private String classificationText;
		private String languageUseText;
		private String languageDevelopmentText;
		private String writingSystemText;
		private String commentsText;
		private String memberLanguagesText;

		public void setPopulationText(String populationText) {
			this.populationText = populationText;
		}

		public String getPopulationText() {
			return populationText;
		}

		public void setLocationText(String locationText) {
			this.locationText = locationText;
		}

		public String getLocationText() {
			return locationText;
		}

		public void setLanguageMapURL(String languageMapURLParams) {
			this.languageMapURLParams = languageMapURLParams;
		}

		public String getLanguageMapURLParams() {
			return languageMapURLParams;
		}

		public void setLanguageMapText(String languageMapText) {
			this.languageMapText = languageMapText;
		}

		public String getLanguageMapText() {
			return languageMapText;
		}

		public void setAlternateNamesText(String alternateNamesText) {
			this.alternateNamesText = alternateNamesText;
		}

		public String getAlternateNamesText() {
			return alternateNamesText;
		}

		public void setDialectsText(String dialectsText) {
			this.dialectsText = dialectsText;
		}

		public String getDialectsText() {
			return dialectsText;
		}

		public void setClassificationText(String classificationText) {
			this.classificationText = classificationText;
		}

		public String getClassificationText() {
			return classificationText;
		}

		public void setLanguageUseText(String languageUseText) {
			this.languageUseText = languageUseText;
		}

		public String getLanguageUseText() {
			return languageUseText;
		}

		public void setLanguageDevelopmentText(String languageDevelopmentText) {
			this.languageDevelopmentText = languageDevelopmentText;
		}

		public String getLanguageDevelopmentText() {
			return languageDevelopmentText;
		}

		public void setWritingSystemText(String writingSystemText) {
			this.writingSystemText = writingSystemText;
		}

		public String getWritingSystemText() {
			return writingSystemText;
		}

		public void setCommentsText(String commentsText) {
			this.commentsText = commentsText;
		}

		public String getCommentsText() {
			return commentsText;
		}

		public void setCountryNameText(String countryNameText) {
			this.countryNameText = countryNameText;
		}

		public String getCountryNameText() {
			return countryNameText;
		}

		public void setLanguageIsoCode(String languageIsoCodes) {
			this.languageIsoCode = languageIsoCodes;
		}

		public String getLanguageIsoCode() {
			return languageIsoCode;
		}

		public void setCountryIsoCode(String countryIsoCode) {
			this.countryIsoCode = countryIsoCode;
		}

		public String getCountryIsoCode() {
			return countryIsoCode;
		}

		public String toString() {
			return countryNameText == null ? countryIsoCode : countryNameText;
		}

		public void setMemberLanguagesText(String memberLanguagesText) {
			this.memberLanguagesText = memberLanguagesText;
		}

		public String getMemberLanguagesText() {
			return memberLanguagesText;
		}
	};

	private static final long serialVersionUID = 1L;

	private ArrayList<CountryInfo> countries = new ArrayList<CountryInfo>();
	private String languageName;
	private String iso639_1Code;

	public void setCountries(ArrayList<CountryInfo> countries) {
		this.countries = countries;
	}

	public ArrayList<CountryInfo> getCountries() {
		return countries;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setIso639_1Code(String iso639_1Code) {
		this.iso639_1Code = iso639_1Code;
	}

	public String getIso639_1Code() {
		return iso639_1Code;
	}

}