package br.com.ufpb.appsnaauthorrank.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;
import br.com.ufpb.appsnaauthorrank.beans.Autor;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.select.Elements;

public class ParserHtmlIEEE {

	private static final String ARTIGO = "detail";
	private static final String LIST_ARTIGOS = "header";
	private static final String SEARCH_RESULTS = "Results";
        private static final String PAGINATION = "pagination"; 
        private static final String NOABSTRACT = "noAbstract";

	public static List<Artigo> realizarParserHtml(String html)
			throws Exception {

		List<Artigo> artigos = new ArrayList<Artigo>();
//		if (diretorio.isDirectory()) {
//			for (File html : diretorio.listFiles()) {
//				if (html.getName().contains("html")) {
					Document doc = Jsoup.parse(html);
                                        
					for (Element e : doc.select("ul")) {

						if (e.className().equalsIgnoreCase(SEARCH_RESULTS)) {
                                                        if(e.getElementsByClass(LIST_ARTIGOS).isEmpty()){
                                                            System.out.println("pagina sem artigo");
                                                            return null;
                                                        }else{
                                                            System.out.println("tem artigo");
                                                        }
							for (Element subE : e
									.getElementsByClass(LIST_ARTIGOS)) {               
								Boolean test = new Boolean(false);
								Artigo artigo = new Artigo();
								Element detail = subE
										.getElementsByClass(ARTIGO).first();
								Element H3 = detail.getElementsByTag("h3")
										.first();
								if (H3.getElementsByTag("a").first() != null) {
									artigo.setTitulo(H3.getElementsByTag("a")
											.first().text());
								} else {
									artigo.setTitulo(H3.text());
								}
								String textoDetail[] = detail.toString().split(
										"\n");
                                                                
                                                                artigo.setAutores(new HashSet<Autor>());                            
                                                                if(!detail.select(".authorPreferredName, .prefNameLink").isEmpty()){
                                                                    for(Element autores : detail.select(".authorPreferredName, .prefNameLink")){
                                                                        Autor autor = new Autor();
                                                                        autor.setNome(autores.text().trim());
                                                                        artigo.getAutores().add(autor);
                                                                    }
                                                                }
                                                                                                                             
								for (String texto : textoDetail) {
									if (texto.contains("<h3>")) {
										if(texto.split("</h3> ").length >= 2){
											test = true;
                                                                                        Autor autor = new Autor();
                                                                                        autor.setNome(texto.split("</h3> ")[1]);
                                                                                        artigo.getAutores().add(autor);
										}
									} else if(texto.contains("dx.doi.org")){
                                                                            Document doc2 = Jsoup
												.parseBodyFragment(texto);
										Element doi = doc2.body();
                                                                            artigo.setIssn(doi.text().trim());
                                                                        }else if (texto
											.contains("Publication Year")) {
										artigo.setPubYear(texto.split(": ")[1]);
									} else if (texto
											.contains("RecentIssue.jsp")) {
										Document doc2 = Jsoup
												.parseBodyFragment(texto);
										Element link = doc2.body();
										artigo.setOndePub(link.text());
									} else if (texto.contains("stamp.jsp?")) {
										Document doc2 = Jsoup
												.parseBodyFragment(texto);
										Element linkDownloadElement = doc2
												.body();
										for (Element link : linkDownloadElement
												.getElementsByAttribute("href")) {
											if (link.attr("href").contains(
													"stamp.jsp?")) {
												artigo.setLinkDownload(link
														.attr("href"));
												break;
											}
										}

									}else if (texto.contains("articleDetails.jsp?")){
                                                                            Document doc2 = Jsoup
												.parseBodyFragment(texto);
										Element linkDownloadElement = doc2
												.body();
										for (Element link : linkDownloadElement
												.getElementsByAttribute("href")) {
											if (link.attr("href").contains(
													"articleDetails.jsp?")) {
												artigo.setLinkDetalhe(link
														.attr("href"));
												break;
											}
										}
                                                                        }
								}
//								if(test){
									artigos.add(artigo);
//								}
							}
						}
					}
                                     return artigos;
				}
			//}
		//}

		
	//}

}
