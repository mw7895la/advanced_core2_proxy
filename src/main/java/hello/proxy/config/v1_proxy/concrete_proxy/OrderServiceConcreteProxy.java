package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

public class OrderServiceConcreteProxy extends OrderServiceV2 {

    // extends까지 선언만 하고 봤더니 오류가 뜬다. 왜냐면 OrderServiceV2는 생성자가 있다.
    // 이러면 자식에서 생성자를 호출해 줘야한다. 사실 기본생성자면 상관이없는데 기본생성자가 아니라서 호출해줘야한다.

    private final OrderServiceV2 target;
    private final LogTrace logTrace;

    public OrderServiceConcreteProxy(OrderServiceV2 target, LogTrace logTrace) {
        //부모 클래스의 생성자가 기본생성자였다면  사실상 super()는 생략해도 된다.(자바가 자동으로 super()를 넣어준다) 그런데 기본생성자가 아니다. 그리고 우린 지금 프록시를 쓰는게 목적이다.
        super(null);        // 지금 프록시를 쓰는게 목적이기 떄문에 부모 클래스를 전혀 안쓴다 그래서 null로 하면 된다.  지금 문법상 어쩔수 없이 부모 생성자를 호출해야하기 때문에 이렇게 한것이다.
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void orderItem(String itemId) {
        TraceStatus status=null;
        try {
            status = logTrace.begin("OrderService.orderItem()");
            //target 호출
            target.orderItem(itemId);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;        //흐름을 바꾸면 안돼서 예외를 그대로 던져라
        }
    }
}
