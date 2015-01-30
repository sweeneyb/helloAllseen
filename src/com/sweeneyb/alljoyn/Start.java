package com.sweeneyb.alljoyn;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.ProxyBusObject;
import org.alljoyn.bus.Status;
import org.alljoyn.bus.BusAttachment.RemoteMessage;

public class Start {
	
	public static String APPNAME = "testApp";
	public static String SERVICE_NAME = "com.sweeneyb.alljoyn.hello";

	public static void main(String[] args) {
		System.loadLibrary("alljoyn_java");
		BusAttachment mBus = new BusAttachment(APPNAME, RemoteMessage.Receive);
		HelloInterface hello = new HelloImpl();
		Status status = null;
		
		status = mBus.registerBusObject(hello, "/servicepath");
		if (Status.OK != status) {
			System.out.println("BusAttachment.registerBusObject() failed: "
					+ status);

			System.exit(0);
			return;
		}

		status= mBus.connect();
		if (Status.OK != status) {
			System.out.println("BusAttachment.connect() failed: " + status);

			System.exit(0);
			return;
		} else {
			System.out.println("connect OK");
		}
		System.out.println(status);
		
		status = mBus.requestName(SERVICE_NAME, 0);
		if (Status.OK != status) {
			System.out.println("BusAttachment.requestName() failed: " + status);

			System.exit(0);
			return;
		} else {
			System.out.println("requestName OK");
		}
		System.out.println("we have a bus... ?");
		
		Client client = new Client();
		client.fireClient();
	}
	
	static class Client {
		public void fireClient(){
			BusAttachment mBus = new BusAttachment(APPNAME);

			Status status = mBus.connect();
			if (Status.OK != status) { 
			   System.out.println("BusAttachment.connect() failed:" + status); 
			   System.exit(0);
			   return;
			}

			ProxyBusObject mProxyObj = mBus.getProxyBusObject(SERVICE_NAME, 
			   "/servicepath",
			   BusAttachment.SESSION_ID_ANY,
			   new Class[] { HelloInterface.class });

			HelloInterface iface = mProxyObj.getInterface(HelloInterface.class);

			status = mBus.registerSignalHandlers(this);
			if (status != Status.OK) {
			   System.out.println("BusAttachment.registerSignalHandlers() failed:" +
			status); 
			   System.exit(0);
			   return;
			}
			
			try {
				System.out.println(iface.GetMyProperty());
			} catch (BusException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
