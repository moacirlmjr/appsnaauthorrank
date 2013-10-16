package br.com.ufpb.appsnaauthorrank.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import br.com.ufpb.mapSis.beans.Artigo;

public class ParserHtmlACM {

	private static final String ARTIGO = "detail";
	private static final String LIST_ARTIGOS = "header";
	private static final String SEARCH_RESULTS = "top";

	public static List<Artigo> realizarParserHtml(File diretorio)
			throws Exception {

		List<Artigo> artigos = new ArrayList<Artigo>();
		if (diretorio.isDirectory()) {
			for (File html : diretorio.listFiles()) {
				if (html.getName().contains("htm")) {
					Document doc = Jsoup.parse(html, "UTF-8", "localhost");
					for (Element e : doc.select("tr")) {
						if (e.attr("valign").equalsIgnoreCase(SEARCH_RESULTS)) {
							Artigo artigo = new Artigo();
							boolean continuar = false;
							for (Element children : e.children()) {
								if (children.attr("class").equals("small-text")
										&& children
												.attr("style")
												.equals("padding-left: 5px; padding-top: 10px;")
										&& children.attr("align").equals(
												"center")) {
									continuar = true;
								}
							}

							if (continuar) {
								artigo.setTitulo(e.getElementsByClass(
										"medium-text").text());
								artigo.setAutores(e.getElementsByClass(
										"authors").text());
								artigo.setOndePub(e.getElementsByClass(
										"addinfo").text());
								for (Element subChildren : e
										.getElementsByClass("small-text")) {
									if (subChildren.hasAttr("nowrap")) {
										artigo.setPubYear(subChildren.text()
												.split(" ") != null ? subChildren
												.text().split(" ")[1] : "");
										break;
									}
								}
								if (!e.getElementsByAttributeValue("title",
										"PDF").attr("href").equals("")) {
									artigo.setLinkDownload(e
											.getElementsByAttributeValue(
													"title", "PDF")
											.attr("href"));
								} else if (!e
										.getElementsByAttributeValue("title",
												"Publisher Site").attr("href")
										.equals("")) {
									artigo.setLinkDownload(e
											.getElementsByAttributeValue(
													"title", "Publisher Site")
											.attr("href"));
								} else if (!e
										.getElementsByAttributeValue("title",
												"Mov").attr("href").equals("")) {
									artigo.setLinkDownload(e
											.getElementsByAttributeValue(
													"title", "Mov")
											.attr("href"));
								} else {
									artigo.setLinkDownload("Sem Link para Download");
								}
								artigos.add(artigo);
							}

						}
					}
				}
			}
		}

		return artigos;
	}

}
