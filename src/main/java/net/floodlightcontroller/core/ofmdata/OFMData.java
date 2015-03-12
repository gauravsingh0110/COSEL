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
package net.floodlightcontroller.core.ofmdata;

import net.floodlightcontroller.core.offdata.OFFData;

public class OFMData {
	public String Switch_id;
	public String Match_in_Port;
	public String Match_src_MAC;
	public String Match_dst_MAC;
	public String Match_src_IP;
	public String Match_dst_IP;
	public String Match_src_port;
	public String Match_dst_port;
	public String Match_Vlan;
	public String Timestamp;
	public String Match_nw_proto_id;
	public String getSwitch_id() {
		return Switch_id;
	}
	public void setSwitch_id(String switch_id) {
		Switch_id = switch_id;
	}
	public OFMData(String switch_id, String match_in_Port,
			String match_src_MAC, String match_dst_MAC, String match_src_IP,
			String match_dst_IP, String match_src_port, String match_dst_port,
			String match_Vlan, String timestamp, String match_nw_proto_id) {
		super();
		Switch_id = switch_id;
		Match_in_Port = match_in_Port;
		Match_src_MAC = match_src_MAC;
		Match_dst_MAC = match_dst_MAC;
		Match_src_IP = match_src_IP;
		Match_dst_IP = match_dst_IP;
		Match_src_port = match_src_port;
		Match_dst_port = match_dst_port;
		Match_Vlan = match_Vlan;
		Timestamp = timestamp;
		Match_nw_proto_id = match_nw_proto_id;
	}
	@Override
	public String toString() {
		return "OFMData [Switch_id=" + Switch_id + ", Match_in_Port="
				+ Match_in_Port + ", Match_src_MAC=" + Match_src_MAC
				+ ", Match_dst_MAC=" + Match_dst_MAC + ", Match_src_IP="
				+ Match_src_IP + ", Match_dst_IP=" + Match_dst_IP
				+ ", Match_src_port=" + Match_src_port + ", Match_dst_port="
				+ Match_dst_port + ", Match_Vlan=" + Match_Vlan
				+ ", Timestamp=" + Timestamp + ", Match_nw_proto_id="
				+ Match_nw_proto_id + "]";
	}
	
	public boolean matches(OFFData flow)
	{
		return(
				this.Switch_id.equals(flow.Switch_id) 
				&& this.Match_in_Port.equals( flow.Match_in_Port )
				&& this.Match_src_MAC.equals( flow.Match_src_MAC )
				&& this.Match_dst_MAC.equals( flow.Match_dst_MAC )
				&& this.Match_src_port.equals( flow.Match_src_port )
				&& this.Match_dst_port.equals( flow.Match_dst_port )
				&& this.Match_Vlan.equals( flow.Match_Vlan )
				&& this.Match_nw_proto_id.equals( flow.Match_nw_proto_id )				
				);
				
	}
	
	public String getMatch_in_Port() {
		return Match_in_Port;
	}
	public void setMatch_in_Port(String match_in_Port) {
		Match_in_Port = match_in_Port;
	}
	public String getMatch_src_MAC() {
		return Match_src_MAC;
	}
	public void setMatch_src_MAC(String match_src_MAC) {
		Match_src_MAC = match_src_MAC;
	}
	public String getMatch_dst_MAC() {
		return Match_dst_MAC;
	}
	public void setMatch_dst_MAC(String match_dst_MAC) {
		Match_dst_MAC = match_dst_MAC;
	}
	public String getMatch_src_IP() {
		return Match_src_IP;
	}
	public void setMatch_src_IP(String match_src_IP) {
		Match_src_IP = match_src_IP;
	}
	public String getMatch_dst_IP() {
		return Match_dst_IP;
	}
	public void setMatch_dst_IP(String match_dst_IP) {
		Match_dst_IP = match_dst_IP;
	}
	public String getMatch_src_port() {
		return Match_src_port;
	}
	public void setMatch_src_port(String match_src_port) {
		Match_src_port = match_src_port;
	}
	public String getMatch_dst_port() {
		return Match_dst_port;
	}
	public void setMatch_dst_port(String match_dst_port) {
		Match_dst_port = match_dst_port;
	}
	public String getMatch_Vlan() {
		return Match_Vlan;
	}
	public void setMatch_Vlan(String match_Vlan) {
		Match_Vlan = match_Vlan;
	}
	public String getTimestamp() {
		return Timestamp;
	}
	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}
	public String getMatch_nw_proto_id() {
		return Match_nw_proto_id;
	}
	public void setMatch_nw_proto_id(String match_nw_proto_id) {
		Match_nw_proto_id = match_nw_proto_id;
	}
}
