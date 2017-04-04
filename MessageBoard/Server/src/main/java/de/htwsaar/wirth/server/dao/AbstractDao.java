package de.htwsaar.wirth.server.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 * Abstract Database Access Object. Implements a basic set of
 * database access methods, such as saving, deleting and querying.
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

    @SuppressWarnings("all")
    public List<T> query(String query) {
        Session session = PersistenceManager.getSession();
        session.beginTransaction();

        List<T> result = session.createQuery(query).list();

        session.getTransaction().commit();
        session.close();

        return result;
    }

    @SuppressWarnings("all")
    public List<T> query(String query, List<DatabaseQueryParameter> parameterList) {
        Session session = PersistenceManager.getSession();
        session.beginTransaction();

        Query queryObj = session.createQuery(query);
        for(int i=0; i<parameterList.size();i++){
            queryObj.setParameter(parameterList.get(i).getKey(), parameterList.get(i).getValue());
        }
		List<T> result = queryObj.list();
        session.getTransaction().commit();
        session.close();

        return result;
    }

    @SuppressWarnings("all")
    public List<T> query(String query, List<DatabaseQueryParameter> parameterList, int limit) {
        Session session = PersistenceManager.getSession();
        session.beginTransaction();

        Query queryObj = session.createQuery(query).setMaxResults(limit);
        for(int i=0; i<parameterList.size();i++){
            queryObj.setParameter(parameterList.get(i).getKey(), parameterList.get(i).getValue());
        }
        List<T> result = queryObj.list();
        session.getTransaction().commit();
        session.close();

        return result;
    }
}
