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

package net.floodlightcontroller.flowremlistener;

import java.util.Collection;
import java.util.Map;

import org.openflow.protocol.OFMatch;
import org.openflow.protocol.OFFlowRemoved;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFType;
import org.openflow.util.HexString;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.packet.Ethernet;
import java.sql.*;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class flowRemovedListener implements IOFMessageListener,
		IFloodlightModule {

	protected IFloodlightProviderService floodlightProvider;
	//protected Set macAddresses;
	protected static Logger logger;
	
		@Override
	public String getName() {
			return flowRemovedListener.class.getSimpleName();
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
		floodlightProvider = context
				.getServiceImpl(IFloodlightProviderService.class);
		logger = LoggerFactory.getLogger(flowRemovedListener.class);
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
		floodlightProvider.addOFMessageListener(OFType.FLOW_REMOVED , this);

	}

	@Override
	public net.floodlightcontroller.core.IListener.Command receive(
			IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
		// Connection conn = null;
				// PreparedStatement preparedStatement = null;
				OFFlowRemoved fr = (OFFlowRemoved) msg;
				String query = "INSERT INTO FlowRem VALUES (?,?,?,?,?,?,?,?,?,?,?)";
				if (cntx != null) {
					//eth is NULL in case of Removal
					/*eth = IFloodlightProviderService.bcStore.get(cntx,
							IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
					if (eth == null)
					{
						System.out.println("\n#######\n"+"Eth is null" +"\n########\n");
					return Command.CONTINUE;
					}*/
				}
				
				//IPacket pkt = (IPacket) eth.getPayload();
				//OFActionOutput act = (OFActionOutput) fr. getActions().get(0);
				OFMatch mat = fr.getMatch();

				try {
					Connection con = DriverManager.getConnection("jdbc:odbc:SDN_DBG",
							"", "");
					// preparedStatement = conn
					//
					// .prepareStatement("insert into  values (default, ?, ?, ?, ? , ?, ?)");
					// Do something with the Connection
					PreparedStatement preparedStatement = con.prepareStatement(query);
					preparedStatement.setString(1, String.valueOf( sw.getId()));
					preparedStatement.setString(2,
							Integer.toString(mat.getInputPort()));
					preparedStatement.setString(3,
							HexString.toHexString(mat.getDataLayerSource()));// eth.getSourceMACAddress()));
					preparedStatement.setString(4,
							HexString.toHexString(mat.getDataLayerDestination()));//eth.getDestinationMACAddress()));

					// if (pkt instanceof ARP)
					// sb.append("arp");
					// else if (pkt instanceof LLDP)
					// sb.append("lldp");
					// else if (pkt instanceof ICMP)
					// sb.append("icmp");
					// else if (pkt instanceof IPv4)
					// sb.append("ip");
					// else if (pkt instanceof DHCP)
					// sb.append("dhcp");
					// else
					// sb.append(this.getEtherType());

					// sb.append("\ndl_vlan: ");


					preparedStatement.setString(5, "0");
					preparedStatement.setString(6, "0");
					preparedStatement.setString(7, "0");
					preparedStatement.setString(8, "0");


					preparedStatement.setString(10, "NA");
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

					
					preparedStatement.setString(5, Integer.toString(mat.getNetworkSourceMaskLen()));
					preparedStatement.setString(6, Integer.toString(mat.getNetworkDestinationMaskLen()));
					
					/*
					if (pkt instanceof ARP) {
						ARP p = (ARP) pkt;
						// sb.append("\nnw_src: ");
						preparedStatement.setString(5, IPv4.fromIPv4Address(IPv4
								.toIPv4Address(p.getSenderProtocolAddress())));
						preparedStatement.setString(6, IPv4.fromIPv4Address(IPv4
								.toIPv4Address(p.getTargetProtocolAddress())));
						preparedStatement.setString(10, "ARP");
					}
					if (pkt instanceof IPv4) {
						IPv4 p = (IPv4) pkt;

						System.out.println("\nI m IPv4 " + p.getParent().toString()
								+ "\n" + p.getPayload().toString());
						preparedStatement.setString(5,
								IPv4.fromIPv4Address(p.getSourceAddress()));
						preparedStatement.setString(6,
								IPv4.fromIPv4Address(p.getDestinationAddress()));
						preparedStatement.setString(10, "IPv4");

						//
						// if (pkt instanceof TCP) {
						// System.out
						// .println("\nI m tcp "
						// + Integer.toString(((TCP) pkt)
						// .getSourcePort())
						// + Integer.toString(((TCP) pkt)
						// .getDestinationPort()) + "\n");
						// preparedStatement.setString(7,
						// Integer.toString(((TCP) pkt).getSourcePort()));
						// preparedStatement.setString(8,
						// Integer.toString(((TCP) pkt).getDestinationPort()));
						//
						// }
						// if (pkt instanceof UDP) {
						// System.out
						// .println("\nI m udp "
						// + Integer.toString(((UDP) pkt)
						// .getSourcePort())
						// + Integer.toString(((UDP) pkt)
						// .getDestinationPort()) + "\n");
						// preparedStatement.setString(7,
						// Integer.toString(((UDP) pkt).getSourcePort()));
						// preparedStatement.setString(8,
						// Integer.toString(((UDP) pkt).getDestinationPort()));
						// }

					}
*/
					// } else if (pkt instanceof DHCP) {
					// sb.append("\ndhcp packet");
					// } else if (pkt instanceof Data) {
					// sb.append("\ndata packet");
					// } else if (pkt instanceof LLC) {
					// sb.append("\nllc packet");
					// } else if (pkt instanceof BPDU) {
					// sb.append("\nbpdu packet");
					// } else
					// sb.append("\nunknwon packet");

					// return sb.toString();
					// }

					if (mat.getDataLayerVirtualLan() == Ethernet.VLAN_UNTAGGED)
						preparedStatement.setString(9, "Untagged");
					else
						preparedStatement.setString(9,
								Short.toString(mat.getDataLayerVirtualLan()));
					preparedStatement.setString(11,  String.valueOf(System.currentTimeMillis()));
					// sb.append("\ndl_vlan_pcp: ");
					// sb.append(this.getPriorityCode());
					preparedStatement.setString(10, Byte.toString(mat.getNetworkProtocol()));
					//preparedStatement.setString(11, Short.toString(act.getLength()));
					//preparedStatement.setString(12, act.getType().toString());
					//preparedStatement.setString(13, Short.toString(act.getMaxLength()));

					//preparedStatement.setString(14, Long.toString(System.currentTimeMillis()));
					//preparedStatement.setString(15, Integer.toString(fr.getBufferId()));
					//preparedStatement.setString(16, Short.toString(fr.getCommand()));
					//preparedStatement.setString(17, Long.toString(fr.getCookie()));
					//preparedStatement.setString(18, Short.toString(fr.getFlags()));
					//preparedStatement.setString(19, Short.toString(fr.getHardTimeout()));
					//preparedStatement.setString(20, Short.toString(fm.getIdleTimeout()));
					//preparedStatement.setString(21, Short.toString(fm.getOutPort()));
					//preparedStatement.setString(22, Short.toString(fm.getPriority()));
					//preparedStatement.setString(23, Short.toString(fm.getLength()));
					//preparedStatement.setString(24, fm.getType().toString());
					//preparedStatement.setString(25, Byte.toString(fm.getVersion()));
					//preparedStatement.setString(26, Integer.toString(fm.getXid()));

					int res = preparedStatement.executeUpdate();

					if (res == 0) {
						System.out.println("Debug Message for fdb: No rows Inserted\n");
					}

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
					// it is a good idea to release
					// resources in a finally{} block
					// in reverse-order of their creation
					// if they are no-longer needed

				}
				// OFFlowMod fm = (OFFlowMod) msg;
				/*
				logger.info("\n#############################################\n");
				logger.info("\n\nFlowRem Message Log 1: {}", sw.toString());
				logger.info("FlowRem Message Log 2: {}", msg.toString());
				// OFMessage.getDataAsString(sw, msg, cntx));
				//logger.info("FlowREm Message Log 3: {}\n\n", eth.toString());
				logger.info("\n#############################################\n");
				// System.out.println("Values :" + fm.getActions().size() + "\n\n");
				*/
				// logger.info("\nFlowMod Message Log 3: {}", cntx.toString(
		return Command.CONTINUE;
	}

}