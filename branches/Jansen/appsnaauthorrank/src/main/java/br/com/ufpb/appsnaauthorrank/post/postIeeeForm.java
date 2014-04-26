package br.com.ufpb.appsnaauthorrank.post;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class postIeeeForm {

	// FORM IEEE

	private static final String URL_FORM = "http://ieeexplore.ieee.org/search/searchresult.jsp";

	public static String post(String busca, int pagina) throws Exception {
		String data = "";

		try {
			data = URL_FORM + "?queryText=" + URLEncoder.encode(busca, "UTF-8")
					+ "&pageNumber=" + pagina + "&rowsPerPage=100";
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
