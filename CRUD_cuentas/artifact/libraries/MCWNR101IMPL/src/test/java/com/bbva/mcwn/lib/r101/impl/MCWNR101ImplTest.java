package com.bbva.mcwn.lib.r101.impl;

import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;
import com.bbva.elara.domain.transaction.request.header.CommonRequestHeader;
import com.bbva.elara.utility.jdbc.JdbcUtils;
import com.bbva.mcwn.dto.holder.AccountDTO;
import com.bbva.mcwn.dto.holder.HolderDTO;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import static org.mockito.Mockito.*;

public class MCWNR101ImplTest {

	@Mock
	private ApplicationConfigurationService applicationConfigurationService;

	/* There are methods of the APX Architecture that require greater complexity to mock, for this reason
	 * an instance of the class to be tested can be created with the overwritten methods and on these
	 * methods the mocking of the classes is carried out, for example Header data
	 * (The Mocking the header is only for libraries that are used online, in batch it would not work)
	 *
	 * Import section:
	 * - import com.bbva.elara.domain.transaction.RequestHeaderParamsName;
	 * - import com.bbva.elara.domain.transaction.request.header.CommonRequestHeader;
	 *
	 * Instance section:
	 * 	@Mock
	 *	private CommonRequestHeader commonRequestHeader;
	 *
	 *	@InjectMocks
	 *	private MCWNR101Impl mcwnR101 = new MCWNR101Impl() {
	 *		@Override
	 *		protected CommonRequestHeader getRequestHeader() {
	 *			return commonRequestHeader;
	 *		}
	 *	};
	 */
	
    @Mock
    private JdbcUtils jdbcUtils;
	
    @Mock
    private CommonRequestHeader commonRequestHeader;
    
