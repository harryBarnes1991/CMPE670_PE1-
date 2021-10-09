import java.net.*;	// All network classes; ServerSocket, Socket
import java.io.*;		// All read and write classes, same I/O as we have already used

/** MyServer - Demo of client / server network communication
	 
*/																													

public class Inet {																						
	public static void main(String [] args) {															

	 	try {																										
	 		// These two lines show how to get the IP address of this client					
	 		System.out.println("getLocalHost: "+InetAddress.getLocalHost() );					
	 		System.out.println("getByName:    "+InetAddress.getByName("localhost") );		
	 		System.out.println("getByName:    "+InetAddress.getByName("www.rit.edu") );		
	 		System.out.println("getByName:    "+InetAddress.getByName("www.google.com") );	
	 	}																										
	 	catch( UnknownHostException e ) { 																			
	 		System.out.println("Error with read and write"); 										
	 		e.printStackTrace();																		
	 	}	 																										
	} // end main																								
}	// end class																								