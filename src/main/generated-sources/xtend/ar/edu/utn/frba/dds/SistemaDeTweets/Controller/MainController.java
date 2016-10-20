package ar.edu.utn.frba.dds.SistemaDeTweets.Controller;

import ar.edu.utn.frba.dds.SistemaDeTweets.Controller.Tweet;
import com.google.common.base.Objects;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.server.Request;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.uqbar.xtrest.api.Result;
import org.uqbar.xtrest.api.XTRest;
import org.uqbar.xtrest.api.annotation.Body;
import org.uqbar.xtrest.api.annotation.Controller;
import org.uqbar.xtrest.api.annotation.Post;
import org.uqbar.xtrest.http.ContentType;
import org.uqbar.xtrest.json.JSONUtils;
import org.uqbar.xtrest.result.ResultFactory;

@Controller
@SuppressWarnings("all")
public class MainController extends ResultFactory {
  private final static String AUTH_URL = "https://api.twitter.com/oauth2/token";
  
  private final static String API_TOKEN = "EvspXtDKDfPTXVeQXIHzgpfOO:VBLSh6mtdB3vsJbkK4VNU9ZcDr9gdHqgdstac1AF78umB7AcLO";
  
  private final static String TOKEN_B64 = "RXZzcFh0REtEZlBUWFZlUVhJSHpncGZPTzpWQkxTaDZtdGRCM3ZzSmJrSzRWTlU5WmNEcjlnZEhxZ2RzdGFjMUFGNzh1bUI3QWNMTw==";
  
  private final static String TWEETS_URL = "https://api.twitter.com/1.1/search/tweets.json";
  
  @Extension
  private JSONUtils _jSONUtils = new JSONUtils();
  
  private static String bearerToken;
  
  public static void main(final String[] args) {
    try {
      DefaultHttpClient httpClient = new DefaultHttpClient();
      HttpPost postRequest = new HttpPost(MainController.AUTH_URL);
      postRequest.addHeader("Authorization", ("Basic " + MainController.TOKEN_B64));
      postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
      List<NameValuePair> nvps = new ArrayList<NameValuePair>();
      BasicNameValuePair _basicNameValuePair = new BasicNameValuePair("grant_type", "client_credentials");
      nvps.add(_basicNameValuePair);
      UrlEncodedFormEntity _urlEncodedFormEntity = new UrlEncodedFormEntity(nvps);
      postRequest.setEntity(_urlEncodedFormEntity);
      HttpResponse response = httpClient.execute(postRequest);
      HttpEntity _entity = response.getEntity();
      InputStream _content = _entity.getContent();
      InputStreamReader _inputStreamReader = new InputStreamReader(_content);
      BufferedReader br = new BufferedReader(_inputStreamReader);
      String output = null;
      JSONObject bearerJson = null;
      while ((!Objects.equal((output = br.readLine()), null))) {
        {
          Object _parse = JSONValue.parse(output);
          bearerJson = ((JSONObject) _parse);
          Object _get = bearerJson.get("access_token");
          String _string = _get.toString();
          MainController.bearerToken = _string;
        }
      }
      ClientConnectionManager _connectionManager = httpClient.getConnectionManager();
      _connectionManager.shutdown();
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        String _message = e.getMessage();
        InputOutput.<String>println(_message);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    XTRest.start(MainController.class, 9000);
  }
  
  public static String imprimirContenidoDeTweet(final Object tweetRaw) {
    String _xblockexpression = null;
    {
      JSONObject tweetJson = ((JSONObject) tweetRaw);
      Object _get = tweetJson.get("text");
      String _string = _get.toString();
      String _plus = ("Contenido de tweet: " + _string);
      _xblockexpression = InputOutput.<String>println(_plus);
    }
    return _xblockexpression;
  }
  
  @Post("/tweets")
  public Result tweets(@Body final String body, final String target, final Request baseRequest, final HttpServletRequest request, final HttpServletResponse response) {
    Result _xtrycatchfinallyexpression = null;
    try {
      Result _xblockexpression = null;
      {
        final String stringQuery = this._jSONUtils.<String>fromJson(body, String.class);
        final List<Tweet> tweets = new ArrayList<Tweet>();
        response.setContentType(ContentType.APPLICATION_JSON);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String _encode = URLEncoder.encode(stringQuery, "UTF-8");
        String _plus = ((MainController.TWEETS_URL + "?q=") + _encode);
        HttpGet getTweetsRequest = new HttpGet(_plus);
        getTweetsRequest.addHeader("Authorization", ("Bearer " + MainController.bearerToken));
        HttpResponse tweetResponse = httpClient.execute(getTweetsRequest);
        HttpEntity _entity = tweetResponse.getEntity();
        InputStream _content = _entity.getContent();
        InputStreamReader _inputStreamReader = new InputStreamReader(_content);
        BufferedReader br2 = new BufferedReader(_inputStreamReader);
        JSONArray tweetsEnJson = null;
        JSONObject statuses = null;
        String output = null;
        while ((!Objects.equal((output = br2.readLine()), null))) {
          {
            Object _parse = JSONValue.parse(output);
            statuses = ((JSONObject) _parse);
            Object _get = statuses.get("statuses");
            tweetsEnJson = ((JSONArray) _get);
          }
        }
        for (final Object tweet : tweetsEnJson) {
          {
            Tweet tweetActual = Tweet.tweetFromJson(tweet);
            tweets.add(tweetActual);
          }
        }
        ClientConnectionManager _connectionManager = httpClient.getConnectionManager();
        _connectionManager.shutdown();
        InputOutput.<String>println("Tweet request served.");
        String _json = this._jSONUtils.toJson(tweets);
        _xblockexpression = ResultFactory.ok(_json);
      }
      _xtrycatchfinallyexpression = _xblockexpression;
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        _xtrycatchfinallyexpression = ResultFactory.badRequest("Request de tweet erroneo.");
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return _xtrycatchfinallyexpression;
  }
  
  public void handle(final String target, final Request baseRequest, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
    {
    	Matcher matcher = 
    		Pattern.compile("/tweets").matcher(target);
    	if (request.getMethod().equalsIgnoreCase("Post") && matcher.matches()) {
    		// take parameters from request
    		String body = readBodyAsString(request);
    		
    		// take variables from url
    		
    		
    	    Result result = tweets(body, target, baseRequest, request, response);
    	    result.process(response);
    	    
    		response.addHeader("Access-Control-Allow-Origin", "*");
    	    baseRequest.setHandled(true);
    	    return;
    	}
    }
    this.pageNotFound(baseRequest, request, response);
  }
  
  public void pageNotFound(final Request baseRequest, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
    response.getWriter().write(
    	"<html><head><title>XtRest - Page Not Found!</title></head>" 
    	+ "<body>"
    	+ "	<h1>Page Not Found !</h1>"
    	+ "	Supported resources:"
    	+ "	<table>"
    	+ "		<thead><tr><th>Verb</th><th>URL</th><th>Parameters</th></tr></thead>"
    	+ "		<tbody>"
    	+ "			<tr>"
    	+ "				<td>POST</td>"
    	+ "				<td>/tweets</td>"
    	+ "				<td>body</td>"
    	+ "			</tr>"
    	+ "		</tbody>"
    	+ "	</table>"
    	+ "</body>"
    );
    response.setStatus(404);
    baseRequest.setHandled(true);
  }
}
