
package minijava.symboltable;

import java.util.Vector;

public class MethodParam implements Table {
	public MethodTable methodtable; //to save the current argument and pass it to the child node
	public int paramnum = 0;
	public Vector<VariType> paramtype = new Vector<VariType>();
	public String paramTempList = "";
	public boolean ifparammorethan20 = false;
	public int temptemp;
	
	public void insertparamtype(VariType varitype) {
		paramtype.add(varitype);
	}

	@Override
	public boolean insertvariable(String name, Vari vari) {
		// TODO Auto-generated method stub
		return false;
	}
	
}