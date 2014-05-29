package br.com.ufpb.appsnaauthorrank.parser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;
import br.com.ufpb.appsnaauthorrank.beans.Autor;
import br.com.ufpb.appsnaauthorrank.main.GerarGraphMLIEEEAllReferencies;
import br.com.ufpb.appsnaauthorrank.post.PostFreeCiteApi;
import br.com.ufpb.appsnaauthorrank.post.PostParaCiteApi;
import br.com.ufpb.appsnaauthorrank.post.postIeeeForm;

public class ParserHtmlIEEEDetalhe implements Callable<Artigo> {

	private static final String AUTHOR = "authors";
	private static final String DOCS = "docs";
	private static final String URL_IEEE = "http://ieeexplore.ieee.org";

	private Artigo artigo;
	private List<Artigo> listArtigos;
	private Long id;

	public synchronized static Artigo realizarParserHtmlAllReferencies(
			Artigo artigo) throws Exception {

		List<Artigo> referencias = new ArrayList<Artigo>();
		artigo.setTitulo(artigo.getTitulo().replace("&", "and")
				.replaceAll("\"", ""));
		if (artigo.getLinkDetalhe() != null) {
			Document doc = null;

			try {
				String urlReferencias = artigo.getLinkDetalhe().replace(
						"articleDetails", "abstractReferences");
				String pagina = postIeeeForm.obterPagina(URL_IEEE
						+ urlReferencias, 0);
				doc = Jsoup.parse(pagina);

				if (!doc.select(".docs").isEmpty()) {
					int count = 0;
					Elements elements = doc.select(".docs li");
					List<String> referenciasString = new ArrayList<>();
					for (Element referencia : elements) {
						referenciasString.add(referencia.text()
								.replace("(7043):814-8", "")
								.replace(", 14333-14337. ", ""));
						if ((count != 0 && count % 20 == 0)
								|| (elements.size() < 20 && count != 0 && count
										% (elements.size() - 1) == 0)
								|| (elements.size()) == (count + 1)) {

							DocumentBuilderFactory dbf = DocumentBuilderFactory
									.newInstance();
							dbf.setNamespaceAware(false);
							DocumentBuilder docBuilder = dbf
									.newDocumentBuilder();
							String retorno = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
									+ PostFreeCiteApi
											.postCitationApi(referenciasString);
							InputSource inStream = new InputSource(
									new ByteArrayInputStream(
											retorno.getBytes("UTF-8")));
							org.w3c.dom.Document docXml = docBuilder
									.parse(inStream);
							for (int i = 0; i < docXml.getElementsByTagName(
									"citation").getLength(); i++) {
								Artigo a = new Artigo();
								a.setAutores(new HashSet<Autor>());

								NodeList childsCitation = docXml
										.getElementsByTagName("citation")
										.item(i).getChildNodes();

								for (int h = 0; h < childsCitation.getLength(); h++) {
									Node no = childsCitation.item(h);

									if (no.getNodeName().equals("authors")) {
										for (int j = 0; j < no.getChildNodes()
												.getLength(); j++) {
											Node autorNode = no.getChildNodes()
													.item(j);
											Autor autor = new Autor();
											autor.setNome(autorNode
													.getTextContent()
													.replaceAll("&", ""));
											a.getAutores().add(autor);
										}
									} else if (no.getNodeName().equals("title")) {
										a.setTitulo(no
												.getTextContent()
												.replaceAll("&", "and")
												.replaceAll("[^\\p{L}\\p{Z}]",
														""));
									} else if (no.getNodeName().equals(
											"booktitle")
											&& (a.getTitulo() == null || a
													.getTitulo().equals(""))) {
										a.setTitulo(no
												.getTextContent()
												.replaceAll("&", "and")
												.replaceAll("[^\\p{L}\\p{Z}]",
														""));
									} else if (no.getNodeName().equals(
											"journal")) {
										// if
										// (no.getTextContent().equals("null"))
										// {
										// System.out.println();
										// }
										a.setOndePub(no.getTextContent()
												.replaceAll("&", "and")
												.replaceAll("\"", ""));
									} else if (no.getNodeName().equals(
											"publisher")) {
										// if
										// (no.getTextContent().equals("null"))
										// {
										// System.out.println();
										// }
										a.setOndePub(no.getTextContent()
												.replaceAll("&", "and")
												.replaceAll("\"", ""));
									} else if (no.getNodeName().equals("year")) {
										// if (!no.getTextContent().equals("")
										// && (Integer.parseInt(no
										// .getTextContent()) < 1912 || Integer
										// .parseInt(no
										// .getTextContent()) > 2013)) {
										// System.out.println();
										// } else if
										// (no.getTextContent().equals(
										// "")) {
										// System.out.println();
										// }
										a.setPubYear(no.getTextContent());
									}
								}
								if (a.getPubYear() == null
										|| a.getPubYear().equals("")
										|| (a.getPubYear() != null && Integer
												.parseInt(a.getPubYear()) > 2013)) {
									for (String ref : referenciasString) {
										if (ref.contains(a.getTitulo() + "")) {
											a.setPubYear(PostParaCiteApi
													.getYearPostParaCiteApi(ref));
											break;
										}
									}
								}
								referencias.add(a);
							}

							referenciasString.clear();
						}

						count++;

					}
					artigo.setReferencia(new HashSet<Artigo>(referencias));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return artigo;
	}

	private Artigo parseReferenciaStringToArtigo(String referencia) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(false);
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			String retorno = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
					+ PostFreeCiteApi.postCitationApi(referencia, 0);
			InputSource inStream = new InputSource(new ByteArrayInputStream(
					retorno.getBytes("UTF-8")));
			org.w3c.dom.Document docXml = docBuilder.parse(inStream);
			for (int i = 0; i < docXml.getElementsByTagName("citation")
					.getLength(); i++) {
				Artigo a = new Artigo();
				a.setAutores(new HashSet<Autor>());

				NodeList childsCitation = docXml
						.getElementsByTagName("citation").item(i)
						.getChildNodes();

				for (int h = 0; h < childsCitation.getLength(); h++) {
					Node no = childsCitation.item(h);

					if (no.getNodeName().equals("authors")) {
						for (int j = 0; j < no.getChildNodes().getLength(); j++) {
							Node autorNode = no.getChildNodes().item(j);
							Autor autor = new Autor();
							autor.setNome(autorNode.getTextContent()
									.replaceAll("&", ""));
							a.getAutores().add(autor);
						}
					} else if (no.getNodeName().equals("title")) {
						a.setTitulo(no.getTextContent().replaceAll("&", "and")
								.replaceAll("[^\\p{L}\\p{Z}]", ""));
					} else if (no.getNodeName().equals("booktitle")
							&& (a.getTitulo() == null || a.getTitulo().equals(
									""))) {
						a.setTitulo(no.getTextContent().replaceAll("&", "and")
								.replaceAll("[^\\p{L}\\p{Z}]", ""));
					} else if (no.getNodeName().equals("journal")) {
						a.setOndePub(no.getTextContent().replaceAll("&", "and")
								.replaceAll("\"", ""));
					} else if (no.getNodeName().equals("publisher")) {
						a.setOndePub(no.getTextContent().replaceAll("&", "and")
								.replaceAll("\"", ""));
					} else if (no.getNodeName().equals("year")) {
						a.setPubYear(no.getTextContent());
					}
				}
				return a;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized Artigo realizarParserHtmlCitacoesEntreSi(Artigo artigo)
			throws Exception {

		List<Artigo> referencias = new ArrayList<Artigo>();
		artigo.setTitulo(artigo.getTitulo().replace("&", "and")
				.replaceAll("\"", ""));
		if (artigo.getLinkDetalhe() != null) {
			Document doc = null;

			try {
				String urlReferencias = artigo.getLinkDetalhe().replace(
						"articleDetails", "abstractReferences");
				String pagina = postIeeeForm.obterPagina(URL_IEEE
						+ urlReferencias, 0);
				doc = Jsoup.parse(pagina);

				if (!doc.select(".docs").isEmpty()) {
					Elements elements = doc.select(".docs li");
					for (Element ref : elements) {
						String referenciaText = ref.text().toLowerCase()
								.replace("(7043):814-8", "")
								.replace(", 14333-14337. ", "")
								.replaceAll("[^\\p{L}\\p{Z}]", "");
						System.out.println(referenciaText);
						Artigo referencia = verificarReferenciaEmListaArtigos(referenciaText);
						if (referencia != null) {
							referencias.add(referencia);
						}
					}
					artigo.setReferencia(new HashSet<Artigo>(referencias));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return artigo;
	}

	private Artigo verificarReferenciaEmListaArtigos(String referencia) {
		for (Artigo a : listArtigos) {
			if (referencia.equalsIgnoreCase(a.getTitulo())) {
				return a;
			}
		}
		return null;
	}

	@Override
	public Artigo call() throws Exception {
		List<Artigo> referencias = new ArrayList<Artigo>();
		artigo.setTitulo(artigo.getTitulo().replace("&", "and")
				.replaceAll("\"", ""));
		if (artigo.getLinkDetalhe() != null) {
			Document doc = null;

			try {
				String urlKeywords = artigo.getLinkDetalhe().replace(
						"articleDetails", "abstractKeywords");
				String paginaKeywords = postIeeeForm.obterPagina(URL_IEEE
						+ urlKeywords, 0);
				doc = Jsoup.parse(paginaKeywords);
				Elements elementsKeywords = doc.getElementsByClass("col-2");
				artigo.setId(id);
				artigo.setKeywords("");
				if (elementsKeywords.size() == 0) {
					Elements elementsAux = doc.getElementsByClass("col-1");
					for (Element e : elementsAux) {
						Elements elementsAux2 = e.getElementsByClass("section");
						if (elementsAux2.size() == 0) {
							Elements elementsAuxCol1 = doc
									.getElementsByClass("col");
							for (Element e2 : elementsAuxCol1) {
								Elements elementsAuxSection = e2
										.getElementsByClass("section");
								for (Element e3 : elementsAuxSection) {
									Elements links = e3.getElementsByTag("a");
									for (Element link : links) {
										String keyword = link.text();
										artigo.setKeywords(artigo.getKeywords()
												+ keyword + ",");
									}
								}

							}

						} else {
							for (Element e2 : elementsAux2) {
								Elements links = e2.getElementsByTag("a");
								for (Element link : links) {
									String keyword = link.text();
									artigo.setKeywords(artigo.getKeywords()
											+ keyword + ", ");
								}
							}
						}

					}
				} else {
					for (Element e : elementsKeywords) {
						Elements links = e.getElementsByTag("a");
						for (Element link : links) {
							String keyword = link.text();
							artigo.setKeywords(artigo.getKeywords() + keyword
									+ ",");
						}
					}
				}

				String urlReferencias = artigo.getLinkDetalhe().replace(
						"articleDetails", "abstractReferences");
				String pagina = postIeeeForm.obterPagina(URL_IEEE
						+ urlReferencias, 0);
				doc = Jsoup.parse(pagina);
				System.out.println("REFERENCIAS OBTIDAS");
				if (!doc.select(".docs").isEmpty()) {
					Elements elements = doc.select(".docs li");
					for (Element ref : elements) {
						Elements elementsLink = ref.select("a");
						if(elementsLink.size() > 0){
							for(Element link: elementsLink){
								if(link.text().equalsIgnoreCase("abstract")){
									String hrefReferencia = link.attr("href");
									String paginaDetalheRef = postIeeeForm.obterPagina(URL_IEEE
											+ hrefReferencia, 0);
									Document docReferencia = Jsoup.parse(paginaDetalheRef);
									Elements title = docReferencia.getElementsByClass("title");
									if(title.size() > 0){
										Element titulo = title.get(0);
										String referenciaText = titulo.text().toLowerCase()
												.replace("(7043):814-8", "")
												.replace(", 14333-14337. ", "")
												.replaceAll("[^\\p{L}\\p{Z}]", "");
										Artigo referencia = verificarReferenciaEmListaArtigos(referenciaText);
										if (referencia != null) {
											System.out.println("---->  " + referenciaText);
											referencias.add(referencia);
										}
									}
								}
							}
						}
					}
					artigo.setReferencia(new HashSet<Artigo>(referencias));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return artigo;
	}

	public Artigo getArtigo() {
		return artigo;
	}

	public void setArtigo(Artigo artigo) {
		this.artigo = artigo;
	}

	public List<Artigo> getListArtigos() {
		return listArtigos;
	}

	public void setListArtigos(List<Artigo> listArtigos) {
		this.listArtigos = listArtigos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
