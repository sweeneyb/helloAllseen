package com.sweeneyb.alljoyn;

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
		return "foo";
	}

	@Override
	public void SetMyProperty(String myProperty) throws BusException {
		// TODO Auto-generated method stub

	}

}
