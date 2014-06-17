package br.com.ufpb.appsnaauthorrank.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ParserCSVInstituicao {
	
	public static String getRGBByInstituicao(String instituicao) throws IOException{
		
		FileReader fr = new FileReader("Instituições e RGB.csv");
		BufferedReader in = new BufferedReader(fr);
		String line;
		while ((line = in.readLine()) != null) {
			if(line.contains(instituicao)){
				return line;
			}
		}
				
		return "";
	}

}
