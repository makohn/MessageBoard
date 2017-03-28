package de.htwsaar.wirth.server.dao;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by Marius on 08.02.17.
 * Edited by oliverseibert 23.03.17
 */
public class PersistenceManager {
	
	private static final Logger logger = LogManager.getLogger(PersistenceManager.class);
	
    /** The appliation's {@code SessionFactory} */
    private static SessionFactory sessionFactory;
    
    private static String databaseNameSuffix;

    /**
     * Provides the single unique instance of this {@code SessionFactory}
     * @return SessionFactory
     */
    //TODO: synchronized ?
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            createSessionFactory();
        }

        return sessionFactory;
    }

    /**
     * Specifies the group suffix which is concatenated to the
     * database name: messageboard.[suffix].db
     * @param name - the specified suffix
     */
    public static void setDatabaseNameSuffix(String name){
        databaseNameSuffix = name;
    }

    /**
     * Creates a new Session Factory
     */
    private static void createSessionFactory() {
        Configuration cfg = new Configuration().configure();
        cfg.setProperty("hibernate.connection.url", "jdbc:sqlite:messageboard."+databaseNameSuffix+".db");
        try {
            sessionFactory = cfg.buildSessionFactory();
            logger.info("Created SessionFactory for database messageboard."+databaseNameSuffix+".db" );
        } catch (Exception e) {
            logger.error("Creating SessionFactory failed.");
        }
    }

    /**
     * Closes an active connection to a {@code SessionFactory} instance
     */
    public static void closeConnection() {
        sessionFactory.close();
        logger.info("Connection to SessionFactory closed.");
    }

    /**
     * Singleton Access Method for this class.
     * @return - the single and unique instance of {@code SessionFactory}
     */
    public static Session getSession() {
        return getSessionFactory().openSession();
    }
}
