package ie.udaltsoft.ethnodroid.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;

public abstract class WebPageParser<T extends Serializable> {

	public WebPageParser() {
		super();
	}

	public abstract T parse(BufferedReader rdr) throws IOException;
}