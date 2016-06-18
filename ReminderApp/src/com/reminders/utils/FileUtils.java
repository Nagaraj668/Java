package com.reminders.utils;

import java.io.File;

public class FileUtils {

	public boolean areFilesAvailable(String path) {
		boolean flag = false;
		try {
			File folder = new File(path);
			if (folder != null)
				if (folder.list().length > 0)
					flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

}
