package ie.udaltsoft.ethnodroid.parsers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class GroupedCodes extends WebPageParserResults implements
		Serializable {

	private static final long serialVersionUID = 1L;

	public static class CodeRef implements Serializable {

		private static final long serialVersionUID = 1L;

		private String code;
		private String name;

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

		public String toString() {
			return name;
		}
	}

	private HashMap<String, ArrayList<CodeRef>> groups;

	public void setGroups(HashMap<String, ArrayList<CodeRef>> countries) {
		this.groups = countries;
	}

	public HashMap<String, ArrayList<CodeRef>> getGroups() {
		return groups;
	}
}