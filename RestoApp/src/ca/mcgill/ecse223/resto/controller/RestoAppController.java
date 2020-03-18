package ca.mcgill.ecse223.resto.controller;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.model.Table;
import ca.mcgill.ecse223.resto.model.Table.Status;
import ca.mcgill.ecse223.resto.model.Bill;
import ca.mcgill.ecse223.resto.model.Menu;
import ca.mcgill.ecse223.resto.model.MenuItem;
import ca.mcgill.ecse223.resto.model.Order;
import ca.mcgill.ecse223.resto.model.OrderItem;
import ca.mcgill.ecse223.resto.model.PricedMenuItem;
import ca.mcgill.ecse223.resto.model.Reservation;
import ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory;
import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.model.Seat;

public class RestoAppController {

	public RestoAppController() {
	}

	public static void moveTable(Table table, int x, int y) throws InvalidInputException{
		String error ="";
		if (table == null) {	
			error = "The table doesn't exist";		
		}
		if(x<0||y<0) {
			error = "Location entered must be positive";
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		int width = table.getWidth();
		int length = table.getLength();
		boolean overlaps = true;
		RestoApp restoApp = RestoAppApplication.getRestoApp();
		for(Table currentTable:restoApp.getCurrentTables()) {
			overlaps = currentTable.doesOverlap(x,y,width,length);
			if (overlaps == true){
				if (!currentTable.equals(table)) {
					error = "Table cannot be placed due to overlapping";
					throw new InvalidInputException(error.trim());
				}
			}
		}
		try {
			table.setX(x);
			table.setY(y);
			RestoAppApplication.save();
		}catch(RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}
	public static List<MenuItem> getMenuItems(ItemCategory itemCategory) throws InvalidInputException {
		if(itemCategory == null) {
		      throw new InvalidInputException("Item category not in system");
		}
		List<MenuItem> listTemp = new ArrayList<MenuItem>();
		RestoApp restoApp = RestoAppApplication.getRestoApp();
		List<MenuItem> menuItems = restoApp.getMenu().getMenuItems();
		for (MenuItem menuitem : menuItems) {
			boolean current = menuitem.hasCurrentPricedMenuItem();
			ItemCategory category = menuitem.getItemCategory();		
			if (current && category.equals(itemCategory)) {
				listTemp.add(menuitem);
			}
		}
		return listTemp;
		
	}
	
	public static List<ItemCategory> getItemCategories() {
		ArrayList<ItemCategory> listTemp = new ArrayList<ItemCategory>();
		for (ItemCategory itemCategory : ItemCategory.values()) {
			listTemp.add(itemCategory);
		}
		return (List<ItemCategory>) listTemp;
	}
	
	public static List<Table> getCurrentTables() {
		return RestoAppApplication.getRestoApp().getCurrentTables();
	}
	
	public static List<Order> getCurrentOrders() {
		return RestoAppApplication.getRestoApp().getCurrentOrders();
	}
	
	public static List<MenuItem> getMenuItems() {
		return RestoAppApplication.getRestoApp().getMenu().getMenuItems();
	}
	
	public static void createTable(String numberString, String xString, String yString, String widthString, String lengthString, String numOfSeatsString) throws InvalidInputException {
		String error = "";
		if (numberString.equals("") || xString.equals("") 
				|| yString.equals("") || widthString.equals("") 
				|| lengthString.equals("") || numOfSeatsString.equals("")) 
			{
				error = error + "Every field has to be filled.";
				throw new InvalidInputException(error.trim());
			}
		try {
			int number = Integer.parseInt(numberString); 
			int x = Integer.parseInt(xString);
			int y = Integer.parseInt(yString); 
			int width = Integer.parseInt(widthString); 
			int length = Integer.parseInt(lengthString);
			int numOfSeats = Integer.parseInt(numOfSeatsString);
			RestoApp restoapp = RestoAppApplication.getRestoApp();
			if (number<=0)
			{
				error = "Table number must be positive. ";
			}
			if (x<0) 
			{
				error = error + "x coordinate can not be negative. ";
			}
			if (y<0)
			{
				error = error + "y coordinate can not be negative. ";
			}
			if (width<=0)
			{
				error = error + "Table width must be positive. ";
			}
			if (length<=0)
			{
				error = error + "Table length must be positive. ";
			}
			if (numOfSeats<=0)
			{
				error = error + "Number of seats must be positive. ";
			}
			
			List<Table> currentTables = restoapp.getCurrentTables();
			
			for (int i=0; i<currentTables.size(); i++)
			{
				if (currentTables.get(i).doesOverlap(x, y, width, length) == true)
				{
					error = error + "Table overlaps with another table. ";
					break;
				}
			}
			
			if (error.length() > 0) {
				throw new InvalidInputException(error.trim());
			}
			
			Table table = new Table(number, x, y, width, length, restoapp);
			restoapp.addCurrentTable(table);
			for (int i=0; i<numOfSeats; i++)
			{
				Seat seat = table.addSeat();
				table.addCurrentSeat(seat);
		}
		
		}catch(NumberFormatException e) {
			throw new InvalidInputException("Please provide non-empty numerical input in all fields.");
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		RestoAppApplication.save();
	}
	
	//adds seat to specified table
	public static void addSeat(Table table) throws InvalidInputException{
		if(table == null)
			throw new InvalidInputException("A table must be specified to add a seat.");
		table.addCurrentSeat(table.addSeat());
		RestoAppApplication.save();
	}
	
	//removes a seat from specified table
	public static void removeSeat(Table table) throws InvalidInputException {
		if(table == null)
			throw new InvalidInputException("A table must be specified to remove a seat.");
		if(table.hasReservations())
			throw new InvalidInputException("A seat cannot be removed from a table that is currently reserved.");
		RestoApp resto = RestoAppApplication.getRestoApp();
		List<Order> currentOrders = resto.getCurrentOrders();
		Boolean tableHasCurrentOrder = false;
		for (Order order : currentOrders)
		{
			List<Table> orderTables = order.getTables();
			tableHasCurrentOrder = orderTables.contains(table);
			if(tableHasCurrentOrder)
			{
				throw new InvalidInputException("A seat cannot be removed from a table that currently has an order.");
			}
		}

		try {
			Seat seat = table.getCurrentSeat(0);
			table.removeCurrentSeat(seat);
		}
		catch(RuntimeException e) {
			throw new InvalidInputException("No more seats can be removed from this table.");
		}
		
		RestoAppApplication.save();
	}
	
	//sets table number of specified table
	public static void setTableNumber(Table table, int number) throws InvalidInputException{
		if(table == null)
			throw new InvalidInputException("A table must be specified to change a table number.");
		if(number <= 0)
			throw new InvalidInputException("A table's number must be greater than 0.");
		if(table.hasReservations())
			throw new InvalidInputException("A table with reservations cannot have its number changed.");
		RestoApp resto = RestoAppApplication.getRestoApp();
		List<Order> currentOrders = resto.getCurrentOrders();
		for(Order order : currentOrders) {
			List<Table> tables = order.getTables();
			Boolean inUse = tables.contains(table);
			if(inUse)
				throw new InvalidInputException("A table's number cannot be changed if it is currently in use.");
		}
		Boolean wasSet = table.setNumber(number);
		if(!wasSet)
			throw new InvalidInputException("A table with this number already exists. Please choose a different number.");
		RestoAppApplication.save();
	}
	
	//removes a table
	public static void removeTable(Table table) throws InvalidInputException{
		if(table == null)
			throw new InvalidInputException("A table must be specified to remove it.");
		if(table.hasReservations())
			throw new InvalidInputException("A table with reservations cannot be removed.");
		RestoApp resto = RestoAppApplication.getRestoApp();
		List<Order> currentOrders = resto.getCurrentOrders();
		for(Order order : currentOrders) {
			List<Table> tables = order.getTables();
			Boolean inUse = tables.contains(table);
			if(inUse)
				throw new InvalidInputException("A table cannot be removed if it is currently in use.");
		}
		resto.removeCurrentTable(table);
		RestoAppApplication.save();
	}
	
	//starts an order
	public static void startOrder(List<Table> tables) throws InvalidInputException {
		if(tables == null)
			throw new InvalidInputException("At least one table is required to create an order.");
		RestoApp r = RestoAppApplication.getRestoApp();
		List<Table> currentTables = r.getCurrentTables();
		for(Table table : tables) {
			Boolean current = currentTables.contains(table);
			if(current == false)
				throw new InvalidInputException("Order must include at least one currently used table.");
		}
		Boolean orderCreated = false;
		Order newOrder = null;
		for(Table table : tables) {
			if(orderCreated)
				table.addToOrder(newOrder);
			else {
				Order lastOrder = null;
				if(table.numberOfOrders() > 0)
					lastOrder = table.getOrder(table.numberOfOrders()-1);
				table.startOrder();
				if(table.numberOfOrders() > 0 && !table.getOrder(table.numberOfOrders()-1).equals(lastOrder)) {
					orderCreated = true;
					newOrder = table.getOrder(table.numberOfOrders()-1);
				}
			}
		}
		if(!orderCreated)
			throw new InvalidInputException("Order could not be created from currently selected tables.");
		r.addCurrentOrder(newOrder);
		RestoAppApplication.save();
	}
	
	//ends an order
	public static void endOrder(Order order) throws InvalidInputException {
		if(order == null)
			throw new InvalidInputException("An order must be selected to remove an order.");
		RestoApp r = RestoAppApplication.getRestoApp();
		List<Order> currentOrders = r.getCurrentOrders();
		Boolean current = currentOrders.contains(order);
		if(!current)
			throw new InvalidInputException("Order is not a current Order.");
		List<Table> tables = order.getTables();
		int size = tables.size();
		for(int i = 0; i < size; i++) {
			Table table = tables.get(i);
			if(table.numberOfOrders() > 0 && table.getOrder(table.numberOfOrders()-1).equals(order)) {
				table.endOrder(order);
				if(tables.size() < size) {
					size--;
					i--;
				}
			}
		}
		if(RestoAppController.allTablesAvailableOrDifferentCurrentOrder(tables, order)) {
			r.removeCurrentOrder(order);
		}else {
			
		}
		RestoAppApplication.save();
	}
	
	private static boolean allTablesAvailableOrDifferentCurrentOrder(List<Table> tables, Order order) {
		for(Table table : tables){
			Order lastOrder = table.getOrder(table.numberOfOrders()-1);
			if(!(table.getStatus() == Status.Available || lastOrder != order))
				return false;
		}
		return true;
	}
	
	//reserve table
	public static void reserveTable(Date date, Time time, int numberInParty, String contactName, String contactEmailAddress, 
									String contactPhoneNumber, List<Table> tables) throws InvalidInputException{
		String error = "";
		if (date == null)
			error = "Date cannot be empty";		
		if(time == null)
			error = "Time cannot be empty";
		if(contactName.equals(""))
			error = "Contact name cannot be empty";
		if(contactEmailAddress.equals(""))
			error = "Contact email address cannot be empty";
		if(contactPhoneNumber.equals(""))
			error = "Contact phone number cannot be empty";
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		if(isDateInPast(date))
			error = "Invalid date";
		if (isTimeInPast(time)&&isDateToday(date))
			error = "Invalid time";
		if(numberInParty <= 0)
			error = "Number in party should be positive";
		if(tables.size() == 0)
			error = "Must select a table";
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		RestoApp restoApp = RestoAppApplication.getRestoApp();
		List<Table> currentTable = restoApp.getCurrentTables();
		int seatCapacity = 0;
		for(Table table: tables) {
			boolean current = currentTable.contains(table);
			if(!current) {
				error = "Table not found in current tables";
				throw new InvalidInputException(error.trim());
			}
			seatCapacity += table.numberOfCurrentSeats();
			List<Reservation> reservations = table.getReservations();
				for(Reservation reservation: reservations) {
					boolean overlaps = reservation.doesOverlap(date, time);
						if(overlaps) {
							error = "Cannot create new reservation due to previous reservations";
							throw new InvalidInputException(error.trim());
						}
				}
		}
		if(seatCapacity < numberInParty)
			throw new InvalidInputException("Not enough seats");
		Table [] tableArray = tables.toArray(new Table[tables.size()]);
		Reservation res = new Reservation(date, time, numberInParty, contactName, contactEmailAddress, contactPhoneNumber, restoApp, tableArray);
		for(Table table: tables) {
			table.addReservation(res);
			List<Reservation> reservations = table.getReservations();
			reservations = new ArrayList<Reservation>(reservations);
			Collections.sort(reservations, new Comparator<Reservation>() {
				public int compare(Reservation o1, Reservation o2) {
				      return o1.getDate().compareTo(o2.getDate());
				  }
			});
		}
		try {
			RestoAppApplication.save();
		}catch(RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	private static boolean isDateToday(Date date) {
		java.util.Date tempToday = RestoAppApplication.getRestoApp().getCurrentDate();
		return date.equals(tempToday);
	}
	private static boolean isDateInPast(Date date) {
		java.util.Date tempToday = RestoAppApplication.getRestoApp().getCurrentDate();
		return date.before(tempToday);
	}

	private static boolean isTimeInPast(Time time) {
		java.util.Date tempToday = RestoAppApplication.getRestoApp().getCurrentTime();
		return time.before(tempToday);
	}
	
	//view order
	public static List<OrderItem> getOrderItem(Table table) throws InvalidInputException{
		if(table == null) {
			throw new InvalidInputException("Table cannot be empty");
		}
		RestoApp r = RestoAppApplication.getRestoApp();
		List <Table> currentTables = r.getCurrentTables();
		boolean current = currentTables.contains(table);
		if(!current)
			throw new InvalidInputException("Table not found in current tables");
		Status status = table.getStatus();
		if(status == Status.Available)
			throw new InvalidInputException("No order found");
		Order lastOrder = null;
		if(table.numberOfOrders() > 0) {
			lastOrder = table.getOrder(table.numberOfOrders() - 1);
		}
		List <Seat> currentSeats = table.getCurrentSeats();
		List <OrderItem> result = new ArrayList<OrderItem>();
		for(Seat seat:currentSeats) {
			List <OrderItem> orderItems = seat.getOrderItems();
			for(OrderItem orderItem :orderItems) {
				Order order = orderItem.getOrder();
				if(lastOrder.equals(order) && !result.contains(orderItem))
					result.add(orderItem);
			}
		}
		return result;
	}
	
	public static void issueBill(List<Seat> seats) throws InvalidInputException{
		if(seats == null || seats.isEmpty())
			throw new InvalidInputException("A bill must be assigned to at least one seat");
		RestoApp r = RestoAppApplication.getRestoApp();
		List<Table> currentTables = r.getCurrentTables();
		Order lastOrder = null;
		for(Seat seat : seats) {
			Table table = seat.getTable();
			Boolean current = currentTables.contains(table);
			if(!current)
				throw new InvalidInputException("At least one seat is not associated with a current order");
			if(lastOrder == null) {
				if(table.numberOfOrders() > 0)
					lastOrder = table.getOrder(table.numberOfOrders()-1);
				else
					throw new InvalidInputException("A seat without an order cannot be billed");
			}
			else {
				Order comparedOrder = null;
				if(table.numberOfOrders() > 0)
					comparedOrder = table.getOrder(table.numberOfOrders()-1);
				else
					throw new InvalidInputException("A seat without an order cannot be billed");
				if(!comparedOrder.equals(lastOrder))
					throw new InvalidInputException("A seat that is not currently used cannot be billed");
			}
		}
		if(lastOrder == null)
			throw new InvalidInputException("A bill must be assigned to at least one seat that can be billed");
		boolean billCreated = false;
		Bill newBill = null;
		for(Seat seat : seats) {
			Table table = seat.getTable();
			boolean hasOrdered = false;
			for(OrderItem orderItem : lastOrder.getOrderItems()) {
				if(orderItem.getSeats().contains(seat))
					hasOrdered = true;
			}
			if(hasOrdered) {
				if(billCreated) {
					table.addToBill(newBill, seat);
				}
				else {
					Bill lastBill = null;
					if(lastOrder.numberOfBills() > 0) {
						lastBill = lastOrder.getBill(lastOrder.numberOfBills()-1);
					}
					table.billForSeat(lastOrder, seat);
					if(lastOrder.numberOfBills() > 0 && !lastOrder.getBill(lastOrder.numberOfBills()-1).equals(lastBill)) {
						billCreated = true;
						newBill = lastOrder.getBill(lastOrder.numberOfBills()-1);
					}
				}
			}
		}
		if(billCreated == false)
			throw new InvalidInputException("Unable to bill seat(s)");
		RestoAppApplication.save();
	}
	
	//returns total amount owed by all seats given in input
	public static double getOwedAmount(Seat seat) throws InvalidInputException {
		if(!currentlyBilled(seat))
			throw new InvalidInputException("Specified seat is not billed");
		Table table = seat.getTable();
		Order order = table.getOrder(table.numberOfOrders()-1);
		double total = 0.00;
		for(OrderItem orderItem : order.getOrderItems()) {
			if(orderItem.getSeats().contains(seat)) {
				PricedMenuItem menuItem = orderItem.getPricedMenuItem();
				total += menuItem.getPrice()*orderItem.getQuantity()/orderItem.numberOfSeats();
			}
		}
		return total;
	}
	
	public static boolean currentlyBilled(Seat seat) {
		if(seat == null)
			return false;
		RestoApp r = RestoAppApplication.getRestoApp();
		Table table = seat.getTable();
		if(table.getStatus()!=Status.Ordered || !r.getCurrentTables().contains(table))
			return false;
		Order order = table.getOrder(table.numberOfOrders()-1);
		List<Bill> bills = order.getBills();
		if(seat.numberOfBills() == 0)
			return false;
		Bill lastBill = seat.getBill(seat.numberOfBills()-1);
		if(!bills.contains(lastBill))
			return false;
		return true;
	}
	
	public static void addMenuItem(String name, ItemCategory category, double price) throws InvalidInputException{
		String error = "";
		if (name == null || name.equals("")) {
			error = "Must indicate a name";	
		}		
		if(category == null) {
			error = "Must select an item category";
		}	
		if(price <= 0) {
			error = "Price must be greater than zero";
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		RestoApp r = RestoAppApplication.getRestoApp();
		Menu menu = r.getMenu();
		try {
			MenuItem menuItem = new MenuItem(name,menu);
			menuItem.setItemCategory(category);
			PricedMenuItem pmi = menuItem.addPricedMenuItem(price, r);
			menuItem.setCurrentPricedMenuItem(pmi);
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		RestoAppApplication.save();
	}
	
	public static void removeMenuItem(MenuItem menuItem) throws InvalidInputException{
		if (menuItem == null) {
			throw new InvalidInputException("Must select a menu item");
		}
		boolean current = menuItem.hasCurrentPricedMenuItem();
		if (current == false) {
			throw new InvalidInputException("Menu item must have a current price");
		}
		menuItem.setCurrentPricedMenuItem(null);
		RestoAppApplication.save();
	}
	
	public static void updateMenuItem(MenuItem menuItem, String name, ItemCategory category, double price) throws InvalidInputException{
		String error = "";
		if (menuItem == null) {
			error = "Must select a menu item";	
		}
		if (name == null || name.equals("")) {
			error = "Must indicate a name";	
		}
		if(category == null) {
			error = "Must select an item category";
		}
		if(price <= 0) {
			error = "Price must be greater than zero";
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		boolean current = menuItem.hasCurrentPricedMenuItem();
		if (current == false) {
			throw new InvalidInputException("Menu item must have a current price");
		}
		boolean duplicate = menuItem.setName(name);
		if (duplicate == false) {
			throw new InvalidInputException("There is already a menu item with that name");
		}
		menuItem.setItemCategory(category);
		if (price != menuItem.getCurrentPricedMenuItem().getPrice()) {
			RestoApp r = RestoAppApplication.getRestoApp();
			PricedMenuItem pmi = menuItem.addPricedMenuItem(price, r);
			menuItem.setCurrentPricedMenuItem(pmi);
		}
		RestoAppApplication.save();
	}
	
	public static void orderMenuItem(MenuItem menuItem, int quantity, List<Seat> seats) throws InvalidInputException{
		String error = "";
		if (menuItem == null) {
			error = "Must select a menu item";	
		}
		if (seats == null || seats.size() == 0) {
			error = "Must select at least one seat";	
		}
		if(quantity <= 0) {
			error = "Quantity must be greater than zero";
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		RestoApp r = RestoAppApplication.getRestoApp();
		boolean current = menuItem.hasCurrentPricedMenuItem();
		if (current == false) {
			throw new InvalidInputException("Menu item must have a current price");
		}
		List<Table> currentTables = r.getCurrentTables();
		Order lastOrder = null;
		for (Seat seat: seats) {
			Table table = seat.getTable();
			current = currentTables.contains(table);
			if (current == false) {
				throw new InvalidInputException("All seats must be part of a current table");
			}
			List<Seat> currentSeats = table.getCurrentSeats();
			current = currentSeats.contains(seat);
			if (current == false) {
				throw new InvalidInputException("All selected seats must be current seats");
			}
			if (lastOrder == null) {
				if (table.numberOfOrders() > 0) {
					lastOrder = table.getOrder(table.numberOfOrders() - 1);
				} else {
					throw new InvalidInputException("A table associated with a selected seat does not have any orders");
				}
			} else {
				Order comparedOrder = null;
				if (table.numberOfOrders() > 0) {
					comparedOrder = table.getOrder(table.numberOfOrders() - 1);
				} else {
					throw new InvalidInputException("A table associated with a selected seat does not have any orders");
				}
				if (!comparedOrder.equals(lastOrder)) {
					throw new InvalidInputException("All selected seats must have tables that are part of the same order");
				}
			}
		}
		if (lastOrder == null) {
			throw new InvalidInputException("A table associated with a selected seat does not have any orders");
		}
		PricedMenuItem pmi = menuItem.getCurrentPricedMenuItem();
		boolean itemCreated = false;
		OrderItem newItem = null;
		for (Seat seat: seats) {
			Table table = seat.getTable();
			if (itemCreated) {
				table.addToOrderItem(newItem, seat);
			} else {
				OrderItem lastItem = null;
				if (lastOrder.numberOfOrderItems() > 0) {
					lastItem = lastOrder.getOrderItem(lastOrder.numberOfOrderItems() - 1);
				}
				table.orderItem(quantity, lastOrder, seat, pmi);
				if (lastOrder.numberOfOrderItems() > 0 && 
						!lastOrder.getOrderItem(lastOrder.numberOfOrderItems() - 1).equals(lastItem)) {
					itemCreated = true;
					newItem = lastOrder.getOrderItem(lastOrder.numberOfOrderItems() - 1);
				}
			}
		}
		if (itemCreated == false) {
			throw new InvalidInputException("Menu item was unable to get ordered");
		}	
		RestoAppApplication.save();
	}
	
	public static void cancelOrderItem(OrderItem orderItem) throws InvalidInputException
	{
		String error = "";
		if (orderItem.equals(null)) 
			{
				error = error + "An order item must be selected";
				throw new InvalidInputException(error.trim());
			}
		try 
		{
			if (error.length() > 0) {
				throw new InvalidInputException(error.trim());
			}
			List<Seat> seats = orderItem.getSeats();
			Order order = orderItem.getOrder();
			List<Table> tables = new ArrayList<Table>();
			
			for (Seat seat : seats)
			{
				Table seatTable = seat.getTable();
				Order lastOrder = null;
				if (seatTable.numberOfOrders()>0)
				{
					lastOrder = seatTable.getOrder(seatTable.numberOfOrders()-1);
				}
				else 
				{
					
				}
				
				if (lastOrder.equals(order) && !tables.contains(seatTable))
				{
					tables.add(seatTable);
				}
				
			}
				
				for (Table table : tables) 
				{
					table.cancelOrderItem(orderItem);
				}
				
		}
		
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		
		RestoAppApplication.save();	
	}
	
	public static void cancelOrder(Table table) throws InvalidInputException
	{
		String error = "";
		if (table.equals(null)) 
			{
				error = error + "A table must be selected";
				throw new InvalidInputException(error.trim());
			}
		try 
		{	
			RestoApp restoApp = RestoAppApplication.getRestoApp();
			List<Table> currentTables = restoApp.getCurrentTables();
			boolean current = currentTables.contains(table);
			if (current == false)
			{
				error = error + "Number of seats must be positive. ";
			}
			if (error.length() > 0) {
				throw new InvalidInputException(error.trim());
			}
			
			table.cancelOrder();
		}
			
			catch (RuntimeException e) {
				throw new InvalidInputException(e.getMessage());
			}
			
		RestoAppApplication.save();	
			
	}
	
}
