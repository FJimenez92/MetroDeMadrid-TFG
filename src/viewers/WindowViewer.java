package viewers;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import controllers.AStarContreller;
import controllers.DataController;

import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.JScrollPane;
import java.awt.Font;

public class WindowViewer {

	private DataController data;
	private AStarContreller aStar;
	
	private JFrame frame;
	private JPanel pnlTitle;
	private JLabel lblTitle;
	private JSplitPane pnlBody;
	
	private JPanel pnlSearch;
	private JLabel lblEstacionOrigen;
	private JComboBox<String> comboBoxEstacionOrigen;
	private JLabel lblEstacionDestino;
	private JComboBox<String> comboBoxEstacionDestino;
	private JButton btnCalcular;
	
	private JPanel pnlResult;
	private JLabel lblRoute;
	private JLabel lblRouteTime;
	private JScrollPane pnlRoute;
	private JTable tableRoute;
	private DefaultTableModel modelRoute;
	private TableColumnModel colModelRoute;
	
	/**
	 * Create the application.
	 */
	public WindowViewer() {
		
	}

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		this.frame = new JFrame();
		frame.setResizable(false);
		this.frame.setBounds(100, 100, 600, 300);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.pnlTitle = new JPanel();
		this.frame.getContentPane().add(this.pnlTitle, BorderLayout.NORTH);
		
		this.lblTitle = new JLabel("Calculador de Trayectos - Metro de Madrid");
		this.pnlTitle.add(this.lblTitle);
		
		this.pnlBody = new JSplitPane();
		this.pnlBody.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.pnlBody.setResizeWeight(0.375);
		this.pnlBody.setEnabled( false );
		this.frame.getContentPane().add(this.pnlBody, BorderLayout.CENTER);
		
		this.pnlSearch = new JPanel();
		this.pnlBody.setLeftComponent(this.pnlSearch);
		this.pnlSearch.setLayout(null);
		
		this.lblEstacionOrigen = new JLabel("Origen:");
		this.lblEstacionOrigen.setBounds(30, 20, 70, 20);
		this.pnlSearch.add(this.lblEstacionOrigen);
		
		this.comboBoxEstacionOrigen = new JComboBox<String>(this.data.getListNameStation().toArray(new String[this.data.getListNameStation().size()]));
		//this.comboBoxEstacionOrigen = new JComboBox<String>();
		this.comboBoxEstacionOrigen.setBounds(100, 16, 180, 28);
		this.pnlSearch.add(this.comboBoxEstacionOrigen);
		
		this.lblEstacionDestino = new JLabel("Destino");
		this.lblEstacionDestino.setBounds(320, 20, 70, 20);
		this.pnlSearch.add(this.lblEstacionDestino);
		
		this.comboBoxEstacionDestino = new JComboBox<String>(this.data.getListNameStation().toArray(new String[this.data.getListNameStation().size()]));
		//this.comboBoxEstacionDestino = new JComboBox<String>();
		this.comboBoxEstacionDestino.setBounds(390, 16, 180, 28);
		this.pnlSearch.add(this.comboBoxEstacionDestino);
		
		this.btnCalcular = new JButton("Calcular");
		btnCalcular.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modelRoute.getDataVector().removeAllElements();
				ArrayList<String[]> route = aStar.calculate(comboBoxEstacionOrigen.getSelectedItem().toString(), comboBoxEstacionDestino.getSelectedItem().toString());
				lblRouteTime.setText(route.get(route.size()-1)[0]);
				route.remove(route.size()-1);
				for (String[] station_node : route) {
					modelRoute.addRow(station_node);
				}
			}
		});
		this.btnCalcular.setBounds(250, 50, 100, 30);
		this.pnlSearch.add(this.btnCalcular);
		
		this.pnlResult = new JPanel();
		this.pnlBody.setRightComponent(pnlResult);
		this.pnlResult.setLayout(null);
		
		this.lblRoute = new JLabel("Ruta:");
		this.lblRoute.setBounds(20, 10, 70, 20);
		this.pnlResult.add(this.lblRoute);
		
		this.pnlRoute = new JScrollPane();
		this.pnlRoute.setBounds(50, 35, 500, 100);
		this.pnlResult.add(this.pnlRoute);
		
		this.tableRoute = new JTable();
		this.tableRoute.setFillsViewportHeight(true);
		this.pnlRoute.setViewportView(this.tableRoute);
		
		this.modelRoute = new DefaultTableModel();
		this.modelRoute.addColumn("Linea");
		this.modelRoute.addColumn("Estacion");
		this.modelRoute.addColumn("Tiempo (min)");
		this.tableRoute.setModel(this.modelRoute);
		
		this.lblRouteTime = new JLabel("");
		this.lblRouteTime.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		this.lblRouteTime.setBounds(70, 10, 300, 20);
		this.pnlResult.add(this.lblRouteTime);
		
		this.colModelRoute = this.tableRoute.getColumnModel();
		this.colModelRoute.getColumn(0).setPreferredWidth(50);     //Linea
		this.colModelRoute.getColumn(1).setPreferredWidth(150);    //Estacion
		this.colModelRoute.getColumn(2).setPreferredWidth(100);    //Tiempo
	}
	
	public void setData(DataController data) {
		this.data = data;
	}

	public void setAStar(AStarContreller aStar) {
		this.aStar = aStar;
	}

	public void start(){
		this.initialize();
		this.frame.setVisible(true);
	}
}
