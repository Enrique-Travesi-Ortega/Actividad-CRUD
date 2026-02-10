package com.bbva.mcwn.dto.holder;

import java.io.Serializable;

public class HolderOutDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7489268613743961141L;
	
	private String name;
	private String lastName;
	private String rfc;
	private String curp;
	private Long age;
	private String holderType;
	private AccountOutDTO accountOut;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getRfc() {
		return rfc;
	}
	
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	
	public String getCurp() {
		return curp;
	}
	
	public void setCurp(String curp) {
		this.curp = curp;
	}
	
	public Long getAge() {
		return age;
	}
	
	public void setAge(Long age) {
		this.age = age;
	}
	
	public String getHolderType() {
		return holderType;
	}
	
	public void setHolderType(String holderType) {
		this.holderType = holderType;
	}
	
	public AccountOutDTO getAccountOut() {
		return accountOut;
	}
	
	public void setAccountOut(AccountOutDTO accountOut) {
		this.accountOut = accountOut;
	}
	
	@Override
	public String toString() {
		return "HolderOutDTO [name=" + name + ", lastName=" + lastName + ", rfc=" + rfc + ", curp=" + curp + ", age="
				+ age + ", holderType=" + holderType + "]";
	}
	
	
}
