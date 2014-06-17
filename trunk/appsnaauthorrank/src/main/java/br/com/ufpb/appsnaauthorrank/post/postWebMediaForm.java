package br.com.ufpb.appsnaauthorrank.post;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class postWebMediaForm {

	// FORM IEEE

	private static final String URL_FORM = "http://paperz.zn.inf.br/";
	private static final Integer TIMEOUT_VALUE = 10000;

	public static String post(String busca) throws Exception {
		String data = "";

		try {
			data = URL_FORM + busca;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return obterPagina(data,0);
	}

	public static String obterPagina(String caminho, Integer countConnections) {
		try {
			if(countConnections >= 3){
				return null;
			}
			URL url = new URL(caminho);
			URLConnection urlConnection = url.openConnection();

			// Obtem as respostas
			urlConnection.setDoOutput(true);
			urlConnection.setConnectTimeout(TIMEOUT_VALUE);
			urlConnection.setReadTimeout(TIMEOUT_VALUE);
			InputStreamReader inputReader = new InputStreamReader(
					urlConnection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputReader);

			String linha = "";
			StringBuffer pag = new StringBuffer();
			while ((linha = bufferedReader.readLine()) != null) {
				pag.append(linha);
			}

			return pag.toString();
		} catch (SocketTimeoutException e) {
			return obterPagina(caminho,countConnections+1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
