package BuddyConvo;
import javax.swing.*;
import java.awt.*;

public class TimeCapsuleUI extends JFrame{
	JTextArea messageArea;
	JButton saveButton;
	
	public TimeCapsuleUI() {
		setTitle("TIME CAPSULE");
		setSize(600, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		Color background = new Color(18, 18, 28);
		Color panelColor = new Color(30, 30, 45);
		Color textColor = new Color(240, 240, 255);
		Color buttonColor = new Color(120, 80, 255);
		
		messageArea = new JTextArea();
		messageArea.setBackground(background);
		messageArea.setForeground(textColor);
		messageArea.setCaretColor(textColor);
		messageArea.setFont(new Font("Arial", Font.PLAIN, 16));
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);
		
		saveButton = new JButton("SAVE TIME CAPSULE..");
		saveButton.setBackground(buttonColor);
		saveButton.setForeground(Color.WHITE);
		saveButton.setFocusPainted(false);
		
		JPanel panel = new JPanel(new java.awt.BorderLayout());
		panel.setBackground(panelColor);
		panel.add(
				new JScrollPane(messageArea),
				BorderLayout.CENTER);
		panel.add(
				saveButton,
				BorderLayout.SOUTH);
		add(panel);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() ->
		        new TimeCapsuleUI()
		);
	}
}
