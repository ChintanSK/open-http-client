package com.ma.open.http.client.request;

import com.ma.open.http.client.consumer.service.ILocalInterfaceToRemoteService;
import com.ma.open.http.client.consumer.service.RemoteServiceAdapter;
import com.ma.open.http.client.request.sender.JerseyHttpRequestSender;

public class OpenHttpClientTest {
	public static void main(String[] args) {
		ILocalInterfaceToRemoteService remoteService1 = new RemoteServiceAdapter(new JerseyHttpRequestSender());

		Object o1 = remoteService1.get("123");
		System.out.println("Jersey-Client GET successful: " + o1);

		Object o2 = remoteService1.getAll().get();
		System.out.println("Jersey-Client GET All successful: " + o2);

		if (remoteService1.create("This is Jersey-Client POST")) {
			System.out.println("Jersey-Client POST successful");
		}

		System.out.println();

		ILocalInterfaceToRemoteService remoteService2 = new RemoteServiceAdapter(new JerseyHttpRequestSender());

		Object o3 = remoteService2.get("123");
		System.out.println("Apache-Http-Client GET successful: " + o3);

		Object o4 = remoteService2.getAll().get();
		System.out.println("Apache-Http-Client GET All successful: " + o4);

		if (remoteService2.create("This is Jersey-Client POST")) {
			System.out.println("Apache-Http-Client POST successful");
		}
	}

}
