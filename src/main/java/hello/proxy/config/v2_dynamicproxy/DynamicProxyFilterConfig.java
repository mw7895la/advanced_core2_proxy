package hello.proxy.config.v2_dynamicproxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceBasicHandler;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceFilterHandler;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyFilterConfig {
    /** PATTERNS를 추가하고 LogTraceFilterHandler로 바꿔주자. */

    private static final String[] PATTERNS = {"request*", "order*", "save*"};       //이 단어들로 시작해야 로그를 남길 거다.


    @Bean
    public OrderControllerV1 orderControllerV1(LogTrace logTrace) {
        OrderControllerV1Impl orderController = new OrderControllerV1Impl(orderServiceV1(logTrace));

        OrderControllerV1 proxy = (OrderControllerV1)Proxy.newProxyInstance(OrderControllerV1.class.getClassLoader(),
                new Class[]{OrderControllerV1.class}, new LogTraceFilterHandler(orderController, logTrace,PATTERNS));
        return proxy;
    }
    @Bean
    public OrderServiceV1 orderServiceV1(LogTrace logTrace) {
        OrderServiceV1 orderServiceV1 = new OrderServiceV1Impl(orderRepositoryV1(logTrace));
        //orderRepositoryV1(logTrace)는 프록시가 반환된다.

        OrderServiceV1 proxy = (OrderServiceV1)Proxy.newProxyInstance(OrderServiceV1.class.getClassLoader(),
                new Class[]{OrderServiceV1.class}, new LogTraceFilterHandler(orderServiceV1, logTrace,PATTERNS));

        return proxy;
    }
    @Bean
    public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace) {
        OrderRepositoryV1 orderRepositoryV1 = new OrderRepositoryV1Impl();

        //동적 프록시를 만들자.
        OrderRepositoryV1 proxy = (OrderRepositoryV1)Proxy.newProxyInstance(OrderRepositoryV1.class.getClassLoader(),
                new Class[]{OrderRepositoryV1.class}, new LogTraceFilterHandler(orderRepositoryV1, logTrace,PATTERNS));
        return proxy;
    }






}
