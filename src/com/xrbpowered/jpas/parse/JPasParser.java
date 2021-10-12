package com.xrbpowered.jpas.parse;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.xrbpowered.jpas.JPas;
import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Assignment;
import com.xrbpowered.jpas.ast.BlockStatement;
import com.xrbpowered.jpas.ast.CaseStatement;
import com.xrbpowered.jpas.ast.ExitStatement;
import com.xrbpowered.jpas.ast.ForLoop;
import com.xrbpowered.jpas.ast.IfStatement;
import com.xrbpowered.jpas.ast.LabelledStatement;
import com.xrbpowered.jpas.ast.RepeatUntil;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.Scope.ScopeEntry;
import com.xrbpowered.jpas.ast.Statement;
import com.xrbpowered.jpas.ast.UnitRef;
import com.xrbpowered.jpas.ast.WhileLoop;
import com.xrbpowered.jpas.ast.data.ArrayType;
import com.xrbpowered.jpas.ast.data.EnumType;
import com.xrbpowered.jpas.ast.data.FileType;
import com.xrbpowered.jpas.ast.data.ForwardPointerType;
import com.xrbpowered.jpas.ast.data.FunctionDeclaration;
import com.xrbpowered.jpas.ast.data.FunctionType;
import com.xrbpowered.jpas.ast.data.IndexableType;
import com.xrbpowered.jpas.ast.data.PointerType;
import com.xrbpowered.jpas.ast.data.Range;
import com.xrbpowered.jpas.ast.data.RangeType;
import com.xrbpowered.jpas.ast.data.RecordType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.ArrayItem;
import com.xrbpowered.jpas.ast.exp.ArrayLiteral;
import com.xrbpowered.jpas.ast.exp.BinaryOp;
import com.xrbpowered.jpas.ast.exp.CharOfString;
import com.xrbpowered.jpas.ast.exp.Constant;
import com.xrbpowered.jpas.ast.exp.CustomFunction;
import com.xrbpowered.jpas.ast.exp.DerefPointer;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.ast.exp.GetPointer;
import com.xrbpowered.jpas.ast.exp.InRangeOp;
import com.xrbpowered.jpas.ast.exp.LValue;
import com.xrbpowered.jpas.ast.exp.LValueArrayItem;
import com.xrbpowered.jpas.ast.exp.LValueRecordItem;
import com.xrbpowered.jpas.ast.exp.RecordItem;
import com.xrbpowered.jpas.ast.exp.RecordLiteral;
import com.xrbpowered.jpas.ast.exp.UnaryOp;
import com.xrbpowered.jpas.ast.exp.Variable;
import com.xrbpowered.jpas.parse.JPasToken.TokenType;
import com.xrbpowered.jpas.units.StandardUnit;
import com.xrbpowered.utils.parser.RecursiveDescentParser;

public class JPasParser extends RecursiveDescentParser<JPasToken, Statement> {

	public File workingDir = null;
	protected final Scope envScope;

	public JPasParser() {
		this(null);
	}

	protected JPasParser(Scope env) {
		super(new JPasTokeniser());
		this.envScope = env;
	}
	
	private boolean hasErrors = false;

	private Expression recordLiteral(Scope scope) {
		List<String> names = new ArrayList<>();
		List<Expression> list = new ArrayList<>();
		boolean sep = true;
		for(;;) {
			if(new JPasToken(']').equals(token)) {
				next();
				return RecordLiteral.make(names, list);
			}
			else if(new JPasToken(';').equals(token)) {
				if(sep)
					return null;
				next();
				sep = true;
			}
			else {
				if(!sep)
					throw new JPasError("Expected ;");
				
				JPasToken t = token;
				if(!accept(TokenType.identifier.token))
					return null;
				names.add((String) t.value);
				
				if(!accept(new JPasToken(':')))
					return null;
				
				Expression ex = expression(scope);
				if(ex==null)
					return null;
				list.add(ex);
				sep = false;
			}
		}
	}
	
	private Expression arrayLiteral(Scope scope) {
		List<Expression> list = new ArrayList<>();
		Type type = null;
		boolean sep = true;
		for(;;) {
			if(new JPasToken(']').equals(token)) {
				next();
				if(list.size()==0)
					throw new JPasError("Array literal is empty.");
				return ArrayLiteral.make(type, list);
			}
			else if(new JPasToken(',').equals(token)) {
				if(sep)
					return null;
				next();
				sep = true;
			}
			else {
				if(!sep)
					throw new JPasError("Expected ,");
				Expression ex = expression(scope);
				if(ex==null)
					return null;
				if(type!=null) {
					Expression ext = Expression.implicitCast(scope, type, ex);
					if(ext==null) {
						type = ex.getType();
						for(int i=0; i<list.size(); i++) {
							Expression prev = Expression.implicitCast(scope, type, list.get(i));
							if(prev==null)
								throw new JPasError("Element type mismatch.");
						}
					}
					else
						ex = ext;
				}
				type = ex.getType();
				list.add(ex);
				sep = false;
			}
		}
	}
	
