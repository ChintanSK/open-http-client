package com.ma.open.http.client.request;

import com.ma.open.http.client.consumer.service.ILocalInterfaceToRemoteService;
import com.ma.open.http.client.consumer.service.RemoteServiceAdapter;
import com.ma.open.http.client.request.sender.SuccessResponder;
import com.ma.open.http.client.request.sender.RetryAfterRedirectionResponder;

public class OpenHttpClientTest {
	public static void main(String[] args) {
		try {
			ILocalInterfaceToRemoteService remoteService1 = new RemoteServiceAdapter(new RetryAfterRedirectionResponder());

			Object o1 = remoteService1.get("123");
			System.out.println("Jersey-Client GET successful: " + o1);

			Object o2 = remoteService1.getAll().get();
			System.out.println("Jersey-Client GET All successful: " + o2);

			if (remoteService1.create("This is Jersey-Client POST")) {
				System.out.println("Jersey-Client POST successful");
			}
			if (remoteService1.createWithRetryAttempts("This is Jersey-Client Retriable POST")) {
				System.out.println("Jersey-Client Retriable POST successful");
			} else {
				System.out.println("After multiple retry attempts Jersey-Client Retriable POST failed");
			}
			System.out.println();

			ILocalInterfaceToRemoteService remoteService2 = new RemoteServiceAdapter(new SuccessResponder());

			Object o3 = remoteService2.get("123");
			System.out.println("Apache-Http-Client GET successful: " + o3);

			Object o4 = remoteService2.getAll().get();
			System.out.println("Apache-Http-Client GET All successful: " + o4);

			if (remoteService2.create("This is Apache-Http-Client POST")) {
				System.out.println("Apache-Http-Client POST successful");
			}
			if (remoteService2.createWithRetryAttempts("This is Apache-Http-Client Retriable POST")) {
				System.out.println("Apache-Http-Client Retriable POST successful");
			} else {
				System.out.println("After multiple retry attempts Apache-Http-Client Retriable POST failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			System.exit(0);
		}
	}

}
