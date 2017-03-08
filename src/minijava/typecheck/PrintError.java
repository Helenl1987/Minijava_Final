
package minijava.typecheck;

//a class used for output
//errornum indicates the current error numbers
//warningnum indicates the current warning numbers
//level indicates the requested output level:
//level-0 only results: Type error or Program type checked successfully
//level-1 results + error message
//level-2 results + error message + warning message
//loopflag indicates if there is an extend loop
//undefineflag indicates if there is an undefined class
//errorexist indicates whether an error exists in the test program

public class PrintError {
	public static int errornum = 0;
	public static int warningnum = 0;
	public static int level = 1;	
	public static boolean loopflag = false;
	public static boolean undefineflag = false;
	public static boolean errorexist = false;
	public static void print(String emsg, int beginline, int begincolumn, int lev) {
		if(lev <= level && lev == 1) {
			errornum++;
			System.out.println("error "+errornum+". line "+beginline+" column "+begincolumn+": "+emsg);			
		}
		if(lev <= level && lev == 2) {
			warningnum++;
			System.out.println("warning "+warningnum+". line "+beginline+" column "+begincolumn+": "+emsg);			
		}
	}
	
	public static void print(String emsg, int lev) {
		if(lev <= level && lev == 1) {
			errornum++;
			System.out.println("error "+errornum+". "+emsg);			
		}
	}
	
	public static void print(int lev) {
		if(lev <= level) {
			if(errorexist)
				System.out.println("Type error");
			else
				System.out.println("Program type checked successfully");
		}
	}
}