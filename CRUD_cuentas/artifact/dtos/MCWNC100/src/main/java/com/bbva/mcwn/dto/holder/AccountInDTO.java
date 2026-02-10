package com.bbva.mcwn.dto.holder;

import java.io.Serializable;

public class AccountInDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5935162425929640571L;
	
	private Long accountNumber;
	private Long accountNip;
	private Long accountCard;
	private Double balance;
	
	public Long getAccountNumber() {
		return accountNumber;
	}
	
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public Long getAccountNip() {
		return accountNip;
	}
	
	public void setAccountNip(Long accountNip) {
		this.accountNip = accountNip;
	}
	
	public Long getAccountCard() {
		return accountCard;
	}
	
	public void setAccountCard(Long accountCard) {
		this.accountCard = accountCard;
	}
	
	public Double getBalance() {
		return balance;
	}
	
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return "AccountInDTO [accountNumber=" + accountNumber + ", accountNip=" + accountNip + ", accountCard="
				+ accountCard + ", balance=" + balance + "]";
	}
	

}
