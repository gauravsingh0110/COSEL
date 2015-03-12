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
package net.floodlightcontroller.portTracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import net.floodlightcontroller.oflink.*;

import java.util.Collection;

import net.floodlightcontroller.core.IFloodlightProviderService;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.Set;

import net.floodlightcontroller.linkdiscovery.ILinkDiscovery.LinkType;
import net.floodlightcontroller.linkdiscovery.ILinkDiscoveryService;
import net.floodlightcontroller.linkdiscovery.LinkInfo;
import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.routing.Link;
import net.floodlightcontroller.topology.ITopologyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFType;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.devicemanager.IDevice;
import net.floodlightcontroller.devicemanager.IDeviceService;
import net.floodlightcontroller.devicemanager.SwitchPort;

public class portTracker implements IFloodlightModule, IOFMessageListener {

	protected IFloodlightProviderService floodlightProvider;
	public ITopologyService g;
	protected Set<Long> macAddresses;
	protected static Logger logger;
	public IDeviceService deviceManager;
	public ILinkDiscoveryService linkservice;
	ArrayList<Integer> devices = new ArrayList<Integer>();
	ArrayList<Long> switches = new ArrayList<Long>();
	Map<Link,LinkInfo> alllink;
	ArrayList<OFLink> arrlink = new ArrayList<OFLink>();
	final Object lock = new Object();
	
	@Override
	public String getName() {
		return portTracker.class.getSimpleName();
	}

	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public net.floodlightcontroller.core.IListener.Command receive(
			
			IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
		synchronized(this){
		if(!switches.contains(sw.getId()))
				switches.add(sw.getId());
       
        alllink = linkservice.getLinks();
        for (Map.Entry<Link,LinkInfo> entry : alllink.entrySet())
        {
            boolean flag=false;//if arrlink contains this link
        	OFLink temp = new OFLink(entry.getKey().getSrc(),entry.getKey().getDst(),entry.getKey().getSrcPort(),entry.getKey().getDstPort(),(short)0,"");
        	
        	for(OFLink ofl:arrlink)
        	{
        		if(ofl.has(temp))
        			flag = true;
        	}
        	if(entry.getValue().getLinkType() != LinkType.DIRECT_LINK)
        		flag=true;
        	synchronized(lock){
        		if(!flag)
	        	{
	        		arrlink.add(temp);
	        		addtodatabase(temp);
	        	}
        	}
        }
    
    	Collection<? extends IDevice> allDevices = deviceManager.getAllDevices();
		for(IDevice d:allDevices)//HexString.toHexString
		{
			for(SwitchPort s:d.getAttachmentPoints())
			{	
				long src = s.getSwitchDPID();
				short oport = (short)s.getPort();
				long dst = d.getMACAddress();
				StringBuffer ip=new StringBuffer(15);
				if(d.getIPv4Addresses().length >=1)
				{	
					ip.append(IPv4.fromIPv4Address(d.getIPv4Addresses()[0]));
					String ip2 = ip.toString();		
					boolean flag=false;//if arrlink contains this link
		        	OFLink temp = new OFLink(src,dst,oport,(short)0,(short)1,
		        			ip2 );
		        	for(OFLink ofl:arrlink)
		        	{
		        		if(ofl.has(temp))
		        			flag = true;
		        	}
		        	
		        	synchronized(lock){
		        	if(!flag)
			        	{
			        		arrlink.add(temp);
			        		addtodatabase(temp);
			        		
			        	}	
		        	}
				}  	
			}
		}
		/*
        System.out.println("!!!!!!Printing Contents of temp");
        for (OFLink t:arrlink)
        	System.out.println(t);
		*/
		}
		
		
        
        // deviceManager.queryDevices(macAddress, vlan, ipv4Address, switchDPID, switchPort)
        return Command.CONTINUE;
	}
	
	public void addtodatabase(OFLink temp)
	{
		try{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:SDN_DBG","", "");
			String query = "INSERT INTO Links VALUES (?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, String.valueOf(temp.getLINK_TYPE()));
			preparedStatement.setString(2, String.valueOf(temp.getSrc()));
			preparedStatement.setString(3, String.valueOf(temp.getDst()));
			preparedStatement.setString(4, String.valueOf(temp.getSrcPort()));
			preparedStatement.setString(5, String.valueOf(temp.getDstPort()));
			preparedStatement.setString(6, String.valueOf(System.currentTimeMillis()));
			preparedStatement.setString(7, temp.getip());
			int res = preparedStatement.executeUpdate();
			if (res == 0) {
				System.out.println("Debug Message for fdb: No rows Inserted\n");
			}
			
			preparedStatement.close();
			con.commit();
			con.close();
			
			}catch(Exception ae)
			{
			System.out.println(ae.getMessage());
			}
	}
	

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		Collection<Class<? extends IFloodlightService>> l =
		        new ArrayList<Class<? extends IFloodlightService>>();
		    l.add(IFloodlightProviderService.class);
		    return l;
		
	}

	@Override
	public void init(FloodlightModuleContext context)
			throws FloodlightModuleException {
		floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
	    g = context.getServiceImpl(ITopologyService.class);
		macAddresses = new ConcurrentSkipListSet<Long>();
	    logger = LoggerFactory.getLogger(portTracker.class);
	    deviceManager = context.getServiceImpl(IDeviceService.class);
	    linkservice = context.getServiceImpl(ILinkDiscoveryService.class);
	    try {
			// The newInstance() call is a work around for some
			// broken Java implementations

			//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

		} catch (Exception ex) {
			// isMySQLavailable = false;
			// handle the error
		}
	    
	}

	@Override
	public void startUp(FloodlightModuleContext context)
			throws FloodlightModuleException {
		floodlightProvider.addOFMessageListener(OFType.FLOW_MOD, this);
	}

}
