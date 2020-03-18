package ca.mcgill.ecse223.resto.view;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoAppController;
import ca.mcgill.ecse223.resto.model.Bill;
import ca.mcgill.ecse223.resto.model.MenuItem;
import ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory;

import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.model.Seat;
import ca.mcgill.ecse223.resto.model.Table;
import ca.mcgill.ecse223.resto.model.Order;
import ca.mcgill.ecse223.resto.model.OrderItem;


public class RestoAppPage extends JFrame {
	
	private static final long serialVersionUID = -4426310869335015542L;
	
	//Error
	private String error = "";
	private JLabel errorMessage;
	
	//Main Menu
	private JButton menu;
	private JButton restaurantLayout;
	private JButton reservation;
	private JButton changeTableStatus;
	private JButton order;
	private JButton issueBill;
	
	//Menu SubMenu
	private JButton viewMenu;
	private JButton	updateMenu;
	
	//Restaurant Layout SubMenu	
	private JButton addTable;
	private JButton removeTable;
	private JButton updateTable;
	private JButton changeTableLocation;
	
	//Reservation SubMenu
	private JButton reserveTable;
	private JButton highChairOptions;
	
	//Change Table Status SubMenu
	private JComboBox <String> SelectGroupList;
	
	private JLabel CHGTABSTASelectTable;
	private JLabel CHGTABSTASelectGroup;
	
	private Integer SelectedGroup = -1;
	
	private JButton CHGTABSTAAssignTables;
	private JButton CHGTABSTARemoveGroup;
	
	private JPanel CHGTABSTASelectTableMenu;
	private JScrollPane CHGTABSTASelectTableMenuScroll;
	private List<Table> CHGTABSTAtables;
	
	//select group combo box hashmap
	private HashMap<Integer, Order> groups;
	
	//Order SubMenu
	private JButton orderMenuItem;
	private JButton cancelOrder;
	private JButton viewOrder;
	
	//View Menu SubMenu
	private JButton appetizer;
	private JButton main; 
	private JButton dessert;
	private JButton alcoholicBeverage; 
	private JButton nonAlcoholicBeverage;
	
	//Add Table SubMenu
	private JLabel tableNumber;
	private JLabel xCoord;
	private JLabel yCoord;
	private JLabel tableWidth;
	private JLabel tableLength;
	private JLabel numOfSeats;
	private JTextField tableNumberField;
	private JTextField xCoordField;
	private JTextField yCoordField;
	private JTextField tableWidthField;
	private JTextField tableLengthField;
	private JTextField numOfSeatsField;
	private JButton addTableConfirm;
	
	//Remove Table SubMenu
	private JComboBox <String> selectTableRemoveTable;
	private JLabel selectTableRemoveTableLabel;
	private JButton removeTableConfirm;
	private Integer selectedTable2 = -1;
	
	/*Update Table SubMenu*/
	//Select table combo box hashmaps
	private HashMap<Integer, Table> tables;
	private HashMap<Table, Integer> tablesReverse;
	
	//select table (update table)
	private JLabel selectTableUpdateTableLabel;
	private JComboBox <String> selectTableUpdateTable;
	private Integer selectedTable1 = -1;
	private java.awt.event.ActionListener selectTableUpdateTableListener;
	
	//update table seat number
	private JLabel changeNumSeatsLabel;
	private JButton addSeat;
	private JButton removeSeat;
	
	//update table number
	private JLabel updateTableNumberLabel;
	private JLabel tableNumberLabel;
	private JTextField newTableNumber;
	private JButton setTableNumber;
	
	//Move Table SubMenu
	private JComboBox <String> tableList;
		
	private JLabel moveTable;
	
	private JLabel xCoord2;
	private JLabel yCoord2;
	private JTextField xCoordField2;
	private JTextField yCoordField2;
		
	private JButton changeTableLocationConfirm;
	private Integer selectedTable = -1;
	
	//Reserve Table SubMenu
	private JLabel RESSelectTable;
	private JLabel RESDate;
	private JLabel RESTime;
	private JLabel RESNumberInParty;
	private JLabel RESContactName;
	private JLabel RESContactEmail;
	private JLabel RESContactPhoneNumber;
		
	private JTextField RESTimeField;
	private JTextField RESNumberInPartyField;
	private JTextField RESContactNameField;
	private JTextField RESContactEmailField;
	private JTextField RESContactPhoneNumberField;
		
	private JButton RESMakeReservation;
	private JDatePickerImpl RESDateCalendar;
	private JPanel RESSelectTableMenu;
	private JScrollPane RESSelectTableMenuScroll;
	private List<Table> REStables;	
		
	//View Menu SubMenu SubMenu
	private JPanel appetizerMenu;
	private JPanel mainMenu;
	private JPanel dessertMenu;
	private JPanel alcoholicBeverageMenu; 
	private JPanel nonAlcoholicBeverageMenu;

	//Restaurant Layout Display
	private RestoLayout restoLayout;
	private JScrollPane restoLayoutContainer;
	
	//View Order
	private JLabel viewOrderSelectTable;
	private JComboBox <String> viewOrderTableList;
	private JLabel viewOrderLabel;
	private JButton viewOrderButton;
	private JTable viewOrderList;
	private Integer selectedTable3 = -1;
	private DefaultTableModel viewOrderDtm;
	private String viewOrderListColumnNames[] = {"Order Item", "Quantity", "Seats"};
	private static final int HEIGHT_OVERVIEW_TABLE = 200;

	private JScrollPane viewOrderScrollPane;
	private List <OrderItem> orderItems;
	
	//Issue Bill
	private JLabel issueBillSelectOrderLabel;
	private JComboBox <String> issueBillSelectOrder;
	//uses groups hashmap
	private Integer selectedOrder = -1;
	
	private JPanel issueBillSelectSeat;
	private JScrollPane issueBillSelectSeatScroll;
	private List<Seat> issueBillSeats;
	
	private JLabel issueBillSelectTableLabel;
	private JComboBox <String> issueBillSelectTable;
	//uses tables hashmap
	private Integer selectedTable4 = -1;
	
	private JButton issueBillCreate;
	private JButton issueBillEntireTable;
	private JButton issueBillEachSeat;
	
	private JLabel issueBillSelectBillLabel;
	private JComboBox <String> issueBillSelectBill;
	private HashMap <Integer, Bill> bills;
	private Integer selectedBill = -1;
	private JButton selectBill;
	
	private JTable viewBillTable;
	private DefaultTableModel viewBillDtm;
	private String viewBillColumnNames[] = {"Seat", "Table", "Amount Owed"};
	//uses HEIGHT_OVERVIEW_TABLE
	private JScrollPane viewBillScrollPane;
	private JLabel billTotalOwed;
	private JLabel billTotalOwedLabel;
	private JButton refreshBillTable;
	
	//Order Menu Item SubMenu
	private JLabel OISelectTableLabel;
	private JComboBox <String> OISelectOrderList;
	private Integer OIselectedOrder = -1;
	private JButton OIupdateSeatDisplayButton;
	
	private JLabel OISelectSeatsLabel;
	private JPanel OISelectSeatsMenu;
	private JScrollPane OISelectSeatsMenuScroll;
	private List<Seat> OIseats;
	
	private JLabel OISelectMenuItemLabel;
	private JComboBox <String> OISelectMenuItemList;
	private Integer OIselectedMenuItem = -1;
	private List<MenuItem> OImenuItems;
	
	private JLabel OIQuantityLabel;
	private JTextField OIQuantityField;
	
	private JButton OIButton;
	
	//Cancel Order SubMenu
	private JLabel COSelectTableLabel;
	private JComboBox <String> COSelectTableList;
	private Integer COselectedTable = -1;
	private JButton COupdateSeatDisplayButton;
	
	private JLabel COSelectSeatLabel;
	private JComboBox <String> COSelectSeatList;
	private Integer COselectedSeat = -1;
	private JButton COupdateOrderItemDisplayButton;
	//private Seat COseat;
	private List<Seat> COseats;
	
	private JLabel COSelectOrderItemLabel;
	private JComboBox <String> COSelectOrderItemList;
	private Integer COselectedOrderItem = -1;
	//private OrderItem COorderItem;
	private List<OrderItem> COorderItems;
	
	private JButton COcancelOrderButton;
	private JButton COcancelOrderItemButton;
	
	private Table COtempTable;
	private Seat COtempSeat;

	//Update Menu SubMenu
	private JLabel UMSelectMenuItemLabel;
	private JComboBox <String> UMSelectMenuItemList;
	private Integer UMselectedMenuItem = -1;
	private List<MenuItem> UMmenuItems;
	
	private JLabel UMSelectItemCategoryLabel;
	private JComboBox <String> UMSelectItemCategoryList;
	private Integer UMselectedItemCategory = -1;
	
	private JLabel UMnameLabel;
	private JLabel UMpriceLabel;
	private JTextField UMnameField;
	private JTextField UMpriceField;
	
	private JButton UMremoveMenuItemButton;
	private JButton UMaddMenuItemButton;
	private JButton UMupdateMenuItemButton;
	
	//add highchair option to reservation submenu
	private JComboBox <String> HCAddHighChair;
	private Integer HCSelectedHighChair = -1;
	private JTextField HCQuantityField;
	private JLabel HCQuantity;
	private JButton HCConfirm;
	
	public RestoAppPage() {
		initComponents();
		returnToMainMenu();
		refreshData();
	}
	
	private void initComponents() {
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setBackground(Color.WHITE);
        setResizable(true);
		setTitle("Restaurant Application");
		
		//Error
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);
		
		//Main Menu		
		menu = new JButton("Menu");
		restaurantLayout = new JButton("Restaurant Layout");
		reservation = new JButton("Reservation");
		changeTableStatus = new JButton("Change Table Status"); 
		order = new JButton("Order");
		issueBill = new JButton("Issue Bill");
		
		//Menu SubMenu
		viewMenu = new JButton("View Menu");
		updateMenu = new JButton("Update Menu");
		
		//Restaurant Layout SubMenu
		addTable = new JButton("Add Table");
		removeTable = new JButton("Remove Table");
		updateTable = new JButton("Update Table");
		changeTableLocation = new JButton("Change Table Location");
		
		//Reservation SubMenu
		reserveTable = new JButton("Reserve Table");
		highChairOptions = new JButton("High chair Options");
		
		//Change Table Status SubMenu
		CHGTABSTASelectTable = new JLabel("Select Table(s)");
		CHGTABSTASelectGroup = new JLabel("Select Group");
		
		CHGTABSTAAssignTables = new JButton("Assign Tables");
		CHGTABSTARemoveGroup = new JButton("Remove Group");
		
		CHGTABSTASelectTableMenu = new JPanel();
		CHGTABSTASelectTableMenuScroll = new JScrollPane(CHGTABSTASelectTableMenu);
		CHGTABSTASelectTableMenuScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		CHGTABSTAtables = new ArrayList<Table>();
		
