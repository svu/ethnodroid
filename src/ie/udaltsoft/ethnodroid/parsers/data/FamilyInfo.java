package ie.udaltsoft.ethnodroid.parsers.data;

import java.io.Serializable;
import java.util.ArrayList;

public class FamilyInfo extends NamedCode implements Serializable {

	private static final long serialVersionUID = 1L;

	private int numberOfLanguages;

	private ArrayList<FamilyInfo> subfamilies = new ArrayList<FamilyInfo>();

	private ArrayList<NamedCode> languages = new ArrayList<NamedCode>();

	public FamilyInfo(String code, String name, int numberOfLanguages) {
		super(code, name);
		this.numberOfLanguages = numberOfLanguages;
	}

	public void setNumberOfLanguages(int numberOfLanguages) {
		this.numberOfLanguages = numberOfLanguages;
	}

	public int getNumberOfLanguages() {
		return numberOfLanguages;
	}

	public String toString() {
		return getName() + " (" + numberOfLanguages + ")";
	}

	public void setSubfamilies(ArrayList<FamilyInfo> subfamilies) {
		this.subfamilies = subfamilies;
	}

	public ArrayList<FamilyInfo> getSubfamilies() {
		return subfamilies;
	}

	public void setLanguages(ArrayList<NamedCode> languages) {
		this.languages = languages;
	}

	public ArrayList<NamedCode> getLanguages() {
		return languages;
	}
}