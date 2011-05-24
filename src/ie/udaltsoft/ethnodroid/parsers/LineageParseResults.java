package ie.udaltsoft.ethnodroid.parsers;

import java.io.Serializable;
import java.util.ArrayList;

public class LineageParseResults extends WebPageParserResults implements
		Serializable {

	public static class FamilyInfo implements Serializable {

		private static final long serialVersionUID = 1L;

		private String code;
		private String name;
		private int numberOfLanguages;

		public void setCode(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setNumberOfLanguages(int numberOfLanguages) {
			this.numberOfLanguages = numberOfLanguages;
		}

		public int getNumberOfLanguages() {
			return numberOfLanguages;
		}

		public String toString() {
			return name + " (" + numberOfLanguages + ")";
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