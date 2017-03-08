//
// Generated by JTB 1.3.2
//

package mips.syntaxtree;

/**
 * Grammar production:
 * f0 -> "LT"
 *       | "PLUS"
 *       | "MINUS"
 *       | "TIMES"
 */
public class Operator implements Node {
   public NodeChoice f0;

   public Operator(NodeChoice n0) {
      f0 = n0;
   }

   public void accept(mips.visitor.Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(mips.visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(mips.visitor.GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(mips.visitor.GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
}

