package ru.gordeev;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.gordeev.entities.Address;
import ru.gordeev.entities.Client;
import ru.gordeev.entities.Phone;

public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        Client client = new Client();
        client.setName("John Doe");

        Address address = new Address();
        address.setStreet("123 Main St");
        client.setAddress(address);

        Phone phone1 = new Phone();
        phone1.setNumber("123-456-7890");
        Phone phone2 = new Phone();
        phone2.setNumber("098-765-4321");

        client.getPhones().add(phone1);
        client.getPhones().add(phone2);

        session.save(client);

        session.getTransaction().commit();
        session.close();
    }
}

