package BuddyConvo; 
import javax.swing.*; 
import java.awt.*; 
import java.io.*; 
import java.net.*; 
import java.time.LocalDateTime; 

public class ChatClient extends JFrame{ 
	JTextArea chatArea; 
	JTextField messageField; 
	JButton sendButton; 
	Socket socket; 
	BufferedReader input; 
	PrintWriter output; 
	 
	public ChatClient() { 
		setTitle("BUDDY CHAT"); 
		setSize(600,500); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		Color background = new Color(18,18,28); 
		Color panelColor = new Color(30,30,45); 
		Color textColor = new Color(240,240,255); 
		Color buttonColor = new Color(120,80,255); 
		 
		chatArea = new JTextArea(); 
		chatArea.setEditable(false); 
		chatArea.setBackground(background); 
		chatArea.setForeground(textColor); 
		chatArea.setFont(new Font("Arial", Font.PLAIN, 15)); 
		 
		messageField = new JTextField(); 
		messageField.setBackground(panelColor); 
		messageField.setForeground(textColor); 
		messageField.setCaretColor(textColor); 
		messageField.setFont(new Font("Arial", Font.PLAIN, 15)); 
		 
		sendButton = new JButton("Send"); 
		sendButton.setBackground(buttonColor); 
		sendButton.setForeground(Color.WHITE); 
		sendButton.setFocusPainted(false); 
	    
	    JButton capsuleButton = new JButton("Time Capsule"); 
	    capsuleButton.setBackground(buttonColor); 
	    capsuleButton.setForeground(Color.WHITE); 
	    capsuleButton.setFocusPainted(false); 
	   
		JPanel bottom = new JPanel( 
				new java.awt.BorderLayout() 
		); 
		
		bottom.setBackground(panelColor); 
		bottom.add(messageField,java.awt.BorderLayout.CENTER); 
		bottom.add(sendButton,java.awt.BorderLayout.EAST); 
		
		add(new JScrollPane(chatArea),java.awt.BorderLayout.CENTER); 
		add(bottom,java.awt.BorderLayout.SOUTH); 
		
		sendButton.addActionListener(e -> 
		        sendMessage() 
		); 
		capsuleButton.addActionListener(e -> { 
			
			String msg = JOptionPane.showInputDialog(
					this, "Enter future message:"
			); 
			if (msg==null || msg.isEmpty()) { 
				return; 
			} 
			String date = JOptionPane.showInputDialog(
					this, "Unlock date (YYYY-MM-DD):"
			); 
			if (date == null || date.isEmpty()) { 
				return; 
			} 
			String time = JOptionPane.showInputDialog(
					this, "Unlock time (HH:MM):"
			); 
			if (time == null || time.isEmpty()) { 
				return; 
			} 
			try { 
				LocalDateTime unlock = 
						LocalDateTime.parse(date + "T" + time); 
				
				TimeCapsule capsule = 
						new TimeCapsule(msg, unlock); 
				
				chatArea.append("Time Capsule created\n"); 
				chatArea.append("Unlocks:" + unlock + "\n"); 

			}catch(Exception ex) { 
				JOptionPane.showMessageDialog(
						this, "Invalid date/Time!"
				); 
			} 
		}); 
		connectToServer(); 
		setLocationRelativeTo(null); 
		setVisible(true); 
	} 
	private void connectToServer() { 
		try { 
			socket = new Socket("localhost",5000); 
			
			input = new BufferedReader( 
					new InputStreamReader( 
							socket.getInputStream() 
					) 
			); 
			
			output = new PrintWriter( 
					socket.getOutputStream(), 
					true 
			); 	
			new Thread(new Runnable() { 
				public void run() { 
					try { 
						String message; 
						
						while ((message = input.readLine()) != null) { 
							chatArea.append(message + "\n"); 
						} 
						
					}catch (Exception e) { 
						SwingUtilities.invokeLater(() -> 
							chatArea.append("Disconnected\n") 
						); 
					} 
				} 
			}).start(); 
			
		}catch(Exception e) { 
			JOptionPane.showMessageDialog( 
					this, "Server not running!"
			); 
		} 
	} 
	private void sendMessage() { 
		String message = messageField.getText(); 
		
		if (!message.isEmpty()) { 
			if(output != null) { 
				output.println(message); 
				chatArea.append("You: " + message + "\n"); 
				messageField.setText(""); 
			}else { 
				JOptionPane.showMessageDialog( 
						this, "Not connected to server"
				); 
			} 
		} 
	} 
	public static void main(String[] args) { 
		SwingUtilities.invokeLater(() -> 
		        new ChatClient() 
		); 
	} 
}