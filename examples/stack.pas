{
This is a module implementing dynamic memory stack.
Running this file will not do anything, run 'stackdemo.pas' instead.
}
Interface

type Item = record
		Value: String;
		Next: ^Item;
	end;

procedure Push(Val: String);
function Pop: String;
function CanPop: Boolean;

implementation

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

function CanPop: Boolean;
begin
	Result := Top<>nil;
end;

End.
