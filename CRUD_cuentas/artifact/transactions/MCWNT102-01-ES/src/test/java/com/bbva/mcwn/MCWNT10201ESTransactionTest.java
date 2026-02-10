package com.bbva.mcwn;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.RequestHeaderParamsName;
import com.bbva.elara.domain.transaction.request.header.CommonRequestHeader;
import com.bbva.mcwn.dto.holder.AccountDTO;
import com.bbva.mcwn.dto.holder.HolderDTO;
import com.bbva.mcwn.dto.holder.HolderOutDTO;
import com.bbva.mcwn.lib.r100.MCWNR100;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MCWNT10201ESTransactionTest {

	private Map<String, Object> parameters;

	private Map<Class<?>, Object> serviceLibraries;
	
	@Mock
	private MCWNR100 mcwnR100;

	@Mock
	private ApplicationConfigurationService applicationConfigurationService;

	@Mock
	private CommonRequestHeader commonRequestHeader;
	
	@InjectMocks
	private final MCWNT10201ESTransaction transaction = new MCWNT10201ESTransaction() {
		
		@Override
		protected void addParameter(String field, Object obj) {
			if (parameters != null) {
				parameters.put(field, obj);
			}
		}

		@Override
		protected Object getParameter(String field) {
			return parameters.get(field);
		}

		@Override
		protected <T> T getServiceLibrary(Class<T> serviceInterface) {
			return (T) serviceLibraries.get(serviceInterface);
		}

		@Override
		public String getProperty(String keyProperty) {
			return applicationConfigurationService.getProperty(keyProperty);
		}

		@Override
		protected CommonRequestHeader getRequestHeader() {
			return commonRequestHeader;
		}
	};

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		initializeTransaction();
		setServiceLibrary(MCWNR100.class, mcwnR100);
	}

	private void initializeTransaction() {
		transaction.setContext(new Context());
		parameters = new HashMap<>();
		serviceLibraries = new HashMap<>();
	}

	private void setServiceLibrary(Class<?> clasz, Object mockObject) {
		serviceLibraries.put(clasz, mockObject);
	}

	private void setParameterToTransaction(String parameter, Object value) {
		parameters.put(parameter, value);
	}

	private Object getParameterFromTransaction(String parameter) {
		return parameters.get(parameter);
	}

	@SuppressWarnings("unchecked")
	private List<HolderOutDTO> getHoldersFromTransaction() {
	    return (List<HolderOutDTO>) parameters.get("holders");
	}

	
	@Test
    public void testExecute_emptyList() {
        when(mcwnR100.executeGetAllAccounts()).thenReturn(Collections.emptyList());

        transaction.execute();

        List<HolderOutDTO> output = getHoldersFromTransaction();

        Assert.assertNotNull(output);
        Assert.assertTrue(output.isEmpty());
    }
	
	 @Test
	    public void testExecute_singleHolder() {

	        AccountDTO acc = new AccountDTO();
	        acc.setAccountNumber(1234567890L);
	        acc.setAccountNip(1234L);
	        acc.setBalance(1000.0);
	        acc.setAccountCard(5678L);
	        acc.setStatus(true);

	        HolderDTO holder = new HolderDTO();
	        holder.setName("Ana");
	        holder.setLastName("Lopez");
	        holder.setAge(25L);
	        holder.setRfc("RFC123");
	        holder.setCurp("CURP123");
	        holder.setAccount(acc);

	        when(mcwnR100.executeGetAllAccounts()).thenReturn(Arrays.asList(holder));

	        transaction.execute();

	        List<HolderOutDTO> output = getHoldersFromTransaction();

	        Assert.assertNotNull(output);
	        Assert.assertEquals(1, output.size());

	        HolderOutDTO out = output.get(0);
	        Assert.assertEquals("Ana", out.getName());
	        Assert.assertEquals("Lopez", out.getLastName());
	        Assert.assertEquals(Long.valueOf(25), out.getAge());
	        Assert.assertEquals("RFC123", out.getRfc());
	        Assert.assertEquals("CURP123", out.getCurp());
	        Assert.assertEquals(Long.valueOf(1234567890L), out.getAccountOut().getAccountNumber());

	    }

	    @Test
	    public void testExecute_multipleHolders() {
	        AccountDTO acc1 = new AccountDTO();
	        acc1.setAccountNumber(1111111111L);
	        acc1.setAccountNip(1111L);
	        acc1.setBalance(500.0);
	        acc1.setAccountCard(1111L);
	        acc1.setStatus(true);

	        HolderDTO holder1 = new HolderDTO();
	        holder1.setName("Juan");
	        holder1.setLastName("Perez");
	        holder1.setAge(30L);
	        holder1.setRfc("RFC111");
	        holder1.setCurp("CURP111");
	        holder1.setAccount(acc1);

	        AccountDTO acc2 = new AccountDTO();
	        acc2.setAccountNumber(2222222222L);
	        acc2.setAccountNip(2222L);
	        acc2.setBalance(1500.0);
	        acc2.setAccountCard(2222L);
	        acc2.setStatus(true);

	        HolderDTO holder2 = new HolderDTO();
	        holder2.setName("Maria");
	        holder2.setLastName("Gomez");
	        holder2.setAge(28L);
	        holder2.setRfc("RFC222");
	        holder2.setCurp("CURP222");
	        holder2.setAccount(acc2);

	        when(mcwnR100.executeGetAllAccounts()).thenReturn(Arrays.asList(holder1, holder2));

	        transaction.execute();

	        List<HolderOutDTO> output = getHoldersFromTransaction();

	        Assert.assertNotNull(output);
	        Assert.assertEquals(2, output.size());

	        Assert.assertEquals("Juan", output.get(0).getName());
	        Assert.assertEquals("Maria", output.get(1).getName());
	    }
}
