package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class Advisor {
    /** 프록시 팩토리는 어드바이저를 알고 있고  , 어드바이저는 포인트컷 과 어드바이스를 알고 있다.*/
    @Test
    void advisorTest1() {
        ServiceInterface target=new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice());
        // 어드바이저를 만들어야 되는데, 이게 기본으로 쓰이고, 여기에는 포인트컷과 어드바이스 둘다 넣어야함.
        // 뭐가 오든지 항상 참인 Pointcut.TRUE

        proxyFactory.addAdvisor(advisor);


        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        proxy.save();
        proxy.find();

    }
}
