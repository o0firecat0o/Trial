package engine.network.jPanel;

import java.awt.event.*;

import javax.swing.*;

public class JPanelNetworkIPInputField {
	public IJPanelNetworkIPInputField hookedInterface;

	public JPanelNetworkIPInputField() {
		JFrame frame = new JFrame("Test");
		frame.setVisible(true);
		frame.setSize(800, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel label = new JLabel("Please enter the target IP adress below and press enter");
		JPanel panel = new JPanel();
		frame.add(panel);
		panel.add(label);

		final JTextField input = new JTextField(24); // The input field with a
														// width of 5 columns
		panel.add(input);

		JButton button = new JButton("Enter");
		panel.add(button);

		button.addActionListener(new ActionListener() { // The action listener
														// which notices when
														// the button is pressed
			@Override
			public void actionPerformed(ActionEvent e) {
				hookedInterface.run(input.getText());
				frame.dispose();
			}
		});
	}
}
