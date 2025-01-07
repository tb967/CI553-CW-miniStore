package clients.cashier;

import catalogue.Basket;
import catalogue.BetterBasket;
import middle.MiddleFactory;

/**
 * A specialised model for the cashier application that uses a {@link BetterBasket}
 * instead of the default {@link Basket}.
 * 
 * <p>
 * This class inherits all the functionality of {@link CashierModel} but overrides
 * the basket creation to use {@link BetterBasket}, which may include additional
 * features or optimisations.
 * </p>
 * 
 * @see CashierModel
 * @see BetterBasket
 */

public class BetterCashierModel extends CashierModel {
	
	/**
     * Constructs a new BetterCashierModel.
     * 
     * @param mf the middle factory used for creating stock and order processing objects
     */

	public BetterCashierModel(MiddleFactory mf) {
		super(mf);
		// TODO Auto-generated constructor stub
	}
	
	/**
     * Creates a new instance of {@link BetterBasket}.
     * 
     * @return a {@link BetterBasket} object for managing customer orders
     */
	
	@Override
	protected Basket makeBasket()
	  {
	    return new BetterBasket();
	  }

}
