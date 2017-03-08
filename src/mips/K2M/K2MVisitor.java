
package mips.K2M;

import java.util.Enumeration;

import mips.syntaxtree.*;
import mips.visitor.*;

public class K2MVisitor extends GJNoArguDepthFirst<String> {
	
	public String mipscode = "";
	public int stackunit;
	
	public void AddString(String s) {
		mipscode += "\t" + s + "\n";
	}
	
	//
	// Auto class visitors--probably don't need to be overridden.
	//
	public String visit(NodeList n) {
		String _ret = null;
		int _count = 0;
		for (Enumeration<Node> e = n.elements(); e.hasMoreElements();) {
			e.nextElement().accept(this);
			_count++;
		}
		return _ret;
	}

	public String visit(NodeListOptional n) {
		if (n.present()) {
			String _ret = null;
			int _count = 0;
			for (Enumeration<Node> e = n.elements(); e.hasMoreElements();) {
				e.nextElement().accept(this);
				_count++;
			}
			return _ret;
		} else
			return null;
	}

	public String visit(NodeOptional n) {
		if (n.present()) {
			String label = n.node.accept(this);
			if(n.node instanceof Label) {
				mipscode += label + ":";
			}
			return label;
		}
		else
			return null;
	}

	public String visit(NodeSequence n) {
		String _ret = null;
		int _count = 0;
		for (Enumeration<Node> e = n.elements(); e.hasMoreElements();) {
			e.nextElement().accept(this);
			_count++;
		}
		return _ret;
	}

	public String visit(NodeToken n) {
		return null;
	}

	//
	// User-generated visitor methods below
	//

	/**
	 * f0 -> "MAIN" 
	 * f1 -> "[" 
	 * f2 -> IntegerLiteral() 
	 * f3 -> "]" 
	 * f4 -> "[" 
	 * f5 -> IntegerLiteral() 
	 * f6 -> "]" 
	 * f7 -> "[" 
	 * f8 -> IntegerLiteral() 
	 * f9 -> "]" 
	 * f10 -> StmtList() 
	 * f11 -> "END" 
	 * f12 -> ( Procedure() )* 
	 * f13 -> <EOF>
	 */
	public String visit(Goal n) {
		String _ret = null;
		mipscode += ".text\n";
		mipscode += ".globl\tmain\n";
		mipscode += "main:\n";
		n.f0.accept(this);
		n.f1.accept(this);
		n.f2.accept(this);
		n.f3.accept(this);
		n.f4.accept(this);
		stackunit = Integer.parseInt(n.f5.accept(this));
		n.f6.accept(this);
		n.f7.accept(this);
		n.f8.accept(this);
		n.f9.accept(this);
		AddString("move $fp, $sp");
		AddString("subu $sp, $sp, " + (stackunit*4 + 4));
		AddString("sw $ra, -4($fp)");
		n.f10.accept(this);
		AddString("lw $ra, -4($fp)");
		AddString("addu $sp, $sp, " + (stackunit*4 + 4));
		AddString("j $ra");
		n.f11.accept(this);
		mipscode += "\n";
		n.f12.accept(this);
		n.f13.accept(this);
		
		mipscode += ".data\n";
		mipscode += ".align\t0\n";
		mipscode += "newl:\t.asciiz\t\"\\n\"\n";
		mipscode += "\n";
		mipscode += ".data\n";
		mipscode += ".align\t0\n";
		mipscode += "str_er:\t.asciiz\t\"ERROR: abnormal termination\\n\"\n";
		return _ret;
	}

	/**
	 * f0 -> ( ( Label() )? Stmt() )*
	 */
	public String visit(StmtList n) {
		String _ret = null;
		n.f0.accept(this);
		return _ret;
	}

