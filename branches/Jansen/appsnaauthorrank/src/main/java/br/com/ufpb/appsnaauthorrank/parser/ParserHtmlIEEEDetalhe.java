package br.com.ufpb.appsnaauthorrank.parser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
import br.com.ufpb.appsnaauthorrank.post.PostFreeCiteApi;
import br.com.ufpb.appsnaauthorrank.post.postIeeeForm;
import br.com.ufpb.appsnaauthorrank.thread.ThreadGetReferencia;
import br.com.ufpb.appsnaauthorrank.util.StringUtil;

public class ParserHtmlIEEEDetalhe {

	private static final String AUTHOR = "authors";
	private static final String DOCS = "docs";
	private static final String URL_IEEE = "http://ieeexplore.ieee.org";

	public synchronized static Artigo realizarParserHtml(Artigo artigo)
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
						+ urlReferencias);
				doc = Jsoup.parse(pagina);

				if (!doc.select(".docs").isEmpty()) {
					int count = 0;
					Elements elements = doc.select(".docs li");
					List<String> referenciasString = new ArrayList<>();
					for (Element referencia : elements) {
						referenciasString.add(referencia.text());
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
													.getTextContent().replaceAll("&", ""));
											a.getAutores().add(autor);
										}
									} else if (no.getNodeName().equals("title")) {
										a.setTitulo(no.getTextContent()
												.replace("&", "and")
												.replaceAll("\"", ""));
									} else if (no.getNodeName().equals(
											"booktitle")) {
										a.setTitulo(no.getTextContent()
												.replace("&", "and")
												.replaceAll("\"", ""));
									} else if (no.getNodeName().equals(
											"journal")) {
										a.setOndePub(no.getTextContent()
												.replace("&", "and")
												.replaceAll("\"", ""));
									} else if (no.getNodeName().equals(
											"publisher")) {
										a.setOndePub(no.getTextContent()
												.replace("&", "and")
												.replaceAll("\"", ""));
									} else if (no.getNodeName().equals("year")) {
										a.setPubYear(no.getTextContent());
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

}
