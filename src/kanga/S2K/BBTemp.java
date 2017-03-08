package kanga.S2K;

public class BBTemp implements Comparable<Object> {
	public int tempnum;
	public int start;
	public int end;
	public int regnum;
	public boolean ifdead = false;
	public boolean canbet = true;
	
	public int compareTo(Object arg0) {
		BBTemp tmp = (BBTemp)arg0;
		return tmp.end - end;
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof BBTemp))
			return false;
		BBTemp tmp = (BBTemp)o;
		return tmp.tempnum == tempnum;
	}
}