let express = require('express');
let app = express();
let fs = require('fs');

function getFormattedTime() {
    return new Date().toISOString().
        replace(/T/, ' ').
        replace(/\..+/, '');
}

app.set('port', (process.env.PORT || 5000));

app.get('/', (req, res) => {
    res.send(getFormattedTime());
});

app.listen(app.get('port'), function() {
    console.log('Node app is running on port', app.get('port'));
});


