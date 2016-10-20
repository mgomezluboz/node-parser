package ar.edu.utn.frba.dds.SistemaDeTweets.Controller;

import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Pure;
import org.json.simple.JSONObject;

@Accessors
@SuppressWarnings("all")
public class Tweet {
  private String fechaDeCreacion;
  
  private String contenido;
  
  private String retweets;
  
  private String user;
  
  private String screenName;
  
  public Tweet() {
  }
  
  public Tweet(final String unaFecha, final String unContenido, final String cantRetweets, final String unUser, final String unName) {
    this.fechaDeCreacion = unaFecha;
    this.contenido = unContenido;
    this.retweets = cantRetweets;
    this.user = unUser;
    this.screenName = unName;
  }
  
  public static Tweet tweetFromJson(final Object tweetRaw) {
    JSONObject tweetJson = ((JSONObject) tweetRaw);
    Object _get = tweetJson.get("created_at");
    String _string = _get.toString();
    Object _get_1 = tweetJson.get("text");
    String _string_1 = _get_1.toString();
    Object _get_2 = tweetJson.get("retweet_count");
    String _string_2 = _get_2.toString();
    Object _get_3 = tweetJson.get("user");
    Object _get_4 = ((JSONObject) _get_3).get("name");
    String _string_3 = _get_4.toString();
    Object _get_5 = tweetJson.get("user");
    Object _get_6 = ((JSONObject) _get_5).get("screen_name");
    String _string_4 = _get_6.toString();
    Tweet transientTweet = new Tweet(_string, _string_1, _string_2, _string_3, _string_4);
    return transientTweet;
  }
  
  public String imprimite() {
    return InputOutput.<String>println((((((((((("Tweet:\n Creado: " + this.fechaDeCreacion) + "\n Contenido: ") + this.contenido) + "\n Autor: @") + this.screenName) + " - ") + this.user) + "\n Retweets: ") + this.retweets) + "\n"));
  }
  
  @Pure
  public String getFechaDeCreacion() {
    return this.fechaDeCreacion;
  }
  
  public void setFechaDeCreacion(final String fechaDeCreacion) {
    this.fechaDeCreacion = fechaDeCreacion;
  }
  
  @Pure
  public String getContenido() {
    return this.contenido;
  }
  
  public void setContenido(final String contenido) {
    this.contenido = contenido;
  }
  
  @Pure
  public String getRetweets() {
    return this.retweets;
  }
  
  public void setRetweets(final String retweets) {
    this.retweets = retweets;
  }
  
  @Pure
  public String getUser() {
    return this.user;
  }
  
  public void setUser(final String user) {
    this.user = user;
  }
  
  @Pure
  public String getScreenName() {
    return this.screenName;
  }
  
  public void setScreenName(final String screenName) {
    this.screenName = screenName;
  }
}
