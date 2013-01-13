package test;

import java.net.Socket;

import javax.swing.JFrame;

public class DSClient extends Thread
{
	private Socket clientSocket;
	private Object lock;
	private JFrame fenetre;

	public DSClient ()
	{
		super();
		
		lock = new Object();
		
		// TODO changer le contenu du programme client
		fenetre = new JFrame();
		fenetre.setSize(300, 300);
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.setVisible(true);
	}
	
	public void coupeThread ()
	{
		synchronized (lock)
		{
			fenetre.setVisible(false);
			lock.notify();
		}
	}
	
	public void run ()
	{
		synchronized(lock)
		{
			System.out.println("Debut Thread");

			while (fenetre.isVisible())
			{
				try
				{
					clientSocket = new Socket("localhost", 6112);
					lock.wait();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			System.out.println("Fin Thread");
		}
	}

	public static void main(String argv[]) throws Exception 
	{
		DSClient prg_client = new DSClient();
		prg_client.start();
		
		PersoWindowListener listener = new PersoWindowListener(prg_client);
		prg_client.fenetre.addWindowListener(listener);
		prg_client.join();

	}

}