	/**
	 * f0 -> Label() 
	 * f1 -> "[" 
	 * f2 -> IntegerLiteral() 
	 * f3 -> "]" 
	 * f4 -> "[" 
	 * f5 -> IntegerLiteral() 
	 * f6 -> "]" 
	 * f7 -> "[" 
	 * f8 -> IntegerLiteral() 
	 * f9 -> "]" 
	 * f10 -> StmtList() 
	 * f11 -> "END"
	 */
	public String visit(Procedure n) {
		String _ret = null;
		String tmp0 = n.f0.accept(this);
		mipscode += ".text\n";
		mipscode += ".globl\t" + tmp0 + "\n";
		mipscode += tmp0 + ":\n";
		n.f1.accept(this);
		n.f2.accept(this);
		n.f3.accept(this);
		n.f4.accept(this);
		stackunit = Integer.parseInt(n.f5.accept(this));
		n.f6.accept(this);
		n.f7.accept(this);
		n.f8.accept(this);
		n.f9.accept(this);
		AddString("sw $fp, -8($sp)");
		AddString("move $fp, $sp");
		AddString("subu $sp, $sp, " + (stackunit*4 + 8));
		AddString("sw $ra, -4($fp)");
		n.f10.accept(this);
		AddString("lw $ra, -4($fp)");
		AddString("addu $sp, $sp, " + (stackunit*4 + 8));
		AddString("lw $fp, -8($sp)");
		AddString("j $ra");
		n.f11.accept(this);
		mipscode += "\n";
		return _ret;
	}

	/**
	 * f0 -> NoOpStmt() 
	 * 		 | ErrorStmt() 
	 * 		 | CJumpStmt() 
	 * 		 | JumpStmt() 
	 * 		 | HStoreStmt()
	 * 		 | HLoadStmt() 
	 * 		 | MoveStmt() 
	 * 		 | PrintStmt() 
	 * 		 | ALoadStmt() 
	 * 		 | AStoreStmt() 
	 * 		 | PassArgStmt() 
	 * 		 | CallStmt()
	 */
	public String visit(Stmt n) {
		String _ret = null;
		n.f0.accept(this);
		return _ret;
	}

	/**
	 * f0 -> "NOOP"
	 */
	public String visit(NoOpStmt n) {
		String _ret = null;
		n.f0.accept(this);
		AddString("nop");
		return _ret;
	}

	/**
	 * f0 -> "ERROR"
	 */
	public String visit(ErrorStmt n) {
		String _ret = null;
		n.f0.accept(this);
		AddString("la $a0, str_er");
		AddString("li $v0, 4");
		AddString("syscall");
		AddString("li $v0, 10");
		AddString("syscall");
		return _ret;
	}

	/**
	 * f0 -> "CJUMP" 
	 * f1 -> Reg() 
	 * f2 -> Label()
	 */
	public String visit(CJumpStmt n) {
		String _ret = null;
		n.f0.accept(this);
		String tmp1 = n.f1.accept(this);
		String tmp2 = n.f2.accept(this);
		AddString("beqz " + tmp1 + ", " + tmp2);
		return _ret;
	}

	/**
	 * f0 -> "JUMP" 
	 * f1 -> Label()
	 */
	public String visit(JumpStmt n) {
		String _ret = null;
		n.f0.accept(this);
		String tmp1 = n.f1.accept(this);
		AddString("j " + tmp1);
		return _ret;
	}

	/**
	 * f0 -> "HSTORE" 
	 * f1 -> Reg() 
	 * f2 -> IntegerLiteral() 
	 * f3 -> Reg()
	 */
	public String visit(HStoreStmt n) {
		String _ret = null;
		n.f0.accept(this);
		String tmp1 = n.f1.accept(this);
		String tmp2 = n.f2.accept(this);
		String tmp3 = n.f3.accept(this);
		AddString("sw " + tmp3 + ", " + tmp2 + "(" + tmp1 + ")");
		return _ret;
	}

	/**
	 * f0 -> "HLOAD" 
	 * f1 -> Reg() 
	 * f2 -> Reg() 
	 * f3 -> IntegerLiteral()
	 */
	public String visit(HLoadStmt n) {
		String _ret = null;
		n.f0.accept(this);
		String tmp1 = n.f1.accept(this);
		String tmp2 = n.f2.accept(this);
		String tmp3 = n.f3.accept(this);
		AddString("lw " + tmp1 + ", " + tmp3 + "(" + tmp2 + ")");
		return _ret;
	}

