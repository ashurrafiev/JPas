{
This program demonstrates reading user input (mouse and keyboard)
from a Graph2D window.
}
Begin
uses Graph2D;

{Initialise}
InitWindow('Graph2D Input', 1280, 768, 1);
SetBackground($F0F0F0);
ClearCanvas;
HighQuality;
SetTextSize(20);

{Display welcome message}
const Hint = 'Draw with a mouse or type a key.';
SetPen($000000, 1);
DrawText(CanvasWidth div 2 - TextWidth(Hint) div 2, 50, Hint);

var PrevX, PrevY: Integer;
repeat
	{Start new frame, do not clear screen}

	{Drawing to current mouse position
	depending on the state of mouse buttons}
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
	{Save current mouse position for the next frame}
	PrevX := MouseX;
	PrevY := MouseY;

	{Process key events}
	while KeyPressed do
		begin
			var Symbol: Char = ReadKey; {Read input character}
			var Code: Integer = Ord(Symbol);
			if Code=0 then
				begin
					{Extended key, read again to get key code}
					Code := Ord(ReadKey);
					SetPaint($DDDDDD);
					FillRect(CanvasWidth div 2 - 70, 90-25, 40, 35);
				end
			else
				begin
					{Draw pressed character.}
					SetPaint($000000);
					FillRect(CanvasWidth div 2 - 70, 90-25, 40, 35);
					SetPen($FFFFFF, 1);
					DrawText(CanvasWidth div 2 -50 - TextWidth(Symbol) div 2, 90, Symbol);
				end;
			{Draw key code.}
			SetPaint($DDDDDD);
			FillRect(CanvasWidth div 2 - 20, 90-25, 80, 35);
			SetPen($777777, 1);
			DrawText(CanvasWidth div 2 - 10, 90, Str(Code));
		end;

	{End frame, refresh window}
	PresentWindow;
	Delay(5)
until False;

End.