package com.srps.util;

import java.io.File;

public class FileUtil {
	
	public static void deleteDirectory(File directory) { 
		File[] files = directory.listFiles(); 
		if(files != null) { 
			for(File f: files) { 
				if(f.isDirectory()) { 
					deleteDirectory(f); 
				} else { 
					f.delete(); 
				}
			}
		}
		directory.delete(); 
	}
}
