{
The program creates an array of random numbers then sorts it using bubble sort.
}
Begin
Randomize;

var n: Integer = 20;
var X: Array[0..n-1] of Integer;
var i: Integer;

{Fill array}
for i:=0 to n-1 do
	X[i] := Random(100);

{Bubble sort}
repeat
	var swapped: Boolean = False;
	for i:=1 to n-1 do
		if X[i-1]>X[i] then
			begin
				var y: Integer = X[i-1];
				X[i-1] := X[i];
				X[i] := y;
				swapped := True;
			end;
until not swapped;

{Output}
for i:=0 to n-1 do
	begin
		if i>0 then Write(', ');
		Write(X[i]);
	end;
WriteLn;

End.
