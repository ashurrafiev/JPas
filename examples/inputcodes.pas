Begin
uses Graph2D;

InitWindow('Graph2D Input', 1280, 768, 1);
SetBackground($F0F0F0);
ClearScreen;
HighQuality;

const Hint = 'Press keys to see their codes highlight.';
SetPen($000000, 1);
DrawText(ScreenWidth div 2 - TextWidth(Hint) div 2, 50, Hint);

var Continue: Boolean = False;
var PrevX, PrevY: Integer;
repeat

var N: Integer;
for N:=0 to 255 do
	begin
		var X: Integer = N div 16 * 60 + (ScreenWidth div 2 - 16*60 div 2);
		var Y: Integer = N mod 16 * 35 + 90;
		if KeyDown(N) then
			begin SetPaint($000000); SetPen($FFFFFF, 1) end
		else
			begin SetPaint($DDDDDD); SetPen($777777, 1) end;
		FillRect(X, Y-20, 55, 30);
		
		DrawText(X+10, Y, Str(N));
	end;

PresentScreen;
Delay(10)
until False;

End.