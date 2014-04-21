package br.com.ufpb.appsnaauthorrank.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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

	public static void main(String[] args) {
		System.out
				.println(obterResultadoUrl("http://ieeexplore.ieee.org/search/searchresult.jsp?newsearch=true&queryText=SNA+online&x=0&y=0"));
		
		System.out
		.println(obterResultadoUrl("http://ieeexplore.ieee.org/search/searchresult.jsp?queryText%3DSNA+online&pageNumber=2"));
	}

}
