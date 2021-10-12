{
This program can be used to test scan codes for key events.
Note: it shows codes up to 255.
}
Begin
uses Graph2D;

{Initialise}
InitWindow('Graph2D Input Codes', 1280, 768, 1);
SetBackground($F0F0F0);
ClearCanvas;
HighQuality;

{Display welcome message}
const Hint = 'Press keys to see their codes highlight.';
SetPen($000000, 1);
DrawText(CanvasWidth div 2 - TextWidth(Hint) div 2, 50, Hint);

repeat
	{Start new frame}

	{Draw a 16x16 grid of numbers}
	var N: Integer;
	for N:=0 to 255 do
		begin
			var X: Integer = N div 16 * 60 + (CanvasWidth div 2 - 16*60 div 2);
			var Y: Integer = N mod 16 * 35 + 90;
			
			{Set colors depending on the state of respective key}
			if KeyDown(N) then
				begin SetPaint($000000); SetPen($FFFFFF, 1) end
			else
				begin SetPaint($DDDDDD); SetPen($777777, 1) end;

			FillRect(X, Y-20, 55, 30);
			DrawText(X+10, Y, Str(N));
		end;

	{End frame, refresh window}
	PresentWindow;
	Delay(10)
until False;

End.