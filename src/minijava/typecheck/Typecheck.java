
package minijava.typecheck;

import java.io.FileNotFoundException;

import minijava.MiniJavaParser;
import minijava.ParseException;
import syntaxtree.*;
import visitor.*;
import minijava.symboltable.*;

public class Typecheck {
   public static void main(String [] args) throws FileNotFoundException{
//   public static void main(String [] args) throws FileNotFoundException{
      try {
//    	 PrintError.level = 2;
//    	 PrintError.level = 1;
    	 PrintError.level = 0;
         Node root = new MiniJavaParser(System.in).Goal();
//         Node root = new MiniJavaParser(new java.io.FileInputStream(args[0])).Goal();
         TopTable toptable = new TopTable();
         
         //first pass: build symbol table
         root.accept(new BuildSymbolTableVisitor(), toptable);
         toptable.classundefine();
         if(PrintError.undefineflag == false) {
        	 toptable.loopextend();
        	 toptable.override();
        	 if(PrintError.loopflag == false) {
        		 //second pass: type check
        		 root.accept(new TypeCheckVisitor(), toptable);  
        		 //third pass: warning check
        		 root.accept(new WarningCheckVisitor(), toptable);
        		 toptable.isunused();
        	 }
         }
         PrintError.print(0);        	 
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
}


