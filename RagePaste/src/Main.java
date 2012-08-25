import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main  extends JFrame
{

	/**
	 * Karl Coe
	 * http://steamcommunity.com/id/cyberXwarrior
	 * Rage Paste
	 * Copies links to rage faces into clipboard for quick pasting
	 * 
	 * To Do:
	 * Hot keys from anywhere
	 * Favorite faces?
	 */
	private static double versionNumber=1.1;
	
	
	public static void main(String[] args) throws IOException
	{
		
		Main window = new Main();
		window.setTitle("Rage Paste "+versionNumber);
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); //window is hidden on close
        window.setContentPane(new menu());
		window.pack();
		window.setVisible(true);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		
		
		//http://stackoverflow.com/questions/5824049/running-a-method-when-closing-the-program
		window.addComponentListener(new ComponentAdapter()
		{
            //When the window is hidden
            public void componentHidden(ComponentEvent e)
            {
                try
                {
                	//exportToFile() is run on hide
					menu.exportToFile();
				}
                catch (FileNotFoundException e1)
                {
					e1.printStackTrace();
				}
                //Window is closed
                ((JFrame)(e.getComponent())).dispose();
            }
        });
	}


}
