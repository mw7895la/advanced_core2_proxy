package hello.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheProxy implements Subject {

    //실제 RealSubject에(실제 객체) 접근할 수 있어야 한다.
    private Subject target;
    private String cacheValue;

    public CacheProxy(Subject target) {
        this.target = target;
    }
    //프록시가 RealSubject를 참조하도록 만든 것.

    @Override
    public String operation() {
        log.info("프록시 호출");
        if (cacheValue == null) {
            cacheValue = target.operation();        //실제 객체인 RealSubject의 operation() 호출 그러면 "data"가 들어가게 됨.
        }
        return cacheValue;                          //처음 조회 이후에는 바로 반환해준다. 즉 실제 객체에 접근하지 않았다.
    }
}
