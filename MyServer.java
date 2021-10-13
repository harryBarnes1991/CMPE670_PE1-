/*
	Group: Harrison Barnes, Long Lam
	CMPE570/670 - Data & Communication Networks
	Practical Exercise 1

	This program provides functionality for the server in a client-server connection.
	The client connects, provides a student name with three exam grades.
	The server calculates the average grade, stores the student and the average in the server.
	The server transmits the average grade back to the client.
	The client is threaded with relevant synchronizations to handle multiple clients error-free.
 */

//Import required java libraries
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
	Class: 			MyServer
	Functionality:	Receives student grade data from clients. Calculates average exam score. Exam averages are
					stored internally and transmitted to the client. MyServer is able to handle multiple clients.
	Parent:			None
	Children: 		ThreadServer (inner Class)
	Dependencies: 	None
 */
public class MyServer{
	/*
		Method:  		main
		Arguments: 		String[] (not used)
		Returns:		void
		Preconditions: 	None
		Functionality:	Static function. Creates a new server.
	 */
	public static void main(String [] args) {
		new MyServer();
	}

	/*
		Method:  		MyServer - Constructor
		Arguments: 		None
		Returns:		None
		Preconditions: 	None
		Functionality:	Establishes connections with one or more clients and creates a thread for each.
	 */
	public MyServer()
	{
		//Allocate memory for server socket for connections
		ServerSocket ss = null;

		//2D list for storing student name and their average
		List<List> student_list = new ArrayList<List>();

		try {	//Run until IOException or BindException
			//Initalize server socket to port 16789
			ss = new ServerSocket(16789);

			//allocate memory for client socket
			Socket cs = null;

			//Print server information to terminal
			System.out.println("The server has been started");
			System.out.println("getLocalHost: "+InetAddress.getLocalHost() );
			System.out.println("getByName:    "+InetAddress.getByName("localhost") + "\n" );
			System.out.println("Waiting for client connection....");

			//Continually loop waiting for connections
			//Terminates when program is terminated or an exception occurs
			while(true){
				// wait for connection
				cs = ss.accept(); //NOTE - Method blocks main thread until a connection is established

				//Announce a connection of a client
				System.out.println("A new client has been connected");

				//Create a thread for the new client and begin thread
				ThreadServer ths = new ThreadServer( cs, student_list);
				ths.start();
			} // end while
		}
		catch( BindException be ) {	//Catch and handle BindExceptions
			System.out.println("Server already running on this computer, stopping."); //Notify user of exception
		}
		catch( IOException ioe ) { //Catch and handle IOExceptions
			System.out.println("IO Error");	//Notify user of exception
			ioe.printStackTrace();			//Print stack trace for debugging and error information
		}

	} // end constructor

	/*
		Class: 			ThreadServer
		Functionality:	Receives student grade data from clients. Calculates average exam score. Exam averages are
						stored internally and transmitted to the client. MyServer is able to handle multiple clients.
		Parent:			MyServer (inner Class of MyServer)
		Children: 		None
		Dependencies: 	None
 	 */
	class ThreadServer extends Thread {  // member inner class
		Socket cs;					//Client socket
		List<List> student_list;	//List of students

		/*
			Method:  		ThreadServer - Constructor
			Arguments: 		cs - client socket
							student_list - list of students
			Returns:		None
			Preconditions: 	Socket cs is a valid connection
			Functionality:	Creates a ThreadServer object
		 */
		public ThreadServer( Socket cs, List<List> student_list) {
			this.cs = cs;
			this.student_list = student_list;
		}

		/*
			Method:  		run
			Arguments: 		None
			Returns:		void
			Preconditions: 	Message is transmitted in format "NAME,GRADE1,GRADE2,GRADE3"
			Functionality:	Handles client transmission and returns exam average
		 */
		public void run() {
			//Initialize and allocate variables for operation
			BufferedReader br;
			PrintWriter opw;
			String clientMsg;			//Message from client
			int[] exams = new int[3];	//3 -> # of exam grades

			try {	//Run until IOException
				//Initialize buffer reader and print writer for server use
				br = new BufferedReader(
						new InputStreamReader(
								cs.getInputStream()));
				opw = new PrintWriter(
						new OutputStreamWriter(
								cs.getOutputStream()));

				//Retrieve transmission string from client and echo to terminal
				clientMsg = br.readLine();
				System.out.println("Received from client: "+clientMsg);

				//Tokenize string using ',' as a delimiter
				String[] temp = clientMsg.split(",");
				String student_name = temp[0];	//Set student name to token zero
				for (int i = 0; i<3 ; i++){	//Parse each token into a usable integer
					exams[i] = Integer.parseInt(temp[i+1]);
				}

				//Calculate average of the student exam grades and trasmit average to client
				System.out.print("Calculating the average-----");
				double average = calculate_average(exams);	//call synchronized function
				System.out.println(average);
				add_to_list(student_name, average);			//Add student name and average to server's list
				opw.println(student_name+ ", your average is  " + average);	//Send average to client
				opw.flush();
			}
			catch( IOException ioe ) { //Catch and handle IOExceptions
				System.out.println("IO Error");	//Notify user of exception
				ioe.printStackTrace();			//Print stack trace for debugging and error information
			}
		} // end while

		/*
			Method:  		calculate_average
			Arguments: 		exams - array of exam grades stored as integers
			Returns:		average exam grade
			Preconditions: 	exams is an array with exactly 3 elements
			Functionality:	Calculates the average grade based on the three exam grades
		 */
		public double calculate_average(int[] exams){
			//Returns the average of the 3 exam grades
			return (exams[0]+exams[1]+exams[2]) / 3.0;
		}

		/*
			Method:  		add_to_list
			Arguments: 		student_name - String containing the name of the student
							average - average exam grade
			Returns:		void
			Preconditions: 	None
			Functionality:	Stores the student and their average into the static list of student grades
			Synchronized:	Only one client can edit at a time
		 */
		public synchronized void add_to_list (String student_name, double average){
			synchronized (student_list) {	//Synchronize on student_list to prevent data corruption
				ArrayList<Serializable> temp_list = new ArrayList<>();	//Create temporary list
				temp_list.add(student_name);							//Add student name
				temp_list.add(average);									//Add student average
				student_list.add(temp_list);							//Update student list with new entry
				System.out.println("Updated student list:");			//Print status message to terminal
				for (List row : student_list) {	//Print entire student list to terminal
					System.out.println(row.get(0) + "," + row.get(1));
				}

			}
		}
	} // end class ThreadServer
} // end MyMultiThreadedServer