Begin
Randomize;
uses Graph2D;
ChDir('examples/graph2d');

{Initialise}
const MaxRows = 12;
const MaxCols = 12;
InitWindow('Game', 320, 200, 4);
SetBackground($F0F0F0);
LowQuality;
ClearScreen;

type MapTile = [Void, Wall, Empty, Target, Box, BoxOnTarget, PlayerTile];

{Load tile bitmaps}
var Tiles: array[Wall..PlayerTile] of Integer;
LoadAtlas('gametiles.png', 16, 16, Tiles, 6);

var Map:array[1..MaxCols, 1..MaxRows] of MapTile;
var MapWidth, MapHeight: Integer;
var Player: record
		X, Y: Integer;
	end;

procedure LoadMap;
begin
	var F: Text;
	Assign(F, 'gamemap.txt');
	Reset(F);
	
	MapWidth := 0;
	MapHeight := 0;
	var Row: Integer = 1;
	label LoadRows:
	while (Row<=MaxRows) do
		begin
			var Line: String;
			ReadLn(F, Line);
			
			var Col, N: Integer;
			N := Min(Length(Line), MaxCols);
			if N=0 then exit LoadRows;
			MapWidth := Max(MapWidth, N);
			
			for Col:=1 to N do
				case Line[Col] of
					'#': Map[Col, Row] := Wall;
					'.': Map[Col, Row] := Empty;
					'X': Map[Col, Row] := Target;
					'$': Map[Col, Row] := Box;
					'*': Map[Col, Row] := BoxOnTarget;
					'O':
						begin
							Map[Col, Row] := Empty;
							Player.X := Col;
							Player.Y := Row;
						end;
				end;
				
			Inc(Row);
		end;
	MapHeight := Row;
	
	Close(F);
end;

procedure DrawState;
begin
	ClearScreen;
	
	var Sx: Integer = ScreenWidth div 2 - MapWidth * 8;
	var Sy: Integer = ScreenHeight div 2 - MapHeight * 8;
	
	var X, Y: Integer;
	for X:=1 to MapWidth do
		for Y:=1 to MapHeight do
			if Map[X, Y]<>Void then
				PutBitmap(Sx+(X-1)*16, Sy+(Y-1)*16, Tiles[Map[X, Y]]);

	PutBitmap(Sx+(Player.X-1)*16, Sy+(Player.Y-1)*16, Tiles[PlayerTile]);
				
	{End frame, refresh window}
	PresentScreen;
end;

procedure Move(Dx, Dy: Integer);
	with Player do
		begin
			X := X+Dx; Y:= Y+Dy;
		end;

procedure PutBox(T: ^MapTile);
begin
	if T^=Empty then
		T^ := Box
	else if T^=Target then
		T^ := BoxOnTarget;
end;

procedure RemoveBox(T: ^MapTile);
begin
	if T^=Box then
		T^ := Empty
	else if T^=BoxOnTarget then
		T^ := Target;
end;

procedure TryMoveBox(Dx, Dy: Integer; Box: ^MapTile);
begin
	var T: ^MapTile = @Map[Player.X+Dx*2, Player.Y+Dy*2];
	if (T^=Empty) or (T^=Target) then
		begin
			PutBox(T);
			RemoveBox(Box);
			Move(Dx, Dy);
		end
end;

procedure TryMove(Dx, Dy: Integer);
begin
	var T: ^MapTile = @Map[Player.X+Dx, Player.Y+Dy];
	if (T^=Empty) or (T^=Target) then
		Move(Dx, Dy)
	else if (T^=Box) or (T^=BoxOnTarget) then
		TryMoveBox(Dx, Dy, T);
end;

{Load level}
LoadMap;

repeat
	{Redraw game state}
	DrawState;
	{Wait for a key press}
	repeat Delay(10) until KeyPressed;

	case ReadKey of
		#0:
			case ReadKey of
				{Arrow keys to move}
				#38: TryMove(0, -1);
				#40: TryMove(0, 1);
				#37: TryMove(-1, 0);
				#39: TryMove(1, 0);
			end;
			
		{Backspace to reload the level}
		#8: LoadMap;
		
		{ESC to exit}
		#27: Halt;
	end;
until False;

End.
