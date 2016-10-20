package ar.edu.utn.frba.dds.SistemaDeTweets.Controller

import org.json.simple.JSONObject
import org.eclipse.xtend.lib.annotations.Accessors

@Accessors
class Tweet {
	String fechaDeCreacion
	String contenido
	String retweets
	String user	
	String screenName
	
	new() {
		
	}
	
	new(String unaFecha, String unContenido, String cantRetweets, String unUser, String unName) {
		fechaDeCreacion = unaFecha
		contenido = unContenido
		retweets = cantRetweets
		user = unUser
		screenName = unName
	}
	
	static def Tweet tweetFromJson(Object tweetRaw) {
		var JSONObject tweetJson = (tweetRaw as JSONObject)
		
		var Tweet transientTweet = new Tweet(tweetJson.get("created_at").toString,
		tweetJson.get("text").toString,
		tweetJson.get("retweet_count").toString,
		(tweetJson.get("user") as JSONObject).get("name").toString,
		(tweetJson.get("user") as JSONObject).get("screen_name").toString)
		
		return transientTweet
	}
	
	def imprimite() {
		println("Tweet:\n Creado: " + fechaDeCreacion + "\n Contenido: " + contenido + "\n Autor: @" + screenName + " - " + user + "\n Retweets: " + retweets + "\n")
	}
}