package test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ConcurrentModificationException;
import java.util.Vector;

import javax.swing.JPanel;

public class PanelDessin extends JPanel implements MouseListener, MouseMotionListener
{
	private Vector<Point> points = new Vector<Point>();
	
	private DSClient client; 
	
	public PanelDessin(DSClient client)
	{
		super();
		
		this.client = client;
		
		setBackground(Color.WHITE);
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void dessineEtEnvoi(Point p, boolean envoi)
	{
		points.add(p);
		repaint();
		
		if (envoi)
		{
			String message = p.x + ":" + p.y;
			client.sendToServeur(2, message);
		}
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		for (Point p : points)
			g.drawLine(p.x, p.y, p.x, p.y);
		
		/*
		try
		{
		}
		catch (ConcurrentModificationException cme)
		{
		}
		*/
	}

	public void mouseDragged(MouseEvent e)
	{
		Point point = new Point(e.getX(), e.getY());
		dessineEtEnvoi(point, true);
	}

	public void mousePressed(MouseEvent e)
	{
		Point point = new Point(e.getX(), e.getY());
		dessineEtEnvoi(point, true);
	}

	
	public void mouseMoved(MouseEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}
	public void mouseReleased(MouseEvent e)
	{
	}

}
