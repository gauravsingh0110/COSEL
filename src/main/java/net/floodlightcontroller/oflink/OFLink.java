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
package net.floodlightcontroller.oflink;

public class OFLink {
	public long src;
	public long dst;
	public short srcPort;
	public short dstPort;
	public short LINK_TYPE; //0 for switch---switch //bidirectional links
							//1 for host---switch //source is always a switch
	public String ip;
	public long getSrc() {
		return src;
	}
	public OFLink(long src, long dst, short srcPort,
									short dstPort, short lINK_TYPE,String ip) {
								super();
								this.src = src;
								this.dst = dst;
								this.srcPort = srcPort;
								this.dstPort = dstPort;
								this.LINK_TYPE = lINK_TYPE;
								this.ip=ip;
							}
	public OFLink() {
		
		// TODO Auto-generated constructor stub
		super();
		this.src = 0;
		this.dst = 0;
		this.srcPort = 0;
		this.dstPort = 0;
		this.LINK_TYPE = 0;
		this.ip="";
	}
	public short getLINK_TYPE() {
		return this.LINK_TYPE;
	}
	public void setLINK_TYPE(short lINK_TYPE) {
		this.LINK_TYPE = lINK_TYPE;
	}
	public String getip() {
		return this.ip;
	}
	public void setip(String ip) {
		this.ip = ip;
	}
	public void setSrc(long src) {
		this.src = src;
	}
	public long getDst() {
		return dst;
	}
	public void setDst(long dst) {
		this.dst = dst;
	}
	public short getSrcPort() {
		return srcPort;
	}
	public void setSrcPort(short srcPort) {
		this.srcPort = srcPort;
	}
	public short getDstPort() {
		return dstPort;
	}
	public void setDstPort(short dstPort) {
		this.dstPort = dstPort;
	}
	
	public boolean has(OFLink t)
	{
		return this.src == t.src
				&& this.dst == t.dst
				&& this.srcPort == t.srcPort
				&& this.dstPort == t.dstPort
				&& this.LINK_TYPE == t.LINK_TYPE
				;
	}
	@Override
	public String toString() {
		return "OFLink [src=" + src + ", dst=" + dst + ", srcPort=" + srcPort
				+ ", dstPort=" + dstPort + ", LINK_TYPE=" + LINK_TYPE + "]";
	}
	
}
