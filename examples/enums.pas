Begin

type Direction = [North, East, South, West];
const DirNames: Array[Direction] of String = ['north', 'east', 'south', 'west'];

var Pass: Array[Direction] of Boolean = [True, False, False, True];

var d: Direction;
for d:=North to West do
	if Pass[d] then
		WriteLn('Can go ', DirNames[d], '.')
	else
		WriteLn('Cannot go ', DirNames[d], '.');
	
End.
