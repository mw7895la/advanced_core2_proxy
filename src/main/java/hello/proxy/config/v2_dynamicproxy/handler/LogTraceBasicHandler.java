package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
@Slf4j
public class LogTraceBasicHandler implements InvocationHandler {

    private final Object target;        //실제 객체
    private final LogTrace logTrace;

    public LogTraceBasicHandler(Object target, LogTrace logTrace) {
        this.target = target;
        this.logTrace = logTrace;
    }
    /**먼저 똑같은 메시지가 나가지 않도록 해결해보자, 메서드 메타정보가 넘어오는데 여기서 어떤 메소드가 호출되는지 알 수 있다. */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //로직을 적용하자
        TraceStatus status = null;

        try {
            //어떤 클래스의 메서드인지 알 수 있다. 지금 기존의 "OrderControllerV1.request()" 이걸 메시지로 쓰려고 하는 것.
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            //log.info(message);
            status = logTrace.begin(message);

            //target 호출(로직 호출)
            Object result = method.invoke(target, args);
            //String result = target.request(itemId);

            logTrace.end(status);
            return result;

        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
