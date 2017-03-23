package de.htwsaar.wirth.server.dao;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

/**
 * Created by Marius on 08.02.17.
 * Edited by oliverseibert 23.03.17
 */
public class PersistenceManager {
    /** Die SessionFactory der Anwendung. */
    private static SessionFactory sessionFactory;
    private static String databaseNameSuffix;

    /**
     * Liefert die einzige Instanz der SessionFactory in der Anwendung.
     *
     * @return SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            createSessionFactory();
        }

        return sessionFactory;
    }

    /**
     * Ändert den Datenbankname auf messagboard.[name]
     * @param name
     */
    public static void setDatabaseNameSuffix(String name){
        databaseNameSuffix = name;
    }

    /**
     * Erstellt eine neue SessionFactory.
     */
    private static void createSessionFactory() {
        Configuration cfg = new Configuration().configure();
        cfg.setProperty("hibernate.connection.url", "jdbc:sqlite:messageboard."+databaseNameSuffix+".db");

        try {
            sessionFactory = cfg.buildSessionFactory();
        } catch (Exception e) {
            System.out.println("Creating SessionFactory failed !!!");
        }
    }

    public static void closeConnection() {
        sessionFactory.close();
    }

    public static Session getSession() {
        return getSessionFactory().openSession();
    }
}