		SelectGroupList = new JComboBox<String>(new String[0]);
		SelectGroupList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {				
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
				SelectedGroup = cb.getSelectedIndex();
			}
		});
		
		//Order SubMenu
		orderMenuItem = new JButton("Order Menu Item");
		cancelOrder = new JButton("Cancel Order");
		viewOrder = new JButton("View Order");
		
		//View Menu SubMenu
		appetizer = new JButton("Appetizer");
		main = new JButton("Main");
		dessert = new JButton("Dessert");
		alcoholicBeverage = new JButton("Alcoholic Beverage"); 
		nonAlcoholicBeverage = new JButton("Non-alcoholic Beverage");
		
		//Add Table SubMenu
		tableNumber = new JLabel("Table Number");
		xCoord = new JLabel("X");
		yCoord = new JLabel("Y");
		tableWidth = new JLabel("Width");
		tableLength = new JLabel("Length");
		numOfSeats = new JLabel("Number of Seats");
		
		tableNumberField = new JTextField("");
		xCoordField = new JTextField("");
		yCoordField = new JTextField("");
		tableWidthField = new JTextField("");
		tableLengthField = new JTextField("");
		numOfSeatsField = new JTextField("");
		
		addTableConfirm = new JButton("Add");
		
		//Remove Table SubMenu
		selectTableRemoveTable = new JComboBox<String>(new String[0]);
		selectTableRemoveTable.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {				
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
				selectedTable2 = cb.getSelectedIndex();
			}
		});
		selectTableRemoveTableLabel = new JLabel("Select Table:");
		removeTableConfirm = new JButton("Remove this table");
				
		/*Update Table SubMenu*/
		Font header = new Font("Dialog", Font.BOLD, 14);
		
		//select table (update table)
		selectTableUpdateTableLabel = new JLabel("Select Table");
		selectTableUpdateTableLabel.setFont(header);
		selectTableUpdateTable = new JComboBox<String>(new String[0]);
		//action listener for combo box removed when combo box rebuilding, added back after (see refreshData())
		selectTableUpdateTable.setMaximumSize(new Dimension(300, 25));
		
		//update seat number
		addSeat = new JButton("Add Seat");
		addSeat.setMaximumSize(new Dimension(136, 20));
		removeSeat = new JButton("Remove Seat");
		removeSeat.setMaximumSize(new Dimension(136, 20));
		changeNumSeatsLabel = new JLabel("Change number of seats");
		changeNumSeatsLabel.setFont(header);
		
		//update table number
		tableNumberLabel = new JLabel("New Table Number:");
		newTableNumber = new JTextField();
		newTableNumber.setMaximumSize(new Dimension(136, 25));
		setTableNumber = new JButton("Set Table Number");
		setTableNumber.setMaximumSize(new Dimension(136, 20));
		updateTableNumberLabel = new JLabel("Change table number");
		updateTableNumberLabel.setFont(header);
		
		//Move Table SubMenu
		tableList = new JComboBox<String>(new String[0]);
		tableList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {				
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
		        selectedTable = cb.getSelectedIndex();
			}
		});
		moveTable = new JLabel("Select Table");
		changeTableLocationConfirm = new JButton("Confirm");
		xCoord2 = new JLabel("X");
		yCoord2 = new JLabel("Y");
		xCoordField2 = new JTextField("");
		yCoordField2 = new JTextField("");
		
		//Reserve Table SubMenu
		RESSelectTable = new JLabel("Select Table(s)");
		RESDate = new JLabel("Date");
		RESTime = new JLabel("Time (hh:mm)");
		RESNumberInParty = new JLabel("Number in party");
		RESContactName = new JLabel("Contact Name");
		RESContactEmail = new JLabel("Contact Email");
		RESContactPhoneNumber = new JLabel("Contact Phone");
		
		RESTimeField = new JTextField("");
		RESNumberInPartyField = new JTextField("");
		RESContactNameField = new JTextField("");
		RESContactEmailField = new JTextField("");
		RESContactPhoneNumberField = new JTextField("");
		
		RESMakeReservation = new JButton("Make Reservation");
			
		SqlDateModel model = new SqlDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		RESDateCalendar = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		
		RESSelectTableMenu = new JPanel();
		RESSelectTableMenuScroll = new JScrollPane(RESSelectTableMenu);
		RESSelectTableMenuScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		REStables = new ArrayList<Table>();
		
		//View Menu SubMenu SubMenu
		appetizerMenu  = new JPanel();
		mainMenu  = new JPanel();
		dessertMenu  = new JPanel();
		alcoholicBeverageMenu = new JPanel();
		nonAlcoholicBeverageMenu  = new JPanel();
		
		//View Order SubMenu
		viewOrderSelectTable = new JLabel("Select Table");
		viewOrderLabel = new JLabel("Order");
		viewOrderButton = new JButton("View Order");
		viewOrderTableList = new JComboBox<String>(new String[0]);
		viewOrderTableList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {				
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
		        selectedTable3 = cb.getSelectedIndex();
			}
		});
		viewOrderList = new JTable();
		viewOrderScrollPane = new JScrollPane(viewOrderList);
		this.add(viewOrderScrollPane);
		Dimension d = viewOrderList.getPreferredSize();
		viewOrderScrollPane.setPreferredSize(new Dimension(d.width, HEIGHT_OVERVIEW_TABLE));
		viewOrderScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		viewOrderDtm = new DefaultTableModel(0, 0);
		viewOrderDtm.setColumnIdentifiers(viewOrderListColumnNames);
		viewOrderList.setModel(viewOrderDtm);
		viewOrderList.setShowGrid(true);
		
		//Issue Bill SubMenu
		issueBillSelectOrderLabel = new JLabel("Select order:");
		issueBillSelectOrder = new JComboBox<String>(new String[0]);
		issueBillSelectOrder.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
				selectedOrder = cb.getSelectedIndex();
				refreshIssueBillSelectSeats();
			}
		});
		issueBillSeats = new ArrayList<Seat>();
		
		issueBillSelectSeat = new JPanel();
		issueBillSelectSeatScroll = new JScrollPane(issueBillSelectSeat);
		issueBillSelectSeatScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		issueBillSelectTableLabel = new JLabel("Select table:");
		issueBillSelectTable = new JComboBox<String>(new String[0]);
		issueBillSelectTable.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
				selectedTable4 = cb.getSelectedIndex();
			}
		});
		
		issueBillCreate = new JButton("Issue bill");
		issueBillEntireTable = new JButton("Issue bill for entire table");
		issueBillEachSeat = new JButton("Issue bill for each seat");
		
		issueBillSelectBillLabel = new JLabel("Select bill:");
		issueBillSelectBill = new JComboBox<String>(new String[0]);
		issueBillSelectBill.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
				selectedBill = cb.getSelectedIndex();
			}
		});
		
		viewBillTable = new JTable();
		viewBillScrollPane = new JScrollPane(viewBillTable);
		this.add(viewBillScrollPane);
		d = viewBillTable.getPreferredSize();
		viewBillScrollPane.setPreferredSize(new Dimension(d.width, HEIGHT_OVERVIEW_TABLE));
		viewBillScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		viewBillDtm = new DefaultTableModel(0, 0);
		viewBillDtm.setColumnIdentifiers(viewBillColumnNames);
		viewBillTable.setModel(viewBillDtm);
		viewBillTable.setShowGrid(true);
		
		billTotalOwedLabel = new JLabel("Total amount owed:");
		billTotalOwed = new JLabel("");
		
		refreshBillTable = new JButton("Refresh table");
		
		//Order Menu Item SubMenu
		OISelectTableLabel = new JLabel("Select Order");
		OISelectOrderList = new JComboBox<String>(new String[0]);
		OISelectOrderList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
				OIselectedOrder = cb.getSelectedIndex();
			}
		});
		OIupdateSeatDisplayButton = new JButton("Update Seat Display");
		
		OISelectSeatsLabel = new JLabel("Select Seat(s)");
		OISelectSeatsMenu = new JPanel();
		OISelectSeatsMenuScroll = new JScrollPane(OISelectSeatsMenu);
		OISelectSeatsMenuScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		OIseats = new ArrayList<Seat>();
		
		OISelectMenuItemLabel = new JLabel("Select Menu Item");
		OISelectMenuItemList = new JComboBox<String>(new String[0]);
		OISelectMenuItemList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
				OIselectedMenuItem = cb.getSelectedIndex();
			}
		});
		OImenuItems = new ArrayList<MenuItem>(); 
		
		OIQuantityLabel = new JLabel("Quantity");
		OIQuantityField = new JTextField("");
		
		OIButton = new JButton("Order Item");
		
		//Cancel Order SubMenu
		COSelectTableLabel = new JLabel("Select Table");
		COSelectTableList = new JComboBox<String>(new String[0]);
		COSelectTableList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
				COselectedTable = cb.getSelectedIndex();
			}
		});
		COupdateSeatDisplayButton = new JButton("Update Seat Display");
		
		COSelectSeatLabel = new JLabel("Select Seat");
		COSelectSeatList = new JComboBox<String>(new String[0]);
		COSelectSeatList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
				COselectedSeat = cb.getSelectedIndex();
			}
		});
		COupdateOrderItemDisplayButton = new JButton("Update Order Item Display");
		
		COSelectOrderItemLabel = new JLabel("Select Item");
		COSelectOrderItemList = new JComboBox<String>(new String[0]);
		COSelectOrderItemList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
				COselectedOrderItem = cb.getSelectedIndex();
			}
		});
		
		COseats = new ArrayList<Seat>();
		COorderItems = new ArrayList<OrderItem>();
		
		COcancelOrderButton = new JButton("Cancel order");
		COcancelOrderItemButton = new JButton("Cancel item");

		//Update Menu SubMenu
		UMSelectMenuItemLabel = new JLabel("Select Menu Item");
		UMSelectMenuItemList = new JComboBox<String>(new String[0]);
		UMSelectMenuItemList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
				UMselectedMenuItem = cb.getSelectedIndex();
			}
		});
		UMmenuItems = new ArrayList<MenuItem>();
		
		UMSelectItemCategoryLabel = new JLabel("Select Item Category");
		UMSelectItemCategoryList = new JComboBox<String>(new String[0]);
		UMSelectItemCategoryList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
				UMselectedItemCategory = cb.getSelectedIndex();
			}
		});
		
		UMnameLabel = new JLabel("Name");
		UMpriceLabel = new JLabel("Price");
		UMnameField = new JTextField("");
		UMpriceField = new JTextField("");
		
		UMremoveMenuItemButton = new JButton("Remove");
		UMaddMenuItemButton = new JButton("Add Menu Item");
		UMupdateMenuItemButton = new JButton("Update Menu Item");
		
		//Highchair portion
		HCQuantityField = new JTextField("");
		HCQuantity = new JLabel("Amount (max 3)");
		HCAddHighChair = new JComboBox<String>(new String[0]);
		HCAddHighChair.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
				HCSelectedHighChair = cb.getSelectedIndex();
			}
		});
		HCConfirm = new JButton("Add High Chair(s)");
		
		/*Action Listeners*/
		//Menu Button
		menu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				returnToMainMenu();
				viewMenu.setVisible(true);
				updateMenu.setVisible(true);
				refreshData();
			}
		});
		
		//View Menu Button
		viewMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeUpdateMenuSubmenu();
				removeViewMenuSubMenuSubMenu();
				appetizer.setVisible(true);
				main.setVisible(true);
				dessert.setVisible(true);
				alcoholicBeverage.setVisible(true);
				nonAlcoholicBeverage.setVisible(true);
				refreshData();
			}
		});
		
		//Update Menu Button
		updateMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeViewMenuSubMenu();
				removeViewMenuSubMenuSubMenu();
				UMSelectMenuItemLabel.setVisible(true);
				UMSelectMenuItemList.setVisible(true);
				UMSelectItemCategoryLabel.setVisible(true);
				UMSelectItemCategoryList.setVisible(true);
				UMnameLabel.setVisible(true);
				UMpriceLabel.setVisible(true);
				UMnameField.setVisible(true);
				UMpriceField.setVisible(true);
				UMremoveMenuItemButton.setVisible(true);
				UMaddMenuItemButton.setVisible(true);
				UMupdateMenuItemButton.setVisible(true);
				refreshData();
			}
		});
		//Appetizer Button
		appetizer.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeViewMenuSubMenuSubMenu();
				appetizerMenu.setVisible(true); 
				String constraint = "width=75" + " height=75"; 
				int numberOfColumns = 2;
				int numberOfRows = (int)Math.ceil(((double)RestoAppController.getItemCategories().size())/numberOfColumns);
		        GridLayout gl = new GridLayout(numberOfRows,numberOfColumns);
		        appetizerMenu.setLayout(gl);
				for (MenuItem menuitem : giveList(RestoAppController.getItemCategories().get(0))){
					JButton button;
					appetizerMenu.add(button = new JButton(menuitem.getName() + "   $" + String.valueOf(menuitem.getCurrentPricedMenuItem().getPrice())),constraint);
					button.setFont(new Font("Calibri", Font.PLAIN, 20));	
				}
				refreshData();
			}
		});
		
		//Main Button
		main.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeViewMenuSubMenuSubMenu();
				mainMenu.setVisible(true);
				String constraint = "width=75" + " height=75"; 
				int numberOfColumns = 2;
				int numberOfRows = (int)Math.ceil(((double)RestoAppController.getItemCategories().size())/numberOfColumns);
		        GridLayout gl = new GridLayout(numberOfRows,numberOfColumns);
		        mainMenu.setLayout(gl);
				for (MenuItem menuitem : giveList(RestoAppController.getItemCategories().get(1))){
					JButton button;
					mainMenu.add(button = new JButton(menuitem.getName() + "   $" + String.valueOf(menuitem.getCurrentPricedMenuItem().getPrice())),constraint);
					button.setFont(new Font("Calibri", Font.PLAIN, 20));	
				}
				refreshData();
			}
		});
		
		//Dessert Button
		dessert.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeViewMenuSubMenuSubMenu();
				dessertMenu.setVisible(true);
				String constraint = "width=75" + " height=75"; 
				int numberOfColumns = 2;
				int numberOfRows = (int)Math.ceil(((double)RestoAppController.getItemCategories().size())/numberOfColumns);
		        GridLayout gl = new GridLayout(numberOfRows,numberOfColumns);
		        dessertMenu.setLayout(gl);
				for (MenuItem menuitem : giveList(RestoAppController.getItemCategories().get(2))){
					JButton button;
					dessertMenu.add(button = new JButton(menuitem.getName() + "   $" + String.valueOf(menuitem.getCurrentPricedMenuItem().getPrice())),constraint);
					button.setFont(new Font("Calibri", Font.PLAIN, 20));	
				}
				refreshData();
			}
		});
		
		//Alcoholic Beverage Button
		alcoholicBeverage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeViewMenuSubMenuSubMenu();
				alcoholicBeverageMenu.setVisible(true);
				String constraint = "width=75" + " height=75"; 
				int numberOfColumns = 2;
				int numberOfRows = (int)Math.ceil(((double)RestoAppController.getItemCategories().size())/numberOfColumns);
		        GridLayout gl = new GridLayout(numberOfRows,numberOfColumns);
		        alcoholicBeverageMenu.setLayout(gl);
				for (MenuItem menuitem : giveList(RestoAppController.getItemCategories().get(3))){
					JButton button;
					alcoholicBeverageMenu.add(button = new JButton(menuitem.getName() + "   $" + String.valueOf(menuitem.getCurrentPricedMenuItem().getPrice())),constraint);
					button.setFont(new Font("Calibri", Font.PLAIN, 20));	
				}
				refreshData();
			}
		});
		
		//Non-Alcoholic Beverage Button
		nonAlcoholicBeverage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeViewMenuSubMenuSubMenu();
				nonAlcoholicBeverageMenu.setVisible(true);
				String constraint = "width=75" + " height=75"; 
				int numberOfColumns = 2;
				int numberOfRows = (int)Math.ceil(((double)RestoAppController.getItemCategories().size())/numberOfColumns);
		        GridLayout gl = new GridLayout(numberOfRows,numberOfColumns);
		        nonAlcoholicBeverageMenu.setLayout(gl);
				for (MenuItem menuitem : giveList(RestoAppController.getItemCategories().get(4))){
					JButton button;
					nonAlcoholicBeverageMenu.add(button = new JButton(menuitem.getName() + "   $" + String.valueOf(menuitem.getCurrentPricedMenuItem().getPrice())),constraint);
					button.setFont(new Font("Calibri", Font.PLAIN, 20));	
				}
				refreshData();
			}
		});
		
		//Restaurant Layout Button
		restaurantLayout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				returnToMainMenu();
				addTable.setVisible(true);
				removeTable.setVisible(true);
				updateTable.setVisible(true);
				changeTableLocation.setVisible(true);
				refreshData();
			}
		});
		
		//Add Table Button
		addTable.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeUpdateTableSubmenu();
				removeMoveTableSubmenu();
				removeRemoveTableSubmenu();
				
				error = "";
				
				tableNumber.setVisible(true);
				xCoord.setVisible(true);
				yCoord.setVisible(true);
				tableWidth.setVisible(true);
				tableLength.setVisible(true);
				numOfSeats.setVisible(true);
				
				tableNumberField.setVisible(true);
				xCoordField.setVisible(true);
				yCoordField.setVisible(true);
				tableWidthField.setVisible(true);
				tableLengthField.setVisible(true);
				numOfSeatsField.setVisible(true);
				
				addTableConfirm.setVisible(true);
				
				refreshData();
			}
		});
		
		//Add Table Confirm Button
		addTableConfirm.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				String tableNumber = tableNumberField.getText();
				String xCoord = xCoordField.getText();
				String yCoord = yCoordField.getText();
				String tableWidth = tableWidthField.getText();
				String tableLength = tableLengthField.getText();
				String numOfSeats = numOfSeatsField.getText();
				if (error.length() == 0) {
					try {
						RestoAppController.createTable(tableNumber, xCoord, yCoord,
															tableWidth, tableLength, numOfSeats);
						
					} catch (InvalidInputException e) {
					// TODO Auto-generated catch block
						error = e.getMessage();
						errorMessage.setText(error);
					}
				}
				refreshData();
			}
		});
		
		//Remove Table Button
		removeTable.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeAddTableSubmenu();
				removeUpdateTableSubmenu();
				removeMoveTableSubmenu();
				
				error = "";
				
				selectTableRemoveTable.setVisible(true);
				selectTableRemoveTableLabel.setVisible(true);
				removeTableConfirm.setVisible(true);
				
				refreshData();
			}
		});
		
		//Remove Table Confirm Button
		removeTableConfirm.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				
				if(restoLayout.getSelectedTable() == tables.get(selectedTable2))
					restoLayout.setSelectedTable(null);
				try {
					RestoAppController.removeTable(tables.get(selectedTable2));
				}
				catch (InvalidInputException e){
					error = e.getMessage();
					errorMessage.setText(error);
				}
				
				refreshData();
			}
		});
		
		//Update Table Button
		updateTable.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeAddTableSubmenu();
				removeMoveTableSubmenu();
				removeRemoveTableSubmenu();
				
				error = "";
				
				selectTableUpdateTable.setVisible(true);
				tableNumberLabel.setVisible(true);
				newTableNumber.setVisible(true);
				setTableNumber.setVisible(true);
				addSeat.setVisible(true);
				removeSeat.setVisible(true);
				selectTableUpdateTableLabel.setVisible(true);
				updateTableNumberLabel.setVisible(true);
				changeNumSeatsLabel.setVisible(true);
				
				refreshData();
			}
		});
		
		//Set Table Number Button
		setTableNumber.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				
				if (error.length() == 0) {
					try {
						RestoAppController.setTableNumber(tables.get(selectedTable1), Integer.parseInt(newTableNumber.getText()));
						
					} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
						error = "Please provide non-empty numerical input for the table number.";
						errorMessage.setText(error);
					} catch (InvalidInputException e) {
					// TODO Auto-generated catch block
						error = e.getMessage();
						errorMessage.setText(error);
					}
				}
				
				refreshData();
			}
		});
		
		//Add Seat Button
		addSeat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				
				if (error.length() == 0) {
					try {
						RestoAppController.addSeat(tables.get(selectedTable1));
						
					} catch (InvalidInputException e) {
					// TODO Auto-generated catch block
						error = e.getMessage();
						errorMessage.setText(error);
					}
				}
				
				refreshData();
			}
		});
		
		//Remove Seat Button
		removeSeat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				
				if (error.length() == 0) {
					try {
						RestoAppController.removeSeat(tables.get(selectedTable1));
						
					} catch (InvalidInputException e) {
					// TODO Auto-generated catch block
						error = e.getMessage();
						errorMessage.setText(error);
					}
				}
				
				refreshData();
			}
		});
		
		//Change Table Location Button
		changeTableLocation.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeAddTableSubmenu();
				removeUpdateTableSubmenu();
				removeRemoveTableSubmenu();
				
				error = "";
				
				moveTable.setVisible(true);
				xCoord2.setVisible(true);
				yCoord2.setVisible(true);
				
				tableList.setVisible(true);
				xCoordField2.setVisible(true);
				yCoordField2.setVisible(true);
				
				changeTableLocationConfirm.setVisible(true);
				
				refreshData();
			}
		});
		
		//Change Table Location Confirm Button
		changeTableLocationConfirm.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				
				if (error.length() == 0) {
					try {
						int xCoord = Integer.parseInt(xCoordField2.getText());
						int yCoord = Integer.parseInt(yCoordField2.getText());
						RestoAppController.moveTable(tables.get(selectedTable),xCoord, yCoord);
						
					}
					catch (NumberFormatException e) {
					// TODO Auto-generated catch block
						error = "Please provide non-empty numerical input for table coordinates.";
						errorMessage.setText(error);
					}
					catch (InvalidInputException e) {
					// TODO Auto-generated catch block
						error = e.getMessage();
						errorMessage.setText(error);
					}
				}
				refreshData();
			}
		});
		
		//Reservation Button
		reservation.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				returnToMainMenu();
				reserveTable.setVisible(true);
				highChairOptions.setVisible(true);
				refreshData();
			}
		});
		
		//Reserve Table Button
		reserveTable.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeHighChairOptionsSubMenu();
				RESSelectTable.setVisible(true);
				RESDate.setVisible(true);
				RESTime.setVisible(true);
				RESNumberInParty.setVisible(true);
				RESContactName.setVisible(true);
				RESContactEmail.setVisible(true);
				RESContactPhoneNumber.setVisible(true);
				
				RESTimeField.setVisible(true);
				RESNumberInPartyField.setVisible(true);
				RESContactNameField.setVisible(true);
				RESContactEmailField.setVisible(true);
				RESContactPhoneNumberField.setVisible(true);
				
				RESMakeReservation.setVisible(true);
				RESDateCalendar.setVisible(true);
				RESSelectTableMenuScroll.setVisible(true);

				refreshData();
			}
		});
		
		//Make Reservation Button
		RESMakeReservation.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				if (error.length() == 0) {
					Date date = (Date) RESDateCalendar.getModel().getValue();
					date = cleanDate(date);
					String time = RESTimeField.getText();
					String numberInPartyString = RESNumberInPartyField.getText();
					String name = RESContactNameField.getText();
					String email = RESContactEmailField.getText();
					String phoneNumber = RESContactPhoneNumberField.getText();
					if (time.equals("") || numberInPartyString.equals("")) {
						error = "Every field must be filled";
						errorMessage.setText(error);
					} else {
						try {
							DateFormat formatter = new SimpleDateFormat("hh:mm");
							formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
							java.util.Date testDate = formatter.parse(time);
							Time newTime = new Time(testDate.getTime() + date.getTime());
							int numberInParty = Integer.parseInt(RESNumberInPartyField.getText()); 
							RestoAppController.reserveTable(date, newTime, numberInParty, name, email, phoneNumber, REStables);
							
						} catch (InvalidInputException e) {
						// TODO Auto-generated catch block
							error = e.getMessage();
							errorMessage.setText(error);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
				refreshData();
			}
		});	
		
		//Change Table Status Button
		changeTableStatus.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				returnToMainMenu();
				error = "";
				
				SelectGroupList.setVisible(true);
				
				CHGTABSTASelectTable.setVisible(true);
				CHGTABSTASelectGroup.setVisible(true);
				
				CHGTABSTAAssignTables.setVisible(true);
				CHGTABSTARemoveGroup.setVisible(true);
				
				CHGTABSTASelectTableMenu.setVisible(true);
				CHGTABSTASelectTableMenuScroll.setVisible(true);

				refreshData();
			}
		});

		//Assign Tables Button
		CHGTABSTAAssignTables.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				
				try {
					RestoAppController.startOrder(CHGTABSTAtables);
				}
				catch(InvalidInputException e) {
					error = e.getMessage();
					errorMessage.setText(error);
				}
				
				refreshData();
			}
		});
		
		//Remove Group Button
		CHGTABSTARemoveGroup.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				
				try {
					RestoAppController.endOrder(groups.get(SelectedGroup));
				}
				catch(InvalidInputException e) {
					error = e.getMessage();
					errorMessage.setText(error);
				}
				
				refreshData();
			}
		});
		
		//Order Button
		order.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				returnToMainMenu();
				orderMenuItem.setVisible(true);
				cancelOrder.setVisible(true);
				viewOrder.setVisible(true);
				refreshData();
			}
		});
		
		//View Order SubMenu Button
		viewOrder.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeOrderMenuItemSubmenu();
				removeCancelOrderSubMenu();
				viewOrderSelectTable.setVisible(true);
				viewOrderTableList.setVisible(true);
				viewOrderScrollPane.setVisible(true);
				viewOrderLabel.setVisible(true);
				viewOrderButton.setVisible(true);
				viewOrderDtm.setRowCount(0);
				refreshData();
			}
		});
		
		//View Order Button
		viewOrderButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				viewOrderDtm.setRowCount(0);
				try {
					orderItems = RestoAppController.getOrderItem(tables.get(selectedTable3));
					if(orderItems == null) {
						throw new InvalidInputException("No order item found");
					}
					for(OrderItem orderItem : orderItems) {
						String orderItemName = orderItem.getPricedMenuItem().getMenuItem().getName();
						List <Seat> seats = orderItem.getSeats();
						String seatList = "";
						for(int i = 0; i < seats.size(); i++) {
							Seat seat = seats.get(i);
							Table table = seat.getTable();
							seatList += ("#"+(table.indexOfCurrentSeat(seat)+1));
							if(i < seats.size()-1)
								seatList += ", ";
						}
						Object []obj = {orderItemName, orderItem.getQuantity(), seatList};
						viewOrderDtm.addRow(obj);
					}
					
				}
				catch(InvalidInputException e) {
					error = e.getMessage();
					errorMessage.setText(error);
				}
				
				refreshData();
			}
		});
		
		refreshBillTable.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";

				viewBillDtm.setRowCount(0);
				billTotalOwed.setText("");
				if(selectedBill >= 0) {
					Bill bill = bills.get(selectedBill);
					for(Seat seat : bill.getIssuedForSeats()) {
						Table table = seat.getTable();
						try {
							BigDecimal bd = new BigDecimal(RestoAppController.getOwedAmount(seat));
						    bd = bd.setScale(2, RoundingMode.HALF_DOWN);
							Object []obj = {(table.indexOfCurrentSeat(seat)+1), table.getNumber(), "$"+bd};
							viewBillDtm.addRow(obj);
						}catch (InvalidInputException e){
							error = e.getMessage();
							errorMessage.setText(error);
						}
					}
					double total = 0.00;
					for (Seat seat : bill.getIssuedForSeats())
						try {
							total += RestoAppController.getOwedAmount(seat);
						}catch (InvalidInputException e){
							//do nothing
						}
					BigDecimal bd = new BigDecimal(total);
				    bd = bd.setScale(2, RoundingMode.HALF_DOWN);
					billTotalOwed.setText("$"+bd);
				}
			}
		});
		
		//Issue Bill Button
		issueBill.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				returnToMainMenu();
		
				issueBillSelectTableLabel.setVisible(true);
				issueBillSelectTable.setVisible(true);
				issueBillEntireTable.setVisible(true);
				issueBillEachSeat.setVisible(true);
				issueBillSelectOrderLabel.setVisible(true);
				issueBillSelectOrder.setVisible(true);
				issueBillSelectSeatScroll.setVisible(true);
				issueBillCreate.setVisible(true);
				issueBillSelectBillLabel.setVisible(true);
				issueBillSelectBill.setVisible(true);
				viewBillScrollPane.setVisible(true);
				billTotalOwedLabel.setVisible(true);
				billTotalOwed.setVisible(true);
				refreshBillTable.setVisible(true);
				
				refreshData();
			}
		});
		
		//create new bill button
		issueBillCreate.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				
				try {
					RestoAppController.issueBill(issueBillSeats);
				}
				catch (InvalidInputException e){
					error = e.getMessage();
					errorMessage.setText(error);
				}
				
				refreshData();
			}
		});
		
		//issue bill to entire table button
		issueBillEntireTable.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				
				Table table = tables.get(selectedTable4);
				if(table == null)
					errorMessage.setText("No table specified");
				else {
					List<Seat> seats = table.getSeats();
					
					try {
						RestoAppController.issueBill(seats);
					}
					catch (InvalidInputException e){
						error = e.getMessage();
						errorMessage.setText(error);
					}
				}
				refreshData();
			}
		});
		
		//issue bill to each seat button
		issueBillEachSeat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				
				Table table = tables.get(selectedTable4);
				if(table == null)
					errorMessage.setText("No table specified");
				else {
					List<Seat> seats = table.getSeats();
					
					try {
						for(Seat seat : seats) {
							List<Seat> seatList = new ArrayList<Seat>();
							seatList.add(seat);
							RestoAppController.issueBill(seatList);
						}
					}
					catch (InvalidInputException e){
						error = e.getMessage();
						errorMessage.setText(error);
					}
				}
				refreshData();
			}
		});
		
		//order menu item Button
		orderMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeViewOrderSubMenu();
				removeCancelOrderSubMenu();
				OISelectTableLabel.setVisible(true);
				OISelectOrderList.setVisible(true);
				OIupdateSeatDisplayButton.setVisible(true);
				OISelectSeatsLabel.setVisible(true);
				OISelectSeatsMenuScroll.setVisible(true);	
				OISelectMenuItemLabel.setVisible(true);
				OISelectMenuItemList.setVisible(true);
				OIQuantityLabel.setVisible(true);
				OIQuantityField.setVisible(true);
				OIButton.setVisible(true);
				refreshData();
			}
		});
		
		//Update Seat Display (order menu item submenu)
		OIupdateSeatDisplayButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (OIselectedOrder >= 0) {
					OIseats.clear();
					OISelectSeatsMenu.removeAll();
					OISelectSeatsMenu.repaint();
					int sizeY = 10;
					Order order = RestoAppController.getCurrentOrders().get(OIselectedOrder);
					for(int i = 0; i < order.numberOfTables(); i++) {
						Table table = order.getTable(i);
						for (final Seat seat : table.getCurrentSeats()){
							JCheckBox seatCheckBox2 = new JCheckBox("Seat #" + (table.indexOfCurrentSeat(seat) + 1) + 
																		" of table #" + table.getNumber());
							seatCheckBox2.addItemListener(new ItemListener() {
							    public void itemStateChanged(ItemEvent e) {
							        if(e.getStateChange() == ItemEvent.SELECTED) {
							        	OIseats.add(seat);
							        } else {
							        	OIseats.remove(seat);
							        };
							    }
							});
							OISelectSeatsMenu.add(seatCheckBox2);
							OISelectSeatsMenu.setPreferredSize(new Dimension(500, sizeY));
							sizeY += 10;
						}
					}
				}
				refreshData();
			}
		});
		
		//order menu item confirm button
		OIButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				List<MenuItem> menuItems = RestoAppController.getMenuItems();
				for (MenuItem menuItem : menuItems) {
					if (menuItem.hasCurrentPricedMenuItem()) {
						OImenuItems.add(menuItem);
					}
				}
				String quantityText = OIQuantityField.getText();
				if (!quantityText.equals("")) {
					if (OIselectedMenuItem == -1) {
						error = "Must select a Menu Item";
						errorMessage.setText(error);
					}else {
						int quantity = Integer.parseInt(quantityText);
						try {
							RestoAppController.orderMenuItem(OImenuItems.get(OIselectedMenuItem),quantity, OIseats);
						} catch (InvalidInputException e) {
							// TODO Auto-generated catch block
							error = e.getMessage();
							errorMessage.setText(error);
						}
					}
				}else {
					error = "Must indicate a quantity";
					errorMessage.setText(error);
				}
				OIseats.clear();
				OISelectSeatsMenu.removeAll();
				OISelectSeatsMenu.repaint();
				refreshData();
			}
		});
		
		//remove menu item
		UMremoveMenuItemButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				List<MenuItem> menuItems = RestoAppController.getMenuItems();
				for (MenuItem menuItem : menuItems) {
					if (menuItem.hasCurrentPricedMenuItem()) {
						UMmenuItems.add(menuItem);
					}
				}
				if (UMselectedMenuItem == -1) {
					error = "Must select a Menu Item";
					errorMessage.setText(error);
				} else {
					try {
						RestoAppController.removeMenuItem(UMmenuItems.get(UMselectedMenuItem));
					} catch (InvalidInputException e) {
						// TODO Auto-generated catch block
						error = e.getMessage();
						errorMessage.setText(error);
					}
				}
				refreshData();
			}
		});
		
		//add menu item
		UMaddMenuItemButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				List<ItemCategory> itemCategories = RestoAppController.getItemCategories();
				String priceString = UMpriceField.getText();
				if (priceString.equals("")) {
					error = "Must indicate a price";
					errorMessage.setText(error);
				} else {
					if (UMselectedItemCategory == -1) {
						error = "Must select an Item Category";
						errorMessage.setText(error);	
					} else {
						String name = UMnameField.getText();
						double price = Double.parseDouble(priceString);
						price = Math.round(price * 100);
						price = price/100;
						try {
							RestoAppController.addMenuItem(name, itemCategories.get(UMselectedItemCategory),
																	price);
						} catch (InvalidInputException e) {
							// TODO Auto-generated catch block
							error = e.getMessage();
							errorMessage.setText(error);
						}
					}
				}
				refreshData();
			}
		});
		
		//update menu item
		UMupdateMenuItemButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				List<ItemCategory> itemCategories = RestoAppController.getItemCategories();
				List<MenuItem> menuItems = RestoAppController.getMenuItems();
				for (MenuItem menuItem : menuItems) {
					if (menuItem.hasCurrentPricedMenuItem()) {
						UMmenuItems.add(menuItem);
					}
				}
				String priceString = UMpriceField.getText();
				if (priceString.equals("")) {
					error = "Must indicate a price";
					errorMessage.setText(error);
				} else {
					if (UMselectedMenuItem == -1) {
						error = "Must select a Menu Item";
						errorMessage.setText(error);
					}else if (UMselectedItemCategory == -1) {
						error = "Must select an Item Category";
						errorMessage.setText(error);	
					} else {
						String name = UMnameField.getText();
						double price = Double.parseDouble(priceString);
						price = Math.round(price * 100);
						price = price/100;
						try {
							RestoAppController.updateMenuItem(UMmenuItems.get(UMselectedMenuItem), name, 
																itemCategories.get(UMselectedItemCategory), 
																price);
						} catch (InvalidInputException e) {
							// TODO Auto-generated catch block
							error = e.getMessage();
							errorMessage.setText(error);
						}
					}
				}
				refreshData();
			}
		});
		
		//Cancel order button
		cancelOrder.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeViewOrderSubMenu();
				removeOrderMenuItemSubmenu();
				COSelectTableLabel.setVisible(true);
				COSelectTableList.setVisible(true);
				COupdateSeatDisplayButton.setVisible(true);
				COSelectSeatLabel.setVisible(true);
				COSelectSeatList.setVisible(true);	
				COupdateOrderItemDisplayButton.setVisible(true);
				COSelectOrderItemLabel.setVisible(true);
				COSelectOrderItemList.setVisible(true);
				COcancelOrderButton.setVisible(true);
				COcancelOrderItemButton.setVisible(true);
				refreshData();
			}
		});
		
		COupdateSeatDisplayButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (COselectedTable >= 0) {
					COSelectSeatList.removeAllItems();
					int counter = 1;
					Table table = RestoAppController.getCurrentTables().get(COselectedTable);
					for (final Seat seat : table.getCurrentSeats()){
						COSelectSeatList.addItem("Seat #" + String.valueOf(counter));
						counter++;
					}
				}
				//int temp = COselectedTable;
				//refreshData();
				//COselectedTable = temp;
			}
			
		});
		
		
		COupdateOrderItemDisplayButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (COselectedSeat >= 0) {
					//int counter = 1;
					COSelectOrderItemList.removeAllItems();
					
					Seat seat = RestoAppController.getCurrentTables().get(COselectedTable).getSeat(COselectedSeat);
					if (seat.hasOrderItems() == true) {
					for (final OrderItem orderItem : seat.getOrderItems()){
						COSelectOrderItemList.addItem(orderItem.getPricedMenuItem().getMenuItem().getName() + " $" + orderItem.getPricedMenuItem().getPrice()+" x "+orderItem.getQuantity());
					}
					}
				}
				//int temp = COselectedSeat;
				//refreshData();
				//COselectedSeat = temp;
			}
			
		});
		
		COcancelOrderItemButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				if (COselectedOrderItem == -1) {
					error = "Must select an Order Item";
					errorMessage.setText(error);
				}
				/*else {
				List<OrderItem> orderItems = RestoAppController.getCurrentTables().get(COselectedTable).getSeat(COselectedSeat).getOrderItems();
				for (OrderItem orderItem : orderItems) {
						COorderItems.add(orderItem);
				}
				}*/
				if (error.length() == 0){
					try {
						RestoAppController.cancelOrderItem(RestoAppController.getCurrentTables().get(COselectedTable).getSeat(COselectedSeat).getOrderItem(COselectedOrderItem));
						//System.out.println(COselectedOrderItem);
						//System.out.println(COorderItems.get(COselectedOrderItem).getPricedMenuItem().getMenuItem().getName());
					} catch (InvalidInputException e) {
						// TODO Auto-generated catch block
						error = e.getMessage();
						errorMessage.setText(error);
					}
				}
					refreshData();
					COSelectOrderItemList.removeAllItems();
					
			}
		});
		
		//ggg
		COcancelOrderButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				Table table = RestoAppController.getCurrentTables().get(COselectedTable);
				if (COselectedTable == -1) {
					error = "Must select a Table";
					errorMessage.setText(error);
				}
				else {
					try {
						RestoAppController.cancelOrder(table);
					} catch (InvalidInputException e) {
						// TODO Auto-generated catch block
						error = e.getMessage();
						errorMessage.setText(error);
					}
			}
				refreshData();
				COSelectOrderItemList.removeAllItems();
				
			}
		});
		
		highChairOptions.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeReserveTableSubmenu();
				HCQuantityField.setVisible(true);
				HCQuantity.setVisible(true);
				HCAddHighChair.setVisible(true);
				HCConfirm.setVisible(true);
				refreshData();
			}
		});
			
		//Restaurant Layout Display
		restoLayout = new RestoLayout(this);
		restoLayoutContainer = new JScrollPane(restoLayout);
	
		restoLayoutContainer.setPreferredSize(new Dimension(1250, 500));
		restoLayoutContainer.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		restoLayoutContainer.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		//Layout
		JSeparator horizontalLine = new JSeparator();
		Dimension verticalLineSize0 = new Dimension(0,100);
		JSeparator verticalLine0 = new JSeparator(SwingConstants.VERTICAL);
		verticalLine0.setMaximumSize(verticalLineSize0);
		JSeparator verticalLine1 = new JSeparator(SwingConstants.VERTICAL);
		verticalLine1.setMaximumSize(verticalLineSize0);
		Dimension verticalLineSize1 = new Dimension(0,150);
		JSeparator verticalLine2 = new JSeparator(SwingConstants.VERTICAL);
		verticalLine0.setMaximumSize(verticalLineSize1);
		JSeparator verticalLine3 = new JSeparator(SwingConstants.VERTICAL);
		verticalLine0.setMaximumSize(verticalLineSize1);
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				//Error
				.addComponent(errorMessage)
				//Main Menu
				.addGroup(layout.createSequentialGroup()
						.addComponent(menu,200,200,400)
						.addComponent(restaurantLayout)
						.addComponent(reservation)
						.addComponent(changeTableStatus)
						.addComponent(order)
						.addComponent(issueBill))
				.addComponent(horizontalLine)
				//Menu SubMenu
				.addGroup(layout.createSequentialGroup()
						.addComponent(viewMenu)
						.addComponent(updateMenu))
				//Restaurant Layout SubMenu
				.addGroup(layout.createSequentialGroup()
						.addComponent(addTable)
						.addComponent(removeTable)
						.addComponent(updateTable)
						.addComponent(changeTableLocation))
				//Reservation SubMenu
				.addGroup(layout.createSequentialGroup()
						.addComponent(reserveTable)
						.addComponent(highChairOptions))
				//Change Table Status SubMenu
				.addGroup(layout.createParallelGroup()
						.addComponent(CHGTABSTASelectTable,100,150,200)	
						.addGroup(layout.createSequentialGroup()
								.addComponent(CHGTABSTASelectTableMenuScroll,500,500,500)
								.addGroup(layout.createParallelGroup()
										.addGroup(layout.createSequentialGroup()
												.addComponent(CHGTABSTAAssignTables))
										.addGroup(layout.createSequentialGroup()
												.addComponent(CHGTABSTASelectGroup))
										.addGroup(layout.createSequentialGroup()
												.addComponent(SelectGroupList)
												.addComponent(CHGTABSTARemoveGroup)))))
				//Order SubMenu
				.addGroup(layout.createSequentialGroup()
						.addComponent(orderMenuItem)
						.addComponent(cancelOrder)
						.addComponent(viewOrder))
				//View Menu SubMenu
				.addGroup(layout.createSequentialGroup()
						.addComponent(appetizer)
						.addComponent(main)
						.addComponent(dessert)
						.addComponent(alcoholicBeverage)
						.addComponent(nonAlcoholicBeverage))
				//Add Table SubMenu
				.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addComponent(tableNumber,100,150,400)
								.addComponent(xCoord)
								.addComponent(yCoord)
								.addComponent(tableWidth)
								.addComponent(tableLength)
								.addComponent(numOfSeats))
						.addGroup(layout.createSequentialGroup()
								.addComponent(tableNumberField)
								.addComponent(xCoordField)
								.addComponent(yCoordField)
								.addComponent(tableWidthField)
								.addComponent(tableLengthField)
								.addComponent(numOfSeatsField))
						.addGroup(layout.createSequentialGroup()
								.addComponent(addTableConfirm)))
				//Remove Table SubMenu
				.addGroup(layout.createSequentialGroup()
						.addComponent(selectTableRemoveTableLabel)
						.addGroup(layout.createParallelGroup()
								.addComponent(selectTableRemoveTable,400,400,500)
								.addComponent(removeTableConfirm)))
				//Update Table SubMenu
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup()
								.addComponent(selectTableUpdateTableLabel)
								.addComponent(selectTableUpdateTable))
						.addComponent(verticalLine0)
						.addGroup(layout.createParallelGroup()
								.addComponent(updateTableNumberLabel)
								.addGroup(layout.createSequentialGroup()
										.addComponent(tableNumberLabel)
										.addGroup(layout.createParallelGroup()
												.addComponent(newTableNumber)
												.addComponent(setTableNumber))))
						.addComponent(verticalLine1)
						.addGroup(layout.createParallelGroup()
								.addComponent(changeNumSeatsLabel)
								.addComponent(addSeat)
								.addComponent(removeSeat)))
				//Move Table SubMenu
				.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addComponent(moveTable,200,200,400)
								.addComponent(xCoord2)
								.addComponent(yCoord2))
						.addGroup(layout.createSequentialGroup()
								.addComponent(tableList)
								.addComponent(xCoordField2)
								.addComponent(yCoordField2))
						.addGroup(layout.createSequentialGroup()
								.addComponent(changeTableLocationConfirm)))
				//Reserve Table SubMenu
				.addGroup(layout.createParallelGroup()
						.addComponent(RESSelectTable,100,150,200)	
						.addGroup(layout.createSequentialGroup()
								.addComponent(RESSelectTableMenuScroll,500,500,500)
								.addGroup(layout.createParallelGroup()
										.addGroup(layout.createSequentialGroup()
												.addComponent(RESDate)
												.addComponent(RESTime)
												.addComponent(RESNumberInParty))
										.addGroup(layout.createSequentialGroup()
												.addComponent(RESDateCalendar)
												.addComponent(RESTimeField)
												.addComponent(RESNumberInPartyField))
										.addGroup(layout.createSequentialGroup()
												.addComponent(RESContactName)
												.addComponent(RESContactEmail)
												.addComponent(RESContactPhoneNumber))
										.addGroup(layout.createSequentialGroup()
												.addComponent(RESContactNameField)
												.addComponent(RESContactEmailField)
												.addComponent(RESContactPhoneNumberField))
										.addComponent(RESMakeReservation,200,200,400))))
				//add highchair option to reservation submenu
				.addGroup(layout.createSequentialGroup()
						.addComponent(HCAddHighChair,100,150,150)
						.addGroup(layout.createParallelGroup()
								.addComponent(HCQuantity)
								.addComponent(HCQuantityField))
						.addComponent(HCConfirm))
				//View Menu SubMenu SubMenu
				.addGroup(layout.createSequentialGroup()
						.addComponent(appetizerMenu)
						.addComponent(mainMenu)
						.addComponent(dessertMenu)
						.addComponent(alcoholicBeverageMenu)
						.addComponent(nonAlcoholicBeverageMenu))
				//View Order SubMenu
				.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addComponent(viewOrderSelectTable,150,150,150)
								.addComponent(viewOrderLabel))
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup()
										.addComponent(viewOrderTableList,150,150,150)
										.addComponent(viewOrderButton))
								.addComponent(viewOrderScrollPane,300,600,600)))
				//Issue Bill SubMenu
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup()
								.addGroup(layout.createSequentialGroup()
										.addComponent(issueBillSelectTableLabel, 75, 75, 75)
										.addComponent(issueBillSelectTable, 150, 150, 150))
								.addComponent(issueBillEntireTable, 235, 235, 235)
								.addComponent(issueBillEachSeat, 235, 235, 235))
						.addComponent(verticalLine2, 1, 1, 1)
						.addGroup(layout.createParallelGroup()
								.addGroup(layout.createSequentialGroup()
										.addComponent(issueBillSelectOrderLabel, 75, 75, 75)
										.addComponent(issueBillSelectOrder, 200, 200, 200))
								.addComponent(issueBillSelectSeatScroll, 350, 350, 400)
								.addComponent(issueBillCreate, 288, 288, 288))
						.addComponent(verticalLine3, 1, 1, 1)
						.addGroup(layout.createParallelGroup()
								.addGroup(layout.createSequentialGroup()
										.addComponent(issueBillSelectBillLabel, 75, 75, 75)
										.addComponent(issueBillSelectBill, 150, 150, 150)
										.addComponent(refreshBillTable))
								.addComponent(viewBillScrollPane, 300, 400, 400)
								.addGroup(layout.createSequentialGroup()
										.addComponent(billTotalOwedLabel)
										.addComponent(billTotalOwed))))
				//Order Menu Item SubMenu
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup()
								.addComponent(OISelectTableLabel)
								.addComponent(OISelectOrderList)
								.addComponent(OIupdateSeatDisplayButton))
						.addGroup(layout.createParallelGroup()
								.addComponent(OISelectSeatsLabel)
								.addComponent(OISelectSeatsMenuScroll,630,630,630))
						.addGroup(layout.createParallelGroup()
								.addComponent(OISelectMenuItemLabel)
								.addComponent(OISelectMenuItemList)
								.addComponent(OIQuantityLabel)
								.addComponent(OIQuantityField)
								.addComponent(OIButton)))		
				//Cancel Order Submenu 
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup()
								.addComponent(COSelectTableLabel)
								.addComponent(COSelectTableList)
								.addComponent(COupdateSeatDisplayButton)
								.addComponent(COcancelOrderButton))
						.addGroup(layout.createParallelGroup()
								.addComponent(COSelectSeatLabel)
								.addComponent(COSelectSeatList)
								.addComponent(COupdateOrderItemDisplayButton))
						.addGroup(layout.createParallelGroup()
								.addComponent(COSelectOrderItemLabel)
								.addComponent(COSelectOrderItemList)
								.addComponent(COcancelOrderItemButton)))	
				//Update Menu SubMenu
				.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addComponent(UMSelectMenuItemLabel,200,200,400)
								.addComponent(UMSelectItemCategoryLabel)
								.addComponent(UMnameLabel)
								.addComponent(UMpriceLabel))								
						.addGroup(layout.createSequentialGroup()
								.addComponent(UMSelectMenuItemList)
								.addComponent(UMSelectItemCategoryList)
								.addComponent(UMnameField)
								.addComponent(UMpriceField))
						.addGroup(layout.createSequentialGroup()
								.addComponent(UMremoveMenuItemButton)
								.addComponent(UMaddMenuItemButton,600,612,612))
						.addComponent(UMupdateMenuItemButton,800,818,818))
				//Restaurant Layout
				.addComponent(restoLayoutContainer));
		
		//Menu
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {restaurantLayout, reservation, changeTableStatus, order, issueBill});
		
		//Menu SubMenus
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {menu, viewMenu, updateMenu, addTable, removeTable, updateTable, 
																				changeTableLocation, reserveTable, orderMenuItem, cancelOrder, viewOrder,
																				appetizer, main, dessert, alcoholicBeverage, nonAlcoholicBeverage});
		//AddTable
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {tableNumber, xCoord, yCoord, tableWidth, tableLength, numOfSeats,
																				tableNumberField, xCoordField, yCoordField, tableWidthField, 
																				tableLengthField, numOfSeatsField, addTableConfirm});
		//Move Table
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {moveTable,xCoord2, yCoord2, tableList, 
																				xCoordField2,yCoordField2,changeTableLocationConfirm});
		//Reserve Table
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {RESSelectTable,RESDate, RESTime, RESNumberInParty, 
																				RESDateCalendar,RESTimeField,RESNumberInPartyField,RESContactName,
																				RESContactEmail, RESContactPhoneNumber, RESContactNameField, RESContactEmailField, 
																				RESContactPhoneNumberField});
		//Change Table Status
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {CHGTABSTASelectTable, CHGTABSTAAssignTables, SelectGroupList, CHGTABSTASelectGroup, CHGTABSTARemoveGroup});
		
		//Update Menu
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {UMSelectMenuItemLabel, UMSelectItemCategoryLabel, UMnameLabel, UMpriceLabel,
																				UMSelectMenuItemList, UMSelectItemCategoryList, UMnameField, UMpriceField, UMremoveMenuItemButton});

		//View Order
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {viewOrderSelectTable, viewOrderTableList, viewOrderScrollPane, viewOrderButton, viewOrderLabel});

		//High Chair
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {HCAddHighChair, HCQuantity, HCQuantityField, HCConfirm});
				
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				//Error
				.addComponent(errorMessage)
				//Main Menu
				.addGroup(layout.createParallelGroup()
						.addComponent(menu)
						.addComponent(restaurantLayout)
						.addComponent(reservation)
						.addComponent(changeTableStatus)
						.addComponent(order)
						.addComponent(issueBill))
				.addComponent(horizontalLine)
				//Menu SubMenu
				.addGroup(layout.createParallelGroup()
						.addComponent(viewMenu)
						.addComponent(updateMenu))
				//Restaurant Layout SubMenu
				.addGroup(layout.createParallelGroup()
						.addComponent(addTable)
						.addComponent(removeTable)
						.addComponent(updateTable)
						.addComponent(changeTableLocation))
				//Reservation SubMenu
				.addGroup(layout.createParallelGroup()
						.addComponent(reserveTable)
						.addComponent(highChairOptions))
				//Change Table Status SubMenu
				.addGroup(layout.createSequentialGroup()
						.addComponent(CHGTABSTASelectTable)	
						.addGroup(layout.createParallelGroup()
								.addComponent(CHGTABSTASelectTableMenuScroll,75,75,200)
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup()
												.addComponent(CHGTABSTAAssignTables))
										.addGroup(layout.createParallelGroup()
												.addComponent(CHGTABSTASelectGroup))
										.addGroup(layout.createParallelGroup()
												.addComponent(SelectGroupList)
												.addComponent(CHGTABSTARemoveGroup)))))
				//Order SubMenu
				.addGroup(layout.createParallelGroup()
						.addComponent(orderMenuItem)
						.addComponent(cancelOrder)
						.addComponent(viewOrder))
				//View Menu SubMenu
				.addGroup(layout.createParallelGroup()
						.addComponent(appetizer)
						.addComponent(main)
						.addComponent(dessert)
						.addComponent(alcoholicBeverage)
						.addComponent(nonAlcoholicBeverage))
				//Add Table SubMenu
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup()
								.addComponent(tableNumber)
								.addComponent(xCoord)
								.addComponent(yCoord)
								.addComponent(tableWidth)
								.addComponent(tableLength)
								.addComponent(numOfSeats))
						.addGroup(layout.createParallelGroup()
								.addComponent(tableNumberField)
								.addComponent(xCoordField)
								.addComponent(yCoordField)
								.addComponent(tableWidthField)
								.addComponent(tableLengthField)
								.addComponent(numOfSeatsField))
						.addComponent(addTableConfirm))
				//Remove Table SubMenu
				.addGroup(layout.createParallelGroup()
						.addComponent(selectTableRemoveTableLabel)
						.addGroup(layout.createSequentialGroup()
								.addComponent(selectTableRemoveTable)
								.addComponent(removeTableConfirm)))
				//Update Table SubMenu
				.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addComponent(selectTableUpdateTableLabel)
								.addComponent(selectTableUpdateTable))
						.addComponent(verticalLine0)
						.addGroup(layout.createSequentialGroup()
								.addComponent(updateTableNumberLabel)
								.addGroup(layout.createParallelGroup()
										.addComponent(tableNumberLabel)
										.addGroup(layout.createSequentialGroup()
												.addComponent(newTableNumber)
												.addComponent(setTableNumber))))
						.addComponent(verticalLine1)
						.addGroup(layout.createSequentialGroup()
								.addComponent(changeNumSeatsLabel)
								.addComponent(addSeat)
								.addComponent(removeSeat)))
				//Move Table SubMenu
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup()
								.addComponent(moveTable)
								.addComponent(xCoord2)
								.addComponent(yCoord2))
						.addGroup(layout.createParallelGroup()
								.addComponent(tableList)
								.addComponent(xCoordField2)
								.addComponent(yCoordField2))
						.addComponent(changeTableLocationConfirm))
				//Reserve Table SubMenu
				.addGroup(layout.createSequentialGroup()
						.addComponent(RESSelectTable)	
						.addGroup(layout.createParallelGroup()
								.addComponent(RESSelectTableMenuScroll,75,75,200)
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup()
												.addComponent(RESDate)
												.addComponent(RESTime)
												.addComponent(RESNumberInParty))
										.addGroup(layout.createParallelGroup()
												.addComponent(RESDateCalendar)
												.addComponent(RESTimeField)
												.addComponent(RESNumberInPartyField))
										.addGroup(layout.createParallelGroup()
												.addComponent(RESContactName)
												.addComponent(RESContactEmail)
												.addComponent(RESContactPhoneNumber))
										.addGroup(layout.createParallelGroup()
												.addComponent(RESContactNameField)
												.addComponent(RESContactEmailField)
												.addComponent(RESContactPhoneNumberField))
										.addComponent(RESMakeReservation))))
				//View Menu SubMenu SubMenu
				.addGroup(layout.createParallelGroup()
						.addComponent(appetizerMenu)
						.addComponent(mainMenu)
						.addComponent(dessertMenu)
						.addComponent(alcoholicBeverageMenu)
						.addComponent(nonAlcoholicBeverageMenu))
				//View Order SubMenu
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup()
								.addComponent(viewOrderSelectTable)
								.addComponent(viewOrderLabel))
						.addGroup(layout.createParallelGroup()
								.addGroup(layout.createSequentialGroup()
										.addComponent(viewOrderTableList,20,20,30)
										.addComponent(viewOrderButton))
								.addComponent(viewOrderScrollPane)))
				//Issue Bill SubMenu
				.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup()
										.addComponent(issueBillSelectTableLabel)
										.addComponent(issueBillSelectTable, 26, 26, 26))
								.addComponent(issueBillEntireTable)
								.addComponent(issueBillEachSeat))
						.addComponent(verticalLine2)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup()
										.addComponent(issueBillSelectOrderLabel)
										.addComponent(issueBillSelectOrder, 26, 26, 26))
								.addComponent(issueBillSelectSeatScroll,75,75,200)
								.addComponent(issueBillCreate))
						.addComponent(verticalLine3)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup()
										.addComponent(issueBillSelectBillLabel)
										.addComponent(issueBillSelectBill)
										.addComponent(refreshBillTable))
								.addComponent(viewBillScrollPane, 130, 130, 130)
								.addGroup(layout.createParallelGroup()
										.addComponent(billTotalOwedLabel)
										.addComponent(billTotalOwed))))
				//Order Menu Item SubMenu
				.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addComponent(OISelectTableLabel)
								.addComponent(OISelectOrderList,20,20,30)
								.addComponent(OIupdateSeatDisplayButton))
						.addGroup(layout.createSequentialGroup()
								.addComponent(OISelectSeatsLabel)
								.addComponent(OISelectSeatsMenuScroll,75,75,200))
						.addGroup(layout.createSequentialGroup()
								.addComponent(OISelectMenuItemLabel)
								.addComponent(OISelectMenuItemList)
								.addComponent(OIQuantityLabel)
								.addComponent(OIQuantityField)
								.addComponent(OIButton)))
				//Cancel Order SubMenu
				.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addComponent(COSelectTableLabel)
								.addComponent(COSelectTableList)
								.addComponent(COupdateSeatDisplayButton)
								.addComponent(COcancelOrderButton))
						.addGroup(layout.createSequentialGroup()
								.addComponent(COSelectSeatLabel)
								.addComponent(COSelectSeatList)
								.addComponent(COupdateOrderItemDisplayButton))
						.addGroup(layout.createSequentialGroup()
								.addComponent(COSelectOrderItemLabel)
								.addComponent(COSelectOrderItemList)
								.addComponent(COcancelOrderItemButton)))
				//Update Menu SubMenu
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup()
								.addComponent(UMSelectMenuItemLabel)
								.addComponent(UMSelectItemCategoryLabel)
								.addComponent(UMnameLabel)
								.addComponent(UMpriceLabel))								
						.addGroup(layout.createParallelGroup()
								.addComponent(UMSelectMenuItemList)
								.addComponent(UMSelectItemCategoryList)
								.addComponent(UMnameField)
								.addComponent(UMpriceField))
						.addGroup(layout.createParallelGroup()
								.addComponent(UMremoveMenuItemButton)
								.addComponent(UMaddMenuItemButton))
						.addComponent(UMupdateMenuItemButton))
				//add highchair option to reservation submenu
				.addGroup(layout.createParallelGroup()
						.addComponent(HCAddHighChair,20,20,30)
						.addGroup(layout.createSequentialGroup()
								.addComponent(HCQuantity)
								.addComponent(HCQuantityField))
						.addComponent(HCConfirm))
				//Restaurant Layout
				.addComponent(restoLayoutContainer)
		);
	}
	
	//Main Menu
	private void returnToMainMenu() {
		
		error = "";
		errorMessage.setText(error);

		removeMenuSubMenu();
		removeRestaurantLayoutSubmenu();
		removeReservationSubmenu();
		removeChangeTableStatusSubmenu();
		removeOrderSubMenu();
		
		removeViewMenuSubMenu();
		
		removeAddTableSubmenu();
		removeRemoveTableSubmenu();
		removeUpdateTableSubmenu();
		removeMoveTableSubmenu();
		
		removeReserveTableSubmenu();
		
		removeViewMenuSubMenuSubMenu();
		
		removeViewOrderSubMenu();
		
		removeIssueBillSubMenu();
		
		removeOrderMenuItemSubmenu();
		removeUpdateMenuSubmenu();
		
		removeCancelOrderSubMenu();
		removeHighChairOptionsSubMenu();
		
		pack();
	}
	
	//Menu SubMenu
	private void removeMenuSubMenu() {
		viewMenu.setVisible(false);
		updateMenu.setVisible(false);
	}
	
	//Restaurant Layout SubMenu	
	private void removeRestaurantLayoutSubmenu() {
		addTable.setVisible(false);
		removeTable.setVisible(false);
		updateTable.setVisible(false);
		changeTableLocation.setVisible(false);
	}
	
	//Reservation SubMenu
	private void removeReservationSubmenu() {
		reserveTable.setVisible(false);
		highChairOptions.setVisible(false);
	}
	
	//Change Table Status SubMenu
	private void removeChangeTableStatusSubmenu() {
		SelectGroupList.setVisible(false);
		
		CHGTABSTASelectTable.setVisible(false);
		CHGTABSTASelectGroup.setVisible(false);
		
		CHGTABSTAAssignTables.setVisible(false);
		CHGTABSTARemoveGroup.setVisible(false);
		
		CHGTABSTASelectTableMenu.setVisible(false);
		CHGTABSTASelectTableMenuScroll.setVisible(false);
	}
	
	//Order SubMenu
	private void removeOrderSubMenu() {
		orderMenuItem.setVisible(false);
		cancelOrder.setVisible(false);
		viewOrder.setVisible(false);
	}
	
	//View Menu SubMenu
	private void removeViewMenuSubMenu() {
		appetizer.setVisible(false);
		main.setVisible(false);
		dessert.setVisible(false);
		alcoholicBeverage.setVisible(false);
		nonAlcoholicBeverage.setVisible(false);
	}
	
	//Add Table SubMenu
	private void removeAddTableSubmenu() {
		tableNumber.setVisible(false);
		xCoord.setVisible(false);
		yCoord.setVisible(false);
		tableWidth.setVisible(false);
		tableLength.setVisible(false);
		numOfSeats.setVisible(false);
		
		tableNumberField.setVisible(false);
		xCoordField.setVisible(false);
		yCoordField.setVisible(false);
		tableWidthField.setVisible(false);
		tableLengthField.setVisible(false);
		numOfSeatsField.setVisible(false);
		
		addTableConfirm.setVisible(false);
	}
	
	//Remove Table SubMenu
	private void removeRemoveTableSubmenu() {
		selectTableRemoveTable.setVisible(false);
		selectTableRemoveTableLabel.setVisible(false);
		removeTableConfirm.setVisible(false);
	}
	
	//Update Table SubMenu
	private void removeUpdateTableSubmenu() {
		selectTableUpdateTable.setVisible(false);
		tableNumberLabel.setVisible(false);
		newTableNumber.setVisible(false);
		setTableNumber.setVisible(false);
		addSeat.setVisible(false);
		removeSeat.setVisible(false);
		selectTableUpdateTableLabel.setVisible(false);
		updateTableNumberLabel.setVisible(false);
		changeNumSeatsLabel.setVisible(false);
	}
	
	//Move Table SubMenu
	private void removeMoveTableSubmenu() {
		moveTable.setVisible(false);
		tableList.setVisible(false);
		changeTableLocationConfirm.setVisible(false);
		xCoord2.setVisible(false);
		yCoord2.setVisible(false);
		xCoordField2.setVisible(false);
		yCoordField2.setVisible(false);
	}
	
	//Reserve Table SubMenu
	private void removeReserveTableSubmenu() {
		RESSelectTable.setVisible(false);
		RESDate.setVisible(false);
		RESTime.setVisible(false);
		RESNumberInParty.setVisible(false);
		RESContactName.setVisible(false);
		RESContactEmail.setVisible(false);
		RESContactPhoneNumber.setVisible(false);
		
		RESTimeField.setVisible(false);
		RESNumberInPartyField.setVisible(false);
		RESContactNameField.setVisible(false);
		RESContactEmailField.setVisible(false);
		RESContactPhoneNumberField.setVisible(false);
		
		RESMakeReservation.setVisible(false);
		RESDateCalendar.setVisible(false);
		RESSelectTableMenu.removeAll();
		RESSelectTableMenuScroll.setVisible(false);
	}
	
	//View Menu SubMenu SubMenu
	private void removeViewMenuSubMenuSubMenu() {
		appetizerMenu.removeAll();
		mainMenu.removeAll();
		dessertMenu.removeAll();
		alcoholicBeverageMenu.removeAll();
		nonAlcoholicBeverageMenu.removeAll();
		
		appetizerMenu.setVisible(false);
		mainMenu.setVisible(false);
		dessertMenu.setVisible(false);
		alcoholicBeverageMenu.setVisible(false);
		nonAlcoholicBeverageMenu.setVisible(false);
	}
	//View Order SubMenu
	private void removeViewOrderSubMenu() {
		viewOrderSelectTable.setVisible(false);
		viewOrderTableList.setVisible(false);
		viewOrderScrollPane.setVisible(false);
		viewOrderLabel.setVisible(false);
		viewOrderButton.setVisible(false);
	}
	
	//Issue Bill SubMenu
	private void removeIssueBillSubMenu() {
		issueBillSelectTableLabel.setVisible(false);
		issueBillSelectTable.setVisible(false);
		issueBillEntireTable.setVisible(false);
		issueBillEachSeat.setVisible(false);
		issueBillSelectOrderLabel.setVisible(false);
		issueBillSelectOrder.setVisible(false);
		issueBillSelectSeatScroll.setVisible(false);
		issueBillCreate.setVisible(false);
		issueBillSelectBillLabel.setVisible(false);
		issueBillSelectBill.setVisible(false);
		viewBillScrollPane.setVisible(false);
		billTotalOwedLabel.setVisible(false);
		billTotalOwed.setVisible(false);
		refreshBillTable.setVisible(false);
	}
	
	//Order Menu Item SubMenu
	private void removeOrderMenuItemSubmenu() {
		OISelectTableLabel.setVisible(false);
		OISelectOrderList.setVisible(false);
		OIupdateSeatDisplayButton.setVisible(false);
		OISelectSeatsLabel.setVisible(false);
		OISelectSeatsMenu.removeAll();
		OISelectSeatsMenuScroll.setVisible(false);	
		OISelectMenuItemLabel.setVisible(false);
		OISelectMenuItemList.setVisible(false);
		OIQuantityLabel.setVisible(false);
		OIQuantityField.setVisible(false);
		OIButton.setVisible(false);
	}
	
	//Cancel Order SubMenu
	private void removeCancelOrderSubMenu() {
		COSelectTableLabel.setVisible(false);
		COSelectTableList.setVisible(false);
		COupdateSeatDisplayButton.setVisible(false);
		COcancelOrderButton.setVisible(false);
		COSelectSeatLabel.setVisible(false);
		COSelectSeatList.setVisible(false);
		COupdateOrderItemDisplayButton.setVisible(false);
		COSelectOrderItemLabel.setVisible(false);
		COSelectOrderItemList.setVisible(false);
		COcancelOrderItemButton.setVisible(false);
	}

	//Update Menu SubMenu
	private void removeUpdateMenuSubmenu() {
		UMSelectMenuItemLabel.setVisible(false);
		UMSelectMenuItemList.setVisible(false);
		UMSelectItemCategoryLabel.setVisible(false);
		UMSelectItemCategoryList.setVisible(false);
		UMnameLabel.setVisible(false);
		UMpriceLabel.setVisible(false);
		UMnameField.setVisible(false);
		UMpriceField.setVisible(false);
		UMremoveMenuItemButton.setVisible(false);
		UMaddMenuItemButton.setVisible(false);
		UMupdateMenuItemButton.setVisible(false);
	}
	
	//High Chair
	private void removeHighChairOptionsSubMenu() {
		HCQuantityField.setVisible(false);
		HCQuantity.setVisible(false);
		HCAddHighChair.setVisible(false);
		HCConfirm.setVisible(false);
	}

	
	//Refresh Data
	private void refreshData() {
		
		errorMessage.setText(error);
		
		tableNumberField.setText("");
		xCoordField.setText("");
		yCoordField.setText("");
		xCoordField2.setText("");
		yCoordField2.setText("");
		tableWidthField.setText("");
		tableLengthField.setText("");
		numOfSeatsField.setText("");
		newTableNumber.setText("");
		RESTimeField.setText("");
		RESNumberInPartyField.setText("");
		RESContactNameField.setText("");
		RESContactEmailField.setText("");
		RESContactPhoneNumberField.setText("");
		OIQuantityField.setText("");
		UMnameField.setText("");
		UMpriceField.setText("");
		OImenuItems.clear();
		UMmenuItems.clear();
		
		//update table table selector removed to prevent unwanted actionPerformed calls
		selectTableUpdateTable.removeActionListener(selectTableUpdateTableListener);
		
		//move table & update table & remove table & view order & issue bill & make order & cancel order combo box refresh (Tables)
		tables = new HashMap<Integer, Table>();
		tablesReverse = new HashMap<Table, Integer>();
		tableList.removeAllItems();
		selectTableUpdateTable.removeAllItems();
		selectTableRemoveTable.removeAllItems();
		viewOrderTableList.removeAllItems();
		issueBillSelectTable.removeAllItems();
		COSelectTableList.removeAllItems();

		Integer index = 0;
		for (Table table : RestoAppController.getCurrentTables()) {
			tables.put(index, table);
			tablesReverse.put(table, index);
			tableList.addItem("#" + table.getNumber());
			selectTableUpdateTable.addItem("#" + table.getNumber());
			selectTableRemoveTable.addItem("#" + table.getNumber());
			viewOrderTableList.addItem("#" + table.getNumber());
			issueBillSelectTable.addItem("#" + table.getNumber());
			COSelectTableList.addItem("#" + table.getNumber());
			index++;
		};
		
		//change table status and issue bill (select order and select bill) combo box refresh
		bills = new HashMap<Integer, Bill>();
		issueBillSelectBill.removeAllItems();
		groups = new HashMap<Integer, Order>();
		SelectGroupList.removeAllItems();
		issueBillSelectOrder.removeAllItems();
		OISelectOrderList.removeAllItems();
		Integer indexGroup = 0;
		for (Order order : RestoAppController.getCurrentOrders()) {
			String tablesInGroup = "";
			for (int i = 0; i<order.numberOfTables(); i++)
			{	
				Table currentTable = order.getTable(i);
				tablesInGroup += "#" + currentTable.getNumber();
				if(i < order.numberOfTables()-1)
					tablesInGroup += ", ";
			}
			
			Integer indexBill = 0;
			for(Bill bill : order.getBills()) {
				bills.put(indexBill, bill);
				
				issueBillSelectBill.addItem("Bill #" + (indexBill+1) + " of order #" + order.getNumber());
				indexBill++;
			}
			
			groups.put(indexGroup, order);
			
			SelectGroupList.addItem("#" + order.getNumber() + ": table(s) "+tablesInGroup);
			issueBillSelectOrder.addItem("#" + order.getNumber() + ": table(s) "+tablesInGroup);
			OISelectOrderList.addItem("#" + order.getNumber() + ": table(s) "+tablesInGroup);
			indexGroup++;
		}
		
		//order menu item & update menu submenu combo box refresh (MenuItems)
		OISelectMenuItemList.removeAllItems();
		UMSelectMenuItemList.removeAllItems();
		Integer MIindex = 0;
		for (MenuItem menuItem : RestoAppController.getMenuItems()) {
			if (menuItem.hasCurrentPricedMenuItem()) {
				OISelectMenuItemList.addItem(menuItem.getName() + " $" + Double.toString(menuItem.getCurrentPricedMenuItem().getPrice()));
				UMSelectMenuItemList.addItem(menuItem.getName() + " $" + Double.toString(menuItem.getCurrentPricedMenuItem().getPrice()));
			}
			MIindex++;
		};
		
		//update menu submenu combo box refresh (ItemCategories)
		UMSelectItemCategoryList.removeAllItems();
		Integer ICindex = 0;
		for (ItemCategory itemCategory : RestoAppController.getItemCategories()) {
			UMSelectItemCategoryList.addItem(itemCategory.toString());
			ICindex++;
		};
		
		//for change table status, combo box resets
		SelectedGroup = -1;
		SelectGroupList.setSelectedIndex(SelectedGroup);
		
		//for move table, combo box resets
		selectedTable = -1;
		tableList.setSelectedIndex(selectedTable);
		
		//for remove table, the combo box resets
		selectedTable2 = -1;
		selectTableRemoveTable.setSelectedIndex(selectedTable2);
		
		//for view order, combo box resets
		selectedTable3 = -1;
		viewOrderTableList.setSelectedIndex(selectedTable3);
		
		//for issue bill, combo box resets
		selectedTable4 = -1;
		issueBillSelectTable.setSelectedIndex(selectedTable4);
		selectedBill = -1;
		issueBillSelectBill.setSelectedIndex(selectedBill);
		selectedOrder = -1;
		issueBillSelectOrder.setSelectedIndex(selectedOrder);
		
		//for order menu item, combo box resets
		OIselectedOrder = -1;
		OISelectOrderList.setSelectedIndex(OIselectedOrder);
		OIselectedMenuItem = -1;
		OISelectMenuItemList.setSelectedIndex(OIselectedMenuItem);
		
		//for cancel order, combo box resets
		COselectedTable = -1;
		COSelectTableList.setSelectedIndex(COselectedTable);
		COselectedSeat = -1;
		COSelectSeatList.setSelectedIndex(COselectedSeat);
		COselectedOrderItem = -1;
		COSelectOrderItemList.setSelectedIndex(COselectedOrderItem);

		//for update menu subMenu, combo box resets
		UMselectedItemCategory = -1;
		UMSelectItemCategoryList.setSelectedIndex(UMselectedItemCategory);
		UMselectedMenuItem = -1;
		UMSelectMenuItemList.setSelectedIndex(UMselectedMenuItem);
		
		//for update table, combo box goes to table selected in restaurant layout, -1 if none selected
		Table layoutSelectedTable = restoLayout.getSelectedTable();
		if(layoutSelectedTable == null)
			selectedTable1 = -1;
		else
			selectedTable1 = tablesReverse.get(layoutSelectedTable);
		selectTableUpdateTable.setSelectedIndex(selectedTable1);
		
		//selectTableUpdateTable re-added
		selectTableUpdateTableListener = new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {				
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
				selectedTable1 = cb.getSelectedIndex();
				restoLayout.setSelectedTable(tables.get(selectedTable1));
			}
		};
		selectTableUpdateTable.addActionListener(selectTableUpdateTableListener);
		REStables.clear();
		RESDateCalendar.getModel().setValue(null);
		RESSelectTableMenu.removeAll();
		int sizeY = 10;
		for (final Table table : RestoAppController.getCurrentTables()){
			JCheckBox tableCheckBox = new JCheckBox("Table #" + String.valueOf(table.getNumber()));
			tableCheckBox.addItemListener(new ItemListener() {
			    public void itemStateChanged(ItemEvent e) {
			        if(e.getStateChange() == ItemEvent.SELECTED) {
			        	REStables.add(table);
			        } else {
			        	REStables.remove(table);
			        };
			    }
			});
			RESSelectTableMenu.add(tableCheckBox);
			RESSelectTableMenu.setPreferredSize(new Dimension(400, sizeY));
			sizeY += 8;
		}
		
		CHGTABSTAtables.clear();
		CHGTABSTASelectTableMenu.removeAll();
		int sizeY2 = 10;
		for (final Table table : RestoAppController.getCurrentTables()){
			JCheckBox tableCheckBox2 = new JCheckBox("Table #" + String.valueOf(table.getNumber()));
			tableCheckBox2.addItemListener(new ItemListener() {
			    public void itemStateChanged(ItemEvent e) {
			        if(e.getStateChange() == ItemEvent.SELECTED) {
			        	CHGTABSTAtables.add(table);
			        } else {
			        	CHGTABSTAtables.remove(table);
			        };
			    }
			});
			CHGTABSTASelectTableMenu.add(tableCheckBox2);
			CHGTABSTASelectTableMenu.setPreferredSize(new Dimension(400, sizeY2));
			sizeY2 += 8;
		}
		
		refreshIssueBillSelectSeats();
		
		restoLayout.setTables(RestoAppController.getCurrentTables());
		
		pack();

	}
	
	/*Helper Methods*/
	
	private void refreshIssueBillSelectSeats() {
		issueBillSeats.clear();
		issueBillSelectSeat.removeAll();
		int Y = 10;
		if(selectedOrder >= 0) {
			for (Table table : groups.get(selectedOrder).getTables()){
				for(final Seat seat : table.getCurrentSeats()) {
					JCheckBox seatCheckBox = new JCheckBox("Seat #" + (table.indexOfCurrentSeat(seat)+1) + " of table #"+table.getNumber());
					seatCheckBox.addItemListener(new ItemListener() {
					    public void itemStateChanged(ItemEvent e) {
					        if(e.getStateChange() == ItemEvent.SELECTED) {
					        	issueBillSeats.add(seat);
					        } else {
					        	issueBillSeats.remove(seat);
					        };
					    }
					});
					issueBillSelectSeat.add(seatCheckBox);
					issueBillSelectSeat.setPreferredSize(new Dimension(350,Y));
					Y = Y + 17;
				}
			}
		}
		repaint();
		pack();
	}
	
	void tableClicked() {
		selectedTable1 = tablesReverse.get(restoLayout.getSelectedTable());
		selectTableUpdateTable.setSelectedIndex(selectedTable1);
		selectedTable = tablesReverse.get(restoLayout.getSelectedTable());
		tableList.setSelectedIndex(selectedTable);
		selectedTable2 = tablesReverse.get(restoLayout.getSelectedTable());
		selectTableRemoveTable.setSelectedIndex(selectedTable2);
		selectedTable3 = tablesReverse.get(restoLayout.getSelectedTable());
		viewOrderTableList.setSelectedIndex(selectedTable3);
	}
	
	private List <MenuItem> giveList(ItemCategory aItemCategory) { 
		error = "";
		errorMessage.setText(error);
        List<MenuItem> listTemp = new ArrayList<MenuItem>();
        try {
        	listTemp = RestoAppController.getMenuItems(aItemCategory);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			error = e.getMessage();
			errorMessage.setText(error);
		}
		return listTemp;
	}
	
	private static Date cleanDate(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTimeInMillis(date.getTime());
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    java.util.Date tempCleanedDate = cal.getTime();
	    java.sql.Date cleanedDate = new java.sql.Date(tempCleanedDate.getTime());
	    return cleanedDate;
	}
}

	
