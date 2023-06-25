package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
     @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try (var session = Util.getSession()) {
            transaction = session.beginTransaction();
            String sql = """
                    CREATE TABLE IF NOT EXISTS users
                    (
                        id       BIGINT NOT NULL AUTO_INCREMENT,
                        name     VARCHAR(255),
                        lastName VARCHAR(255),
                        age      TINYINT,
                        PRIMARY KEY (id)
                    );
                    """;
            Query query = session.createSQLQuery(sql);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;
        try (var session = Util.getSession()) {
            transaction = session.beginTransaction();
            String sql = """
                    DROP TABLE IF EXISTS users;
                    """;
            Query query = session.createSQLQuery(sql);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (var session = Util.getSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
            System.out.printf("User с именем – %s добавлен в базу данных\n", name);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (var session = Util.getSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            session.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;
        Transaction transaction = null;
        try (var session = Util.getSession()) {
            transaction = session.beginTransaction();
            String hql = """
                    FROM User
                    """;
            Query query = session.createQuery(hql);
            users = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (var session = Util.getSession()) {
            transaction = session.beginTransaction();
            String sql = """
                    DELETE FROM users;
                    """;
            Query query = session.createSQLQuery(sql);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public void test() {
         int isolationLevel = 4;
        try (var session1 = Util.getSession();
        var session2 = Util.getSession()) {
//            session1.doWork(connection -> connection.setTransactionIsolation(isolationLevel));
//            session2.doWork(connection -> connection.setTransactionIsolation(isolationLevel));

            session1.beginTransaction();
            session2.beginTransaction();

            var user1 = session1.find(User.class, 5L);

            var user2 = session2.find(User.class, 5L);
            user2.setTest(10);

            session2.getTransaction().commit();

            user1.setTest(100);
            System.out.println(user1);

            session1.getTransaction().commit();



        }
    }

    @Override
    public void saveUser(User user) {
        Transaction transaction = null;
        try (var session = Util.getSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
