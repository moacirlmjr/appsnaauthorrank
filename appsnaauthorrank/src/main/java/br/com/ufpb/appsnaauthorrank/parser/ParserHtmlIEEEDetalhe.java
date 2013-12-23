package br.com.ufpb.appsnaauthorrank.parser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;
import br.com.ufpb.appsnaauthorrank.beans.Autor;
import br.com.ufpb.appsnaauthorrank.post.PostFreeCityApi;
import br.com.ufpb.appsnaauthorrank.post.postIeeeForm;

public class ParserHtmlIEEEDetalhe {

	private static final String AUTHOR = "authors";
	private static final String DOCS = "docs";
	private static final String URL_IEEE = "http://ieeexplore.ieee.org";

	public static Artigo realizarParserHtml(Artigo artigo) throws Exception {

		List<Artigo> referencias = new ArrayList<Artigo>();

		if (artigo.getLinkDetalhe() != null) {
			Document doc = Jsoup.parse(postIeeeForm.obterPagina(URL_IEEE
					+ artigo.getLinkDetalhe()));

			if (!doc.getElementsByClass(AUTHOR).isEmpty()) {
				Element divAuthor = doc.getElementsByClass(AUTHOR).first();
				for (Element e : divAuthor
						.select(".authorPreferredName, .prefNameLink")) {
					Autor autor = new Autor();
					autor.setNome(e.text().trim());
					if (!artigo.getAutores().contains(autor)) {
						artigo.getAutores().add(autor);
						System.out.println("autor add: " + autor.getNome());
						System.out.println("do titulo: " + artigo.getTitulo());
					}
				}

			}
			String urlReferencias = artigo.getLinkDetalhe().replace(
					"articleDetails", "abstractReferences");
			String pagina = postIeeeForm.obterPagina(URL_IEEE + urlReferencias);
			doc = Jsoup.parse(pagina);
			if (!doc.select(".docs").isEmpty()) {
				for (Element e : doc.select(".docs li")) {
					try {
						Artigo a = new Artigo();
						a.setAutores(new HashSet<Autor>());
						if (!e.text().contains("http")) {
							DocumentBuilderFactory dbf = DocumentBuilderFactory
									.newInstance();
							dbf.setNamespaceAware(false);
							DocumentBuilder docBuilder = dbf
									.newDocumentBuilder();
							String retorno = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
									+ PostFreeCityApi
											.postCitationApi(e
													.text()
													.split("  Abstract | Full Text: PDF")[0]
													.replace("[CrossRef]", ""));
							InputSource inStream = new InputSource(
									new ByteArrayInputStream(
											retorno.getBytes("UTF-8")));
							org.w3c.dom.Document docXml = docBuilder
									.parse(inStream);
							NodeList autores = docXml
									.getElementsByTagName("author");
							for (int i = 0; i < autores.getLength(); i++) {
								Node autorNode = autores.item(i);
								Autor autor = new Autor();
								autor.setNome(autorNode.getTextContent());
								a.getAutores().add(autor);
							}
							if (docXml.getElementsByTagName("title").item(0) != null) {
								a.setTitulo(docXml
										.getElementsByTagName("title").item(0)
										.getTextContent());
							} else if (docXml.getElementsByTagName("booktitle")
									.item(0) != null) {
								a.setTitulo(docXml
										.getElementsByTagName("booktitle")
										.item(0).getTextContent());
							}

							if (docXml.getElementsByTagName("journal").item(0) != null) {
								a.setOndePub(docXml
										.getElementsByTagName("journal")
										.item(0).getTextContent());
							} else if (docXml.getElementsByTagName("publisher")
									.item(0) != null) {
								a.setOndePub(docXml
										.getElementsByTagName("publisher")
										.item(0).getTextContent());
							}

							if (docXml.getElementsByTagName("year").item(0) != null) {
								a.setPubYear(docXml
										.getElementsByTagName("year").item(0)
										.getTextContent());
							} 
						} else {
							a.setTitulo(e.text());
						}
						referencias.add(a);
					} catch (Exception e2) {
						e2.printStackTrace();
						System.out.println(e.text());
					}
				}
				artigo.setReferencia(new HashSet<Artigo>(referencias));
			}
		}

		return artigo;
	}
}