package ie.udaltsoft.ethnodroid.parsers.data;

import java.io.Serializable;

public class LanguageFamilyTreeParseResults extends WebPageParserResults
		implements Serializable {

	private static final long serialVersionUID = 1L;

	private FamilyInfo topFamily;

	public void setTopFamily(FamilyInfo topFamily) {
		this.topFamily = topFamily;
	}

	public FamilyInfo getTopFamily() {
		return topFamily;
	}
}