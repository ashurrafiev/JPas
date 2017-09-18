{
Read user input from console.
}
Begin

var Name: String;
var x, y: Integer;

WriteLn('What is your name?');
ReadLn(Name);
WriteLn('Hello ', Name, '!');
Write('Enter integer X and Y:');
ReadLn(x, y);
WriteLn('X+Y=', x+y);

End.