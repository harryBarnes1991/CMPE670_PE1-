/*
	Group: Harrison Barnes, Long Lam, Adam Seidman
	CMPE570/670 - Data & Communication Networks
	Practical Exercise 1

	This program provides functionality for the client in a client-server connection.
	The client connects, provides a student name with three exam grades.
	The client then disconnects upon receiving the exam average.
 */

//Import required java libraries
import java.net.*;
import java.io.*;
import java.util.Scanner;

/*
	Class: 			MyClient
	Functionality:	Connects to server. Provides a student name and three exam grades to average.
					Receives averaged grades from server and prints to terminal.
	Parent:			None
	Children: 		None
	Dependencies: 	None
 */
public class MyClient
{
	/*
		Method:  		main
		Arguments: 		String[] (not used)
		Returns:		void
		Preconditions: 	None
		Functionality:	Connects to server. Provides a student name and three exam grades to average.
						Receives averaged grades from server and prints to terminal.
	 */
	public static void main(String [] args)
	{
		try{	//Run until IOException
			//Create a socket to port 16789 and establish IO streams and PrintWriter
			Socket s = new Socket( "localhost", 16789);
			InputStream in = s.getInputStream();
			OutputStream out = s.getOutputStream();
			PrintWriter pout = new PrintWriter(out);

			//Prompt and receive input from user
			Scanner myScanner = new Scanner(System.in);
			System.out.println("Please enter student name and the 3 exams scores (Separate the name and each exam scores with a comma)");
			String userinput = myScanner.nextLine();

			//Send string to server
			pout.println(userinput);		//Writes some String to server
			pout.flush(); 					//Forces the data through to server

			//Establish receive buffer and wait for transmission from server
			BufferedReader bin = new BufferedReader(new InputStreamReader(in));

			//Print string from server transmission to terminal
			System.out.println(userinput+" <=returned-as=> " + bin.readLine());

			//Close connection and data streams
			out.close();
			pout.close();
			s.close();
		}
		catch(IOException ioe) { //Catch and handle IOExceptions
			System.out.println("IO error");	//Notify user of exception
			ioe.printStackTrace();			//Print stack trace for debugging and error information
		}
	}
}