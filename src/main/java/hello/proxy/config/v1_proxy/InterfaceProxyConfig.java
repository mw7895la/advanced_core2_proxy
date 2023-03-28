package hello.proxy.config.v1_proxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfaceProxyConfig {

    /**
     * 빈 이름이 중복이 될 수 있으니 메소드의 버전 정보를 빼자
     *
     * OrderControllerV1이 인터페이스긴 한데 이제는 구현체를 반환하면 안되고 프록시 구현체를 반환해야한다.
     * 그래야 프록시를 통해서 target인 실제 구현체가 호출이 되니까.
     *
     * LogTrace를 빈으로 등록했다고 하면, 파라미터로 주입받을 수 있다. 그럼 스프링빈이 넘어온다.
     *
     * 근데 OrderControllerV1Impl 구현체는 서비스 프록시를 호출해야될까? 진짜 서비스를 호출해야될까??
     * 서비스 프록시를 호출해야지~  그래야 서비스 프록시에서 또 로그Trace를 남기지..
     */

    /**
     * orderController 는 프록시를 생성하고 프록시를 빈으로 등록한다  구현체는 orderService()를 참조하는데 orderService()는 프록시를 반환한다.
     * 그리고 service프록시를 빈으로 등록한다.  service 구현체는 orderRepository()를 참조한다.  orderRepository()는 repository프록시를 반환한다.
     */

    @Bean
    public OrderControllerV1 orderController(LogTrace logTrace) {
        OrderControllerV1Impl controllerImpl = new OrderControllerV1Impl(orderService(logTrace));
        return new OrderControllerInterfaceProxy(controllerImpl,logTrace);
    }
    //orderController라는 이름의 빈이 등록되면서 실제 빈 객체는 OrderControllerInterfaceProxy 이다. 그럼 controllerImpl은??
    //이거는 프록시 객체를 통해서 참조될 뿐이다.

    @Bean
    public OrderServiceV1 orderService(LogTrace logTrace) {
        OrderServiceV1Impl serviceImpl = new OrderServiceV1Impl(orderRepository(logTrace));
        return new OrderServiceInterfaceProxy(serviceImpl,logTrace);
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace logTrace) {
        OrderRepositoryV1Impl repositoryImpl = new OrderRepositoryV1Impl();
        return new OrderRepositoryInterfaceProxy(repositoryImpl, logTrace);
    }
}
