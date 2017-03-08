
package mips.K2M;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import mips.KangaParser;
import mips.ParseException;
import mips.syntaxtree.*;
import mips.visitor.*;


public class Main {
	public static void main(String [] args) throws ParseException, FileNotFoundException, kanga.ParseException, minijava.ParseException {
		String kangaout = kanga.S2K.S2K.main();
		InputStream mipsin = new ByteArrayInputStream(kangaout.getBytes());
		Node root = new KangaParser(mipsin).Goal();
//		Node root = new KangaParser(new java.io.FileInputStream("/Users/apple/Downloads/大三上/编译实习/5mips/mips资料/mytest_inputmykg/myout10.kg")).Goal();

		K2MVisitor k2m = new K2MVisitor();
        root.accept(k2m);
        System.out.println(k2m.mipscode);
//        PrintStream pr = new PrintStream(new FileOutputStream("/Users/apple/Downloads/大三上/编译实习/5mips/mips资料/mytest_final/out10.asm"));
//        pr.println(k2m.mipscode);
//        pr.close();
	}
}

