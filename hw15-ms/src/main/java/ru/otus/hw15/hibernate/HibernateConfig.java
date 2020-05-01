package ru.otus.hw15.hibernate;

import java.util.Arrays;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;
import ru.otus.hw15.model.User;

@org.springframework.context.annotation.Configuration
public class HibernateConfig {

  @Bean
  public SessionFactory sessionFactory() {
    return HibernateConfig.buildSessionFactory("hibernate.cfg.xml", User.class);
  }

  private static SessionFactory buildSessionFactory(String configResourceFileName, Class<?>... annotatedClasses) {
    Configuration configuration = new Configuration().configure(configResourceFileName);
    MetadataSources metadataSources = new MetadataSources(createServiceRegistry(configuration));
    Arrays.stream(annotatedClasses).forEach(metadataSources::addAnnotatedClass);

    Metadata metadata = metadataSources.getMetadataBuilder().build();
    return metadata.getSessionFactoryBuilder().build();
  }

  private static StandardServiceRegistry createServiceRegistry(Configuration configuration) {
    return new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties()).build();
  }
}
