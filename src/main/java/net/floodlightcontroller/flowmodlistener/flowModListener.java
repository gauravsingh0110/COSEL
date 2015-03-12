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

package net.floodlightcontroller.flowmodlistener;

import java.util.Collection;
import java.util.Map;

import org.openflow.protocol.OFFlowMod;
import org.openflow.protocol.OFMatch;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFType;
import org.openflow.protocol.action.OFActionOutput;
import org.openflow.util.HexString;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.packet.ARP;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPacket;
import net.floodlightcontroller.packet.IPv4;
import java.sql.*;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class flowModListener implements IOFMessageListener, IFloodlightModule {

	protected IFloodlightProviderService floodlightProvider;
	protected static Logger logger;
	protected boolean isMySQLavailable;

	// @Override
	// public String getName() {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public String getName() {
		return flowModListener.class.getSimpleName();
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

	// @Override
	// public Collection<Class<? extends IFloodlightService>>
	// getModuleDependencies() {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
		l.add(IFloodlightProviderService.class);
		return l;
	}

	// @Override
	// public void init(FloodlightModuleContext context)
	// throws FloodlightModuleException {
	// // TODO Auto-generated method stub
	//
	// }

	@Override
	public void init(FloodlightModuleContext context)
			throws FloodlightModuleException {
		floodlightProvider = context
				.getServiceImpl(IFloodlightProviderService.class);
		logger = LoggerFactory.getLogger(flowModListener.class);
		try {
			// The newInstance() call is a work around for some
			// broken Java implementations

			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

		} catch (Exception ex) {
			// isMySQLavailable = false;
			// handle the error
		}

	}

	// @Override
	// public void startUp(FloodlightModuleContext context) {
	// // TODO Auto-generated method stub
	//
	// }

	@Override
	public void startUp(FloodlightModuleContext context) {
		floodlightProvider.addOFMessageListener(OFType.FLOW_MOD, this);

	}

	@Override
	public net.floodlightcontroller.core.IListener.Command receive(
			IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
		
		// Ethernet eth = IFloodlightProviderService.bcStore.get(cntx,
		// IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
		//
		// Long sourceMACHash = Ethernet.toLong(eth.getSourceMACAddress());
		Ethernet eth = null;
		// Connection conn = null;
		// PreparedStatement preparedStatement = null;
		OFFlowMod fm = (OFFlowMod) msg;
		String query = "INSERT INTO FlowEntry VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (cntx != null) {
			eth = IFloodlightProviderService.bcStore.get(cntx,
					IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
			if (eth == null)
			{
				//System.out.println("\n\n\n\n\nYou are inside!!\n\n\n\n\n\n");
				return Command.CONTINUE;
		
			}
		}
		IPacket pkt = (IPacket) eth.getPayload();
		OFActionOutput act = (OFActionOutput) fm.getActions().get(0);
		OFMatch mat = fm.getMatch();

		try {
			Connection con = DriverManager.getConnection("jdbc:odbc:SDN_DBG",
					"", "");
			
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1,String.valueOf( sw.getId() ));
			preparedStatement.setString(2,
					Integer.toString(fm.getMatch().getInputPort()));
			preparedStatement.setString(3,
					HexString.toHexString(eth.getSourceMACAddress()));
			preparedStatement.setString(4,
					HexString.toHexString(eth.getDestinationMACAddress()));
			preparedStatement.setString(28, Byte.toString(fm.getMatch().getNetworkProtocol()));
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

			if (pkt instanceof ARP) {
				ARP p = (ARP) pkt;
				// sb.append("\nnw_src: ");
				preparedStatement.setString(5, IPv4.fromIPv4Address(IPv4
						.toIPv4Address(p.getSenderProtocolAddress())));
				preparedStatement.setString(6, IPv4.fromIPv4Address(IPv4
						.toIPv4Address(p.getTargetProtocolAddress())));
				preparedStatement.setString(27, "ARP");
			}
			if (pkt instanceof IPv4) {
				IPv4 p = (IPv4) pkt;

				/*System.out.println("\nI m IPv4 " + p.getParent().toString()
						+ "\n" + p.getPayload().toString());*/
				preparedStatement.setString(5,
						IPv4.fromIPv4Address(p.getSourceAddress()));
				preparedStatement.setString(6,
						IPv4.fromIPv4Address(p.getDestinationAddress()));
				preparedStatement.setString(27, "IPv4");

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

			if (eth.getVlanID() == Ethernet.VLAN_UNTAGGED)
				preparedStatement.setString(9, "Untagged");
			else
				preparedStatement.setString(9,
						Integer.toString(eth.getVlanID()));
			// sb.append("\ndl_vlan_pcp: ");
			// sb.append(this.getPriorityCode());

			preparedStatement.setString(10, Short.toString(act.getPort()));
			preparedStatement.setString(11, Short.toString(act.getLength()));
			preparedStatement.setString(12, act.getType().toString());
			preparedStatement.setString(13, Short.toString(act.getMaxLength()));

			preparedStatement.setString(14,  String.valueOf(System.currentTimeMillis()));
			preparedStatement.setString(15, Integer.toString(fm.getBufferId()));
			preparedStatement.setString(16, Short.toString(fm.getCommand()));
			preparedStatement.setString(17, Long.toString(fm.getCookie()));
			preparedStatement.setString(18, Short.toString(fm.getFlags()));
			preparedStatement.setString(19, Short.toString(fm.getHardTimeout()));
			preparedStatement.setString(20, Short.toString(fm.getIdleTimeout()));
			preparedStatement.setString(21, Short.toString(fm.getOutPort()));
			preparedStatement.setString(22, Short.toString(fm.getPriority()));
			preparedStatement.setString(23, Short.toString(fm.getLength()));
			preparedStatement.setString(24, fm.getType().toString());
			preparedStatement.setString(25, Byte.toString(fm.getVersion()));
			preparedStatement.setString(26, Integer.toString(fm.getXid()));
			
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
		logger.info("\n\nFlowMod Message Log 1: {}", sw.toString());
		logger.info("FlowMod Message Log 2: {}", msg.toString());
		// OFMessage.getDataAsString(sw, msg, cntx));
		logger.info("FlowMod Message Log 3: {}\n\n", eth.toString());
		*/
		// System.out.println("Values :" + fm.getActions().size() + "\n\n");

		// logger.info("\nFlowMod Message Log 3: {}", cntx.toString());

		return Command.CONTINUE;
	}
}