package br.com.ufpb.appsnaauthorrank.post;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class postIeeeForm {

	// FORM IEEE

	private static final String URL_FORM = "http://ieeexplore.ieee.org/search/searchresult.jsp";
	private static final Integer TIMEOUT_VALUE = 10000;

	public static String post(String busca, int pagina) throws Exception {
		String data = "";

		try {
			data = URL_FORM + "?queryText=" + URLEncoder.encode(busca, "UTF-8")
					+ "&pageNumber=" + pagina + "&rowsPerPage=100";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return obterPagina(data, 0);
	}

	public static String obterPagina(String caminho, Integer countConnections) {
		try {
			if (countConnections >= 3) {
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
			return obterPagina(caminho, countConnections + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String obterResultadoUrlPOST(String urlString,
			String parametros) throws Exception {
		try {
			String linha = "";
			String linhaRetorno = "";

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(urlString);

			// Request parameters and other properties.
			if (parametros != null) {
				List<NameValuePair> params = new ArrayList<NameValuePair>(2);
				for (String param : parametros.split("\\&")) {
					String[] p = param.split("=");
					params.add(new BasicNameValuePair(p[0],
							p.length == 2 ? p[1] : ""));
				}
				httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			}

			// Execute and get the response.
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				entity = response.getEntity();
				InputStream instream = entity.getContent();
				try {
					InputStreamReader isr = new InputStreamReader(instream);
					BufferedReader br = new BufferedReader(isr);
					while ((linha = br.readLine()) != null) {
						linhaRetorno += linha;
					}
				} finally {
					instream.close();
				}

			}
			return linhaRetorno;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
