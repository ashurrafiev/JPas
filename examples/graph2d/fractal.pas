{
This program demonstrates using transform functions and
transform stack by drawing a fractal image.
}
Begin
uses Graph2D;

{Initialise}
InitWindow('Transform', 1280, 768, 1);
SetBackground($F0F0F0);
HighQuality;
ClearCanvas;

{Recursive function to draw a fractal shape.}
procedure Draw(D: Integer);
begin
	{Draw circle}
	DrawOval(-200, -200, 400, 400);
	
	{Recursively draw 3 smaller shapes}
	if D>0 then
		begin
			TFPush;
			TFRotate(0, 0, -20);
			TFTranslate(-180, 0);
			TFScale(0.6, 0.6);
			Draw(D-1);
			TFPop;
			
			TFPush;
			TFRotate(0, 0, -20);
			TFTranslate(90, 150);
			TFScale(0.6, 0.6);
			Draw(D-1);
			TFPop;
			
			TFPush;
			TFRotate(0, 0, -20);
			TFTranslate(90, -150);
			TFScale(0.6, 0.6);
			Draw(D-1);
			TFPop;
		end;
end;

{Start drawing in the middle of the screen}
TFTranslate(CanvasWidth div 2, CanvasHeight div 2);
SetPen($335577, 10);
Draw(7);

{End frame, refresh window}
PresentWindow;
{Wait for a key press}
repeat Delay(10) until KeyPressed;

End.
