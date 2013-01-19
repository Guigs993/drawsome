package client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class GoButton extends JButton
{
	//////////////////
	// Constructeur //
	//////////////////
	public GoButton ()
	{
		try
		{
			BufferedImage icon = ImageIO.read(new File("images/go.png"));
			BufferedImage icon_rollover = createRolloverIcon(icon);
			
			setIcon(new ImageIcon(icon));
			setRolloverIcon(new ImageIcon(icon_rollover));
		}
		catch (IOException ioe)
		{
			System.out.println("Display Icon Problem");
			ioe.printStackTrace();
		}
	}
	
	//////////////
	// Methodes //
	//////////////
	// Redifinition de paintComponent pour enlever les proprietes basiques du JButton
	protected void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		
		setContentAreaFilled(false); // Enleve le fond par defaut des JButton
		setFocusPainted(false); // Enleve le cadre qui s'affiche une fois qu'on clique dessus
		setBorder(null); // Enleve les bordures du bouton et son effet du survol
	}
	
	// Methode qui fusionne 2 images en une nouvelle
	public static BufferedImage createRolloverIcon(BufferedImage icon)
	{
		BufferedImage icon_rollover = null;
		
		try
		{
			BufferedImage selection = ImageIO.read(new File("images/go_over.png"));
			
			int w = Math.max(icon.getWidth(), selection.getWidth());
			int h = Math.max(icon.getHeight(), selection.getHeight());
			icon_rollover = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			
			Graphics g = icon_rollover.getGraphics();
			g.drawImage(icon, 0, 0, null);
			g.drawImage(selection, 0, 0, null);
		}
		catch (IOException ioe)
		{
			System.out.println("Display RolloverIcon Problem");
			ioe.printStackTrace();
		}
		
		return icon_rollover;
	}
}
