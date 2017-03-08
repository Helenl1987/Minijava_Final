
package minijava.symboltable;

//to save all the information of a variable
//including name, type(using class type VariType), 
//array length(if it is of type array), if it has been initialized, if it has been used

public class Vari {
	public String vari_name;
	public VariType vari_type;
	public int beginline;
	public int begincolumn;
	public int alen = 0;
	public boolean init = false;
	public boolean use = false;
}