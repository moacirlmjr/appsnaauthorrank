package br.com.ufpb.appsnaauthorrank.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;
import br.com.ufpb.appsnaauthorrank.beans.Autor;
import br.com.ufpb.appsnaauthorrank.beans.to.XmlTO;
import br.com.ufpb.appsnaauthorrank.enumeration.TypeEnum;
import br.com.ufpb.appsnaauthorrank.parser.ParserHtmlIEEE;
import br.com.ufpb.appsnaauthorrank.parser.ParserHtmlIEEEDetalhe;
import br.com.ufpb.appsnaauthorrank.post.postIeeeForm;
import br.com.ufpb.appsnaauthorrank.util.XMLUtil;

public class GerarGraphMLIEEE {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			List<Artigo> artigos = new ArrayList<Artigo>();
			List<Artigo> tempArtigos = new ArrayList<Artigo>();
			int contador = 1;

			while (tempArtigos != null) {
				tempArtigos = ParserHtmlIEEE.realizarParserHtml(postIeeeForm
						.post("\"social network analysis\" and online",
								contador));
				if (tempArtigos != null)
					artigos.addAll(tempArtigos);
				contador++;
					
			}

			// obter as referencias e atualizar artigos
			for (Artigo a : artigos) {
				if (a.getTitulo() != null)
					a = ParserHtmlIEEEDetalhe.realizarParserHtml(a);
				System.out.println(a.getTitulo());
			}

//			criaCabecalho(true);
//			criarNodosArtigo(artigos);
//			criarArestasArtigo(artigos);
//			criaArquivo("GrafoDeArtigos2.graphml");
//			salvarArquivo("");

			criaCabecalho(true);
			criarNodosAutor(artigos);
			criarArestasAutor(artigos);
			criaArquivo("GrafoDeAutores2.graphml");
			salvarArquivo("");

			System.out.println("Quantidade de artigos: " + artigos.size());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static void criaCabecalho(boolean direcionado) {

		XmlTO field1 = new XmlTO("name", true, "name", TypeEnum.STRING_TYPE);

		List<XmlTO> listaTO = new ArrayList<XmlTO>();
		listaTO.add(field1);

		XMLUtil.generateHeader(listaTO, direcionado);
	}

	private static void criarNodosArtigo(List<Artigo> list) throws Exception {

		XMLUtil.addSpace(2);
		List<String> listaNodes = new ArrayList<String>();
		for (Artigo paper : list) {
			if (verificar(paper.getTitulo(), listaNodes)) {
				XMLUtil.generateNodes(paper.getTitulo());
				listaNodes.add(paper.getTitulo());
			}

			if (paper.getReferencia() != null) {
				for (Artigo referencia : paper.getReferencia()) {
					if (referencia.getTitulo() != null) {
						if (verificar(referencia.getTitulo(), listaNodes)) {
							XMLUtil.generateNodes(referencia.getTitulo());
						}
					}
				}
			}
		}
	}

	private static void criarArestasArtigo(List<Artigo> list) throws Exception {

		XMLUtil.addSpace(2);

		for (Artigo paper : list) {
			if (paper.getReferencia() != null && paper.getTitulo() != null) {
				for (Artigo referencia : paper.getReferencia()) {
					if (referencia.getTitulo() != null) {
						XMLUtil.generateEdges(paper.getTitulo(),
								referencia.getTitulo(), 1);
					}
				}
			}
		}

	}

	private static void criarNodosAutor(List<Artigo> list) throws Exception {

		XMLUtil.addSpace(2);
		List<String> listaNodes = new ArrayList<String>();
		for (Artigo paper : list) {
			if (paper.getAutores() != null) {
				for (Autor autor : paper.getAutores()) {
					if (verificar(autor.getNome(), listaNodes)) {
						XMLUtil.generateNodes(autor.getNome());
						listaNodes.add(autor.getNome());
					}

				}
			}

			if (paper.getReferencia() != null) {
				for (Artigo referencia : paper.getReferencia()) {
					if (referencia.getAutores() != null) {
						for (Autor autor : referencia.getAutores()) {
							if (verificar(autor.getNome(), listaNodes)) {
								XMLUtil.generateNodes(autor.getNome());
								listaNodes.add(autor.getNome());
							}

						}
					}
				}
			}
		}
	}

	private static void criarArestasAutor(List<Artigo> list) throws Exception {

		XMLUtil.addSpace(2);

		for (Artigo paper : list) {
			if (paper.getAutores() != null) {
				for (Autor a : paper.getAutores()) {
					for (Autor a2 : paper.getAutores()) {
						if (a.getNome() != null
								&& !a.getNome().equals(a2.getNome())) {
							XMLUtil.generateEdges(a.getNome(), a2.getNome(), 1);
						}
					}
				}
			}

			if (paper.getReferencia() != null) {
				for (Artigo referencia : paper.getReferencia()) {
					if (referencia.getAutores() != null
							&& paper.getAutores() != null) {
						for (Autor a : paper.getAutores()) {
							if (verificarAutor(a.getNome(),
									referencia.getAutores())) {
								for (Autor a2 : referencia.getAutores()) {
									if (a.getNome() != null
											&& a2.getNome() != null
											&& !a.getNome()
													.equals(a2.getNome())) {
										XMLUtil.generateEdges(a.getNome(),
												a2.getNome(), 1);
									}
								}
							}
						}
					}
				}
			}
		}

	}

	private static void criaArquivo(String nomeDoMeuGrafo) {
		XMLUtil.fechaArquivo();
		XMLUtil.salvarXML(nomeDoMeuGrafo);

	}

	private static void salvarArquivo(String path) {
		XMLUtil.salvarXML(path);
	}

	private static boolean verificarAutor(String autor, Set<Autor> set) {
		for (Autor a2 : set) {
			if (a2.getNome() != null && !autor.equals(a2.getNome())) {
				return true;
			}
		}
		return false;
	}

	private static boolean verificar(String id, List<String> lista) {

		for (String teste : lista) {
			if (id.equals(teste)) {
				return false;
			}
		}

		return true;
	}

}
