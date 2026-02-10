package com.bbva.mcwn;

import com.bbva.elara.transaction.AbstractTransaction;
import com.bbva.mcwn.dto.holder.AccountInDTO;
import com.bbva.mcwn.dto.holder.HolderOutDTO;

/**
 * In this class, the input and output data is defined automatically through the setters and getters.
 */
public abstract class AbstractMCWNT10101ESTransaction extends AbstractTransaction {

	protected AbstractMCWNT10101ESTransaction(){
	}


	/**
	 * Return value for input parameter accountIn
	 */
	protected AccountInDTO getAccountin(){
		return (AccountInDTO)this.getParameter("accountIn");
	}

	/**
	 * Set value for HolderOutDTO output parameter holderOut
	 */
	protected void setHolderout(final HolderOutDTO field){
		this.addParameter("holderOut", field);
	}
}
