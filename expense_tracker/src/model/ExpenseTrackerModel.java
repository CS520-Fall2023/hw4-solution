package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The ExpenseTrackerModel class is the data representation for the
 * ExpenseTrackerApp containing the list of transactions.
 *
 * NOTE) The class is applying the MVC architecture pattern as well
 *       as the Observer design pattern.
 */
public class ExpenseTrackerModel {

  //encapsulation - data integrity

  /**
   * The current list of transactions
   *
   * NOTE) Each transaction must be non-null and valid.
   */
  private List<Transaction> transactions;

  /**
   * The current list of transactions that match the most recently
   * applied filter.
   *
   * NOTE) Each index must be in the range 0 to the number of transactions
   *       minus one (inclusive).
   */
  private List<Integer> matchedFilterIndices;

  /**
   * The list of registered observers.
   *
   * This class is applying the Observer design pattern.                          
   * Specifically, the class is the Observable class.
   */
  private List<ExpenseTrackerModelListener> observers;

  /**
   * Creates a new ExpenseTrackerModel.
   *
   * Post-conditions:
   * <ul>
   * <li> The list of transactions is empty. </li>
   * <li> The matched filter indices is empty. </li>
   * <li> There are no registered observers. </li>
   * </ul>
   */
  public ExpenseTrackerModel() {
    transactions = new ArrayList<Transaction>();
    matchedFilterIndices = new ArrayList<Integer>();
    observers = new ArrayList<ExpenseTrackerModelListener>();
  }

  /**
   * Adds the given non-null Transaction to the list of transactions.
   *
   * Post-condition:
   * - The list of transactions will contain the non-null Transaction.
   *
   * @param t The Transaction to be added
   * @throws IllegalArgumentExcpetion if the given Transaction is null
   */
  public void addTransaction(Transaction t) {
    // Perform input validation to guarantee that all transactions added are non-null.
    if (t == null) {
      throw new IllegalArgumentException("The new transaction must be non-null.");
    }
    transactions.add(t);
    // The previous filter is no longer valid.
    matchedFilterIndices.clear();
    
    // Since there were state changes, notify the registered listeners.
    stateChanged();
  }

  /**
   * Removes the given Transaction from the list of transactions.
   *
   * Post-condition: The list of transaction will not contain the
   *                 given transaction.
   *
   * @param t The Transaction to be removed
   */
  public void removeTransaction(Transaction t) {
    transactions.remove(t);
    // The previous filter is no longer valid.
    matchedFilterIndices.clear();

    // Since there were state changes, notify the registered listeners.
    stateChanged();
  }

  /**
   * Returns the current list of transactions.
   *
   * Post-condition: The list of transactions will be non-null.
   *
   * @return The current list of transactions
   */
  public List<Transaction> getTransactions() {
    //encapsulation - data integrity
    return Collections.unmodifiableList(new ArrayList<>(transactions));
  }

  /**
   * Sets the list of matched filter indices.
   *
   * Pre-condition:
   * <ul>
   * <li> The newMatchedFilterIndices must be non-null.</li>
   * <li> Each index must be in the range from 0 to the number of transactions
   *   minus one (inclusive).</li>
   * </ul>
   *
   * @param newMatchedFilterIndices The list of matched filter indices
   */
  public void setMatchedFilterIndices(List<Integer> newMatchedFilterIndices) {
      // Perform input validation
      if (newMatchedFilterIndices == null) {
	  throw new IllegalArgumentException("The matched filter indices list must be non-null.");
      }
      for (Integer matchedFilterIndex : newMatchedFilterIndices) {
	  if ((matchedFilterIndex < 0) || (matchedFilterIndex > this.transactions.size() - 1)) {
	      throw new IllegalArgumentException("Each matched filter index must be between 0 (inclusive) and the number of transactions (exclusive).");
	  }
      }
      // For encapsulation, copy in the input list 
      this.matchedFilterIndices.clear();
      this.matchedFilterIndices.addAll(newMatchedFilterIndices);

      // Since there were state changes, notify the registered listeners.
      stateChanged();
  }

  /**
   * Returns the current list of matched filter indices.
   *
   * Post-condition: The list of matched filter indices is non-null.
   *
   * @return The current list of matched filter indices
   */
  public List<Integer> getMatchedFilterIndices() {
      // For encapsulation, copy out the output list
      List<Integer> copyOfMatchedFilterIndices = new ArrayList<Integer>();
      copyOfMatchedFilterIndices.addAll(this.matchedFilterIndices);
      return copyOfMatchedFilterIndices;
  }

  /**
   * Registers the given ExpenseTrackerModelListener for
   * state change events.
   *
   * Post-condition:
   * - The given non-null ExpenseTrackerModelListener is contained
   *   in the list of observers.
   *
   * @param listener The ExpenseTrackerModelListener to be registered
   * @return If the listener is non-null and not already registered,
   *         returns true. If not, returns false.
   */   
  public boolean register(ExpenseTrackerModelListener listener) {
      // For the Observable class, this is one of the methods.
      if (listener == null) {
	  return false;
      }
      else if (containsListener(listener)) {
	  return false;
      }
      return observers.add(listener);
  }

  /**
   * Returns the number of registered observers
   *
   * @return The number of registered observers
   */
  public int numberOfListeners() {
      // For testing, this is one of the methods.
      //
      return observers.size();
  }

  /**
   * Returns true if the given listener is already registered (i.e contained
   * in the list of observers) and false otherwise.
   *
   * @return True if the given listener is already registered and
   *         false otherwise
   */
  public boolean containsListener(ExpenseTrackerModelListener listener) {
      // For testing, this is one of the methods.
      //
      return observers.contains(listener);
  }

  /**
   * After the Controller manipulates the Model (i.e. calls a method in the
   * Model that modifies its fields), broadcast to all of the observers
   * that the Model was modified.
   */
  protected void stateChanged() {
      // For the Observable class, this is one of the methods.
      //
      for (ExpenseTrackerModelListener observer : observers) {
	  observer.update(this);
      } // end for observer
  }
}
