
package minijava.symboltable;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import minijava.typecheck.PrintError;

//the symbol table is divided into three levels, this is the second level
//to save the specific information of a class, including its name, parent class name, 
//all methods in this class and their address(using HashMap), 
//all member variables in this class and their address(using HashMap)

public class ClassTable implements Table {
	public TopTable toptable;
	public String class_name;
	public int beginline;
	public int begincolumn;
	public String parentclass_name = null;
//	public ClassTable parenttable;
	public HashMap<String, MethodTable> member_method = new HashMap<String, MethodTable>();
	public Vector<MethodTable> member_method_V = new Vector<MethodTable>();
	public HashMap<String, Vari> member_variable = new HashMap<String, Vari>();
	public Vector<Vari> member_variable_V = new Vector<Vari>();
	
	public Vector<String> total_member_method = new Vector<String>();
	public int total_member_method_num = 0;
	public int total_member_variable_num = 0;
	public boolean ifgettotal = false;
	
	public boolean insertmethod(String method_name, MethodTable methodtable) {
		if(member_method.containsKey(method_name)) {
			return false;
		}
		else {
			member_method.put(method_name, methodtable);
			member_method_V.addElement(methodtable);
			return true;
		}
	}
	public boolean insertvariable(String vari_name, Vari vari) {
		if(member_variable.containsKey(vari_name)) {
			return false;
		}
		else {
			member_variable.put(vari_name, vari);
			member_variable_V.addElement(vari);
			return true;
		}
	}
	
	//child class may use the function in its parent class, so a loop is needed to find the address of a method until reaching a class which doesn't have parent class
	public MethodTable getmethodtable(String method_name) {
		if(member_method.containsKey(method_name)) {
			return member_method.get(method_name);			
		}
		ClassTable cur = this;
		while(true) {
			if(cur.parentclass_name == null)
				break;
			cur = cur.toptable.getclasstable(cur.parentclass_name);
			if(cur.member_method.containsKey(method_name))
				return cur.member_method.get(method_name);
		}
		return null;
	}
	
	//the same as method, child class may use the member variables in its parent class, so a loop is needed to find the address of a member variable until reaching a class which doesn't have parent class
	public Vari getvari(String vari_name) {
		if(member_variable.containsKey(vari_name)) {
			return member_variable.get(vari_name);			
		}
		ClassTable cur = this;
		while(true) {
			if(cur.parentclass_name == null)
				break;
			cur = cur.toptable.getclasstable(cur.parentclass_name);
			if(cur.member_variable.containsKey(vari_name))
				return cur.member_variable.get(vari_name);
		}
		return null;
	}
	
	//go through all the methods of this class, use a loop to check if it can override any method in its predecessor's method
	public void override() {
		MethodTable mt, pmt;
		ClassTable cur;
		Set<Entry<String, MethodTable>> sets = member_method.entrySet();
		for(Entry<String, MethodTable> entry : sets) {
			mt = entry.getValue();
			cur = this;
			while(true) {
				if(cur.parentclass_name == null)
					break;
				cur = cur.toptable.getclasstable(cur.parentclass_name);
				Set<Entry<String, MethodTable>> psets = cur.member_method.entrySet();
				for(Entry<String, MethodTable> pentry : psets) {
					pmt = pentry.getValue();
					if(mt.method_name == pmt.method_name) {
						mt.override(pmt);
					}
				}
			}
		}
	}
	
	//check all identifiers among the member variables and methods in this class if they match any class name in toptable, also call the same function of its method's
	public void classundefine() {
		Vari vari;
		Set<Entry<String, Vari>> set1 = member_variable.entrySet();
		for(Entry<String, Vari> entry1 : set1) {
			vari = entry1.getValue();
			if(vari.vari_type.type == "Object" && toptable.getclasstable(vari.vari_type.name) == null) {
		    	PrintError.errorexist = true;
				PrintError.undefineflag = true;
				String emsg = "type \"" + vari.vari_type.name + "\" has an undefined class type";
				PrintError.print(emsg, vari.beginline, vari.begincolumn, 1);
			}
		}
		
		if(parentclass_name != null && toptable.getclasstable(parentclass_name) == null) {
	    	PrintError.errorexist = true;
			PrintError.undefineflag = true;
			String emsg = "parentclass \"" + parentclass_name + "\" is an undefined class type";
			PrintError.print(emsg, beginline, begincolumn, 1);
		}
		
		MethodTable methodtable;
		Set<Entry<String, MethodTable>> sets = member_method.entrySet();
		for(Entry<String, MethodTable> entry : sets) {
			methodtable = entry.getValue();
			methodtable.classundefine();
		}
	}
	
	//member variables need not to do this check, so just call the same function of its method's
	public void isunused() {
		MethodTable methodtable;
		Set<Entry<String, MethodTable>> sets = member_method.entrySet();
		for(Entry<String, MethodTable> entry : sets) {
			methodtable = entry.getValue();
			methodtable.isunused();
		}
	}
	
	//完善类的方法表，将所有祖辈的函数都加进来，若有override，则修改所有祖辈的函数。同时将函数名改为"类名_函数名"的形式。
	//同时计算继承祖辈的所有函数和变量后，该类的总函数数量和变量数量
	public void GetTotal() {
		if(this.ifgettotal)
			return;
		if(this.parentclass_name != null) {
			ClassTable parent = toptable.getclasstable(parentclass_name);
			parent.GetTotal();
			this.total_member_method = parent.total_member_method;
			this.total_member_method_num = parent.total_member_method_num;
			this.total_member_variable_num = parent.total_member_variable_num;
		}
		for(int i = 0; i < member_method_V.size(); i++) {
			for(int j = 0; j < this.total_member_method.size(); j++) {
				if(this.total_member_method.get(j).endsWith("_" + this.member_method_V.get(i).method_name)) {
					this.total_member_method.set(j, this.class_name + "_" + this.member_method_V.get(i).method_name);
				}
			}
			this.total_member_method.addElement(this.class_name + "_" + this.member_method_V.get(i).method_name);
		}
		this.total_member_method_num  += this.member_method_V.size();
		this.total_member_variable_num += this.member_variable_V.size();
		this.ifgettotal = true;
	}
	
	//通过函数名找到函数在类的函数表中的位置，返回index值
	public int GetMethodNum(String methodname) {
		for(int i = 0; i < this.total_member_method.size(); i++) {
			if(this.total_member_method.get(i).endsWith("_"+methodname))
				return i;
		}
		return -1;
	}
	
	//通过变量名找到变量在类的变量表中的位置，返回index的值
	public int GetVariNum(String variname) {
		int parentclassvarinum = 0;
		if(this.parentclass_name != null) {
			parentclassvarinum = toptable.getclasstable(parentclass_name).total_member_variable_num;
		}
		for(int i = 0; i < this.member_variable_V.size(); i++) {
			if(this.member_variable_V.get(i).vari_name == variname)
				return parentclassvarinum+i;
		}
		if(this.parentclass_name != null) {
			return toptable.getclasstable(parentclass_name).GetVariNum(variname);
		}
		return -1;
	}
	
}






