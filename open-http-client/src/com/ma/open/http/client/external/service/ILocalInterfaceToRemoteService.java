package com.ma.open.http.client.external.service;

public interface ILocalInterfaceToRemoteService {

	Object get(String id);

	void getAll();

	boolean create(Object newObject);

	boolean remove(String id);

}
