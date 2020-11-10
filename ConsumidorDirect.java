package rabbitmqUtility;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.security.auth.login.Configuration;

import  com.rabbitmq.client.Channel ; 
import  com.rabbitmq.client.Connection ; 
import  com.rabbitmq.client.ConnectionFactory ; 
import  com.rabbitmq.client.QueueingConsumer ;
 
public class ConsumidorDirect  implements Runnable{

	 private static final String EXCHANGE_NAME = "TestExchangue";
	 
	 private static final String ROUTING_KEY = "";
	 
	 private static final boolean NO_ACK = false;
	 
	/* public static void main(String []args) {
		ConsumidorTopico test = new ConsumidorTopico();
		test.run();
	}*/
	 public void run() {
		 String user="guest";
		 String pass= "guest";
		 String host="localhost";
		 int port= 5672;
		 
		 ConnectionFactory factory = new ConnectionFactory();
		 factory.setUsername(user);
		 factory.setPassword(pass);
		 factory.setHost(host);
		 factory.setPort(port);
		 
		 Connection conn;
		 try {
		 conn = factory.newConnection();
		 Channel channel = conn.createChannel();
		 channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
		 String queueName = channel.queueDeclare().getQueue();
		 channel.queueBind(queueName, EXCHANGE_NAME, ROUTING_KEY);
		 QueueingConsumer consumer = new QueueingConsumer(channel);
		 channel.basicConsume(queueName, NO_ACK, consumer);
		 while (true) { // you might want to implement some loop-finishing
			 
		 // logic here ;)
		 QueueingConsumer.Delivery delivery;
		 try {
		 delivery = consumer.nextDelivery();
		 System.out.println("received message: " + new String(delivery.getBody()) + " in thread: " + Thread.currentThread().getName());
		 channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		 } catch (InterruptedException ie) {
		 continue;
		 }
		 }
		 } catch (IOException e) {
		 e.printStackTrace();
		 } catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 }
}
