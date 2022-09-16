package com.example.spring_mongodb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Queue;

@SpringBootApplication
public class SpringMongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMongodbApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(
            StudentRepository repository, MongoTemplate mongoTemplate) {
        return args -> {
            Address address = new Address(
                    "Engladn",
                    "London",
                    "EN9"
            );
            String email = "jamed@gmail.com";
            Student student = new Student(
                    "Jamilia",
                    "Agmed",
                    email,
                    Gender.MALE,
                    address,
                    List.of("CS", "Math"),
                    BigDecimal.TEN,
                    LocalDateTime.now()
            );
//            usingMongoTemplateAndQuery(repository, mongoTemplate, email, student);

            repository.findStudentByEmail(email).ifPresentOrElse(s -> {
                System.out.println(s + "already exist");
            }, ()->{
                System.out.println("Insert student" + student);
                repository.insert(student);
            });
        };
    }

    private void usingMongoTemplateAndQuery(StudentRepository repository, MongoTemplate mongoTemplate, String email, Student student) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        List<Student> students = mongoTemplate.find(query, Student.class);
        if(students.size()>1){
            throw new IllegalStateException(
                    "found many students with email" + email
            );
        }
        if(students.isEmpty()){
            System.out.println("Insert student" + student);
            repository.insert(student);
        }else {
            System.out.println(student + "already exist");
        }
    }

}
