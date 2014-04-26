package br.com.ufpb.appsnaauthorrank.post;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import br.com.ufpb.appsnaauthorrank.util.StringUtil;


public class PostParaCiteApi {

	private static final String url = "http://paracite.eprints.org/cgi-bin/paracite.cgi?ref=";

	public static String postCitationApi(String citation) throws Exception {

		URL obj = new URL(url + URLEncoder.encode(citation, "UTF-8"));
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// Send post request
		con.setDoOutput(true);
		InputStreamReader inputReader = new InputStreamReader(
				con.getInputStream());
		BufferedReader bufferedReader = new BufferedReader(inputReader);

		String linha = "";
		StringBuffer pag = new StringBuffer();
		while ((linha = bufferedReader.readLine()) != null) {
			pag.append(linha+"\n");
		}

		return pag.toString();
	}
	
	public static void main(String[] args) {
		try {
			System.out
			.println(postCitationApi("de Laat, M., Network and Content in an Online Community Discourse, from http://www.uu.nl/uupublish/content/2002%20Networked%20Learning%201.pd, 2002."));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
