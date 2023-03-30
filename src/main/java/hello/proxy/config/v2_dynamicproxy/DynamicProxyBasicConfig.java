package hello.proxy.config.v2_dynamicproxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceBasicHandler;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyBasicConfig {

    /** 프록시가 스프링 빈으로 등록된다. 프록시가 컨트롤러에서 호출이 된다.
     *
     * <<아래는 이전에 같은 V1을 사용했었던 예제의 내용 >>
     * OrderControllerInterfaceProxy 에는 @GetMapping 이 없는데 동작하는 것을 보니
     * 인터페이스의 구현체에는 @GetMapping 을 명시하지 않아도 되는건가요?
     *
     * 인터페이스에 어노테이션을 적용하면 해당 인터페이스 구현체에서는 해당 어노테이션을 명시해주지 않아도 적용됩니다
     *
     * OrderControllerV1  인터페이스의 경우  @RequestMapping  애노테이션이 있고, 우리는 InterfaceProxyConfig를 통해
     * Proxy들을 스프링 빈으로 등록했다( V1Impl들은 등록하지 않음) 그래서 처음에 OrderControllerInterfaceProxy가 호출이 된 것이다.
     * (인터페이스에 적용된 어노테이션들은  구현체에도 적용이 되므로)
     *
     * public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h) */


    @Bean
    public OrderControllerV1 orderControllerV1(LogTrace logTrace) {
        OrderControllerV1Impl orderController = new OrderControllerV1Impl(orderServiceV1(logTrace));

        OrderControllerV1 proxy = (OrderControllerV1)Proxy.newProxyInstance(OrderControllerV1.class.getClassLoader(),
                new Class[]{OrderControllerV1.class}, new LogTraceBasicHandler(orderController, logTrace));
        return proxy;
    }
    @Bean
    public OrderServiceV1 orderServiceV1(LogTrace logTrace) {
        OrderServiceV1 orderServiceV1 = new OrderServiceV1Impl(orderRepositoryV1(logTrace));
        //orderRepositoryV1(logTrace)는 프록시가 반환된다.

        OrderServiceV1 proxy = (OrderServiceV1)Proxy.newProxyInstance(OrderServiceV1.class.getClassLoader(),
                new Class[]{OrderServiceV1.class}, new LogTraceBasicHandler(orderServiceV1, logTrace));

        return proxy;
    }
    @Bean
    public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace) {
        OrderRepositoryV1 orderRepositoryV1 = new OrderRepositoryV1Impl();

        //동적 프록시를 만들자.
        OrderRepositoryV1 proxy = (OrderRepositoryV1)Proxy.newProxyInstance(OrderRepositoryV1.class.getClassLoader(),
                new Class[]{OrderRepositoryV1.class}, new LogTraceBasicHandler(orderRepositoryV1, logTrace));
        return proxy;
    }






}
