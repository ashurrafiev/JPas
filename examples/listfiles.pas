{
This program recursively lists files in the 'examples' directory
and its subdirectories.
}
Begin

{Set current directory to 'examples'.}
ChDir('examples');
WriteLn(GetDir);

{Recursive procedure for listring files and subdirectories.}
procedure ListDir(Name, Tabs: String);
begin
	{Go inside directory Name.}
	ChDir(Name);
	
	var P: ^array of String;
	var N, i: Integer;

	{Find all subdirectories.}
	N := FindDirs(P);
	for i:=0 to N-1 do
		begin
			{Print the name of a subdirectory.}
			WriteLn(Tabs, UpCase(P^[i]));
			{Recursively call ListDir for this subdirectory.
			Add few spaces to tabulation.}
			ListDir(P^[i], Tabs+'  ');
		end;

	{Find all files in the directory and print their names.}
	N := FindFiles(P);
	for i:=0 to N-1 do
		WriteLn(Tabs, '- ', P^[i]);
	WriteLn;
	
	{Go back to the parent directory.}
	ChDir('..');
end;

{Start ListDir recursion from the current directory.}
ListDir('.', '');

End.
