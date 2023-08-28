package br.edu.seffrin.senderReceiver;

import java.io.Serializable;

public class Connection implements Serializable {

	private static final long serialVersionUID = -6893634578516949025L;

	private final boolean mIsConnecting;

	private final String mName;

	public Connection(boolean isConnecting, String name) {
		mIsConnecting = isConnecting;
		mName = name;
	}

	public boolean isIsConnecting() {
		return mIsConnecting;
	}

	public String getName() {
		return mName;
	}
}