{
This program implements comprehensive text input
in Graph2D using ReadKey events.
}
Begin
uses Graph2D;

{Initialise}
InitWindow('Graph2D Typing', 1280, 768, 1);
SetBackground($F0F0F0);
HighQuality;
SetTextSize(25);

{Procedure to input text. Paramters:
	Str - a vartiable to receive the input
	Max - max allowed string length
	X, Y, Width, Height - coordinates of the input box
	Baseline - text position in relation to the input box
	BgColor - input box color
	FgColor - text color
}
procedure InputText(var Str: String; Max, X, Y, Width, Height,
		Baseline, BgColor, FgColor: Integer);

begin
	repeat
		{Draw input box}
		SetPaint(BgColor);
		FillRect(X, Y, Width, Height);
		{Draw entered text}
		SetPen(FgColor, 2);
		DrawText(X, Y+Baseline, Str);
		{Draw cursor}
		var CurX: Integer = X+TextWidth(Str)+2;
		DrawLine(CurX, Y, CurX, Y+Height);
		
		{Display and wait for key events}
		PresentWindow;
		repeat Delay(10) until KeyPressed;
		
		var Len: Integer = Length(Str);
		{Read key event from the buffer}
		var Key: Char = ReadKey;
		case Key of
			{If control key, skip the scan code.}
			#0: ReadKey;
			
			{If backspace and Str is not empty, delete the last character}
			#8: if Len>0 then Delete(Str, Len, Max+1);
			
			{Otherwise, if entered character is displayable (Space or above)
			and Str does not exceed Max symbols, add the character to
			the end of the string}
			else if (Key>=' ') and (Len<Max) then
				Insert(Key, Str, Len+1);
		end;
	{If hit Enter, exit the procedure}
	until Key=#10;
end;

{Main program cycle}
var OldStr:String = '';
repeat
	ClearCanvas;

	{Display previously entered text}
	if OldStr<>'' then
		begin
			SetPen($999999, 1);
			DrawText(200, 400, 'You entered:');
			SetPen($990000, 1);
			DrawText(200, 430, OldStr);
		end;

	{Request new input}
	SetPen($999999, 1);
	DrawText(200, 580, 'Enter some text:');
	
	var NewStr: String = '';
	InputText(NewStr, 32, 200, 600, 800, 40, 30, $000000, $FFFFFF);
	OldStr := NewStr;

until False;

End.