	private Expression expressionLit(Scope scope) {
		if(TokenType.number.token.equals(token)) {
			String s = (String) token.value;
			next();
			return Constant.parseNumber(s);
		}
		else if(TokenType.string.token.equals(token)) {
			String s = (String) token.value;
			next();
			return s.length()==1 ? new Constant(Type.character, new Character(s.charAt(0))) : new Constant(Type.string, s);
		}
		else if(JPasToken.keyword("true").equals(token)) {
			next();
			return Constant.constTrue;
		}
		else if(JPasToken.keyword("false").equals(token)) {
			next();
			return Constant.constFalse;
		}
		else if(JPasToken.keyword("nil").equals(token)) {
			next();
			return Constant.constNil;
		}
		else if(JPasToken.keyword("result").equals(token)) {
			ScopeEntry e = scope.find("result");
			if(e==null || e.getScopeEntryType()!=EntryType.variable)
				throw new JPasError("Not in a function");
			next();
			return (Expression) e;
		}
		else if(TokenType.identifier.token.equals(token)) {
			String name = (String) token.value;
			ScopeEntry e = scope.find(name);
			if(e==null)
				throw new JPasError("Unknown identifier: "+name);
			if(e instanceof CustomFunction) {
				next();
				return ((CustomFunction) e).makeFuncRef();
			}
			if(e.getScopeEntryType()==EntryType.function) {
				next();
				return function(scope, scope.findTrueName(name), (Function) e);
			}
			else if(e.getScopeEntryType()==EntryType.variable) {
				next();
				return (Expression) e;
			}
			else if(e.getScopeEntryType()==EntryType.type && e instanceof EnumType) {
				next();
				return function(scope, scope.findTrueName(name), ((EnumType) e).converter);
			}
			else
				return null;
		}
		else if(new JPasToken('[').equals(token)) {
			int index = tokeniser.getIndex();
			next();
			boolean rec = accept(TokenType.identifier.token) && accept(new JPasToken(':'));
			tokeniser.rollBackTo(index);
			expectedToken = null;
			next();
			return rec ? recordLiteral(scope) : arrayLiteral(scope);
		}
		else if(new JPasToken('(').equals(token)) {
			next();
			Expression x = expression(scope);
			if(accept(new JPasToken(')')))
				return x;
			else
				return null;
		}
		else
			return null;
	}

	private List<Expression> indexList(Scope scope) {
		List<Expression> list = new ArrayList<>();
		boolean sep = true;
		for(;;) {
			if(new JPasToken(']').equals(token)) {
				next();
				return list;
			}
			else if(new JPasToken(',').equals(token)) {
				if(sep)
					return null;
				next();
				sep = true;
			}
			else {
				if(!sep)
					throw new JPasError("Expected ,");
				Expression ex = expression(scope);
				if(ex==null)
					return null;
				list.add(ex);
				sep = false;
			}
		}
	}
	
	private Expression indexAccess(Scope scope, Expression x, List<Expression> indices, int dim) {
		x = FunctionType.dereference(scope, x);
		Type type = x.getType();
		if(!(type instanceof IndexableType))
			throw new JPasError("Indexing error.");
		Type itype = ((IndexableType) type).indexType();
		if(itype==null) {
			throw new JPasError("Indexing error.");
		}
		Expression ex = Expression.implicitCast(scope, itype, indices.get(dim));
		if(ex==null)
			throw new JPasError("Index type mismatch");
		
		Expression res = null;
		if(type==Type.string)
			res = new CharOfString(x, ex);
		else if(type instanceof ArrayType) {
			if(x instanceof LValue)
				res = new LValueArrayItem((LValue) x, ex);
			else
				res = new ArrayItem(x, ex);
		}
		
		if(res==null)
			return null;
		if(dim<indices.size()-1) {
			res = indexAccess(scope, res, indices, dim+1);
		}
		return res;
	}
	
	private Expression expressionPost(Scope scope) {
		Expression x = expressionLit(scope);
		for(;;) {
			if(x==null)
				return null;
			if(new JPasToken('(').equals(token)) {
				Type type = x.getType();
				if(type instanceof FunctionType) {
					return function(scope, (FunctionType) type, x);
				}
				else
					return null;
			}
			else if(new JPasToken('[').equals(token)) {
				next();
				List<Expression> indices = indexList(scope);
				if(indices==null || indices.isEmpty())
					return null;
				x = indexAccess(scope, x, indices, 0);
			}
			else if(new JPasToken('.').equals(token)) {
				next();
				x = FunctionType.dereference(scope, x);
				if(!(x.getType() instanceof RecordType))
					throw new JPasError("Not a record.");
				RecordType rec = (RecordType) x.getType();
				JPasToken t = token;
				if(!accept(TokenType.identifier.token))
					return null;
				String name = (String) t.value;
				int index = rec.find(name);
				if(index<0)
					throw new JPasError("No such member.");
				if(x instanceof LValue)
					x = new LValueRecordItem((LValue) x, index);
				else
					x = new RecordItem(x, index);
			}
			else if(new JPasToken('^').equals(token)) {
				next();
				x = FunctionType.dereference(scope, x);
				if(!(x.getType() instanceof PointerType))
					throw new JPasError("Not a pointer.");
				x = new DerefPointer(((PointerType) x.getType()).getType(), x);
			}
			else
				return x;
		}
	}
	
	private Expression makeUnaryOp(Scope scope, UnaryOp.Operation op) {
		JPasToken t = token;
		next();
		Expression x = expressionTerm(scope);
		if(x==null)
			return null;
		x = Expression.precalc(FunctionType.dereference(scope, x));
		Expression out = UnaryOp.make(op, x);
		if(out==null)
			throw new JPasError(t+" operand type mismatch.");
		return out;
	}
	
	private Expression expressionTerm(Scope scope) {
		if(new JPasToken('-').equals(token))
			return makeUnaryOp(scope, UnaryOp.Operation.neg);
		else if(new JPasToken('+').equals(token))
			return makeUnaryOp(scope, UnaryOp.Operation.pos);
		else if(JPasToken.keyword("not").equals(token))
			return makeUnaryOp(scope, UnaryOp.Operation.not);
		else if(new JPasToken('@').equals(token)) {
			next();
			Expression x = expressionTerm(scope);
			if(x==null)
				return null;
			if(x instanceof LValue)
				return new GetPointer((LValue) x);
			else
				throw new JPasError("Expected LValue.");
		}
		else
			return expressionPost(scope);
	}
	
