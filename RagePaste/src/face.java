import java.io.IOException;
import javax.swing.*;
import java.net.*;

public class face extends JFrame
{
	private String name;
	private String url;
	private URL imageURL;
	
	face(String nameIn, String urlIn) throws IOException
	{
		setName(nameIn);
		setUrl(urlIn);
		setImageURL();
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public URL getImageURL() {
		return imageURL;
	}

	public void setImageURL() throws MalformedURLException {
		URL urlTemp = new URL(url);
		this.imageURL = urlTemp;
	}
}
