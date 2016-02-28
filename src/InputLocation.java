
public class InputLocation<L,R> {

	  public L left;
	  public R right;

	  public InputLocation(L left, R right) {
	    this.left = left;
	    this.right = right;
	  }
	  
	  public InputLocation(InputLocation<L,R> anotherLocation)
	  {
		  this.left = anotherLocation.left;
		  this.right = anotherLocation.right;
	  }
	  
	  
	  public void changeInput(L left, R right)
	  {
		  this.left= left;
		  this.right = right;
	  }
	  
	  public boolean compareLocation(InputLocation<L, R> anotherLocation)
	  {
		  if (anotherLocation.getLeft() !=  this.left || anotherLocation.getRight() != this.right)
			  return false;
		  return true;
	  }
	  
	  public L getLeft() { return left; }
	  public R getRight() { return right; }
	  
	  public String toString()
	  {
		  return "Layer number " + String.valueOf(this.left) + ", input number " + String.valueOf(this.right);
	  }

	  @Override
	  public int hashCode() { return left.hashCode() ^ right.hashCode(); }

	  @Override
	  public boolean equals(Object o) {
	    if (!(o instanceof InputLocation)) return false;
	    InputLocation pairo = (InputLocation) o;
	    return this.left.equals(pairo.getLeft()) &&
	           this.right.equals(pairo.getRight());
	  }

	}