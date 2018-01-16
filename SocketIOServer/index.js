const alphavantage = require('alphavantage')({key: 'LW906MWVENK2Z6XN'});
const PORT = process.env.PORT || 3000;
var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

server.listen(PORT, () => console.log(`listening in port ${PORT}`));

app.get('/', function (req, res) {
  res.status(403).send('Forbidden');
});

io.on('connect', function (socket) {
  let stock = null;
  socket.on('sendStockName', (data) => {
    stock = data;
    console.log(`New Stock : ${stock}`);
  });

  let postStockPrice = setInterval(() => {
    console.log(`Sending new stock price`);
      if(!socket){
        console.log(`Error: No socket`);
      return;
      }
      if(!stock){
        socket.emit('notFound');
        console.log(`Error: No stock name`);
        return;
      }
      alphavantage.data.intraday('FB').then((data) => {
      if(!data){
        socket.emit('notFound');
        console.log(`Error: Stock not found`);
        return;
      }
      let mostRecentStock = Object.keys(data['Time Series (1min)'])[0];
      const stockPrice = data['Time Series (1min)'][mostRecentStock]["1. open"];
      const stockData = {
        lastUpdated : mostRecentStock,
        price: stockPrice
      };
      socket.emit('postStockPrice', stockData);
  });
}, 15000);

  socket.on('disconnect',() => {
    clearInterval(postStockPrice);
  });
});
