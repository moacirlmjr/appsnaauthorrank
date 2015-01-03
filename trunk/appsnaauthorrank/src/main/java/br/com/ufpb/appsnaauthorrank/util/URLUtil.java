package br.com.ufpb.appsnaauthorrank.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

public class URLUtil {

	public static String obterResultadoUrl(String urlString) {
		String linha = "";
		String linhaRetorno = "";
		try {
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);

			// Get the response
			InputStream is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			while ((linha = br.readLine()) != null) {
				linhaRetorno += linha + "\n";
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return linhaRetorno;
	}

	public static void main(String[] args) throws IOException {
		
		for(int i = 1; i <= 26; i++){
			String html = obterResultadoUrl("http://link.springer.com/search/page/"+i+"?date-facet-mode=between&query=%22social+network+analysis%22+and+online&facet-start-year=2013&facet-end-year=2014&facet-content-type=%22Article%22");
			Files.write(Paths.get(".\\Search Results - Springer" + i +".html"), html.getBytes());
		}
	}

}
