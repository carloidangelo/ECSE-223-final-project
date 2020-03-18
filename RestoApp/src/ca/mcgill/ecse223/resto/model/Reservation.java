/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse223.resto.model;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

// line 15 "../../../../../RestoAppPersistence.ump"
// line 34 "../../../../../RestoApp v3.ump"
public class Reservation implements Serializable
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextReservationNumber = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Reservation Attributes
  private Date date;
  private Time time;
  private int numberInParty;
  private String contactName;
  private String contactEmailAddress;
  private String contactPhoneNumber;

  //Autounique Attributes
  private int reservationNumber;

  //Reservation Associations
  private List<Table> tables;
  private RestoApp restoApp;
  private List<HighChair> highChairs;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Reservation(Date aDate, Time aTime, int aNumberInParty, String aContactName, String aContactEmailAddress, String aContactPhoneNumber, RestoApp aRestoApp, Table... allTables)
  {
    date = aDate;
    time = aTime;
    numberInParty = aNumberInParty;
    contactName = aContactName;
    contactEmailAddress = aContactEmailAddress;
    contactPhoneNumber = aContactPhoneNumber;
    reservationNumber = nextReservationNumber++;
    tables = new ArrayList<Table>();
    boolean didAddTables = setTables(allTables);
    if (!didAddTables)
    {
      throw new RuntimeException("Unable to create Reservation, must have at least 1 tables");
    }
    boolean didAddRestoApp = setRestoApp(aRestoApp);
    if (!didAddRestoApp)
    {
      throw new RuntimeException("Unable to create reservation due to restoApp");
    }
    highChairs = new ArrayList<HighChair>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setTime(Time aTime)
  {
    boolean wasSet = false;
    time = aTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumberInParty(int aNumberInParty)
  {
    boolean wasSet = false;
    numberInParty = aNumberInParty;
    wasSet = true;
    return wasSet;
  }

  public boolean setContactName(String aContactName)
  {
    boolean wasSet = false;
    contactName = aContactName;
    wasSet = true;
    return wasSet;
  }

  public boolean setContactEmailAddress(String aContactEmailAddress)
  {
    boolean wasSet = false;
    contactEmailAddress = aContactEmailAddress;
    wasSet = true;
    return wasSet;
  }

  public boolean setContactPhoneNumber(String aContactPhoneNumber)
  {
    boolean wasSet = false;
    contactPhoneNumber = aContactPhoneNumber;
    wasSet = true;
    return wasSet;
  }

  public Date getDate()
  {
    return date;
  }

  public Time getTime()
  {
    return time;
  }

  public int getNumberInParty()
  {
    return numberInParty;
  }

  public String getContactName()
  {
    return contactName;
  }

  public String getContactEmailAddress()
  {
    return contactEmailAddress;
  }

  public String getContactPhoneNumber()
  {
    return contactPhoneNumber;
  }

  public int getReservationNumber()
  {
    return reservationNumber;
  }

  public Table getTable(int index)
  {
    Table aTable = tables.get(index);
    return aTable;
  }

  /**
   * only from currentTables
   */
  public List<Table> getTables()
  {
    List<Table> newTables = Collections.unmodifiableList(tables);
    return newTables;
  }

  public int numberOfTables()
  {
    int number = tables.size();
    return number;
  }

  public boolean hasTables()
  {
    boolean has = tables.size() > 0;
    return has;
  }

  public int indexOfTable(Table aTable)
  {
    int index = tables.indexOf(aTable);
    return index;
  }

  public RestoApp getRestoApp()
  {
    return restoApp;
  }

  public HighChair getHighChair(int index)
  {
    HighChair aHighChair = highChairs.get(index);
    return aHighChair;
  }

  public List<HighChair> getHighChairs()
  {
    List<HighChair> newHighChairs = Collections.unmodifiableList(highChairs);
    return newHighChairs;
  }

  public int numberOfHighChairs()
  {
    int number = highChairs.size();
    return number;
  }

  public boolean hasHighChairs()
  {
    boolean has = highChairs.size() > 0;
    return has;
  }

  public int indexOfHighChair(HighChair aHighChair)
  {
    int index = highChairs.indexOf(aHighChair);
    return index;
  }

  public boolean isNumberOfTablesValid()
  {
    boolean isValid = numberOfTables() >= minimumNumberOfTables();
    return isValid;
  }

  public static int minimumNumberOfTables()
  {
    return 1;
  }

  public boolean addTable(Table aTable)
  {
    boolean wasAdded = false;
    if (tables.contains(aTable)) { return false; }
    tables.add(aTable);
    if (aTable.indexOfReservation(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aTable.addReservation(this);
      if (!wasAdded)
      {
        tables.remove(aTable);
      }
    }
    return wasAdded;
  }

  public boolean removeTable(Table aTable)
  {
    boolean wasRemoved = false;
    if (!tables.contains(aTable))
    {
      return wasRemoved;
    }

    if (numberOfTables() <= minimumNumberOfTables())
    {
      return wasRemoved;
    }

    int oldIndex = tables.indexOf(aTable);
    tables.remove(oldIndex);
    if (aTable.indexOfReservation(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aTable.removeReservation(this);
      if (!wasRemoved)
      {
        tables.add(oldIndex,aTable);
      }
    }
    return wasRemoved;
  }

  public boolean setTables(Table... newTables)
  {
    boolean wasSet = false;
    ArrayList<Table> verifiedTables = new ArrayList<Table>();
    for (Table aTable : newTables)
    {
      if (verifiedTables.contains(aTable))
      {
        continue;
      }
      verifiedTables.add(aTable);
    }

    if (verifiedTables.size() != newTables.length || verifiedTables.size() < minimumNumberOfTables())
    {
      return wasSet;
    }

    ArrayList<Table> oldTables = new ArrayList<Table>(tables);
    tables.clear();
    for (Table aNewTable : verifiedTables)
    {
      tables.add(aNewTable);
      if (oldTables.contains(aNewTable))
      {
        oldTables.remove(aNewTable);
      }
      else
      {
        aNewTable.addReservation(this);
      }
    }

    for (Table anOldTable : oldTables)
    {
      anOldTable.removeReservation(this);
    }
    wasSet = true;
    return wasSet;
  }

  public boolean addTableAt(Table aTable, int index)
  {  
    boolean wasAdded = false;
    if(addTable(aTable))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTables()) { index = numberOfTables() - 1; }
      tables.remove(aTable);
      tables.add(index, aTable);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTableAt(Table aTable, int index)
  {
    boolean wasAdded = false;
    if(tables.contains(aTable))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTables()) { index = numberOfTables() - 1; }
      tables.remove(aTable);
      tables.add(index, aTable);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTableAt(aTable, index);
    }
    return wasAdded;
  }

  public boolean setRestoApp(RestoApp aRestoApp)
  {
    boolean wasSet = false;
    if (aRestoApp == null)
    {
      return wasSet;
    }

    RestoApp existingRestoApp = restoApp;
    restoApp = aRestoApp;
    if (existingRestoApp != null && !existingRestoApp.equals(aRestoApp))
    {
      existingRestoApp.removeReservation(this);
    }
    restoApp.addReservation(this);
    wasSet = true;
    return wasSet;
  }

  public static int minimumNumberOfHighChairs()
  {
    return 0;
  }

  public static int maximumNumberOfHighChairs()
  {
    return 3;
  }

  public boolean addHighChair(HighChair aHighChair)
  {
    boolean wasAdded = false;
    if (highChairs.contains(aHighChair)) { return false; }
    if (numberOfHighChairs() >= maximumNumberOfHighChairs())
    {
      return wasAdded;
    }

    Reservation existingReservation = aHighChair.getReservation();
    if (existingReservation == null)
    {
      aHighChair.setReservation(this);
    }
    else if (!this.equals(existingReservation))
    {
      existingReservation.removeHighChair(aHighChair);
      addHighChair(aHighChair);
    }
    else
    {
      highChairs.add(aHighChair);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeHighChair(HighChair aHighChair)
  {
    boolean wasRemoved = false;
    if (highChairs.contains(aHighChair))
    {
      highChairs.remove(aHighChair);
      aHighChair.setReservation(null);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addHighChairAt(HighChair aHighChair, int index)
  {  
    boolean wasAdded = false;
    if(addHighChair(aHighChair))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfHighChairs()) { index = numberOfHighChairs() - 1; }
      highChairs.remove(aHighChair);
      highChairs.add(index, aHighChair);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveHighChairAt(HighChair aHighChair, int index)
  {
    boolean wasAdded = false;
    if(highChairs.contains(aHighChair))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfHighChairs()) { index = numberOfHighChairs() - 1; }
      highChairs.remove(aHighChair);
      highChairs.add(index, aHighChair);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addHighChairAt(aHighChair, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    ArrayList<Table> copyOfTables = new ArrayList<Table>(tables);
    tables.clear();
    for(Table aTable : copyOfTables)
    {
      aTable.removeReservation(this);
    }
    RestoApp placeholderRestoApp = restoApp;
    this.restoApp = null;
    if(placeholderRestoApp != null)
    {
      placeholderRestoApp.removeReservation(this);
    }
    while( !highChairs.isEmpty() )
    {
      highChairs.get(0).setReservation(null);
    }
  }

  // line 21 "../../../../../RestoAppPersistence.ump"
   public static  void reinitializeAutouniqueRN(List<Reservation> reservations){
    nextReservationNumber = 0;
		for (Reservation reservation : reservations) {
			if (reservation.getReservationNumber() > nextReservationNumber) {
				nextReservationNumber = reservation.getReservationNumber();
			}
		}
	  	nextReservationNumber++;
  }

  // line 44 "../../../../../RestoApp v3.ump"
   public boolean doesOverlap(Date date, Time time){
    boolean overlap = false;
    long time1 = this.getTime().getTime();
    long time2 = time.getTime();
    long twoh= 7200000;
    long timediff = Math.abs(time2 - time1);
    if(timediff <= twoh) {
    	overlap=true;
    }
    return overlap;
  }


  public String toString()
  {
    return super.toString() + "["+
            "reservationNumber" + ":" + getReservationNumber()+ "," +
            "numberInParty" + ":" + getNumberInParty()+ "," +
            "contactName" + ":" + getContactName()+ "," +
            "contactEmailAddress" + ":" + getContactEmailAddress()+ "," +
            "contactPhoneNumber" + ":" + getContactPhoneNumber()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "time" + "=" + (getTime() != null ? !getTime().equals(this)  ? getTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "restoApp = "+(getRestoApp()!=null?Integer.toHexString(System.identityHashCode(getRestoApp())):"null");
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 18 "../../../../../RestoAppPersistence.ump"
  private static final long serialVersionUID = 2315072607928790501L ;

  
}