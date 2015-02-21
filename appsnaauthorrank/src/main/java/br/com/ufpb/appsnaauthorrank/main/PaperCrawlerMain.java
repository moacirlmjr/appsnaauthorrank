package br.com.ufpb.appsnaauthorrank.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;
import br.com.ufpb.appsnaauthorrank.parser.ParserHtmlIEEE;
import br.com.ufpb.appsnaauthorrank.parser.ParserHtmlIEEEDetalhe;
import br.com.ufpb.appsnaauthorrank.post.postIeeeForm;
import br.com.ufpb.appsnaauthorrank.util.NetworkUtil;
import br.com.ufpb.appsnaauthorrank.util.ValueComparator;

public class PaperCrawlerMain {

	private static final int ACTIVES_TASK = 2;
	private static final int QTE_THREADS_EXEC = 50;
	private static final int NTHREADS = Runtime.getRuntime()
			.availableProcessors() * 4;
	private static final ExecutorService exec = Executors
			.newFixedThreadPool(NTHREADS);

	public static void main(String[] args) {
		try {
			List<Artigo> listArtigos = new ArrayList<>();
			Scanner sn = new Scanner(System.in);

			System.out
					.println("######################### PAPERCRAWLER #########################");
			System.out.print("Digite o Seu termo de busca: ");
			String query = sn.nextLine();

			System.err.println("Termo de Busca Escolhido: " + query);
			System.out
					.println("######################### INICIANDO BUSCA #########################");
			System.out
					.println("* Atualmente o sistema só realiza busca no IEEEXplorer");

			Integer lastPage = ParserHtmlIEEE.getLastPage(postIeeeForm.post(
					query, 1));
			int count = 0;
			System.out
					.println("Quantidade de Páginas Encontradas: " + lastPage);

			Map<Integer, Future<List<Artigo>>> mapArtigosRetornadosCall = new HashMap<>();
			if (lastPage == null) {
				lastPage = 1;
			}
			for (int i = 1; i <= lastPage; i++) {
				ParserHtmlIEEE parser = new ParserHtmlIEEE();
				parser.setQuery(query);
				parser.setPage(i);
				Future<List<Artigo>> artigoListCall = exec.submit(parser);
				mapArtigosRetornadosCall.put(i, artigoListCall);
				if ((i != 0 && i % QTE_THREADS_EXEC == 0)
						|| (lastPage < QTE_THREADS_EXEC && i != 0 && i
								% (lastPage - 1) == 0)
						|| (lastPage) == (count + 1)) {
					while (getQteThreadsRunning() != 0) {
						Thread.sleep(100);
					}

					for (Integer key : mapArtigosRetornadosCall.keySet()) {
						listArtigos.addAll(mapArtigosRetornadosCall.get(key)
								.get());
					}
					mapArtigosRetornadosCall = new HashMap<>();
				}
				count++;
			}

			System.out
					.println("######################### INICIANDO IDENTIFICAÇÃO DAS CITAÇÕES #########################");

			System.out
					.println("1- Citados entre Si (só irá buscar referências que também estiverem na lista de artigos iniciais) ");
			System.out
					.println("2- Todas Referências (Busca todas as referencias dos artigos encontrados inicialmente)");
			System.out.print("Digite o Sua opção: ");
			String opcao = sn.nextLine();

			List<Artigo> listAux = new LinkedList<Artigo>();
			switch (opcao) {
			case "1":
				System.out
						.println("* As citações consideradas só são aquelas que são indexadas pelo ieeexplorer e aparecem na lista de publicações iniciais");
				List<Future<Artigo>> listArtigosCall = new LinkedList<>();
				count = 0;
				for (Artigo a : listArtigos) {
					if (a.getTitulo() != null) {
						ParserHtmlIEEEDetalhe parserDetalhe = new ParserHtmlIEEEDetalhe();
						parserDetalhe.setArtigo(a);
						parserDetalhe.setListArtigos(listArtigos);
						Future<Artigo> artigoCall = exec.submit(parserDetalhe);
						listArtigosCall.add(artigoCall);

						if ((count != 0 && count % QTE_THREADS_EXEC == 0)
								|| (listArtigos.size() < QTE_THREADS_EXEC
										&& count != 0 && count
										% (listArtigos.size() - 1) == 0)
								|| (listArtigos.size()) == (count + 1)) {
							while (getQteThreadsRunning() != 0) {
								Thread.sleep(100);
							}

							for (Future<Artigo> future : listArtigosCall) {
								listAux.add(future.get());
							}
							listArtigosCall = new LinkedList<>();
						}

						count++;
					}
				}
				break;
			case "2":
				for (Artigo a : listArtigos) {
					if (a.getTitulo() != null) {
						a = ParserHtmlIEEEDetalhe
								.realizarParserHtmlAllReferencies(a);
						listAux.add(a);
					}
				}
				break;
			default:
				System.out.println("Você não digitou a opção corretamente");
				break;
			}

			// // ranking dos meios de publicação mais relevantes
			// Map<String, Integer> rankingMeiosPublicacao = new HashMap<>();
			// List<String> listaArtigosVistos = new LinkedList<>();
			// for (Artigo a : listAux) {
			// if (a.getOndePub() != null && !a.getOndePub().equals("")
			// && a.getTitulo() != null && !a.getTitulo().equals("")) {
			// if (rankingMeiosPublicacao.containsKey(a.getOndePub())
			// && !listaArtigosVistos.contains(a.getTitulo())) {
			// Integer contagem = rankingMeiosPublicacao.get(a
			// .getOndePub());
			// rankingMeiosPublicacao.put(a.getOndePub(), ++contagem);
			// } else {
			// rankingMeiosPublicacao.put(a.getOndePub(), 1);
			// }
			// }
			// if (a.getReferencia() != null) {
			// for (Artigo referencia : a.getReferencia()) {
			// if (referencia.getOndePub() != null
			// && !referencia.getOndePub().equals("")
			// && referencia.getTitulo() != null
			// && !referencia.getTitulo().equals("")) {
			// if (rankingMeiosPublicacao.containsKey(referencia
			// .getOndePub())
			// && !listaArtigosVistos.contains(referencia
			// .getTitulo())) {
			// Integer contagem = rankingMeiosPublicacao
			// .get(referencia.getOndePub());
			// rankingMeiosPublicacao.put(
			// referencia.getOndePub(), ++contagem);
			// } else {
			// rankingMeiosPublicacao.put(
			// referencia.getOndePub(), 1);
			// }
			// }
			// }
			// }
			// }
			//
			// ValueComparator bvc = new
			// ValueComparator(rankingMeiosPublicacao);
			// TreeMap<String, Integer> sorted_map = new TreeMap<String,
			// Integer>(
			// bvc);
			// sorted_map.putAll(rankingMeiosPublicacao);
			// System.out.println("results: " + sorted_map);

			// gerando rede de artigos
			try {
				NetworkUtil.gerarRedePublicacoes(listAux);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			System.out
					.println("## Calculando Evolução de publicações ao longo dos anos ##");
			Map<String, Integer> anoMap = new HashMap<String, Integer>();
			for (Artigo a : listAux) {
				if (!anoMap.containsKey(a.getPubYear())) {
					anoMap.put(a.getPubYear(), 1);
				} else {
					anoMap.put(a.getPubYear(), 1 + anoMap.get(a.getPubYear()));
				}
			}

			System.out.println("Evolução de publicações ao longo dos anos: "
					+ anoMap);

			try {
				NetworkUtil.gerarRedeCoautoria(listAux);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			System.out.println(new Date());
			System.exit(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private static Integer getQteThreadsRunning() {
		return Integer.parseInt(exec.toString().split(",")[ACTIVES_TASK]
				.split("=")[1].replace(" ", ""));
	}

}