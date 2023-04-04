package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;

@Slf4j
public class AdvisorTest {
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

    @Test
    @DisplayName("직접 만든 포인트 컷")
    void advisorTest2() {
        ServiceInterface target=new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MyPointcut(), new TimeAdvice());
        // 어드바이저를 만들어야 되는데, 이게 기본으로 쓰이고, 여기에는 포인트컷과 어드바이스 둘다 넣어야함.
        // 뭐가 오든지 항상 참인 Pointcut.TRUE

        proxyFactory.addAdvisor(advisor);


        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        proxy.save();
        proxy.find();
        /** 1. save() 호출시 클라이언트가 프록시 호출하는데 어드바이스 적용 여부 확인한다. 포인트컷 필터 true가 나왔으니 부가기능 적용하고 실제 타겟도 호출
         * find()의 경우 포인트컷 필터가 false니까 부가기능 로직 호출 안하고 바로 실제 타겟 호출 .  */

    }

    /**
     * 처음 MethodMatcher 나 Pointcut을 생성할때 의존객체들에 대한 레퍼런스만 저장해두고,
     * proxy.save() 나 .find()의 실제 동작 메서드들이 호출되면 내부적으로 targetClass나 method 정보를 포인트컷에 파라미터로 전달한다*/
    static class MyPointcut implements Pointcut {
        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;        //항상 트루를 반환하는 클래스 필터
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new MyMethodMatcher();
        }
    }

    static class MyMethodMatcher implements MethodMatcher {
        private String matchName = "save";      //여거 때문에 save()는 true find()는 false

        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            //메서드가 넘어오고 타겟 클래스가 넘어온다
            boolean result = method.getName().equals(matchName);
            log.info("포인트 컷 호출 method={} targetClass={}", method.getName(), targetClass);
            log.info("포인트컷 결과  result={}", result);
            return result;
        }

        @Override
        public boolean isRuntime() {
            return false;           /** 여기가 false면 위의 matches가 호출이 되고  true면 아래 matches가 호출이 된다.  false면 어떤 장점이 있냐면, 위의 matches들의 데이터는 정적이다 그래서 캐싱이 가능하다
                                    그런데 아래 matches는 인수 넘어오는게 캐싱이 안된다. Object... args - 매개변수가 동적으로 변경된다. 딱, 이렇게만 알아두자. */
        }

        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            return false;
        }
    }


    @Test
    @DisplayName("스프링이 제공하는 포인트 컷")
    void advisorTest3() {
        ServiceInterface target=new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        //스프링이 제공하는 포인트 컷
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("save");//메서드의 이름이 save()인 경우에만 적용

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new TimeAdvice());
        // 어드바이저를 만들어야 되는데, 이게 기본으로 쓰이고, 여기에는 포인트컷과 어드바이스 둘다 넣어야함.
        // 뭐가 오든지 항상 참인 Pointcut.TRUE

        proxyFactory.addAdvisor(advisor);


        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        proxy.save();
        proxy.find();


    }
}
