package br.com.ufpb.appsnaauthorrank.main;

import java.io.File;
import java.util.List;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;
import br.com.ufpb.appsnaauthorrank.parser.ParserHtmlIEEE;

public class ExecutarMappingIEEE {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		File input = new File("htmls");
		try {
			List<Artigo> artigos = ParserHtmlIEEE.realizarParserHtml(input);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
