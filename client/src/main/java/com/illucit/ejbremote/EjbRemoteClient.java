package com.illucit.ejbremote;

import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static javax.naming.Context.PROVIDER_URL;
import static javax.naming.Context.URL_PKG_PREFIXES;

import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.illucit.ejbremote.server.ExampleService;

/**
 * Client EJB distant.
 */
public class EjbRemoteClient {

	/**
	 * Exécuter l'exemple.
	 *
	 * @param args
	 *            (non utilisé)
	 */
	public static void main(String[] args) {

		// Connexion à l'instance du serveur Wildfly
		String host = "127.0.0.1";
		String port = "8080"; // Port HTTP de Wildfly

		Context remotingContext;
		try {
			remotingContext = createRemoteEjbContext(host, port);
		} catch (NamingException e) {
			System.err.println("Erreur lors de la configuration du contexte distant");
			e.printStackTrace();
			return;
		}

		// Syntaxe : ejb:${appName}/${moduleName}/${beanName}!${remoteView}
		// appName = nom du déploiement EAR (ou vide pour les déploiements EJB/WAR uniques)
		// moduleName = nom du déploiement EJB/WAR
		// beanName = nom de l'EJB (nom simple de la classe EJB)
		// remoteView = classe de l'interface distante entièrement qualifiée
		String ejbUrl = "ejb:/ejb-remote-server/ExampleServiceImpl!com.illucit.ejbremote.server.ExampleService";

		ExampleService service;
		try {
			service = createEjbProxy(remotingContext, ejbUrl, ExampleService.class);
		} catch (NamingException e) {
			System.err.println("Erreur lors de la résolution du bean");
			e.printStackTrace();
			return;
		} catch (ClassCastException e) {
			System.err.println("Le bean EJB résolu est du mauvais type");
			e.printStackTrace();
			return;
		}

		// Appeler la méthode distante avec un paramètre

		String toGreet = "World";

		String exampleResult;
		try {
			exampleResult = service.greet(toGreet);
		} catch (Exception e) {
			System.err.println("Erreur lors de l'accès au bean distant");
			e.printStackTrace();
			return;
		}

		// Bonjour le monde !
		System.out.println("Résultat de l'exemple : " + exampleResult);

		// Récupérer le résultat de l'appel EJB

		Map<Object, Object> systemProperties;
		try {
			systemProperties = service.getSystemProperties();
		} catch (Exception e) {
			System.err.println("Erreur lors de l'accès au bean distant");
			e.printStackTrace();
			return;
		}

		System.out.println("Répertoire d'accueil de Wildfly : " + systemProperties.get("jboss.home.dir"));

	}

	/**
	 * Créer un contexte EJB distant.
	 *
	 * @param host
	 *            hôte à connecter (par exemple "127.0.0.1")
	 * @param port
	 *            port à connecter (port HTTP de Wildfly, par exemple 8080)
	 * @return contexte EJB distant
	 * @throws NamingException
	 *             si la création du contexte échoue
	 */
	private static Context createRemoteEjbContext(String host, String port) throws NamingException {

		Hashtable<Object, Object> props = new Hashtable<>();

		props.put(INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		props.put(URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

		props.put("jboss.naming.client.ejb.context", false);
		props.put("org.jboss.ejb.client.scoped.context", true);

		props.put("endpoint.name", "client-endpoint");
		props.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", false);
		props.put("remote.connections", "default");
		props.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", false);

		props.put(PROVIDER_URL, "http-remoting://" + host + ":" + port);
		props.put("remote.connection.default.host", host);
		props.put("remote.connection.default.port", port);

		return new InitialContext(props);
	}

	/**
	 * Obtenir un proxy pour un EJB distant.
	 *
	 * @param remotingContext
	 *            contexte EJB distant
	 * @param ejbUrl
	 *            URL de l'EJB
	 * @param ejbInterfaceClass
	 *            classe de l'interface distante de l'EJB
	 * @param <T>
	 *            type de l'interface distante EJB
	 * @return proxy EJB
	 * @throws NamingException
	 *             si la résolution du nom échoue
	 * @throws ClassCastException
	 *             si le proxy EJB n'est pas du type donné
	 */
	@SuppressWarnings("unchecked")
	private static <T> T createEjbProxy(Context remotingContext, String ejbUrl, Class<T> ejbInterfaceClass)
			throws NamingException, ClassCastException {
		Object resolvedproxy = remotingContext.lookup(ejbUrl);
		return (T) resolvedproxy;
	}

}