package ie.udaltsoft.ethnodroid.parsers.data;

import java.io.Serializable;
import java.util.ArrayList;

public class LineageParseResults extends WebPageParserResults implements
		Serializable {

	public static class FamilyInfo extends NamedCode implements Serializable {

		private static final long serialVersionUID = 1L;

		private int numberOfLanguages;

		public void setNumberOfLanguages(int numberOfLanguages) {
			this.numberOfLanguages = numberOfLanguages;
		}

		public int getNumberOfLanguages() {
			return numberOfLanguages;
		}

		public String toString() {
			return getName() + " (" + numberOfLanguages + ")";
		}
	}

	private static final long serialVersionUID = 1L;

	private ArrayList<FamilyInfo> families = new ArrayList<FamilyInfo>();

	public void setFamilies(ArrayList<FamilyInfo> families) {
		this.families = families;
	}

	public ArrayList<FamilyInfo> getFamilies() {
		return families;
	}

}