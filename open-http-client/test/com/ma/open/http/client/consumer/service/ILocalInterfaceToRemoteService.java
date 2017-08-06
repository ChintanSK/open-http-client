package com.ma.open.http.client.consumer.service;

import java.util.function.Supplier;

public interface ILocalInterfaceToRemoteService {

	Object get(String id);

	Supplier<Object> getAll();

	boolean create(Object newObject);

	boolean remove(String id);

	boolean createWithRetryAttempts(Object newObject);

}
