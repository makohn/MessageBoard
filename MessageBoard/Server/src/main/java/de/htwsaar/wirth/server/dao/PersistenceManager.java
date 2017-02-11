package de.htwsaar.wirth.server.dao;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Created by Marius on 08.02.17.
 */
public class PersistenceManager {
    /** Die SessionFactory der Anwendung. */
    private static SessionFactory sessionFactory;

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
     * Erstellt eine neue SessionFactory.
     */
    private static void createSessionFactory() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
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
