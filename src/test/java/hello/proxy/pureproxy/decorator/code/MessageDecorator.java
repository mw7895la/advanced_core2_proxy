package hello.proxy.pureproxy.decorator.code;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecorator implements Component {

    private Component component;        //데코레이터가 실제 Real Component를 알아야해

    public MessageDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("MessageDecorator 실행");
        String result = component.operation();
        String decoResult = "*****"+result+"*****";
        //데이터를 꾸며준다.
        log.info("MessageDecorator 꾸미기 적용 전 ={}, 적용 후={}", result, decoResult);
        return decoResult;
    }


}
