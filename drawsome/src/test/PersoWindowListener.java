package test;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PersoWindowListener extends WindowAdapter
{
	private DSClient dsclient;
	
	public PersoWindowListener (DSClient dsclient)
	{
		super();
		
		this.dsclient = dsclient;
	}
	
	public void windowClosing (WindowEvent we)
	{
		dsclient.coupeThread();
	}
}