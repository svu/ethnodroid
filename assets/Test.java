import java.io.*;
import java.util.regex.*;

public class Test
{
  public static void main(String args[])
  {
    try {
      final BufferedReader br = new BufferedReader(new FileReader("sample.eng.txt"));

      final Pattern p1 = Pattern.compile("\\s*<p><a href=\"ethno_docs/introduction.asp#iso_code\".*<a href=\"http://www.sil.org/iso639-3/documentation.asp\\?id=.*\" target=\"_blank\">(.*)</a></p>\\s*");
      final Pattern p2 = Pattern.compile("\\s*<td>(.*)</td>\\s*");
      final Pattern p3 = Pattern.compile("\\s*<h3>Also spoken in:</h3>\\s*");
      String s;
      boolean isPopulationSection = false;
      boolean alsoSpokenIn = false;
      while ((s = br.readLine()) != null) {
        //System.out.println(s);
        final Matcher m1 = p1.matcher(s);
        if (m1.matches()) {
          System.out.println("found the group");
          isPopulationSection = true;
          System.out.println(m1.group(1));
        } else if (isPopulationSection && !alsoSpokenIn) {
          final Matcher m2 = p2.matcher(s);
          if (m2.matches()) {
            System.out.println(m2.group(1));
            isPopulationSection = false;
          }
        } else {
          final Matcher m3 = p3.matcher(s);
          if (m3.matches()) {
            alsoSpokenIn = true;
          }
        }
      }
    } catch (IOException ex) {
      System.err.println(ex);
    }
  }
}