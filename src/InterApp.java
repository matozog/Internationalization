import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ChoiceFormat;
import java.text.DateFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import org.w3c.dom.Node;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import java.awt.Font;

public class InterApp implements ActionListener{

	private JFrame frame;
	private JTable objectTable;
	private JScrollPane scrollTable;
	private JPanel menuPanel;
	private Vector data,column;
	private JButton btnAddItem;
	private JButton btnRemoveItem;
	private JComboBox comboLanguage;
	private Locale[] locales = {Locale.US,Locale.GERMANY, Locale.FRANCE};
	private Locale locale;
	private DateFormat localDate;
	private String title, tableName, tableAmount, tablePrice, tableDateOfAdd, titlePanel, addItem, removeItem, editItem, language, managment,titleDialog;
	private ResourceBundle res;
	private JLabel lblLanguage, lblManagingBase;
	private NumberFormat currencyFormat;
	private AddDialog addDialog;
	private Elements elements;
	private JPanel picturePanel;
	private ArrayList<String> imagesList = new ArrayList<String>();
	private File[] files;
	private ArrayList<File> listFiles = new ArrayList<File>();
	private JLabel lblDolar;
	private JTextField txtEuro;
	private JLabel lblEuro;
	private JLabel lblExchangeRate;
	private double exchangeRate=0.81;
	private JButton btnSet;

