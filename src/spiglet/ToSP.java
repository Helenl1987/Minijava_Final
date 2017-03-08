
package spiglet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import minijava.MiniJavaParser;
import minijava.ParseException;
import minijava.symboltable.TopTable;
import minijava.typecheck.BuildSymbolTableVisitor;
import syntaxtree.*;
import visitor.*;


public class ToSP {
	public static String main() throws ParseException, FileNotFoundException {
		Node root = new MiniJavaParser(System.in).Goal();
//		Node root = new MiniJavaParser(new java.io.FileInputStream("/Users/apple/Downloads/������/����ʵϰ/5mips/mips����/mytest_final/jh.java")).Goal();

        TopTable toptable = new TopTable();
        root.accept(new BuildSymbolTableVisitor(), toptable);
        toptable.GetTotal();
        ToSPVisitor tosp = new ToSPVisitor();
        root.accept(tosp, toptable);
//        (new PrintStream(new FileOutputStream("/Users/apple/Downloads/������/����ʵϰ/4kanga/kanga����/mytest_inputmyspg/out10.spg"))).println(tosp.pigletCode);
//        System.out.println(tosp.pigletCode);
        return tosp.pigletCode;
	}
}

