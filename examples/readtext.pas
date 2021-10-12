{
This program reads numbers from file into a dynamically created array,
then it finds minimum and maximum, and computes the sum.
}
Begin
ChDir('examples');

{Open a text file for reading.}
var Input: Text;
Assign(Input, 'input.txt');
Reset(Input);

{Read N.}
var N, i: Integer;
Read(Input, N);

{Dynamically create an array[0..(N-1)] of Integer.}
var PArr: ^array of Integer;
NewArray(PArr, N);

{Read N numbers from file to the array.}
for i:=1 to N do
	Read(Input, PArr^[i-1]);

{Close the file.}
Close(Input);

{Go though the array and find Min, Max, and Sum}
var Min, Max, Sum: Integer = 0;
for i:=1 to N do
	begin
		var X: Integer = PArr^[i-1];
		Sum := Sum + X;
		if i=1 then
			begin
				Min := X;
				Max := X;
			end
		else
			begin
				if X<Min then Min := X;
				if X>Max then Max := X;
			end;
	end;

{Output the result.}
WriteLn('Min = ', Min);
WriteLn('Max = ', Max);
WriteLn('Sum = ', Sum);
	
End.
