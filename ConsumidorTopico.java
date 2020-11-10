package rabbitmqUtility;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ConsumidorTopico {
	
	private static final String EXCHANGE_NAME = "pruebaColoresExchangue";
	//private static final String ROUTING_KEY = "blue";
	public static void main(String [] argv) throws IOException, TimeoutException {
		 ConnectionFactory factory = new ConnectionFactory();
			 factory.setUsername("guest");
			 factory.setPassword("guest");
			 factory.setHost("localhost");
			 factory.setPort(5672);
			 Connection conn;
		     Connection connection = factory.newConnection();
	         
	        
	        	conn = factory.newConnection();
	        	Channel channel = connection.createChannel();
	        	System.out.print("Se inicion la conexion");
	        	channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
	        	System.out.println("Se abre el canal");
	        	
	        	String queueName = channel.queueDeclare().getQueue();
	        	//channel.queueBind(queueName, EXCHANGE_NAME, ROUTING_KEY);
	        	
	        	
	        	if (argv.length < 1) {
		            System.err.println("Uso: ConsumidorTopico [clave_enlace]...");
		            System.exit(1);
		        }
	        	for (String bindingKey : argv) {
		            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
		        }
	        	
	        	System.out.println(" [*] EsperandoMensajes...");
	        	
	        	DeliverCallback deliverCallback = (consumerTag, delivery) -> {
		            String message = new String(delivery.getBody(), "UTF-8");
		            System.out.println(" [x] Recibido '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
		        };
		        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });


	  /*      channel.exchangeDeclare(EXCHANGE_NAME, "topic");
	        String queueName = channel.queueDeclare().getQueue();

	        
	        if (argv.length < 1) {
	            System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
	            System.exit(1);
	        }

	        for (String bindingKey : argv) {
	            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
	        }

	        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	            String message = new String(delivery.getBody(), "UTF-8");
	            System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
	        };
	        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
	  */  }

}
