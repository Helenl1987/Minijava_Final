package kanga.S2K;

import java.util.BitSet;
import java.util.Vector;

public class BasicBlock implements SymbolCFG {
	public Vector<BBStatement> bbs = new Vector<BBStatement>();
	public Vector<BasicBlock> predecessor = new Vector<BasicBlock>();
	public Vector<BasicBlock> successor = new Vector<BasicBlock>();
	public ControlFlowGraph cfg;
	public String startlabel = null;
	public String endlabel = null;
	public BitSet In = new BitSet();
	public BitSet Out = new BitSet();
	public BitSet Def = new BitSet();
	public BitSet Use = new BitSet();
	
	BasicBlock() {
		In.clear();
		Out.clear();
		Def.clear();
		Use.clear();
	}
	
	public boolean LiveAnalyze() {
		Out.clear();
		for(BasicBlock nextblock: successor) {
			Out.or(nextblock.In);
		}
		BitSet oldIn = (BitSet)In.clone();
		In.clear();
		In.or(Out);
		In.andNot(Def);
		In.or(Use);
		return In.equals(oldIn);
	}
	
	public void LiveAnalyzeStatement() {
		bbs.lastElement().LiveAnalyze(Out);
		for(int i = bbs.size()-2; i >= 0; i--) {
			bbs.get(i).LiveAnalyze(bbs.get(i+1).In);
		}
	}
	
}