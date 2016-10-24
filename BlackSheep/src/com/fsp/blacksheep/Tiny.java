package com.fsp.blacksheep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Tiny {
	public static String CreateUrl(String original) {
		String tinyUrl = null;
		try {

			HttpClient client = new DefaultHttpClient();
			String urlTemplate = "http://tinyurl.com/api-create.php?url=%s";
			String uri = String
					.format(urlTemplate, URLEncoder.encode(original));
			HttpGet request = new HttpGet(uri);
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			InputStream in = entity.getContent();
			try {
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == HttpStatus.SC_OK) {
					// TODO: Support other encodings
					String enc = "utf-8";
					Reader reader = new InputStreamReader(in, enc);
					BufferedReader bufferedReader = new BufferedReader(reader);
					tinyUrl = bufferedReader.readLine();
					if (tinyUrl != null) {
						System.out.println("Created Url-" + tinyUrl);
					} else {
						throw new IOException("empty response");
					}
				} else {
					String errorTemplate = "unexpected response: %d";
					String msg = String.format(errorTemplate, statusCode);
					throw new IOException(msg);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			tinyUrl = "ERROR";
			System.out.println("tiny url error=" + e);
		}

		return tinyUrl;

	}
}
