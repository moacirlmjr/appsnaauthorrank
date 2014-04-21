package br.com.ufpb.appsnaauthorrank.post;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class postAcmForm {

	// FORM IEEE

	private static final String URL_FORM = "http://dl.acm.org/results.cfm?h=1&cfid=281408275&cftoken=64820829";

	public static String post(String busca, int pagina) throws Exception {
		String data = "";

		try {
			data = URL_FORM + "&query=" + URLEncoder.encode(busca, "UTF-8")
					+ "&querydisp=" + URLEncoder.encode(busca, "UTF-8")
					+ "&source_query="
					+ "&start="+ pagina
					+ "&srt=score%20dsc&short=0&source_disp=&since_month=&since_year=&before_month=&before_year=&coll=DL&dl=GUIDE&termshow=matchall&range_query=&CFID=281408275&CFTOKEN=64820829";
					
		} catch (Exception e) {
			e.printStackTrace();
		}

		return obterPagina(data);
	}

	public static String obterPagina(String caminho) throws Exception {

		URL url = new URL(caminho);
		URLConnection urlConnection = url.openConnection();

		// Obtem as respostas
		urlConnection.setDoOutput(true);
		InputStreamReader inputReader = new InputStreamReader(
				urlConnection.getInputStream());
		BufferedReader bufferedReader = new BufferedReader(inputReader);

		String linha = "";
		StringBuffer pag = new StringBuffer();
		while ((linha = bufferedReader.readLine()) != null) {
			pag.append(linha);
		}

		return pag.toString();
	}
}
