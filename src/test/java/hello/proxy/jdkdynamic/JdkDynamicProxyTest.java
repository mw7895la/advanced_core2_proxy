package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class JdkDynamicProxyTest {

    @Test
    void dynamicA() {
        AInterface target = new AImpl();

        //프록시에 적용할 핸들러 로직이다.
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        //Object proxy = Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);
        //즉 ,지정된 Class Loader에 의해 정의된 Interface를 구현한 Proxy 객체의 InvocationHandler를 가지고 있는 Object타입의 Proxy인스턴스라는 것이다.
        AInterface proxy = (AInterface) Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);
        //자바 언어차원에서 제공해주는 프록시 객체 생성 기술이다.
        //첫 번째 파라미터는 프록시가 어디에 생성될지 클래스 로더를 지정해줘야 한다.  getClassLoader()는 런타임에 클래스를 동적으로 JVM에 로드한다.
        //두 번째는 인터페이스를 넣어줘야하는데  어떤 인터페이스를 기반으로(구현하여) 프록시 만드는지 알아야 함. (인터페이스는 여러개일 수 있다.)
        //세 번째는 프록시가 사용해야할 로직을 넣어줘라.

        proxy.call();//왜냐, AInterface에 call() 이 있잖아.  // 이 프록시는 우리가 만든 핸들러 로직을 호출해주는 역할을 한다.

        /** proxy.call()을 호출하게 되면 proxy가 handler 로직을 수행한다. 그러면 handler의 invoke()가 실행되고, invoke()에서 파라미터 method는 call()을 넘겨받은 것이다.
         *  그럼 어떤것의 call()을 실행하냐, target의 call()이다  ( AInterface타입의 AImpl 구현체 ) */

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());    //proxyClass=class com.sun.proxy.$Proxy12 -JDK프록시가 만들어준 프록시 클래스, AInterface라는 것을 구현받아서 Proxy가 만들어진 것.
    }

    /**
     * 지금 A,B용 따로 프록시를 만들지 않았다.(proxy/jdkdynamic/code/)  TimeInvocationHandler 로직 하나만 만들어 놓고 , 프록시 클래스는 그냥 java.lang.reflect.Proxy를 통해서 만들었다. 프록시 객체는 실행시 만들어진다
     */

    @Test
    void dynamicB() {
        BInterface target = new BImpl();

        //프록시를 호출할 로직이 된다.
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        BInterface proxy = (BInterface) Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]{BInterface.class}, handler);

        proxy.call();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());    //proxyClass=class com.sun.proxy.$Proxy12 -JDK프록시가 만들어준 프록시 클래스, AInterface라는 것을 구현받아서 Proxy가 만들어진 것.
    }
}
