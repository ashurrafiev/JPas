package com.xrbpowered.jpas.parse;

import java.util.ArrayList;
import java.util.List;

import com.xrbpowered.jpas.JPasError;
import com.xrbpowered.jpas.ast.Assignment;
import com.xrbpowered.jpas.ast.BlockStatement;
import com.xrbpowered.jpas.ast.ForLoop;
import com.xrbpowered.jpas.ast.IfStatement;
import com.xrbpowered.jpas.ast.Range;
import com.xrbpowered.jpas.ast.RepeatUntil;
import com.xrbpowered.jpas.ast.Scope;
import com.xrbpowered.jpas.ast.Scope.EntryType;
import com.xrbpowered.jpas.ast.Scope.ScopeEntry;
import com.xrbpowered.jpas.ast.Statement;
import com.xrbpowered.jpas.ast.WhileLoop;
import com.xrbpowered.jpas.ast.data.ArrayType;
import com.xrbpowered.jpas.ast.data.IndexableType;
import com.xrbpowered.jpas.ast.data.Type;
import com.xrbpowered.jpas.ast.exp.ArrayItem;
import com.xrbpowered.jpas.ast.exp.BinaryOp;
import com.xrbpowered.jpas.ast.exp.CharOfString;
import com.xrbpowered.jpas.ast.exp.Constant;
import com.xrbpowered.jpas.ast.exp.CustomFunction;
import com.xrbpowered.jpas.ast.exp.Expression;
import com.xrbpowered.jpas.ast.exp.Function;
import com.xrbpowered.jpas.ast.exp.LValue;
import com.xrbpowered.jpas.ast.exp.LValueArrayItem;
import com.xrbpowered.jpas.ast.exp.UnaryOp;
import com.xrbpowered.jpas.ast.exp.Variable;
import com.xrbpowered.jpas.parse.JPasToken.TokenType;
import com.xrbpowered.utils.parser.RecursiveDescentParser;

public class JPasParser extends RecursiveDescentParser<JPasToken, Statement> {

	public JPasParser() {
		super(new JPasTokeniser());
	}
	
	private boolean hasErrors = false;


