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
package net.floodlightcontroller.core.offdata;

public class OFFData {
	public String Switch_id;
	public String Match_in_Port;
	public String Match_src_MAC;
	public String Match_dst_MAC;
	public String Match_src_IP;
	public String Match_dst_IP;
	public String Match_src_port;
	public String Match_dst_port;
	public String Match_Vlan;
	public String Action_out_port;
	public String Action_length;
	public String Action_type;
	public String Action_maxLength;
	public String Timestamp;
	public String BufferID;
	public String Command;
	public String Cookie;
	public String Flags;
	public String Hard_Timeout;
	public String Idle_Timeout;
	public String M_out_port;
	public String M_priority;	
	public String M_length;
	public String M_type;
	public String M_version;
	public String M_xid;
	public String Match_nw_protocol;
	public String Match_nw_proto_id;
	
	
	

	public OFFData(String switch_id, String match_in_Port,
			String match_src_MAC, String match_dst_MAC, String match_src_IP,
			String match_dst_IP, String match_src_port, String match_dst_port,
			String match_Vlan, String action_out_port, String action_length,
			String action_type, String action_maxLength, String timestamp,
			String bufferID, String command, String cookie, String flags,
			String hard_Timeout, String idle_Timeout, String m_out_port,
			String m_priority, String m_length, String m_type,
			String m_version, String m_xid, String match_nw_protocol,
			String match_nw_proto_id) {
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
		Action_out_port = action_out_port;
		Action_length = action_length;
		Action_type = action_type;
		Action_maxLength = action_maxLength;
		Timestamp = timestamp;
		BufferID = bufferID;
		Command = command;
		Cookie = cookie;
		Flags = flags;
		Hard_Timeout = hard_Timeout;
		Idle_Timeout = idle_Timeout;
		M_out_port = m_out_port;
		M_priority = m_priority;
		M_length = m_length;
		M_type = m_type;
		M_version = m_version;
		M_xid = m_xid;
		Match_nw_protocol = match_nw_protocol;
		Match_nw_proto_id = match_nw_proto_id;
	}




	public String getSwitch_id() {
		return Switch_id;
	}




	public void setSwitch_id(String switch_id) {
		Switch_id = switch_id;
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




	public String getAction_out_port() {
		return Action_out_port;
	}




	public void setAction_out_port(String action_out_port) {
		Action_out_port = action_out_port;
	}




	public String getAction_length() {
		return Action_length;
	}




	public void setAction_length(String action_length) {
		Action_length = action_length;
	}




	public String getAction_type() {
		return Action_type;
	}




	public void setAction_type(String action_type) {
		Action_type = action_type;
	}




	public String getAction_maxLength() {
		return Action_maxLength;
	}




	public void setAction_maxLength(String action_maxLength) {
		Action_maxLength = action_maxLength;
	}




	public String getTimestamp() {
		return Timestamp;
	}




	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}




	public String getBufferID() {
		return BufferID;
	}




	public void setBufferID(String bufferID) {
		BufferID = bufferID;
	}




	public String getCommand() {
		return Command;
	}




	public void setCommand(String command) {
		Command = command;
	}




	public String getCookie() {
		return Cookie;
	}




	public void setCookie(String cookie) {
		Cookie = cookie;
	}




	public String getFlags() {
		return Flags;
	}




	public void setFlags(String flags) {
		Flags = flags;
	}




	public String getHard_Timeout() {
		return Hard_Timeout;
	}




	public void setHard_Timeout(String hard_Timeout) {
		Hard_Timeout = hard_Timeout;
	}




	public String getIdle_Timeout() {
		return Idle_Timeout;
	}




	public void setIdle_Timeout(String idle_Timeout) {
		Idle_Timeout = idle_Timeout;
	}




	public String getM_out_port() {
		return M_out_port;
	}




	public void setM_out_port(String m_out_port) {
		M_out_port = m_out_port;
	}




	public String getM_priority() {
		return M_priority;
	}




	public void setM_priority(String m_priority) {
		M_priority = m_priority;
	}




	public String getM_length() {
		return M_length;
	}




	public void setM_length(String m_length) {
		M_length = m_length;
	}




	public String getM_type() {
		return M_type;
	}




	public void setM_type(String m_type) {
		M_type = m_type;
	}




	public String getM_version() {
		return M_version;
	}




	public void setM_version(String m_version) {
		M_version = m_version;
	}




	public String getM_xid() {
		return M_xid;
	}




	public void setM_xid(String m_xid) {
		M_xid = m_xid;
	}




	public String getMatch_nw_protocol() {
		return Match_nw_protocol;
	}




	public void setMatch_nw_protocol(String match_nw_protocol) {
		Match_nw_protocol = match_nw_protocol;
	}




	public String getMatch_nw_proto_id() {
		return Match_nw_proto_id;
	}




	public void setMatch_nw_proto_id(String match_nw_proto_id) {
		Match_nw_proto_id = match_nw_proto_id;
	}




	@Override
	public String toString() {
		return "OFFData [Switch_id=" + Switch_id + ", Match_in_Port="
				+ Match_in_Port + ", Match_src_MAC=" + Match_src_MAC
				+ ", Match_dst_MAC=" + Match_dst_MAC + ", Match_src_IP="
				+ Match_src_IP + ", Match_dst_IP=" + Match_dst_IP
				+ ", Match_src_port=" + Match_src_port + ", Match_dst_port="
				+ Match_dst_port + ", Match_Vlan=" + Match_Vlan
				+ ", Action_out_port=" + Action_out_port + ", Action_length="
				+ Action_length + ", Action_type=" + Action_type
				+ ", Action_maxLength=" + Action_maxLength + ", Timestamp="
				+ Timestamp + ", BufferID=" + BufferID + ", Command=" + Command
				+ ", Cookie=" + Cookie + ", Flags=" + Flags + ", Hard_Timeout="
				+ Hard_Timeout + ", Idle_Timeout=" + Idle_Timeout
				+ ", M_out_port=" + M_out_port + ", M_priority=" + M_priority
				+ ", M_length=" + M_length + ", M_type=" + M_type
				+ ", M_version=" + M_version + ", M_xid=" + M_xid
				+ ", Match_nw_protocol=" + Match_nw_protocol
				+ ", Match_nw_proto_id=" + Match_nw_proto_id + "]";
	}
	

}
