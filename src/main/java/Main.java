import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Main {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Session session = sessionFactory.openSession();

        // создание запросов через Query builder
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Course> query = builder.createQuery(Course.class); // получаем курсы
        Root<Course> root = query.from(Course.class); // селект запрос методом from
//      query.select(root); // запрос всех элементов таблицы root

        // запрос курсов по условию - цена больше 100 тыс. Условия выбираем методом greater ... и т.д.
        query.select(root).where(builder.greaterThan(root.<Integer>get("price"), 100000))
                .orderBy(builder.desc(root.get("price"))); // все условия внутрь операторов передаются bilder,
                                                           // сортируем в обратном порядке по полю price

        List<Course> courseList = session.createQuery(query).setMaxResults(5).getResultList(); // получение результата
                                                                            // setMaxResults(5) получение 5 результатов
        for (Course course : courseList) {
            System.out.println(course.getName() + " - " + course.getPrice()); // печать названия курсов
        }

        // пример запроса с помощью hql
        String hql = "From " + Course.class.getSimpleName() + " Where price > 120000";
        List<Course> courseList1 = session.createQuery(hql).getResultList();
        for (Course course : courseList1) {
            System.out.println(course.getName() + " - " + course.getPrice());
        }

        // изменения в БД проводятся в рамках транзакции
//      Transaction transaction = session.beginTransaction();

        // создаем новый курс
//        Course course = new Course();
//        course.setName("Новый курс");
//        course.setType(CourseType.BUSINESS);
//        course.setTeacherId(1);

        // простой select запрос
//      Course course = session.get(Course.class, 1); // получаем курс

//      course.setName("Совсем новый курс"); // меняем параметры курса

//      session.delete(course); // удаление нового курса

//      System.out.println(course.getTeacher().getName()); // получ. учителя по Id курса (Связи ManyToOne и OneToMany)

//        System.out.println(course.getStudents().size()); // получ. кол-ва студентов (Связи ManyToMany)
//        List<Student> studentList = course.getStudents(); // получ. коллекции студентов
//        for (Student student : studentList) { // печать студентов, связанных с курсом
//            System.out.println(student.getName()); }
//
//        session.save(course);
//        transaction.commit();


//        // получение курса с помощью ORM системы hibernate
//        Course course = session.get(Course.class,1);
//        System.out.println(course.getName());

        sessionFactory.close();
    }
}

//
//        String url = "jdbc:mysql://localhost:3306/skillbox";
//        String user = "root";
//        String pass = "3507988Asd";
//        try {
//            // подключение к локальной базе данных
//            Connection connection = DriverManager.getConnection(url, user, pass);
//            Statement statement = connection.createStatement();
//            // запрос и проверка успешности запроса, булеан результат
//            statement.execute("UPDATE Courses SET name='Веб-разработчик с нуля до PRO' WHERE id = 1");
//            // селект запрос к базе с получением результата
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM Courses");
//            while (resultSet.next())
//            {
//                Course course = new Course(); // объект класса Course
//                course.setId(resultSet.getInt("id")); // присваиваем все поля
//
//
//                // получение данных из базы - поле из таблицы запроса
//                String courseName = resultSet.getString("name");
//                System.out.println(courseName);
//            }
//            resultSet.close();
//            statement.close();
//            connection.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//}