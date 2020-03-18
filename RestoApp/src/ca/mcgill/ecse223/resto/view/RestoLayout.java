package ca.mcgill.ecse223.resto.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.controller.RestoAppController;
import ca.mcgill.ecse223.resto.model.Order;
import ca.mcgill.ecse223.resto.model.OrderItem;
import ca.mcgill.ecse223.resto.model.Seat;
import ca.mcgill.ecse223.resto.model.Table;
import ca.mcgill.ecse223.resto.model.Table.Status;

public class RestoLayout extends JPanel {
	
	private static final long serialVersionUID = 5765666411683246454L;
	
	private List<Rectangle2D> rectangles = new ArrayList<Rectangle2D>();
	private HashMap<Rectangle2D, Table> visualTables;
	private static final int SPACING = 100;
	private List<Table> tables;
	private Table selectedTable;
	private RestoAppPage restoAppPage;
	
	public RestoLayout(RestoAppPage restoPage) {
		super();
		this.restoAppPage = restoPage;
		init();
	}

	public Table getSelectedTable() {
		return this.selectedTable;
	}
	
	public void setSelectedTable(Table table) {
		this.selectedTable = table;
		repaint();
	}
	
	private void init() {
		selectedTable = null;
		visualTables = new HashMap<Rectangle2D, Table>();
		if (RestoAppController.getCurrentTables().size() > 0) {
			this.tables = RestoAppController.getCurrentTables();
		} else
			this.tables = null;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				for (Rectangle2D rectangle : rectangles) {
					if (rectangle.contains(x, y)) {
						selectedTable = visualTables.get(rectangle);
						restoAppPage.tableClicked();
						break;
					}
				}
				repaint();
			}
		});
	}
	
	public void setTables(List<Table> tables) {
		this.tables = tables;
		visualTables = new HashMap<Rectangle2D, Table>();
		if (this.tables.size() != 0) {
			ArrayList<Integer> positionXList = new ArrayList<Integer>();
			ArrayList<Integer> positionYList = new ArrayList<Integer>();
			for (Table table : this.tables) {
				positionXList.add(table.getX());
				positionYList.add(table.getY());
			}
			int xValue = Collections.max(positionXList);
			int yValue = Collections.max(positionYList);
			for (Table table : this.tables) {
				if (table.getX() == xValue) {
					setPreferredSize(new Dimension(xValue + table.getWidth() + SPACING, getPreferredSize().height));
				}
				if (table.getY() == yValue) {
					setPreferredSize(new Dimension(getPreferredSize().width, yValue + table.getLength() + SPACING));
				}
				
			}
		}
		revalidate();
		repaint();
	}
	
	private void doDrawing(Graphics g) {
		rectangles.clear();
		visualTables.clear();
		Graphics2D g2d = (Graphics2D) g.create();
		Font currentFont = g2d.getFont();
		BasicStroke thinStroke = new BasicStroke(2);
		g2d.setStroke(thinStroke);
		g2d.setColor(Color.WHITE);
		for (Table table : tables) {
			int numberSeatsInUse = 0;
			if (table.getStatus() != Status.Available) {
				Order order = table.getOrder(table.numberOfOrders()-1);
			   //creates list of seats that are in use (and belong to relevant table)
				List<OrderItem> orderItems = order.getOrderItems();
				ArrayList<Seat> usedSeats = new ArrayList<Seat>();
				for(OrderItem orderItem : orderItems) {
					for(Seat seat : orderItem.getSeats()) {
						if(table.getCurrentSeats().contains(seat) && !usedSeats.contains(seat))
							usedSeats.add(seat);
					}
				}
				numberSeatsInUse = usedSeats.size();
			}
			Rectangle2D rectangle = new Rectangle2D.Float(table.getX(), table.getY(), table.getWidth(), table.getLength());
			rectangles.add(rectangle);
			visualTables.put(rectangle, table);
			int numberLength = (int) Math.log10(table.getNumber()) + 1;
			if (selectedTable != null && selectedTable.equals(table)) {
				g2d.setColor(Color.YELLOW);
				g2d.fill(rectangle);
				g2d.setColor(Color.BLACK);
				g2d.draw(rectangle);
				g2d.setFont(currentFont);
				int seatNumberLength = (int) Math.log10(table.getCurrentSeats().size()) + 1;
				if (table.getWidth() < (40 + (seatNumberLength - 1) * currentFont.getSize() * 3 / 4) && table.getX() < (20 + (seatNumberLength - 1) * currentFont.getSize() * 3 / 4)) {
					g2d.drawString(new Integer(numberSeatsInUse).toString() + "/" + new Integer(table.getCurrentSeats().size()).toString() + " seats", 
										(int) rectangle.getCenterX() + currentFont.getSize() * 7 / 4 + seatNumberLength * currentFont.getSize() / 3, 
										(int) rectangle.getCenterY() + currentFont.getSize() / 3);
				}else {
					g2d.drawString(new Integer(numberSeatsInUse).toString() + "/" + new Integer(table.getCurrentSeats().size()).toString() + " seats", 
										(int) rectangle.getCenterX() - currentFont.getSize() * 7 / 4 - seatNumberLength * currentFont.getSize() / 3, 
										(int) rectangle.getCenterY() + currentFont.getSize() * 4 / 2);
				}
			}else {
				if (table.getStatus() != Status.Available) {
					g2d.setColor(Color.CYAN);
					g2d.fill(rectangle);
				}else {
					g2d.setColor(Color.WHITE);
					g2d.fill(rectangle);
				}
				g2d.setColor(Color.BLACK);
				g2d.draw(rectangle);
			}
			g2d.setFont(currentFont);
			if (table.hasReservations()) {
				g2d.drawString("<reserved>", 
						(int) rectangle.getCenterX() - currentFont.getSize() * 26/10, 
						(int) rectangle.getCenterY() - currentFont.getSize() * 3 / 2);
			}
			Font newFont = currentFont.deriveFont(currentFont.getSize() * 2F);
			g2d.setFont(newFont);
			g2d.drawString(new Integer(table.getNumber()).toString(), 
					(int) rectangle.getCenterX() - numberLength * newFont.getSize() * 7 / 25, 
					(int) rectangle.getCenterY() + newFont.getSize() * 9 / 25);
		}
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}
	
}
