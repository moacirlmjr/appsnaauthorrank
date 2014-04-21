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


public class PostFreeCiteApi {

	private static final String url = "http://freecite.library.brown.edu/citations/create";

	public static String postCitationApi(String citation) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("Accept", "text/xml");
		con.setRequestProperty("user-agent", "Mozilla/5.0");

		String urlParameters = "citation=" + URLEncoder.encode(citation, "UTF-8");

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		return response.toString();
	}
	
	public static String postCitationApi(List<String> citations) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("Accept", "text/xml");
		con.setRequestProperty("user-agent", "Mozilla/5.0");

		String urlParameters = "";
		for(String citation : citations){
			urlParameters+= "citation[]=" +  URLEncoder.encode(StringUtil.tratarCitacao(citation), "UTF-8") + "&";
		}

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		return response.toString();
	}

	public static void main(String[] args) {
		try {
//			System.out
//					.println(postCitationApi("Hong, W., Han, X. P., Zhou, T. & Wang, B. H. Scaling Behaviors in Short-Message Communication"));
			List<String> citations = new ArrayList<>();
			citations.add("Spatariu, A., Hartley, K. and Bendixen, L.D. 2004, \"Defining and measuring quality in on-line discussion\", Journal of Interactive Online Learning. Vol 2 (4)");
			citations.add("de Laat, M., Lally, V., Lipponen, L., Simon, R., J., \"Investigating patterns of interaction in networked learning and computer-supported collaborative learning: A role for Social Network Analysis\", Journal of Computer Supported Collaborative Learning, 2007. ");
			citations.add("Harasim, L., Hiltz, S. R., Teles, L., & TuroV, M, \"Learning networks: A Weld guide to teaching and learning online. Cambridge, MA: MIT Press, 1995. ");
			citations.add("de Laat, M., Network and Content in an Online Community Discourse, from http://www.uu.nl/uupublish/content/2002%20Networked%20Learning%201.pd, 2002. ");
			
			System.out
			.println(postCitationApi(citations));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
