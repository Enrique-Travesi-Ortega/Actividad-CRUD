package com.bbva.mcwn.lib.r100.impl;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbva.elara.domain.transaction.RequestHeaderParamsName;
import com.bbva.mcwn.dto.holder.AccountDTO;
import com.bbva.mcwn.dto.holder.HolderDTO;

/**
 * The MCWNR100Impl class...
 */
public class MCWNR100Impl extends MCWNR100Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(MCWNR100Impl.class);
	private static final String MCWN01415034 = "MCWN01415034";

	@Override
	public HolderDTO executeCreateAccountAndOwner(HolderDTO holderIn) {
		String clientType = 
			(String) this.getRequestHeader().getHeaderParameter(RequestHeaderParamsName.CLIENTDOCUMENT);
		
		if(!validateClientType(holderIn, clientType))
			return null;
		
		if (!validParameters(holderIn))
			return null;
		
		String max_balance_string = applicationConfigurationService.getProperty("MAX_BALANCE");
		Double max_balance = Double.parseDouble(max_balance_string);
		
		if (max_balance < holderIn.getAccount().getBalance()) {
			this.addAdvice("MCWN01415048");
		}
		long randomNip = (long) (1000 + Math.random() * 9000);
		holderIn.getAccount().setAccountNip(randomNip);
		
		if (checkAccountExists(holderIn.getAccount().getAccountNumber(), holderIn.getAccount().getAccountCard()))
			return null;
		
		HolderDTO newAccount = mcwnR101.executeCreateAccountAndOwner(holderIn);
		if (newAccount == null) {
			getAdvice();
			return null;
		}
		
		if (clientType.equals("0"))
			newAccount.setHolderType("FISICO");
		else if (clientType.equals("1"))
			newAccount.setHolderType("MORAL");
		
		return newAccount;
	}

	@Override
