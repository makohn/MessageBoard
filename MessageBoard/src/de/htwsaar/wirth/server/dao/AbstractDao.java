package de.htwsaar.wirth.server.dao;

import org.hibernate.Session;

import java.util.List;

/**
 * Created by Marius on 08.02.17
 */
public abstract class AbstractDao<T> {

    public boolean save(T obj) {
        try {
            Session session = PersistenceManager.getSession();
            session.beginTransaction();
            session.saveOrUpdate(obj);
            session.getTransaction().commit();
            session.close();

            return true;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean delete(T obj){
        boolean success = true;
        try {
            Session session = PersistenceManager.getSession();
            session.beginTransaction();

            session.delete(obj);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            success = false;
        }
        return success;
    }

    public List<T> query(String query) {
        Session session = PersistenceManager.getSession();
        session.beginTransaction();

        List<T> result = session.createQuery(query).list();

        session.getTransaction().commit();
        session.close();

        return result;
    }
}
