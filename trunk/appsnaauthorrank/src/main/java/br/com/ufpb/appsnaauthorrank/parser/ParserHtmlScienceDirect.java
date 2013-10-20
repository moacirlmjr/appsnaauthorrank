package br.com.ufpb.appsnaauthorrank.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;

public class ParserHtmlScienceDirect {

	private static final String ARTIGO = "detail";
	private static final String LIST_ARTIGOS = "header";
	private static final String SEARCH_RESULTS = "bodyMainResults";

	public static List<Artigo> realizarParserHtml(File diretorio)
			throws Exception {

		List<Artigo> artigos = new ArrayList<Artigo>();
		if (diretorio.isDirectory()) {
			for (File html : diretorio.listFiles()) {
				if (html.getName().contains("htm")) {
					Document doc = Jsoup.parse(html, "UTF-8", "localhost");
					for (Element element : doc.select("div")) {
						if (element.attr("id").equalsIgnoreCase(SEARCH_RESULTS)) {
							for (Element children : element.children()) {
								Artigo artigo = new Artigo();
								artigo.setOrigem(html.getName());
								if (children.attr("class").equals("resultRow")) {
									artigo.setTitulo(children.getElementsByClass(
									"cLink").text());
									try{
										artigo.setAutores(children.toString().split("\n")[13].split("<br />")[2]);
									}catch (Exception e2) {
										artigo.setAutores("Sem Autor");
									}
									int count = 1;
									for(Element linkPdf : children.getElementsByAttributeValue("target", "newPdfWin")){
										artigo.setLinkDownload(linkPdf.attr("href"));
									}
									if(artigo.getLinkDownload() == null || artigo.getLinkDownload().equals("")){
										System.out.println();
										artigo.setLinkDownload("sem Link para download");
									}
									for(Element component : children.getElementsByTag("i")){
										if(count == 1){
											artigo.setOndePub(component.text());
										}else if(count == 3){
											if(component.text().split(" ").length >= 2){
												artigo.setPubYear(component.text().split(" ")[1]);
											}else{
												artigo.setPubYear(component.text());
											}
										}
										count++;
									}
									artigos.add(artigo);
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