	private Expression makeBinaryOpTerm(Scope scope, BinaryOp.Operation op, Expression x) {
		JPasToken t = token;
		next();
		Expression y = expressionTerm(scope);
		if(x==null || y==null)
			return null;
		x = Expression.precalc(FunctionType.dereference(scope, x));
		y = Expression.precalc(FunctionType.dereference(scope, y));
		Expression out = BinaryOp.make(op, x, y);
		if(out==null)
			throw new JPasError("\'"+t+"\' operand type mismatch.");
		return out;
	}
	
	private Expression expressionProd(Scope scope) {
		Expression x = expressionTerm(scope);
		for(;;) {
			if(x==null)
				return null;
			if(token.equals(new JPasToken('*')))
				x = makeBinaryOpTerm(scope, BinaryOp.Operation.mul, x);
			else if(token.equals(new JPasToken('/')))
				x = makeBinaryOpTerm(scope, BinaryOp.Operation.div, x);
			else if(token.equals(JPasToken.keyword("div")))
				x = makeBinaryOpTerm(scope, BinaryOp.Operation.idiv, x);
			else if(token.equals(JPasToken.keyword("mod")))
				x = makeBinaryOpTerm(scope, BinaryOp.Operation.mod, x);
			else if(token.equals(JPasToken.keyword("and")))
				x = makeBinaryOpTerm(scope, BinaryOp.Operation.and, x);
			else if(token.equals(JPasToken.keyword("shl")))
				x = makeBinaryOpTerm(scope, BinaryOp.Operation.shl, x);
			else if(token.equals(JPasToken.keyword("shr")))
				x = makeBinaryOpTerm(scope, BinaryOp.Operation.shr, x);
			else
				return x;
		}
	}
	
	private Expression makeBinaryOpProd(Scope scope, BinaryOp.Operation op, Expression x) {
		JPasToken t = token;
		next();
		Expression y = expressionProd(scope);
		if(x==null || y==null)
			return null;
		x = Expression.precalc(FunctionType.dereference(scope, x));
		y = Expression.precalc(FunctionType.dereference(scope, y));
		Expression out = BinaryOp.make(op, x, y);
		if(out==null)
			throw new JPasError("\'"+t+"\' operand type mismatch.");
		return out;
	}
	
	private Expression expressionSum(Scope scope) {
		Expression x = expressionProd(scope);
		for(;;) {
			if(x==null)
				return null;
			if(token.equals(new JPasToken('+')))
				x = makeBinaryOpProd(scope, BinaryOp.Operation.add, x);
			else if(token.equals(new JPasToken('-')))
				x = makeBinaryOpProd(scope, BinaryOp.Operation.sub, x);
			else if(token.equals(JPasToken.keyword("or")))
				x = makeBinaryOpProd(scope, BinaryOp.Operation.or, x);
			else if(token.equals(JPasToken.keyword("xor")))
				x = makeBinaryOpProd(scope, BinaryOp.Operation.xor, x);
			else
				return x;
		}
	}
	
	private Expression makeBinaryOpSum(Scope scope, BinaryOp.Operation op, Expression x) {
		JPasToken t = token;
		next();
		Expression y = expressionSum(scope);
		if(x==null || y==null)
			return null;
		x = Expression.precalc(FunctionType.dereference(scope, x));
		y = Expression.precalc(FunctionType.dereference(scope, y));
		Expression out = BinaryOp.make(op, x, y);
		if(out==null)
			throw new JPasError("\'"+t+"\' operand type mismatch.");
		return out;
	}
	
	private Expression expressionRel(Scope scope) {
		Expression x = expressionSum(scope);
		for(;;) {
			if(x==null)
				return null;
			if(new JPasToken(TokenType.operator, "=").equals(token))
				x = makeBinaryOpSum(scope, BinaryOp.Operation.eq, x);
			else if(new JPasToken(TokenType.operator, "<>").equals(token))
				x = makeBinaryOpSum(scope, BinaryOp.Operation.neq, x);
			else if(new JPasToken(TokenType.operator, ">").equals(token))
				x = makeBinaryOpSum(scope, BinaryOp.Operation.gt, x);
			else if(new JPasToken(TokenType.operator, "<").equals(token))
				x = makeBinaryOpSum(scope, BinaryOp.Operation.lt, x);
			else if(new JPasToken(TokenType.operator, ">=").equals(token))
				x = makeBinaryOpSum(scope, BinaryOp.Operation.ge, x);
			else if(new JPasToken(TokenType.operator, "<=").equals(token))
				x = makeBinaryOpSum(scope, BinaryOp.Operation.le, x);
			else if(JPasToken.keyword("in").equals(token)) {
				next();
				Type type = type(scope);
				if(type==null)
					return null;
				if(type instanceof RangeType) {
					RangeType rt = (RangeType) type;
					x = Expression.implicitCast(scope, rt.getBaseType(), x);
					if(x==null)
						throw new JPasError("Type mismatch.");
					x = new InRangeOp(rt.range, x);
				}
				else if(type instanceof EnumType) {
					EnumType et = (EnumType) type;
					x = Expression.implicitCast(scope, Type.integer, x);
					if(x==null)
						throw new JPasError("Type mismatch.");
					x = new InRangeOp(et.getRange(), x);
				}
				else
					throw new JPasError("Unsupported type in 'in' operator");
			}
			else
				return x;
		}
	}

	private Expression expression(Scope scope) {
		return Expression.precalc(expressionRel(scope));
	}
	
