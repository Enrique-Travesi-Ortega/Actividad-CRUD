package com.bbva.mcwn.lib.r100;

import java.util.List;

import com.bbva.mcwn.dto.holder.AccountDTO;
import com.bbva.mcwn.dto.holder.HolderDTO;

/**
 * The  interface MCWNR100 class...
 */
public interface MCWNR100 {

	/**
	 * The execute method...
	 */
	
	HolderDTO executeCreateAccountAndOwner(HolderDTO holderIn);
	HolderDTO executeRetrieveAccountHolderTransaction(AccountDTO accountIn);
	List<HolderDTO> executeGetAllAccounts();
	HolderDTO executeUpdateAccountHolderInfoTransaction(HolderDTO holderIn);
	Boolean executeManageAccountStatusTransaction(AccountDTO accountIn);



}
