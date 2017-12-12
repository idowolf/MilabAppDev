let CONST_STR = "Hello World my name is Ido Wolf";
for(let i = 0 ; i < CONST_STR.length ; i++) {
	console.log(CONST_STR);
	CONST_STR = CONST_STR.slice(-1) +
		CONST_STR.slice(0, CONST_STR.length - 1);
}
