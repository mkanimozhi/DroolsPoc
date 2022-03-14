package com.drools.poc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.drools.poc.constants.DroolsPocServiceConstants;

public class DroolsPocServiceUtil {

	public static void writeToFile(String drl) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(DroolsPocServiceConstants.DRL_FILE_PATH));
		writer.write(drl);
		writer.close();
	}
	
	public static byte[] readFromFile() throws FileNotFoundException, IOException {
		String drlFileStr = "";
		File file = new File(DroolsPocServiceConstants.DRL_FILE_PATH);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		StringBuilder linebuilder = new StringBuilder();
		String line;
		while((line = br.readLine()) != null){
			linebuilder.append(line);
			linebuilder.append("\n");
		}
		drlFileStr = linebuilder.toString();
		br.close();

		byte[] readContents = drlFileStr.getBytes();
		return readContents;
	}
	
}
