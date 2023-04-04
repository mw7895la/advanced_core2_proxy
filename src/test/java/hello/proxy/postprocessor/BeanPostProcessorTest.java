package hello.proxy.postprocessor;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanPostProcessorTest {

    @Test
    void basicConfig() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class);
        //이 자체가 스프링 컨테이너, 여기에다가 BasicConfig를 넣어주면 BasicConfig가 스프링 빈으로 등록이 된다.
        //@Bean 부분을 스프링 컨테이너가 뜨면서 호출하고 리턴된 객체를 스프링 빈에 등록한다.

        //A는 빈으로 등록된다.
        //A a = applicationContext.getBean("beanA", A.class);
        //a.helloA();

        //빈 후처리기 등록 후/
        B b = applicationContext.getBean("beanA", B.class);
        b.helloB();
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(A.class));


        //B는 빈으로 등록되지 않는다.
        //B b =applicationContext.getBean(B.class);
        //Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(B.class));
    }

    @Slf4j
    @Configuration
    static class BeanPostProcessorConfig {
        @Bean(name = "beanA")       /** 강의자료 5장, 처음에 bean A를 빈 후처리기에 전달했을때 바꿔치기 하여 B 객체를 스프링 빈에 등록할 것이다. 이름만 beanA고 실제 빈 객체는 B다.*/
        public A a(){
            return new A();
        }

        @Bean
        public AToBPostProcessor helloPostProcessor(){
            return new AToBPostProcessor();
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

    @Slf4j
    static class AToBPostProcessor implements BeanPostProcessor {
        /** 인터페이스 안에 메서드가 default로 되어있다 , 이러면 구현을 안해도 된다. 우린 기능이 필요하니 오버라이드 하자.
         *  빈 후처리기를 사용할거면 스프링 빈으로 등록해라.
         *
         *  처음에 스프링 컨테이너가 beanA라는 이름으로 A 객체를 생성 한 다음, 빈 후처리기에 전달한다.(빈 후처리기를 우리가 스프링 빈으로 등록을 해놨으므로..)
         *  빈 후처리기에서 빈이름은 놔두고 빈 객체를 B로 바꿔치기 하여 스프링 빈으로 등록한다.
         *
         * */

        //초기화 다 끝나고 나서 빈 후처리기로 처리할 것.
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("beanName={} bean={}", beanName, bean);
            //스프링 컨테이너가 A객체를 전달할 때 빈 이름과 빈 객체 둘다 전달한다.

            if (bean instanceof A) {
                return new B();     //bean이 new A()면 B로 바꿔치기 한다.!
            }
            return bean;
        }
    }


}
