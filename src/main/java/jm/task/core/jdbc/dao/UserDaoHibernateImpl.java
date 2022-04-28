package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory();
    private Session session = null;

    public UserDaoHibernateImpl() {}

    @Override
    public void createUsersTable() {
        beginTransaction("Не удалось создать таблицу пользователей.", () ->
                Optional.of(this.session
                        .createNativeQuery("create table if not exists users (" +
                                        "id bigint primary key auto_increment," +
                                        "name varchar(20) not null check (name like '__%')," +
                                        "lastname varchar(20) not null check (lastname != '')," +
                                        "age tinyint unsigned not null)", User.class)
                        .executeUpdate())
        );
    }

    @Override
    public void dropUsersTable() {
        beginTransaction("Не удалось удалить таблицу пользователей.", () ->
                Optional.of(this.session
                        .createNativeQuery("drop table if exists users", User.class)
                        .executeUpdate())
        );
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        beginTransaction(
                String.format("Не удалось сохранить пользователя - [%s, %s, %d].", name, lastName, age),
                () -> {
                    this.session.persist(new User(name, lastName, age));
                    return Optional.empty();
                }
        );
    }

    @Override
    public void removeUserById(long id) {
        beginTransaction("Не удалось удалить пользователя с id: " + id, () -> {
            Optional.ofNullable(this.session.get(User.class, id))
                    .ifPresent((user) -> this.session.remove(user));
            return Optional.empty();
        });
    }

    @Override
    public List<User> getAllUsers() {
        return beginTransaction("Не удалось получить ползователей.", () ->
                Optional.of(this.session.createQuery("from User", User.class).list()))
                .orElse(List.of());
    }

    @Override
    public void cleanUsersTable() {
        beginTransaction("Не удалось очистить таблицу пользователей.", () ->
                Optional.of(this.session
                        .createNativeQuery("truncate table users", User.class)
                        .executeUpdate())
        );
    }

    private <T> Optional<T> beginTransaction(String errorMessage, Supplier<Optional<T>> transaction) {
        try {
            Optional<T> result;

            this.session = this.sessionFactory.openSession();
            this.session.beginTransaction();
            result = transaction.get();
            this.session.getTransaction().commit();

            return result;

        } catch (HibernateException e) {
            if (this.session != null) {
                this.session.getTransaction().rollback();
            }
            throw new RuntimeException(errorMessage + '\n' + e.getMessage());

        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }
}
