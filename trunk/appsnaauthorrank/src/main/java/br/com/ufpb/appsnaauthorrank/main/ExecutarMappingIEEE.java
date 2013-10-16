package br.com.ufpb.appsnaauthorrank.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.ufpb.mapSis.beans.Artigo;
import br.com.ufpb.mapSis.integracao.post.postGoogleForm;
import br.com.ufpb.mapSis.parser.ParserHtmlIEEE;

public class ExecutarMappingIEEE {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		File input = new File("htmls");
		try {
			List<Artigo> artigos = ParserHtmlIEEE.realizarParserHtml(input);
			
			for(Artigo a: artigos){
				postGoogleForm.post(a, "IEEE Explorer");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
