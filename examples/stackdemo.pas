{
This program demonstrates dynamic memory stack implemented using pointers.
	The actual implementation is moved to 'stack.pas' module.
}
Begin
uses 'stack.pas';

WriteLn('Stack demo.');

repeat
	var Cmd: String;
	Write('pop, push or exit? ');
	ReadLn(Cmd);
	
	case Cmd of
		'pop': begin
				if CanPop then
					WriteLn('Popped ''', Pop, '''.')
				else
					WriteLn('Stack is empty.');
			end;
		'push': begin
				var Val: String;
				WriteLn('Enter new string value:');
				ReadLn(Val);
				Push(Val);
				WriteLn('Pushed ''', Val, '''.');
			end;
	end;

until Cmd='exit';

WriteLn('Done.');
	
End.
