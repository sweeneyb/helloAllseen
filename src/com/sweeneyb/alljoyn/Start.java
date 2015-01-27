package com.sweeneyb.alljoyn;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.ProxyBusObject;
import org.alljoyn.bus.Status;

public class Start {
	
	public static String APPNAME = "testApp";
	public static String SERVICE_NAME = "com.sweeneyb.alljoyn.hello";

	public static void main(String[] args) {
		System.loadLibrary("alljoyn_java");
		BusAttachment mBus = new BusAttachment(APPNAME);
		HelloInterface hello = new HelloImpl();
		Status status = mBus.connect();
		if (Status.OK != status) {
			System.out.println("BusAttachment.connect() failed: " + status);

			System.exit(0);
			return;
		} else {
			System.out.println("connect OK");
		}
		status = mBus.requestName(SERVICE_NAME, 0);
		if (Status.OK != status) {
			System.out.println("BusAttachment.requestName() failed: " + status);

			System.exit(0);
			return;
		} else {
			System.out.println("requestName OK");
		}
		System.out.println("we have a bus... ?");

//		ProxyBusObject mProxyObj = mBus.getProxyBusObject(
//				"org.my.well.known.name", "/servicepath",
//				BusAttachment.SESSION_ID_ANY,
//				new Class[] { HelloInterface.class });
//
//		HelloInterface myInterface = mProxyObj
//				.getInterface(HelloInterface.class);
//
//		status = mBus.registerSignalHandlers(myInterface);
//		if (status != Status.OK) {
//			System.out.println("BusAttachment.registerSignalHandlers() failed:"
//					+ status);
//			System.exit(0);
//			return;
//		}

		status = mBus.registerBusObject(hello, "/servicepath");
		// mBus.getProxyBusObject("com.sweeneyb.alljoyn.HelloInterface",
		// "/servicepath", BusAttachment.SESSION_ID_ANY, new
		// Class[]{HelloInterface.class});
		if (Status.OK != status) {
			System.out.println("BusAttachment.registerBusObject() failed: "
					+ status);

			System.exit(0);
			return;
		}

		
		System.out.println(status);
		
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
