const express = require('express');
const fs = require('fs');
const bodyParser = require('body-parser');
let app = express();
app.use(bodyParser.json());
app.set('port', (process.env.PORT || 8080));

function getFormattedTime(date) {
    return date.toISOString().
        replace(/T/, ' ').
        replace(/\..+/, '');
}

function calcTime(inputTzOffset){
    let now = new Date(); // get the current time

    let currentTzOffset = -now.getTimezoneOffset() / 60
    let deltaTzOffset = inputTzOffset - currentTzOffset + 2; // timezone diff

    let nowTimestamp = now.getTime(); // get the number of milliseconds since unix epoch 
    let deltaTzOffsetMilli = deltaTzOffset * 1000 * 60 * 60; // convert hours to milliseconds (tzOffsetMilli*1000*60*60)
    let outputDate = new Date(nowTimestamp + deltaTzOffsetMilli) // your new Date object with the timezone offset applied.

    return getFormattedTime(outputDate);
}

function getCurrentFormattedTime() {
    return new Date().toString();
}

app.post('/', (req, res) => {
    let timeZone = req.body.timeZone;
ObjectMapper mapper = new ObjectMapper();
let myTime = "";
if(timeZone == "" || timeZone == null) {
    myTime = getCurrentFormattedTime();
} else {
    myTime = calcTime(timezone);
}
  res.json({ 
    time: myTime
  });
});

app.listen(app.get('port'), function() {
    console.log('Node app is running on port', app.get('port'));
});


