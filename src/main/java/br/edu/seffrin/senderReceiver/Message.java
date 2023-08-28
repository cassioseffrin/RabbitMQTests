package br.edu.seffrin.senderReceiver;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = -4830937675487871354L;

	private final String mName;

	private final String mContent;

	private final String mTime;

	public Message(String name, String content, String time) {
		mName = name;
		mContent = content;
		mTime = time;
	}

	public String getName() {
		return mName;
	}

	public String getContent() {
		return mContent;
	}

	public String getTime() {
		return mTime;
	}
}