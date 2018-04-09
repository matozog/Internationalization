import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;

import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Action;

public class AddDialog extends JDialog implements ActionListener {
	private JTextField txtName;
	private JTextField txtAmount;
	private JTextField txtPrice;
	private String titleAddDialog, buttonAdd, buttonCancel, attributs;
	private JButton btnAdd, btnCancel;
	private Locale locales[] = {Locale.US, Locale.GERMAN, Locale.FRANCE};
	private Locale currentLocale;
	private ResourceBundle res;
	private NumberFormat currencyFormat;
	private JPanel panelAtr;
	private JLabel lblName, lblAmount, lblPrice, lblPathjpg;
	private InterApp mainFrame;
	private JButton btnChooseFile;
	private JFileChooser fileChooser;
	private FileNameExtensionFilter jpgFilter;
	private boolean addedImage=false;
	/**
	 * Create the dialog.
	 */
	public AddDialog(Locale currentLocale,InterApp mainFrame) {
		setBounds(100, 100, 299, 289);
		getContentPane().setLayout(null);
		this.setLocationRelativeTo(null);
		this.currentLocale=currentLocale;
		this.mainFrame=mainFrame;
		
		jpgFilter = new FileNameExtensionFilter("JPEG file", "jpg", "jpeg");

		
		btnAdd = new JButton("Add");
		btnAdd.setBounds(12, 184, 114, 37);
		btnAdd.addActionListener(this);
		getContentPane().add(btnAdd);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(155, 184, 114, 37);
		btnCancel.addActionListener(this);
		getContentPane().add(btnCancel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(12, 169, 257, 2);
		getContentPane().add(separator);
		
		panelAtr = new JPanel();
		panelAtr.setBounds(12, 13, 257, 143);
		panelAtr.setBorder(BorderFactory.createTitledBorder("Attributes"));
		getContentPane().add(panelAtr);
		panelAtr.setLayout(null);
		
		lblName = new JLabel("Name: ");
		lblName.setBounds(12, 25, 94, 16);
		panelAtr.add(lblName);
		
		lblAmount = new JLabel("Amount:");
		lblAmount.setBounds(12, 54, 94, 16);
		panelAtr.add(lblAmount);
		
		lblPrice = new JLabel("Price:");
		lblPrice.setBounds(12, 83, 94, 16);
		panelAtr.add(lblPrice);
		
		txtName = new JTextField();
		txtName.setBounds(118, 22, 127, 22);
		panelAtr.add(txtName);
		txtName.setColumns(10);
		
		txtAmount = new JTextField();
		txtAmount.setBounds(118, 51, 127, 22);
		panelAtr.add(txtAmount);
		txtAmount.setColumns(10);
		
		txtPrice = new JTextField();
		txtPrice.setBounds(118, 80, 127, 22);
		panelAtr.add(txtPrice);
		txtPrice.setColumns(10);
		
		lblPathjpg = new JLabel("Path .jpg:");
		lblPathjpg.setBounds(12, 112, 94, 16);
		panelAtr.add(lblPathjpg);
		
		btnChooseFile = new JButton("Choose file");
		btnChooseFile.setBounds(118, 108, 127, 25);
		btnChooseFile.addActionListener(this);
		panelAtr.add(btnChooseFile);
		
		setCurrentLocale(currentLocale);
		fileChooser = new JFileChooser();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	Object z = e.getSource();
	
	if(z==btnAdd)
	{
			try {
				Element element = new Element();
				element.setName(txtName.getText());
				element.setAmount(Integer.parseInt(txtAmount.getText()));
				if(currentLocale.getCountry().equals("US"))
					element.setPrice(Float.parseFloat(txtPrice.getText()));
				else
					element.setPrice((Float.parseFloat(txtPrice.getText())*(1/mainFrame.getExchangeRate())));
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				element.setDateOfAdd(LocalDateTime.now().format(formatter));
				mainFrame.getElements().addElement(element);
				mainFrame.marshall();
				
				if(element.getName().length()!=0 && addedImage) {
					File source = fileChooser.getSelectedFile();
					File dest = new File("Images/"+txtName.getText()+".jpg");
					try {
						FileUtils.copyFile(source, dest);
						mainFrame.getFiles().add(dest);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
				this.setVisible(false);
				fileChooser.cancelSelection();
				addedImage=false;
				clearDialog();
			}catch(NumberFormatException ex)
			{
				JOptionPane.showMessageDialog(this,"Wrong data format!");
			}
		}
		else if(z==btnCancel)
		{
			clearDialog();
			this.setVisible(false);	
		}
		else if(z==btnChooseFile)
		{
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setFileFilter(jpgFilter);
			fileChooser.setAcceptAllFileFilterUsed(false);
			if(fileChooser.showOpenDialog(this)== JFileChooser.APPROVE_OPTION)
			{
				addedImage = true;

			}
			
		}
	}
	
	public void clearDialog()
	{
		for(Component control : panelAtr.getComponents())
		{
		    if(control instanceof JTextField)
		    {
		         ((JTextField) control).setText("");
		    }
		}
	}
	
	public void setCurrentLocale(Locale currentLocale)
	{
		this.currentLocale=currentLocale;
		res = ResourceBundle.getBundle("InterAppStrings", currentLocale);
		currencyFormat = NumberFormat.getCurrencyInstance(currentLocale);
		updateDisplay();
	}
	
	public void updateDisplay()
	{
		this.setTitle(res.getString("titleDialog"));
		this.panelAtr.setBorder(BorderFactory.createTitledBorder(res.getString("attributs")));
		this.btnAdd.setText(res.getString("add"));
		btnCancel.setText(res.getString("cancel"));
		lblName.setText(res.getString("tableName"));
		lblAmount.setText(res.getString("tableAmount"));
		lblPrice.setText(res.getString("tablePrice"));
		lblPathjpg.setText(res.getString("path.jpg"));
		btnChooseFile.setText(res.getString("chooseFile"));
		
	}

	public JTextField getTxtName() {
		return txtName;
	}

	public JTextField getTxtAmount() {
		return txtAmount;
	}

	public JTextField getTxtPrice() {
		return txtPrice;
	}
}
