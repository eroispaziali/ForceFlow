package com.spaceheroes.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	public static final String FORMAT_WITH_SLASH = "%s//%s";
	public static final String FORMAT_WITHOUT_SLASH = "%s%s";
	
	public static String getCorrectFolderPath(String pPath, String pFolder){
		return String.format(pPath.endsWith("/") ? FORMAT_WITHOUT_SLASH : FORMAT_WITH_SLASH, pPath, pFolder);
	}
	
	public static List<String> listFilesForFolder(final File folder) {
		List<String> fileNameToReturn = new ArrayList<String>();
	    for (final File fileEntry : folder.listFiles()) {
	        if (!fileEntry.isDirectory()) {
	            fileNameToReturn.add(fileEntry.getName());
	        }
	    }
		return fileNameToReturn;
	}

}
