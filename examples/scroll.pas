{
The program scrolls text in an infinite loop.
}
Begin

const s: String = 'Hello world! ';
var i, j: Integer;

i := 0;
repeat
	for j:=0 to Length(s)-1 do
		begin
			var x: Integer = (i+j) mod Length(s);
			Write(s[x+1]);
		end;
	WriteLn();
	
	i := i+1;
	Delay(100);
	
until False;

End.
