const express = require("express");
const app = express();
const fs = require("fs");
const exphbs = require("express-handlebars");
const bodyParser = require("body-parser");

app.use(express.static(__dirname + '/dist'));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
const motor = exphbs.create(
    {
        defaultLayout: 'main',
        extname: 'hbs',
    });

app.engine('hbs', motor.engine);

app.set("view engine", "hbs");


app.get(['/', '/index'], (req, res) => {
    res.render("index");
})

app.get("/home", (req, res) => {
    res.render("index");
})

app.listen(8080, () => {
    console.log("Server iniciado");
});