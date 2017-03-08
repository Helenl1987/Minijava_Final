
package kanga.S2K;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import kanga.ParseException;
import kanga.SpigletParser;
import kanga.syntaxtree.*;
import kanga.visitor.*;


public class S2K {
	public static String main() throws ParseException, FileNotFoundException, minijava.ParseException {
		String spigletout = spiglet.ToSP.main();
		InputStream kangain = new ByteArrayInputStream(spigletout.getBytes());
		Node root = new SpigletParser(kangain).Goal();
//		Node root = new SpigletParser(new java.io.FileInputStream("/Users/apple/Downloads/������/����ʵϰ/4kanga/kanga����/mytest_inputmyspg/out10.spg")).Goal();

		AllCFG allcfg = new AllCFG();
        S2KVisitor s2k = new S2KVisitor();
        root.accept(s2k, allcfg);
        String res = "";
//        PrintStream pr = new PrintStream(new FileOutputStream("/Users/apple/Downloads/������/����ʵϰ/4kanga/kanga����//mytest/myout8.kg"));
//        PrintStream pr = new PrintStream(new FileOutputStream("/Users/apple/Downloads/������/����ʵϰ/4kanga/kanga����//mytest_inputmyspg/myout10.kg"));
//        PrintStream pr = new PrintStream(new FileOutputStream("/Users/apple/Downloads/������/����ʵϰ/4kanga/kanga����/myout.kg"));
        for(String cfgName: allcfg.cfgsVec) {
        	ControlFlowGraph cfg = allcfg.cfgs.get(cfgName);
//        	System.out.println("########## " + cfgName);
        	S2KVisitor2 s2k2 = new S2KVisitor2(cfg);        	
//        	pr.println(s2k2.kangacode);
//        	System.out.println(s2k2.kangacode);
        	res += s2k2.kangacode;
        }
        return res;
//        pr.close();
	}
}

