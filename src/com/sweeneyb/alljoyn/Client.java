package com.sweeneyb.alljoyn;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusAttachment.RemoteMessage;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusListener;
import org.alljoyn.bus.Mutable;
import org.alljoyn.bus.ProxyBusObject;
import org.alljoyn.bus.SessionListener;
import org.alljoyn.bus.SessionOpts;
import org.alljoyn.bus.Status;

import com.sweeneyb.alljoyn.Start;

public class Client {
	private static final short CONTACT_PORT = 42;

	public static void main(String args[]) {
		Client client = new Client();
		client.fireClient();
	}

	public void fireClient() {
		final BusAttachment mBus = new BusAttachment(Start.APPNAME,
				BusAttachment.RemoteMessage.Receive);
		mBus.registerBusListener(new BusListener() {
			@Override
			public void foundAdvertisedName(String name, short transport,
					String namePrefix) {
				mBus.enableConcurrentCallbacks();
				short contactPort = CONTACT_PORT;
				SessionOpts sessionOpts = new SessionOpts();
				Mutable.IntegerValue sessionId = new Mutable.IntegerValue();

				Status status = mBus.joinSession(Start.SERVICE_NAME,
						contactPort, sessionId, sessionOpts,
						new SessionListener());
				if (status == Status.OK) {
					System.out.println("session joined");
				} else {
					System.out.println("session join failed.");
				}
				ProxyBusObject mProxyObj = mBus.getProxyBusObject(
						Start.SERVICE_NAME, "/servicepath",
						BusAttachment.SESSION_ID_ANY,
						new Class[] { HelloInterface.class });

				HelloInterface iface = mProxyObj
						.getInterface(HelloInterface.class);

				status = mBus.registerSignalHandlers(this);
				if (status != Status.OK) {
					System.out
							.println("BusAttachment.registerSignalHandlers() failed:"
									+ status);
					System.exit(0);
					return;
				}

				try {
					System.out.println("property gotten from remote: "+iface.GetMyProperty());
				} catch (BusException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

		status = mBus.findAdvertisedName(Start.SERVICE_NAME);
		if (status != Status.OK) {
			System.out.println("ad name not found");
			System.exit(0);
			return;
		} else {
			System.out.println("ad name found");
		}
	}
}