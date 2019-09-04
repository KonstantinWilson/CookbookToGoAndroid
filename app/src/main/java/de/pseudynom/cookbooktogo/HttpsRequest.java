package de.pseudynom.cookbooktogo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Konstantin on 26.10.2018.
 */

public class HttpsRequest extends AsyncTask<String, Void, Object>{
	public static final int TYPE_JSON = 1;
	public static final int TYPE_BITMAP = 2;

	private Exception exception;
	private Responsable parent = null;
	private String urlString = null;
	private int type = 0;

	public HttpsRequest(Responsable parent, String url, int type){
		System.out.println("HttpsRequest.HTTPS_TYPE: " + type);
		this.parent = parent;
		urlString = url;
		this.type = type;
	}

	protected Object doInBackground(String... args){
		System.out.println("doInBackground.HTTPS_TYPE: " + this.type);
		switch (this.type){
			case TYPE_JSON:
				return httpsString(args);
			case TYPE_BITMAP:
				return httpsBitmap(args);
			default:
				return null;
		}
	}

	public String httpsString(String... args){
		URL url = null;
		StringBuilder response = new StringBuilder();
		try{
			//return Resources.getSystem().getString(R.string.url_recipes);
			//url = new URL(Resources.getSystem().getString(R.string.url_recipes));
			url = new URL(urlString);
		}
		catch(Exception e){
			return e.toString(); //https://cookbook-togo.com/recipes/recipes.php
		}

		HttpsURLConnection connection = null;
		try{
			connection = (HttpsURLConnection)url.openConnection();
			connection.setDoOutput(false);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

			int status = connection.getResponseCode();
			if (status != 200) {
				throw new IOException("Post failed with error code " + status);
			} else {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				int count = 0;
				while ((inputLine = in.readLine()) != null) {
					count++;
					response.append(inputLine);
				}
				in.close();
			}
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null; //e.toString();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}

			//Here is your json in string format
			//tvMessage.setText("JSON: " + responseJSON);
		}
	}

	public Object httpsBitmap(String... args) {
		try {
			/*
			System.out.println("httpsDrawableFirst");
			InputStream inputStream = (InputStream)new URL(urlString).getContent();
			Drawable d = Drawable.createFromStream(inputStream, "src name");
			System.out.println("httpsDrawableSecond");
			return d;
			*/
			URL url = new URL(urlString);
			Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			return image;
		}
		catch (Exception e){
			System.out.println("httpsDrawableException: " + e.toString());
			return e;
		}
	}

	protected void onPostExecute(Object result){
		parent.response(result);
	}
}