	private List<Expression> callArguments(Scope scope) {
		List<Expression> args = new ArrayList<>();
		boolean sep = true;
		for(;;) {
			if(new JPasToken(')').equals(token)) {
				next();
				return args;
			}
			else if(new JPasToken(',').equals(token)) {
				if(sep)
					return null;
				next();
				sep = true;
			}
			else {
				if(!sep)
					throw new JPasError("Expected ,");
				Expression ex = expression(scope);
				if(ex==null)
					return null;
				args.add(ex);
				sep = false;
			}
		}
	}
	
	private Expression function(Scope scope, String name, Function f) {
		Expression[] args = null;
		if(new JPasToken('(').equals(token)) {
			next();
			List<Expression> argList = callArguments(scope);
			if(argList==null)
				return null;
			else if(argList.size()>0) {
				args = new Expression[argList.size()];
				for(int i=0; i<args.length; i++)
					args[i] = argList.get(i);
			}
		}
		try {
			return f.makeCall(scope, args);
		}
		catch(JPasError e) {
			throw new JPasError(e.getMessage() + " in call to "+name);
		}
	}
	
	private Expression function(Scope scope, FunctionType ftype, Expression fex) {
		Expression[] args = null;
		if(new JPasToken('(').equals(token)) {
			next();
			List<Expression> argList = callArguments(scope);
			if(argList==null)
				return null;
			else if(argList.size()>0) {
				args = new Expression[argList.size()];
				for(int i=0; i<args.length; i++)
					args[i] = argList.get(i);
			}
		}
		return ftype.makeCall(scope, fex, args);
	}

	private List<Statement> blockStatements(Scope inner, JPasToken end) {
		List<Statement> block = new ArrayList<>();
		boolean sep = true;
		for(;;) {
			if(end.equals(token)) {
				next();
				return block;
			}
			else if(new JPasToken(';').equals(token)) {
				sep = true;
				next();
			}
			else {
				if(!sep)
					throw new JPasError("Expected ;");
				Statement s = checkedStatement(inner);
				if(token==null)
					return null;
				if(s!=null) {
					if(s!=Statement.nop)
						block.add(s);
					sep = false;
				}
			}
		}
	}

	private Statement block(String label, Scope scope) {
		Scope inner = new Scope(scope);
		List<Statement> block = blockStatements(inner, JPasToken.keyword("end"));
		if(block==null)
			return null;
		inner.checkDefs();
		
		return new BlockStatement(label, block, inner.stackFrame);
	}

	private Statement repeatUntil(String label, Scope scope) {
		Scope inner = new Scope(scope);
		List<Statement> block = blockStatements(inner, JPasToken.keyword("until"));
		if(block==null)
			return null;
		inner.checkDefs();
		
		Expression ex = expression(inner);
		if(ex==null)
			return null;
		ex = Expression.implicitCast(scope, Type.bool, ex);
		if(ex==null)
			throw new JPasError("Condition must be boolean.");
		return new RepeatUntil(label, block, inner.stackFrame, ex);
	}
	
	private Statement exit(Scope scope) {
		String label;
		if(new JPasToken(';').equals(token)) {
			label = "";
		}
		else {
			JPasToken t = token;
			int ch = choice(TokenType.number.token, TokenType.identifier.token);
			if(ch==0) {
				try {
					Integer.parseInt((String) t.value);
				}
				catch(NumberFormatException e) {
					ch = -1;
				}
			}
			if(ch<0)
				return null;
			label = (String) t.value;
		}
		if(label.isEmpty() || scope.labels.contains(label.toLowerCase()))
			return new ExitStatement(label);
		else
			throw new JPasError("Unknown label "+label);
	}
	
	private Range range(Scope scope, ScopeEntry emin) {
		Expression min;
		if(emin==null)
			min = Expression.precalc(expressionTerm(scope));
		else if(emin.getScopeEntryType()==EntryType.variable)
			min = (Expression) emin;
		else
			return null;
		if(min==null)
			return null;
		if(!accept(new JPasToken(TokenType.operator, "..")))
			return null;
		Expression max = Expression.precalc(expressionTerm(scope));
		if(max==null)
			return null;
		return Range.make(min, max);
	}
	
	private List<Range> ranges(Scope scope) {
		List<Range> ranges = new ArrayList<>();
		boolean sep = true;
		for(;;) {
			if(new JPasToken(']').equals(token)) {
				next();
				return ranges;
			}
			else if(new JPasToken(',').equals(token)) {
				if(sep)
					return null;
				next();
				sep = true;
			}
			else {
				if(!sep)
					throw new JPasError("Expected ,");
				
				Type rt = type(scope);
				if(rt==null)
					return null;
				if(rt instanceof RangeType)
					ranges.add(((RangeType) rt).range);
				else if(rt instanceof EnumType)
					ranges.add(((EnumType) rt).getRange());
				else
					throw new JPasError("Range type error.");
				sep = false;
			}
		}
	}
	
	private boolean recordMembers(Scope scope, RecordType rec) {
		boolean sep = true;
		for(;;) {
			if(JPasToken.keyword("end").equals(token)) {
				next();
				return true;
			}
			else if(new JPasToken(';').equals(token)) {
				if(sep)
					return false;
				next();
				sep = true;
			}
			else {
				if(!sep)
					throw new JPasError("Expected ;");
				List<String> names = varList();
				if(names==null)
					return false;
				Type type = type(scope);
				if(type==null)
					return false;
				for(String name : names)
					rec.add(name, type);
				sep = false;
			}
		}
	}
	
	private boolean enumItems(Scope scope, EnumType type) {
		for(;;) {
			JPasToken t = token;
			if(!accept(TokenType.identifier.token))
				return false;
			
			scope.add((String) t.value, new Constant(type, type.addValue()));
			
			if(new JPasToken(']').equals(token)) {
				next();
				return true;
			}
			else if(new JPasToken(',').equals(token)) {
				next();
			}
			else
				return false;
		}
	}
	
