package br.com.ufpb.appsnaauthorrank.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;
import br.com.ufpb.appsnaauthorrank.beans.Autor;
import br.com.ufpb.appsnaauthorrank.beans.to.XmlTO;
import br.com.ufpb.appsnaauthorrank.enumeration.TypeEnum;

public class PaperUtil {
	public static void criaCabecalhoArtigo(boolean direcionado) {

		XmlTO field1 = new XmlTO("name", true, "name", TypeEnum.STRING_TYPE);
		XmlTO field2 = new XmlTO("year", true, "year", TypeEnum.INT_TYPE);
		XmlTO field3 = new XmlTO("journal", true, "journal",
				TypeEnum.STRING_TYPE);
		XmlTO field4 = new XmlTO("author", true, "author", TypeEnum.STRING_TYPE);
		XmlTO field5 = new XmlTO("keywords", true, "keywords",
				TypeEnum.STRING_TYPE);

		List<XmlTO> listaTO = new ArrayList<XmlTO>();
		listaTO.add(field1);
		listaTO.add(field2);
		listaTO.add(field3);
		listaTO.add(field4);
		listaTO.add(field5);

		XMLUtil.generateHeader(listaTO, direcionado);
	}

	public static void criaCabecalhoAutor(boolean direcionado, boolean color) {

		XmlTO field1 = new XmlTO("name", true, "name", TypeEnum.STRING_TYPE);
		XmlTO field2 = new XmlTO("instituicao", true, "instituicao",
				TypeEnum.STRING_TYPE);
		XmlTO field6 = new XmlTO("qteArtigos", true, "qteArtigos",
				TypeEnum.INT_TYPE);

		XmlTO field3 = new XmlTO("r", true, "r", TypeEnum.INT_TYPE);
		XmlTO field4 = new XmlTO("g", true, "g", TypeEnum.INT_TYPE);
		XmlTO field5 = new XmlTO("b", true, "b", TypeEnum.INT_TYPE);

		List<XmlTO> listaTO = new ArrayList<XmlTO>();
		listaTO.add(field1);
		listaTO.add(field2);
		if (color) {
			listaTO.add(field3);
			listaTO.add(field4);
			listaTO.add(field5);
		}
		listaTO.add(field6);

		XMLUtil.generateHeader(listaTO, direcionado);
	}

	public static void criarNodosArtigo(List<Artigo> list) throws Exception {

		XMLUtil.addSpace(2);
		List<String> listaNodes = new ArrayList<String>();
		for (Artigo paper : list) {
			if (verificar(paper.getTitulo().toLowerCase(), listaNodes)) {
				XMLUtil.generateNodes(
						paper.getTitulo().toLowerCase(),
						paper.getPubYear() != null ? Integer.parseInt(paper
								.getPubYear().replaceAll(" ", "")) : 0,
						paper.getOndePub(),
						0,
						paper.getKeywords() != null
								&& paper.getKeywords().equals("") ? "No Keyword"
								: paper.getKeywords(),
						paper.getAutores() != null ? paper.getAutores() : null);
				listaNodes.add(paper.getTitulo());
			}

			// if (paper.getReferencia() != null) {
			// for (Artigo referencia : paper.getReferencia()) {
			// if (referencia.getTitulo() != null) {
			// if (verificar(referencia.getTitulo().toLowerCase(),
			// listaNodes)) {
			// XMLUtil.generateNodes(
			// referencia.getTitulo().toLowerCase(),
			// referencia.getPubYear() != null
			// && !referencia.getPubYear().equals(
			// "") ? Integer
			// .parseInt(referencia.getPubYear()
			// .replaceAll(" ", "")) : 0,
			// referencia.getOndePub() != null ? referencia
			// .getOndePub() : "Sem local",
			// 1, paper.getKeywords() , paper.getAutores() != null
			// ?paper.getAutores():null);
			// }
			// }
			// }
			// }
		}
	}

	public static void criarArestasArtigo(List<Artigo> list) throws Exception {

		XMLUtil.addSpace(2);

		for (Artigo paper : list) {
			if (paper.getReferencia() != null && paper.getTitulo() != null) {
				for (Artigo referencia : paper.getReferencia()) {
					if (referencia.getTitulo() != null) {
						XMLUtil.generateEdges(paper.getTitulo().toLowerCase(),
								referencia.getTitulo().toLowerCase(), 1);
					}
				}
			}
		}

	}

