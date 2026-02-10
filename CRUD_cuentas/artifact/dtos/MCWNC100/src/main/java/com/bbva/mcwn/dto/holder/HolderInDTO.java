package com.bbva.mcwn.dto.holder;

import java.io.Serializable;

public class HolderInDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2594614716179719495L;
		
	private String name;
	private String lastName;	
	private String rfc;
	private String curp;
	private Long age;	
	private AccountInDTO accountIn;
	
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

	public AccountInDTO getAccountIn() {
		return accountIn;
	}

	public void setAccountIn(AccountInDTO accountIn) {
		this.accountIn = accountIn;
	}

	@Override
	public String toString() {
		return "HolderInDTO [name=" + name + ", lastName=" + lastName + ", rfc=" + rfc + ", curp=" + curp + ", age="
				+ age + ", accountIn=" + accountIn + "]";
	}
	
	
	

}
