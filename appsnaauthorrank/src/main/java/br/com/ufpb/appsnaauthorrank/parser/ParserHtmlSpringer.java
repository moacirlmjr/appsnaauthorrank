package br.com.ufpb.appsnaauthorrank.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;

public class ParserHtmlSpringer {

	private static final String ARTIGO = "detail";
	private static final String LIST_ARTIGOS = "header";
	private static final String SEARCH_RESULTS = "results";
	private static final String SEARCH_RESULTS_CLASS = "content-item-list";

	public static List<Artigo> realizarParserHtml(File diretorio)
			throws Exception {

		List<Artigo> artigos = new ArrayList<Artigo>();
		if (diretorio.isDirectory()) {
			for (File html : diretorio.listFiles()) {
				if (html.getName().contains("htm")) {
					Document doc = Jsoup.parse(html, "UTF-8", "localhost");
					for (Element resultsDiv : doc.select("div")) {
						if(resultsDiv.attr("id").equals(SEARCH_RESULTS)){
							for(Element subResult: resultsDiv.getElementsByTag("ol")){
								if(subResult.attr("id").equals("results-list")){
									for(Element resultsList : subResult.getElementsByAttributeValue("id", "results-list")){
										for(Element result : resultsList.children()){
												Artigo artigo = new Artigo();
												artigo.setTitulo(result.getElementsByAttributeValue("class", "title").text());
												artigo.setAutores(result.getElementsByAttributeValue("class", "authors").text());
												artigo.setOndePub(result.getElementsByAttributeValue("class", "publication-title").text());
												if(result.getElementsByAttributeValue("class", "year").attr("title").split(" ").length > 1){
													artigo.setPubYear(result.getElementsByAttributeValue("class", "year").attr("title").split(" ")[1]);	
												}else if(result.getElementsByAttributeValue("class", "year").attr("title").split(" ").length == 1){
													artigo.setPubYear(result.getElementsByAttributeValue("class", "year").attr("title").split(" ")[0]);
												}
												if(result.getElementsByAttributeValue("class", "webtrekk-track pdf-link").size() != 0){
													artigo.setLinkDownload("http://link.springer.com" + result.getElementsByAttributeValue("class", "webtrekk-track pdf-link").attr("href"));
												}else if(result.getElementsByAttributeValue("class", "lookinside-href webtrekk-track").size() != 0){
													artigo.setLinkDownload("http://link.springer.com" + result.getElementsByAttributeValue("class", "title").attr("href"));
												} else {
													artigo.setLinkDownload("Sem link para download");
												} 
												artigos.add(artigo);
											}
										}
								}
								
							}
							
						}
					}
				}
			}
		}

		return artigos;
	}

}
