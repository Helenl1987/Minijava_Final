package kanga.S2K;

import java.util.BitSet;

import kanga.syntaxtree.Node;

public class BBStatement implements SymbolCFG {
	public Node node;
	public BasicBlock basicblock;
	public int ind;
	public BitSet In = new BitSet();
	public BitSet Out = new BitSet();
	public BitSet Def = new BitSet();
	public BitSet Use = new BitSet();
	public String label = null;
	
	public void LiveAnalyze(BitSet bitset) {
		Out.clear();
		Out.or(bitset);
		In.clear();
		In.or(Out);
		In.andNot(Def);
		In.or(Use);
	}
}