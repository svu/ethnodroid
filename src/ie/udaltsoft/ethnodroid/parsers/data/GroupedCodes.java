package ie.udaltsoft.ethnodroid.parsers.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class GroupedCodes extends WebPageParserResults implements Serializable {

	private static final long serialVersionUID = 1L;

	private HashMap<String, ArrayList<NamedCode>> groups = new HashMap<String, ArrayList<NamedCode>>();

	public void setGroups(HashMap<String, ArrayList<NamedCode>> countries) {
		this.groups = countries;
	}

	public HashMap<String, ArrayList<NamedCode>> getGroups() {
		return groups;
	}
}