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
package net.floodlightcontroller.core.crawler;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;

import net.floodlightcontroller.core.crawler.Crawl;
import net.floodlightcontroller.core.offdata.OFFData;

import java.sql.Statement;

import javax.swing.JTextArea;

import net.floodlightcontroller.devicemanager.IDeviceService;
import net.floodlightcontroller.oflink.OFLink;

public class Crawl {
	public IDeviceService deviceManager;
	public String timestamp;
	public String ipsrc;
	public String ipdest;
	public String tcp_port;
	final Object lock = new Object();
	ArrayList<OFFData> data = new ArrayList<OFFData>();
	ArrayList<OFLink> links = new ArrayList<OFLink>();
	public Crawl(String timestamp, String ipsrc, String ipdest, String tcp_port) {
		super();
		this.timestamp = timestamp;
		this.ipsrc = ipsrc;
		this.ipdest = ipdest;
		this.tcp_port = tcp_port;
	}
	public void run(JTextArea textArea)
	{
		System.out.println("\n\nPRINTING TRACE: \n");
		try{
		Connection con = DriverManager.getConnection("jdbc:odbc:SDN_DBG",
				"", "");
		
		Statement s1 = null;
		
		
		
		
		String q1 = "SELECT LINK_TYPE,src,dst,srcPort,dstPort,Timestamp,ip from Links WHERE LINK_TYPE='" + String.valueOf(1) +"' AND ip='" + ipsrc +"'";
		
		s1 = con.createStatement();
        ResultSet rs = s1.executeQuery(q1);
        while (rs.next()) {
        	StringBuffer path = new StringBuffer(1000);
        	textArea.append("\n" + ipsrc);
        	textArea.append("-->");
        	path.append(ipsrc);
        	path.append("-->");
        	//path.append(str)
        	//System.out.print(ipsrc + "-->");
        	//this.path.append(ipsrc);
        	//this.path.append("-->");
        	OFLink l= new OFLink(
        			Long.valueOf(rs.getString("dst")),
        			Long.valueOf(rs.getString("src")),
        			Short.valueOf(rs.getString("dstPort")),//swap of port
        			Short.valueOf(rs.getString("srcPort")),
        			Short.valueOf(rs.getString("LINK_TYPE")),
        			rs.getString("ip")
        			);
        	//Swap due to assumption that in D<-->S link,always source is switch
        	findnext(l,0,path,textArea);
        	return;
		        }
		}catch(Exception ae)
		        {
		        	System.out.println(ae.getMessage());
		        }
		return;
		}
		//endprintthis.path
	
	
	
	
	public void findnext(OFLink l,int isFirstCall,StringBuffer path, JTextArea textArea)
	{
		if((String.valueOf(l.dstPort)).equals("0") && isFirstCall != 0)
		{
			path.append(l.ip);
			textArea.append(l.ip);
			textArea.append("\n");
			
			System.out.println("PATH discovered is : " + path.toString());
			//System.out.print(l.ip);
			//this.path.append(l.ip);
			return;
		}
		else
		{
			try{
				path.append(String.valueOf(l.dst));
				path.append("-->");
				textArea.append(String.valueOf(l.dst));
				textArea.append("-->");
				//System.out.print(String.valueOf(l.dst) + "-->"); 
				//this.path.append( String.valueOf(l.dst));
				//this.path.append("-->");
				Connection con = DriverManager.getConnection("jdbc:odbc:SDN_DBG",
						"", "");
				Statement s2 = null;
				s2 = con.createStatement();
				
				String q2= "SELECT * FROM Result WHERE Match_src_IP =\'" + ipsrc
						+	"\' AND Match_dst_IP =\'" + ipdest 
						+	"\' AND Match_src_port =\'" + tcp_port
						+	"\' AND Switch_id =\'" + String.valueOf(l.dst)
						+	"\' AND Match_in_Port =\'" + String.valueOf(l.dstPort) + "\'"
						;
				ResultSet rs = s2.executeQuery(q2);
				
				String oport = "";
				long min = Long.MAX_VALUE;
				while (rs.next()) {
				
				long current = Long.valueOf(rs.getString("M_priority"));
				if(min > current);
					{
						//System.out.println("\n\n#Result query resulted in Actionout" + current);
						min = current;
						oport= rs.getString("Action_out_port");
					}
				}//end of while
				
				Statement s3 = null;
				s3 = con.createStatement();
				
				String q3= "SELECT * FROM Links WHERE "
						+ "src ='" + String.valueOf(l.dst)
						+	"' AND srcPort ='" + oport +"'"
						;
				ResultSet rs2 = s3.executeQuery(q3);
				
				while(rs2.next())
				{
					
					OFLink l2 = new OFLink(
			        			Long.valueOf(rs2.getString("src")),
			        			Long.valueOf(rs2.getString("dst")),
			        			Short.valueOf(rs2.getString("srcPort")),
			        			Short.valueOf(rs2.getString("dstPort")),
			        			Short.valueOf(rs2.getString("LINK_TYPE")),
			        			rs2.getString("ip")
			        			);
					findnext(l2,1,path,textArea);
					return;
				}
				
				
				
			}catch(Exception ae)
	        {
	        	System.out.println(ae.getMessage());
	        }
		}
	}
}