	private Type type(Scope scope) {
		if(JPasToken.keyword("integer").equals(token)) {
			next();
			return Type.integer;
		}
		else if(JPasToken.keyword("real").equals(token)) {
			next();
			return Type.real;
		}
		else if(JPasToken.keyword("boolean").equals(token)) {
			next();
			return Type.bool;
		}
		else if(JPasToken.keyword("char").equals(token)) {
			next();
			return Type.character;
		}
		else if(JPasToken.keyword("string").equals(token)) {
			next();
			return Type.string;
		}
		else if(JPasToken.keyword("text").equals(token)) {
			next();
			return FileType.text;
		}
		else if(JPasToken.keyword("function").equals(token)) {
			next();
			FunctionDeclaration decl = functionDeclSpec(scope, false, true);
			if(decl==null)
				return null;
			return new FunctionType(decl);
		}
		else if(JPasToken.keyword("procedure").equals(token)) {
			next();
			FunctionDeclaration decl = functionDeclSpec(scope, true, true);
			if(decl==null)
				return null;
			return new FunctionType(decl);
		}
		else if(JPasToken.keyword("file").equals(token)) {
			next();
			Type type = null;
			if(JPasToken.keyword("of").equals(token)) {
				next();
				type = type(scope);
				if(type==null)
					return null;
				if(!type.isSerialisable())
					throw new JPasError("Type is not serializable");
			}
			return FileType.make(type);
		}
		else if(JPasToken.keyword("array").equals(token)) {
			next();
			List<Range> r = null;
			if(new JPasToken('[').equals(token)) {
				next();
				r = ranges(scope);
				if(r==null)
					return null;
				if(r.size()<1)
					r = null;
			}
			if(!accept(JPasToken.keyword("of")))
				return null;
			Type type = type(scope);
			if(type==null)
				return null;
			return ArrayType.make(r, type);
		}
		else if(JPasToken.keyword("record").equals(token)) {
			next();
			RecordType rec = new RecordType(false);
			if(!recordMembers(scope, rec))
				return null;
			return rec;
		}
		else if(TokenType.identifier.token.equals(token)) {
			String name = (String) token.value;
			next();
			ScopeEntry e = scope.find(name);
			if(e==null)
				throw new JPasError("Unknown identifier: "+name);
			if(e.getScopeEntryType()!=EntryType.type) {
				Range r = range(scope, e);
				if(r==null)
					throw new JPasError("Expected type.");
				else
					return new RangeType(r);
			}
			return (Type) e;
		}
		else if(new JPasToken('^').equals(token)) {
			next();
			if(TokenType.identifier.token.equals(token)) {
				String name = (String) token.value;
				ScopeEntry e = scope.find(name);
				if(e==null) {
					next();
					return new ForwardPointerType(scope, name);
				}
			}
			Type t = type(scope);
			if(t==null)
				return null;
			return new PointerType(t);
		}
		else if(new JPasToken('[').equals(token)) {
			next();
			EnumType type = new EnumType();
			if(enumItems(scope, type))
				return type;
			else
				return null;
		}
		else {
			Range r = range(scope, null);
			if(r==null)
				return null;
			return new RangeType(r);
		}
	}

	private List<String> varList() {
		ArrayList<String> list = new ArrayList<>();
		for(;;) {
			JPasToken t = token;
			if(!accept(TokenType.identifier.token))
				return null;
			list.add((String) t.value);
			if(new JPasToken(':').equals(token)) {
				next();
				return list;
			}
			else if(new JPasToken(',').equals(token)) {
				next();
			}
			else
				return null;
		}
	}
	
	private Statement varDecl(Scope scope) {
		List<String> names = varList();
		if(names==null)
			return null;
		Type type = type(scope);
		if(type==null)
			return null;
		Expression def = null;
		if(new JPasToken(TokenType.operator, "=").equals(token)) {
			next();
			def = expression(scope);
			if(def==null)
				return null;
			def = Expression.implicitCast(scope, type, def);
			if(def==null)
				throw new JPasError("Initialization type mismatch.");
		}
		Variable[] vars = new Variable[names.size()];
		for(int i=0; i<vars.length; i++)
			vars[i] = (Variable) scope.add(names.get(i), new Variable(type, scope.stackFrame));
		return new Variable.VarInit(vars, def);
	}

	private Statement constDecl(Scope scope) {
		JPasToken t = token;
		if(!accept(TokenType.identifier.token))
			return null;
		String name = (String) t.value;
		Type type = null;
		if(new JPasToken(':').equals(token)) {
			next();
			type = type(scope);
			if(type==null)
				return null;
		}
		if(!accept(new JPasToken(TokenType.operator, "=")))
			return null;
		Expression def = expression(scope);
		if(def==null)
			return null;
		if(type!=null) {
			def = Expression.implicitCast(scope, type, def);
			if(def==null)
				throw new JPasError("Initialization type mismatch.");
		}
		else
			type = def.getType();

		if(def.isConst()) {
			scope.add(name, new Constant(type, def.evaluate()));
			return Statement.nop;
		}
		else {
			throw new JPasError("Expression is not constant.");
		}
	}

