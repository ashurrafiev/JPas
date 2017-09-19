package com.xrbpowered.jpas.parse;

import java.util.regex.Pattern;

import com.xrbpowered.jpas.parse.JPasToken.TokenType;
import com.xrbpowered.utils.parser.Tokeniser;

public class JPasTokeniser extends Tokeniser<JPasToken> {

	public static final String[] KEYWORDS = {
			"begin", "end", "interface", "implementation", "label", "exit",
			"var", "const", "type", "function", "procedure", "forward", "uses",
			"if", "then", "else", "while", "do", "repeat", "until", "for", "to", "downto", "case", "with",
			"integer", "real", "boolean", "char", "string", "array", "of", "record",
			"not", "div", "mod", "and", "shl", "shr", "add", "sub", "or", "xor",
			"true", "false", "nil",
		};
	
	public JPasTokeniser() {
		super(new Pattern[] {
				Pattern.compile("\\s+", Pattern.MULTILINE+Pattern.DOTALL), // 0: whitespace
				Pattern.compile("\\{.*?\\}", Pattern.MULTILINE+Pattern.DOTALL), // 1: comment
				Pattern.compile("\\(\\*.*?\\*\\)", Pattern.MULTILINE+Pattern.DOTALL), // 2: comment
				Pattern.compile("\\#?\\d+(\\.\\d+)?"), // 3: number
				Pattern.compile("\\#?\\$[0-9A-Fa-f]+"), // 4: hex number
				Pattern.compile("\\\'.*?(\\\'\\\'.*?)*\\\'"), // 5: string
				Pattern.compile("_*[A-Za-z][A-Za-z0-9_]*"), // 6: identifier
				Pattern.compile("\\:\\="), // 7: operator
				Pattern.compile("\\.\\."), // 8: operator
				Pattern.compile("[\\<\\>\\=]+"), // 9: operator
				Pattern.compile(".") // symbol
		});
	}

	@Override
	protected JPasToken evaluateToken(int match, String raw) {
		switch(match) {
			case 0:
			case 1:
			case 2:
				return null;
			case 3:
			case 4:
				return new JPasToken(TokenType.number, raw);
			case 5:
				return new JPasToken(TokenType.string, raw.substring(1, raw.length()-1).replaceAll("\\\'\\\'", "\'"));
			case 6:
				for(int i=0; i<KEYWORDS.length; i++)
					if(KEYWORDS[i].equalsIgnoreCase(raw))
						return JPasToken.keyword(KEYWORDS[i]);
				return new JPasToken(TokenType.identifier, raw);
			case 7:
			case 8:
			case 9:
				return new JPasToken(TokenType.operator, raw);
			default:
				return new JPasToken(raw.charAt(0));
		}
	}

}
