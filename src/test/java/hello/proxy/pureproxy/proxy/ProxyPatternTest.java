package hello.proxy.pureproxy.proxy;

import hello.proxy.pureproxy.proxy.code.CacheProxy;
import hello.proxy.pureproxy.proxy.code.ProxyPatternClient;
import hello.proxy.pureproxy.proxy.code.RealSubject;
import org.junit.jupiter.api.Test;

public class ProxyPatternTest {

    @Test
    void noProxyTest() {
        RealSubject realSubject = new RealSubject();
        ProxyPatternClient client = new ProxyPatternClient(realSubject);

        client.execute();
        client.execute();
        client.execute();
    }

    @Test
    void cacheProxyTest() {
        RealSubject realSubject = new RealSubject();
        CacheProxy cacheProxy = new CacheProxy(realSubject);                   //캐시 프록시에는 실제 객체가 주입되어야 함.
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);       //기존에는 RealSubject를 주입했지만 이제는 Proxy를 주입해야됨.

        client.execute();
        client.execute();
        client.execute();

        /**
         * 처음에는 캐시 프록시를 호출하고 처음에는 cacheValue값이 없기 때문에  실제 객체의 operation()을 호출한다.
         * 2번째 부터는 cacheValue값이 있으니 있던걸 바로 반환하다 ( 프록시에서 바로 반환 )
         */
    }
}
