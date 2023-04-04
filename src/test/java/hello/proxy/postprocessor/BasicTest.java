package hello.proxy.postprocessor;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BasicTest {

    @Test
    void basicConfig() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicConfig.class);
        //이 자체가 스프링 컨테이너, 여기에다가 BasicConfig를 넣어주면 BasicConfig가 스프링 빈으로 등록이 된다.
        //@Bean 부분을 스프링 컨테이너가 뜨면서 호출하고 리턴된 객체를 스프링 빈에 등록한다.

        //A는 빈으로 등록된다.
        A a = applicationContext.getBean("beanA", A.class);
        a.helloA();

        //B는 빈으로 등록되지 않는다.
        //B b =applicationContext.getBean(B.class);
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(B.class));
    }

    @Slf4j
    @Configuration
    static class BasicConfig {
        @Bean(name = "beanA")       /** 강의자료 5장, 처음에 bean A를 빈 후처리기에 전달했을때 바꿔치기 하여 B 객체를 스프링 빈에 등록할 것이다. 이름만 beanA고 실제 빈 객체는 B다.*/
        public A a(){
            return new A();
        }
    }

    @Slf4j
    static class A{
        public void helloA(){
            log.info("hello A");
        }
    }
    @Slf4j
    static class B{
        public void helloB(){
            log.info("hello B");
        }
    }

}
