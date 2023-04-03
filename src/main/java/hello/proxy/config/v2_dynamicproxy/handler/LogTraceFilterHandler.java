
package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;        //실제 객체
    private final LogTrace logTrace;
    private final String[] patterns;    /** 메서드 명이 patterns에 있을 때만 로그를 남기도록 할 것이다. */

    public LogTraceFilterHandler(Object target, LogTrace logTrace, String[] patterns) {
        this.target = target;
        this.logTrace = logTrace;
        this.patterns = patterns;
    }

    /**먼저 똑같은 메시지가 나가지 않도록 해결해보자, 메서드 메타정보가 넘어오는데 여기서 어떤 메소드가 호출되는지 알 수 있다. */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /** 메서드 이름 필터, 어떤 메서드가 호출되는지 메서드 이름을 얻는다.*/
        String methodName = method.getName();
        //save, request, reque* , *est   과 같은 패턴 적용해보자

        /** PatternMatchUtils는 추상 클래스로 선언된 유틸 클래스다 첫 번째 파라미터는 string , string[] 이다.
         * pattern styles: "xxx*", "*xxx", "*xxx*" and "xxx*yyy" matches (with an arbitrary number of pattern parts), as well as direct equality. */

        if (!PatternMatchUtils.simpleMatch(patterns, methodName)) {
            //만약 매칭이 안되면 , 넌 프록시 로직 호출하지말고 실제 로직을 바로 호출해준다.
            // v1/no-log 호출하면 로그가 남지 않는다.
            return method.invoke(target, args);
        }


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
