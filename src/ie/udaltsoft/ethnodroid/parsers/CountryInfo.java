package ie.udaltsoft.ethnodroid.parsers;

import java.io.Serializable;

public class CountryInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String languageIsoCode;
	private String countryIsoCode;
	private String countryNameText;
	private String populationText;
	private String locationText;
	private String languageMapURL;
	private String languageMapText;
	private String alternateNamesText;
	private String dialectsText;
	private String classificationText;
	private String languageUseText;
	private String languageDevelopmentText;
	private String writingSystemText;
	private String commentsText;

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

	public void setLanguageMapURL(String languageMapURL) {
		this.languageMapURL = languageMapURL;
	}

	public String getLanguageMapURL() {
		return languageMapURL;
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

	public void setLanguageIsoCode(String languageIsoCode) {
		this.languageIsoCode = languageIsoCode;
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
}