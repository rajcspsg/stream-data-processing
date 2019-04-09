package word.count.demo;

public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        //org.apache.kafka.clients.consumer.KafkaConsumer consumer = null;
        System.out.println(new App().getGreeting());
    }
}
