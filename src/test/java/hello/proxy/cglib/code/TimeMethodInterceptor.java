package hello.proxy.cglib.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {

    //항상 프록시는 호출할 target이 필요하다
    private final Object target;

    public TimeMethodInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        Object result = methodProxy.invoke(target, args);       //methodProxy를 사용하면 좀더 빨른 호출이 된다정도..

        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;

        log.info("Time Proxy 종료 resultTime ={}", resultTime);
        return result;
    }
}
