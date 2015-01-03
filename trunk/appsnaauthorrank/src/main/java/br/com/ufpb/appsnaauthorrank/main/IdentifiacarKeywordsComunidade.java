package br.com.ufpb.appsnaauthorrank.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class IdentifiacarKeywordsComunidade {
	
	public static void main(String[] args) throws Exception {
		FileReader fr = new FileReader("C:\\Users\\moacir\\Desktop\\keywords 6090.csv");
		BufferedReader in = new BufferedReader(fr);
		String line;
		Map<String, Integer> map = new HashMap<>();
		while ((line = in.readLine()) != null) {
			for(String s: line.split(",")){
				if(!s.equals("")){
					if(map.containsKey(s)){
						map.put(s, map.get(s) + 1);
					}else{
						map.put(s, 0);
					}
				}
			}
		}
		
		for(String key : map.keySet()){
			System.out.println(key+"," + map.get(key));
		}
	}

}