	private Expression expressionLit(Scope scope) {
		if(TokenType.number.token.equals(token)) {
			Expression x = Constant.parseNumber((String) token.value);
			next();
			return x;
		}
		else if(TokenType.string.token.equals(token)) {
			String s = (String) token.value;
			Expression x = s.length()==1 ? new Constant(Type.character, new Character(s.charAt(0))) : new Constant(Type.string, s);
			next();
			return x;
		}
		else if(JPasToken.keyword("true").equals(token)) {
			Expression x = new Constant(Type.bool, true);
			next();
			return x;
		}
		else if(JPasToken.keyword("false").equals(token)) {
			Expression x = new Constant(Type.bool, false);
			next();
			return x;
		}
		else if(TokenType.identifier.token.equals(token)) {
			String name = (String) token.value;
			ScopeEntry e = scope.find(name);
			if(e==null)
				throw new JPasError("Unknown identifier: "+name);
			if(e.getScopeEntryType()==EntryType.function) {
				next();
				return function(scope, name, (Function) e);
			}
			else if(e.getScopeEntryType()==EntryType.variable) {
				next();
				return (Variable) e;
			}
			else
				return null;
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
				sep = true;
				next();
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
	
	private Expression indexAccess(Expression x, List<Expression> indices, int dim) {
		Type type = x.getType();
		if(!(type instanceof IndexableType))
			throw new JPasError("Indexing error.");
		Expression ex = Expression.implicitCast(((IndexableType) type).indexType(), indices.get(dim));
		if(ex==null)
			throw new JPasError("Index type mismatch");
		
		Expression res = null;
		if(type==Type.string)
			res = new CharOfString(x, ex);
		else if(type instanceof ArrayType) {
			// if(x instanceof LValue)
				res = new LValueArrayItem((LValue) x, ex); // TODO non-lvalue arrays
			// else
			//	res = new ArrayItem(x, ex);
		}
		
		if(res==null)
			return null;
		if(dim<indices.size()-1) {
			res = indexAccess(res, indices, dim+1);
		}
		return res;
	}
	
	private Expression expressionPost(Scope scope) {
		Expression x = expressionLit(scope);
		for(;;) {
			if(x==null)
				return null;
			if(new JPasToken('[').equals(token)) {
				next();
				List<Expression> indices = indexList(scope);
				if(indices==null || indices.isEmpty())
					return null;
				x = indexAccess(x, indices, 0);
			}
			else if(new JPasToken('.').equals(token))
				return null; // TODO recordMember(scope);
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
		Expression out = UnaryOp.make(op, x);
		if(out==null)
			throw new JPasError("\'"+t+"\' operand type mismatch.");
		return out;
	}
	
	private Expression expressionTerm(Scope scope) {
		if(new JPasToken('-').equals(token))
			return makeUnaryOp(scope, UnaryOp.Operation.neg);
		else if(new JPasToken('+').equals(token))
			return makeUnaryOp(scope, UnaryOp.Operation.pos);
		else if(JPasToken.keyword("not").equals(token))
			return makeUnaryOp(scope, UnaryOp.Operation.not);
		else
			return expressionPost(scope);
	}
	
	private Expression makeBinaryOpTerm(Scope scope, BinaryOp.Operation op, Expression x) {
		JPasToken t = token;
		next();
		Expression y = expressionTerm(scope);
		if(x==null || y==null)
			return null;
		x = Expression.precalc(x);
		y = Expression.precalc(y);
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
		x = Expression.precalc(x);
		y = Expression.precalc(y);
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
		x = Expression.precalc(x);
		y = Expression.precalc(y);
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
			else
				return x;
		}
	}

	private Expression expression(Scope scope) {
		return Expression.precalc(expressionRel(scope));
	}
	
	private List<Expression> callArguments(Scope scope, Function f) {
		List<Expression> args = new ArrayList<>();
		boolean sep = true;
		for(;;) {
			if(new JPasToken(')').equals(token)) {
				next();
				return args;
			}
			else if(new JPasToken(',').equals(token)) {
				sep = true;
				next();
			}
			else {
				if(!sep)
					throw new JPasError("Expected ,");
				Expression ex = expression(scope);
				if(ex!=null) {
					args.add(ex);
					sep = false;
				}
				else
					next();
			}
		}
	}
	
	private Expression function(Scope scope, String name, Function f) {
		Expression[] args = null;
		if(new JPasToken('(').equals(token)) {
			next();
			List<Expression> argList = callArguments(scope, f);
			if(argList==null)
				return null;
			else if(argList.size()>0) {
				args = new Expression[argList.size()];
				for(int i=0; i<args.length; i++)
					args[i] = argList.get(i);
			}
		}
		try {
			return f.makeCall(args);
		}
		catch(JPasError e) {
			throw new JPasError(e.getMessage() + " in "+name);
		}
	}
	
	private Statement block(Scope scope) {
		List<Statement> block = new ArrayList<>();
		boolean sep = true;
		Scope inner = new Scope(scope);
		for(;;) {
			if(JPasToken.keyword("end").equals(token)) {
				next();
				return new BlockStatement(block, inner.stackFrame);
			}
			else if(new JPasToken(';').equals(token)) {
				sep = true;
				next();
			}
			else {
				if(!sep)
					throw new JPasError("Expected ;");
				Statement s = checkedStatement(inner);
				if(s!=null) {
					if(s!=Statement.nop)
						block.add(s);
					sep = false;
				}
				else
					next();
			}
		}
	}

	private Statement repeatUntil(Scope scope) {
		List<Statement> block = new ArrayList<>();
		boolean sep = true;
		Scope inner = new Scope(scope);
		for(;;) {
			if(JPasToken.keyword("until").equals(token)) {
				next();
				Expression ex = expression(inner);
				if(ex==null)
					return null;
				ex = Expression.implicitCast(Type.bool, ex);
				if(ex==null)
					throw new JPasError("Condition must be boolean.");
				return new RepeatUntil(block, inner.stackFrame, ex);
			}
			else if(new JPasToken(';').equals(token)) {
				sep = true;
				next();
			}
			else {
				if(!sep)
					throw new JPasError("Expected ;");
				Statement s = checkedStatement(inner);
				if(s!=null) {
					if(s!=Statement.nop)
						block.add(s);
					sep = false;
				}
				else
					next();
			}
		}
	}
	
	private Range range(Scope scope) {
		Expression min = expression(scope);
		if(min==null)
			return null;
		if(!accept(new JPasToken(TokenType.operator, "..")))
			return null;
		Expression max = expression(scope);
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
				sep = true;
				next();
			}
			else {
				if(!sep)
					throw new JPasError("Expected ,");
				Range r = range(scope);
				if(r==null)
					return null;
				ranges.add(r);
				sep = false;
			}
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
		else if(TokenType.identifier.token.equals(token)) {
			String name = (String) token.value;
			next();
			ScopeEntry e = scope.find(name);
			if(e==null)
				throw new JPasError("Unknown identifier: "+name);
			if(e.getScopeEntryType()!=EntryType.type)
				throw new JPasError("Expected type.");
			return (Type) e;
		}
		else
			return null;
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
			def = Expression.implicitCast(type, def);
			if(def==null)
				throw new JPasError("Initialization type mismatch.");
		}
		Variable[] vars = new Variable[names.size()];
		for(int i=0; i<vars.length; i++)
			vars[i] = scope.addVariable(names.get(i), type);
		return new Variable.VarInit(vars, def);
	}
	
