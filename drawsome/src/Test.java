import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Test
{
	public static void main(String[] args) throws IOException
	{
		/*
		InetAddress adresse = InetAddress.getLocalHost();
		System.out.println(adresse);
		
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        
        while (interfaces.hasMoreElements())
        {  
           NetworkInterface currentInterface = interfaces.nextElement(); 
           
           //chaque carte réseau peut disposer de plusieurs adresses IP
           Enumeration<InetAddress> addresses = currentInterface.getInetAddresses(); 
           while (addresses.hasMoreElements())
           {  
               InetAddress currentAddress = addresses.nextElement();
               System.out.println(currentAddress.getHostAddress());
           }
       }
       
       */
		
		System.out.println(isValidAddress("www.developpezzzzaz.com"));
	}
	
	public static boolean isValidAddress(String host)
	{
		try
		{
			return InetAddress.getByName(host).isReachable(100);
		} 
		catch(Exception e)
		{
			return false;
		}
	}

}
