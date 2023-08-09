import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Loader {

	public static Properties loadProperties() throws IOException {
		Properties configuration = new Properties();
		InputStream inputStream = Loader.class.getClassLoader().getResourceAsStream("application.properties");
		configuration.load(inputStream);
		inputStream.close();
		return configuration;
	}
}
