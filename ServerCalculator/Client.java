import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A client program for a server/client style calculator.
 */

public class Client {
	

	/**
	 * Serves as a message carrier between client and server.
	 * If the server responds on the call, we print out the received answer.
	 * @param out
	 * @param in
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	public static boolean sendMessage(PrintWriter out, BufferedReader in, String msg) throws IOException {
		out.println(msg);
		String answer = in.readLine();
		if (answer.equals("Recieved")) {
			System.out.println("Server is doing the calculation");
			System.out.println(in.readLine());
			return true;
		} else {
			System.out.println("Server not responding...");
			return false;
		}
		
	}

	/**
	 * A repl style message loop, reads an expression from stdIn, writes it to out and
	 * prints the answer from in, until the user presses Ctrl-D.
	 * @param stdIn
	 * @param out
	 * @param in
	 */
	public static void msgLoop(BufferedReader stdIn, PrintWriter out, BufferedReader in) {
		try {
			String userInput = stdIn.readLine();
			while (userInput != null && userInput.length() > 0) {
				if (sendMessage(out, in, userInput)) {
					userInput = stdIn.readLine();
				}
			}
		} catch (IOException e) {
			System.err.println("Unknown IOException while communicating with server");
			e.printStackTrace();
		}
	}

	/**
	 * args[0] is the host address, defaults to 127.0.0.1 if not supplied.
	 * args[1] is an (optional) expression to send to the server, defaults to interactive if not supplied.
	 * @param args
	 */
	public static void main(String[] args) {
		Inet4Address address = null;
		String host = null;
		if (args.length > 0) {
			host = args[0];
		} else {
			host = "127.0.0.1";
		}
		try {
			address = (Inet4Address) Inet4Address.getByName(host);
		} catch (UnknownHostException e) {
			System.err.println("Err: Could not connect to host '" + host + "'. Unknown host");
			return;
		}
		try {
			Socket socket = new Socket(address, 1337);
			socket.setSoTimeout(1000);

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			if (args.length > 1) {
				sendMessage(out, in, args[1]);
			} else {
				msgLoop(new BufferedReader(new InputStreamReader(System.in)), out, in);
			}
			socket.close();
		} catch (ConnectException e) {
			System.err.println("Host " + host + " refused connection on port " + 1337 + ".");
		} catch (IOException e) {
			System.err.println("Unknown IOException");
			e.printStackTrace();
		}
	}
}
