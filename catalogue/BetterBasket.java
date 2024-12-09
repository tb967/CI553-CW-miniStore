package catalogue;

import java.io.Serializable;
import java.util.Collections;

/**
 * Write a description of class BetterBasket here.
 * 
 * @author  Your Name 
 * @version 1.0
 */
public class BetterBasket extends Basket 
{
  //private static final long serialVersionUID = 1L;
  @Override
	public boolean add( Product pr )
	{
	  for(Product prInList: this) {
		  if(prInList.getProductNum().equals(pr.getProductNum())) {
			  int quantity = pr.getQuantity() + prInList.getQuantity();
			  prInList.setQuantity(quantity);
			  return (true);
		  }
	  }
	  super.add( pr ); // Call add in Arraylist
	  //Collections.sort(this);
	  Collections.sort(this, new SortByNum());
	  return (true);
	}
  // this is a small change in test-rebase branch, not betterBasket branch

}
