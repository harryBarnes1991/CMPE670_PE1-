import java.net.*;
import java.io.*;
import java.util.Scanner;

public class MyClient
{
	public static void main(String [] args)
	{
		try{
			
			Socket s = new Socket( "localhost", 16789);
			InputStream in = s.getInputStream();
			OutputStream out = s.getOutputStream();
			
			PrintWriter pout = new PrintWriter(out);

			//Getting input from user
			Scanner myScanner = new Scanner(System.in);
			System.out.println("Please enter student name and the 3 exams scores (Separate the name and each exam scores with a comma)");
			String userinput = myScanner.nextLine();
			pout.println(userinput);		// Writes some String to server
			pout.flush(); 					// forces the data through to server
			
			BufferedReader bin = new BufferedReader(new InputStreamReader(in));
			System.out.println(userinput+" <=returned-as=> " + bin.readLine());

			out.close();
			pout.close();
			s.close();
		}
		catch(IOException ioe)
		{
			System.out.println("IO error");
			ioe.printStackTrace();
		}
	   
	}
}