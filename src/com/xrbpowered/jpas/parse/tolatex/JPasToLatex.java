package com.xrbpowered.jpas.parse.tolatex;

import java.io.File;
import java.io.IOException;

import com.xrbpowered.jpas.parse.JPasToken;
import com.xrbpowered.jpas.parse.JPasTokeniser;
import com.xrbpowered.jpas.parse.JPasToken.TokenType;

public class JPasToLatex {

	private static String process(String ws) {
		// WTF mode on
		return ws.
				replaceAll("\\{", "\\\\{").
				replaceAll("\\}", "\\\\}").
				replaceAll("\\^", "\\\\^\\{\\}").
				replaceAll("\\r?\\n", "\\\\\\\\\n").
				replaceAll("\\\\\\\\\n\\\\\\\\", "\\\\\\\\\n~\\\\\\\\").
				replaceAll("\\n\\t", "\n\\\\null\\\\quad ").
				replaceAll("\\t", "\\\\quad ").
				replaceAll("\\s\\\\quad", "\\\\quad");
	}
	
	public static void main(String[] args) {
		try {
			File in = new File(args.length<1 ? "tolatex.pas" : args[0]);
			JPasTokeniser tokeniser = new JPasTokeniser(true);
			tokeniser.start(in);
			
			String prevWs = "\\codebox{%\n";
			for(;;) {
				JPasToken token = tokeniser.getNextToken();
				if(token==null)
					break;
				
				String s;
				if(token.type==TokenType.symbol)
					s = ""+token.value;
				else
					s = (String) token.value;
				s = process(s);

				if(token.type!=TokenType.whitespace) {
					System.out.print(prevWs);
					prevWs = "";
				}
				else {
					prevWs = s;
					continue;
				}
				if(token.type==TokenType.keyword) {
					System.out.print("\\kw{");
					System.out.print(s);
					System.out.print("}");
				}
				else if(token.type==TokenType.string) {
					System.out.print("`");
					System.out.print(s);
					System.out.print("\'");
				}
				else if(token.type==TokenType.comment) {
					System.out.print("\\comm{");
					System.out.print(s);
					System.out.print("}");
				}
				else
					System.out.print(s);
			}
			System.out.println("%\n}");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

}
