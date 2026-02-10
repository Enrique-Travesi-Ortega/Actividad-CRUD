package com.bbva.mcwn.lib.r100.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.mcwn.dto.holder.AccountDTO;
import com.bbva.mcwn.dto.holder.AccountInDTO;
import com.bbva.mcwn.dto.holder.HolderDTO;
import com.bbva.mcwn.lib.r101.MCWNR101;

/**
 * The MCWNR100Impl class...
 */
public class MCWNR100Impl extends MCWNR100Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(MCWNR100Impl.class);
	
	 private MCWNR101 mcwnr101;

	@Override
	public HolderDTO executeCreateAccountAndOwner(HolderDTO holderIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HolderDTO executeRetrieveAccountHolderTransaction(AccountDTO accountIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public List<HolderDTO> executeGetAllAccounts() {
    	 LOGGER.info("MCWNR100Impl: Ejecutar metodo de la lista executeGetAllAccounts");
    	 return mcwnR101.executeGetAllAccounts();
    	 
    }

	@Override
	public HolderDTO executeUpdateAccountHolderInfoTransaction(HolderDTO holderIn) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public Boolean executeManageAccountStatusTransaction(AccountDTO accountIn) {

	    LOGGER.info("MCWNR100Impl: executeManageAccountStatusTransaction - start");

	    if (accountIn == null || accountIn.getAccountNumber() == null) {
	        LOGGER.info("MCWNR100Impl: accountNumber is null");
	        this.addAdvice("MCWN01415034", "accountNumber is mandatory");
	        return false;
	    }

	    LOGGER.info("MCWNR100Impl: accountNumber validation OK");

	    Long acc = accountIn.getAccountNumber();
	    
	    if (acc < 10) {
	        this.addAdvice("MCWN01415034", "accountNumber must have 10 digits");
	        return false;
	    }

	    Boolean exists = mcwnR101.executeGetAccountExists(accountIn.getAccountNumber());
	    
	    LOGGER.info("MCWNR100Impl: exists={}", exists);

	    if (Boolean.FALSE.equals(exists)) {
	        this.addAdvice("MCWN01415035", "The account does not exist");
	        return false;
	    }

	    Boolean isActive = mcwnR101.executeGetAccountIsActive(accountIn.getAccountNumber());
	    LOGGER.info("MCWNR100Impl: isActive={}", isActive);

	    if (Boolean.FALSE.equals(isActive)) {
	        this.addAdvice("MCWN01415037", "The account is already inactive");
	        return false;
	    }

	    Boolean updated = mcwnR101.executeUpdateAccountStatus(accountIn.getAccountNumber(), 0);
	    LOGGER.info("MCWNR100Impl: updated={}", updated);

	    if (!updated) {
	        this.addAdvice("MCWN01415041", "Could not update the status");
	        return false;
	    }

	    LOGGER.info("MCWNR100Impl: executeManageAccountStatusTransaction - complete");
	    return true;
	}



}
