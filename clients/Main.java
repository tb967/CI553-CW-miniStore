package clients;

import clients.backDoor.BackDoorController;
import clients.backDoor.BackDoorModel;
import clients.backDoor.BackDoorView;
import clients.cashier.BetterCashierModel;
import clients.cashier.CashierController;
import clients.cashier.CashierModel;
import clients.cashier.CashierView;
import clients.customer.CustomerController;
import clients.customer.CustomerModel;
import clients.customer.CustomerView;
import clients.packing.PackingController;
import clients.packing.PackingModel;
import clients.packing.PackingView;
import middle.LocalMiddleFactory;
import middle.MiddleFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Starts all the clients (user interface)  as a single application.
 * Good for testing the system using a single application.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 * @author  Shine University of Brighton
 * @version year-2024
 */

class Main
{
  private static final int SPLASH_SCREEN_DURATION = 5000; // Displays welcome screen for 5 seconds
  private static final int GOODBYE_SCREEN_DURATION = 5000; // Goodbye screen duration in milliseconds
  private final List<JFrame> windows = new ArrayList<>(); // Track all windows
	
  public static void main (String args[])
  {
	new Main().showWelcomeScreen();
    // new Main().begin();
  }
  
  /**
   * Displays the welcome screen for 5 seconds, then launches the application.
   */
  public void showWelcomeScreen() {
      JFrame welcomeFrame = new JFrame("Welcome");
      welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      welcomeFrame.setSize(400, 300);
      welcomeFrame.setLocationRelativeTo(null); // Centre the frame
      welcomeFrame.setUndecorated(true); // Remove window borders

      // Add a welcome message panel
      JPanel panel = new JPanel();
      panel.setBackground(new Color(0, 0, 139)); // Dark blue background
      JLabel welcomeLabel = new JLabel("Welcome to Mini Store!", SwingConstants.CENTER);
      welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
      welcomeLabel.setForeground(Color.WHITE); // White text colour
      panel.setLayout(new BorderLayout());
      panel.add(welcomeLabel, BorderLayout.CENTER);

      welcomeFrame.add(panel);
      welcomeFrame.setVisible(true);

      // Timer to close the splash screen and start the main application
      Timer timer = new Timer(SPLASH_SCREEN_DURATION, e -> {
          welcomeFrame.dispose(); // Close the splash screen
          begin(); // Launch the main application
      });
      timer.setRepeats(false); // Ensure the timer only runs once
      timer.start();
  }

  /**
   * Starts the system (Non distributed)
   */
  public void begin()
  {
	// Set Metal Look and Feel
	try {
	    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	  
    //DEBUG.set(true); /* Lots of debug info */
    MiddleFactory mlf = new LocalMiddleFactory();  // Direct access
    startCustomerGUI_MVC( mlf );
    startCashierGUI_MVC( mlf );
    startCashierGUI_MVC( mlf ); // you can create multiple clients
    startPackingGUI_MVC( mlf );
    startBackDoorGUI_MVC( mlf );
  }
  
  /**
   * Creates a JFrame window with the specified title and sets up an exit listener.
   */
  private JFrame createWindow(String title) {
      JFrame window = new JFrame(title);
      window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      window.setSize(400, 300);
      window.setLocationRelativeTo(null);

      window.addWindowListener(new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
              closeWindowsShowGbScreen();
          }
      });

      return window;
  }

  private void closeWindowsShowGbScreen() {
      for (JFrame window : windows) {
          window.dispose();
      }
      windows.clear(); // Clear the list after disposing of windows
      showGoodbyeScreen();
  }

  /**
   * Displays the goodbye screen for 5 seconds before exiting the application.
   */
  private void showGoodbyeScreen() {
      JFrame goodbyeFrame = new JFrame("Goodbye");
      goodbyeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      goodbyeFrame.setSize(400, 300);
      goodbyeFrame.setLocationRelativeTo(null); // Centre the frame
      goodbyeFrame.setUndecorated(true); // Remove window borders

      JLabel goodbyeLabel = new JLabel("Goodbye, Thank You for Shopping at Mini Store!", SwingConstants.CENTER);
      goodbyeLabel.setFont(new Font("Arial", Font.BOLD, 16));
      goodbyeLabel.setForeground(Color.WHITE); // White text colour
      goodbyeFrame.getContentPane().setBackground(new Color(0, 0, 139)); // Dark blue background
      goodbyeFrame.add(goodbyeLabel);

      goodbyeFrame.setVisible(true);

      // Timer to close the goodbye screen and terminate the application
      Timer timer = new Timer(GOODBYE_SCREEN_DURATION, e -> System.exit(0));
      timer.setRepeats(false);
      timer.start();
  }
  
  /**
  * start the Customer client, -search product
  * @param mlf A factory to create objects to access the stock list
  */
  public void startCustomerGUI_MVC(MiddleFactory mlf )
  {
	JFrame window = createWindow("Customer Client MVC");
	windows.add(window); // Add to list
	Dimension pos = PosOnScrn.getPos();
    
    CustomerModel model      = new CustomerModel(mlf);
    CustomerView view        = new CustomerView( window, mlf, pos.width, pos.height );
    CustomerController cont  = new CustomerController( model, view );
    view.setController( cont );
    view.setModel( model );

    // model.addObserver( view );       // Add observer to the model, ---view is observer, model is Observable
    model.addListener(view);    
    window.setVisible(true);         // start Screen
  }

  /**
   * start the cashier client - customer check stock, buy product
   * @param mlf A factory to create objects to access the stock list
   */
  public void startCashierGUI_MVC(MiddleFactory mlf )
  {
    JFrame window = createWindow("Cashier Client MVC");
    windows.add(window); // Add to list
    Dimension pos = PosOnScrn.getPos();
    
    BetterCashierModel model      = new BetterCashierModel(mlf);
    CashierView view        = new CashierView( window, mlf, pos.width, pos.height );
    CashierController cont  = new CashierController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model
    window.setVisible(true);         // Make window visible
    model.askForUpdate();            // Initial display
  }

  /**
   * start the Packing client - for warehouse staff to pack the bought order for customer, one order at a time
   * @param mlf A factory to create objects to access the stock list
   */
  
  public void startPackingGUI_MVC(MiddleFactory mlf)
  {
	JFrame window = createWindow("Packing Client MVC");
	windows.add(window); // Add to list
	Dimension pos = PosOnScrn.getPos();
    
    PackingModel model      = new PackingModel(mlf);
    PackingView view        = new PackingView( window, mlf, pos.width, pos.height );
    PackingController cont  = new PackingController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model
    window.setVisible(true);         // Make window visible
  }
  
  /**
   * start the BackDoor client - store staff to check and update stock
   * @param mlf A factory to create objects to access the stock list
   */
  public void startBackDoorGUI_MVC(MiddleFactory mlf )
  {
    JFrame window = createWindow("BackDoor Client MVC");
    windows.add(window); // Add to list
    Dimension pos = PosOnScrn.getPos();
    
    BackDoorModel model      = new BackDoorModel(mlf);
    BackDoorView view        = new BackDoorView( window, mlf, pos.width, pos.height );
    BackDoorController cont  = new BackDoorController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model
    window.setVisible(true);         // Make window visible
  }
 
}
