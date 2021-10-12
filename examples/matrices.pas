{
The program multiplies two random matrices.
}
Begin
Randomize;

const n = 5;
type Matrix = Array[1..n, 1..n] of Integer;

{Output matrix.}
procedure WriteMatrix(var X: Matrix);
begin
	var i, j: Integer;
	for i:=1 to n do
		begin
			for j:=1 to n do
				begin
					if j>1 then Write(', ');
					Write(X[i, j]);
				end;
			WriteLn;
		end;
end;

{Fill matrix with random values and then output it.}
procedure InitMatrix(var X: Matrix);
begin
	var i, j: Integer;
	for i:=1 to n do
		for j:=1 to n do
			X[i, j] := Random(5);
	WriteMatrix(X);
end;

{Initialize matrices.}
var X, Y, Z: Matrix;
WriteLn('X = ');
InitMatrix(X);
WriteLn('Y = ');
InitMatrix(Y);

{Multiply matrices}
var i, j, k: Integer;
for i:=1 to n do
	for j:=1 to n do
		begin
			var a: Integer = 0;
			for k:=1 to n do
				a := a + X[i, k]*Y[k, j];
			Z[i, j] := a;
		end;

{Output the result.}
WriteLn('X*Y = ');
WriteMatrix(Z);

End.
