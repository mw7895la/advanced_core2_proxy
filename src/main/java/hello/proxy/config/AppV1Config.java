package hello.proxy.config;

import hello.proxy.app.v1.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppV1Config {

    //스프링 빈으로 등록하면서 의존관계가 딱딱딱 주입되고 있다.
    @Bean
    public OrderControllerV1 orderControllerV1() {
        return new OrderControllerV1Impl(orderServiceV1());
    }

    @Bean
    public OrderServiceV1 orderServiceV1() {
        return new OrderServiceV1Impl(orderRepositoryV1());
    }

    @Bean
    public OrderRepositoryV1 orderRepositoryV1() {
        return new OrderRepositoryV1Impl();
    }

    /**
     * AppV1Config가 스프링 빈으로 등록이 되어야.. @Bean 3개가 스프링 빈으로 등록될 수 있는 것.
     *
     */
}
