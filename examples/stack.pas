{
This program implements dynamic memory stack using pointers.
}
Begin

type Item = record
		Value: String;
		Next: ^Item;
	end;

var Top: ^Item = nil;

procedure Push(Val: String);
begin
	var p: ^Item;
	New(p);
	p^.Value := Val;
	p^.Next := Top;
	Top := p;
end;

function Pop: String;
begin
	Result := Top^.Value;
	Top := Top^.Next;
	{No need for Dispose, GC will save us all.}
end;

WriteLn('Stack demo.');

repeat
	var Cmd: String;
	Write('pop, push or exit? ');
	ReadLn(Cmd);
	
	if Cmd='pop' then
		begin
			if Top=nil then
				WriteLn('Stack is empty.')
			else
				WriteLn('Popped ''', Pop, '''.');
		end
	else if Cmd='push' then
		begin
			var Val: String;
			WriteLn('Enter new string value:');
			ReadLn(Val);
			Push(Val);
			WriteLn('Pushed ''', Val, '''.');
		end;

until Cmd='exit';

WriteLn('Done.');
	
End.
