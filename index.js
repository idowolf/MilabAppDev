let express = require('express');
let app = express();
let fs = require('fs');

function getFormattedTime() {
    return new Date().toISOString().
        replace(/T/, ' ').
        replace(/\..+/, '');
}

function readWithFilename(res,filename) {
    fs.readFile(filename, 'utf8', function(err, contents) {
        if(err)
            res.send(`Error loading file ${filename}`);
        else
            res.send(contents);
    });
}

app.set('port', (process.env.PORT || 5000));

app.get('/', (req, res) => {
    res.send(`Usage: 1. /getTime 2. /getFile?filename=ABC.txt 3. /getFile/ABC.txt`);
});

app.get('/getFile', (req, res) => {
    let filename = req.query.filename || "<unknown>";
    readWithFilename(res,filename)});

app.get('/getFile/:filename', function(req, res) {
    let filename = req.params.filename;
    readWithFilename(res,filename);
});

app.get('/getTime', (req, res) => {
    res.send(getFormattedTime());
});

app.listen(app.get('port'), function() {
    console.log('Node app is running on port', app.get('port'));
});


