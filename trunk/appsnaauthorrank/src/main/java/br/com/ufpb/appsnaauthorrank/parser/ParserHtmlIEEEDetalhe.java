package br.com.ufpb.appsnaauthorrank.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;
import br.com.ufpb.appsnaauthorrank.beans.Autor;
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
			System.out.println();
			String urlReferencias = artigo.getLinkDetalhe().replace(
					"articleDetails", "abstractReferences");
			String pagina = postIeeeForm.obterPagina(URL_IEEE + urlReferencias);
			doc = Jsoup.parse(pagina);
			if (!doc.select(".docs").isEmpty()) {
				for (Element e : doc.select(".docs li")) {
					try {
						Artigo a = new Artigo();
						a.setAutores(new HashSet<Autor>());
						String articleValues[] = e.text()
								.replace(" by et al", "").split("\"");
//						if(articleValues.length > 1){
							String autores[] = articleValues[0].split(" and ");
							
							Autor author = new Autor();
							if (autores.length > 1) {
								if (Pattern.compile(".*[0-9].*")
										.matcher(autores[1]).matches()) {
									autores[1] = autores[1].replaceAll("[\\d].*",
											"");
									author.setNome(autores[1].substring(0,
											autores[1].length() - 2));
								} else {
									author.setNome(autores[1].replace(",", ""));
								}
								a.getAutores().add(author);
							}
							
							String divisaoAutor2[] = autores[0].split("\\.\\,");
							if (divisaoAutor2.length > 1) {
								for (String autor : divisaoAutor2) {
									author = new Autor();
									author.setNome(autor.replace("\\.\\,", ""));
									a.getAutores().add(author);
								}
							} else {
								author = new Autor();
								author.setNome(autores[0].replace("\\.\\,", ""));
								a.getAutores().add(author);
							}
							
							a.setTitulo(articleValues[1]);
							if (articleValues[2].split(",")[0].equals("")) {
								a.setOndePub(articleValues[2].split(",")[1]);
							} else {
								a.setOndePub(articleValues[2].split(",")[0]);
							}
//						}else{
							 e.text().split("[\\d\\.,*],");
//						}
					} catch (Exception e2) {
						System.out.println(e.text());
					}
				}
			}
		}

		return artigo;
	}

}