	private List<FunctionDeclaration.ArgDef> argDefList(Scope scope) {
		List<FunctionDeclaration.ArgDef> list = new ArrayList<>();
		boolean sep = true;
		for(;;) {
			if(new JPasToken(')').equals(token)) {
				next();
				return list;
			}
			else if(new JPasToken(';').equals(token)) {
				if(sep)
					return null;
				next();
				sep = true;
			}
			else {
				if(!sep)
					throw new JPasError("Expected ;");
				
				boolean lvalue = false;
				if(JPasToken.keyword("var").equals(token)) {
					next();
					lvalue = true;
				}
				List<String> names = varList();
				if(names==null)
					return null;
				Type type = type(scope);
				if(type==null)
					return null;
				
				for(String name : names)
					list.add(new FunctionDeclaration.ArgDef(name, type, lvalue));
				sep = false;
			}
		}
	}
	
	private FunctionDeclaration functionDeclSpec(Scope scope, boolean proc, boolean strict) {
		List<FunctionDeclaration.ArgDef> args = null;
		if(new JPasToken('(').equals(token)) {
			next();
			args = argDefList(scope);
			if(args==null)
				return null;
		}
		Type type = null;
		if(!proc) {
			if(new JPasToken(':').equals(token)) {
				next();
				type = type(scope);
				if(type==null)
					return null;
			}
			else if(strict) {
				expectedToken = new JPasToken(':');
				return null;
			}
		}
		return new FunctionDeclaration(args, type);
	}
	
	private Statement functionDecl(Scope scope, boolean proc, boolean interf) {
		JPasToken t = token;
		if(!accept(TokenType.identifier.token))
			return null;
		String name = (String) t.value;
		ScopeEntry e = scope.find(name);
		if(CustomFunction.isDuplicateId(e, proc ? EntryType.procedure : EntryType.function))
			throw new JPasError("Duplicate identifier");

		FunctionDeclaration decl = functionDeclSpec(scope, proc, e==null);
		if(decl==null)
			return null;
		
		CustomFunction f;
		if(e==null) {
			f = new CustomFunction(decl);
			scope.add(name, f);
			f.forwardScope = scope;
		}
		else {
			f = ((CustomFunction) e).implement(decl);
			if(f==null)
				throw new JPasError("Declarations do not match.");
		}
		
		if(!interf) {
			if(!accept(new JPasToken(';')))
				return null;
			if(JPasToken.keyword("forward").equals(token)) {
				if(e!=null)
					throw new JPasError("Repeating forward declaration.");
				next();
			}
			else {
				if(f.forwardScope!=scope.forwardScope)
					throw new JPasError("Implementation must be in the same scope.");
				Scope inner = f.createScope(scope);
				inner.breakLabels();
				Statement st = checkedStatement(inner);
				if(st==null)
					return null;
				inner.checkDefs();
				f.body = st;
			}
		}
		return Statement.nop;
	}
	
	private Statement typeDecl(Scope scope) {
		JPasToken t = token;
		if(!accept(TokenType.identifier.token))
			return null;
		String name = (String) t.value;
		if(!accept(new JPasToken(TokenType.operator, "=")))
			return null;
		Type type = type(scope);
		if(type==null)
			return null;
		if(!type.isInitialisable())
			throw new JPasError("Type value cannot be initialized");
		scope.add(name, type);
		return Statement.nop;
	}
	
	private List<Object> switchList(Scope scope, Type type) {
		ArrayList<Object> list = new ArrayList<>();
		for(;;) {
			Expression x = expression(scope);
			if(x==null)
				return null;
			if(!x.isConst())
				throw new JPasError("Expression is not constant.");
			x = Expression.implicitCast(scope, type, x);
			if(x==null)
				throw new JPasError("Type mismatch");
			list.add(x.evaluate());
			if(new JPasToken(':').equals(token)) {
				next();
				return list;
			}
			else if(new JPasToken(',').equals(token)) {
				next();
			}
			else
				return null;
		}
	}
	
	private Statement caseList(Scope scope, Type type, CaseStatement cs) {
		for(;;) {
			if(JPasToken.keyword("else").equals(token)) {
				next();
				Statement sf = checkedStatement(scope);
				if(sf==null)
					return null;
				cs.addElse(sf);
				if(accept(new JPasToken(';')) && accept(JPasToken.keyword("end")))
					return cs;
				else
					return null;
			}
			else {
				List<Object> switches = switchList(scope, type);
				if(switches==null)
					return null;
				Statement s = checkedStatement(scope);
				if(s==null)
					return null;
				for(Object val : switches)
					cs.addSwitch(val, s);
			}
			
			if(!accept(new JPasToken(';')))
				return null;
			
			if(JPasToken.keyword("end").equals(token)) {
				next();
				return cs;
			}
		}
	}
	
	private Statement makeWith(Scope scope, List<String> names, int index, boolean lvalue) {
		if(index>=names.size()) {
			return checkedStatement(scope);
		}
		else {
			String name = names.get(index);
			ScopeEntry e = scope.find(name);
			if(e==null)
				throw new JPasError("Unknown identifier: "+name);
			if(e.getScopeEntryType()==EntryType.variable && (!lvalue || lvalue && e instanceof LValue)) {
				Type type = ((Expression) e).getType();
				if(type instanceof RecordType) {
					Scope inner = new Scope(scope, true);
					if(lvalue)
						((RecordType) type).addLValueTo((LValue) e, inner);
					else
						((RecordType) type).addTo((Expression) e, inner);
					Statement s = makeWith(inner, names, index+1, lvalue);
					if(s==null)
						return null;
					inner.checkDefs();
					return s;
				}
			}
			throw new JPasError("Not a record");
		}
	}

