package clients.customer;

import catalogue.Basket;
import catalogue.BetterBasket;
import clients.Picture;
import middle.MiddleFactory;
import middle.StockReader;

import java.awt.Color; 
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 */

// public class CustomerView implements Observer
public class CustomerView implements PropertyChangeListener
{
  class Name                              // Names of buttons
  {
    public static final String CHECK  = "<html>Check <br>ID</html>"; // The whole text fits in the button
    public static final String CLEAR  = "Clear";
  }

  private static final int H = 300;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels

  private final JLabel      pageTitle  = new JLabel();
  private final JLabel      theAction  = new JLabel();
  private final JTextField  theInput   = new JTextField();
  private final JTextArea   theOutput  = new JTextArea();
  private final JScrollPane theSP      = new JScrollPane();
  private final JButton     theBtCheck = new JButton( Name.CHECK );
  private final JButton     theBtCheckName = new JButton( "<html>Check<br>Name</html>" ); // The whole text fits in the button
  private final JButton     theBtClear = new JButton( Name.CLEAR );

  private CustomerModel model;
  private Picture thePicture = new Picture(80,80);
  private StockReader theStock   = null;
  private CustomerController cont= null;

  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  
  public CustomerView( RootPaneContainer rpc, MiddleFactory mf, int x, int y )
  {
    try                                             // 
    {      
      theStock  = mf.makeStockReader();             // Database Access
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }
    Container cp         = rpc.getContentPane();    // Content Pane
    Container rootWindow = (Container) rpc;         // Root Window
    cp.setLayout(null);                             // No layout manager
    rootWindow.setSize( W, H );                     // Size of Window
    rootWindow.setLocation( x, y );
    cp.setBackground(new Color(174, 198, 207)); // Pastel blue background

    Font f = new Font("Monospaced",Font.PLAIN,12);  // Font f is
    
    pageTitle.setBounds( 110, 0 , 270, 20 );       
    pageTitle.setText( "Search products by ID or by Name." );                        
    cp.add( pageTitle );

    theBtCheck.setBounds( 16, 25+60*0, 80, 40 );    // Check button
    theBtCheck.setBackground(new Color(153, 217, 234)); // Light pastel blue background
    theBtCheck.addActionListener(                   // Call back code
      e -> cont.doCheck( theInput.getText() ) );
    cp.add( theBtCheck );                           //  Add to canvas

    theBtCheckName.setBounds( 16, 25+60*1, 80, 40 );    // CheckName button
    theBtCheckName.setBackground(new Color(153, 217, 234)); // Light pastel blue background
    // theBtCheckName.setForeground(Color.RED);
    theBtCheckName.addActionListener(                   // Call back code
      e -> cont.doCheckByName( theInput.getText() ) );
    cp.add( theBtCheckName ); 
    
    theBtClear.setBounds( 16, 25+60*2, 80, 40 );    // Clear button
    theBtClear.setBackground(new Color(153, 217, 234)); // Light pastel blue background
    theBtClear.addActionListener(                   // Call back code
      e -> cont.doClear() );
    cp.add( theBtClear );                           //  Add to canvas

    theAction.setBounds( 110, 25 , 270, 20 );       // Message area
    theAction.setText( " " );                       // blank
    cp.add( theAction );                            //  Add to canvas

    theInput.setBounds( 110, 50, 270, 40 );         // Product no area
    theInput.setText("");                           // Blank
    cp.add( theInput );                             //  Add to canvas
    
    theSP.setBounds( 110, 100, 270, 160 );          // Scrolling pane
    theOutput.setText( "Your search will appear here." );
    theOutput.setFont( f );                         //  Uses font  
    cp.add( theSP );                                //  Add to canvas
    theSP.getViewport().add( theOutput );           //  In TextArea

    thePicture.setBounds( 16, 25+60*3, 80, 80 );   // Picture area
    cp.add( thePicture );                           //  Add to canvas
    thePicture.clear();
    
    rootWindow.setVisible( true );                  // Make visible);
    theInput.requestFocus();                        // Focus is here
  }

   /**
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */

  public void setController( CustomerController c )
  {
    cont = c;
  }
  
  public void setModel( CustomerModel m )
  {
    model = m;
  }

  /**
   * Update the view
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
   
	/*
	 * public void update( Observable modelC, Object arg ) { CustomerModel model =
	 * (CustomerModel) modelC; String message = (String) arg; theAction.setText(
	 * message ); ImageIcon image = model.getPicture(); // Image of product if (
	 * image == null ) { thePicture.clear(); // Clear picture } else {
	 * thePicture.set( image ); // Display picture } theOutput.setText(
	 * model.getBasket().getDetails() ); theInput.requestFocus(); // Focus is here }
	 */

@Override
public void propertyChange(PropertyChangeEvent evt) {
	// TODO Auto-generated method stub
	String proName = evt.getPropertyName();
	// String oldValue = (String) evt.getOldValue();
	String newValue = (String) evt.getNewValue();
	
	theAction.setText( newValue );
	switch(proName) {
	case "doCheck":
		ImageIcon image = model.getPicture();
		if ( image == null )
	    {
	      thePicture.clear();                  // Clear picture
	    } else {
	      thePicture.set( image );             // Display picture
	    }
	    theOutput.setText( model.getBasket().getDetails() );
	    theInput.requestFocus();               // Focus is here
		break;
		
	case "doClear":         // The clear button clears most areas
		thePicture.clear();
		theInput.setText("");
		theOutput.setText("Start a new search.");
		theAction.setText("");
		break;
	}
}

}
