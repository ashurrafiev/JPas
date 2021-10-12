{
This programs renders texts in different fonts.
}
Begin
uses Graph2D;

{Initialise}
InitWindow('Graph2D Fonts', 1280, 768, 1);
SetBackground($F0F0F0);
ClearCanvas;
HighQuality;
SetTextSize(20);

{Draw welcome message}
const Hint = 'Press any key to exit.';
SetPen($000000, 1);
DrawText(CanvasWidth div 2 - TextWidth(Hint) div 2, CanvasHeight - 40, Hint);

{Draw page}
SetPaint($FFFFFF);
SetPen($999999, 1);
FillRect(200, 50, CanvasWidth-400, CanvasHeight-150);
DrawRect(200, 50, CanvasWidth-400, CanvasHeight-150);

{Draw text}
SetPen($000000, 1);

SetTextFont('Times New Roman', False, False);
SetTextSize(40);
DrawText(300, 150, 'Times New Roman, Normal.');

SetTextFont('Times New Roman', True, False);
SetTextSize(40);
DrawText(300, 200, 'Times New Roman, Bold.');

SetTextFont('Times New Roman', False, True);
SetTextSize(40);
DrawText(300, 250, 'Times New Roman, Italic.');

SetTextFont('Times New Roman', True, True);
SetTextSize(40);
DrawText(300, 300, 'Times New Roman, Bold Italic.');

SetTextFont('Arial', False, False);
SetTextSize(35);
DrawText(650, 420, 'Arial, Normal.');

SetTextFont('Arial', True, False);
SetTextSize(35);
DrawText(650, 470, 'Arial, Bold.');

SetTextFont('Arial', False, True);
SetTextSize(35);
DrawText(650, 520, 'Arial, Italic.');

SetTextFont('Arial', True, True);
SetTextSize(35);
DrawText(650, 570, 'Arial, Bold Italic.');

{End drawing, refresh screen}
PresentWindow;
{Wait for any key}
repeat Delay(10) until KeyPressed;

End.
