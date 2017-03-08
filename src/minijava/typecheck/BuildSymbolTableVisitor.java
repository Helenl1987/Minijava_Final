package minijava.typecheck;

import java.util.Enumeration;

import syntaxtree.*;
import visitor.*;
import minijava.symboltable.*;

//in buildsymboltable visitor, we only handle the declaration part because we just want to build the symbol table, so we ignore the statement part.
//multiple declaration can be checked there

public class BuildSymbolTableVisitor extends GJDepthFirst<VariType, Table> {
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

	   public VariType visit(NodeToken n, Table argu) {
//		   System.out.println(n.tokenImage); 
		   return null; 
	   }

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
	      toptable.mainclass_name = n.f1.f0.tokenImage;
	      ClassTable classtable = new ClassTable();
	      classtable.toptable = toptable;
	      classtable.class_name = n.f1.f0.tokenImage;
	      classtable.beginline = n.f1.f0.beginLine;
	      classtable.begincolumn = n.f1.f0.beginColumn;
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
	      MethodTable methodtable = new MethodTable();
	      methodtable.toptable = toptable;
	      methodtable.classtable = classtable;
	      methodtable.method_name = "main";
	      methodtable.beginline = n.f6.beginLine;
	      methodtable.begincolumn = n.f6.beginColumn;
	      methodtable.methodinclass_name = classtable.class_name;
	      methodtable.returntype = new VariType();
	      methodtable.returntype.type = "void";
	      Vari vari = new Vari();
	      vari.vari_name = n.f11.f0.tokenImage;
	      vari.vari_type = new VariType("Undefined");
	      vari.beginline = n.f11.f0.beginLine;
	      vari.begincolumn = n.f11.f0.beginColumn;
	      vari.init = true;
	      n.f12.accept(this, argu);
	      n.f13.accept(this, argu);
	      n.f14.accept(this, methodtable);
	      n.f15.accept(this, argu);
	      n.f16.accept(this, argu);
	      n.f17.accept(this, argu);
	      if(methodtable.insertparam(vari.vari_name, vari) == false) {
	    	  PrintError.errorexist = true;
	    	  String emsg = "multiple declaration for variable "+vari.vari_name;
	    	  PrintError.print(emsg, vari.beginline, vari.begincolumn, 1);
	      }
	      if(classtable.insertmethod(methodtable.method_name, methodtable) == false) {
	    	  PrintError.errorexist = true;
	    	  String emsg = "multiple declaration for function "+methodtable.method_name;
	    	  PrintError.print(emsg, methodtable.beginline, methodtable.begincolumn, 1);
	      }
	      if(toptable.insertclass(classtable.class_name, classtable) == false) {
	    	  PrintError.errorexist = true;
	    	  String emsg = "multiple declaration for class "+classtable.class_name;
	    	  PrintError.print(emsg, classtable.beginline, classtable.begincolumn, 1);
	      }
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
	      ClassTable classtable = new ClassTable();
	      classtable.toptable = toptable;
	      classtable.class_name = n.f1.f0.tokenImage;
	      classtable.beginline = n.f1.f0.beginLine;
	      classtable.begincolumn = n.f1.f0.beginColumn;
	      n.f2.accept(this, argu);
	      n.f3.accept(this, classtable);
	      n.f4.accept(this, classtable);
	      n.f5.accept(this, argu);
	      if(toptable.insertclass(classtable.class_name, classtable) == false) {
	    	  PrintError.errorexist = true;
	    	  String emsg = "multiple declaration for class "+classtable.class_name;
	    	  PrintError.print(emsg, classtable.beginline, classtable.begincolumn, 1);
	      }
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
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
	      ClassTable classtable = new ClassTable();
	      classtable.toptable = toptable;
	      classtable.class_name = n.f1.f0.tokenImage;
	      classtable.beginline = n.f1.f0.beginLine;
	      classtable.begincolumn = n.f1.f0.beginColumn;
	      classtable.parentclass_name = n.f3.f0.tokenImage;
	      n.f4.accept(this, argu);
	      n.f5.accept(this, classtable);
	      n.f6.accept(this, classtable);
	      n.f7.accept(this, argu);
	      if(toptable.insertclass(classtable.class_name, classtable) == false) {
	    	  PrintError.errorexist = true;
	    	  String emsg = "multiple declaration for class "+classtable.class_name;
	    	  PrintError.print(emsg, classtable.beginline, classtable.begincolumn, 1);
	      }	      return _ret;
	   }

	   /**
	    * f0 -> Type()
	    * f1 -> Identifier()
	    * f2 -> ";"
	    */
	   public VariType visit(VarDeclaration n, Table argu) {
	      VariType _ret=null;
	      VariType varitype = n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      Vari vari= new Vari();
	      vari.vari_name = n.f1.f0.tokenImage;
	      vari.vari_type = varitype;
	      vari.beginline = n.f1.f0.beginLine;
	      vari.begincolumn = n.f1.f0.beginColumn;
	      if(argu.insertvariable(vari.vari_name, vari) == false) {
	    	  PrintError.errorexist = true;
	    	  String emsg = "multiple declaration for variable "+vari.vari_name;
	    	  PrintError.print(emsg, vari.beginline, vari.begincolumn, 1);
	      }
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
	      VariType varitype = n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      MethodTable methodtable = new MethodTable();
	      methodtable.toptable = classtable.toptable;
	      methodtable.classtable = classtable;
	      methodtable.method_name = n.f2.f0.tokenImage;
	      methodtable.beginline = n.f2.f0.beginLine;
	      methodtable.begincolumn = n.f2.f0.beginColumn;
	      methodtable.methodinclass_name = classtable.class_name;
	      methodtable.returntype = varitype;
	      n.f3.accept(this, argu);
	      n.f4.accept(this, methodtable);
	      n.f5.accept(this, argu);
	      n.f6.accept(this, argu);
	      n.f7.accept(this, methodtable);
	      n.f8.accept(this, argu);
	      n.f9.accept(this, argu);
	      n.f10.accept(this, argu);
	      n.f11.accept(this, argu);
	      n.f12.accept(this, argu);
	      if(classtable.insertmethod(methodtable.method_name, methodtable) == false) {
	    	  PrintError.errorexist = true;
	    	  String emsg = "multiple declaration for function "+methodtable.method_name;
	    	  PrintError.print(emsg, methodtable.beginline, methodtable.begincolumn, 1);
	      }
	      return _ret;
	   }

	   /**
	    * f0 -> FormalParameter()
	    * f1 -> ( FormalParameterRest() )*
	    */
	   public VariType visit(FormalParameterList n, Table argu) {
	      VariType _ret=null;
	      MethodTable methodtable = (MethodTable)argu;
	      methodtable.paramnum++;
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
	      MethodTable methodtable = (MethodTable)argu;
	      VariType varitype = n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      Vari vari = new Vari();
	      vari.vari_name = n.f1.f0.tokenImage;
	      vari.vari_type = varitype;
	      vari.beginline = n.f1.f0.beginLine;
	      vari.begincolumn = n.f1.f0.beginColumn;
	      vari.init = true;
	      if(methodtable.insertparam(vari.vari_name, vari) == false) {
	    	  PrintError.errorexist = true;
	    	  String emsg = "multiple declaration for variable "+vari.vari_name;
	    	  PrintError.print(emsg, vari.beginline, vari.begincolumn, 1);
	      }
	      return _ret;
	   }

	   /**
	    * f0 -> ","
	    * f1 -> FormalParameter()
	    */
	   public VariType visit(FormalParameterRest n, Table argu) {
	      VariType _ret=null;
	      MethodTable methodtable = (MethodTable)argu;
	      methodtable.paramnum++;
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
	      _ret = n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "int"
	    * f1 -> "["
	    * f2 -> "]"
	    */
	   public VariType visit(ArrayType n, Table argu) {
	      VariType _ret = new VariType();
	      _ret.type = "Array";
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "boolean"
	    */
	   public VariType visit(BooleanType n, Table argu) {
	      VariType _ret = new VariType();
	      _ret.type = "Boolean";
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "int"
	    */
	   public VariType visit(IntegerType n, Table argu) {
	      VariType _ret = new VariType();
	      _ret.type = "Integer";
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
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
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
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      n.f5.accept(this, argu);
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
	      n.f2.accept(this, argu);
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
	      n.f2.accept(this, argu);
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
	      n.f2.accept(this, argu);
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
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "&&"
	    * f2 -> PrimaryExpression()
	    */
	   public VariType visit(AndExpression n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "<"
	    * f2 -> PrimaryExpression()
	    */
	   public VariType visit(CompareExpression n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "+"
	    * f2 -> PrimaryExpression()
	    */
	   public VariType visit(PlusExpression n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "-"
	    * f2 -> PrimaryExpression()
	    */
	   public VariType visit(MinusExpression n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "*"
	    * f2 -> PrimaryExpression()
	    */
	   public VariType visit(TimesExpression n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "["
	    * f2 -> PrimaryExpression()
	    * f3 -> "]"
	    */
	   public VariType visit(ArrayLookup n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "."
	    * f2 -> "length"
	    */
	   public VariType visit(ArrayLength n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
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
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      n.f5.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> Expression()
	    * f1 -> ( ExpressionRest() )*
	    */
	   public VariType visit(ExpressionList n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> ","
	    * f1 -> Expression()
	    */
	   public VariType visit(ExpressionRest n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
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
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> <INTEGER_LITERAL>
	    */
	   public VariType visit(IntegerLiteral n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "true"
	    */
	   public VariType visit(TrueLiteral n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "false"
	    */
	   public VariType visit(FalseLiteral n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> <IDENTIFIER>
	    */
	   public VariType visit(Identifier n, Table argu) {
	      VariType _ret = new VariType();
	      _ret.type = "Object";
	      _ret.name = n.f0.tokenImage;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "this"
	    */
	   public VariType visit(ThisExpression n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
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
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "new"
	    * f1 -> Identifier()
	    * f2 -> "("
	    * f3 -> ")"
	    */
	   public VariType visit(AllocationExpression n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "!"
	    * f1 -> Expression()
	    */
	   public VariType visit(NotExpression n, Table argu) {
	      VariType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
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
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }
}