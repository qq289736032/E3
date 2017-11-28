package com.jisen.e3mall.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * 测试activemq
 * 
 * @author Administrator
 *
 */
public class ActiveMqSpring {
	/**
	 * tcp点到点形式发送消息
	 */
	@Test
	public void testSpringQueueProducer() throws Exception {
		//初始化spring容器/e3-manager-service/src/main/resources/spring/applicationContext-activemq.xml
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//从容器中获得jmstemplate
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		//从容器中获得一个destination对象那个
		Destination destination = (Destination) applicationContext.getBean("queueDestination");
		//发送消息
		jmsTemplate.send(destination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("send activemq message");
			}
		});
	}

	/**
	 * tcp点对点消费
	 * 
	 * @throws Exception
	 */
	@Test
	public void testQueueCusumer() throws Exception {
		// 创建连接工厂对象,需要指定服务的ip及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		// 使用工厂对象创建一个connection对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接,调用Connection的start方法
		connection.start();
		// 创建一个session对象,param1是否开启事物,一般不开启,如果开启,则第二个参数无意义
		// params:应答模式,自动应答和手动应答,一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 使用session对象创建一个destination对象,两种形式queue,topic,现在使用queue
		Queue queue = session.createQueue("spring-queue");
		// 使用session对 象创建一个Consumer对象
		MessageConsumer consumer = session.createConsumer(queue);
		// 接收消息
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				// 打印结果
				TextMessage textMessage = (TextMessage) message;
				String text;
				try {
					text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		// 等待接收消息
		System.in.read();
		// 关闭
		consumer.close();
		connection.close();
		session.close();
	}

	/**
	 * topic广播形式发送消息
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTopicProducer() throws Exception {
		// 创建连接工厂对象,需要指定服务的ip及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		// 使用工厂对象创建一个connection对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接,调用Connection的start方法
		connection.start();
		// 创建一个session对象,param1是否开启事物,一般不开启,如果开启,则第二个参数无意义
		// params:应答模式,自动应答和手动应答,一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 使用session对象创建一个destination对象,两种形式queue,topic,现在使用topic
		Topic topic = session.createTopic("testName");
		// 使用session对 象创建一个Product对象
		MessageProducer producer = session.createProducer(topic);
		// 创建一个message对象,可以使用Textmessage.
		// TextMessage textMessage = new ActiveMQTextMessage();
		// textMessage.setText("hello activemq");
		TextMessage textMessage2 = session.createTextMessage("topicmessage activemq");
		// 发送消息
		producer.send(textMessage2);
		// 关闭消息
		producer.close();
		connection.close();
		session.close();
	}

	/**
	 * topic广播形式接收消息
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTopicConsumer() throws Exception {
		// 创建连接工厂对象,需要指定服务的ip及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		// 使用工厂对象创建一个connection对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接,调用Connection的start方法
		connection.start();
		// 创建一个session对象,param1是否开启事物,一般不开启,如果开启,则第二个参数无意义
		// params:应答模式,自动应答和手动应答,一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 使用session对象创建一个destination对象,两种形式queue,topic,现在使用topic
		Topic topic = session.createTopic("testName");
		// 使用session对 象创建一个消费者对象
		MessageConsumer consumer = session.createConsumer(topic);
		// 接收消息
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				// 打印结果
				TextMessage textMessage = (TextMessage) message;
				String text;
				try {
					text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("消费者3已经启动");
		// 等待接收消息
		System.in.read();
		// 关闭
		consumer.close();
		connection.close();
		session.close();
	}
}
