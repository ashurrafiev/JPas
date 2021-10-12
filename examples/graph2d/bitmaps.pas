{
This program demonstrates drawing inside a bitmap.
}
Begin
Randomize;
uses Graph2D;

{Initialise}
InitWindow('Bitmaps', 1280, 768, 1);
SetBackground($F0F0F0);
HighQuality;

{Procedure to draw a smile inside the given bitmap}
procedure DrawSmile(Bitmap: Integer);
begin
	{Use bitmap for drawing}
	UseBitmapCanvas(Bitmap);
	HighQuality;
	
	{Clear bitmap to transparent color}
	TransparencyOn;
	SetBackground(0);
	ClearCanvas;
	TransparencyOff;
	
	{Draw a smile}
	SetPaint($FFDD00);
	FillOval(5, 5, 90, 90);
	SetPen($000000, 3);
	DrawOval(5, 5, 90, 90);
	DrawArc(25, 25, 50, 50, 0, -180);
	SetPaint($000000);
	FillOval(35-3, 35-3, 7, 7);
	FillOval(65-3, 35-3, 7, 7);
	
	{Switch back to drawing on the screen}
	UseWindowCanvas;
end;

{Create a bitmap}
var Smile: Integer = CreateBitmap(100, 100, True);
DrawSmile(Smile);

repeat
ClearCanvas;

{Draw the bitmap on the screen 50 times 
in random locations and varying the size}
var i: Integer;
for i:=1 to 50 do
	begin
		var Size: Integer = Random(50)+50;
		StretchBitmap(
			Random(CanvasWidth+100)-100,
			Random(CanvasHeight+100)-100,
			Size, Size, Smile);
	end;

{End frame, refresh window}
PresentWindow;
{Wait for a key press}
repeat Delay(10) until KeyPressed;

until ReadKey=#27;

End.
