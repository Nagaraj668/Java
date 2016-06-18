package com.reminders.main;

import com.reminders.frame.ReminderFrame;
import com.reminders.utils.Constants;

public class ReminderMain {

	public static void main(String[] args) {
		ReminderFrame frame = new ReminderFrame(Constants.APP_NAME);
		frame.showFrame();
	}

}
