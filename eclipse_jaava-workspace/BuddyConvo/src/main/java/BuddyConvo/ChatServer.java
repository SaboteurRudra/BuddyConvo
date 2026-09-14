package BuddyConvo;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	private static final int PORT = 5000;
	private static final Set<Socket> clients =
			Collections.synchronizedSet(new HashSet<>());
	public static void main(String[] args) {
		System.out.println("BuddyChat SERVER STARTED...");
		try (ServerSocket serverSocket =
				new ServerSocket(PORT)){
			while (true) {
				Socket socket = serverSocket.accept();
				clients.add(socket);
				System.out.println("New client connected!");
				new Thread(() -> {
					try {
						BufferedReader input = new BufferedReader(
								new InputStreamReader(socket.getInputStream()));
						String message;
						while ((message = input.readLine()) !=null) {
							System.out.println("Message: "+ message);
							synchronized (clients) {
								for (Socket client : clients) {
									PrintWriter output =
											new PrintWriter(
													client.getOutputStream(),true);
									output.println(message);
								}
							}
						}
					}catch (IOException e) {
						System.out.println(
								"Client disconnected.");
					}finally {
						clients.remove(socket);
						try {
							socket.close();
						}catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
			
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
