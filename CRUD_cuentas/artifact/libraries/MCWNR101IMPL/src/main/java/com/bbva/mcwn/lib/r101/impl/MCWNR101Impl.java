package com.bbva.mcwn.lib.r101.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;

import com.bbva.apx.exception.db.NoResultException;
import com.bbva.mcwn.dto.holder.AccountDTO;
import com.bbva.mcwn.dto.holder.HolderDTO;

/**
 * The MCWNR101Impl class...
 */
public class MCWNR101Impl extends MCWNR101Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(MCWNR101Impl.class);

	@Override
	public HolderDTO executeCreateAccountAndOwner(HolderDTO holderIn) {

		Map<String, Object> holder = new HashMap<>();
		holder.put("FIRST_NAME", holderIn.getName());
		holder.put("LAST_NAME", holderIn.getLastName());
		holder.put("RFC", holderIn.getRfc());
		holder.put("CURP", holderIn.getCurp());
		holder.put("AGE",holderIn.getAge());
		holder.put("ACCOUNT_NUMBER", holderIn.getAccount().getAccountNumber());
		holder.put("NIP", holderIn.getAccount().getAccountNip());
		holder.put("CARD_NUMBER", holderIn.getAccount().getAccountCard());
		holder.put("BALANCE", holderIn.getAccount().getBalance());
		holder.put("CLIENT_TYPE", holderIn.getHolderType());
		holder.put("STATUS", 1);
		
		Integer affectedRows = jdbcUtils.update("insert.account", holder);
		if (affectedRows == 0) {
			this.addAdvice("MCWN01415040");
			return null;
		}
		
		HolderDTO newAccount = executeRetrieveAccountHolderTransaction(holderIn.getAccount().getAccountNumber());
		
		return newAccount;
	}

	@Override
