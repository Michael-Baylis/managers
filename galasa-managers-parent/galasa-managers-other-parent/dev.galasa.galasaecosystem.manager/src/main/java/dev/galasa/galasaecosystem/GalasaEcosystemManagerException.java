/*
 * Licensed Materials - Property of IBM
 * 
 * (c) Copyright IBM Corp. 2020.
 */
package dev.galasa.galasaecosystem;

import dev.galasa.ManagerException;

public class GalasaEcosystemManagerException extends ManagerException {
	private static final long serialVersionUID = 1L;

	public GalasaEcosystemManagerException() {
	}

	public GalasaEcosystemManagerException(String message) {
		super(message);
	}

	public GalasaEcosystemManagerException(Throwable cause) {
		super(cause);
	}

	public GalasaEcosystemManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public GalasaEcosystemManagerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}