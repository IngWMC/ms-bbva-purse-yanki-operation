package com.nttdata.bbva.purseyankioperation.services;

public interface ICRUD<T, V> {

	T insert(T obj);
	T update(T obj);
	T findAll();
	T findById(V id);
	Void delete(V id);
}
