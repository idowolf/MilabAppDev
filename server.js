const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const MongoClient = require('mongodb').MongoClient;
const MONGO_URL = "mongodb://default:12345678@ds251747.mlab.com:51747/musicdb";
const ObjectId = require('mongodb').ObjectId;
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
  extended: true
}));
let myDb = "";
app.set('port', (process.env.PORT || 5000));

function createEntry(song, album, artist, genre, res) {
  let songObj = {name: song, album: album, artist: artist, genre: genre};
  myDb.collection('songs').insert([songObj], function (err, result) {
        if (err) {
            res.send("Error adding new song")
        } else {
            res.send("Added a new song" + result["ops"][0]["_id"]);
        }
    });
}

function deleteEntry(id, res) {
  myDb.collection('songs').deleteOne({ _id: ObjectId(id) }, function(err, result) {
      res.send("Deleted song " + id);
  });
}

function updateEntry(id, song, album, artist, genre, res) {
  let songObj = {
    name: song,
    album: album,
    artist: artist,
    genre: genre,
  };
  myDb.collection('songs').updateOne({_id: ObjectId(id)}, {$set: songObj}, function (err, numUpdated) {
    if (err) {
        res.send("Error updating " + id);
    } else if (numUpdated) {
        res.send("Updated successfully");
    } else {
        res.send("No matching song found");
    }
});
}

function printSong(songObj, res) {
  res.write(`ID: ${songObj._id}\n`);
  res.write(`Name: ${songObj.name}\n`);
  res.write(`Album: ${songObj.album}\n`);
  res.write(`Artist: ${songObj.artist}\n`);
  res.write(`Genre: ${songObj.genre}\n`);
  res.write(`\n`);
}

function readSong(song, res) {
  myDb.collection('songs').findOne({name: song}, function(err, document) {
    if(err || !document) {
      res.send("Cannot find a song named " + song);
    } else {
      printSong(document, res);
      res.end();
    }
  });
}

function readAlbum(album, res) {
 myDb.collection('songs').find({album: album}).toArray((err, results) => {
 if(err || !results[0]) {
   res.send("Error finding songs");
 } else {
   res.write(`Songs found:\n`);
   results.forEach(doc => printSong(doc, res));
   res.end();
 }
});
}

function readArtist(artist, res) {
 myDb.collection('songs').find({artist: artist}).toArray((err, results) => {
 if(err || !results[0]) {
   res.send("Error finding songs");
 } else {
   res.write(`Songs found:\n`);
   results.forEach(doc => printSong(doc, res));
   res.end();
 }
});
}

// Delete operation (REST DELETE)
app.delete('/:id', function(req, res) {
    let entryId = req.params.id;
    deleteEntry(entryId, res);
});

// Create operation (REST POST)
app.post('/', function(req, res) {
    if(!req.body.songname || !req.body.albumname || !req.body.artistname || !req.body.genre) {
      res.status(400).send({message: "Missing parameters 1" + req.body + "IDO" + req.body[0] });
    } else {
      let status = createEntry(req.body.songname, req.body.albumname, req.body.artistname, req.body.genre, res);
    }
});

// Update operation (REST PUT)
app.put('/:id', function(req, res) {
    updateEntry(req.body.id, req.body.songname, req.body.albumname, req.body.artistname, req.body.genre, res);
});

// Various read operations (REST GET)
app.get('/', function(req, res) {
  if(req.query.songname) {
    readSong(req.query.songname, res);
  } else if(req.query.albumname) {
    readAlbum(req.query.albumname, res);
  } else if(req.query.artistname) {
    readArtist(req.query.artistname, res);
  } else {
    res.status(400).send({message: "Missing parameters 2"});
  }
});

MongoClient.connect(MONGO_URL, (err, db) => {
  if(err) {
    console.error(err);
    return;
  }
  console.log("Connected successfully to the database");
  myDb = db.db('musicdb');
  myDb.collection('songs').createIndex({ name: 1 });
  myDb.collection('songs').createIndex({ album: 1 });
  myDb.collection('songs').createIndex({ artist: 1 });
  app.listen(app.get('port'), function() {
      console.log('Node app is running on port', app.get('port'));
  });
});
