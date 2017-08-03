package com.ma.open.http.client.external.service;

import com.ma.open.http.client.external.request.sender.impl.JerseyHttpRequestSender;

public class OpenHttpClientTest {
	public static void main(String[] args) {
		ILocalInterfaceToRemoteService remoteService = new RemoteServiceAdapter(new JerseyHttpRequestSender());
		Object getOne = remoteService.get("123");
		System.out.println("Jersey-Client GET successful: " + getOne);
		remoteService.getAll();
		if (remoteService.create("This is Jersey-Client POST")) {
			System.out.println("Jersey-Client POST successful");
		}
	}

}
