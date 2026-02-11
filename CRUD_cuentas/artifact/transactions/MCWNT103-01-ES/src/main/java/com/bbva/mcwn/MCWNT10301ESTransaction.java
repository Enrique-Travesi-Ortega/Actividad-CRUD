package com.bbva.mcwn;

import com.bbva.elara.domain.transaction.Advice;
import com.bbva.elara.domain.transaction.Severity;
import com.bbva.mcwn.dto.holder.AccountDTO;
import com.bbva.mcwn.dto.holder.AccountOutDTO;
import com.bbva.mcwn.dto.holder.HolderDTO;
import com.bbva.mcwn.dto.holder.HolderInDTO;
import com.bbva.mcwn.dto.holder.HolderOutDTO;
import com.bbva.mcwn.lib.r100.MCWNR100;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transacción para la actualización de cuentas
 *
 */
public class MCWNT10301ESTransaction extends AbstractMCWNT10301ESTransaction {

	private static final Logger LOGGER = LoggerFactory.getLogger(MCWNT10301ESTransaction.class);

	@Override
	public void execute() {
		
		LOGGER.info("MCWNT103-01-ES: Entrando a la transaccion");
		MCWNR100 mcwnR100 = this.getServiceLibrary(MCWNR100.class);
		
		HolderInDTO holderIn = getHolder();
		
		HolderDTO holder = new HolderDTO();
		holder.setName(holderIn.getName());
		holder.setLastName(holderIn.getLastName());
		holder.setCurp(holderIn.getCurp());
		holder.setRfc(holderIn.getRfc());
		holder.setAge(holderIn.getAge());
		
		AccountDTO account = new AccountDTO();
		account.setAccountCard(holderIn.getAccountIn().getAccountCard());
		account.setAccountNumber(holderIn.getAccountIn().getAccountNumber());
		account.setBalance(holderIn.getAccountIn().getBalance());
		account.setAccountNip(holderIn.getAccountIn().getAccountNip());
		
		holder.setAccount(account);
		
		HolderDTO newAccount = mcwnR100.executeUpdateAccountHolderInfoTransaction(holder);
		Advice advice = getAdvice();
		
		if (advice == null) {
			HolderOutDTO holderOut = new HolderOutDTO();
			holderOut.setName(newAccount.getName());
			holderOut.setLastName(newAccount.getLastName());
			holderOut.setRfc(newAccount.getRfc());
			holderOut.setCurp(newAccount.getCurp());
			holderOut.setAge(newAccount.getAge());
			holderOut.setHolderType(newAccount.getHolderType());
			
			AccountOutDTO accountOut = new AccountOutDTO();
			accountOut.setAccountCard(newAccount.getAccount().getAccountCard());
			accountOut.setAccountNip(newAccount.getAccount().getAccountNip());
			accountOut.setAccountNumber(newAccount.getAccount().getAccountNumber());
			accountOut.setBalance(newAccount.getAccount().getBalance());
			accountOut.setStatus(newAccount.getAccount().getStatus());
			
			holderOut.setAccountOut(accountOut);
			setHolderout(holderOut);
		}
		else {
			setSeverity(Severity.ENR);
		}
		
	}

}
