
package minijava.symboltable;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import minijava.typecheck.PrintError;

//the symbol table is divided into three levels, this is the third level
//to save all the information of a specific method
//including its name, which class it is in, its parameter numbers, 
//all parameters and their address(using Vector), parameter type(using Vector), return type, 
//all local variables in this method and their address(using HashMap)(parameters are also in this map)

public class MethodTable implements Table {
	public TopTable toptable;
	public ClassTable classtable;
	public String method_name;
	public int beginline;
	public int begincolumn;
	public String methodinclass_name;
	public int paramnum = 0;
	public Vector<Vari> param = new Vector<Vari>();
	public Vector<VariType> paramtype = new Vector<VariType>();
	public VariType returntype;
	public HashMap<String, Vari> local_variable = new HashMap<String, Vari>();
	public Vector<Vari> local_variable_V = new Vector<Vari>();
	
	public int templ = 0; //used when parameters of a method were asking for TEMPs
	public int tempb = 20; //used when local variables of a method were asking for TEMPs
//	public boolean startparam = false; //to determine whether to use templ or tempb when adding new TEMPs
	
//	public boolean left; 
//	//used in **visitor to determine whether the statement/expression is in the left side of the equals sign.
//	//that is, whether we are define or use it in the program.
//	//for warning check
	
	public boolean insertvariable(String vari_name, Vari vari) {
		if(local_variable.containsKey(vari_name)) {
			return false;
		}
		else {
			local_variable.put(vari_name, vari);
			local_variable_V.addElement(vari);
			return true;
		}
	}
	public boolean insertparam(String param_name, Vari vari) {
		if(local_variable.containsKey(param_name))
			return false;
		local_variable.put(param_name, vari);
		param.add(vari);
		paramtype.add(vari.vari_type);
		return true;
	}
	public Vari getvari(String vari_name) {
		return local_variable.get(vari_name);
	}
	
	//check if the return type, the parameter number, each parameter type of two methods are matched
	public void override(MethodTable pmt) {
		if(VariType.isTheSameType(toptable, pmt.returntype, returntype) == false) {
	    	PrintError.errorexist = true;
	    	String emsg="function override, return value does not match return type: return value is type "+returntype.type+", expected return type is "+pmt.returntype.type;
			PrintError.print(emsg, beginline, begincolumn, 1);
		}
		else if(this.paramnum != pmt.paramnum) {
	    	PrintError.errorexist = true;
			String emsg = "function override, number of function parameters does not match, expect "+pmt.paramnum+" parameters";
			PrintError.print(emsg, beginline, begincolumn, 1);
		}
		else {
			VariType t1, t2;
			for(int i = 0; i < this.paramnum; i++) {
				t1 = pmt.paramtype.get(i);
				t2 = this.paramtype.get(i);
				if(VariType.isTheSameType(toptable, t1, t2) == false) {
			    	PrintError.errorexist = true;
					String emsg = "function override, type of function parameters does not match";
					PrintError.print(emsg, beginline, begincolumn, 1);
				}
			}
		}
	}
	
	//check if all identifiers in this method can match a class in toptable
	public void classundefine() {
		if(returntype.type == "Object" && toptable.getclasstable(returntype.name) == null) {
	    	PrintError.errorexist = true;
			PrintError.undefineflag = true;
			String emsg = "return type of the method has an undefined class type";
			PrintError.print(emsg, beginline, begincolumn, 1); //TODO
		}
		
		Vari vari;
		Set<Entry<String, Vari>> set = local_variable.entrySet();
		for(Entry<String, Vari> entry : set) {
			vari = entry.getValue();
			if(vari.vari_type.type == "Object" && toptable.getclasstable(vari.vari_type.name) == null) {
				if(param.contains(vari)) {
			    	PrintError.errorexist = true;
					PrintError.undefineflag = true;
					String emsg = "parameter \"" + vari.vari_type.name + "\" of the method has an undefined class type";
					PrintError.print(emsg, vari.beginline, vari.begincolumn, 1);					
				}
				else {
			    	PrintError.errorexist = true;
					PrintError.undefineflag = true;
					String emsg = "type \"" + vari.vari_type.name + "\" is an undefined class type";
					PrintError.print(emsg, vari.beginline, vari.begincolumn, 1);					
				}
			}
		}
	}
	
	//check if all identifiers in this method are used
	public void isunused() {
		Vari vari;
		Set<Entry<String, Vari>> sets = local_variable.entrySet();
		for(Entry<String, Vari> entry : sets) {
			vari = entry.getValue();
			if(param.contains(vari) == false && vari.use == false) {
				String emsg = "the value of the local variable \"" + vari.vari_name + "\" is not used";
				PrintError.print(emsg, vari.beginline, vari.begincolumn, 2);
			}
		}
	}
	
	//methodtable的TEMP 第0个位置放类地址，第1到19放参数，第20开始放本地变量，后面供程序使用
//	public int GetVariNum(String variname) {
//		if(this.local_variable.containsKey(variname)) {
//			for(int i = 0; i < this.local_variable_V.size(); i++) {
//				if(this.local_variable_V.get(i).vari_name == variname)
//					return 20+i;
//			}
//			for(int i = 0; i < this.param.size(); i++) {
//				if(this.param.get(i).vari_name == variname)
//					return 1+i;
//			}
//		}
//		return -1;
//	}
	
	//methodtable的TEMP 第0个位置放类地址，第1到19放参数，第20开始放本地变量，后面供程序使用
	public int GetVariNumLocal(String variname) {
		if(this.local_variable.containsKey(variname)) {
			for(int i = 0; i < this.local_variable_V.size(); i++) {
				if(this.local_variable_V.get(i).vari_name == variname)
					return 20+i;
			}
		}
		return -1;
	}
	
	public int GetVariNumParam(String variname) {
		if(this.local_variable.containsKey(variname)) {
			for(int i = 0; i < this.param.size(); i++) {
				if(this.param.get(i).vari_name == variname)
					return 1+i;
			}
		}
		return -1;
	}
	
}