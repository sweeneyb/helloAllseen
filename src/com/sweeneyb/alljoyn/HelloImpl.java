package com.sweeneyb.alljoyn;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusObject;

public class HelloImpl implements HelloInterface, BusObject {

	@Override
	public String MyMethod(String inStr) throws BusException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void MySignal(String inStr) throws BusException {
		// TODO Auto-generated method stub

	}

	@Override
	public String GetMyProperty() throws BusException {
		try {
			String host = InetAddress.getLocalHost().getHostName();
			System.out.println("returning "+host);
			return host;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("returning bar");
		return "bar";
	}

	@Override
	public void SetMyProperty(String myProperty) throws BusException {
		// TODO Auto-generated method stub

	}

}