	/**
	 * f0 -> "MOVE" 
	 * f1 -> Reg() 
	 * f2 -> Exp()
	 */
	public String visit(MoveStmt n) {
		String _ret = null;
		n.f0.accept(this);
		String tmp1 = n.f1.accept(this);
		String tmp2 = n.f2.accept(this);
		if(n.f2.f0.choice instanceof HAllocate) {
			AddString("move " + tmp1 + ", " + tmp2);
		}
		else if(n.f2.f0.choice instanceof BinOp) {
			BinOp node = (BinOp)(n.f2.f0.choice);
			String nodetmp0 = node.f0.accept(this);
			String nodetmp1 = node.f1.accept(this);
			String nodetmp2 = node.f2.accept(this);
			if(node.f2.f0.choice instanceof Reg) {
				
			}
			else if(node.f2.f0.choice instanceof IntegerLiteral) {
				if(nodetmp2.equals("$v0")) {
					AddString("li $v1, " + nodetmp2);
					nodetmp2 = "$v1";
				}
				else {
					AddString("li $v0, " + nodetmp2);
					nodetmp2 = "$v0";
				}
			}
			else {
				if(nodetmp2.equals("$v0")) {
					AddString("la $v1, " + nodetmp2);
					nodetmp2 = "$v1";
				}
				else {
					AddString("la $v0, " + nodetmp2);
					nodetmp2 = "$v0";
				}	
			}
			
			if(nodetmp0.equals("LT")) {
				AddString("slt " + tmp1 + ", " + nodetmp1 + ", " + nodetmp2);
			}
			else if(nodetmp0.equals("PLUS")) {
				AddString("add " + tmp1 + ", " + nodetmp1 + ", " + nodetmp2);
			}
			else if(nodetmp0.equals("MINUS")) {
				AddString("sub " + tmp1 + ", " + nodetmp1 + ", " + nodetmp2);
			}
			else {
				AddString("mul " + tmp1 + ", " + nodetmp1 + ", " + nodetmp2);
			}
		}
		else {
			SimpleExp node = (SimpleExp)(n.f2.f0.choice);
			if(node.f0.choice instanceof Reg) {
				AddString("move " + tmp1 + ", " + tmp2);
			}
			else if(node.f0.choice instanceof IntegerLiteral) {
				AddString("li " + tmp1 + ", " + tmp2);
			}
			else {
				AddString("la " + tmp1 + ", " + tmp2);
			}
		}
		return _ret;
	}

	/**
	 * f0 -> "PRINT" 
	 * f1 -> SimpleExp()
	 */
	public String visit(PrintStmt n) {
		String _ret = null;
		n.f0.accept(this);
		String tmp1 = n.f1.accept(this);
		if(n.f1.f0.choice instanceof Reg) {
			AddString("move $a0, " + tmp1);
		}
		else if(n.f1.f0.choice instanceof IntegerLiteral){
			AddString("li $a0, " + tmp1);
		}
		else {
			AddString("la $a0, " + tmp1);
		}
		AddString("li $v0, 1");
		AddString("syscall");
		AddString("la $a0, newl");
		AddString("li $v0, 4");
		AddString("syscall");
		return _ret;
	}

	/**
	 * f0 -> "ALOAD" 
	 * f1 -> Reg() 
	 * f2 -> SpilledArg()
	 */
	public String visit(ALoadStmt n) {
		String _ret = null;
		n.f0.accept(this);
		String tmp1 = n.f1.accept(this);
		String tmp2 = n.f2.accept(this);
		AddString("lw " + tmp1 + ", " + tmp2);
		return _ret;
	}

	/**
	 * f0 -> "ASTORE" 
	 * f1 -> SpilledArg() 
	 * f2 -> Reg()
	 */
	public String visit(AStoreStmt n) {
		String _ret = null;
		n.f0.accept(this);
		String tmp1 = n.f1.accept(this);
		String tmp2 = n.f2.accept(this);
		AddString("sw " + tmp2 + ", " + tmp1);
		return _ret;
	}

	/**
	 * f0 -> "PASSARG" 
	 * f1 -> IntegerLiteral() 
	 * f2 -> reg()
	 */
	public String visit(PassArgStmt n) {
		String _ret = null;
		n.f0.accept(this);
		int tmp1 = Integer.parseInt(n.f1.accept(this));
		String tmp2 = n.f2.accept(this);
		AddString("sw " + tmp2 + ", " + (-4*(tmp1+2)) + "($sp)");
		return _ret;
	}

