package ar.edu.utn.frba.dds.SistemaDeTweets.Controller

import org.uqbar.xtrest.api.XTRest
import java.net.URLEncoder
import org.uqbar.xtrest.api.annotation.Controller
import org.uqbar.xtrest.api.annotation.Get
import org.uqbar.xtrest.api.Result
import org.uqbar.xtrest.http.ContentType
import org.uqbar.xtrest.json.JSONUtils
import ar.edu.utn.frba.dds.SistemaDeTweets.Exception.BusinessException
import org.uqbar.xtrest.api.annotation.Put
import org.uqbar.xtrest.api.annotation.Body
import org.uqbar.xtrest.api.annotation.Post
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.NameValuePair
import java.util.ArrayList
import java.util.List
import org.json.simple.JSONObject
import org.json.simple.JSONValue
import org.apache.http.client.methods.HttpGet
import org.json.simple.JSONArray

@Controller
class MainController {
	
	private static final String AUTH_URL = "https://api.twitter.com/oauth2/token"
	private static final String API_TOKEN = "EvspXtDKDfPTXVeQXIHzgpfOO:VBLSh6mtdB3vsJbkK4VNU9ZcDr9gdHqgdstac1AF78umB7AcLO"
	private static final String TOKEN_B64 = "RXZzcFh0REtEZlBUWFZlUVhJSHpncGZPTzpWQkxTaDZtdGRCM3ZzSmJrSzRWTlU5WmNEcjlnZEhxZ2RzdGFjMUFGNzh1bUI3QWNMTw=="
	
	private static final String TWEETS_URL = "https://api.twitter.com/1.1/search/tweets.json"
	
	extension JSONUtils = new JSONUtils
	
	static String bearerToken
	
	def static void main(String[] args) {
		try {
			var DefaultHttpClient httpClient = new DefaultHttpClient()
			var HttpPost postRequest = new HttpPost(AUTH_URL)
			
			postRequest.addHeader("Authorization", "Basic " + TOKEN_B64)
			postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
			
			var List<NameValuePair> nvps = new ArrayList<NameValuePair>()
			nvps.add(new BasicNameValuePair("grant_type", "client_credentials"))
			postRequest.setEntity(new UrlEncodedFormEntity(nvps))
			
			var HttpResponse response = httpClient.execute(postRequest)
				
			/*if (response.getStatusLine().getStatusCode() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatusLine().getStatusCode());
			}*/
	
			var BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
	
			var String output;
			var JSONObject bearerJson
			
			while ((output = br.readLine()) != null) {
				bearerJson = (JSONValue.parse(output) as JSONObject)
				bearerToken = bearerJson.get("access_token").toString
			}
			
			httpClient.getConnectionManager().shutdown();
		}
		catch (Exception e) {
			println(e.message)
		}
		
		XTRest.start(MainController, 9000)	
	}
	
	static def imprimirContenidoDeTweet(Object tweetRaw) {
		var JSONObject tweetJson = (tweetRaw as JSONObject)
		
		println("Contenido de tweet: " + tweetJson.get("text").toString)
	}
	
	@Post("/tweets")
	def Result tweets(@Body String body) {
		try {
			val stringQuery = body.fromJson(String)
			val List<Tweet> tweets = new ArrayList<Tweet>()
			
			response.contentType = ContentType.APPLICATION_JSON
			
			var DefaultHttpClient httpClient = new DefaultHttpClient()
			
			var HttpGet getTweetsRequest = new HttpGet(TWEETS_URL + "?q=" + URLEncoder.encode(stringQuery, "UTF-8"))
				
			getTweetsRequest.addHeader("Authorization", "Bearer " + bearerToken)
			var HttpResponse tweetResponse = httpClient.execute(getTweetsRequest)
			
			var BufferedReader br2 = new BufferedReader(new InputStreamReader((tweetResponse.getEntity().getContent())));
			var JSONArray tweetsEnJson
			var JSONObject statuses
			var String output
			
			
			while ((output = br2.readLine()) != null) {
				//println(output)
				statuses = (JSONValue.parse(output) as JSONObject)
				tweetsEnJson = (statuses.get("statuses") as JSONArray)
			}
			
			for(tweet : tweetsEnJson) {
				var tweetActual = Tweet.tweetFromJson(tweet)
				//tweetActual.imprimite()
				tweets.add(tweetActual)
			}
			
			httpClient.getConnectionManager().shutdown();
			
			println("Tweet request served.")
			
			ok(tweets.toJson)
		} catch (Exception e) {
			badRequest("Request de tweet erroneo.")
		}
	}
}