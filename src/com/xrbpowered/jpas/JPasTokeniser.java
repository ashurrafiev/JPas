package com.xrbpowered.jpas;

import java.util.regex.Pattern;

import com.xrbpowered.jpas.JPasToken.TokenType;
import com.xrbpowered.utils.parser.Tokeniser;

public class JPasTokeniser extends Tokeniser<JPasToken> {

	public static final String[] KEYWORDS = {
			"begin", "end",
			"var",
			"if", "then", "else", "while", "do", "repeat", "until", "for", "to", "downto",
			"integer", "real", "boolean", "string", "array", "of",
			"not", "div", "mod", "and", "shl", "shr", "add", "sub", "or", "xor",
			"true", "false"
		};
	
	public JPasTokeniser() {
		super(new Pattern[] {
				Pattern.compile("\\s+", Pattern.MULTILINE+Pattern.DOTALL), // 0: whitespace
				Pattern.compile("\\{.*?\\}", Pattern.MULTILINE+Pattern.DOTALL), // 1: comment
				Pattern.compile("\\d+(\\.\\d+)?"), // 2: number
				Pattern.compile("\\\'.*?(\\\'\\\'.*?)*\\\'"), // 3: string
				Pattern.compile("_*[A-Za-z][A-Za-z0-9_]*"), // 4: identifier
				Pattern.compile("\\:\\="), // 5: operator
				Pattern.compile("\\.\\."), // 6: operator
				Pattern.compile("[\\<\\>\\=]+"), // 7: operator
				Pattern.compile(".") // symbol
		});
	}

	@Override
	protected JPasToken evaluateToken(int match, String raw) {
		switch(match) {
			case 0:
			case 1:
				return null;
			case 2:
				return new JPasToken(TokenType.number, raw);
			case 3:
				return new JPasToken(TokenType.string, raw.substring(1, raw.length()-1).replaceAll("\\\'\\\'", "\'"));
			case 4:
				for(int i=0; i<KEYWORDS.length; i++)
					if(KEYWORDS[i].equalsIgnoreCase(raw))
						return JPasToken.keyword(KEYWORDS[i]);
				return new JPasToken(TokenType.identifier, raw.toLowerCase());
			case 5:
			case 6:
			case 7:
				return new JPasToken(TokenType.operator, raw);
			default:
				return new JPasToken(raw.charAt(0));
		}
	}

}
