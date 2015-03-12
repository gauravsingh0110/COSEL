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
package net.floodlightcontroller.flowpushlistener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import org.openflow.protocol.OFFlowMod;
import org.openflow.protocol.OFMatch;
import org.openflow.protocol.OFType;
import org.openflow.protocol.action.OFActionOutput;
import org.openflow.util.HexString;
import org.openflow.util.U16;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;














import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.staticflowentry.StaticFlowEntries;
import net.floodlightcontroller.storage.IResultSet;
import net.floodlightcontroller.storage.IStorageSourceListener;
import net.floodlightcontroller.storage.IStorageSourceService;

public class flowPushListener implements IStorageSourceListener,
		IFloodlightModule {
	protected IFloodlightProviderService floodlightProvider;
	protected IStorageSourceService storageSource;
	
	
	public static final String TABLE_NAME = "controller_staticflowtableentry";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SWITCH = "switch_id";
    public static final String COLUMN_ACTIVE = "active";
    public static final String COLUMN_IDLE_TIMEOUT = "idle_timeout";
    public static final String COLUMN_HARD_TIMEOUT = "hard_timeout";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_COOKIE = "cookie";
    public static final String COLUMN_WILDCARD = "wildcards";
    public static final String COLUMN_IN_PORT = "in_port";
    public static final String COLUMN_DL_SRC = "dl_src";
    public static final String COLUMN_DL_DST = "dl_dst";
    public static final String COLUMN_DL_VLAN = "dl_vlan";
    public static final String COLUMN_DL_VLAN_PCP = "dl_vlan_pcp";
    public static final String COLUMN_DL_TYPE = "dl_type";
    public static final String COLUMN_NW_TOS = "nw_tos";
    public static final String COLUMN_NW_PROTO = "nw_proto";
    public static final String COLUMN_NW_SRC = "nw_src"; // includes CIDR-style
                                                         // netmask, e.g.
                                                         // "128.8.128.0/24"
    public static final String COLUMN_NW_DST = "nw_dst";
    public static final String COLUMN_TP_DST = "tp_dst";
    public static final String COLUMN_TP_SRC = "tp_src";
    public static final String COLUMN_ACTIONS = "actions";
    public static String ColumnNames[] = { COLUMN_NAME, COLUMN_SWITCH,
            COLUMN_ACTIVE, COLUMN_IDLE_TIMEOUT, COLUMN_HARD_TIMEOUT,
            COLUMN_PRIORITY, COLUMN_COOKIE, COLUMN_WILDCARD, COLUMN_IN_PORT,
            COLUMN_DL_SRC, COLUMN_DL_DST, COLUMN_DL_VLAN, COLUMN_DL_VLAN_PCP,
            COLUMN_DL_TYPE, COLUMN_NW_TOS, COLUMN_NW_PROTO, COLUMN_NW_SRC,
            COLUMN_NW_DST, COLUMN_TP_DST, COLUMN_TP_SRC, COLUMN_ACTIONS };

	
	
	
	protected static Logger log = LoggerFactory.getLogger(flowPushListener.class);
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
		// TODO Auto-generated method stub
		Collection<Class<? extends IFloodlightService>> l =
		        new ArrayList<Class<? extends IFloodlightService>>();
		    l.add(IStorageSourceService.class);
		return l;
	}

	@Override
	public void init(FloodlightModuleContext context)
			throws FloodlightModuleException {
		// TODO Auto-generated method stub
		floodlightProvider =
	            context.getServiceImpl(IFloodlightProviderService.class);
		storageSource =
	            context.getServiceImpl(IStorageSourceService.class);
		try {
			// The newInstance() call is a work around for some
			// broken Java implementations

			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

		} catch (Exception ex) {
			// isMySQLavailable = false;
			// handle the error
		}

	}

	@Override
	public void startUp(FloodlightModuleContext context)
			throws FloodlightModuleException {
		storageSource.addListener("controller_staticflowtableentry", this);
	}

	@Override
	public void rowsModified(String tableName, Set<Object> rowKeys) {
		// TODO Auto-generated method stub
		
		HashMap<String, Map<String, OFFlowMod>> entriesToAdd =
	            new HashMap<String, Map<String, OFFlowMod>>();
	        // build up list of what was added
		   
		for (Object key: rowKeys) {
	            IResultSet resultSet = storageSource.getRow(tableName, key);
	            Iterator<IResultSet> it = resultSet.iterator();
	            while (it.hasNext()) {
	                Map<String, Object> row = it.next().getRow();
	                 
	                parseRow(row, entriesToAdd);
	            }
	        }
	     //  return Command.CONTINUE;
	}

	@Override
	public void rowsDeleted(String tableName, Set<Object> rowKeys) {
		// TODO Auto-generated method stub

	}

	public String getName() {
	    return flowPushListener.class.getSimpleName();
	}
	
	
	
	void parseRow(Map<String, Object> row, Map<String, Map<String, OFFlowMod>> entries) {
        String switchName = null;
        String entryName = null;
        
        StringBuffer matchString = new StringBuffer();

        OFFlowMod flowMod = (OFFlowMod) floodlightProvider.getOFMessageFactory()
                .getMessage(OFType.FLOW_MOD);

        if (!row.containsKey(COLUMN_SWITCH) || !row.containsKey(COLUMN_NAME)) {
            log.debug(
                    "skipping entry with missing required 'switch' or 'name' entry: {}",
                    row);
            return;
        }
        
        // most error checking done with ClassCastException
        try {
            // first, snag the required entries, for debugging info
            switchName = (String) row.get(COLUMN_SWITCH);
            entryName = (String) row.get(COLUMN_NAME);
            if (!entries.containsKey(switchName))
                entries.put(switchName, new HashMap<String, OFFlowMod>());
            StaticFlowEntries.initDefaultFlowMod(flowMod, entryName);
            
            for (String key : row.keySet()) {
            	
                if (row.get(key) == null)
                    continue;
                if (key.equals(COLUMN_SWITCH) || key.equals(COLUMN_NAME)
                        || key.equals("id"))
                    continue; // already handled
                // explicitly ignore timeouts and wildcards
                if (key.equals(COLUMN_HARD_TIMEOUT) || key.equals(COLUMN_IDLE_TIMEOUT) ||
                        key.equals(COLUMN_WILDCARD))
                    continue;
                if (key.equals(COLUMN_ACTIVE)) {
                    if  (!Boolean.valueOf((String) row.get(COLUMN_ACTIVE))) {
                        log.debug("skipping inactive entry {} for switch {}",
                                entryName, switchName);
                        entries.get(switchName).put(entryName, null);  // mark this an inactive
                        return;
                    }
                } else if (key.equals(COLUMN_ACTIONS)){
                    StaticFlowEntries.parseActionString(flowMod, (String) row.get(COLUMN_ACTIONS), log);
                } else if (key.equals(COLUMN_COOKIE)) {
                    flowMod.setCookie(
                            StaticFlowEntries.computeEntryCookie(flowMod,
                                    Integer.valueOf((String) row.get(COLUMN_COOKIE)),
                                    entryName));
                } else if (key.equals(COLUMN_PRIORITY)) {
                    flowMod.setPriority(U16.t(Integer.valueOf((String) row.get(COLUMN_PRIORITY))));
                } else { // the rest of the keys are for OFMatch().fromString()
                    if (matchString.length() > 0)
                        matchString.append(",");
                    matchString.append(key + "=" + row.get(key).toString());
                }
            }
        } catch (ClassCastException e) {
            if (entryName != null && switchName != null) {
                log.warn(
                        "Skipping entry {} on switch {} with bad data : "
                                + e.getMessage(), entryName, switchName);
            } else {
                log.warn("Skipping entry with bad data: {} :: {} ",
                        e.getMessage(), e.getStackTrace());
            }
        }
        
        OFMatch ofMatch = new OFMatch();
        String match = matchString.toString();
        
        try {
        	    ofMatch.fromString(match);
        } catch (IllegalArgumentException e) {
        	System.out.println("\n\nError is : " + match );
            log.debug(
                    "ignoring flow entry {} on switch {} with illegal OFMatch() key: "
                            + match, entryName, switchName);
           /* System.out.println("\n match :" + match + "\nentryname:" +entryName+
            		"\n"
            		)*/
            return;
        }
        //System.out.println("\n\n\nEntries are being added to table###\n\n\n");
        flowMod.setMatch(ofMatch);
        entries.get(switchName).put(entryName, flowMod);
        
        addtodb(flowMod,switchName);
    }

	String toSwitchID(String name)
	{
		
		return String.valueOf(Long.valueOf(name.replace(":", ""), 16));
	}
	void addtodb(OFFlowMod flow,String switchName)
	{
		String query = "INSERT INTO FlowEntry VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		OFMatch mat = flow.getMatch();
		OFActionOutput act = (OFActionOutput)flow.getActions().get(0);
		try {
			Connection con = DriverManager.getConnection("jdbc:odbc:SDN_DBG",
					"", "");
			
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, toSwitchID(switchName) );
			preparedStatement.setString(2,String.valueOf(mat.getInputPort()));
			preparedStatement.setString(3,
					HexString.toHexString(mat.getDataLayerSource()));
			preparedStatement.setString(4,
					HexString.toHexString(mat.getDataLayerDestination()));
			preparedStatement.setString(28, Byte.toString(mat.getNetworkProtocol()));
		


			preparedStatement.setString(5, IPv4.fromIPv4Address(mat.getNetworkSource()));// "0");
			preparedStatement.setString(6, IPv4.fromIPv4Address(mat.getNetworkDestination()));//"0");
			preparedStatement.setString(7,String.valueOf(mat.getTransportSource() * -1
					+ Short.MAX_VALUE));
			preparedStatement.setString(8, String.valueOf(mat.getTransportDestination() * -1
					+ Short.MAX_VALUE)); //"0");



			preparedStatement.setString(27, "NA");
			if (mat.getTransportSource() < 0)

				preparedStatement.setString(
						7,
						Integer.toString(mat.getTransportSource() * -1
								+ Short.MAX_VALUE));
			else
				preparedStatement.setString(7,
						Integer.toString(mat.getTransportSource()));

			if (mat.getTransportDestination() < 0)

				preparedStatement.setString(
						8,
						Integer.toString(mat.getTransportDestination() * -1
								+ Short.MAX_VALUE));
			else
				preparedStatement.setString(8,
						Integer.toString(mat.getTransportDestination()));

//			Ethernet eth;
			if (mat.getDataLayerVirtualLan() == Ethernet.VLAN_UNTAGGED)
				preparedStatement.setString(9, "Untagged");
			else
				preparedStatement.setString(9,
						Short.toString(mat.getDataLayerVirtualLan()));
			// sb.append("\ndl_vlan_pcp: ");
			// sb.append(this.getPriorityCode());

			preparedStatement.setString(10, Short.toString(act.getPort()));
			preparedStatement.setString(11, Short.toString(act.getLength()));
			preparedStatement.setString(12, act.getType().toString());
			preparedStatement.setString(13, Short.toString(act.getMaxLength()));

			preparedStatement.setString(14,  String.valueOf(System.currentTimeMillis()));
			preparedStatement.setString(15, Integer.toString(flow.getBufferId()));
			preparedStatement.setString(16, Short.toString(flow.getCommand()));
			preparedStatement.setString(17, Long.toString(flow.getCookie()));
			preparedStatement.setString(18, Short.toString(flow.getFlags()));
			preparedStatement.setString(19, Short.toString(flow.getHardTimeout()));
			preparedStatement.setString(20, Short.toString(flow.getIdleTimeout()));
			preparedStatement.setString(21, Short.toString(flow.getOutPort()));
			preparedStatement.setString(22, Short.toString(flow.getPriority()));
			preparedStatement.setString(23, Short.toString(flow.getLength()));
			preparedStatement.setString(24, flow.getType().toString());
			preparedStatement.setString(25, Byte.toString(flow.getVersion()));
			preparedStatement.setString(26, Integer.toString(flow.getXid()));
			
			int res = preparedStatement.executeUpdate();
			if (res == 0) {
				System.out.println("Debug Message for fdb: No rows Inserted\n");
			}
			else
				System.out.println("\nAdded entries\n");
			preparedStatement.close();
			con.commit();
			con.close();
			// System.out.println("\n Temp message " + act.toString());

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} finally {
			

		}

	}
}
