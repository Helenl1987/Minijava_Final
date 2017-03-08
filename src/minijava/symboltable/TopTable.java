
package minijava.symboltable;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import minijava.typecheck.PrintError;

//symboltable is divided into three levels, this is the first level
//to save all classes and their address, using HashMap

public class TopTable implements Table {
	public String mainclass_name;
	public HashMap<String, ClassTable> top = new HashMap<String, ClassTable>();
	public boolean insertclass(String class_name, ClassTable classtable) {
		if(top.containsKey(class_name)) {
			return false;
		}
		else {
			top.put(class_name, classtable);
			return true;
		}
	}
	public ClassTable getclasstable(String class_name) {
		return top.get(class_name);
	}
	
	//check if any child class wants to override the function in its parent class
	//if true, the two function must have the same return type, the same parameter number, each parameter must have the same type
	public void override() {
		Set<Entry<String, ClassTable>> sets = top.entrySet();
		for(Entry<String, ClassTable> entry : sets) {
			entry.getValue().override();
		}
	}
	
	//check if there is an extend loop, using the topological sort algorithm
	public void loopextend() {
		int len = top.size();
		int graph[][] = new int[len][len];
		int deg[] = new int[len];
		for(int i = 0; i < len; i++) {
			for(int j = 0; j < len; j++) {
				graph[i][j] = 0;
			}
			deg[i] = 0;
		}
		HashMap<String, Integer> classtab = new HashMap<String, Integer>();
		int ind = 0;
		Set<Entry<String, ClassTable>> sets = top.entrySet();
		for(Entry<String, ClassTable> entry : sets) {
			classtab.put(entry.getKey(), ind);
			ind++;
		}
		for(Entry<String, ClassTable> entry2 : sets) {
			ClassTable cl = entry2.getValue();
			String pname = cl.parentclass_name;
			if(pname == null)
				continue;
			int p = classtab.get(cl.class_name);
			int q = classtab.get(pname);
			graph[p][q] = 1;
			deg[q]++;
		}
		boolean flag;
		while(true) {
			flag = false;
			for(int i = 0; i < len; i++) {
				if(deg[i] == 0) {
					deg[i] = -1;
					flag = true;
					for(int j = 0; j < len; j++) {
						if(graph[i][j] == 1) {
							graph[i][j] = 0;
							deg[j]--;
						}
					}
				}
			}
			if(flag == false)
				break;
		}
		flag = false;
		for(int i = 0; i < len; i++) {
			if(deg[i] > 0) {
				flag = true;
				break;
			}
		}
		if(flag) {
	    	PrintError.errorexist = true;
			PrintError.loopflag = true;
			String emsg = "cyclic inheritance exists";
			PrintError.print(emsg, 1);
		}
	}
	
	//check if there is any class undefined by going through all the declaration part, or to say, all the identifiers in the symbol table
	public void classundefine() {
		ClassTable classtable;
		Set<Entry<String, ClassTable>> sets = top.entrySet();
		for(Entry<String, ClassTable> entry : sets) {
			classtable = entry.getValue();
			classtable.classundefine();
		}
	}
	
	//check if there is any variables unused by going through the declaration part, or to say, all the identifiers in the symbol table
	public void isunused() {
		ClassTable classtable;
		Set<Entry<String, ClassTable>> sets = top.entrySet();
		for(Entry<String, ClassTable> entry : sets) {
			classtable = entry.getValue();
			classtable.isunused();
		}
	}
	
	@Override
	public boolean insertvariable(String name, Vari vari) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void GetTotal() {
		ClassTable classtable;
		Set<Entry<String, ClassTable>> sets = top.entrySet();
		for(Entry<String, ClassTable> entry : sets) {
			classtable = entry.getValue();
			classtable.GetTotal();
		}
	}
}