package com.sweeneyb.alljoyn;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusAttachment.RemoteMessage;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusListener;
import org.alljoyn.bus.Mutable;
import org.alljoyn.bus.ProxyBusObject;
import org.alljoyn.bus.SessionListener;
import org.alljoyn.bus.SessionOpts;
import org.alljoyn.bus.SessionPortListener;
import org.alljoyn.bus.Status;

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

		status = mBus.connect();
		if (Status.OK != status) {
			System.out.println("BusAttachment.connect() failed: " + status);

			System.exit(0);
			return;
		} else {
			System.out.println("connect OK");
		}
		System.out.println(status);

		final short CONTACT_PORT = 42;
		Mutable.ShortValue contactPort = new Mutable.ShortValue(CONTACT_PORT);
		SessionOpts sessionOpts = new SessionOpts();
		sessionOpts.traffic = SessionOpts.TRAFFIC_MESSAGES;
		sessionOpts.isMultipoint = false;
		sessionOpts.proximity = SessionOpts.PROXIMITY_ANY;
		sessionOpts.transports = SessionOpts.TRANSPORT_ANY;

		status = mBus.bindSessionPort(contactPort, sessionOpts,
				new SessionPortListener() {
					@Override
					public boolean acceptSessionJoiner(short sessionPort,
							String joiner, SessionOpts sessionOpts) {
						if (sessionPort == CONTACT_PORT) {
							return true;
						} else {
							return false;
						}
					}
				});

		status = mBus.requestName(SERVICE_NAME, 0);
		if (Status.OK != status) {
			System.out.println("BusAttachment.requestName() failed: " + status);

			System.exit(0);
			return;
		} else {
			System.out.println("requestName OK");
		}
		System.out.println("we have a bus... ?");

		status = mBus.advertiseName(SERVICE_NAME, SessionOpts.TRANSPORT_ANY);
		if (status != Status.OK) {
			System.out.println("unable to advertise name.");
			/*
			 * If we are unable to advertise the name, release the well-known
			 * name from the local bus.
			 */
			mBus.releaseName(SERVICE_NAME);
			System.exit(0);
			return;
		}
		if (status != Status.OK) {
			System.exit(0);
			return;
		}
		System.out.println("client firing.");
		Client client = new Client();
		client.fireClient();
	}

	static class Client {
		private static final short CONTACT_PORT=42;
		public void fireClient() {
			final BusAttachment mBus = new BusAttachment(APPNAME,
					RemoteMessage.Receive);
			mBus.registerBusListener(new BusListener() {
				@Override
				public void foundAdvertisedName(String name, short transport,
						String namePrefix) {
					mBus.enableConcurrentCallbacks();
					short contactPort = CONTACT_PORT;
					SessionOpts sessionOpts = new SessionOpts();
					Mutable.IntegerValue sessionId = new Mutable.IntegerValue();

					Status status = mBus.joinSession(SERVICE_NAME,
							contactPort, sessionId, sessionOpts,
							new SessionListener());
					if(status == Status.OK){
						System.out.println("session joined");
					} else {
						System.out.println("session join failed.");
					}
				}
			});
			System.out.println("connecting client...");
			Status status = mBus.connect();
			if (Status.OK != status) {
				System.out.println("BusAttachment.connect() failed:" + status);
				System.exit(0);
				return;
			}

//			ProxyBusObject mProxyObj = mBus.getProxyBusObject(SERVICE_NAME,
//					"/servicepath", BusAttachment.SESSION_ID_ANY,
//					new Class[] { HelloInterface.class });
//
//			HelloInterface iface = mProxyObj.getInterface(HelloInterface.class);
//
//			status = mBus.registerSignalHandlers(this);
//			if (status != Status.OK) {
//				System.out
//						.println("BusAttachment.registerSignalHandlers() failed:"
//								+ status);
//				System.exit(0);
//				return;
//			}
//
//			try {
//				System.out.println(iface.GetMyProperty());
//			} catch (BusException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			status = mBus.findAdvertisedName(SERVICE_NAME);
			if (status != Status.OK) {
				System.out.println("ad name not found");
			   System.exit(0);
			   return;
			} else {
				System.out.println("ad name found");
			}
		}
	}
}
