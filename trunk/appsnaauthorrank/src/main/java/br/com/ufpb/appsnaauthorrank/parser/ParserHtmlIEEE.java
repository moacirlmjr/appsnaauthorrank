package br.com.ufpb.appsnaauthorrank.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;
import br.com.ufpb.appsnaauthorrank.beans.Autor;
import br.com.ufpb.appsnaauthorrank.post.postIeeeForm;

public class ParserHtmlIEEE implements Callable<List<Artigo>> {

	private static final String ARTIGO = "detail";
	private static final String LIST_ARTIGOS = "header";
	private static final String SEARCH_RESULTS = "Results";
	private static final String PAGINATION = "pagination";
	private static final String NOABSTRACT = "noAbstract";

	private String query;
	private Integer page;

	public List<Artigo> realizarParserHtml(String html) throws Exception {

		List<Artigo> artigos = new ArrayList<Artigo>();
		Document doc = Jsoup.parse(html);

		for (Element e : doc.select("ul")) {

			if (e.className().equalsIgnoreCase(SEARCH_RESULTS)) {
				if (e.getElementsByClass(LIST_ARTIGOS).isEmpty()) {
					return null;
				}

				for (Element subE : e.getElementsByClass(LIST_ARTIGOS)) {
					Boolean test = new Boolean(false);
					Artigo artigo = new Artigo();
					Element detail = subE.getElementsByClass(ARTIGO).first();
					Element H3 = detail.getElementsByTag("h3").first();
					if (H3.getElementsByTag("a").first() != null) {
						artigo.setTitulo(H3.getElementsByTag("a").first()
								.text().toLowerCase().replaceAll("&", "and")
								.replaceAll("[^\\p{L}\\p{Z}]", ""));
					} else {
						artigo.setTitulo(H3.text().toLowerCase()
								.replaceAll("&", "and")
								.replaceAll("[^\\p{L}\\p{Z}]", ""));
					}

					if (artigo
							.getTitulo()
							.equals("an operational approach to requirements specification for embedded systems")) {
						System.out.println();
					}

					String textoDetail[] = detail.toString().split("\n");

					artigo.setAutores(new HashSet<Autor>());
					if (!detail.select(".authorPreferredName, .prefNameLink")
							.isEmpty()) {
						for (Element autores : detail
								.select(".authorPreferredName, .prefNameLink")) {
							Autor autor = new Autor();
							String avetor[] = autores.text().trim().split(",");
							if (avetor.length > 1) {
								autor.setNome(avetor[1].replace(" ", "")
										.replace("\\.", "") + " " + avetor[0]);
							} else {
								autor.setNome(autores.text().trim());
							}
							artigo.getAutores().add(autor);
						}
					}

					for (String texto : textoDetail) {
						if (texto.contains("<h3>")) {
							if (texto.split("</h3> ").length >= 2) {
								test = true;
								Autor autor = new Autor();
								autor.setNome(texto.split("</h3> ")[1]);
								artigo.getAutores().add(autor);
							}
						} else if (texto.contains("dx.doi.org")) {
							Document doc2 = Jsoup.parseBodyFragment(texto);
							Element doi = doc2.body();
							artigo.setIssn(doi.text().trim());
						} else if (texto.contains("Publication Year")) {
							artigo.setPubYear(texto.split(": ")[1]);
						} else if (texto.contains("RecentIssue.jsp")) {
							Document doc2 = Jsoup.parseBodyFragment(texto);
							Element link = doc2.body();
							artigo.setOndePub(link.text()
									.replaceAll("&", "and"));
						} else if (texto.contains("stamp.jsp?")) {
							Document doc2 = Jsoup.parseBodyFragment(texto);
							Element linkDownloadElement = doc2.body();
							for (Element link : linkDownloadElement
									.getElementsByAttribute("href")) {
								if (link.attr("href").contains("stamp.jsp?")) {
									artigo.setLinkDownload(link.attr("href"));
									break;
								}
							}

						} else if (texto.contains("articleDetails.jsp?")) {
							Document doc2 = Jsoup.parseBodyFragment(texto);
							Element linkDownloadElement = doc2.body();
							for (Element link : linkDownloadElement
									.getElementsByAttribute("href")) {
								if (link.attr("href").contains(
										"articleDetails.jsp?")) {
									artigo.setLinkDetalhe(link.attr("href"));
									break;
								}
							}
						}
					}
					artigos.add(artigo);
				}
			}
		}
		System.out.println("Metadados das Publicações da Página " + page
				+ " obtidos");
		return artigos;
	}

	public static Integer getLastPage(String html) throws Exception {

		Document doc = Jsoup.parse(html);
		for (Element e : doc.getElementsByAttributeValue("title", "Last")) {
			String onclick = e.attr("onclick").replaceAll("[^\\d]", "");
			return Integer.parseInt(onclick);
		}

		return null;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	@Override
	public List<Artigo> call() throws Exception {
		System.out
				.println("Iniciando obtenção de Metadados das Publicações da Página "
						+ page);
		return realizarParserHtml(postIeeeForm.post(query, page));
	}

}
