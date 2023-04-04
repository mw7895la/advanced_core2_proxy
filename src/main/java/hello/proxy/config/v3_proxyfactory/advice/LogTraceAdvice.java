package hello.proxy.config.v3_proxyfactory.advice;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

@Slf4j
public class LogTraceAdvice implements MethodInterceptor {

    /** 프록시 팩토리를 통해서 하면, target을 여기에 선언할 필요도 없다.*/
    private final LogTrace logTrace;

    public LogTraceAdvice(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TraceStatus status = null;

        try {
            //메소드를 뽑아보자.
            Method method = invocation.getMethod();

            //어떤 클래스의 메서드인지 알 수 있다. 지금 기존의 "OrderControllerV1.request()" 이걸 메시지로 쓰려고 하는 것.
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            //log.info(message);
            status = logTrace.begin(message);

            //target 호출(로직 호출)
            //Object result = method.invoke(target, args);
            Object result = invocation.proceed();

            logTrace.end(status);
            return result;

        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
