Begin
uses Graph2D;

InitWindow('Graph2D Animation', 1280, 768, 1);
SetBackground($F0F0F0);
HighQuality;

const Volume = 1.0E6;
var T: Real = 0;

repeat
ClearScreen;

procedure DrawSquare(X: Integer; Amp, TOffs: Real; Color: Integer);
begin
var YTop: Real = Amp*(Sin(T+TOffs));
var Height: Real = Amp/5*(Cos((T+TOffs)*2))+100;
var Width: Real = Sqrt(Volume/Height);
SetPaint(Color);
FillRect(Round(X-Width/2), Round(ScreenHeight/2-YTop), Round(Width), Round(Height));
end;

DrawSquare(440, 200, 0, $DD3300);
DrawSquare(640, 150, 5, $777777);
DrawSquare(840, 200, 10, $0099FF);

T := T + 0.05;
	
PresentScreen;
Delay(10)
until False;

End.