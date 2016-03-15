import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;

/**
 * A Thread serving a request from a client in a server/client style calculator.
 * 
 * @author holger
 *
 */
public class ServeThread extends Thread {
	public static final double multiplyCost = 30;
	public static final double divideCost = 50;
	public static final double addCost = 5;
	public static final double subtractCost = 10;
	private Socket socket;
	private int connectionTimes; // @Name

	/**
	 * Create a ServeThread from the socket making the request as well as the
	 * number of times this client has connected.
	 * 
	 * @param cl
	 * @param connectionTimes
	 */
	public ServeThread(Socket cl, int connectionTimes) {
		this.socket = cl;
		this.connectionTimes = connectionTimes;
	}

	/**
	 * Count the number of times a char occures in a string.
	 * 
	 * @param str
	 * @param c
	 * @return
	 */
	private int countOccurencesInString(String str, char c) {
		int num = 0;
		for (int i = 0; i < str.length(); ++i) {
			if (str.charAt(i) == c) {
				num++;
			}
		}
		return num;
	}

	/**
	 * Serves the clients request. Overrides the threads run method. This method
	 * is the entry point for the new thread.
	 */
	@Override
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
