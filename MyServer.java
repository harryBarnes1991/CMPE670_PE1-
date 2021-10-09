import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MyServer{
	public static void main(String [] args) {
		new MyServer();
	}

	public MyServer()
	{
		ServerSocket ss = null;

		List<List> student_list = new ArrayList<List>(); //2D list for storing student name and their
		//average

		try {
			ss = new ServerSocket(16789);
			Socket cs = null;
			System.out.println("getLocalHost: "+InetAddress.getLocalHost() );
			System.out.println("getByName:    "+InetAddress.getByName("localhost") + "\n" );
			while(true){
				cs = ss.accept(); 				// wait for connection
				System.out.println("A new client has been connected");
				ThreadServer ths = new ThreadServer( cs, student_list);
				ths.start();
			} // end while
		}
		catch( BindException be ) {
			System.out.println("Server already running on this computer, stopping.");
		}
		catch( IOException ioe ) {
			System.out.println("IO Error");
			ioe.printStackTrace();
		}

	} // end constructor

	class ThreadServer extends Thread {  // member inner class
		Socket cs;
		List<List> student_list;

		public ThreadServer( Socket cs, List<List> student_list) {
			this.cs = cs;
			this.student_list = student_list;
		}

		public void run() {
			BufferedReader br;
			PrintWriter opw;
			String clientMsg;
			int[] exams = new int[3];

			try {
				br = new BufferedReader(
						new InputStreamReader(
								cs.getInputStream()));
				opw = new PrintWriter(
						new OutputStreamWriter(
								cs.getOutputStream()));

				clientMsg = br.readLine();					// from client
				System.out.println("Received from client: "+clientMsg);
				String[] temp = clientMsg.split(",");
				String student_name = temp[0];
				for (int i = 0; i<3 ; i++){
					exams[i] = Integer.parseInt(temp[i+1]);
				}
				System.out.print("Calculating the average-----");
				double average = calculate_average(exams);
				System.out.println(average);
				add_to_list(student_name, average);
				opw.println(student_name+ ", your average is  " + average);	//to client
				opw.flush();
			}
			catch( IOException e ) {
				System.out.println("Inside catch");
				e.printStackTrace();
			}
		} // end while


		public double calculate_average(int[] exams){

			return (exams[0]+exams[1]+exams[2]) / 3.0;
		}

		public synchronized void add_to_list (String student_name, double average){

			synchronized (student_list) {

				ArrayList<Serializable> temp_list = new ArrayList<>();
				temp_list.add(student_name);
				temp_list.add(average);
				student_list.add(temp_list);
				System.out.println("Updated student list:");
				for (List row : student_list) {
					System.out.println(row.get(0) + "," + row.get(1));
				}

			}
		}






	} // end class ThreadServer
} // end MyMultiThreadedServer