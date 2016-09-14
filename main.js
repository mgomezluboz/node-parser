var express = require('express');
var app = express();

var Parser = require("simple-text-parser");
var parser = new Parser();

var texto = "Hola. Quisiera saber más sobre la propuesta laboral para Sirena. Dejo mis datos: Nombre: Martín - Apellido: Gomez Luboz - Teléfono: 1567913672 - Email: martingomezluboz@gmail.com";
var texto2 = "Hola. Quisiera saber más sobre la propuesta laboral para Sirena. Dejo mis datos: Nombre: Martín Apellido: Gomez Luboz Teléfono: 1567913672 Email: martingomezluboz@gmail.com";

app.get('/', function(req, res) {

	parser.addRule(/(nombre:\s|apellido:\s|teléfono:\s|email:\s)/ig, function() {});
	
	var arbolParseado = parser.toTree(texto);
	var arbolParseado2 = parser.toTree(texto2);
	
	var jason = {
		"nombre": arbolParseado[1].text.split("-")[0].trim(),
		"apellido": arbolParseado[2].text.split("-")[0].trim(),
		"telefono": arbolParseado[3].text.split("-")[0].trim(),
		"email": arbolParseado[4].text.trim()
	};
	
	var jason2 = {
		"nombre": arbolParseado2[1].text.trim(),
		"apellido": arbolParseado2[2].text.trim(),
		"telefono": arbolParseado2[3].text.trim(),
		"email": arbolParseado2[4].text.trim()
	};	
	
	res.send("Jason del texto con guiones:</br>" + JSON.stringify(jason) + "</br></br>Jason del texto sin guiones:</br>" + JSON.stringify(jason2));
})

var server = app.listen(3000, function() {
	
	console.log("Aplicacion escuchando pedidos en http://localhost:3000/")
	
})