	/**
	 * f0 -> "CALL" 
	 * f1 -> SimpleExp()
	 */
	public String visit(CallStmt n) {
		String _ret = null;
		n.f0.accept(this);
		String tmp1 = n.f1.accept(this);
		if(n.f1.f0.choice instanceof Reg) {
			AddString("jalr " + tmp1);
		}
		else if(n.f1.f0.choice instanceof IntegerLiteral) {
			AddString("li $v0, " + tmp1);
			AddString("jalr $v0");
		}
		else {
			AddString("jal " + tmp1);
		}
		return _ret;
	}

	/**
	 * f0 -> HAllocate() 
	 * 		 | BinOp() 
	 * 		 | SimpleExp()
	 */
	public String visit(Exp n) {
		String _ret = null;
		_ret = n.f0.accept(this);
		return _ret;
	}

	/**
	 * f0 -> "HALLOCATE" 
	 * f1 -> SimpleExp()
	 */
	public String visit(HAllocate n) {
		String _ret = null;
		n.f0.accept(this);
		String tmp1 = n.f1.accept(this);
		if(n.f1.f0.choice instanceof Reg) {
			AddString("move $a0, " + tmp1);
		}
		else if(n.f1.f0.choice instanceof IntegerLiteral) {
			AddString("li $a0, " + tmp1);
		}
		else {
			AddString("la $a0, " + tmp1);
		}
		AddString("li $v0, 9");
		AddString("syscall");
		_ret = "$v0";
		return _ret;
	}

	/**
	 * f0 -> Operator() 
	 * f1 -> Reg() 
	 * f2 -> SimpleExp()
	 */
	public String visit(BinOp n) {
		String _ret = null;
//		n.f0.accept(this);
//		n.f1.accept(this);
//		n.f2.accept(this);
		return _ret;
	}

	/**
	 * f0 -> "LT" 
	 * 		 | "PLUS" 
	 * 		 | "MINUS" 
	 * 		 | "TIMES"
	 */
	public String visit(Operator n) {
		String _ret = null;
		n.f0.accept(this);
		_ret = n.f0.choice.toString();
		return _ret;
	}

	/**
	 * f0 -> "SPILLEDARG" 
	 * f1 -> IntegerLiteral()
	 */
	public String visit(SpilledArg n) {
		String _ret = null;
		n.f0.accept(this);
		int tmp1 = Integer.parseInt(n.f1.accept(this));
		_ret = Integer.toString(4*(stackunit-1-tmp1)) + "($sp)";
		return _ret;
	}

	/**
	 * f0 -> Reg() 
	 * 		 | IntegerLiteral() 
	 * 		 | Label()
	 */
	public String visit(SimpleExp n) {
		String _ret = null;
		_ret = n.f0.accept(this);
		return _ret;
	}

	/**
	 * f0 -> "a0" 
	 * 		 | "a1" 
	 * 		 | "a2" 
	 * 		 | "a3" 
	 * 		 | "t0" 
	 * 		 | "t1" 
	 * 		 | "t2" 
	 * 		 | "t3" 
	 * 		 | "t4" 
	 * 		 | "t5"
	 * 		 | "t6" 
	 * 		 | "t7" 
	 * 		 | "s0" 
	 * 		 | "s1" 
	 * 		 | "s2" 
	 * 		 | "s3" 
	 * 		 | "s4" 
	 * 		 | "s5" 
	 * 		 | "s6" 
	 * 		 | "s7" 
	 * 		 | "t8" 
	 * 		 | "t9" 
	 * 		 | "v0" 
	 * 		 | "v1"
	 */
	public String visit(Reg n) {
		String _ret = null;
		n.f0.accept(this);
		_ret = "$" + n.f0.choice.toString();
		return _ret;
	}

	/**
	 * f0 -> <INTEGER_LITERAL>
	 */
	public String visit(IntegerLiteral n) {
		String _ret = null;
		n.f0.accept(this);
		_ret = n.f0.tokenImage;
		return _ret;
	}

	/**
	 * f0 -> <IDENTIFIEr>
	 */
	public String visit(Label n) {
		String _ret = null;
		n.f0.accept(this);
		_ret = n.f0.tokenImage;
		return _ret;
	}

}
