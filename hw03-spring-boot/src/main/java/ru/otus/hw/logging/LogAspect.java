package ru.otus.hw.logging;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
@Aspect
public class LogAspect {

    @Before("@annotation(ru.otus.hw.logging.Loggable)")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Прокси : " + joinPoint.getThis().getClass().getName());
        System.out.println("Класс : " + joinPoint.getTarget().getClass().getName());
        System.out.println("Аргументы : " + Arrays.toString(joinPoint.getArgs()));
        System.out.println("Вызов метода : " + joinPoint.getSignature().getName());
    }
}
