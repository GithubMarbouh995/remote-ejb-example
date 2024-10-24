package com.illucit.ejbremote.server;

import java.util.Map;

import javax.ejb.Remote;

/**
 * Interface distante pour {@link ExampleService}.
 */
@Remote
public interface ExampleService {

	/**
	 * Saluer un nom.
	 *
	 * @param name
	 *            nom à saluer
	 * @return "Bonjour $name!"
	 */
	public String greet(String name);

	/**
	 * Obtenir les propriétés système du serveur.
	 *
	 * @return propriétés système sous forme de map
	 */
	public Map<Object, Object> getSystemProperties();

}