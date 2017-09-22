Begin
uses Graph2D;

InitWindow('Graph2D Input', 1280, 768, 1);
SetBackground($F0F0F0);
ClearScreen;
HighQuality;
SetTextSize(19);

const Hint = 'Draw with a mouse or type a key.';
SetPen($000000, 1);
DrawText(ScreenWidth div 2 - TextWidth(Hint) div 2, 50, Hint);

var Continue: Boolean = False;
var PrevX, PrevY: Integer;
repeat

if LeftMouse then
	begin
		SetPen($DD3300, 3);
		DrawLine(PrevX, PrevY, MouseX, MouseY);
	end
else if RightMouse then
	begin
		SetPen($0099FF, 3);
		DrawLine(PrevX, PrevY, MouseX, MouseY);
	end;
PrevX := MouseX;
PrevY := MouseY;

while KeyPressed do
	begin
		var Symbol: Char = ReadKey;
		var Code: Integer = Ord(Symbol);
		if Code=0 then
			begin
				Code := Ord(ReadKey);
				SetPaint($DDDDDD);
				FillRect(ScreenWidth div 2 - 70, 90-25, 40, 35);
			end
		else
			begin
				SetPaint($000000);
				FillRect(ScreenWidth div 2 - 70, 90-25, 40, 35);
				SetPen($FFFFFF, 1);
				DrawText(ScreenWidth div 2 -50 - TextWidth(Symbol) div 2, 90, Symbol);
			end;
		SetPaint($DDDDDD);
		FillRect(ScreenWidth div 2 - 20, 90-25, 80, 35);
		SetPen($777777, 1);
		DrawText(ScreenWidth div 2 - 10, 90, Str(Code));
	end;

PresentScreen;
Delay(10)
until False;

End.