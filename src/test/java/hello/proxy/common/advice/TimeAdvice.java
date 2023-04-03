package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


/** Advice 를 만들자 */
@Slf4j
public class TimeAdvice implements MethodInterceptor {
    //implements시 패키지 주의

    //이제는 타겟을 안넣어줘도 된다.  프록시 팩토리에서 동적 프록시를 만들때 타겟을 넣어준다.
    //private final Object target;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        //Object result = methodProxy.invoke(target, args);       //methodProxy를 사용하면 좀더 빨른 호출이 된다정도..
        Object result = invocation.proceed();
        //invocation 이 알아서 타겟을 찾아서  실행을 해준다.
        //왜냐면 프록시팩토리를 생성할 떄 타겟정보를 넘기기 때문이다.


        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;

        log.info("Time Proxy 종료 resultTime ={}", resultTime);
        return result;
    }
}
