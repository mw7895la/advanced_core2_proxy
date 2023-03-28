package hello.proxy.pureproxy.decorator;

import hello.proxy.pureproxy.decorator.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DecoratorPatternTest {

    @Test
    void noDecorator(){
        Component realComponent = new RealComponent();

        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);

        client.execute();
    }

    @Test
    void decorator1() {
        Component realComponent = new RealComponent();
        Component messageDecorator = new MessageDecorator(realComponent);
        DecoratorPatternClient client = new DecoratorPatternClient(messageDecorator);
        client.execute();
        //클라이언트가 메시지 데코레이터를 통해서 메시지 데코레이터가 리얼 컴포넌트를 호출한다.
        //RealComponent가 값을 반환하면 MessageDecorator가 값을 꾸며서 client에게 반환한다.
    }

    @Test
    void decorator2() {
        Component realComponent = new RealComponent();
        Component messageDecorator = new MessageDecorator(realComponent);
        Component timeDecorator = new TimeDecorator(messageDecorator);
        DecoratorPatternClient client = new DecoratorPatternClient(timeDecorator);
        client.execute();
        //client는 timedecorator를 의존하고  timedecorator는 messagedecorator를 의존한다. messageDecorator는 실제 realcomponent를 의존
        //Decorator들은 꾸며줄 대상이 있어야 한다.  여기선 TimeDecorator가 호출대상인 messageDecorator를 가지고 있다. 그리고 항상 호출대상이 필요하다.

    }
}
