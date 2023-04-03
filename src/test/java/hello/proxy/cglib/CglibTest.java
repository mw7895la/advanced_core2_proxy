package hello.proxy.cglib;

import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibTest {

    @Test
    void cglib() {
        /** ConcreteService는 인터페이스가 없다  , 인터페이스가 없는데 동적 프록시 만들자  */
        ConcreteService target = new ConcreteService();

        Enhancer enhancer = new Enhancer();             //cglib를 만드는 코드
        enhancer.setSuperclass(ConcreteService.class);  // cglib로 동적 프록시를 만들어야 되는데 인터페이스를 지정하는게 아닌  구체클래스를 기반으로 ConcreteService를 상속받는 프록시를 만들어야 해
        enhancer.setCallback(new TimeMethodInterceptor(target));        //실제 객체를 TimeMethodInterceptor의 파라미터로 넣어준다.

        //Object proxy = enhancer.create();
        //이 부분을 ConcreteService  타입으로 만들 수 있다.(캐스팅 가능)
        //왜? 프록시의 부모타입을  위에서  setSuperclass에서 지정했기 때문이다.
        ConcreteService proxy = (ConcreteService) enhancer.create();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
        //ConcreteService$$EnhancerByCGLIB$$25d6b0e3
        // CGLIB가 동적으로 클래스를 만들어 내는데  ConcreteService를 상속받아서 만든 것.

        proxy.call();
    }
}

