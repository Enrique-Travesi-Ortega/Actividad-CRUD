package com.bbva.mcwn.dto.holder;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The HolderDTO class...
 */
public class HolderDTO implements Serializable  {
	
	private static final long serialVersionUID = 393398932927027329L;
	
	private String name;
	private String lastName;
	private String rfc;
	private String curp;
	private Long age;
	private String holderType;
	private AccountDTO account;
	
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
	
	public AccountDTO getAccount() {
		return account;
	}
	
	public void setAccount(AccountDTO account) {
		this.account = account;
	}
	
	@Override
	public String toString() {
		return "HolderDTO [name=" + name + ", lastName=" + lastName + ", rfc=" + rfc + ", curp=" + curp + ", age=" + age
				+ ", holderType=" + holderType + ", account=" + account + "]";
	}
	
	
}
