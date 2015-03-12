/**
 * Authors:
 *  Nihal Srivastava, IIT Delhi (nsnihal960@gmail.com)
 *  Gaurav Singh, IIT Delhi (gauravsingh0110@gmail.com) 
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License"); you may
 *    not use this file except in compliance with the License. You may obtain
 *    a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *    License for the specific language governing permissions and limitations
 *    under the License.
**/
package net.floodlightcontroller.core.myframe;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import net.floodlightcontroller.core.crawler.Crawl;
import net.floodlightcontroller.core.offdata.OFFData;
import net.floodlightcontroller.core.ofmdata.OFMData;

import java.util.ArrayList;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class myFrame {

	public JFrame frmSdnDebugger;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JLabel lblHostReachability;
	private JTextField textField_3;
	final Object lock = new Object();
	private JButton btnClearAllData;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					myFrame window = new myFrame();
					window.frmSdnDebugger.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public myFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSdnDebugger = new JFrame();
		frmSdnDebugger.setTitle("SDN Debugger");
		frmSdnDebugger.setBounds(100, 100, 635, 362);
		frmSdnDebugger.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmSdnDebugger.getContentPane().setLayout(null);
		
		JLabel lblEnterTheTime = new JLabel("Enter the time:");
		lblEnterTheTime.setBounds(5, 37, 94, 14);
		frmSdnDebugger.getContentPane().add(lblEnterTheTime);
		
		final JTextArea textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setBounds(358, 37, 242, 206);
		frmSdnDebugger.getContentPane().add(textArea);
		
		textField = new JTextField();
		textField.setBounds(141, 37, 179, 20);
		textField.setText("2410080302460");
		frmSdnDebugger.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnSubmit = new JButton("Trace");
		btnSubmit.setBounds(292, 296, 110, 23);
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Long timestamp = Long.valueOf(textField.getText());
				
				try{
				Connection con = DriverManager.getConnection("jdbc:odbc:SDN_DBG",
						"", "");
				
				Statement s1 = null;
				Statement s2 = null;
				
				ArrayList<OFFData> data = new ArrayList<OFFData>();
				ArrayList<OFMData> match = new ArrayList<OFMData>();
				
				String q1 = "SELECT * FROM FlowEntry";
				String q2 = "SELECT * FROM FLowRem";
				
				s1 = con.createStatement();
		        ResultSet rs = s1.executeQuery(q1);
		        while (rs.next()) {
		        	String str = rs.getString("Timestamp");
		        	if(timestamp > Long.valueOf(str));
		        	{ 
		        		data.add(new OFFData(
		            		rs.getString("Switch_id"),
		            		rs.getString("Match_in_Port"),
		            		rs.getString("Match_src_MAC"),
		            		rs.getString("Match_dst_MAC"),
		            		rs.getString("Match_src_IP"),
		            		rs.getString("Match_dst_IP"),
		            		rs.getString("Match_src_port"),
		            		rs.getString("Match_dst_port"),
		            		rs.getString("Match_Vlan"),
		            		rs.getString("Action_out_port"),
		            		rs.getString("Action_length"),
		            		rs.getString("Action_type"),
		            		rs.getString("Action_maxLength"),
		            		str,
		            		rs.getString("BufferID"),
		            		rs.getString("Command"),
		            		rs.getString("Cookie"),
		            		rs.getString("Flags"),
		            		rs.getString("Hard_Timeout"),
		            		rs.getString("Idle_Timeout"),
		            		rs.getString("M_out_port"),
		            		rs.getString("M_priority"),
		            		rs.getString("M_length"),
		            		rs.getString("M_type"),
		            		rs.getString("M_version"),
		            		rs.getString("M_xid"),
		            		rs.getString("Match_nw_protocol"),
		            		rs.getString("Match_nw_proto_id")		            		
		            		));
		        	}
		        }
		        	        	
		        
		        s2 = con.createStatement();
		        ResultSet rs2 = s2.executeQuery(q2);
		        while (rs2.next()) {
		        	
		        	String str2 = rs2.getString("Timestamp");
		        	if(timestamp > Long.valueOf(str2))
		        	{
		        		
				            match.add(new OFMData(
				            		rs2.getString("Switch_id"),
				            		rs2.getString("Match_in_Port"),
				            		rs2.getString("Match_src_MAC"),
				            		rs2.getString("Match_dst_MAC"),
				            		rs2.getString("Match_src_IP"),
				            		rs2.getString("Match_dst_IP"),
				            		rs2.getString("Match_src_port"),
				            		rs2.getString("Match_dst_port"),
				            		rs2.getString("Match_Vlan"),
				            		str2,
				            		rs2.getString("Match_nw_proto_id")	            		
				            		));
		        }
		        }
		       /*
		        System.out.println("########Flow in the flow table are : ");
		        for(OFFData temp:data){
		        	if(timestamp > Long.valueOf(temp.getTimestamp())) 
		        	System.out.println(temp);
		        }
		        System.out.println("########Flow in the remove table are : ");
		        for(OFMData temp:match){
		        	if(timestamp > Long.valueOf(temp.getTimestamp()))
		        	System.out.println(temp);
		        }*/
		        
		        for(int i=0;i<data.size();i++)
		        {   
		        	if(match.isEmpty())
	        			break;
		        	for(int j=0;j<match.size();j++)
		        	{	if(match.isEmpty())
		        			break;
		        		if( (match.get(j)).matches(data.get(i)) )
		        		{
		        			match.remove(j);
		        			data.remove(i);
		        			j=0;
		        		}		        				
		        	}
		        }
		        /*
		        System.out.println("########Flow in the flow table are : ");
		        
		        for(OFFData temp:data){
		        	if(timestamp > Long.valueOf(temp.getTimestamp())) 
		        	System.out.println(temp);
		        }*/		        
		        	        
		        for(OFFData t:data)
		        {
		        	 	
		        	String q3 = "INSERT INTO Result VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			        PreparedStatement preparedStatement = con.prepareStatement(q3);
			        
		        	preparedStatement.setString(1,t.Switch_id);
		        	preparedStatement.setString(2,t.Match_in_Port );
		        	preparedStatement.setString(3,t.Match_src_MAC );
		        	preparedStatement.setString(4,t.Match_dst_MAC );
		        	preparedStatement.setString(5,t.Match_src_IP );
		        	preparedStatement.setString(6,t.Match_dst_IP );
		        	preparedStatement.setString(7,t.Match_src_port );
		        	preparedStatement.setString(8,t.Match_dst_port );
		        	preparedStatement.setString(9,t.Match_Vlan );
		        	preparedStatement.setString(10,t.Action_out_port );
		        	preparedStatement.setString(11,t.Action_length ) ;
		        	preparedStatement.setString(12,t.Action_type )  ;
		        	preparedStatement.setString(13,t.Action_maxLength ) ;
		        	preparedStatement.setString(14,t.Timestamp ) ;
		        	preparedStatement.setString(15,t.BufferID );
		        	preparedStatement.setString(16,t.Command );
		        	preparedStatement.setString(17,t.Cookie );
		        	preparedStatement.setString(18,t.Flags );
		        	preparedStatement.setString(19,t.Hard_Timeout )  ;
		        	preparedStatement.setString(20,t.Idle_Timeout ) ;
		        	preparedStatement.setString(21,t.M_out_port ) ;
		        	preparedStatement.setString(22,t.M_priority ) ;
		        	preparedStatement.setString(23,t.M_length ) ;
		        	preparedStatement.setString(24,t.M_type );
		        	preparedStatement.setString(25,t.M_version );
		        	preparedStatement.setString(26,t.M_xid );
		        	preparedStatement.setString(27,t.Match_nw_protocol ) ;
		        	preparedStatement.setString(28,t.Match_nw_proto_id ) ;
		        	preparedStatement.executeUpdate();
		        	preparedStatement.close();
		        	
		        }
		        con.commit();
				con.close();
		        
		   
				
				} catch(Exception em)
					{
					System.out.println(em.getMessage());
					}
			
			/////Now call the crawler/////////////
				
				Crawl crawl = new Crawl(textField.getText(),
						textField_1.getText(),
						textField_2.getText(),
						textField_3.getText());
				synchronized(lock){
				crawl.run(textArea);
				}
			}
		});
		frmSdnDebugger.getContentPane().add(btnSubmit);
		
		JLabel lblEnterSurceIp = new JLabel("Enter Source IP:");
		lblEnterSurceIp.setBounds(5, 101, 94, 14);
		frmSdnDebugger.getContentPane().add(lblEnterSurceIp);
		
		JLabel lblEnterDestIp = new JLabel("Enter Dest. IP:");
		lblEnterDestIp.setBounds(5, 165, 94, 14);
		frmSdnDebugger.getContentPane().add(lblEnterDestIp);
		
		textField_1 = new JTextField();
		textField_1.setBounds(141, 101, 179, 20);
		textField_1.setText("10.0.0.1");
		frmSdnDebugger.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(141, 165, 179, 20);
		textField_2.setText("10.0.0.2");
		frmSdnDebugger.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		lblHostReachability = new JLabel("Host Reachability");
		lblHostReachability.setBounds(102, 11, 148, 14);
		lblHostReachability.setFont(new Font("Sylfaen", Font.BOLD, 16));
		frmSdnDebugger.getContentPane().add(lblHostReachability);
		
		JLabel lblEnterPrototype = new JLabel("Enter Proto_Type:");
		lblEnterPrototype.setBounds(5, 229, 122, 14);
		frmSdnDebugger.getContentPane().add(lblEnterPrototype);
		
		textField_3 = new JTextField();
		textField_3.setBounds(141, 229, 179, 20);
		textField_3.setText("8");
		frmSdnDebugger.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		
		
		JLabel lblPathsFound = new JLabel("Paths Found:");
		lblPathsFound.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPathsFound.setBounds(414, 11, 110, 14);
		frmSdnDebugger.getContentPane().add(lblPathsFound);
		
		JButton btnNewButton = new JButton("Clear Result");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//CLEAR CODE HERE
				try{
					Connection con = DriverManager.getConnection("jdbc:odbc:SDN_DBG",
							"", "");					
					String q1 = "DELETE * FROM Result";
					Statement s = con.createStatement();
					s.execute(q1);
			        
				}catch(Exception ae)
				{
					System.out.println(ae.getMessage());
				}
				
			}
		});
		btnNewButton.setBounds(149, 296, 122, 23);
		frmSdnDebugger.getContentPane().add(btnNewButton);
		
		btnClearAllData = new JButton("Clear All Data");
		btnClearAllData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try{
					Connection con = DriverManager.getConnection("jdbc:odbc:SDN_DBG",
							"", "");					
					String q1 = "DELETE * FROM Result";
					Statement s = con.createStatement();
					s.execute(q1);
					String q2 = "DELETE * FROM FlowEntry";
					Statement s2 = con.createStatement();
					s2.execute(q2);
					String q3 = "DELETE * FROM FlowRem";
					Statement s3 = con.createStatement();
					s3.execute(q3);
					String q4 = "DELETE * FROM Links";
					Statement s4 = con.createStatement();
					s4.execute(q4);
			        
				}catch(Exception ae)
				{
					System.out.println(ae.getMessage());
				}
			}
		});
		btnClearAllData.setBounds(10, 296, 117, 23);
		frmSdnDebugger.getContentPane().add(btnClearAllData);
		
		
	}
}
