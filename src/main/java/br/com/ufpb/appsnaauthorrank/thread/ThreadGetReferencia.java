package br.com.ufpb.appsnaauthorrank.thread;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.concurrent.Callable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.nodes.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;
import br.com.ufpb.appsnaauthorrank.beans.Autor;
import br.com.ufpb.appsnaauthorrank.post.PostFreeCiteApi;
import br.com.ufpb.appsnaauthorrank.util.StringUtil;

public class ThreadGetReferencia implements Callable<Artigo> {

	private Element e;

	public Element getE() {
		return e;
	}

	public void setE(Element e) {
		this.e = e;
	}

	@Override
	public Artigo call() throws Exception {
		try {
			Artigo a = new Artigo();
			a.setAutores(new HashSet<Autor>());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(false);
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			String retorno = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
					+ PostFreeCiteApi.postCitationApi(StringUtil
							.tratarCitacao(e.text()));
			System.out.println("--->>>>>>" + StringUtil.tratarCitacao(e.text())
					+ " ------- " + retorno.contains("valid='true'"));
			InputSource inStream = new InputSource(new ByteArrayInputStream(
					retorno.getBytes("UTF-8")));
			org.w3c.dom.Document docXml = docBuilder.parse(inStream);
			if (docXml.getElementsByTagName("title").item(0) != null
					|| docXml.getElementsByTagName("booktitle").item(0) != null) {
				NodeList autores = docXml.getElementsByTagName("author");
				for (int i = 0; i < autores.getLength(); i++) {
					Node autorNode = autores.item(i);
					Autor autor = new Autor();
					autor.setNome(autorNode.getTextContent());
					a.getAutores().add(autor);
				}
				if (docXml.getElementsByTagName("title").item(0) != null) {
					a.setTitulo(docXml.getElementsByTagName("title").item(0)
							.getTextContent().replace("&", "and")
							.replaceAll("\"", ""));
				} else if (docXml.getElementsByTagName("booktitle").item(0) != null) {
					a.setTitulo(docXml.getElementsByTagName("booktitle")
							.item(0).getTextContent().replace("&", "and")
							.replaceAll("\"", ""));
				}

				if (docXml.getElementsByTagName("journal").item(0) != null) {
					a.setOndePub(docXml.getElementsByTagName("journal").item(0)
							.getTextContent().replace("&", "and")
							.replaceAll("\"", ""));
				} else if (docXml.getElementsByTagName("publisher").item(0) != null) {
					a.setOndePub(docXml.getElementsByTagName("publisher")
							.item(0).getTextContent().replace("&", "and")
							.replaceAll("\"", ""));
				}

				if (docXml.getElementsByTagName("year").item(0) != null) {
					a.setPubYear(docXml.getElementsByTagName("year").item(0)
							.getTextContent());
				}
			} else {
				a.setTitulo(e.text().replace("&", "and").replaceAll("\"", ""));
			}
			return a;
		} catch (Exception e2) {
			e2.printStackTrace();
			System.out.println(e.text());
		}

		return null;
	}

}
