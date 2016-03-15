import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.HashMap;

public class Server {

	/**
	 * Logs the inetAddress and amount of connections established of each client.
	 * @param string
	 * @param clients
	 */
	public static void loggAdress(String string, HashMap<String, Integer> clients) {

		if (clients.containsKey(string)) {
			clients.put(string, clients.get(string) + 1);
		} else if (!clients.containsKey(string)) {
			clients.put(string, 1);
		}
	}

	/**
	 * Creates a server socket and handles incoming connections.
	 * @param args
	 */
	public static void main(String[] args) {

		HashMap<String, Integer> clients = new HashMap<String, Integer>();

		ServerSocket socket = null;
		Socket cl = null;
		try {

			socket = new ServerSocket(1337);

			while (true) {

				cl = socket.accept();

				//System.out.println("Connection from : " + cl.getInetAddress().getHostAddress() + ':' + cl.getPort());

				loggAdress(cl.getInetAddress().getHostAddress(), clients);

				// New thread for each new connection.
				new clientThread(cl, clients.get(cl.getInetAddress().getHostAddress())).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException ie) {
			System.err.println("Cannot close socket for some reason." + ie);
		}

	}

}

/**
 * Thread class responsible for connecting each client on a separate thread.
 * Handles arithmetic complexity cost of each requested expression delivered.
 */
class clientThread extends Thread {

	public static final double multiplyCost = 30;
	public static final double divideCost = 50;
	public static final double addCost = 5;
	public static final double subtractCost = 10;

	private Socket socket;
	private int connectionTimes; // @Name

	clientThread(Socket cl, int connectionTimes) {
		this.socket = cl;
		this.connectionTimes = connectionTimes;
	}

	private int countOccurencesInString(String str, char c) {
		int num = 0;
		for (int i = 0; i < str.length(); ++i) {
			if (str.charAt(i) == c) {
				num++;
			}
		}
		return num;
	}

	public void run() {

		BufferedReader in = null;
		PrintWriter out = null;

		try {

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			String inputLine = "";

			inputLine = in.readLine();

			while (inputLine != null) {

				out.println("Recieved");

				try {
					double resultOfCalculation = Expression.calc(inputLine);
					double cost = addCost * countOccurencesInString(inputLine, '+')
							+ multiplyCost * countOccurencesInString(inputLine, '*')
							+ subtractCost * countOccurencesInString(inputLine, '-')
							+ divideCost * countOccurencesInString(inputLine, '/');
					double quantityDiscount = 1.0 / (connectionTimes - 5 < 1 ? 1 : connectionTimes - 5);
					out.println("Calculating: " + resultOfCalculation + ". That costs "
							+ (int) ((cost * quantityDiscount) + 0.5) + " bits. You saved "
							+ (int) ((cost - cost * quantityDiscount) + 0.5) + " bits in quantity discount");
				} catch (ParseException e) {
					out.println("Failed to parse '" + inputLine + "' due to \"" + e + "\". Free of charge.");
				}
				inputLine = in.readLine();

			}
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
