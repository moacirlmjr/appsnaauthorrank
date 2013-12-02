package br.com.ufpb.appsnaauthorrank.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;
import br.com.ufpb.appsnaauthorrank.beans.Autor;
import br.com.ufpb.appsnaauthorrank.post.postIeeeForm;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.select.Elements;

public class ParserHtmlIEEEDetalhe {

    private static final String AUTHOR = "authors";
    private static final String DOCS = "docs";
    private static final String URL_IEEE = "http://ieeexplore.ieee.org";

    public static Artigo realizarParserHtml(Artigo artigo)
            throws Exception {

        List<Artigo> referencias = new ArrayList<Artigo>();
        
        if (artigo.getLinkDetalhe() != null) {
            Document doc = Jsoup.parse(postIeeeForm.obterPagina(URL_IEEE+artigo.getLinkDetalhe()));

            if (!doc.getElementsByClass(AUTHOR).isEmpty()) {
                Element divAuthor = doc.getElementsByClass(AUTHOR).first();
                for (Element e : divAuthor.select(".authorPreferredName, .prefNameLink")) {
                    Autor autor = new Autor();
                    autor.setNome(e.text().trim());
                    if(!artigo.getAutores().contains(autor)){
                        artigo.getAutores().add(autor);
                        System.out.println("autor add: "+autor.getNome());
                        System.out.println("do titulo: "+artigo.getTitulo());
                    }
                }

            }
            
            String urlReferencias = artigo.getLinkDetalhe().replace("articleDetails", "abstractReferences");
            String pagina = postIeeeForm.obterPagina(URL_IEEE+urlReferencias);
            doc = Jsoup.parse(pagina);
            if (!doc.select(".docs").isEmpty()) {
                //Element divDocs = doc.getElementsByClass(DOCS).first();
                for (Element e : doc.select(".docs a")) {
                    if(e.text().equals("Abstract")){
                        System.out.println(e.attr("href"));
                    }                      
                }
            }
        }

        return artigo;
    }
}