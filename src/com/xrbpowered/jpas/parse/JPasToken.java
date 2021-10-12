package com.xrbpowered.jpas.parse;

import java.util.HashMap;

public class JPasToken {

	public static enum TokenType {
		symbol, number, string, identifier, keyword, operator, whitespace, comment;
		
		public JPasToken token;
		
		private TokenType() {
			this.token = new JPasToken(this);
		}
	};
	
	public TokenType type;
	public Object value;
	
	public JPasToken(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}

	public JPasToken(TokenType type) {
		this.type = type;
		value = null;
	}

	public JPasToken(char c) {
		type = TokenType.symbol;
		value = c;
	}
	
	@Override
	public boolean equals(Object obj) {
		return equals((JPasToken) obj);
	}
	
	public boolean equals(JPasToken t) {
		boolean res = t!=null && type==t.type &&
			(value==null || t.value==null || value.equals(t.value));
		return res;
	}
	
	@Override
	public String toString() {
		return ((value==null) ? type.toString() : value.toString());
	}
	
	public static boolean ofType(JPasToken t, TokenType type) {
		return t!=null && t.type==type;
	}
	
	public static HashMap<String, JPasToken> keywords = new HashMap<>();
	
	public static JPasToken keyword(String s) {
		JPasToken t = keywords.get(s);
		if(t==null) {
			t = new JPasToken(TokenType.keyword, s);
			keywords.put(s, t);
		}
		return t;
	}
}
