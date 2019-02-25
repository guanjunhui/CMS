package cn.cebest.entity;

import java.io.Serializable;

public class Node<T> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private T data;
	private Node<T> prev;
	
	public Node(){
	}
	public Node(T data){
		this.data=data;
	}
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public Node<T> getPrev() {
		return prev;
	}
	public void setPrev(Node<T> prev) {
		this.prev = prev;
	}
}
