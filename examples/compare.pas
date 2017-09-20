{
The program demonstrates the use of records and also array and record literals.
}
Begin
Randomize;

type Person = record
		Name: String;
		Age: Integer;
	end;
	
procedure Compare(var A, B: Person);
begin
	if A.Age=B.Age then
		WriteLn(A.Name, ' and ', B.Name, ' are of same age.')
	else if A.Age>B.Age then
		WriteLn(A.Name, ' is older than ', B.Name, '.')
	else
		WriteLn(A.Name, ' is younger than ', B.Name, '.');
end;

const MaxStaff = 5;
var NumStaff: Integer = 3;
var Staff: Array[1..MaxStaff] of Person = [
		[Name: 'Bob'; Age: 32],
		[Name: 'Alice'; Age: 21],
		[Name: 'Michael'; Age: 54]
		{The rest is initialized to default (empty/zero) values.}
	];
var P: ^Person;

repeat
P := @Staff[NumStaff+1];

WriteLn('Enter new person''s name (or blank to exit):');
ReadLn(P^.Name);

if P^.Name<>'' then
	begin
		WriteLn('What is ', P^.Name, '''s age:');
		ReadLn(P^.Age);
		
		var i: Integer;
		for i:=1 to NumStaff do
			Compare(P^, Staff[i]);
			
		Inc(NumStaff);
		if NumStaff=MaxStaff then
			WriteLn('Staff is full.');
	end;
	
until (P^.Name='') or (NumStaff=MaxStaff);

End.
