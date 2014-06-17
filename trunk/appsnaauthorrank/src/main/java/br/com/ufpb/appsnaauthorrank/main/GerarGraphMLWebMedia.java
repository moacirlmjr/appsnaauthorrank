package br.com.ufpb.appsnaauthorrank.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;
import br.com.ufpb.appsnaauthorrank.beans.Autor;
import br.com.ufpb.appsnaauthorrank.beans.Evento;
import br.com.ufpb.appsnaauthorrank.parser.ParserHtmlWebMedia;
import br.com.ufpb.appsnaauthorrank.util.PaperUtil;
import br.com.ufpb.appsnaauthorrank.util.XMLUtil;

public class GerarGraphMLWebMedia {

	public static void main(String[] args) {
		try {

			List<Evento> listEventos = ParserHtmlWebMedia
					.realizarParserHtml("editions");
			int count = 1995;
			List<Artigo> listArtigo = new ArrayList<>();
			Map<String, String> listInstituicoes = new HashMap<>();

			for (Evento evento : listEventos) {
				listArtigo.addAll(evento.getArtigos());
				for (Artigo a : listArtigo) {
					for (Autor au : a.getAutores()) {
						listInstituicoes.put(au.getInstituicao(),
								au.getInstituicao());
					}
				}
				PaperUtil.criaCabecalhoAutor(false, true);
				PaperUtil.criarNodosAutor(evento.getArtigos(), true);
				PaperUtil.criarArestasAutor(evento.getArtigos());
				PaperUtil
						.criaArquivo("C:\\Users\\Moacir\\Desktop\\ARS\\WebMedia\\GrafoWebMedia"
								+ count + ".graphml");
				XMLUtil.arquivo = new StringBuffer(
						"<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
						.append("\n<!-- GraphML gerado eautomaticamente pela AppSNA -->")
						.append("\n<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\">");
				count++;
			}

			quantidadeCoautoresPorArtigo(listArtigo);

			quantidadedeArtigosPorAutor(listArtigo);

			quantidadedeArtigosPorCoautor(listArtigo);

			System.out.println("Instituições");
			for (String key : listInstituicoes.keySet()) {
				System.out.println(key);
			}

			PaperUtil.criaCabecalhoAutor(false, true);
			PaperUtil.criarNodosAutor(listArtigo, true);
			PaperUtil.criarArestasAutor(listArtigo);
			PaperUtil
					.criaArquivo("C:\\Users\\Moacir\\Desktop\\ARS\\WebMedia\\GrafoWebMediaTOTAL.graphml");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private static void quantidadeCoautoresPorArtigo(List<Artigo> listArtigo) {
		int count;
		Map<Artigo, List<Autor>> mapArtigosCoautor = new HashMap<>();
		for (Artigo a : listArtigo) {
			count = 0;
			List<Autor> listCoautor = new ArrayList<>();
			for (Autor au : a.getAutores()) {
				if (count != 0) {
					listCoautor.add(au);
				}
				count++;
			}
			mapArtigosCoautor.put(a, listCoautor);
		}

		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		int count4 = 0;
		int count5 = 0;
		for (Artigo a : mapArtigosCoautor.keySet()) {
			List<Autor> list = mapArtigosCoautor.get(a);
			if (list.size() == 1) {
				count1++;
			} else if (list.size() == 2) {
				count2++;
			} else if (list.size() == 3) {
				count3++;
			} else if (list.size() == 4) {
				count4++;
			} else if (list.size() >= 5) {
				count5++;
			}
		}

		System.out.println("Quantidade de Coautores Por artigo ");
		System.out.println("1 " + count1);
		System.out.println("2 " + count2);
		System.out.println("3 " + count3);
		System.out.println("4 " + count4);
		System.out.println(">=5 " + count5);

		List<combine> list = new ArrayList<combine>();
		for (Entry<Artigo, List<Autor>> value : mapArtigosCoautor.entrySet()) {
			combine a = new combine(value.getValue().size(), value.getKey()
					.getTitulo());
			list.add(a);
		}

		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

	private static void quantidadedeArtigosPorAutor(List<Artigo> listArtigo) {
		Map<Autor, List<Artigo>> mapAutores = new HashMap<>();
		for (Artigo a : listArtigo) {
			for (Autor autor : a.getAutores()) {
				if (mapAutores.containsKey(autor)) {
					List<Artigo> list = mapAutores.get(autor);
					list.add(a);
					mapAutores.put(autor, list);
				} else {
					List<Artigo> list = new ArrayList<>();
					list.add(a);
					mapAutores.put(autor, list);
				}
			}
		}

		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		int count4 = 0;
		int count5 = 0;
		for (Autor a : mapAutores.keySet()) {
			List<Artigo> list = mapAutores.get(a);
			if (list.size() == 1) {
				count1++;
			} else if (list.size() == 2) {
				count2++;
			} else if (list.size() == 3) {
				count3++;
			} else if (list.size() == 4) {
				count4++;
			} else if (list.size() >= 5) {
				count5++;
			}
		}

		System.out.println("Quantidade de Artigos Por Autor ");
		System.out.println("1 " + count1);
		System.out.println("2 " + count2);
		System.out.println("3 " + count3);
		System.out.println("4 " + count4);
		System.out.println(">=5 " + count5);
	}

	private static void quantidadedeArtigosPorCoautor(List<Artigo> listArtigo) {

		Map<Autor, Integer> mapAutores = new HashMap<>();
		for (Artigo a : listArtigo) {
			int count = 0;
			for (Autor autor : a.getAutores()) {
				if (count != 0) {
					if (mapAutores.containsKey(autor)) {
						Integer qte = mapAutores.get(autor);
						qte++;
						mapAutores.put(autor, qte);
					} else {
						mapAutores.put(autor, 1);
					}
				}
				count++;
			}
		}

		List<combine> list = new ArrayList<combine>();
		for (Entry<Autor, Integer> value : mapAutores.entrySet()) {
			combine a = new combine(value.getValue(), value.getKey().getNome());
			list.add(a);
		}

		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

}

class combine implements Comparable<combine> {

	public int value;
	public String key;

	public combine(int value, String key) {
		this.value = value;
		this.key = key;
	}

	@Override
	public int compareTo(combine arg0) {
		// TODO Auto-generated method stub
		return this.value < arg0.value ? 1 : this.value > arg0.value ? -1 : 0;
	}

	public String toString() {
		return this.value + " " + this.key;
	}
}
