package engine.network.jPanel;

import java.awt.event.*;

import javax.swing.*;

public class JPanelNetworkSelectServerClient {

	public IJPanelNetworkSelectServerClient hookedInterface;

	public JPanelNetworkSelectServerClient() {
		JFrame frame = new JFrame("Test");
		frame.setVisible(true);
		frame.setSize(500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel label = new JLabel("Are you are Server or a Client?");
		JPanel panel = new JPanel();
		frame.add(panel);
		panel.add(label);

		JButton button = new JButton("Server");
		panel.add(button);

		JButton button2 = new JButton("Client");
		panel.add(button2);

		// send true to the interface if is server
		button.addActionListener(new ActionListener() { // The action listener
														// which notices when
														// the button is pressed
			@Override
			public void actionPerformed(ActionEvent e) {
				hookedInterface.run(true);
				frame.dispose();
			}
		});

		// send false to the interface if is client
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hookedInterface.run(false);
				frame.dispose();
			}
		});
	}

}
