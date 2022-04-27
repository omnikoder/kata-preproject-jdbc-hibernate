package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {}

    @Override
    public void createUsersTable() {
        Session session = null;

        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            session
                    .createNativeQuery("create table if not exists users (" +
                            "id bigint primary key auto_increment," +
                            "name varchar(20) not null check (name like '__%')," +
                            "lastname varchar(20) not null check (lastname != '')," +
                            "age tinyint unsigned not null)",
                            User.class)
                    .executeUpdate();
            session.getTransaction().commit();

        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new RuntimeException("Не удалось создать таблицу пользователей.\n" + e.getMessage());

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = null;

        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            session
                    .createNativeQuery("drop table if exists users", User.class)
                    .executeUpdate();
            session.getTransaction().commit();

        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new RuntimeException("Не удалось удалить таблицу пользователей.\n" + e.getMessage());

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = null;

        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            session.persist(new User(name, lastName, age));
            session.getTransaction().commit();

        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new RuntimeException(String.format("Не удалось сохранить пользователя - [%s, %s, %d]\n%s",
                    name, lastName, age, e.getMessage()));

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = null;

        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            session.getTransaction().commit();

        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new RuntimeException("Не удалось удалить пользователя с id: " + id + '\n' + e.getMessage());

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = null;
        List<User> users = null;

        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            users = session
                    .createQuery("from User", User.class)
                    .list();
            session.getTransaction().commit();

        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new RuntimeException("Не удалось получить ползователей.\n" + e.getMessage());

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = null;

        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            session
                    .createNativeQuery("truncate table users", User.class)
                    .executeUpdate();
            session.getTransaction().commit();

        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new RuntimeException("Не удалось очистить таблицу пользователей.\n" + e.getMessage());

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
