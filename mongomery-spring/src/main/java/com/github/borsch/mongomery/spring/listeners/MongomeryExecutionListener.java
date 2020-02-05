package com.github.borsch.mongomery.spring.listeners;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.github.borsch.mongomery.MongoDBTester;
import com.github.borsch.mongomery.spring.types.DatabaseMongoSetup;
import com.github.borsch.mongomery.spring.types.ExpectedMongoDatabase;
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
        getInitFilesLocation(testContext.getTestClass())
            .forEach(tester::setDBState);
        getInitFilesLocation(testContext.getTestMethod())
            .forEach(tester::setDBState);
    }

    @Override
    public void afterTestMethod(TestContext testContext) {
        tester.cleanIgnorePath();

        getExpectedFileLocations(testContext.getTestMethod(), expectedMongoDatabase -> {
            tester.addIgnorePaths(expectedMongoDatabase.defaultExcludeFields());
            tester.addIgnorePaths(expectedMongoDatabase.excludeFields());
            Arrays.asList(expectedMongoDatabase.value())
                .forEach(tester::assertDBStateEquals);
        });

        tester.dropDataBase();
    }

    private static List<String> getInitFilesLocation(final AnnotatedElement annotatedElement) {
        final DatabaseMongoSetup mongoSetup = annotatedElement.getAnnotation(DatabaseMongoSetup.class);
        final List<String> result = new ArrayList<>();
        if (mongoSetup != null) {
            Collections.addAll(result, mongoSetup.value());
        }
        return result;
    }

    private static void getExpectedFileLocations(final Method method, final Consumer<ExpectedMongoDatabase> consumer) {
        final ExpectedMongoDatabase mongoSetup = method.getAnnotation(ExpectedMongoDatabase.class);

        if (mongoSetup != null) {
            consumer.accept(mongoSetup);
        }
    }
}
