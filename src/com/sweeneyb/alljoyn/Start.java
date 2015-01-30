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
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// ignore;
			}
		}
//		 Client client = new Client();
//		 client.fireClient();
	}



}