public HolderDTO executeRetrieveAccountHolderTransaction(Long accountNumber) {
		
		LOGGER.info("MCWNR101IMPL inicio ejecución de executeRetrieveAccountHolderTransaction");
		
		Map<String, Object> args = new HashMap<>();
	    args.put("accountNumber", BigDecimal.valueOf(accountNumber));
 
	    try {
	      Map<String, Object> accountData = jdbcUtils.queryForMap("select.account.by.accountNumber", args);
 
 
	      LOGGER.info("Accountsdata: {}", accountData);
 
	      if (accountData != null && !accountData.isEmpty()) {
 
	        HolderDTO holder = new HolderDTO();
	        holder.setName((String) accountData.get("FIRST_NAME"));
	        holder.setLastName((String) accountData.get("LAST_NAME"));
	        holder.setAge(parseBigDecimalToLong(accountData.get("AGE")));
	        holder.setCurp((String) accountData.get("CURP"));
	        holder.setRfc((String) accountData.get("RFC"));
	        Long clientType = parseBigDecimalToLong(accountData.get("CLIENT_TYPE"));
	        holder.setHolderType (clientType !=null && clientType == 0L ? "Fisico" : "Moral" );
 
 
	        AccountDTO innerAccount = new AccountDTO();
 
	        innerAccount.setAccountNumber(parseBigDecimalToLong(accountData.get("ACCOUNT_NUMBER")));
	        innerAccount.setAccountNip(parseBigDecimalToLong(accountData.get("NIP")));	        
	        innerAccount.setBalance(parseBigDecimalToDouble(accountData.get("BALANCE")));
	        innerAccount.setAccountCard(parseBigDecimalToLong(accountData.get("CARD_NUMBER")));
	        innerAccount.setStatus(parseBigDecimalToBoolean(accountData.get("STATUS")));
 
	        holder.setAccount(innerAccount);
	        LOGGER.info("Holder name: {}", holder.getLastName());
	        System.out.println(holder.toString());
	        return holder;
	      }
 
	    }
 
	    catch (NoResultException e) {
	      LOGGER.info("No account found for number={}", accountNumber);
	      return null;
 
	    }
	    return null;
 
 
	  }

	@Override
	public List<HolderDTO> executeGetAllAccounts() {
		Map<String,Object> args = new HashMap<>();
		args.put("minStatus", 0);
		args.put("maxStatus", 1);
	
	    List<HolderDTO> accountsList = new ArrayList<>();
 
	    try {
	        List<Map<String,Object>> accountsData =
	                jdbcUtils.queryForList("select.accounts.by.status", args);
 
	        LOGGER.info("accountsData {}", accountsData);
 
	        if (accountsData != null && !accountsData.isEmpty()) {
 
	            for (Map<String,Object> account : accountsData) {
 
	                HolderDTO holder = new HolderDTO();
	                holder.setName((String) account.get("FIRST_NAME"));
	                holder.setLastName((String) account.get("LAST_NAME"));
	                holder.setAge(parseBigDecimalToLong(account.get("AGE")));
	                holder.setCurp((String) account.get("CURP"));
	                holder.setRfc((String) account.get("RFC"));
 
	                Integer clientType = parseBigDecimalToInt(account.get("CLIENT_TYPE"));
	                if (clientType != null) {
	                    if (clientType == 0) {
	                        holder.setHolderType("FISICO");
	                    } else if (clientType == 1) {
	                        holder.setHolderType("MORAL");
	                    }
	                }
 
	                AccountDTO innerAccount = new AccountDTO();
	                innerAccount.setAccountNumber(parseBigDecimalToLong(account.get("ACCOUNT_NUMBER")));
	                innerAccount.setAccountNip(parseBigDecimalToLong(account.get("NIP")));
	                innerAccount.setAccountCard(parseBigDecimalToLong(account.get("CARD_NUMBER")));
	                innerAccount.setBalance(parseBigDecimalToDouble(account.get("BALANCE")));
	                innerAccount.setStatus(parseBigDecimalToBoolean(account.get("STATUS")));
 
	                holder.setAccount(innerAccount);
 
	                accountsList.add(holder);
	            }
	        }
 
	    } catch (EmptyResultDataAccessException e) {
	        LOGGER.info("No data found {}", e.getMessage());
	    }
 
	    return accountsList;
	}

	@Override
	public HolderDTO executeUpdateAccountHolderInfoTransaction(HolderDTO holderIn) {
		
		LOGGER.info("Entramos al metodo de la 101");

		 Map<String, Object> p = new HashMap<>();
		    p.put("FIRST_NAME",   holderIn.getName());
		    p.put("LAST_NAME",    holderIn.getLastName());
		    p.put("RFC",          holderIn.getRfc());
		    p.put("CURP",         holderIn.getCurp());
		    p.put("AGE",          holderIn.getAge());
	        p.put("NIP",          holderIn.getAccount().getAccountNip());
	        p.put("CARD_NUMBER",  holderIn.getAccount().getAccountCard());
	        p.put("BALANCE",      holderIn.getAccount().getBalance());
		    p.put("CLIENT_TYPE",  holderIn.getHolderType());
		    p.put("ACCOUNT_NUMBER", holderIn.getAccount().getAccountNumber());
		    
		    LOGGER.info("ESTADO DEL MAPA QUE METEREMOS EN BBDD: {}", p);

		    Integer affected = jdbcUtils.update("update.account.by.accountNumber", p);
		    
		    LOGGER.info("Hemos conseguido hacer el cambio en bbdd");

		    if (affected == null || affected == 0) {
		    	this.addAdvice("MCWN01415041");
		        return null;
		    }
		    return executeRetrieveAccountHolderTransaction(holderIn.getAccount().getAccountNumber());
	}

	public Integer checkStatusWithAccountNumber(Long accountNumber) {
		
		Map<String, Object> args = new HashMap<>();
		args.put("accountNumber", accountNumber);
		
		LOGGER.info("EL valor de accountNumber del cual estamos buscando una cuenta es: {}", accountNumber);

		List<Map<String, Object>> rows = jdbcUtils.queryForList("check.status.by.accountNumber", args);

	    if (rows == null || rows.isEmpty()) {
	        LOGGER.debug("No se encontró ninguna cuenta para accountNumber={}", accountNumber);
	        return null;
	    }
	
	    Map<String, Object> firstRow = rows.get(0);
	    LOGGER.debug("Fila encontrada: {}", firstRow);
	
	    return convertBigDecimalToInteger(firstRow.get("STATUS"));
	}

	
	public Integer checkStatusWithAccountCard(Long accountCard) {
		
		Map<String, Object> args = new HashMap<>();
		args.put("accountCard", accountCard);
		

		List<Map<String, Object>> rows = jdbcUtils.queryForList("check.status.by.accountCard", args);

		if (rows == null || rows.isEmpty()) {
			LOGGER.debug("No se encontró ninguna cuenta para accountCard={}", accountCard);
			return null; // sin coincidencias → null
		}

		Map<String, Object> firstRow = rows.get(0);
		LOGGER.debug("Fila encontrada: {}", firstRow);

		// Asegúrate que la columna/alias es exactamente "STATUS" en el SQL
		return convertBigDecimalToInteger(firstRow.get("STATUS"));
	}
	
	private static Integer convertBigDecimalToInteger(Object object) {
	    if (object instanceof BigDecimal) {
	      return ((BigDecimal) object).intValue();
	    }
	    return null;
	  }
	
	@Override
	public Boolean executeGetAccountExists(Long accountNumber) {
	    LOGGER.info("MCWNR101Impl: executeGetAccountExists - start, accountNumber={}", accountNumber);
	    try {
	        Map<String, Object> args = new HashMap<>();
	        args.put("accountNumber", accountNumber);
 
	        List<Map<String,Object>> rows =
	                jdbcUtils.queryForList("select.account.exists.by.accountNumber", args);
 
	        return (rows != null && !rows.isEmpty());
 
	    } catch (Exception ex) {
	        LOGGER.error("MCWNR101Impl: Error consultando existencia de cuenta", ex);
	        this.addAdvice("MCWN01415041", "Fallo técnico verificando existencia", ex);
	        return false;
	    }
	}
	
	@Override
	public Boolean executeGetAccountIsActive(Long accountNumber) {
	    LOGGER.info("MCWNR101Impl: executeGetAccountIsActive - start, accountNumber={}", accountNumber);
	    try {
	        Map<String, Object> args = new HashMap<>();
	        args.put("accountNumber", accountNumber);
 
	        List<Map<String,Object>> rows =
	                jdbcUtils.queryForList("select.account.status.by.accountNumber", args);
 
	        if (rows == null || rows.isEmpty()) {
	            return false;
	        }
 
	        Integer status = parseBigDecimalToInt(rows.get(0).get("STATUS"));
	        return status != null && status != 0;
 
	    } catch (Exception ex) {
	        LOGGER.error("MCWNR101Impl: Error consultando estado de cuenta", ex);
	        this.addAdvice("MCWN01415041", "Fallo técnico verificando estado de cuenta", ex);
	        return false;
	    }
	}
	
	@Override
	public Boolean executeUpdateAccountStatus(Long accountNumber, int status) {
	    LOGGER.info("MCWNR101Impl: executeUpdateAccountStatus - start, accountNumber={}, status={}",
	            accountNumber, status);
	    try {
	        Map<String, Object> args = new HashMap<>();
	        args.put("accountNumber", accountNumber);
	        args.put("status", status);
 
	        return jdbcUtils.update("update.account.status.by.accountNumber", args) > 0;
 
	    } catch (Exception ex) {
	        LOGGER.error("MCWNR101Impl: Error actualizando estado de cuenta", ex);
	        this.addAdvice("MCWN01415041", "Fallo técnico actualizando estado de cuenta", ex);
	        return false;
	    }
	}
	
	private static Long parseBigDecimalToLong(Object object) {
		if(object instanceof BigDecimal) {
			return ((BigDecimal) object).longValue();
		}
		return null;
	}
	
	private static Integer parseBigDecimalToInteger(Object object) {
		if(object instanceof BigDecimal) {
			return ((BigDecimal) object).intValue();
		}
		return null;
	}
	
	private static Double parseBigDecimalToDouble(Object object) {
	    if (object == null) return null;

	    if (object instanceof BigDecimal) {
	        return ((BigDecimal) object).doubleValue();
	    }
	    if (object instanceof Number) {
	        return ((Number) object).doubleValue();
	    }
	    return Double.valueOf(object.toString());
	}

	
	private static Boolean parseBigDecimalToBoolean(Object object) {
	    if (object == null) return null;

	    if (object instanceof Boolean) {
	        return (Boolean) object;
	    }
	    if (object instanceof Number) {
	        return ((Number) object).intValue() == 1;
	    }

	    String value = object.toString().trim().toLowerCase();
	    return value.equals("1") || value.equals("true") || value.equals("y") || value.equals("yes");
	}


	private static Integer parseBigDecimalToInt(Object object) {
	    if (object == null) {
	        return null;
	    }

	    if (object instanceof BigDecimal) {
	        return ((BigDecimal) object).intValue();
	    }

	    if (object instanceof Number) {
	        return ((Number) object).intValue();
	    }

	    try {
	        return Integer.valueOf(object.toString());
	    } catch (NumberFormatException e) {
	        return null;
	    }
	}
}
