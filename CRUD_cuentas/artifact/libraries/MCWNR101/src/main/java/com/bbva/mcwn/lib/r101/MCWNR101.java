package com.bbva.mcwn.lib.r101;

import java.util.List;

import com.bbva.mcwn.dto.holder.AccountDTO;
import com.bbva.mcwn.dto.holder.HolderDTO;

/**
 * The  interface MCWNR101 class...
 */
public interface MCWNR101 {

	/**
	 * The execute method...
	 */
	HolderDTO executeCreateAccountAndOwner(HolderDTO holderIn);
	HolderDTO executeRetrieveAccountHolderTransaction(Long accountNumber);
	List<HolderDTO> executeGetAllAccounts();
	HolderDTO executeUpdateAccountHolderInfoTransaction(HolderDTO holderIn);
	
	Integer checkStatusWithAccountNumber(Long accountNumber);
	Integer checkStatusWithAccountCard(Long accountCard);
	Boolean executeUpdateAccountStatus(Long accountNumber, int status);
	Boolean executeGetAccountIsActive(Long accountNumber);
	Boolean executeGetAccountExists(Long accountNumber);

}
