package kanga.S2K;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;


public class AllCFG implements SymbolCFG {
	public HashMap<String, ControlFlowGraph> cfgs = new HashMap<String, ControlFlowGraph>();
	public Vector<String> cfgsVec = new Vector<String>();
//	public HashMap<String, String> oldL2newL = new HashMap<String, String>();
	
	public void ReLabel() {
		ControlFlowGraph cfg = null;
		String oldlabel = null;
		String newlabel = null;
		int cnt = 0;
		for(Entry<String, ControlFlowGraph> entry : cfgs.entrySet()) {
			cfg = entry.getValue();
			for(Entry<String, BasicBlock> entry2 : cfg.startlabel2bb.entrySet()) {
				oldlabel = entry2.getKey();
				newlabel = "L" + cnt;
				cnt++;
				cfg.oldL2newL.put(oldlabel, newlabel);
			}
		}
	}
}