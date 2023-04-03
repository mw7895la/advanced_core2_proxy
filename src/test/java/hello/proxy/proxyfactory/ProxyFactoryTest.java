package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl();        //인터페이스가 있는 구현체
        ProxyFactory proxyFactory = new ProxyFactory(target);
        //프록시 팩토리를 만들때 target을 넣어줌. 그래서 프록시 팩토리는 타겟정보를 이미 알고 있다.
        //여기선 인터페이스가 있기 때문에, 인터페이스 기반으로 JDK 동적 프록시를 생성한다.

        proxyFactory.addAdvice(new TimeAdvice());       //부가 기능로직 어드바이스를 우리가 만든 어드바이스로 넣어주면 된다.

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();        //위에서 만든 동적프록시의 객체를 생성하고 반환받아서 이제 사용하자.
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());   //인터페이스가 있으니까 JDK 동적 프록시

        proxy.save();
        /** 프록시 팩토리 생성 시 target을 넣어줬기 때문에 , 프록시 팩토리 내부에선 이미 InvocationHandler가(JDK) Advice를 호출하도록 했다.
         * Advice를 호출하면서 (TimeAdvice) invocation.proceed() 수행 시 실제 타겟 클래스에 있는 메서드 save() 로직을 수행한다. */

        //프록시 팩토리를 사용하면 장점이 있다.
        //아래 AopUtils들은 프록시 팩토리를 통해서 만들었을 때만 사용할 수 있다.
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        //Jdk동적 프록시인가??
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();

        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();

    }

    /** 위에 jdk 동적프록시에서 구체클래스만 있는 것으로 바꾸고 */
    @Test
    @DisplayName("구체 클래스만 있으면 CGLIB 사용")
    void concreteProxy() {
        ConcreteService target = new ConcreteService();
        ProxyFactory proxyFactory = new ProxyFactory(target);//프록시 팩토리를 만들때 target을 넣어줌. 그래서 프록시 팩토리는 타겟정보를 이미 알고 있다.

        proxyFactory.addAdvice(new TimeAdvice());
        //어드바이스를 우리가 만든 어드바이스를 넣어주면 된다.
        //이것은 단순히 편의메서드일 뿐,

        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();      //프록시 객체를 생성하고 그 결과를 받는다.
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());   //인터페이스가 있으니까 JDK 동적 프록시

        proxy.call();

        //프록시 팩토리를 사용하면 장점이 있다.
        //아래 AopUtils들은 프록시 팩토리를 통해서 만들었을 때만 사용할 수 있다.
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        //Jdk동적 프록시인가??
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();

        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();

    }

    /** 인터페이스가 있든 없든, 나는 CGLIB를 사용하겠다 !*/
    @Test
    @DisplayName("ProxyTargetClass 옵션을 사용하면 인터페이스가 있어도 CGLIB를 사용하고, 클래스기반 프록시 사용")
    void proxyTargetClass() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        /** 인터페이스가 있지만 클래스기반 동적 프록시를 만들겠다.*/
        proxyFactory.setProxyTargetClass(true);     // 프록시를 만드는데, 타겟 클래스를 기반으로 프록시를 만들 거다 !

        proxyFactory.addAdvice(new TimeAdvice());

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());        //proxyClass=class hello.proxy.common.service.ServiceImpl$$EnhancerBySpringCGLIB$$1cb49ff8

        proxy.save();

        //프록시 팩토리를 사용하면 장점이 있다.
        //아래 AopUtils들은 프록시 팩토리를 통해서 만들었을 때만 사용할 수 있다.
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        //Jdk동적 프록시인가??
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();

        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();

    }
}
