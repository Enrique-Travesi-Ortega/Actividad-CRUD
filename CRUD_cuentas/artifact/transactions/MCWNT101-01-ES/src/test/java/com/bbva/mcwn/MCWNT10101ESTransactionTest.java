package com.bbva.mcwn;

import static org.mockito.Mockito.when;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Advice;
import com.bbva.elara.domain.transaction.AdviceType;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.request.header.CommonRequestHeader;
import com.bbva.mcwn.dto.holder.AccountDTO;
import com.bbva.mcwn.dto.holder.AccountInDTO;
import com.bbva.mcwn.dto.holder.HolderDTO;
import com.bbva.mcwn.lib.r100.MCWNR100;

public class MCWNT10101ESTransactionTest {

  private Map<String, Object> parameters;

  private Map<Class<?>, Object> serviceLibraries;

  @Mock
  MCWNR100 mcwnR100;

  @Mock
  private ApplicationConfigurationService applicationConfigurationService;

  @Mock
  private CommonRequestHeader commonRequestHeader;

  private final MCWNT10101ESTransaction transaction = new MCWNT10101ESTransaction() {
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


  };

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    initializeTransaction();
    // TODO - Set the mocks created from the libraries to the transaction, e.g.:
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

  @Test
  public void executeTestSuccess() {
    // when(commonRequestHeader.getHeaderParameter(RequestHeaderParamsName.COUNTRYCODE)).thenReturn("ES");
    // when(applicationConfigurationService.getProperty("config.property")).thenReturn("value");
    AccountInDTO accountIn = new AccountInDTO();
    AccountDTO account = new AccountDTO();
    HolderDTO holder = new HolderDTO();
    holder.setName("Prueba");
    holder.setAccount(account);

    setParameterToTransaction("accountIn", accountIn);
    when(mcwnR100.executeRetrieveAccountHolderTransaction(Mockito.any(AccountDTO.class))).thenReturn(holder);
    transaction.execute();
    Assert.assertEquals(0, transaction.getAdviceList().size());
  }

  @Test
  public void given_AccountNull_whenAfterExecuteGetAccount_thenAvoidAdvice() {
    // when(commonRequestHeader.getHeaderParameter(RequestHeaderParamsName.COUNTRYCODE)).thenReturn("ES");
    // when(applicationConfigurationService.getProperty("config.property")).thenReturn("value");
    AccountInDTO accountIn = new AccountInDTO();
    AccountDTO account = new AccountDTO();
    HolderDTO holder = new HolderDTO();


    setParameterToTransaction("accountIn", accountIn);
    when(mcwnR100.executeRetrieveAccountHolderTransaction(Mockito.any(AccountDTO.class))).thenReturn(holder);
    
    Assert.assertEquals(0, transaction.getAdviceList().size());
  }

  @Test
  public void given_AccountNotFound_whenExecuteGetAccount_thenThrowAdvice() {
    // when(commonRequestHeader.getHeaderParameter(RequestHeaderParamsName.COUNTRYCODE)).thenReturn("ES");
    // when(applicationConfigurationService.getProperty("config.property")).thenReturn("value");
    AccountInDTO accountIn = new AccountInDTO();
    AccountDTO account = new AccountDTO();
    HolderDTO holder = new HolderDTO();
    holder.setName("Prueba");
    holder.setAccount(account);

    setParameterToTransaction("accountIn", accountIn);
    Advice advice = new Advice("MCWN01415036", "Account not found", AdviceType.E, null);
    when(mcwnR100.executeRetrieveAccountHolderTransaction(Mockito.any(AccountDTO.class))).thenAnswer(new Answer<HolderDTO>() {

      @Override
      public HolderDTO answer(InvocationOnMock invocation) throws Throwable {
        transaction.getContext().getAdviceList().add(advice);
        return new HolderDTO();
      }



    });
    transaction.execute();
    Assert.assertEquals(1, transaction.getAdviceList().size());
  }
}