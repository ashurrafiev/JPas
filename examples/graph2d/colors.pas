{
This program displays a grid of colors using color interpolation function
and gradient paints.
}
Begin
Randomize;
uses Graph2D;

{Initialise}
InitWindow('Graph2D Colors', 1280, 768, 1);
SetBackground($F0F0F0);

repeat
	ClearCanvas;

	{Generate random colors}
	var ColorA, ColorB, ColorC, ColorD: Integer;
	ColorA := MakeRGB(Random, Random, Random, 1);
	ColorB := MakeRGB(Random, Random, Random, 1);
	ColorC := MakeRGB(Random, Random, Random, 1);
	ColorD := MakeRGB(Random, Random, Random, 1);
	
	{Calculate grid coordinates}
	var XLeft, XRight, YTop, YBottom: Integer;
	XLeft := CanvasWidth div 2 - 235;
	XRight := XLeft + 470;
	YTop := CanvasHeight div 2 - 235;
	YBottom := YTop + 470;

	{Paint corners}
	SetPaint(ColorA);
	FillRect(XLeft-40, YTop-40, 20, 20);
	SetPaint(ColorB);
	FillRect(XLeft-40, YBottom+20, 20, 20);
	SetPaint(ColorC);
	FillRect(XRight+20, YTop-40, 20, 20);
	SetPaint(ColorD);
	FillRect(XRight+20, YBottom+20, 20, 20);
	
	{Paint side gradients}
	GradientPaint(0, YTop, 0, YBottom, ColorA, ColorB);
	FillRect(XLeft-40, YTop, 20, YBottom-YTop);
	GradientPaint(0, YTop, 0, YBottom, ColorC, ColorD);
	FillRect(XRight+20, YTop, 20, YBottom-YTop);
	GradientPaint(XLeft, 0, XRight, 0, ColorA, ColorC);
	FillRect(XLeft, YTop-40, XRight-XLeft, 20);
	GradientPaint(XLeft, 0, XRight, 0, ColorB, ColorD);
	FillRect(XLeft, YBottom+20, XRight-XLeft, 20);

	{Paint grid of swatches}
	const w = 7;
	var y:Integer;
	for y:=0 to w do
		begin
			var ColorL, ColorR: Integer;
			ColorL := BlendColors(y/w, ColorA, ColorB);
			ColorR := BlendColors(y/w, ColorC, ColorD);
			
			var x: Integer;
			for x := 0 to w do
				begin
					SetPaint(BlendColors(x/w, ColorL, ColorR));
					FillRect(XLeft + x*60, YTop + y*60, 50, 50);
				end;
		end;

	PresentWindow;
	repeat Delay(10) until KeyPressed;
	{Wait for a key then redraw}

until ReadKey=#27;

End.