public HolderDTO executeRetrieveAccountHolderTransaction(AccountDTO accountIn) {
		
		LOGGER.info("MCWNR100IMPL inicio ejecución de executeRetrieveAccountHolderTransaction");
		
				
		//Objeto vacío
		HolderDTO empty = new HolderDTO();
		
		//Validaciones datos obligatorios (Nº de cuenta y el NIP)
		if (accountIn == null || accountIn.getAccountNumber() == null || accountIn.getAccountNip() == null) {
		      this.addAdvice("MCWN01415034");
		      return empty;
		    }
		//Validaciones de longitud (Nº de cuenta y el NIP)
		String acctStr = String.valueOf(accountIn.getAccountNumber());
	    if (acctStr.length() != 10) {
	      this.addAdvice("MCWN01415029");
	      return empty;
	    }
	    
	    long nip = accountIn.getAccountNip();
	    String nipStr = String.valueOf(nip);
	    if (nipStr.length() != 4) {
	      this.addAdvice("MCWN01415029");
	      return empty;
	    }	
	    
	    //Recibimos una Objeto con la cuenta y lo consultamos con la db
	 		HolderDTO data = mcwnR101.executeRetrieveAccountHolderTransaction(accountIn.getAccountNumber());
	 		
	 		if (data == null) {
	 		      this.addAdvice("MCWN01415035");
	 		      return null;
	 		    }		
		
		//Validar estado de la cuenta (Boolean: False = inactiva y True: activa)
		Boolean status = data.getAccount().getStatus();
		Boolean isActive = Boolean.TRUE.equals(status);
		
		if (!isActive) {
		    this.addAdvice("MCWN01415044");
		    return empty;
		}			
		
		//Validación de existencia del AccountNip
		
		Long expectedNip = (data.getAccount() != null) ? data.getAccount().getAccountNip() : null;
	    if (expectedNip == null || !expectedNip.equals(accountIn.getAccountNip())) {
	      this.addAdvice("MCWN01415036");
	      return empty;
	    }
	    
	        
	    LOGGER.info("MCWNR100IMPL fin ejecución de executeRetrieveAccountHolderTransaction");
	    
	    return data;
 
	  }

	@Override
	public List<HolderDTO> executeGetAllAccounts() {
		LOGGER.info("MCWNR100Impl: Ejecutar metodo de la lista executeGetAllAccounts");
   	 	return mcwnR101.executeGetAllAccounts();
	}

	@Override
	public HolderDTO executeUpdateAccountHolderInfoTransaction(HolderDTO holderIn) {
		
		LOGGER.info("Entrando en el metodo de la 100");
		
		if (holderIn.getAccount() == null) {
		      this.addAdvice(MCWN01415034, "account");
		      return null;
		    }
		
		if (holderIn.getAccount().getAccountNumber() == null) {
		      this.addAdvice(MCWN01415034, "accountNumber");
		      return null;
		    }
		
		String clientType = 
				(String) this.getRequestHeader().getHeaderParameter(RequestHeaderParamsName.CLIENTDOCUMENT);
		
		LOGGER.info("Recupero header de client document para establecer client type");
		
		if(!validateClientType(holderIn, clientType))
			return null;
		
		LOGGER.info("Hemos hecho las validaciones del clientType");
		
		HolderDTO oldHolder = mcwnR101.executeRetrieveAccountHolderTransaction(holderIn.getAccount().getAccountNumber());
		
		LOGGER.info("Obtengo el holder antiguo que quiero modificar");
		
		if (validateUpdateCommonParams(holderIn, oldHolder) == null)
			return null;
		if (clientType.equals("0")) {
			if(validateUpdatePhysicPerson(holderIn, oldHolder) == null)
				return null;
		}else {
			if(validateUpdateMoralPerson(holderIn, oldHolder) == null)
				return null;
		}
		
		LOGGER.info("Hemos hecho las validaciones principales");
		
		if(!oldHolder.getAccount().getAccountCard().equals(holderIn.getAccount().getAccountCard())) {
			LOGGER.info("La credit cards han cambiado por lo que voy a mirar si ya existe la nueva en la BBDD");
			
			Integer statusWithAccountCard = 
					mcwnR101.checkStatusWithAccountCard(holderIn.getAccount().getAccountCard());
			
			if (Objects.equals(statusWithAccountCard, 1) || Objects.equals(statusWithAccountCard, 0)) {
				this.addAdvice("MCWN01415039");
				return null;
			}
		}
		
		LOGGER.info("He llegado a al metodo donde preparo holder para update");
		
		HolderDTO updateHolder = prepareHolderForUpdate(oldHolder, holderIn);
		
		LOGGER.info("He salido del metodo donde preparo holder para update");
		
		HolderDTO finalHolder = mcwnR101.executeUpdateAccountHolderInfoTransaction(updateHolder);
		getAdvice();
		
		if (clientType.equals("0"))
			finalHolder.setHolderType("FISICO");
		else if (clientType.equals("1"))
			finalHolder.setHolderType("MORAL");
		
		LOGGER.info("VOLVIMOOOOS");
		
		
		return finalHolder;
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

	private Boolean validParameters(HolderDTO holder) {
		
		LOGGER.info("MCWNR100Impl: Validating parameters");
		
		if (StringUtils.isBlank(holder.getName())) {
		      this.addAdvice(MCWN01415034, "name");
		      return false;
		    }
		
		if (StringUtils.isBlank(holder.getRfc())) {
		      this.addAdvice(MCWN01415034, "rfc");
		      return false;
		    }
		
		if (holder.getAccount() == null) {
		      this.addAdvice(MCWN01415034, "account");
		      return false;
		    }
		
		if (holder.getAccount().getAccountCard() == null) {
		      this.addAdvice(MCWN01415034, "accountCard");
		      return false;
		    }
		
		if (holder.getAccount().getAccountNumber() == null) {
		      this.addAdvice(MCWN01415034, "accountNumber");
		      return false;
		    }
		if (holder.getAccount().getBalance() == null) {
		      this.addAdvice(MCWN01415034, "balance");
		      return false;
		    }
		if (holder.getHolderType().equals("FISICO")) {
			
			 if (StringUtils.isBlank(holder.getLastName())) {
			      this.addAdvice(MCWN01415034, "lastName");
			      return false;
			    }

			    if (StringUtils.isBlank(holder.getCurp())) {
			      this.addAdvice(MCWN01415034, "curp");
			      return false;
			    }

			    if (holder.getAge() == null) {
			      this.addAdvice(MCWN01415034, "age");
			      return false;
			    }

			    if (holder.getCurp().length() != 18) {
			      this.addAdvice("MCWN01415031");
			      return false;
			    }

			    if (holder.getRfc().length() != 13) {
			      this.addAdvice("MCWN01415032");
			      return false;
			    }
		}
		
		if (holder.getHolderType().equals("MORAL")) {
			if (holder.getRfc().length() != 12) {
			      this.addAdvice("MCWN01415033");
			      return false;
			    }
		}
		
		return true;
	}
	
	private Boolean checkAccountExists(Long accountNumber, Long accountCard) {
		
	Integer statusWithAccountNumber = 
			mcwnR101.checkStatusWithAccountNumber(accountNumber);

	if (Objects.equals(statusWithAccountNumber, 1)) {
		this.addAdvice("MCWN01415038");
		return true;
	}
	if(Objects.equals(statusWithAccountNumber, 0)) {
		this.addAdvice("MCWN01415042");
		return true;
	}
	
	Integer statusWithAccountCard = 
			mcwnR101.checkStatusWithAccountCard(accountCard);
	
	if (Objects.equals(statusWithAccountCard, 1)) {
		this.addAdvice("MCWN01415039");
		return true;
	}
	if(Objects.equals(statusWithAccountCard, 0)) {
		this.addAdvice("MCWN01415043");
		return true;
	}
	
	return false;
	}
	
	private Boolean validateClientType(HolderDTO holder, String clientType) {
			
		if (clientType.equals("0"))
			holder.setHolderType("0");
		else if (clientType.equals("1"))
			holder.setHolderType("1");
		else {
			this.addAdvice("MCWN01415030");
			return false;
		}
		return true;
	}
	
	private HolderDTO validateUpdateCommonParams(HolderDTO newHolder, HolderDTO oldHolder) {
		
		if (StringUtils.isBlank(newHolder.getName()) && StringUtils.isBlank(oldHolder.getName())){
		      this.addAdvice(MCWN01415034, "name");
		      return null;
		}
		
		if (StringUtils.isBlank(newHolder.getRfc()) && StringUtils.isBlank(oldHolder.getRfc())){
		      this.addAdvice(MCWN01415034, "rfc");
		      return null;
		}
		
		if (oldHolder.getAccount() == null && newHolder.getAccount() == null) {
		      this.addAdvice(MCWN01415034, "account");
		      return null;
		    }
		
		if (oldHolder.getAccount().getAccountCard() == null && newHolder.getAccount().getAccountCard() == null) {
		      this.addAdvice(MCWN01415034, "accountCard");
		      return null;
		    }
		
		if (oldHolder.getAccount().getAccountNumber() == null && newHolder.getAccount().getAccountNumber() == null) {
		      this.addAdvice(MCWN01415034, "accountNumber");
		      return null;
		    }
		if (oldHolder.getAccount().getBalance() == null && newHolder.getAccount().getBalance() == null) {
		      this.addAdvice(MCWN01415034, "balance");
		      return null;
		    }
		
		return newHolder;
	}
	
	
	
	private HolderDTO validateUpdatePhysicPerson(HolderDTO newHolder, HolderDTO oldHolder) {
		
		if (StringUtils.isBlank(newHolder.getLastName()) && StringUtils.isBlank(oldHolder.getLastName())){
		      this.addAdvice(MCWN01415034, "lastName");
		      return null;
		}
		
		if (StringUtils.isBlank(newHolder.getCurp()) && StringUtils.isBlank(oldHolder.getCurp())){
		      this.addAdvice(MCWN01415034, "curp");
		      return null;
		}
		
		if (oldHolder.getAge() == null && newHolder.getAge() == null) {
		      this.addAdvice(MCWN01415034, "age");
		      return null;
		}
		
		if (newHolder.getCurp().length() != 18) {
		      this.addAdvice("MCWN01415031");
		      return null;
		}

	    if (newHolder.getRfc().length() != 13) {
		      this.addAdvice("MCWN01415032");
		      return null;
	    }
		
		
		return newHolder;
	}
	
	private HolderDTO validateUpdateMoralPerson(HolderDTO newHolder, HolderDTO oldHolder) {
		
		Boolean advice = false;
		
		if (!newHolder.getLastName().isBlank()) {
			this.addAdvice("MCWN01415045", "lastName");
			newHolder.setLastName("NO_APLICA");
			advice = true;
			
		}
		if (!newHolder.getCurp().isBlank()) {
			this.addAdvice("MCWN01415045", "curp");
			newHolder.setCurp("NO_APLICA");
			advice = true;
		}
		if (newHolder.getAge() != null) {
			this.addAdvice("MCWN01415045", "age");
			newHolder.setAge(0L);
			advice = true;
		}
		
		if (advice == true)
			return null;
		
		if (newHolder.getRfc().length() != 12) {
		      this.addAdvice("MCWN01415033");
		      return null;
		    }
		
		
		return newHolder;
	}
	
	private HolderDTO prepareHolderForUpdate(HolderDTO oldHolder, HolderDTO newHolder) {
		HolderDTO readyHolder = new HolderDTO();
		AccountDTO readyAccount = new AccountDTO();
		
		LOGGER.info("INFO DEL HOLDER EN BBDD: {}", oldHolder);
		LOGGER.info("INFO DEL HOLDER NUEVO: {}", newHolder);
		
		if(!oldHolder.getName().equals(newHolder.getName())) {
			readyHolder.setName(newHolder.getName());
		}
		
		if(!oldHolder.getLastName().equals(newHolder.getLastName())) {
			readyHolder.setLastName(newHolder.getLastName());
		}
		
		if(!oldHolder.getCurp().equals(newHolder.getCurp())) {
			readyHolder.setCurp(newHolder.getCurp());
		}
		
		if(!oldHolder.getRfc().equals(newHolder.getRfc())) {
			readyHolder.setRfc(newHolder.getRfc());
		}
		
		if(!oldHolder.getAge().equals(newHolder.getAge())) {
			readyHolder.setAge(newHolder.getAge());
		}
		
		if(!oldHolder.getAccount().getAccountCard().equals(newHolder.getAccount().getAccountCard())) {
			readyAccount.setAccountCard(newHolder.getAccount().getAccountCard());
		}
		
		if(!oldHolder.getAccount().getAccountNip().equals(newHolder.getAccount().getAccountNip())) {
			readyAccount.setAccountNip(newHolder.getAccount().getAccountNip());
		}
		
		if(!oldHolder.getAccount().getBalance().equals(newHolder.getAccount().getBalance())) {
			readyAccount.setBalance(newHolder.getAccount().getBalance());
		}
		
		if(!oldHolder.getAccount().getStatus().equals(newHolder.getAccount().getStatus())) {
			readyAccount.setStatus(newHolder.getAccount().getStatus());
		}
		
		readyHolder.setAccount(readyAccount);
		
		LOGGER.info("ESTADO DEL HOLDER QUE QUEREMOS CAMBIAR: {}", readyHolder);
		
		return newHolder;
		
	}
	
}
