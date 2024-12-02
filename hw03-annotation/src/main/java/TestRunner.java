import annotation.After;
import annotation.Before;
import annotation.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

    public void run(Class<?> testClass) {
        List<Method> beforeMethods = searchAnnotationMethod(Before.class, testClass);
        List<Method> afterMethods = searchAnnotationMethod(After.class, testClass);
        int success = 0;
        int failed = 0;
        int countTest = 0;

        Method[] methods = testClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                Object instance = createInstance(testClass);
                invokeMetnods(beforeMethods, instance);
                if (invokeTest(method, instance)) {
                    success += 1;
                }else {
                    failed += 1;
                }
                invokeMetnods(afterMethods, instance);
                countTest = failed + success;
            }
        }
        logger.info("Tests count: {} \nTest success: {} \nTest failed: {}",countTest, success, failed);
    }

    private List<Method> searchAnnotationMethod(Class<? extends Annotation> annotation, Class<?> testClass) {
        List<Method> methods = new ArrayList<>();
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                methods.add(method);
            }
        }
        return methods;
    }

    private void invokeMetnods(List<Method> methodsList, Object instance) {
        for (Method method : methodsList) {
            try {
                method.invoke(instance);
            } catch (Exception e) {
                logger.info("failed: {}", method.getName());
            }
        }
    }

    private boolean invokeTest(Method testMethod, Object testInstance) {
        try {
            testMethod.invoke(testInstance);
            logger.info("{} passed.", testMethod.getName());
            return true;
        } catch (Exception e) {
            logger.info("failed: {}", testMethod.getName());
            return false;
        }
    }

    private Object createInstance(Class<?> testClass) {
        try {
            return testClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
