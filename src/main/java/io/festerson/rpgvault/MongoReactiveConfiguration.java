package io.festerson.rpgvault;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories
public class MongoReactiveConfiguration extends AbstractReactiveMongoConfiguration {

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017/rpgvault");
    }

    @Override
    protected String getDatabaseName() {
        return "rpgvault";
    }

    @Override
    public com.mongodb.reactivestreams.client.MongoClient reactiveMongoClient() {
        return com.mongodb.reactivestreams.client.MongoClients.create("mongodb://localhost:27017/rpgvaullt");
    }
}