	private List<CustomFunction.ArgDef> argDefList(Scope scope) {
		List<CustomFunction.ArgDef> list = new ArrayList<>();
		boolean sep = true;
		for(;;) {
			if(new JPasToken(')').equals(token)) {
				next();
				return list;
			}
			else if(new JPasToken(';').equals(token)) {
				sep = true;
				next();
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
					list.add(new CustomFunction.ArgDef(name, type, lvalue));
				sep = false;
			}
		}
	}
	
	private Statement functionDecl(Scope scope, boolean proc) {
		JPasToken t = token;
		if(!accept(TokenType.identifier.token))
			return null;
		String name = (String) t.value;
		ScopeEntry e = scope.find(name);
		if(CustomFunction.isDuplicateId(e, proc ? EntryType.procedure : EntryType.function))
			throw new JPasError("Duplicate identifier");
		List<CustomFunction.ArgDef> args = null;
		if(new JPasToken('(').equals(token)) {
			next();
			args = argDefList(scope);
			if(args==null)
				return null;
		}
		Type type = null;
		if(!proc) {
			if(!accept(new JPasToken(':')))
				return null;
			type = type(scope);
			if(type==null)
				return null;
		}
		if(!accept(new JPasToken(';')))
			return null;
		
		CustomFunction f = new CustomFunction(args, type);
		scope.addFunction(name, f);
		
		Scope inner = f.createScope(scope);
		// TODO forward
		Statement st = checkedStatement(inner);
		if(st==null)
			return null;
		f.body = st;
		
		return Statement.nop;
	}
	
	private Statement typeDecl(Scope scope) {
		JPasToken t = token;
		if(!accept(TokenType.identifier.token))
			return null;
		String name = (String) t.value;
		if(!accept(new JPasToken(TokenType.operator, "=")))
			return null;
		Type type = type(scope); // TODO type fixing
		scope.addType(name, type);
		return Statement.nop;
	}
	
