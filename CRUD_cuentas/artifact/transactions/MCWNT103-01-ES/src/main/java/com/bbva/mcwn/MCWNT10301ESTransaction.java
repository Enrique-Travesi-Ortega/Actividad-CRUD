package com.bbva.mcwn;

import com.bbva.mcwn.dto.holder.AccountDTO;
import com.bbva.mcwn.dto.holder.HolderDTO;
import com.bbva.mcwn.dto.holder.HolderInDTO;
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
		
		HolderDTO updatedAccount = mcwnR100.executeUpdateAccountHolderInfoTransaction(holder);
		
	}

}
