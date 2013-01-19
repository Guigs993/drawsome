package test;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServeurThread implements Runnable
{
	private Thread t;
	private Socket s;
	
	private OutputStreamWriter out; // inutilisée pour le moment
	private BufferedReader in;

	private DSClient client;
	
	public ServeurThread(Socket s, DSClient client)
	{
		this.s = s;
		this.client = client;
		
		try
		{
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			// out = new DataOutputStream(s.getOutputStream());
			
			t = new Thread(this);
			t.start();
		}
		catch (IOException e)
		{
			System.out.println("Erreur creation flux serveur");
		}

	}
	
	public void run()
	{
		String message = "";
		
	    try {
			while((message=in.readLine()) != null)
			{
				decodeMessage(message);
				//client.addMessage(message);
			}
	    }
	    catch (Exception e)
		{
	    	//
		}
	}
	
	public void decodeMessage(String m)
	{
		String[] split = m.split(";");
		int type = Integer.parseInt(split[0]);
		String message = split[1];
		
		switch (type) 
		{
			case 1 :
				client.addMessage(message);
				break;
			case 2 :
				split = message.split(":");
				Point p = new Point(Integer.parseInt(split[0]),Integer.parseInt(split[1]));
				client.getDessinPanel().dessineEtEnvoi(p, false);
				break;
		}
	}

}
