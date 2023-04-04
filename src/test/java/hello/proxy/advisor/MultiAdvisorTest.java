package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

@Slf4j
public class MultiAdvisorTest {
    @Test
    @DisplayName("여러 프록시 적용")
    void multiAdvisorTest1() {
        /**client -> proxy2(advisor2) -> proxy1(advisor1) -> target
         * 프록시2 가 프록시1을 호출해야 하니까 프록시2에 프록시1 을 걸어줌. */

        /**단점은 10개적용하려면 10개 다 만들어야함. */

        //프록시 1 생성
        ServiceInterface target=new ServiceImpl();      //shift + f6 하면 변수명 한번에 바꿀 수 있음.

        ProxyFactory proxyFactory1 = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());
        proxyFactory1.addAdvisor(advisor1);
        ServiceInterface proxy1 = (ServiceInterface) proxyFactory1.getProxy();

        //프록시 2 생성
        ProxyFactory proxyFactory2 = new ProxyFactory(proxy1);      // proxy2는 proxy1을 걸어야해.
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());
        proxyFactory2.addAdvisor(advisor2);
        ServiceInterface proxy2 = (ServiceInterface) proxyFactory2.getProxy();

        proxy2.save();      //클라이언트가 호출

    }

    @Test
    @DisplayName("하나의 프록시, 여러 어드바이저 ")
    void multiAdvisorTest2() {
        /**client -> proxy2 -> advisor2  -> advisor1 -> target
         *
         * 클라이언트가 프록시 호출하고 프록시가 어드바이저 호출하는데  어드바이스2번에 들어와, 어드바이스2번에서 포인트컷 체크하지 근데 항상 true니까 어드바이스 적용되고
         * 그다음 어드바이스1에서 포인트컷 체크하는데 true니까 어드바이스 적용되고 그다음에 target 호출. 중요한건, 프록시는 지금 1개다. 어드바이저만 여러개일 뿐...
         * */

        //프록시 1 생성
        ServiceInterface target=new ServiceImpl();      //shift + f6 하면 변수명 한번에 바꿀 수 있음.
        DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());
        ProxyFactory proxyFactory = new ProxyFactory(target);

        proxyFactory.addAdvisor(advisor2);      //위의 시나리오는 advisor2 -> advisor1 이기 때문에 순서도 맞춰줘야함.
        proxyFactory.addAdvisor(advisor1);

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();



        proxy.save();      //클라이언트가 호출

    }

    //어드바이스 2개 만들자
    static class Advice1 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice1 호출");
            return invocation.proceed();

        }

    }
    static class Advice2 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice2 호출");
            return invocation.proceed();

        }

    }


}
