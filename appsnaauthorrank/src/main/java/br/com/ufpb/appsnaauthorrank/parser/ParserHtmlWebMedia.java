package br.com.ufpb.appsnaauthorrank.parser;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;
import br.com.ufpb.appsnaauthorrank.beans.Autor;
import br.com.ufpb.appsnaauthorrank.beans.Evento;
import br.com.ufpb.appsnaauthorrank.post.postWebMediaForm;

public class ParserHtmlWebMedia {

	private String query;

	public static List<Evento> realizarParserHtml(String query) throws Exception {

		List<Evento> eventos = new ArrayList<Evento>();
		Document doc = Jsoup.parse(postWebMediaForm.post(query));

		for (Element linha : doc.select("tr")) {
			Elements eTd = linha.getElementsByTag("td");
			Evento evento = new Evento();
			int count = 0;
			for(Element coluna: eTd){
				switch(count){
				case 0:
					evento.setNome(URLEncoder.encode(coluna.text().toLowerCase(), "UTF-8"));
					evento.setUrl(coluna.getElementsByTag("a").first().attr("href"));
					break;
				case 2:
					evento.setAno(Integer.parseInt(coluna.text()));
					break;
				case 3:
					evento.setLocal(coluna.text());
					break;
				}
				count++;
			}
			if(evento.getNome() != null)
				eventos.add(evento);
		}
		
		for(Evento evento: eventos){
			doc = Jsoup.parse(postWebMediaForm.post(evento.getUrl()));
			evento.setArtigos(new LinkedList<Artigo>());
			for (Element linha : doc.select("tr")) {
				Elements eTd = linha.getElementsByTag("td");
				Artigo artigo = new Artigo();
				int count = 0;
				for(Element coluna: eTd){
					switch(count){
					case 0:
						String titulo = coluna.getElementsByTag("a").first().text();
						artigo.setTitulo(titulo.toLowerCase());
						break;
					case 3:
						artigo.setAutores(new HashSet<Autor>());
						Autor autor = new Autor();
						for(Element linkAutor:coluna.getElementsByTag("a")){
							if(linkAutor.attr("href").contains("author")){
								autor.setNome(linkAutor.text());
							}else if(linkAutor.attr("href").contains("institutes")){
								autor.setInstituicao(linkAutor.text());
								artigo.getAutores().add(autor);
								autor = new Autor();
							}
						}
						break;
					}
					count++;
				}
				if(artigo.getTitulo() != null)
					evento.getArtigos().add(artigo);
			}
			
		}
		
		return eventos;
	}
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