	private Statement statement(Scope scope) {
		
		if(JPasToken.keyword("begin").equals(token)) {
			next();
			return block(scope);
		}
		
		else if(JPasToken.keyword("repeat").equals(token)) {
			next();
			return repeatUntil(scope);
		}
		
		else if(JPasToken.keyword("var").equals(token)) {
			next();
			return varDecl(scope);
		}
		
		else if(JPasToken.keyword("type").equals(token)) {
			next();
			return typeDecl(scope);
		}
		
		else if(JPasToken.keyword("function").equals(token)) {
			next();
			return functionDecl(scope, false);
		}

		else if(JPasToken.keyword("procedure").equals(token)) {
			next();
			return functionDecl(scope, true);
		}
		
		else if(JPasToken.keyword("if").equals(token)) {
			next();
			Expression ex = expression(scope);
			if(ex==null)
				return null;
			ex = Expression.implicitCast(Type.bool, ex);
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
		
		else if(JPasToken.keyword("while").equals(token)) {
			next();
			Expression ex = expression(scope);
			if(ex==null)
				return null;
			ex = Expression.implicitCast(Type.bool, ex);
			if(ex==null)
				throw new JPasError("Condition must be boolean.");
			if(!accept(JPasToken.keyword("do")))
				return null;
			Statement s = checkedStatement(scope);
			if(s!=null) {
				return new WhileLoop(ex, s);
			}
			else
				return null;
		}
		
		else if(JPasToken.keyword("for").equals(token)) {
			next();
			if(!TokenType.identifier.token.equals(token)) // TODO lvalue for
				return null;
			String name = (String) token.value; // TODO lvalue expression?
			next();
			ScopeEntry e = scope.find(name);
			if(e==null)
				throw new JPasError("Unknown identifier: "+name);
			if(!(e instanceof LValue))
				throw new JPasError("Expected LValue.");
			LValue dst = (LValue) e;
			if(dst.getType().getOrdinator()==null)
				throw new JPasError("For loop requires ordinal type.");
			if(!accept(new JPasToken(TokenType.operator, ":=")))
				return null;
			
			Expression start = expression(scope);
			if(start==null)
				return null;
			start = Expression.implicitCast(dst.getType(), start);
			if(start==null)
				throw new JPasError("Type mismatch.");
			int dir = choice(JPasToken.keyword("to"), JPasToken.keyword("downto"));
			if(dir<0)
				return null;
			Expression end = expression(scope);
			if(end==null)
				return null;
			end = Expression.implicitCast(dst.getType(), end);
			if(end==null)
				throw new JPasError("Type mismatch.");
			
			if(!accept(JPasToken.keyword("do")))
				return null;
			Statement s = checkedStatement(scope);
			if(s!=null) {
				return new ForLoop(dir, (Variable) dst, start, end, s); // TODO lvalue instead of variable
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
					Expression f = function(scope, name, (Function) e);
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
				ex = Expression.implicitCast(dst.getType(), ex);
				if(ex==null)
					throw new JPasError("Assignment type mismatch.");
				return new Assignment(dst, ex);
			}
			else
				return Expression.call(e);
		}
	}
	
	private Statement checkedStatement(Scope scope) {
		try {
			Statement s = statement(scope);
			if(s==null) {
				hasErrors = true;
				if(token==null)
					error("Unexpected end of file.");
				else if(expectedToken==null)
					error("Syntax error: "+token);
				else
					error("Unexpected "+token+", missed "+expectedToken);
			}
			else
				return s;
		}
		catch(JPasError err) {
			hasErrors = true;
			error(err.getMessage());
		}
		while(!(token==null || new JPasToken(';').equals(token) || JPasToken.keyword("end").equals(token)))
			next();
		return null;
	}
	
	@Override
	protected Statement top() {
		hasErrors = false;
		Scope scope = Scope.global();
		Statement top = checkedStatement(scope);
		if(top==null || hasErrors)
			return null;
		if(!accept(new JPasToken('.')))
			error("Expected end of program.");
		return top;
	}

}
