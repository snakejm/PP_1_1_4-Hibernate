package jm.task.core.jdbc.util;


import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class Util {
    private static volatile SessionFactory sessionFactory;

    private static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (Util.class) {
                if (sessionFactory == null) {
                    try {
                        Configuration configuration = new Configuration();
                        Properties settings = new Properties();
                        settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                        settings.put(Environment.URL, "jdbc:mysql://localhost:3306/testdb?allowPublicKeyRetrieval=true&useSSL=false");
                        settings.put(Environment.USER, "root");
                        settings.put(Environment.PASS, "root");
                        settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
                        settings.put(Environment.SHOW_SQL, "true");
                        settings.put(Environment.FORMAT_SQL, "true");
                        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                        settings.put(Environment.HBM2DDL_AUTO, "create-drop");
                        settings.put(Environment.ISOLATION, "4");
                        configuration.setProperties(settings);
                        configuration.addAnnotatedClass(User.class);
                        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                                .applySettings(configuration.getProperties()).build();
                        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sessionFactory;
    }

    public static Session getSession() {
        return getSessionFactory().openSession();
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
