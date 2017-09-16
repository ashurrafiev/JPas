{
The program generates and compares pairs of random numbers
	until one of the numbers is 0.
}
Begin
Randomize;

repeat
	var x, y: Integer;
	x := Random(20);
	y := Random(20);

	if x=y then
		WriteLn(x, ' equals to ', y)
	else if x>y then
		WriteLn(x, ' is bigger than ', y)
	else
		WriteLn(x, ' is smaller than ', y);
		
until (x=0) or (y=0);

WriteLn('Done');
End.
