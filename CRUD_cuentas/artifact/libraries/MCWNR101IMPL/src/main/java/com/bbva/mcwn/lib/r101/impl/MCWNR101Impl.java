package com.bbva.mcwn.lib.r101.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;

import com.bbva.mcwn.dto.holder.AccountDTO;
import com.bbva.mcwn.dto.holder.HolderDTO;

/**
 * The MCWNR101Impl class...
 */
public class MCWNR101Impl extends MCWNR101Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(MCWNR101Impl.class);

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean executeManageAccountStatusTransaction(AccountDTO accountIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean executeGetActiveAccountWitchSameNumber(Long accountNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean executeGetActiveAccountWitchSameCard(Long accountCard) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean executeGetInactiveAccountWitchSameNumber(Long accountNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean executeGetInactiveAccountWitchSameCard(Long accountCard) {
		// TODO Auto-generated method stub
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




	/**
	 * The execute method...
	 */
	
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
