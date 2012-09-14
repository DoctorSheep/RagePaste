import java.awt.Image;
import java.io.IOException;
import javax.swing.*;

import java.net.*;

public class face extends JFrame
{
	private String name;
	private String url;
	private URL imageURL;
	private ImageIcon image;
	private String imageType;
	private ImageIcon image200;
	private ImageIcon image400;
	private ImageIcon image100;
	private ImageIcon image50;
	
	face(String nameIn, String urlIn) throws IOException
	{
		setName(nameIn);
		setUrl(urlIn);
		setImageURL();
	}

	private void scale()
	{
		//Check content type from website connection, If it's not a gif
		if(imageType.contains("gif")!=true)
		{
			Image img=image.getImage();
			
			//Not a gif, can use scale smooth
			Image tempImg;
			if(image.getIconHeight()>image.getIconWidth())
			{
				image400=new ImageIcon(img.getScaledInstance(-1,400,java.awt.Image.SCALE_SMOOTH));
				image200=new ImageIcon(img.getScaledInstance(-1,200,java.awt.Image.SCALE_SMOOTH));
				image100=new ImageIcon(img.getScaledInstance(-1,100,java.awt.Image.SCALE_SMOOTH));
				image50=new ImageIcon(img.getScaledInstance(-1,50,java.awt.Image.SCALE_SMOOTH));
				
			}
			else if(image.getIconHeight()<image.getIconWidth())
			{
				image400=new ImageIcon(img.getScaledInstance(400,-1,java.awt.Image.SCALE_SMOOTH));
				image200=new ImageIcon(img.getScaledInstance(200,-1,java.awt.Image.SCALE_SMOOTH));
				image100=new ImageIcon(img.getScaledInstance(100,-1,java.awt.Image.SCALE_SMOOTH));
				image50=new ImageIcon(img.getScaledInstance(50,-1,java.awt.Image.SCALE_SMOOTH));
			}
			else
			{
				image400=new ImageIcon(img.getScaledInstance(400,400,java.awt.Image.SCALE_SMOOTH));
				image200=new ImageIcon(img.getScaledInstance(200,200,java.awt.Image.SCALE_SMOOTH));
				image100=new ImageIcon(img.getScaledInstance(100,100,java.awt.Image.SCALE_SMOOTH));
				image50=new ImageIcon(img.getScaledInstance(50,50,java.awt.Image.SCALE_SMOOTH));
			}
			
		}
		//Check content type from website connection, If it is a gif
		else if(imageType.contains("gif")==true)
		{
			Image img=image.getImage();
			
			//Smooth scaling doesn't work with animated gifs, have to use scale_fast
			Image tempImg;
			if(image.getIconHeight()>image.getIconWidth())
			{
				image400=new ImageIcon(img.getScaledInstance(-1,400,java.awt.Image.SCALE_FAST));
				image200=new ImageIcon(img.getScaledInstance(-1,200,java.awt.Image.SCALE_FAST));
				image100=new ImageIcon(img.getScaledInstance(-1,100,java.awt.Image.SCALE_FAST));
				image50=new ImageIcon(img.getScaledInstance(-1,50,java.awt.Image.SCALE_FAST));
			}
			else if(image.getIconHeight()<image.getIconWidth())
			{
				image400=new ImageIcon(img.getScaledInstance(400,-1,java.awt.Image.SCALE_FAST));
				image200=new ImageIcon(img.getScaledInstance(200,-1,java.awt.Image.SCALE_FAST));
				image100=new ImageIcon(img.getScaledInstance(100,-1,java.awt.Image.SCALE_FAST));
				image50=new ImageIcon(img.getScaledInstance(50,-1,java.awt.Image.SCALE_FAST));
			}
			else
			{
				image400=new ImageIcon(img.getScaledInstance(400,400,java.awt.Image.SCALE_FAST));
				image200=new ImageIcon(img.getScaledInstance(200,200,java.awt.Image.SCALE_FAST));
				image100=new ImageIcon(img.getScaledInstance(100,100,java.awt.Image.SCALE_FAST));
				image50=new ImageIcon(img.getScaledInstance(50,50,java.awt.Image.SCALE_FAST));
			}
			
		}
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


	public ImageIcon getImage() {
		return image;
	}
	
	public ImageIcon getImage(int size)
	{
		if(size==3)
			return image400;
		else if(size==2)
			return image200;
		else if(size==1)
			return image100;
		else if(size==0)
			return image50;
		
		else
			return null;
	}


	public void setImage(ImageIcon image)
	{
		this.image = image;
		
		scale();
	}


	public String getImageType() {
		return imageType;
	}


	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	
	
}
