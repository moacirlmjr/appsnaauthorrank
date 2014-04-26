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

public class PostParsCitApi {

	private static final String url = "http://aye.comp.nus.edu.sg/parsCit/parsCit.cgi";

	public static String postCitationApi(String citation) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		con.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=----WebKitFormBoundaryVxLBp3AVkup705BG");
		con.setRequestProperty(
				"User-agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.72 Safari/537.36");

		String urlParameters = "textlines="
				+ URLEncoder.encode(citation, "UTF-8")+ "&demo=3&bib3=on";

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
			citations.add("Tsai, W. and S. Ghoshal, Social Capital and Value Creation: The Role of Intrafirm Networks. The Academy of Management Journal, 1998. 41(4): p. 464-476. ");
			citations.add("de Laat, M., Lally, V., Lipponen, L., Simon, R., J., \"Investigating patterns of interaction in networked learning and computer-supported collaborative learning: A role for Social Network Analysis\", Journal of Computer Supported Collaborative Learning, 2007. ");
			citations.add("Harasim, L., Hiltz, S. R., Teles, L., & TuroV, M, \"Learning networks: A Weld guide to teaching and learning online. Cambridge, MA: MIT Press, 1995. ");
			citations.add("de Laat, M., Network and Content in an Online Community Discourse, from http://www.uu.nl/uupublish/content/2002%20Networked%20Learning%201.pd, 2002. ");
			
			System.out
			.println(postCitationApi("Tsai, W. and S. Ghoshal, Social Capital and Value Creation: The Role of Intrafirm Networks. The Academy of Management Journal, 1998. 41(4): p. 464-476. "));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