    @InjectMocks
    private MCWNR101Impl mcwnR101 = new MCWNR101Impl() {

		
    @Override
    protected CommonRequestHeader getRequestHeader() {
        return commonRequestHeader;
        }
    };

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		ThreadContext.set(new Context());
	}

	 @Test
	    public void executeGetAllAccounts_whenDataExists() {

	        Map<String, Object> row = new HashMap<>();
	        row.put("FIRST_NAME", "Lulu");
	        row.put("LAST_NAME", "Perez");
	        row.put("AGE", new BigDecimal(30));
	        row.put("CURP", "CURP123");
	        row.put("RFC", "RFC123");
	        row.put("ACCOUNT_NUMBER", new BigDecimal(123456));
	        row.put("NIP", new BigDecimal(1234));
	        row.put("CARD_NUMBER", new BigDecimal(999999));
	        row.put("BALANCE", new BigDecimal("1500.50"));
	        row.put("STATUS", new BigDecimal(1));

	        when(jdbcUtils.queryForList(eq("select.accounts.by.status"), anyMap()))
	                .thenReturn(Collections.singletonList(row));

	        List<HolderDTO> result = mcwnR101.executeGetAllAccounts();

	        assertNotNull(result);
	        assertEquals(1, result.size());

	        HolderDTO holder = result.get(0);
	        assertEquals("Lulu", holder.getName());
	        assertEquals("Perez", holder.getLastName());
	        assertEquals(Long.valueOf(30), holder.getAge());

	        AccountDTO account = holder.getAccount();
	        assertEquals(Long.valueOf(123456), account.getAccountNumber());

	    }

	    @Test
	    public void executeGetAllAccounts_EmptyList() {

	        when(jdbcUtils.queryForList(eq("select.accounts.by.status"), anyMap()))
	                .thenReturn(Collections.emptyList());

	        List<HolderDTO> result = mcwnR101.executeGetAllAccounts();

	        assertNotNull(result);
	        assertTrue(result.isEmpty());
	    }
	    
	    @Test
	    public void executeGetAllAccounts_whenResultIsNull() {

	        when(jdbcUtils.queryForList(eq("select.accounts.by.status"), anyMap()))
	                .thenReturn(null);

	        List<HolderDTO> result = mcwnR101.executeGetAllAccounts();

	        assertNotNull(result);
	        assertTrue(result.isEmpty());
	    }

	    @Test
	    public void executeGetAllAccounts_whenNumericFieldsAreNull() {

	        Map<String, Object> row = new HashMap<>();
	        row.put("FIRST_NAME", "Lulu");
	        row.put("LAST_NAME", "Lopez");
	        row.put("AGE", null);
	        row.put("ACCOUNT_NUMBER", null);
	        row.put("NIP", null);
	        row.put("CARD_NUMBER", null);
	        row.put("BALANCE", null);
	        row.put("STATUS", null);

	        when(jdbcUtils.queryForList(eq("select.accounts.by.status"), anyMap()))
	                .thenReturn(Collections.singletonList(row));

	        List<HolderDTO> result = mcwnR101.executeGetAllAccounts();

	        HolderDTO holder = result.get(0);
	        AccountDTO account = holder.getAccount();

	        assertNull(holder.getAge());
	        assertNull(account.getAccountNumber());
	        assertNull(account.getAccountNip());
	        assertNull(account.getAccountCard());
	        assertNull(account.getBalance());
	        assertNull(account.getStatus());
	    }

	    @Test
	    public void executeGetAllAccounts_whenEmptyResultDataAccessExceptionOccurs() {

	        when(jdbcUtils.queryForList(eq("select.accounts.by.status"), anyMap()))
	                .thenThrow(new EmptyResultDataAccessException(1));

	        List<HolderDTO> result = mcwnR101.executeGetAllAccounts();

	        assertNotNull(result);
	        assertTrue(result.isEmpty());
	    }

	    @Test
	    public void executeGetAllAccounts_whenBalanceIsString() {

	        Map<String, Object> row = new HashMap<>();
	        row.put("FIRST_NAME", "Lulu");
	        row.put("LAST_NAME", "Ramirez");
	        row.put("BALANCE", "2500.75");
	        row.put("STATUS", 1);

	        when(jdbcUtils.queryForList(eq("select.accounts.by.status"), anyMap()))
	                .thenReturn(Collections.singletonList(row));

	        List<HolderDTO> result = mcwnR101.executeGetAllAccounts();

	        assertNotNull(result);
	        assertEquals(1, result.size());

	        AccountDTO account = result.get(0).getAccount();
	        assertEquals(Double.valueOf(2500.75), account.getBalance());
	    }

	    


			@Test
			public void setsHolderTypeFisico_whenClientTypeIs0() {
			    Map<String, Object> row = new HashMap<>();
			  
			    row.put("CLIENT_TYPE", Integer.valueOf(0));
			    row.put("STATUS", new BigDecimal(1));
			
			    when(jdbcUtils.queryForList(eq("select.accounts.by.status"), anyMap()))
			        .thenReturn(Collections.singletonList(row));
			
			    List<HolderDTO> result = mcwnR101.executeGetAllAccounts();
			
			    assertNotNull(result);
			    assertEquals(1, result.size());
			
			    HolderDTO holder = result.get(0);
			    assertEquals("FISICO", holder.getHolderType()); 
			}



			@Test
			public void setsHolderTypeMoral_whenClientTypeIs1() {
			    Map<String, Object> row = new HashMap<>();
			    row.put("CLIENT_TYPE", Integer.valueOf(1)); 
			    row.put("STATUS", new BigDecimal(1));
			
			    when(jdbcUtils.queryForList(eq("select.accounts.by.status"), anyMap()))
			        .thenReturn(Collections.singletonList(row));
			
			    List<HolderDTO> result = mcwnR101.executeGetAllAccounts();
			
			    assertNotNull(result);
			    assertEquals(1, result.size());
			
			    HolderDTO holder = result.get(0);
			    assertEquals("MORAL", holder.getHolderType());
			}


	   
	  
	    
}