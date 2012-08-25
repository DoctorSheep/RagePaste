import java.util.*;
import java.util.concurrent.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import javax.activation.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class menu extends JComponent implements ActionListener, MouseMotionListener, MouseListener
{
	static ArrayList<face> faces=new ArrayList<face>();
	JButton settingsButton=null;
	JScrollPane scrollPane=null; //Comment
	JPanel panel=null;
	static JProgressBar progressBar=null;
	ArrayList<JButton> buttons=new ArrayList<JButton>(); //array of buttons that parallels faces array for easy button checking in action listener
	face pressedFace=null; //face that the mouse was pressed down on
	int pressedFaceIndex=-1; //current index of the face that was pressed
	int releasedFaceIndex=-1; //current index of the face that was released on
	face tempFace=null; //used when moving faces around
	File settingsFile=new File(System.getProperty("user.home")+"\\.rage_paste_settings.txt"); //File in user directory with faces and their order
	int tempScrollBarLocation=0; //used to return user to original position after UI update initiated by a face move
	static int faceGridSentinel=0;//Check to see if the grid has been made
	static ArrayList<ImageIcon> images=new ArrayList<ImageIcon>();//holding downloaded images
	
	
	public menu() throws IOException
	{
		//set size of window
		setPreferredSize(new Dimension(800, 600));
		
		//If a settings file already exists
		if(settingsFile.exists())
		{
			//Get faces from file
			//System.out.println("Importing from existing settings file");
			importFromFile();
		}
		else
		{
			//No file exists, continue on to jar importing
			//System.out.println("No pre existing file");
		}
		
		//import rage faces from jar file
		importJarFaces();
		//create on screen buttons
		makeButtons();
		//create a grid of icons 3 across
		//setUpGrid();
		
		progressBar.setVisible(true);
		progressBar.setIndeterminate(true);
		//http://www.dreamincode.net/forums/topic/27952-progress-bar-tutorial/
		new Thread(new thread1()).start();

		repaint();
		
	}
	
	private void setUpGrid() throws IOException
	{		
		//Null out everything just in case we are re-arranging faces
		if(scrollPane!=null)
		{
			this.remove(scrollPane);
		}
		panel=null;
		buttons.clear();
		scrollPane=null;
		
		panel = new JPanel(new GridLayout(0,3));
		
		
		for(int i=0;i<faces.size();i++)
		{
			
			JButton temp = null;
			//ImageIcon tempImageIcon=faces.get(i).getImage();
			//temp=new JButton(tempImageIcon); //get image for button
			
			
			temp=new JButton(images.get(i));
			//System.out.println(faces.get(i).getName()+" set.");
			
			//temp=new JButton(new ImageIcon(faces.get(i).getImageURL()));
			
			//final URL url = new URL("file:///C:/Users/Karl/Desktop/Dropbox/workspace/Rage%20Paste/rsrc/images/i%20have%20no%20idea%20what%20i%27m%20doing.jpg");
			//temp=new JButton(new ImageIcon(url)); //get image for button
			
			temp.addActionListener(this);
			temp.addMouseMotionListener(this);
			temp.addMouseListener(this);
			temp.setSize(200, 200);
			buttons.add(temp);
			panel.add(temp);
			//panel.add(new JButton(faces.get(i).getImage()));
			
			
	    }
		//Scroll pane of grid dimensions
		int scrollpanex=720;
		int scrollpaney=550;
		scrollPane=new JScrollPane(panel);
		scrollPane.setSize(scrollpanex, scrollpaney);
		//Set the grid in the bottom right corner no matter the size
		scrollPane.setLocation(getPreferredSize().width-scrollpanex,getPreferredSize().height-scrollpaney);
		scrollPane.setVisible(true);
		this.add(scrollPane);
		
		scrollPane.getVerticalScrollBar().setUnitIncrement(16); //Set scroll speed to 16px/tick
		scrollPane.updateUI(); //Updates scroll pane (to view changes made to data)
		scrollPane.getVerticalScrollBar().setValue(tempScrollBarLocation); //Set scroll bar location twice, otherwise location goes to 90 pixels
		scrollPane.getVerticalScrollBar().setValue(tempScrollBarLocation); //Having only one works if scroll location was <90 pixels
		
	}
	
	//Get face info from faces.txt and add them to the faces arraylist
	private void importJarFaces() throws IOException
	{
		String tempName=null;
		String tempUrl=null;
		int sentinel=1; //1=add face, 0=have face
		
		//getClass().getResourceAsStream( is required to read from files inside the .jar
		Scanner sc=new Scanner(getClass().getResourceAsStream("/faces.txt"));
		
		while(sc.hasNext())
		{
			tempName=sc.nextLine();
			tempUrl=sc.nextLine();
			//System.out.println("Checking "+tempName);
			
			//If there are no existing faces the face is guaranteed to be new, so add it
			if(faces.size()==0)
			{
				//System.out.println("Adding this new face");
				faces.add(new face(tempName,tempUrl));
			}
			//There are existing faces
			else
			{
				sentinel=1; //Default to adding the face
				//Go through all existing faces
				for(int j=0;j<faces.size();j++)
				{
					//System.out.println("Checking face number "+j);
					//If you have the face already
					if(faces.get(j).getName().contains(tempName)&&faces.get(j).getUrl().contains(tempUrl))
					{
						//System.out.println("Already have this face");
						sentinel=0; //Dont add the face
						break; //Don't need to check remaining faces, already found the droids we were looking for
					}
				}
				//If face wasn't found
				if(sentinel==1)
				{
					//System.out.println("Adding this new face");
					faces.add(new face(tempName,tempUrl)); //Add the face to the array
				}
				
			}
		}
		//System.out.println();
	}
	
	//Creates all of the buttons in the window
	private void makeButtons()
	{
		
		//settings button
		settingsButton=new JButton("Settings");
		settingsButton.setSize(95, 30);
		settingsButton.setLocation(0,0);
		settingsButton.setVisible(true);
		settingsButton.addActionListener(this);
		this.add(settingsButton);
		
		
		
		progressBar =new JProgressBar(0, 0);
		//progressBar.setValue(5);
		//progressBar.setVisible(true);
		progressBar.setLocation(95,0);
		progressBar.setSize(800-85,30);
		
		progressBar.setMaximum(faces.size()-1);
		progressBar.setStringPainted(true);
		this.add(progressBar);
		
		repaint();
	}
	
	//Write a string to the keyboard
	private static void writeToClipboard(String s)
	{
	  Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	  Transferable transferable = new StringSelection(s);
	  clipboard.setContents(transferable, null);
	}
	
	//Export current faces to a text file for easy importing
	public static void exportToFile() throws FileNotFoundException
	{
		//File is saved in the home folder of user
		File file=new File(System.getProperty("user.home")+"//.rage_paste_settings.txt");
		PrintWriter pw=new PrintWriter(file);
		for(int i=0;i<faces.size();i++)
		{
			//Print name and url to file
			pw.println(faces.get(i).getName());
			pw.print(faces.get(i).getUrl());
			//If the last face printed is not the last face go to the next line
			//Prevents a blank line at the end of the file
			if(i<faces.size()-1)
			{
				pw.println();
			}
		}
		pw.close();
	}
	
	//Import face order from an export
	private void importFromFile() throws IOException
	{
		Scanner sc=new Scanner(settingsFile);
		int i=0; //current face number added
		while(sc.hasNext())
		{
			//new face(face name, url to face online)
			faces.add(new face(sc.nextLine(),sc.nextLine()));
			//System.out.println(faces.get(i).getName()+" Added");
			i++;
		}
		
		
	}
	
	//Action Listener for buttons
	public void actionPerformed(ActionEvent e) 
	{
		
		
		//If the settings button is clicked
		if(e.getSource()==settingsButton)
		{
			//Place holder outputs
			System.out.println();
			System.out.println("OS Name: "+System.getProperty("os.name"));
			System.out.println("OS Version: "+System.getProperty("os.version"));
			System.out.println("OS Architecture: "+System.getProperty("os.arch"));
			System.out.println("Java Version: "+System.getProperty("java.version"));
			System.out.println("System Temp Dir: "+System.getProperty("java.io.tmpdir"));
			System.out.println("Running Dir: "+System.getProperty("user.dir"));
			System.out.println("User Home Dir: "+System.getProperty("user.home"));
			System.out.println("User Name: "+System.getProperty("user.name"));
			System.out.println();
		}
		
		else
		{
			//Check to see if the grid was made yet before trying to check the components
			if(faceGridSentinel==1)
			{
				//Check all of the rage faces in the grid of buttons
				for(Component c:panel.getComponents())
				{
					//If clicked
					if(c.equals(e.getSource()))
					{
						//This is only run if the click is released on the button
						
						//send the url of that face to the clipboard
						writeToClipboard(faces.get(buttons.indexOf((JButton)c)).getUrl());
					}
				}
			}
		}

	}

	public class thread1 implements Runnable{
		public void run()
		{
			//Download all images using threads
			long startThreadTimer=System.nanoTime(); //Start thread timer
			//http://stackoverflow.com/questions/7502718/java-download-multiple-files-using-threads
			ExecutorService pool = Executors.newFixedThreadPool(100);
			for(int i=0;i<faces.size();i++)
			{
			    pool.submit(new DownloadTask(i, faces.get(i).getUrl()));
			}
			pool.shutdown();
			try {
				pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			// all tasks have now finished (unless an exception is thrown above)
			long endThreadTimer=System.nanoTime(); //End grid timer
			System.out.println("threads loaded in: "+(endThreadTimer-startThreadTimer)+"ns. "+((endThreadTimer-startThreadTimer)/1000000000)+"secs");
			
			
			//create a grid of icons with faces 3 across
			try
			{
				long startGridTimer=System.nanoTime(); //Start grid timer
				setUpGrid();
				long endGridTimer=System.nanoTime(); //End grid timer
				System.out.println("Grid loaded in: "+(endGridTimer-startGridTimer)+"ns. "+((endGridTimer-startGridTimer)/1000000000)+"secs");
			}
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			

			faceGridSentinel=1; //once the grid has been made set 1
			
			progressBar.setVisible(false);
			progressBar.setValue(0);
			
			
		}
	}
	
	
	//Part of threading, downloading images using threads
	private static class DownloadTask implements Runnable
	{
	    private int name;
	    private final String toPath;

	    public DownloadTask(int name, String toPath)
	    {
	        this.name = name;
	        this.toPath = toPath;
	    }
	    @Override
	    public void run()
	    {
	    	//System.out.println(images.size());
	        //images.add(new ImageIcon(faces.get(name).getImageURL()));
	        
	        
	        ImageIcon tempImageIcon=new ImageIcon(faces.get(name).getImageURL());
			//Make a new connection to the image website
			//URLConnection connection = faces.get(i).getImageURL().openConnection();
			
			HttpURLConnection connection = null;
			try {
				connection = (HttpURLConnection)  faces.get(name).getImageURL().openConnection();
				connection.setRequestMethod("HEAD");
				connection.connect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//String contentType = connection.getContentType();
			
			
			//Check content type from website connection, If it's not a gif
			if(connection.getContentType().contains("gif")!=true)
			{
				Image img=tempImageIcon.getImage();
				
				//Smooth scaling doesn't work with animated gifs, have to use scale_fast
				Image tempImg;
				if(tempImageIcon.getIconHeight()>tempImageIcon.getIconWidth())
				{
					tempImg=img.getScaledInstance(-1,200,java.awt.Image.SCALE_SMOOTH);
				}
				else if(tempImageIcon.getIconHeight()<tempImageIcon.getIconWidth())
				{
					tempImg=img.getScaledInstance(200,-1,java.awt.Image.SCALE_SMOOTH);
				}
				else
				{
					tempImg=img.getScaledInstance(200,200,java.awt.Image.SCALE_SMOOTH);
				}
				tempImageIcon=new ImageIcon(tempImg);
			}
			//Check content type from website connection, If it is a gif
			else if(connection.getContentType().contains("gif")==true)
			{
				Image img=tempImageIcon.getImage();
				
				//Smooth scaling doesn't work with animated gifs, have to use scale_fast
				Image tempImg;
				if(tempImageIcon.getIconHeight()>tempImageIcon.getIconWidth())
				{
					tempImg=img.getScaledInstance(-1,200,java.awt.Image.SCALE_FAST);
				}
				else if(tempImageIcon.getIconHeight()<tempImageIcon.getIconWidth())
				{
					tempImg=img.getScaledInstance(200,-1,java.awt.Image.SCALE_FAST);
				}
				else
				{
					tempImg=img.getScaledInstance(200,200,java.awt.Image.SCALE_FAST);
				}
				tempImageIcon=new ImageIcon(tempImg);
				
				
			}
			//Have the threads enter limbo when done processing, this insures they are added in order.
			boolean sentinel=false; //if this thread is sleeping and hasnt been counted as a percentage done set false
			do
			{
				//Add the 0th picture
				if(name==0)
				{
					images.add(tempImageIcon);
					if(sentinel==false)
					{
						progressBar.setIndeterminate(false);
						sentinel=true;
						progressBar.setValue(progressBar.getValue()+1);
					}
					//progressBar.setValue(name);
					//System.out.println(name+"done. images size:"+images.size());
					break;
				}
				//Add all other pictures in order if it's their time
				else if(name==(images.size()))
				{
					images.add(tempImageIcon);
					if(sentinel==false)
					{
						progressBar.setIndeterminate(false);
						sentinel=true;
						progressBar.setValue(progressBar.getValue()+1);
					}
					
					//progressBar.setValue(name);
					//System.out.println(name+"done");
					break;
				}
				//If it isn't their time sleep for 50ms and try again.
				else
				{
					if(sentinel==false)
					{
						progressBar.setIndeterminate(false);
						sentinel=true;
						progressBar.setValue(progressBar.getValue()+1);
					}
					
					try {
						//System.out.println(name+"sleeping");
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}while(name!=(images.size()-1));
	    }
	}
	
	
	//Mouse listener for full mouse click
	public void mouseReleased(MouseEvent e)
	{
		//Get index in faces Array List of the face the mouse was released on
		releasedFaceIndex=buttons.indexOf(panel.findComponentAt(e.getXOnScreen()-panel.getLocationOnScreen().x,e.getYOnScreen()-panel.getLocationOnScreen().y));
		
		//If the user did not drag the face outside of the grid, swap faces normally
		if(releasedFaceIndex!=-1)
		{
			//System.out.println(faces.get(buttons.indexOf(panel.findComponentAt(e.getXOnScreen()-panel.getLocationOnScreen().x,e.getYOnScreen()-panel.getLocationOnScreen().y))).getName()+" was released on");
	        //System.out.println();
			
	        //If the face that the mouse was pressed on and the one it was released on are different (AKA: dragging one face to another)
			if(faces.get(buttons.indexOf(panel.findComponentAt(e.getXOnScreen()-panel.getLocationOnScreen().x,e.getYOnScreen()-panel.getLocationOnScreen().y)))!=pressedFace)
	        {
				//Record location of vertical scroll  bar before move
				tempScrollBarLocation=scrollPane.getVerticalScrollBar().getValue();
				
	        	//Swap faces!
	        	tempFace=faces.get(pressedFaceIndex);
	        	faces.remove(pressedFaceIndex);
	        	faces.add(releasedFaceIndex, tempFace);
	        	
	        	//swap images
	        	ImageIcon tempImage = images.get(pressedFaceIndex);
	        	images.remove(pressedFaceIndex);
	        	images.add(releasedFaceIndex, tempImage);
	        	try {
					setUpGrid();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	tempFace=null;
	        }
	        else
	        {
	        	//Do nothing, its just a normal press of the button. Let the action listener handle it
	        }
		}
		else
		{
			//Do nothing, User dragged face out side of grid
		}
    }

	//Mouse listener for mouse button pushed in
	public void mousePressed(MouseEvent e)
	{
		//Get the face that was pressed and its index in the array
		pressedFace=faces.get(buttons.indexOf(panel.findComponentAt(e.getXOnScreen()-panel.getLocationOnScreen().x,e.getYOnScreen()-panel.getLocationOnScreen().y)));
		pressedFaceIndex=buttons.indexOf(panel.findComponentAt(e.getXOnScreen()-panel.getLocationOnScreen().x,e.getYOnScreen()-panel.getLocationOnScreen().y));
		//System.out.println(faces.get(buttons.indexOf(panel.findComponentAt(e.getXOnScreen()-panel.getLocationOnScreen().x,e.getYOnScreen()-panel.getLocationOnScreen().y))).getName()+" was pressed on");
	}
	
	//--------------------------------------------------------------------BLANK PAST HERE--------------------------------------------------------------------------------
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		//System.out.println(e);
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		//System.out.println(e);
	}
	
}
