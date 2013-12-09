package br.com.ufpb.appsnaauthorrank.main;

import java.io.File;
import java.util.List;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;
import br.com.ufpb.appsnaauthorrank.parser.ParserHtmlIEEE;
import br.com.ufpb.appsnaauthorrank.parser.ParserHtmlIEEEDetalhe;
import br.com.ufpb.appsnaauthorrank.post.postIeeeForm;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExecutarMappingIEEE {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        try {
            List<Artigo> artigos = new ArrayList<Artigo>();
            List<Artigo> tempArtigos = new ArrayList<Artigo>();
            int contador = 1;
            
            while(tempArtigos != null){
                tempArtigos = ParserHtmlIEEE.realizarParserHtml(postIeeeForm.post("\"social network analysis\" and online",contador));
                if(tempArtigos!=null)
                    artigos.addAll(tempArtigos);
                contador++;
            }
            
           //obter as referencias e atualizar artigos
           for(Artigo a : artigos){
               a = ParserHtmlIEEEDetalhe.realizarParserHtml(a);
           }
            
           for(Artigo a : artigos){
                if(a.getAutores().isEmpty()){
                    System.out.println("artigo nulo: "+a.getTitulo());
                }
            }
           System.out.println("Quantidade de artigos: "+artigos.size());
           
        } catch (Exception ex) {
            ex.printStackTrace();
        }

	}

}
