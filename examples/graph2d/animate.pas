{
This program shows animation in Graph2D.
}
Begin
uses Graph2D;

{Initialise}
InitWindow('Graph2D Animation', 1280, 768, 1);
SetBackground($F0F0F0);
HighQuality;
SetPen($000000, 1);

repeat
	{Start new frame}
	ClearCanvas;
	
	{Show FPS}
	DrawText(20, 30, Format('%.2f FPS', FPSCount));

	procedure DrawBox(X: Integer; Amp, T: Real; Color: Integer);
	begin
		{Calculate the position and dimensions of the box
		depending on timer T}
		const Volume = 1.0E6;
		var YTop: Real = Amp*Sin(T);
		var Height: Real = Amp/5*Cos(T*2)+100;
		var Width: Real = Sqrt(Volume/Height);
		
		{Draw the box}
		SetPaint(Color);
		FillRect(Round(X-Width/2), Round(CanvasHeight/2-YTop), Round(Width), Round(Height));
	end;

	DrawBox(440, 200, Elapsed*3, $DD3300);
	DrawBox(640, 150, Elapsed*3+0.5, $777777);
	DrawBox(840, 200, Elapsed*3+1, $0099FF);

	{End frame, refresh window}
	PresentWindow;
	Delay(5)
until False;

End.