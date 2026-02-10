package com.bbva.mcwn;

import com.bbva.mcwn.dto.holder.AccountOutDTO;
import com.bbva.mcwn.dto.holder.HolderDTO;
import com.bbva.mcwn.dto.holder.HolderOutDTO;
import com.bbva.mcwn.lib.r100.MCWNR100;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transacción para consulta de todas las cuentas
 *
 */
public class MCWNT10201ESTransaction extends AbstractMCWNT10201ESTransaction {

	private static final Logger LOGGER = LoggerFactory.getLogger(MCWNT10201ESTransaction.class);

	/**
	 * The execute method...
	 */
	@Override
	public void execute() {
		
		 LOGGER.info("MCWNT102-01-ES: Transaction start");
		
		MCWNR100 mcwnR100 = this.getServiceLibrary(MCWNR100.class);
		
		 LOGGER.info("MCWNT102-01-ES: Transaction 102");
			
		 List<HolderDTO> holders = mcwnR100.executeGetAllAccounts();
	   
	     LOGGER.info("MCWNT102-01-ES: holder values: {}", holders);
		
		
	     List<HolderOutDTO> output = holders.stream()
	             .map(holder -> {
	                 HolderOutDTO out = new HolderOutDTO();
 
	                 out.setName(holder.getName());
	                 out.setLastName(holder.getLastName());
	                 out.setAge(holder.getAge());
	                 out.setCurp(holder.getCurp());
	                 out.setRfc(holder.getRfc());
	                 
	                 out.setHolderType(holder.getHolderType());
 
	                 AccountOutDTO accountOut = new AccountOutDTO();
	                 accountOut.setAccountNumber(holder.getAccount().getAccountNumber());
	                 accountOut.setAccountNip(holder.getAccount().getAccountNip());
	                 accountOut.setBalance(holder.getAccount().getBalance());
	                 accountOut.setAccountCard(holder.getAccount().getAccountCard());
	                 accountOut.setStatus(holder.getAccount().getStatus());
 
	                 out.setAccountOut(accountOut);
	                 
	                 LOGGER.info("MCWNT102-01-ES: Output data {}", out);
	                
	                 LOGGER.info("MCWNT102-01-ES: Output data: Name={} LastName={} Age={} Curp={} Rfc={} AccountNumber={}",
	                         out.getName(), out.getLastName(), out.getAge(), out.getCurp(), out.getRfc(),
	                         out.getAccountOut().getAccountNumber());
	                 return out;
	             })
	             .collect(Collectors.toList());
	     setHolders(output);
 
	     LOGGER.info("MCWNT102-01-ES: Transaction complete, {} records sent", output.size());
	     
	}

}
