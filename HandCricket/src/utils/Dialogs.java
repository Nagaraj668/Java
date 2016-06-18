package utils;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Dialogs {

	public static void alert(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

	public static boolean confirm(String message) {
		boolean flag = false;
		if (JOptionPane.showConfirmDialog(null, message) == 0)
			flag = true;
		return flag;
	}

	public static String showDropdown(String message, String title, String[] choices, JFrame frame) {
		String s = (String) JOptionPane.showInputDialog(frame, message, title, JOptionPane.PLAIN_MESSAGE, null, choices,
				choices[0]);
		return s;
	}

}
