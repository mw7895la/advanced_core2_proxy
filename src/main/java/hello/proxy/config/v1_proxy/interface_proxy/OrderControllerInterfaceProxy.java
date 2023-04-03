package hello.proxy.config.v1_proxy.interface_proxy;

import hello.proxy.app.v1.OrderControllerV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderControllerInterfaceProxy implements OrderControllerV1 {
    private final OrderControllerV1 target;
    private final LogTrace logTrace;


    @Override
    public String request(String itemId) {
        TraceStatus status=null;
        try {
            status = logTrace.begin("OrderController.request()");
            //target 호출
            String result = target.request(itemId);     //OrderController V1 은 문자를 반환하기 때문에 반환값이 있어야 한다.
            logTrace.end(status);

            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;        //흐름을 바꾸면 안돼서 예외를 그대로 던져라
        }
    }

    @Override
    public String noLog() {
        return target.noLog();
        /**
         * noLog는 로그를 찍으면 안돼, 우리가 시나리오상 보안상 문제가 있어서 로그를 안 찍는 케이스다.
         *
         * 관련된 것들 - LogTraceFilterHandler.java // DynamicProxyFilterConfig.java
         */
    }
}