	public static void criarNodosAutor(List<Artigo> list,
			boolean colorByInstituicao) throws Exception {
		
		Map<Autor, List<Artigo>> mapAutores = new HashMap<>();
		for(Artigo a: list){
			for(Autor autor: a.getAutores()){
				if(mapAutores.containsKey(autor)){
					List<Artigo> listAu = mapAutores.get(autor);
					listAu.add(a);
					mapAutores.put(autor, listAu);
				}else{
					List<Artigo> listAu = new ArrayList<>();
					listAu.add(a);
					mapAutores.put(autor, listAu);
				}
			}
		}

		XMLUtil.addSpace(2);
		List<String> listaNodes = new ArrayList<String>();
		for (Artigo paper : list) {
			if (paper.getAutores() != null) {
				for (Autor autor : paper.getAutores()) {
					if (verificar(autor.getNome(), listaNodes)) {
						if (colorByInstituicao) {
							String rgb = ParserCSVInstituicao
									.getRGBByInstituicao(autor.getInstituicao());
							String[] rgbArray = rgb.split(";");
							XMLUtil.generateNodesAutorWithColor(
									autor.getNome(), autor.getInstituicao(), mapAutores.get(autor).size(),
									Integer.parseInt(rgbArray[1]),
									Integer.parseInt(rgbArray[2]),
									Integer.parseInt(rgbArray[3]));
						} else {
							XMLUtil.generateNodesAutor(autor.getNome(),
									autor.getInstituicao(), mapAutores.get(autor).size());
						}
						listaNodes.add(autor.getNome());
					}

				}
			}

			// if (paper.getReferencia() != null) {
			// for (Artigo referencia : paper.getReferencia()) {
			// if (referencia.getAutores() != null) {
			// for (Autor autor : referencia.getAutores()) {
			// if (verificar(autor.getNome(), listaNodes)) {
			// XMLUtil.generateNodes(autor.getNome());
			// listaNodes.add(autor.getNome());
			// }
			//
			// }
			// }
			// }
			// }
		}
	}

	public static void criarArestasAutor(List<Artigo> list) throws Exception {

		XMLUtil.addSpace(2);

		for (Artigo paper : list) {
			if (paper.getAutores() != null) {
				for (Autor a : paper.getAutores()) {
					for (Autor a2 : paper.getAutores()) {
						if (a.getNome() != null
								&& !a.getNome().equals(a2.getNome())
								&& a2.getNome() != null
								&& !a2.getNome().equals("")) {
							XMLUtil.generateEdges(a.getNome(), a2.getNome(), 1);
						}
					}
				}
			}

			// if (paper.getReferencia() != null) {
			// for (Artigo referencia : paper.getReferencia()) {
			// if (referencia.getAutores() != null
			// && paper.getAutores() != null) {
			// for (Autor a : paper.getAutores()) {
			// if (verificarAutor(a.getNome(),
			// referencia.getAutores())) {
			// for (Autor a2 : referencia.getAutores()) {
			// if (a.getNome() != null
			// && a2.getNome() != null
			// && !a2.getNome().equals("")
			// && !a.getNome()
			// .equals(a2.getNome())) {
			// XMLUtil.generateEdges(a.getNome(),
			// a2.getNome(), 1);
			// }
			// }
			// }
			// }
			// }
			// }
			// }

		}

	}

	public static void criaArquivo(String nomeDoMeuGrafo) {
		XMLUtil.fechaArquivo();
		XMLUtil.salvarXML(nomeDoMeuGrafo);

	}

	public static boolean verificarAutor(String autor, Set<Autor> set) {
		for (Autor a2 : set) {
			if (a2.getNome() != null && !autor.equals(a2.getNome())) {
				return true;
			}
		}
		return false;
	}

	public static boolean verificar(String id, List<String> lista) {

		for (String teste : lista) {
			if (id.equals(teste)) {
				return false;
			}
		}

		return true;
	}
}
