
package minijava.minijava2piglet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import minijava.MiniJavaParser;
import minijava.ParseException;
import minijava.symboltable.TopTable;
import minijava.typecheck.BuildSymbolTableVisitor;
import syntaxtree.*;
import visitor.*;


public class M2P {
	public static void main(String [] args) throws ParseException, FileNotFoundException {
		Node root = new MiniJavaParser(System.in).Goal();
//		Node root = new MiniJavaParser(new java.io.FileInputStream("/Users/apple/Desktop/tests/test99.java")).Goal();

        TopTable toptable = new TopTable();
        root.accept(new BuildSymbolTableVisitor(), toptable);
        toptable.GetTotal();
        M2PVisitor m2p = new M2PVisitor();
        root.accept(m2p, toptable);
//        (new PrintStream(new FileOutputStream("/Users/apple/Downloads/大三上/编译实习/2piglet/out.pg"))).println(m2p.pigletCode);
        System.out.println(m2p.pigletCode);
	}
}