	private Statement uses(Scope scope) {
		if(TokenType.identifier.token.equals(token)) {
			String name = (String) token.value;
			next();
			if(scope.isUnitLoaded(name))
				return Statement.nop;
			scope.add(name, new UnitRef());
			StandardUnit unit = Scope.getStandardUnit(name.toLowerCase());
			if(unit!=null)
				return unit.use(scope);
			else
				return null;
		}
		else if(TokenType.string.token.equals(token)) {
			String name = (String) token.value;
			next();
			if(scope.isUnitLoaded(name))
				return Statement.nop;
			File file = new File(workingDir, name);
			scope.add(file.getAbsolutePath(), new UnitRef());
			LinkedList<String> labels = scope.labels;
			scope.breakLabels();
			Statement s = JPas.compile(new JPasParser(scope), file);
			scope.restoreLabels(labels);
			return s;
		}
		else {
			expectedToken = TokenType.identifier.token;
			return null;
		}
	}
	
	private Statement declaration(Scope scope) {
		if(new JPasToken(';').equals(token)) {
			return Statement.nop;
		}
		else if(JPasToken.keyword("uses").equals(token)) {
			next();
			return uses(scope);
		}
		else if(JPasToken.keyword("var").equals(token)) {
			next();
			return varDecl(scope);
		}
		else if(JPasToken.keyword("const").equals(token)) {
			next();
			return constDecl(scope);
		}
		else if(JPasToken.keyword("type").equals(token)) {
			next();
			return typeDecl(scope);
		}
		else if(JPasToken.keyword("function").equals(token)) {
			next();
			return functionDecl(scope, false, true);
		}
		else if(JPasToken.keyword("procedure").equals(token)) {
			next();
			return functionDecl(scope, true, true);
		}
		else
			return null;
	}

	private Statement interfaceDecl(Scope scope) {
		List<Statement> block = new ArrayList<>();
		boolean sep = true;
		for(;;) {
			if(JPasToken.keyword("implementation").equals(token)) {
				next();
				break;
			}
			else if(new JPasToken(';').equals(token)) {
				sep = true;
				next();
			}
			else {
				if(!sep)
					throw new JPasError("Expected ;");
				Statement s = checkedStatement(scope, true);
				if(token==null)
					return null;
				if(s!=null) {
					if(s!=Statement.nop)
						block.add(s);
					sep = false;
				}
				else
					next();
			}
		}
		sep = true;
		Scope impl = new Scope(scope, true);
		for(;;) {
			if(JPasToken.keyword("end").equals(token)) {
				next();
				break;
			}
			else if(new JPasToken(';').equals(token)) {
				sep = true;
				next();
			}
			else {
				if(!sep)
					throw new JPasError("Expected ;");
				Statement s = checkedStatement(impl, false);
				if(token==null)
					return null;
				if(s!=null) {
					if(s!=Statement.nop)
						block.add(s);
					sep = false;
				}
				else
					next();
			}
		}
		return new BlockStatement(null, block, null);
	}
	
