package com.bbva.mcwn.dto.holder;

import java.io.Serializable;

public class AccountDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3503127249103190568L;
	
	private Long accountNumber;
	private Long accountNip;
	private Long accountCard;
	private Double balance;
	private Boolean status;
	
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
	
	public Boolean getStatus() {
		return status;
	}
	
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "AccountDTO [accountNumber=" + accountNumber + ", accountNip=" + accountNip + ", accountCard="
				+ accountCard + ", balance=" + balance + ", status=" + status + "]";
	}
	

}
