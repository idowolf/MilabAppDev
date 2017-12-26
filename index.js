const express = require('express');
const app = express();
const fs = require('fs');
const mime = require('mime-types');

app.set('port', (process.env.PORT || 5000));

app.get('/', (req, res) => {
    res.send(`Usage: /files/filename. Files include: 'dankmeme(1-10).png' OR 'TestFile(1-3).txt'`);
});

app.get('/files/:filename', function(req, res) {
    let filename = req.params.filename;
    res.setHeader("Content-Type", mime.lookup(req.originalUrl));
    fs.readFile(filename, 'utf8', function(err, contents) {
        if(err)
            res.send(`Error loading file '${filename}'.\nDid you check out the lovely images at 'dankmeme(1-10).png'?\nOr the text files 'TestFile(1-3).txt?'`);
        else
            fs.createReadStream("./" + filename).pipe(res);
    });
});

app.listen(app.get('port'), function() {
    console.log('Node app is running on port', app.get('port'));
});


