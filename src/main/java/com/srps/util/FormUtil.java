package com.srps.util;

import java.util.List;
import java.util.UUID;

public class FormUtil {

	// used for testing purpose
	public static String prepareFormList(String forms) {
		StringBuilder output = new StringBuilder();

		output.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		output.append("<forms>");
		output.append("<form url=\"http://localhost:8080/SRPS/odk/form?name=abc\">abc</form>\n");
		output.append("<form url=\"http://localhost:8080/SRPS/odk/form?name=123\">123</form>\n");
		output.append("<form url=\"http://localhost:8080/SRPS/odk/form?name=xyz\">xyz</form>\n");
		output.append("<form url=\"http://localhost:8080/SRPS/odk/form?name=456\">456</form>\n");
		output.append("</forms>");

		return output.toString();
	}

	// build xml representation of available forms
	public static String compileFormList(List<String> forms) {

		StringBuilder output = new StringBuilder();

		output.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		output.append("<forms>");

		for (String form : forms) {
			output.append("<form url=\"http://192.168.56.101:8443/SRPS/mobile/download?formName="
					+ form + "\"> " + form + "</form>\n");
		}

		output.append("</forms>");

		System.out.println("output : " + output);

		return output.toString();

	}

	public static String escapeEmptySpaces(String filename, int formId) {
		StringBuilder modifiedFilename = new StringBuilder();
		for (int i = 0; i < filename.length(); i++) {
			if (filename.charAt(i) == '.') {
				modifiedFilename.append("_" + formId + ".");
				continue;
			}
			if (filename.charAt(i) == ' ') {
				modifiedFilename.append("_");
			} else {
				modifiedFilename.append(filename.charAt(i));
			}
		}
		return modifiedFilename.toString();
	}
	
	public static String generateSubmissionId() { 
		UUID uuid = UUID.randomUUID(); 
		return uuid.toString(); 
	}
}