	/**
	 * Create the application.
	 */
	public InterApp(){
		frame = new JFrame(title);
		frame.setBounds(100, 100, 658, 488);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		data = new Vector();
		column = new Vector();
		locale.setDefault(Locale.ENGLISH);
		
		File file = new File("Images");
		files = findFiles(file);
		
		frame.getContentPane().setLayout(null);
		
		objectTable = new JTable(data,column);
		objectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	
		scrollTable = new JScrollPane(objectTable);
		scrollTable.setBounds(221, 13, 407, 392);
		frame.getContentPane().add(scrollTable);
		

		menuPanel = new JPanel();
		menuPanel.setBounds(12, 13, 197, 281);
		menuPanel.setBorder(BorderFactory.createTitledBorder(titlePanel));
		
		frame.getContentPane().add(menuPanel);
		menuPanel.setLayout(null);
		
		btnAddItem = new JButton(addItem);
		btnAddItem.setBounds(12, 137, 173, 35);
		btnAddItem.addActionListener(this);
		menuPanel.add(btnAddItem);
		
		btnRemoveItem = new JButton(removeItem);
		btnRemoveItem.setBounds(12, 185, 173, 35);
		btnRemoveItem.addActionListener(this);
		menuPanel.add(btnRemoveItem);
		
		lblLanguage = new JLabel(language);
		lblLanguage.setBounds(63, 13, 64, 35);
		menuPanel.add(lblLanguage);
		
		comboLanguage = new JComboBox();
		comboLanguage.setBounds(12, 40, 173, 27);
		for(Locale l: locales)
			comboLanguage.addItem(l.getDisplayName());
		comboLanguage.setSelectedIndex(0);
		comboLanguage.addActionListener(this);
		menuPanel.add(comboLanguage);
		
		lblManagingBase = new JLabel(managment);
		lblManagingBase.setBounds(22, 108, 163, 16);
		menuPanel.add(lblManagingBase);
		elements= new Elements();
		
		lblExchangeRate = new JLabel("Exchange rate:");
		lblExchangeRate.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblExchangeRate.setBounds(264, 414, 129, 16);
		frame.getContentPane().add(lblExchangeRate);
		
		btnSet = new JButton("Set");
		btnSet.setBounds(516, 410, 97, 25);
		btnSet.addActionListener(this);
		frame.getContentPane().add(btnSet);
		
		currencyFormat = NumberFormat.getCurrencyInstance(locales[comboLanguage.getSelectedIndex()]);
		setCurrentLocale();

		unmarshal();
		
		JSeparator separator = new JSeparator();
		separator.setBounds(12, 93, 173, 2);
		menuPanel.add(separator);
		
		picturePanel = new JPanel(new BorderLayout());
		picturePanel.setBounds(12, 307, 197, 123);
		frame.getContentPane().add(picturePanel);
		
		lblDolar = new JLabel("$1  = ");
		lblDolar.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblDolar.setBounds(379, 414, 49, 16);
		frame.getContentPane().add(lblDolar);
		
		txtEuro = new JTextField();
		txtEuro.setBounds(422, 411, 38, 22);
		txtEuro.setText(String.valueOf(exchangeRate));
		frame.getContentPane().add(txtEuro);
		txtEuro.setColumns(10);
		
		lblEuro = new JLabel("\u20AC");
		lblEuro.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEuro.setBounds(472, 414, 49, 16);
		frame.getContentPane().add(lblEuro);
		
		addDialog = new AddDialog(locales[comboLanguage.getSelectedIndex()],this);
		
		for(File f:files)
		{
			listFiles.add(f);
		}
		objectTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(objectTable.getSelectedRow() == -1) return;
				int selected_row = objectTable.getSelectedRow();
				for(File f : listFiles)
				{
					if(f.getName().toLowerCase().equals(elements.getElements().get(selected_row).getName().toLowerCase() + ".jpg"))
					{
						loadPicture(f.getAbsolutePath().toString());
						break;
					}
					else {
						loadPicture("Images/image_not_found.jpg");
					}
				}
			}
		});
		loadPicture("Images/image_not_found.jpg");
		

	}
	
	public void loadPicture(String path)
	{
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(path));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		Image dimg = img.getScaledInstance(picturePanel.getWidth(), picturePanel.getHeight(),
		        Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(dimg);
		JLabel label = new JLabel("", image, JLabel.CENTER);
		picturePanel.removeAll();
		picturePanel.add( label, BorderLayout.CENTER );
		picturePanel.repaint();
		picturePanel.revalidate();
	}
	
	public void unmarshal()
	{
		data.clear();
		try {			
			JAXBContext jabx = JAXBContext.newInstance(Elements.class);

			Unmarshaller unmarsh = jabx.createUnmarshaller();
			elements = (Elements) unmarsh.unmarshal(new File("Database.xml"));
			if(elements.getElements()!=null)
				refreshDataTable();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String parseDate(String dateStr)
	{
		try {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = dateFormat.parse(dateStr);
		localDate = DateFormat.getDateInstance(DateFormat.SHORT,locales[comboLanguage.getSelectedIndex()]);
		String d = localDate.format(date);
		return d;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object z = e.getSource();
		if(z==btnAddItem)
		{
			addItem();
		}
		else if(z==btnRemoveItem)
		{
			removeItem();
			loadPicture("Images/image_not_found.jpg");
		}
		else if(z==comboLanguage)
		{
			setCurrentLocale();
			unmarshal();
		}
		else if(z==btnSet)
		{
			try {
				if(Double.parseDouble(txtEuro.getText())>0)
				{
					exchangeRate = Double.parseDouble(txtEuro.getText());
					refreshDataTable();
					refreshTable();
				}
			}catch(NumberFormatException ex)
			{
				JOptionPane.showMessageDialog(null,"Wrong data format!");
			}
		}
	}
	
	public File[] findFiles(File folder)
	{
		return folder.listFiles(new FilenameFilter() {public boolean accept (File folder, String filename) {return filename.endsWith(".jpg");}});
	}
	
	public void setCurrentLocale()
	{
		Locale currentLocale = locales[comboLanguage.getSelectedIndex()];
		res = ResourceBundle.getBundle("InterAppStrings", currentLocale);
		currencyFormat = NumberFormat.getCurrencyInstance(currentLocale);
		updateDisplay();
		
	}
	
	public void updateDisplay()
	{
		frame.setTitle(res.getString("title"));
		menuPanel.setBorder(BorderFactory.createTitledBorder(res.getString("titlePanel")));
		btnAddItem.setText(res.getString("addItem"));
		btnRemoveItem.setText(res.getString("removeItem"));
		lblLanguage.setText(res.getString("language"));
		lblManagingBase.setText(res.getString("management"));
		lblExchangeRate.setText(res.getString("exchangeRate"));
		btnSet.setText(res.getString("set"));
		column.clear();
		column.add(res.getString("tableName"));
		column.add(res.getString("tableAmount"));
		column.add(res.getString("tablePrice"));
		column.add(res.getString("tableDateOfAdd"));
		refreshTable();
	}
	
	public void addItem()
	{
		addDialog.setCurrentLocale(locales[comboLanguage.getSelectedIndex()]);
		addDialog.setVisible(true);
		
		
	}
	
	public void removeItem()
	{
		if(objectTable.getSelectedRowCount() != 1){ 
			  JOptionPane.showMessageDialog(null, "Item is not marked!"); 
			return;
		}
		int selected_row = objectTable.getSelectedRow();
		elements.getElements().remove(selected_row);
		marshall();
	}
	
	public Elements getElements()
	{
		return elements;
	}
	
	public void editItem()
	{
		
	}
	
	public Double getExchangeRate()
	{
		return exchangeRate;
	}
	
	public void refreshTable()
	{
	 	((DefaultTableModel) objectTable.getModel()).setDataVector(data,column);
	}
	
	public void refreshDataTable()
	{
		data.clear();
		for(Element e : elements.getElements())
		{
			Vector row = new Vector();
			String dateStr = null;
			row.add(e.getName());
			row.add(formatNum(e.getAmount()));
			if(comboLanguage.getSelectedIndex()!=0)
				row.add(currencyFormat.format(e.getPrice()*exchangeRate));
			else
				row.add(currencyFormat.format(e.getPrice()));
			dateStr=e.getDateOfAdd().trim();
			row.add(parseDate(dateStr));
			data.add(row);
		} 
	}
	
	public String formatNum(int amount)
	{
		double[] fileLimits = {0,1,2};
		String [] fileStrings = {
		    res.getString("noItems"),
		    res.getString("oneItem"),
		    res.getString("moreItems")
		};
		
		MessageFormat messageForm = new MessageFormat("");
		messageForm.setLocale(locales[comboLanguage.getSelectedIndex()]);
		String pattern = res.getString("pattern");
		messageForm.applyPattern(pattern);
		ChoiceFormat choiceForm = new ChoiceFormat(fileLimits, fileStrings);
		

		
		Format[] formats = {choiceForm, null, NumberFormat.getInstance()};
		messageForm.setFormats(formats);
		
		Object[] messageArguments = {amount};
	    String result = messageForm.format(messageArguments);
	    	    
	    return result;
	}
	
	public void marshall()
	{
	    try {
	    	JAXBContext jaxbContext = JAXBContext.newInstance(Elements.class);
		    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(elements, new File("Database.xml"));
			refreshDataTable();
			refreshTable();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<File> getFiles() {
		return listFiles;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterApp window = new InterApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
