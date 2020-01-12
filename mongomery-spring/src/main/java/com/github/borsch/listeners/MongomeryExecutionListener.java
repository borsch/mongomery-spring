package com.github.borsch.listeners;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.github.borsch.annotations.DatabaseMongoSetup;
import com.github.borsch.annotations.ExpectedMongoDatabase;
import com.github.borsch.mongomery.MongoDBTester;
import com.mongodb.client.MongoDatabase;

public class MongomeryExecutionListener extends AbstractTestExecutionListener {

    private MongoDBTester tester;

    @Override
    public void prepareTestInstance(TestContext testContext) {
        final MongoTemplate mongoTemplate = testContext.getApplicationContext().getBean(MongoTemplate.class);
        final MongoDatabase mongoDatabase = mongoTemplate.getDb();

        tester = new MongoDBTester(mongoDatabase);
    }

    @Override
    public void beforeTestMethod(TestContext testContext) {
        getInitFilesLocation(testContext.getTestMethod())
            .forEach(tester::setDBState);
    }

    @Override
    public void afterTestMethod(TestContext testContext) {
        getExpectedFileLocations(testContext.getTestMethod())
            .forEach(tester::assertDBStateEquals);

        tester.dropDataBase();
    }

    private static List<String> getInitFilesLocation(final Method method) {
        final DatabaseMongoSetup mongoSetup = method.getAnnotation(DatabaseMongoSetup.class);
        final List<String> result = new ArrayList<>();
        if (mongoSetup != null) {
            Collections.addAll(result, mongoSetup.value());
        }
        return result;
    }

    private static List<String> getExpectedFileLocations(final Method method) {
        final ExpectedMongoDatabase mongoSetup = method.getAnnotation(ExpectedMongoDatabase.class);
        final List<String> result = new ArrayList<>();
        if (mongoSetup != null) {
            Collections.addAll(result, mongoSetup.value());
        }
        return result;
    }
}
