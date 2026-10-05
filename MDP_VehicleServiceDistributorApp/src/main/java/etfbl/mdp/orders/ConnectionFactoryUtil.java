package etfbl.mdp.orders;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import etfbl.mdp.constants.Constants;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

public class ConnectionFactoryUtil {

	public static Connection createConnection() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(Constants.MQ_HOST);
		factory.setPort(5673);
		factory.setUsername(Constants.MQ_USERNAME);
		factory.setPassword(Constants.MQ_PASSWORD);
		return factory.newConnection();
	}
}
