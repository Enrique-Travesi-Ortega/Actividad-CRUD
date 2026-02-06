package com.bbva.mcwn;

import com.bbva.mcwn.lib.r100.MCWNR100;
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
		MCWNR100 mcwnR100 = this.getServiceLibrary(MCWNR100.class);
		// TODO - Implementation of business logic
	}

}
