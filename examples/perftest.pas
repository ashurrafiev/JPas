{
Performance test. Run with -time option.
}
Begin

const n = Int(1E9); { = 1000000000 (one billion)}
Write('Started counting to ', n, '... ');
var i: Integer;
for i:=0 to n do;
WriteLn('Done');

End.
