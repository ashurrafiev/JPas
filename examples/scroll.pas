{
The program scrolls text in an infinite loop.
}
Begin

var s: String = 'Hello world! ';
var i, j: Integer;

repeat
	for j:=0 to Length(s)-1 do
		Write(s[(i+j) mod Length(s) + 1]);
	WriteLn();
	
	i := i+1;
	Delay(100);
	
until False;

End.
