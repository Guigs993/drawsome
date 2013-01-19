package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JPanel;

import core.Figure;

public class ClientPanelDessin extends JPanel implements MouseListener, MouseMotionListener
{
	private Vector<Figure> tabFigures;
	private Vector<Point> points;
	
	private Point lastPoint;
	
	public ClientPanelDessin ()
	{
		super();
		
		tabFigures = new Vector<Figure>();
		points = new Vector<Point>();
		lastPoint = null;
		
		setBackground(Color.WHITE);
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	protected void paintComponent (Graphics g)
	{
		super.paintComponent(g);

		for (Point p : points)
			g.fillOval(p.x, p.y, 2, 2);
	}

	public void mouseDragged (MouseEvent e)
	{
		Point point = new Point(e.getX(), e.getY());
		points.add(point);
		repaint();
	}

	public void mousePressed (MouseEvent e)
	{
		Point point = new Point(e.getX(), e.getY());
		points.add(point);
		repaint();
	}
	
	public void mouseMoved (MouseEvent e) {}

	public void mouseClicked (MouseEvent e) {}

	public void mouseEntered (MouseEvent e) {}

	public void mouseExited (MouseEvent e) {}
	
	public void mouseReleased (MouseEvent e) {}

}
