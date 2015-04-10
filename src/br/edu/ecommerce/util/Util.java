package br.edu.ecommerce.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import br.edu.ecommerce.entidades.Produto;

import com.google.gson.Gson;

public class Util {

	public static StringBuilder converterJSONUTF8(List<Produto> produtos) {
		Gson gson = new Gson();
		StringBuilder produtosJson = new StringBuilder();
		String string;
		try {
			InputStream inputStream = new ByteArrayInputStream(gson.toJson(produtos).getBytes(StandardCharsets.UTF_8));
			if (inputStream != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				while (null != (string = reader.readLine())) {
					produtosJson.append(string);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return produtosJson;
	}
	
}
