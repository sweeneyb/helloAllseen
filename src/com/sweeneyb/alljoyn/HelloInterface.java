package com.sweeneyb.alljoyn;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusObject;
import org.alljoyn.bus.annotation.BusInterface;
import org.alljoyn.bus.annotation.BusMethod;
import org.alljoyn.bus.annotation.BusProperty;
import org.alljoyn.bus.annotation.BusSignal;

@BusInterface(name = "com.sweeneyb.alljoyn.HelloInterface")
public interface HelloInterface extends BusObject {

	@BusMethod
	public String MyMethod(String inStr) throws BusException;

	@BusSignal
	public void MySignal(String inStr) throws BusException;

	@BusMethod
	public String GetMyProperty() throws BusException;

	@BusSignal
	public void SetMyProperty(String myProperty) throws BusException;

}
