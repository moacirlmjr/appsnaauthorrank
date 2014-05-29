package br.com.ufpb.appsnaauthorrank.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;
import br.com.ufpb.appsnaauthorrank.parser.ParserHtmlIEEE;
import br.com.ufpb.appsnaauthorrank.parser.ParserHtmlIEEEDetalhe;
import br.com.ufpb.appsnaauthorrank.post.postIeeeForm;
import br.com.ufpb.appsnaauthorrank.util.PaperUtil;
import br.com.ufpb.appsnaauthorrank.util.ValueComparator;

public class GerarGraphMLIEEEAllReferencies {

	public static List<Artigo> listArtigos = new ArrayList<>(); 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			List<Artigo> tempArtigos = new ArrayList<Artigo>();
			int contador = 1;
			ParserHtmlIEEE p = new ParserHtmlIEEE();
			while (tempArtigos != null) {
				tempArtigos = p.realizarParserHtml(postIeeeForm
						.post("\"Embedded System\"",
								contador));
				if (tempArtigos != null)
					listArtigos.addAll(tempArtigos);
				contador++;
			}

			// obter as referencias e atualizar artigos
			for (Artigo a : listArtigos) {
				System.out.println(a.getTitulo());
				if (a.getTitulo() != null)
					a = ParserHtmlIEEEDetalhe.realizarParserHtmlAllReferencies(a);
			}

			// ranking dos meios de publicação mais relevantes
			Map<String, Integer> rankingMeiosPublicacao = new HashMap<>();
			List<String> listaArtigosVistos = new LinkedList<>();
			for (Artigo a : listArtigos) {
				if (a.getOndePub() != null && !a.getOndePub().equals("")
						&& a.getTitulo() != null && !a.getTitulo().equals("")) {
					if (rankingMeiosPublicacao.containsKey(a.getOndePub())
							&& !listaArtigosVistos.contains(a.getTitulo())) {
						Integer contagem = rankingMeiosPublicacao.get(a
								.getOndePub());
						rankingMeiosPublicacao.put(a.getOndePub(), ++contagem);
					} else {
						rankingMeiosPublicacao.put(a.getOndePub(), 1);
					}
				}
				if (a.getReferencia() != null) {
					for (Artigo referencia : a.getReferencia()) {
						if (referencia.getOndePub() != null
								&& !referencia.getOndePub().equals("")
								&& referencia.getTitulo() != null
								&& !referencia.getTitulo().equals("")) {
							if (rankingMeiosPublicacao.containsKey(referencia
									.getOndePub())
									&& !listaArtigosVistos.contains(referencia
											.getTitulo())) {
								Integer contagem = rankingMeiosPublicacao
										.get(referencia.getOndePub());
								rankingMeiosPublicacao.put(
										referencia.getOndePub(), ++contagem);
							} else {
								rankingMeiosPublicacao.put(
										referencia.getOndePub(), 1);
							}
						}
					}
				}
			}

			ValueComparator bvc = new ValueComparator(rankingMeiosPublicacao);
			TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(
					bvc);
			sorted_map.putAll(rankingMeiosPublicacao);
			System.out.println("results: " + sorted_map);

			// gerando rede de artigos
			try {
				PaperUtil.criaCabecalhoArtigo(true);
				PaperUtil.criarNodosArtigo(listArtigos);
				PaperUtil.criarArestasArtigo(listArtigos);
				PaperUtil.criaArquivo("C:\\Users\\Moacir\\Desktop\\ARS\\GrafoDeArtigosRetornados.graphml");
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			// try {
			// criaCabecalhoAutor(true);
			// criarNodosAutor(artigos);
			// criarArestasAutor(artigos);
			// criaArquivo("C:\\Users\\Moacir\\Desktop\\ARS\\GrafoDeAutores2.graphml");
			// } catch (Exception ex) {
			// ex.printStackTrace();
			// }
			System.out.println("Quantidade de artigos: " + listArtigos.size());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}