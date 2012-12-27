package com.srps.util;

public class FormUtil {
	
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
	
	
}
