package ServerSide;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Vector;

public class BroadcastMultiClientServer {
	static Vector<SocketData> allConnections = new Vector<SocketData>();

	public static void main(String[] args) throws IOException {

		ServerSocket server = new ServerSocket(7000);

		System.out.println(new Date() + " --> Server waits for clients...");
		while (true) {
			final Socket socket = server.accept(); // blocking

			new Thread(new Runnable() {
				@Override
				public void run() {
					SocketData currentSocketData = new SocketData(socket);
					allConnections.add(currentSocketData);
					System.out.println(new Date()
							+ " --> Client connected from "
							+ currentSocketData.getClientAddress());

					currentSocketData.getOutputStream().println("Welcome to server!");

					String line = "";
					try {
						while (!line.equals("goodbye")) {
							line = currentSocketData.getInputStream().readLine();
							sendBroadcastMessage(line, currentSocketData);
							System.out.println(new Date()
									+ " --> Recieved from client "
									+ currentSocketData.getClientAddress()
									+ ": " + line);
						}
					} catch (Exception e) {
						System.err.println(e);
					} finally {
						try {
							socket.close();
							System.out.println("The client from "
									+ currentSocketData.getClientAddress()
									+ " is disconnected");
						} catch (IOException e) { // log and ignore
						}
					}
				} // run
			}).start();
		} // while
	} // main

	public static void sendBroadcastMessage(String theMessage,
			SocketData theSender) {
		for (SocketData sd : allConnections)
			if (sd.getSocket().isConnected()) {
				sd.getOutputStream().println(
						theSender.getClientAddress() + "@" + theMessage);
			}
	}
} // class BroadcastMultiClientServer
