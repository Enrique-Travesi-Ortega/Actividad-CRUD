package com.bbva.mcwn;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.domain.transaction.Advice;
import com.bbva.elara.domain.transaction.Severity;
import com.bbva.mcwn.dto.holder.AccountDTO;
import com.bbva.mcwn.dto.holder.AccountInDTO;
import com.bbva.mcwn.lib.r100.MCWNR100;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transacción para la eliminación de cuenta
 *
 */
public class MCWNT10401ESTransaction extends AbstractMCWNT10401ESTransaction {

	private static final Logger LOGGER = LoggerFactory.getLogger(MCWNT10401ESTransaction.class);

	@Override
	public void execute() {

	    LOGGER.info("MCWNT104-01-ES: Transaction start (Logical delete of account)");

	    AccountInDTO in = this.getAccountin();

	    if (in == null || in.getAccountNumber() == null) {
	        LOGGER.warn("MCWNT104-01-ES: accountNumber is null");
	        this.addAdvice("MCWN01415034", "accountNumber is mandatory");
	        return;
	    }

	    LOGGER.info("MCWNT104-01-ES: accountNumber received={}", in.getAccountNumber());

	    MCWNR100 r100 = this.getServiceLibrary(MCWNR100.class);

	    AccountDTO orchestrationIn = new AccountDTO();
	    orchestrationIn.setAccountNumber(in.getAccountNumber());

	    LOGGER.info("MCWNT104-01-ES: orchestrationIn.accountNumber={}",
	                orchestrationIn.getAccountNumber());

	    Boolean deleted = r100.executeManageAccountStatusTransaction(orchestrationIn);
	    
	    Advice advice = getAdvice();
	    
	    if (advice == null) {
	    	this.setDeleted(Boolean.TRUE.equals(deleted));

		    LOGGER.info("MCWNT104-01-ES: Transaction complete, accountNumber={} deleted={}",
		                in.getAccountNumber(), deleted);
	    }
	    else {
	    	if (advice.getCode().equals("MCWN01415035")) {
	    		setSeverity(Severity.RESOURCE_NOT_FOUND_ENR);
	    	}else {
	    		setSeverity(Severity.ENR);
	    	}
	    }
	}

	
}