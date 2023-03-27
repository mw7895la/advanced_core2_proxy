package hello.proxy.pureproxy.proxy.code;

public class ProxyPatternClient {
    //Subject라는 인터페이스만 의존하고 있어서 어떤 구현체가 들어오든 클라이언트는 모른다.

    private Subject subject;

    public ProxyPatternClient(Subject subject) {        //생성자 주입
        this.subject = subject;
    }

    public void execute() {
        subject.operation();
    }
}
