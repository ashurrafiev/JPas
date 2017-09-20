{
The program demonstrates the use of exit label syntax
	to emulate break and continue behaviours.
}
Begin

var x: Integer;

label Loop:
for x:=1 to 10 do
	label Body:
	begin
		WriteLn('x = ', x);
	
		if x=5 then
			begin
				WriteLn('Hit the brakes!');
				exit Loop; {Break.}
			end;
			
		if x<7 then
			exit Body; {Continue.}
			
		WriteLn('You''ll never see this...');
	end;

WriteLn('Done.');
	
End.
