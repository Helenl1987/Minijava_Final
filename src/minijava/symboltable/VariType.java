
package minijava.symboltable;

//to save information of a variable type
//including type, name(if its type is Object, save class name), 
//if it is a integer literal, value of integer literal(if it is a integer literal)

public class VariType {
	public String type;
	public String name;
	public boolean ifintegerliteral = false;
	public int integerliteral;
	public int tempnum;
	
	public VariType() {}
	public VariType(String vtype) {
		type = vtype;
	}
	public VariType(String vname, String vtype) {
		type = vtype;
		name = vname;
	}
	public VariType(String vname, String vtype, int vtempnum) {
		type = vtype;
		name = vname;
		tempnum = vtempnum;
	}
	
	//check if two varitype are of the same type
	//boolean, integer, array type must be exactly the same
	//object type can be assigned to its predecessors' object type, so a recursion is needed
	public static boolean isTheSameType(TopTable toptable, VariType varitype1, VariType varitype2) {
		if(varitype1.type == "Boolean" || varitype1.type == "Integer" || varitype1.type == "Array") {
			return varitype1.type == varitype2.type;
		}
		if(varitype1.type == "Object") {
			if(varitype2.type != "Object")
				return false;
			else if(varitype1.name == varitype2.name)
				return true;
			ClassTable classtable = toptable.getclasstable(varitype2.name);
			if(classtable == null)
				return false;
			return isTheSameType(toptable, varitype1, new VariType(classtable.parentclass_name, "Object"));
		}
		return false;
	}
}