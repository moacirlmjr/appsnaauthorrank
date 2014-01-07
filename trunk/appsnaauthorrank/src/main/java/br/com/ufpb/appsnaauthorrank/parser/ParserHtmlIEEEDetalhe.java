package br.com.ufpb.appsnaauthorrank.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;
import br.com.ufpb.appsnaauthorrank.beans.Autor;
import br.com.ufpb.appsnaauthorrank.post.postIeeeForm;
import br.com.ufpb.appsnaauthorrank.thread.ThreadGetReferencia;

public class ParserHtmlIEEEDetalhe {

	private static final String AUTHOR = "authors";
	private static final String DOCS = "docs";
	private static final String URL_IEEE = "http://ieeexplore.ieee.org";

	private static final int QTE_THREADS_EXEC = 20;
	private static final int ACTIVES_TASK = 2;
	private static final int NTHREADS = Runtime.getRuntime()
			.availableProcessors() * 4;
	private static final ExecutorService exec = Executors
			.newFixedThreadPool(NTHREADS);

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
					List<Future<Artigo>> listArtigoTratadas = new ArrayList<Future<Artigo>>();
					int count = 0;
					Elements elements = doc.select(".docs li");
					for (Element e : elements) {

						ThreadGetReferencia t = new ThreadGetReferencia();
						t.setE(e);
						Future<Artigo> artigoTratada = exec.submit(t);
						listArtigoTratadas.add(artigoTratada);

						if ((count != 0 && count % QTE_THREADS_EXEC == 0)
								|| (elements.size() < QTE_THREADS_EXEC
										&& count != 0 && count
										% (elements.size() - 1) == 0)
								|| (elements.size()) == (count + 1)) {
							while (getQteThreadsRunning() != 0) {
								Thread.sleep(100);
							}

							for (Future<Artigo> future : listArtigoTratadas) {
								referencias.add(future.get());
							}
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

	private static Integer getQteThreadsRunning() {
		return Integer.parseInt(exec.toString().split(",")[ACTIVES_TASK]
				.split("=")[1].replace(" ", ""));
	}

}
