package minijava.typecheck;

import java.util.Enumeration;

import syntaxtree.*;
import visitor.*;
import minijava.symboltable.*;

//bonus part: warning is checked in this visitor
//four kinds of warning can be checked:
//array used before allocation
//array index go out of bounds
//local variables may not have been initialized before use
//value of local variable is not used(in fact it is checked in toptable.isunused() function)

public class WarningCheckVisitor extends GJDepthFirst<VariType, Table> {
	   //
	   // Auto class visitors--probably don't need to be overridden.
	   //
	   public VariType visit(NodeList n, Table argu) {
	      VariType _ret=null;
	      int _count=0;
	      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
	         e.nextElement().accept(this,argu);
	         _count++;
	      }
	      return _ret;
	   }

	   public VariType visit(NodeListOptional n, Table argu) {
	      if ( n.present() ) {
	         VariType _ret=null;
	         int _count=0;
	         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
	            e.nextElement().accept(this,argu);
	            _count++;
	         }
	         return _ret;
	      }
	      else
	         return null;
	   }

	   public VariType visit(NodeOptional n, Table argu) {
	      if ( n.present() )
	         return n.node.accept(this,argu);
	      else
	         return null;
	   }

	   public VariType visit(NodeSequence n, Table argu) {
	      VariType _ret=null;
	      int _count=0;
	      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
	         e.nextElement().accept(this,argu);
	         _count++;
	      }
	      return _ret;
	   }

	   public VariType visit(NodeToken n, Table argu) { return null; }

	   //
	   // User-generated visitor methods below
	   //

	   /**
	    * f0 -> MainClass()
	    * f1 -> ( TypeDeclaration() )*
	    * f2 -> <EOF>
	    */
	   public VariType visit(Goal n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "class"
	    * f1 -> Identifier()
	    * f2 -> "{"
	    * f3 -> "public"
	    * f4 -> "static"
	    * f5 -> "void"
	    * f6 -> "main"
	    * f7 -> "("
	    * f8 -> "String"
	    * f9 -> "["
	    * f10 -> "]"
	    * f11 -> Identifier()
	    * f12 -> ")"
	    * f13 -> "{"
	    * f14 -> ( VarDeclaration() )*
	    * f15 -> ( Statement() )*
	    * f16 -> "}"
	    * f17 -> "}"
	    */
	   public VariType visit(MainClass n, Table argu) {
	      VariType _ret=null;
	      TopTable toptable = (TopTable)argu;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      n.f5.accept(this, argu);
	      n.f6.accept(this, argu);
	      n.f7.accept(this, argu);
	      n.f8.accept(this, argu);
	      n.f9.accept(this, argu);
	      n.f10.accept(this, argu);
	      n.f11.accept(this, argu);
	      n.f12.accept(this, argu);
	      n.f13.accept(this, argu);
	      n.f14.accept(this, toptable.getclasstable(toptable.mainclass_name).getmethodtable("main"));
	      n.f15.accept(this, toptable.getclasstable(toptable.mainclass_name).getmethodtable("main"));
	      n.f16.accept(this, argu);
	      n.f17.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> ClassDeclaration()
	    *       | ClassExtendsDeclaration()
	    */
	   public VariType visit(TypeDeclaration n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "class"
	    * f1 -> Identifier()
	    * f2 -> "{"
	    * f3 -> ( VarDeclaration() )*
	    * f4 -> ( MethodDeclaration() )*
	    * f5 -> "}"
	    */
	   public VariType visit(ClassDeclaration n, Table argu) {
	      VariType _ret=null;
	      TopTable toptable = (TopTable)argu;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      ClassTable classtable = toptable.getclasstable(n.f1.f0.tokenImage);
	      n.f2.accept(this, argu);
	      n.f3.accept(this, classtable);
	      n.f4.accept(this, classtable);
	      n.f5.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "class"
	    * f1 -> Identifier()
	    * f2 -> "extends"
	    * f3 -> Identifier()
	    * f4 -> "{"
	    * f5 -> ( VarDeclaration() )*
	    * f6 -> ( MethodDeclaration() )*
	    * f7 -> "}"
	    */
	   public VariType visit(ClassExtendsDeclaration n, Table argu) {
	      VariType _ret=null;
	      TopTable toptable = (TopTable)argu;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      ClassTable classtable = toptable.getclasstable(n.f1.f0.tokenImage);
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      n.f5.accept(this, classtable);
	      n.f6.accept(this, classtable);
	      n.f7.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> Type()
	    * f1 -> Identifier()
	    * f2 -> ";"
	    */
	   public VariType visit(VarDeclaration n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "public"
	    * f1 -> Type()
	    * f2 -> Identifier()
	    * f3 -> "("
	    * f4 -> ( FormalParameterList() )?
	    * f5 -> ")"
	    * f6 -> "{"
	    * f7 -> ( VarDeclaration() )*
	    * f8 -> ( Statement() )*
	    * f9 -> "return"
	    * f10 -> Expression()
	    * f11 -> ";"
	    * f12 -> "}"
	    */
	   public VariType visit(MethodDeclaration n, Table argu) {
	      VariType _ret=null;
	      ClassTable classtable = (ClassTable)argu;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      MethodTable methodtable = classtable.getmethodtable(n.f2.f0.tokenImage);
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      n.f5.accept(this, argu);
	      n.f6.accept(this, argu);
	      n.f7.accept(this, methodtable);
	      n.f8.accept(this, methodtable);
	      n.f9.accept(this, argu);
	      VariType varitype = n.f10.accept(this, methodtable);
//	      if(VariType.isTheSameType(classtable.toptable, methodtable.returntype, varitype) == false) {
//	    	  PrintError.errorexist = true;
//	    	  String emsg="return value does not match return type: return value is type "+varitype.type+", expected return type is "+methodtable.returntype.type;
//	    	  PrintError.print(emsg, n.f9.beginLine, n.f9.beginColumn, 1);
//	      }
	      n.f11.accept(this, argu);
	      n.f12.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> FormalParameter()
	    * f1 -> ( FormalParameterRest() )*
	    */
	   public VariType visit(FormalParameterList n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> Type()
	    * f1 -> Identifier()
	    */
	   public VariType visit(FormalParameter n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> ","
	    * f1 -> FormalParameter()
	    */
	   public VariType visit(FormalParameterRest n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> ArrayType()
	    *       | BooleanType()
	    *       | IntegerType()
	    *       | Identifier()
	    */
	   public VariType visit(Type n, Table argu) { 
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "int"
	    * f1 -> "["
	    * f2 -> "]"
	    */
	   public VariType visit(ArrayType n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "boolean"
	    */
	   public VariType visit(BooleanType n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "int"
	    */
	   public VariType visit(IntegerType n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> Block()
	    *       | AssignmentStatement()
	    *       | ArrayAssignmentStatement()
	    *       | IfStatement()
	    *       | WhileStatement()
	    *       | PrintStatement()
	    */
	   public VariType visit(Statement n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "{"
	    * f1 -> ( Statement() )*
	    * f2 -> "}"
	    */
	   public VariType visit(Block n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> Identifier()
	    * f1 -> "="
	    * f2 -> Expression()
	    * f3 -> ";"
	    */
	   public VariType visit(AssignmentStatement n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      MethodTable methodtable = (MethodTable)argu;
	      Vari vari = methodtable.getvari(n.f0.f0.tokenImage);
	      if(vari == null) {
	    	  ClassTable classtable = methodtable.classtable;
	    	  vari = classtable.getvari(n.f0.f0.tokenImage);
	    	  if(vari == null) {
//	    		  PrintError.errorexist = true;
//		    	  String emsg = "undefined variable \""+n.f0.f0.tokenImage+"\"";
//	    		  PrintError.print(emsg, n.f0.f0.beginLine, n.f0.f0.beginColumn, 1);
	    		  return _ret;
	    	  }
	      }
	      n.f1.accept(this, argu);
	      VariType t2 = n.f2.accept(this, argu);
//	      if(VariType.isTheSameType(methodtable.toptable, vari.vari_type, t2) == false) {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "assignstatement does not have matched types, one has type "+vari.vari_type.type+" , the other one has type "+t2.type;
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	    	  return _ret;
//	      }
//	      if(t2.type == "Array" && t2.arraylength != 0) {
//	    	  vari.alen = t2.arraylength;
//	      }
	      n.f3.accept(this, argu);
	      vari.init = true;
	      return _ret;
	   }

	   /**
	    * f0 -> Identifier()
	    * f1 -> "["
	    * f2 -> Expression()
	    * f3 -> "]"
	    * f4 -> "="
	    * f5 -> Expression()
	    * f6 -> ";"
	    */
	   public VariType visit(ArrayAssignmentStatement n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      MethodTable methodtable = (MethodTable)argu;
    	  Vari vari = methodtable.getvari(n.f0.f0.tokenImage);
    	  if(vari == null) {
    		  ClassTable classtable = methodtable.classtable;
    		  vari = classtable.getvari(n.f0.f0.tokenImage);
    		  if(vari == null) {
//    			  PrintError.errorexist = true;
//    			  String emsg = "undefined variable \""+n.f0.f0.tokenImage+"\"";
//    			  PrintError.print(emsg, n.f0.f0.beginLine, n.f0.f0.beginColumn, 1);
    			  return _ret;
    		  }
    	  }
    	  if(vari.init == false) {
    		  String emsg = "array \"" + n.f0.f0.tokenImage + "\" used before allocation";
    		  PrintError.print(emsg, n.f0.f0.beginLine, n.f0.f0.beginColumn, 2);
    	  }
//    	  if(vari.vari_type.type != "Array") {
//    		  PrintError.errorexist = true;
//	    	  String emsg = "variable \""+n.f0.f0.tokenImage+"\" does not match type Array, cannot use operator []";
//	    	  PrintError.print(emsg, n.f0.f0.beginLine, n.f0.f0.beginColumn, 1);
//	      }
	      n.f1.accept(this, argu);
	      VariType t2 = n.f2.accept(this, argu);
//	      if(t2.type != "Integer") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "array subscript does not match type Integer";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
	      if(vari.vari_type.type == "Array" && vari.alen != 0 && t2.type == "Integer" && t2.ifintegerliteral == true) {
	    	  if(t2.integerliteral >= vari.alen || t2.integerliteral < 0) {
	    		  String emsg = "array index may go out of bounds, the array length is " + vari.alen + ", but index is " + t2.integerliteral;
	    		  PrintError.print(emsg, n.f0.f0.beginLine, n.f0.f0.beginColumn, 2);
	    	  }
	      }
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      VariType t5 = n.f5.accept(this, argu);
//	      if(t5.type != "Integer") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "assignstatement does not have matched types, one has type Integer, the other one has type "+t2.type;
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
	      n.f6.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "if"
	    * f1 -> "("
	    * f2 -> Expression()
	    * f3 -> ")"
	    * f4 -> Statement()
	    * f5 -> "else"
	    * f6 -> Statement()
	    */
	   public VariType visit(IfStatement n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      VariType t2 = n.f2.accept(this, argu);
//	      if(t2.type != "Boolean") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "expression in If Statement does not match type Boolean";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      n.f5.accept(this, argu);
	      n.f6.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "while"
	    * f1 -> "("
	    * f2 -> Expression()
	    * f3 -> ")"
	    * f4 -> Statement()
	    */
	   public VariType visit(WhileStatement n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      VariType t2 = n.f2.accept(this, argu);
//	      if(t2.type != "Boolean") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "expression in While Statement does not match type Boolean";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "System.out.println"
	    * f1 -> "("
	    * f2 -> Expression()
	    * f3 -> ")"
	    * f4 -> ";"
	    */
	   public VariType visit(PrintStatement n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
//	      n.f2.accept(this, argu);
	      VariType t2 = n.f2.accept(this, argu);
//	      if(t2.type != "Integer") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "System.out can only print Integer type";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> AndExpression()
	    *       | CompareExpression()
	    *       | PlusExpression()
	    *       | MinusExpression()
	    *       | TimesExpression()
	    *       | ArrayLookup()
	    *       | ArrayLength()
	    *       | MessageSend()
	    *       | PrimaryExpression()
	    */
	   public VariType visit(Expression n, Table argu) {
	      VariType _ret=null;
	      _ret = n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "&&"
	    * f2 -> PrimaryExpression()
	    */
	   public VariType visit(AndExpression n, Table argu) {
	      VariType _ret = new VariType();
	      VariType t0 = n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      VariType t2 = n.f2.accept(this, argu);
//	      if(t0.type != "Boolean") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "variable type does not match, variable has type "+t0.type+", expect type Boolean";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
//	      if(t2.type != "Boolean") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "variable type does not match, variable has type "+t2.type+", expect type Boolean";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
	      _ret.type = "Boolean";
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "<"
	    * f2 -> PrimaryExpression()
	    */
	   public VariType visit(CompareExpression n, Table argu) {
	      VariType _ret = new VariType();
	      VariType t0 = n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      VariType t2 = n.f2.accept(this, argu);
//	      if(t0.type != "Integer") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "variable type does not match, variable has type "+t0.type+", expect type Integer";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
//	      if(t2.type != "Integer") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "variable type does not match, variable has type "+t2.type+", expect type Integer";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
	      _ret.type = "Boolean";
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "+"
	    * f2 -> PrimaryExpression()
	    */
	   public VariType visit(PlusExpression n, Table argu) {
	      VariType _ret = new VariType();
	      VariType t0 = n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      VariType t2 = n.f2.accept(this, argu);
//	      if(t0.type != "Integer") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "variable type does not match, variable has type "+t0.type+", expect type Integer";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
//	      if(t2.type != "Integer") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "variable type does not match, variable has type "+t2.type+", expect type Integer";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
	      _ret.type = "Integer";
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "-"
	    * f2 -> PrimaryExpression()
	    */
	   public VariType visit(MinusExpression n, Table argu) {
	      VariType _ret = new VariType();
	      VariType t0 = n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      VariType t2 = n.f2.accept(this, argu);
//	      if(t0.type != "Integer") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "variable type does not match, variable has type "+t0.type+", expect type Integer";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
//	      if(t2.type != "Integer") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "variable type does not match, variable has type "+t2.type+", expect type Integer";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
	      _ret.type = "Integer";
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "*"
	    * f2 -> PrimaryExpression()
	    */
	   public VariType visit(TimesExpression n, Table argu) {
	      VariType _ret = new VariType();
	      VariType t0 = n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      VariType t2 = n.f2.accept(this, argu);
//	      if(t0.type != "Integer") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "variable type does not match, variable has type "+t0.type+", expect type Integer";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
//	      if(t2.type != "Integer") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "variable type does not match, variable has type "+t2.type+", expect type Integer";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
	      _ret.type = "Integer";
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "["
	    * f2 -> PrimaryExpression()
	    * f3 -> "]"
	    */
	   public VariType visit(ArrayLookup n, Table argu) {
	      VariType _ret = new VariType();
	      VariType t0 = n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      VariType t2 = n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
//	      if(t0.type != "Array") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "variable does not match type Array, cannot use operator []";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
//	      if(t2.type != "Integer") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "array subscript does not match type Integer";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
	      if(t0.type == "Array" && t2.type == "Integer") {
	    	  NodeToken nodetoken = ((Identifier)(n.f0.f0.choice)).f0;
	    	  MethodTable methodtable = (MethodTable)argu;
	    	  Vari vari = methodtable.getvari(nodetoken.tokenImage);
	    	  if(vari == null) {
	    		  ClassTable classtable = methodtable.classtable;
	    		  vari = classtable.getvari(nodetoken.tokenImage);
	    		  if(vari == null) {
	    			  _ret = new VariType();
	    			  _ret.type = "Undefined";
	    			  return _ret;
	    		  }
	    	  }
	    	  if(vari.alen != 0 && t2.ifintegerliteral == true) {
	    		  if(t2.integerliteral >= vari.alen || t2.integerliteral < 0) {
	    			  String emsg = "array index may go out of bounds, the array length is " + vari.alen + ", but index is " + t2.integerliteral;
	    			  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 2);
	    		  }
	    	  }
	      }
	      _ret.type = "Integer";
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "."
	    * f2 -> "length"
	    */
	   public VariType visit(ArrayLength n, Table argu) {
	      VariType _ret = new VariType();
	      VariType t0 = n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
//	      if(t0.type != "Array") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "variable does not match type Array, cannot use operator []";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1);
//	      }
	      _ret.type = "Integer";
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "."
	    * f2 -> Identifier()
	    * f3 -> "("
	    * f4 -> ( ExpressionList() )?
	    * f5 -> ")"
	    */
	   public VariType visit(MessageSend n, Table argu) {
	      VariType _ret=null;
	      VariType t0 = n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      if(t0.type != "Object") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "not a class object, cannot use function call";
//	    	  PrintError.print(emsg, n.f1.beginLine, n.f1.beginColumn, 1); 
	    	  return t0;
	      }
	      ClassTable classtable = ((MethodTable)argu).classtable.toptable.getclasstable(t0.name);
	      MethodTable methodtable = classtable.getmethodtable(n.f2.f0.tokenImage);
	      if(methodtable == null) {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "undefined method \""+n.f2.f0.tokenImage+"\"";
//	    	  PrintError.print(emsg, n.f2.f0.beginLine, n.f2.f0.beginColumn, 1);
	    	  return t0;
	      }
//	      MethodParam methodparam = new MethodParam();
//	      methodparam.methodtable = (MethodTable)argu;
	      n.f3.accept(this, argu);
//	      n.f4.accept(this, methodparam);
	      n.f4.accept(this, argu);
	      n.f5.accept(this, argu);
//	      if(methodtable.paramnum != methodparam.paramnum) {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "number of function parameters does not match, expect "+methodtable.paramnum+" parameters";
//	    	  PrintError.print(emsg, n.f3.beginLine, n.f3.beginColumn, 1);
//	    	  _ret = methodtable.returntype;
//	    	  return _ret;
//	      }
//	      VariType t1, t2;
//	      TopTable toptable = ((MethodTable)argu).toptable;
//	      for(int i = 0; i < methodtable.paramtype.size(); i++) {
//	    	  t1 = methodtable.paramtype.get(i);
//	    	  t2 = methodparam.paramtype.get(i);
//	    	  if(VariType.isTheSameType(toptable, t1, t2) == false) {
//	    		  PrintError.errorexist = true;
//	    		  String emsg="type of function parameters does not match";
//	    		  PrintError.print(emsg, n.f3.beginLine, n.f3.beginColumn, 1);
//	    	  }
//	      }
	      _ret = methodtable.returntype;
	      return _ret;
	   }

	   /**
	    * f0 -> Expression()
	    * f1 -> ( ExpressionRest() )*
	    */
	   public VariType visit(ExpressionList n, Table argu) {
	      VariType _ret=null;
//	      MethodParam methodparam = (MethodParam)argu;
//	      methodparam.paramnum++;
//	      VariType t0 = n.f0.accept(this, methodparam.methodtable);
	      n.f0.accept(this, argu);
//	      methodparam.insertparamtype(t0);
	      n.f1.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> ","
	    * f1 -> Expression()
	    */
	   public VariType visit(ExpressionRest n, Table argu) {
	      VariType _ret=null;
//	      MethodParam methodparam = (MethodParam)argu;
//	      methodparam.paramnum++;
	      n.f0.accept(this, argu);
//	      VariType t1 = n.f1.accept(this, methodparam.methodtable);
	      n.f1.accept(this, argu);
//	      methodparam.insertparamtype(t1);
	      return _ret;
	   }

	   /**
	    * f0 -> IntegerLiteral()
	    *       | TrueLiteral()
	    *       | FalseLiteral()
	    *       | Identifier()
	    *       | ThisExpression()
	    *       | ArrayAllocationExpression()
	    *       | AllocationExpression()
	    *       | NotExpression()
	    *       | BracketExpression()
	    */
	   public VariType visit(PrimaryExpression n, Table argu) { 
	      VariType _ret=null;
	      _ret = n.f0.accept(this, argu);
	      if(n.f0.choice instanceof Identifier) {
	    	  NodeToken nodetoken = ((Identifier)(n.f0.choice)).f0;
	    	  MethodTable methodtable = (MethodTable)argu;
	    	  Vari vari = methodtable.getvari(nodetoken.tokenImage);
	    	  if(vari != null) {
	    		  if(vari.init == false) {
	    			  String emsg = "local variable \"" + nodetoken.tokenImage + "\" may not have been initialized before use";
	    			  PrintError.print(emsg, nodetoken.beginLine, nodetoken.beginColumn, 2);
	    		  }
	    		  vari.use = true;
	    		  return vari.vari_type;
	    	  }
	    	  ClassTable classtable = methodtable.classtable;
	    	  vari = classtable.getvari(nodetoken.tokenImage);
	    	  if(vari != null) {
	    		  vari.use = true;
	    		  return vari.vari_type;
	    	  }
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "undefined variable \""+nodetoken.tokenImage+"\"";
//	    	  PrintError.print(emsg, nodetoken.beginLine, nodetoken.beginColumn, 1);
	    	  _ret = new VariType();
	    	  _ret.type = "Undefined";
	      }
	      return _ret;
	   }

	   /**
	    * f0 -> <INTEGER_LITERAL>
	    */
	   public VariType visit(IntegerLiteral n, Table argu) {
	      VariType _ret = new VariType();
	      n.f0.accept(this, argu);
	      _ret.type = "Integer";
	      _ret.ifintegerliteral = true;
	      _ret.integerliteral = Integer.parseInt(n.f0.tokenImage);
	      return _ret;
	   }

	   /**
	    * f0 -> "true"
	    */
	   public VariType visit(TrueLiteral n, Table argu) {
	      VariType _ret = new VariType();
	      n.f0.accept(this, argu);
	      _ret.type = "Boolean";
	      return _ret;
	   }

	   /**
	    * f0 -> "false"
	    */
	   public VariType visit(FalseLiteral n, Table argu) {
	      VariType _ret = new VariType();
	      n.f0.accept(this, argu);
	      _ret.type = "Boolean";
	      return _ret;
	   }

	   /**
	    * f0 -> <IDENTIFIER>
	    */
	   public VariType visit(Identifier n, Table argu) { 
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "this"
	    */
	   public VariType visit(ThisExpression n, Table argu) {
	      VariType _ret = new VariType();
	      MethodTable methodtable = (MethodTable)argu;
	      n.f0.accept(this, argu);
	      _ret.type = "Object";
	      _ret.name = methodtable.methodinclass_name;
	      return _ret;
	   }

	   /**
	    * f0 -> "new"
	    * f1 -> "int"
	    * f2 -> "["
	    * f3 -> Expression()
	    * f4 -> "]"
	    */
	   public VariType visit(ArrayAllocationExpression n, Table argu) { 
	      VariType _ret = new VariType();
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      VariType t3 = n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
//	      if(t3.type != "Integer") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "array length type does not match type Integer";
//	    	  PrintError.print(emsg, n.f2.beginLine, n.f2.beginColumn, 1);
//	      }
	      _ret.type = "Array";
	      return _ret;
	   }

	   /**
	    * f0 -> "new"
	    * f1 -> Identifier()
	    * f2 -> "("
	    * f3 -> ")"
	    */
	   public VariType visit(AllocationExpression n, Table argu) {
	      VariType _ret = new VariType();
	      n.f0.accept(this, argu);
	      VariType t1 = n.f1.accept(this, argu);
	      if(((MethodTable)argu).toptable.getclasstable(n.f1.f0.tokenImage) == null) {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "undefined class \""+n.f1.f0.tokenImage+"\"";
//	    	  PrintError.print(emsg, n.f2.beginLine, n.f2.beginColumn, 1);
	    	  _ret.type = "Undefined";
	    	  return _ret;
	      }
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
	      _ret.type = "Object";
	      _ret.name = n.f1.f0.tokenImage;
	      return _ret;
	   }

	   /**
	    * f0 -> "!"
	    * f1 -> Expression()
	    */
	   public VariType visit(NotExpression n, Table argu) { 
	      VariType _ret = new VariType();
	      n.f0.accept(this, argu);
	      VariType t1 = n.f1.accept(this, argu);
//	      if(t1.type != "Boolean") {
//	    	  PrintError.errorexist = true;
//	    	  String emsg = "variable type does not match, variable has type "+t1.type+", expect type Boolean";
//	    	  PrintError.print(emsg, n.f0.beginLine, n.f0.beginColumn, 1);
//	      }
	      _ret.type = "Boolean";
	      return _ret;
	   }

	   /**
	    * f0 -> "("
	    * f1 -> Expression()
	    * f2 -> ")"
	    */
	   public VariType visit(BracketExpression n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      _ret = n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }
}