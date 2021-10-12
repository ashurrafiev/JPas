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
ClearCanvas;

{Type Tile enumerates all possible map tiles and images}
type Tile = [Void, Wall, Empty, Target, Box, BoxOnTarget,
		PlayerUp, PlayerDown, PlayerLeft, PlayerRight];
type MapTile = Void .. BoxOnTarget;

{Load sprite bitmaps}
const SpriteSize = 16;
type SpriteTile = Wall .. PlayerRight;
var Sprites: array[SpriteTile] of Integer;
LoadAtlas('gametiles.png', SpriteSize, SpriteSize, Sprites, Length(Sprites));

{Define game variables}
var Map:array[1..MaxCols, 1..MaxRows] of MapTile; {Game map}
var MapWidth, MapHeight: Integer;
var Player: record
		X, Y: Integer; {Player coordinates on the map}
		Image: SpriteTile; {Player image}
	end;

{
This procedure loads game map from file
and resets player's position
}
procedure LoadMap;
begin
	var F: Text;
	Assign(F, 'gamemap.txt');
	{Open map file}
	Reset(F);
	
	MapWidth := 0;
	MapHeight := 0;
	{Load map from file row by row}
	var Row: Integer = 1;
	while (Row<=MaxRows) and not Eof(F) do
		begin
			{Read row from file as a string of characters}
			var Line: String;
			ReadLn(F, Line);
			
			{Detect row width, limited to the maximum}
			var Col, N: Integer;
			N := Min(Length(Line), MaxCols);
			MapWidth := Max(MapWidth, N);
			
			{Decode each character in the row}
			for Col:=1 to N do
				case Line[Col] of
					'#': Map[Col, Row] := Wall;
					'.': Map[Col, Row] := Empty;
					'X': Map[Col, Row] := Target;
					'$': Map[Col, Row] := Box;
					'*': Map[Col, Row] := BoxOnTarget;
					'O':
						with Player do
							begin
								Map[Col, Row] := Empty;
								X := Col;
								Y := Row;
								Image := PlayerUp;
							end;
				end;

			{Go to the next row}
			Inc(Row);
		end;
	MapHeight := Row;

	{Close file}
	Close(F);
end;

{
This procedure draws the current game state on the screen
}
procedure DrawState;
begin
	ClearCanvas;
	
	{Calculate the top-left coordinate of the map
	depending on the map size}
	var Sx: Integer = CanvasWidth div 2 - MapWidth * SpriteSize div 2;
	var Sy: Integer = CanvasHeight div 2 - MapHeight * SpriteSize div 2;
	
	{Draw the map}
	var X, Y: Integer;
	for X:=1 to MapWidth do
		for Y:=1 to MapHeight do
			if Map[X, Y]<>Void then
				PutBitmap(
					Sx+(X-1)*SpriteSize,
					Sy+(Y-1)*SpriteSize,
					Sprites[Map[X, Y]]
				);

	{Draw the player}
	with Player do
		PutBitmap(
			Sx+(X-1)*SpriteSize,
			Sy+(Y-1)*SpriteSize,
			Sprites[Image]
		);
				
	{End drawing, refresh window}
	PresentWindow;
end;

{Move player by delta Dx, Dy}
procedure Move(Dx, Dy: Integer);
	with Player do
		begin
			X := X+Dx; Y:= Y+Dy;
		end;

{Put a box to the referenced map location}
procedure PutBox(T: ^MapTile);
begin
	if T^=Empty then
		T^ := Box
	else if T^=Target then
		T^ := BoxOnTarget;
end;

{Remove a box from the referenced map location}
procedure RemoveBox(T: ^MapTile);
begin
	if T^=Box then
		T^ := Empty
	else if T^=BoxOnTarget then
		T^ := Target;
end;

{If possible, move the player and the box (push the box)}
procedure TryMoveBox(Dx, Dy: Integer; Box: ^MapTile);
begin
	{Get the map location behind the box}
	var T: ^MapTile = @Map[Player.X+Dx*2, Player.Y+Dy*2];
	{If it is empty, do the movement}
	if (T^=Empty) or (T^=Target) then
		begin
			{Remove the box from the old location}
			RemoveBox(Box);
			{Put the box to the new location}
			PutBox(T);
			{Move the player}
			Move(Dx, Dy);
		end
end;

{If possible, move the player and update their image}
procedure TryMove(Dx, Dy: Integer; Image: SpriteTile);
begin
	{Change the player image}
	Player.Image := Image;
	{Get the target map location}
	var T: ^MapTile = @Map[Player.X+Dx, Player.Y+Dy];
	{If it is empty, move there}
	if (T^=Empty) or (T^=Target) then
		Move(Dx, Dy)
	{If there is a box, try to push it}
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
				#38: TryMove(0, -1, PlayerUp);
				#40: TryMove(0, 1, PlayerDown);
				#37: TryMove(-1, 0, PlayerLeft);
				#39: TryMove(1, 0, PlayerRight);
			end;
			
		{Backspace to reload the level}
		#8: LoadMap;
		
		{ESC to exit}
		#27: Halt;
	end;
until False;

End.
