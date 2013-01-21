package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JPanel;

import core.Message;

// Panel de dessin
public class ClientPanelDessin extends JPanel implements MouseListener, MouseMotionListener
{
	private ClientMain prgm_client;
	
	// tableau de tableaux de points
	private Vector<Vector<Point>> dessins;
	// un tableau de points correspond a une ligne tracee a la souris
	private Vector<Point> figure;
	
	public ClientPanelDessin (ClientMain prgm_client)
	{
		super();
		
		this.prgm_client = prgm_client;
		
		dessins = new Vector<Vector<Point>>();
		figure = new Vector<Point>();
		
		setBackground(Color.WHITE);
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	// on envoit un point au serveur
	public void sendPoint (Point p)
	{
		Message send_point = new Message(21, p);

		prgm_client.sendMessage(send_point);
	}

	// on envoit un signal de fin de trace au serveur
	private void sendEndFigure()
	{
		Message send_end = new Message(22, null);

		prgm_client.sendMessage(send_end);
	}
	
	// on ajoute un point a la figure en cours
	public void setDraw (Point p)
	{
		figure.add(p);
		
		repaint();
	}
	
	// on a fini la figure, on la rajoute alors au tableau des figures (ou le tableau de tableau
	// de point) et on reinitialise la variable figure
	public void setDraw ()
	{
		dessins.add(figure);
		figure = new Vector<Point>();
		
		repaint();
	}
	
	// quand on clique on recupere la position de la souris et on l'ajoute au tableau
	// on envoie aussi ses coordonnees au serveur
	public void mousePressed (MouseEvent e)
	{
		Point point = new Point(e.getX(), e.getY());
		setDraw(point);

		sendPoint(point);
	}

	// quand on maintenant un clic on recupere la position de la souris a chaque instant
	// et on l'ajoute au tableau, on envoie aussi ses coordonnees au serveur
	public void mouseDragged (MouseEvent e)
	{
		Point point = new Point(e.getX(), e.getY());
		setDraw(point);

		sendPoint(point);
	}
	
	// quand on relache la souris, on a fini un trace, on la rajoute alors au tableau des figures
	// (ou le tableau de tableau de point) et on reinitialise la variable figure
	public void mouseReleased (MouseEvent e)
	{
		setDraw();
	}

	// redefinition de la methode paint
	protected void paintComponent (Graphics g)
	{
		super.paintComponent(g);

		Point p1 = new Point();
		Point p2 = new Point();
		
		// on dessine tous les traces effectues a la souris (1 trace = un tabeau de point)
		for (Vector<Point> figure : dessins)
		{
			// on test si c'est un trace ou un simple clic/release
			// si c'est un trace, on relie tous les points entre eux
			if (figure.size() > 1)
			{
				for (int i=1; i<figure.size(); i++)
				{
					p1 = figure.get(i-1);
					p2 = figure.get(i);
					
					g.drawLine(p1.x, p1.y, p2.x, p2.y);	
				}
			}
			// sinon on ne dessine qu'un seul point
			else
			{
				p1 = figure.get(0);
				g.fillOval(p1.x, p1.y, 2, 2);
			}
		}

		// meme chose pour la figure en cours de modification
		if (figure.size() > 1)
		{
			for (int i=1; i<figure.size(); i++)
			{
				p1 = figure.get(i-1);
				p2 = figure.get(i);
				
				g.drawLine(p1.x, p1.y, p2.x, p2.y);	
			}
		}
		else if (figure.size() == 1)
		{
			p1 = figure.get(0);
			g.fillOval(p1.x, p1.y, 2, 2);
		}
	}
	
	public void mouseMoved (MouseEvent e) {}

	public void mouseClicked (MouseEvent e) {}
	
	public void mouseEntered (MouseEvent e) {}

	public void mouseExited (MouseEvent e) {}

}
