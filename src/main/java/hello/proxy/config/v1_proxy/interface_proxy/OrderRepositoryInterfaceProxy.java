package hello.proxy.config.v1_proxy.interface_proxy;

import hello.proxy.app.v1.OrderRepositoryV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderRepositoryInterfaceProxy implements OrderRepositoryV1 {

    private final OrderRepositoryV1 target;      //실제 객체를 참조해야돼, 호출할 수 있어야 한다.

    private final LogTrace logTrace;
    @Override
    public void save(String itemId) {
        TraceStatus status=null;
        try {
            status = logTrace.begin("OrderRepository.request()");
            //target 호출
            target.save(itemId);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;        //흐름을 바꾸면 안돼서 예외를 그대로 던져라
        }
    }

    /**
     * 이렇게 구현하고 나면  기존 코드인 OrderControllerV1Impl 는 전혀 손 대지 않았다.  여긴 부가기능 로직이 다 프록시로 들어간 것이다.
     */
}
