package hello.proxy.pureproxy.concreteproxy.code;

public class ConcreteClient {
    private ConcreteLogic concreteLogic;        // ConcreteLogic , TimeProxy 모두 주입 가능
                                                // ConcreteLogic의 자식 클래스들은 다 여기 주입 가능.
    public ConcreteClient(ConcreteLogic concreteLogic) {
        this.concreteLogic = concreteLogic;
    }

    public void execute(){
        concreteLogic.operation();
    }
}
