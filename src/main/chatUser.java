package main;
import java.net.InetAddress;

class chatUser{
	private InetAddress Addr;
	private int Port;
	
	public chatUser(InetAddress Addr, int Port) {
		this.Addr = Addr;
		this.Port = Port;
	}
	public InetAddress getAddr() {
		return Addr;
	}
	public int getPort() {
		return Port;
	}
	public boolean equals(Object obj) {
		if (this==obj)
			return true;
		if (obj==null || this.getClass() != obj.getClass())
			return false;
		chatUser p1 = (chatUser)obj;
		return this.Addr.equals(p1.Addr) 
				&& this.Port == p1.Port;
				
		
	}
}