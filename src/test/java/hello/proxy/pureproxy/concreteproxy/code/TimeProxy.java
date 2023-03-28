package hello.proxy.pureproxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeProxy extends ConcreteLogic {

    //프록시니까 실제 호출하는 대상이 있어야 함.
    private ConcreteLogic concreteLogic;

    public TimeProxy(ConcreteLogic concreteLogic) {
        this.concreteLogic = concreteLogic;
    }

    //operation을 오버라이드 하자.

    //시간을 측정하는 로직이(추가기능) 들어간 프록시라고 보자.
    @Override
    public String operation() {
        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();
        String result = concreteLogic.operation();
        long endTime = System.currentTimeMillis();
        long resultTime =  endTime -startTime;
        log.info("TimeDecorator 종료 resultTime={}ms", resultTime);

        return result;
    }
}
