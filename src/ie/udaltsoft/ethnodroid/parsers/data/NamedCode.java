package ie.udaltsoft.ethnodroid.parsers.data;

import java.io.Serializable;

public class NamedCode implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;

	public void setCode(String isoCode) {
		this.code = isoCode;
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