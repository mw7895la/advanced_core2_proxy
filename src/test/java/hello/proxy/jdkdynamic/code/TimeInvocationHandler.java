package hello.proxy.jdkdynamic.code;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class TimeInvocationHandler implements InvocationHandler {

    private final Object target;
    //타입이 Object다 굉장히 범용으로 쓰겠다.
    //프록시는 항상 프록시가 호출할 대상이 있어야 한다

    public TimeInvocationHandler(Object target) {
        this.target = target;
    }


    /** 어떤 메서드가 호출되어야 하는지 넘어온다 */
    /** 시간 측정기능은 추가기능이다 */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /**
         * Object proxy : 프록시 자신
         * Method method : 호출할 메서드
         * Object[] args : 메서드를 호출할 때 전달한 인수
         */
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();
        Object result = method.invoke(target, args);
        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;

        log.info("Time Proxy 종료 resultTime ={}", resultTime);
        return result;

    }
}
