package hello.proxy.config.v6_aop.aspect;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;

@Aspect
/** 어드바이스 + 포인트컷 = 어드바이저를 편리하게 생성해준다.*/
@Slf4j
public class LogTraceAspect {

    private final LogTrace logTrace;


    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    //지금은 그냥 아~ 이런걸로 포인트컷 어드바이스 를 설정하는구나
    @Around("execution(* hello.proxy.app..*(..))")          //이렇게 하면 모든 메서드에 해당하는 포인트 컷.
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable{
        //@Around("execution(* hello.proxy.app..*(..))") 포인트 컷이고
        //메서드 안은 부가기능 로직인 어드바이스 !

        TraceStatus status = null;

        try {
            //메소드를 뽑아보자.
            String message = joinPoint.getSignature().toShortString();
            //Method method = invocation.getMethod();
            //String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            //어떤 클래스의 메서드인지 알 수 있다. 지금 기존의 "OrderControllerV1.request()" 이걸 메시지로 쓰려고 하는 것.
            status = logTrace.begin(message);

            //로직 호출  //target 호출
            Object result = joinPoint.proceed();
            //Object result = invocation.proceed();
            //Object result = method.invoke(target, args);

            logTrace.end(status);
            return result;

        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
