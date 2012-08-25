import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.net.*;

public class face extends JFrame
{
	private ImageIcon image;
	private String name;
	private String url;
	private URL imageURL;
	
	face(String nameIn, String urlIn) throws IOException
	{
		setName(nameIn);
		setUrl(urlIn);
		setImage();
		setImageURL();
	}
	
	private void setImage() throws IOException
	{
		//getClass().getResourceAsStream( is required to read from files inside the .jar
		
		
		try
		{
			this.image = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/images/"+name+".jpg")));
		}
		catch (Exception e)
		{
			try
			{
				//InputStream in = getClass().getResourceAsStream("/images/"+name+".gif");
				//Image imageImage = Toolkit.getDefaultToolkit().createImage(org.apache.commons.io.IOUtils.toByteArray(in));
				//Image imageImage = ImageIO.read(in);
				//this.image = new ImageIcon(imageImage);
				this.image = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/images/"+name+".gif")));
				image.setImageObserver(getParent());
			}
			catch (Exception f)
			{
				e.printStackTrace();
				System.out.println("!!You didn't refresh the resources folder &&|| make the picture a .jpg/.gif!!");
			}
		}
	}

	public ImageIcon getImage() {
		return image;
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
