package com.bbva.mcwn;

import com.bbva.elara.transaction.AbstractTransaction;
import com.bbva.mcwn.dto.holder.AccountInDTO;

/**
 * In this class, the input and output data is defined automatically through the setters and getters.
 */
public abstract class AbstractMCWNT10401ESTransaction extends AbstractTransaction {

	protected AbstractMCWNT10401ESTransaction(){
	}


	/**
	 * Return value for input parameter accountIn
	 */
	protected AccountInDTO getAccountin(){
		return (AccountInDTO)this.getParameter("accountIn");
	}

	/**
	 * Set value for Boolean output parameter deleted
	 */
	protected void setDeleted(final Boolean field){
		this.addParameter("deleted", field);
	}
}