	private Statement statement(String label, Scope scope) {
		
		if(new JPasToken(';').equals(token)) {
			return Statement.nop;
		}
		
		else if(JPasToken.keyword("begin").equals(token)) {
			next();
			return block(label, scope);
		}

		else if(JPasToken.keyword("repeat").equals(token)) {
			next();
			return repeatUntil(label, scope);
		}
		
		else if(JPasToken.keyword("exit").equals(token)) {
			next();
			return exit(scope);
		}
		
		else if(JPasToken.keyword("interface").equals(token)) {
			next();
			return interfaceDecl(scope);
		}
		
		else if(JPasToken.keyword("uses").equals(token)) {
			next();
			return uses(scope);
		}

		else if(JPasToken.keyword("var").equals(token)) {
			next();
			return varDecl(scope);
		}

		else if(JPasToken.keyword("const").equals(token)) {
			next();
			return constDecl(scope);
		}

		else if(JPasToken.keyword("type").equals(token)) {
			next();
			return typeDecl(scope);
		}
		
		else if(JPasToken.keyword("function").equals(token)) {
			next();
			return functionDecl(scope, false, false);
		}

		else if(JPasToken.keyword("procedure").equals(token)) {
			next();
			return functionDecl(scope, true, false);
		}
		
		
		else if(JPasToken.keyword("with").equals(token)) {
			next();
			Expression ex = expression(scope);
			if(ex==null)
				return null;
			Type type = ex.getType();
			if(!(type instanceof RecordType))
				throw new JPasError("Not a record");
			Scope inner = new Scope(scope, true);
			if(ex instanceof LValue)
				((RecordType) type).addLValueTo((LValue) ex, inner);
			else
				((RecordType) type).addTo(ex, inner);
			
			List<String> names = new ArrayList<>();
			for(;;) {
				if(JPasToken.keyword("do").equals(token)) {
					next();
					break;
				}
				else if(new JPasToken(',').equals(token)) {
					next();
				}
				else
					return null;
				
				JPasToken t = token;
				if(!accept(TokenType.identifier.token))
					return null;
				names.add((String) t.value);
			}
			
			Statement s = makeWith(inner, names, 0, ex instanceof LValue);
			if(s==null)
				return null;
			inner.checkDefs();
			return s;
		}
		
		else if(JPasToken.keyword("if").equals(token)) {
			next();
			Expression ex = expression(scope);
			if(ex==null)
				return null;
			ex = Expression.implicitCast(scope, Type.bool, ex);
			if(ex==null)
				throw new JPasError("Condition must be boolean.");
			if(!accept(JPasToken.keyword("then")))
				return null;
			Statement st = checkedStatement(scope);
			if(st!=null) {
				Statement sf = null;
				if(JPasToken.keyword("else").equals(token)) {
					next();
					sf = checkedStatement(scope);
					if(sf==null)
						return null;
				}
				return new IfStatement(ex, st, sf);
			}
			else
				return null;
		}
		
		else if(JPasToken.keyword("case").equals(token)) {
			next();
			Expression ex = expression(scope);
			if(ex==null)
				return null;
			Type type = ex.getType();
			if(type!=Type.string && type.getOrdinator()==null)
				throw new JPasError("Invalid expression type");
			if(!accept(JPasToken.keyword("of")))
				return null;
			CaseStatement cs = new CaseStatement(ex);
			return caseList(scope, type, cs);
		}
		
		else if(JPasToken.keyword("while").equals(token)) {
			next();
			Expression ex = expression(scope);
			if(ex==null)
				return null;
			ex = Expression.implicitCast(scope, Type.bool, ex);
			if(ex==null)
				throw new JPasError("Condition must be boolean.");
			if(!accept(JPasToken.keyword("do")))
				return null;
			Statement s = checkedStatement(scope);
			if(s!=null) {
				return new WhileLoop(label, ex, s);
			}
			else
				return null;
		}
		
		else if(JPasToken.keyword("for").equals(token)) {
			next();
			Expression ex = expression(scope);
			if(ex==null)
				return null;
			if(!(ex instanceof LValue))
				throw new JPasError("Expected LValue.");
			LValue dst = (LValue) ex;
			if(dst.getType().getOrdinator()==null)
				throw new JPasError("For loop requires ordinal type.");
			if(!accept(new JPasToken(TokenType.operator, ":=")))
				return null;
			
			Expression start = expression(scope);
			if(start==null)
				return null;
			start = Expression.implicitCast(scope, dst.getType(), start);
			if(start==null)
				throw new JPasError("Type mismatch.");
			int dir = choice(JPasToken.keyword("to"), JPasToken.keyword("downto"));
			if(dir<0)
				return null;
			Expression end = expression(scope);
			if(end==null)
				return null;
			end = Expression.implicitCast(scope, dst.getType(), end);
			if(end==null)
				throw new JPasError("Type mismatch.");
			
			if(!accept(JPasToken.keyword("do")))
				return null;
			Statement s = checkedStatement(scope);
			if(s!=null) {
				return ForLoop.make(label, dir, dst, start, end, s);
			}
			else
				return null;
		}
		
		else {
			if(TokenType.identifier.token.equals(token)) {
				String name = (String) token.value;
				ScopeEntry e = scope.find(name);
				if(e==null)
					throw new JPasError("Unknown identifier: "+name);
				if(e.getScopeEntryType()==EntryType.procedure) {
					next();
					Expression f = function(scope, scope.findTrueName(name), (Function) e);
					if(f!=null)
						return Expression.call(f);
					else
						return null;
				}
			}
			Expression e = expression(scope);
			if(e==null)
				return null;
			if(new JPasToken(TokenType.operator, ":=").equals(token)) {
				if(!(e instanceof LValue))
					throw new JPasError("Expected LValue.");
				LValue dst = (LValue) e;
				next();
				Expression ex = expression(scope);
				if(ex==null)
					return null;
				ex = Expression.implicitCast(scope, dst.getType(), ex);
				if(ex==null)
					throw new JPasError("Assignment type mismatch.");
				return new Assignment(dst, ex);
			}
			else {
				Type t = null;
				try {
					t = e.getType();
				}
				catch(JPasError err) {
				}
				if(t!=null && t instanceof FunctionType) {
					e = ((FunctionType) t).makeCall(scope, e, null);
				}
				return Expression.call(e);
			}
		}
	}

	private String label() {
		next();
		JPasToken t = token;
		int ch = choice(TokenType.number.token, TokenType.identifier.token);
		if(ch==0) {
			try {
				Integer.parseInt((String) t.value);
			}
			catch(NumberFormatException e) {
				ch = -1;
			}
		}
		if(ch<0)
			return null;
		String label = (String) t.value;
		if(accept(new JPasToken(':')))
			return label.toLowerCase();
		else
			return null;
	}
	
	private Statement checkedStatement(Scope scope) {
		return checkedStatement(scope, false);
	}
	
	private Statement checkedStatement(Scope scope, boolean decl) {
		try {
			String label = null;
			int labelLine = 0;
			if(JPasToken.keyword("label").equals(token)) {
				labelLine = tokeniser.getLineIndex();
				label = label();
				if(label==null) {
					showError();
					throw new JPasError(null);
				}
			}
			
			if(label!=null)
				scope.labels.add(label);
			Statement s = decl ? declaration(scope) : statement(label, scope);
			if(label!=null)
				scope.labels.removeLast();
			if(label!=null && (!(s instanceof LabelledStatement) || ((LabelledStatement) s).label!=label)) {
				System.err.println("Line "+labelLine+": Wrong label placement.");
				throw new JPasError(null);
			}
			if(s==null)
				showError();
			else
				return s;
		}
		catch(JPasError err) {
			hasErrors = true;
			String msg = err.getMessage();
			if(msg!=null)
				error(msg);
		}
		while(!(token==null || new JPasToken(';').equals(token) || JPasToken.keyword("end").equals(token)))
			next();
		return null;
	}
	
	private void showError() {
		hasErrors = true;
		if(token==null)
			error("Unexpected end of file.");
		else if(expectedToken==null)
			error("Syntax error: "+token);
		else
			error("Unexpected "+token+", missed "+expectedToken);
	}
	
	@Override
	protected Statement top() {
		hasErrors = false;
		Statement top;
		if(envScope==null) {
			Scope scope = Scope.global();
			top = checkedStatement(scope);
			scope.checkDefs();
			if(scope.stackFrame!=null && scope.stackFrame.size()>0)
				top = BlockStatement.wrap(top, scope.stackFrame);
		}
		else
			top = checkedStatement(envScope);
		if(top==null || hasErrors)
			return null;
		if(!accept(new JPasToken('.')))
			error("Expected end of program.");
		return top;
	}

}
