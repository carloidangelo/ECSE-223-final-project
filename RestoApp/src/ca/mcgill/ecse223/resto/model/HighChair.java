/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse223.resto.model;

// line 95 "../../../../../RestoApp v3.ump"
public class HighChair
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //HighChair Associations
  private Table table;
  private Reservation reservation;
  private RestoApp restoApp;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public HighChair(RestoApp aRestoApp)
  {
    boolean didAddRestoApp = setRestoApp(aRestoApp);
    if (!didAddRestoApp)
    {
      throw new RuntimeException("Unable to create highChair due to restoApp");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public Table getTable()
  {
    return table;
  }

  public boolean hasTable()
  {
    boolean has = table != null;
    return has;
  }

  public Reservation getReservation()
  {
    return reservation;
  }

  public boolean hasReservation()
  {
    boolean has = reservation != null;
    return has;
  }

  public RestoApp getRestoApp()
  {
    return restoApp;
  }

  public boolean setTable(Table aTable)
  {
    boolean wasSet = false;
    if (aTable != null && aTable.numberOfHighChairs() >= Table.maximumNumberOfHighChairs())
    {
      return wasSet;
    }

    Table existingTable = table;
    table = aTable;
    if (existingTable != null && !existingTable.equals(aTable))
    {
      existingTable.removeHighChair(this);
    }
    if (aTable != null)
    {
      aTable.addHighChair(this);
    }
    wasSet = true;
    return wasSet;
  }

  public boolean setReservation(Reservation aReservation)
  {
    boolean wasSet = false;
    if (aReservation != null && aReservation.numberOfHighChairs() >= Reservation.maximumNumberOfHighChairs())
    {
      return wasSet;
    }

    Reservation existingReservation = reservation;
    reservation = aReservation;
    if (existingReservation != null && !existingReservation.equals(aReservation))
    {
      existingReservation.removeHighChair(this);
    }
    if (aReservation != null)
    {
      aReservation.addHighChair(this);
    }
    wasSet = true;
    return wasSet;
  }

  public boolean setRestoApp(RestoApp aRestoApp)
  {
    boolean wasSet = false;
    //Must provide restoApp to highChair
    if (aRestoApp == null)
    {
      return wasSet;
    }

    //restoApp already at maximum (3)
    if (aRestoApp.numberOfHighChairs() >= RestoApp.maximumNumberOfHighChairs())
    {
      return wasSet;
    }
    
    RestoApp existingRestoApp = restoApp;
    restoApp = aRestoApp;
    if (existingRestoApp != null && !existingRestoApp.equals(aRestoApp))
    {
      boolean didRemove = existingRestoApp.removeHighChair(this);
      if (!didRemove)
      {
        restoApp = existingRestoApp;
        return wasSet;
      }
    }
    restoApp.addHighChair(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    if (table != null)
    {
      Table placeholderTable = table;
      this.table = null;
      placeholderTable.removeHighChair(this);
    }
    if (reservation != null)
    {
      Reservation placeholderReservation = reservation;
      this.reservation = null;
      placeholderReservation.removeHighChair(this);
    }
    RestoApp placeholderRestoApp = restoApp;
    this.restoApp = null;
    if(placeholderRestoApp != null)
    {
      placeholderRestoApp.removeHighChair(this);
    }
  }

}