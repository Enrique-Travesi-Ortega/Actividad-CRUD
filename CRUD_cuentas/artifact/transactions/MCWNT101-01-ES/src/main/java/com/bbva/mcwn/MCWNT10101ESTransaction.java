package com.bbva.mcwn;

import com.bbva.elara.domain.transaction.Advice;
import com.bbva.elara.domain.transaction.Severity;
import com.bbva.mcwn.dto.holder.AccountDTO;
import com.bbva.mcwn.dto.holder.AccountInDTO;
import com.bbva.mcwn.dto.holder.AccountOutDTO;
import com.bbva.mcwn.dto.holder.HolderDTO;
import com.bbva.mcwn.dto.holder.HolderOutDTO;
import com.bbva.mcwn.lib.r100.MCWNR100;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transacción para la consulta de cuenta
 *
 */
public class MCWNT10101ESTransaction extends AbstractMCWNT10101ESTransaction {

	private static final Logger LOGGER = LoggerFactory.getLogger(MCWNT10101ESTransaction.class);

	/**
	 * The execute method...
	 */
	@Override
	public void execute() {
		
		LOGGER.info("MCWNT101-01-ES inicio ejecución transacción");
		
		AccountInDTO in = getAccountin();

	    // 1) Mapeamos a DTO interno de librería
	    AccountDTO accountIn = new AccountDTO();
	    accountIn.setAccountNumber(in.getAccountNumber());
	    accountIn.setAccountNip(in.getAccountNip());
		
		// Invocamos a la librería
		MCWNR100 mcwnR100 = this.getServiceLibrary(MCWNR100.class);
		HolderDTO holder = mcwnR100.executeRetrieveAccountHolderTransaction(accountIn);
		
		// 3) Consultamos Advice
	    Advice advice = getAdvice();
	    if (advice != null) {
	      setSeverity(Severity.ENR);
	      return; // Con error, no mapeamos salida de éxito
	    }
	    
	    // Mapeamos Holder

	    HolderOutDTO holderOut = new HolderOutDTO();
	    holderOut.setName(holder.getName());
	    holderOut.setLastName(holder.getLastName());
	    holderOut.setAge(holder.getAge());
	    holderOut.setCurp(holder.getCurp());
	    holderOut.setRfc(holder.getRfc());
	    holderOut.setHolderType(holder.getHolderType());


	    // 5) Mapeo account
	    AccountOutDTO accountOut = new AccountOutDTO();
	    accountOut.setAccountNumber(holder.getAccount().getAccountNumber());
	    accountOut.setAccountNip(holder.getAccount().getAccountNip());
	    accountOut.setBalance(holder.getAccount().getBalance());
	    accountOut.setAccountCard(holder.getAccount().getAccountCard());
	    accountOut.setStatus(holder.getAccount().getStatus());
	    
	    // Agrego cuenta dentro del holderOut
	    holderOut.setAccountOut(accountOut);


	    setHolderout(holderOut);

	    LOGGER.info("Transacción finalizada correctamente.");

		
	}

}
