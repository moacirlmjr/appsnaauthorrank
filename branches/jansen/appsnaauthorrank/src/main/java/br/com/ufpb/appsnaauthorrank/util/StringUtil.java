package br.com.ufpb.appsnaauthorrank.util;

public class StringUtil {
	
	public static String stringProcessing(String anterior){
		String nova;
		nova = anterior.replace("\r", "$").replace("\n", "$").replace(";", "$");
		return nova;
	}
	
	public static boolean wordVerification(String word){
		
		if(word==null || word==""){
			return false;
		}
		return true;
		
	}
	
	public static String tratarCitacao(String citacao){
		return citacao.split(
				"  Abstract | Full Text: PDF")[0]
				.replace("[CrossRef]", "").replace("<", "")
				.replace(">", "").replace(", (unpublished)", "");
	}

}
