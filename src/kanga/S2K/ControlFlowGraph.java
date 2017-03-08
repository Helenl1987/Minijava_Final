package kanga.S2K;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import kanga.syntaxtree.Call;
import kanga.syntaxtree.JumpStmt;
import kanga.syntaxtree.MoveStmt;
import kanga.syntaxtree.Node;


public class ControlFlowGraph implements SymbolCFG {
	public Vector<BasicBlock> bbset = new Vector<BasicBlock>();
	public Vector<BBStatement> bbstate = new Vector<BBStatement>();
	public HashMap<String, BasicBlock> startlabel2bb = new HashMap<String, BasicBlock>();
	public AllCFG allcfg;
	public String methodname = null;
	public int paramnum = 0;
	public int stackunit = 0;
	public int maxproparam = 0;
	public HashMap<String, String> oldL2newL = new HashMap<String, String>();
	
	public HashMap<Integer, BBTemp> temptable = new HashMap<Integer, BBTemp>();
	public HashMap<Integer, BBTemp> argtemptable = new HashMap<Integer, BBTemp>();
	public Vector<Integer> callstmt = new Vector<Integer>();
	
	public void CreateCFG() {
		int len = bbset.size();
		BasicBlock bb = null;
		BasicBlock nextbb = null;
		for(int i = 0; i < len; i++) {
			bb = bbset.get(i);
			//delete empty block
			if(bb.bbs.isEmpty()) {
				bbset.removeElement(bb);
				len = bbset.size();
				continue;
			}
//			if(i == 0) {
//				for(int x = 0; x < paramnum; x++) {
//					bb.Use.set(x);
//					bb.bbs.firstElement().Use.set(x);
//				}
//			}
			Node lastnode = bb.bbs.lastElement().node;
			if(!(lastnode instanceof JumpStmt) && i < len-1) {
				nextbb = bbset.get(i+1);
				bb.successor.add(nextbb);
				nextbb.predecessor.add(bb);
			}
			if(bb.endlabel != null) {
				nextbb = startlabel2bb.get(bb.endlabel);
				bb.successor.add(nextbb);
				nextbb.predecessor.add(bb);
			}
		}
		LiveVarAnalyze();
	}
	
	public void LiveVarAnalyze() {
		HashSet<BasicBlock> changeset = new HashSet<BasicBlock>();
		changeset.addAll(bbset);
		while(!changeset.isEmpty()) {
			BasicBlock block = changeset.iterator().next();
			changeset.remove(block);
			if(block.LiveAnalyze() == false) {
				for(BasicBlock pre: block.predecessor) {
					changeset.add(pre);
				}
			}
		}
		for(BasicBlock bb: bbset) {
			bb.LiveAnalyzeStatement();
		}
	}
	
	public void InitTemp() {
		bbstate.clear();
		int statenum = 0;
		for(BasicBlock bb: bbset) {
			if(bb.startlabel != null) {
				bb.bbs.firstElement().label = bb.startlabel;
			}
			for(BBStatement bbs: bb.bbs) {
				bbs.ind = statenum;
				bbstate.add(statenum, bbs);
				if(bbs.node instanceof MoveStmt) {
					if(((MoveStmt)bbs.node).f2.f0.choice instanceof Call) {
						callstmt.addElement(statenum);
					}
				}
				statenum++;
				for(int x = bbs.Use.nextSetBit(0); x >= 0; x = bbs.Use.nextSetBit(x+1)) {
					if(!temptable.containsKey(x)) {
						BBTemp bbtmp = new BBTemp();
						temptable.put(x, bbtmp);
						bbtmp.tempnum = x;
					}
				}
//				for(int x = bbs.Def.nextSetBit(0); x >= 0; x = bbs.Def.nextSetBit(x+1)) {
//					if(!temptable.containsKey(x)) {
//						BBTemp bbtmp = new BBTemp();
//						temptable.put(x, bbtmp);
//						bbtmp.tempnum = x;
//					}
//				}
			}
		}
		BBStatement bbs = bbstate.firstElement();
		for(Integer x: temptable.keySet()) {
			BBTemp tmp = temptable.get(x);
			if(tmp.tempnum < 20) {
				bbs.In.set(tmp.tempnum);
			}
		}
		for(int x = bbs.In.nextSetBit(0); x >= 0; x = bbs.In.nextSetBit(x+1)) {
			if(temptable.containsKey(x)) {
				argtemptable.put(x, temptable.get(x));
			}
		}
		HashMap<Integer, BBTemp> tempStart = (HashMap<Integer, BBTemp>)temptable.clone();
		HashMap<Integer, BBTemp> tempEnd = (HashMap<Integer, BBTemp>)temptable.clone();
		for(int i = 0; i < statenum; i++) {
			bbs = bbstate.get(i);
			for(int x = bbs.In.nextSetBit(0); x >= 0; x = bbs.In.nextSetBit(x+1)) {
				if(tempStart.containsKey(x)) {
					temptable.get(x).start = i;
					tempStart.remove(x);
				}
			}
			for(int x = bbs.Out.nextSetBit(0); x >= 0; x = bbs.Out.nextSetBit(x+1)) {
				if(tempStart.containsKey(x)) {
					temptable.get(x).start = i;
					tempStart.remove(x);
				}
			}
		}
		for(int i = statenum - 1; i >= 0; i--) {
			bbs = bbstate.get(i);
			for(int x = bbs.In.nextSetBit(0); x >= 0; x = bbs.In.nextSetBit(x+1)) {
				if(tempEnd.containsKey(x)) {
					temptable.get(x).end = i;
					tempEnd.remove(x);
				}
			}
			for(int x = bbs.Out.nextSetBit(0); x >= 0; x = bbs.Out.nextSetBit(x+1)) {
				if(tempEnd.containsKey(x)) {
					temptable.get(x).end = i;
					tempEnd.remove(x);
				}
			}
		}
		for(Integer x: temptable.keySet()) {
			BBTemp bbtmp = temptable.get(x);
			for(Integer callline: callstmt) {
				if(callline > bbtmp.start && callline < bbtmp.end) {
					bbtmp.canbet = false;
					break;
				}
			}
		}
	}
	
}








