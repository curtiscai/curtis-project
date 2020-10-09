package com.curtis.rabbitmq.basic;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.100.160");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("root");

        String exchangeName = "exchange_demo";
        String routingKey = "routingkey_demo";
        String queueName = "queue_demo";
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            /**
             * @param exchange the name of the exchange 交换机名称
             * @param type the exchange type 交换机类型
             * @param durable true if we are declaring a durable exchange (the exchange will survive a server restart) 创建持久化交换机
             * @param autoDelete true if the server should delete the exchange when it is no longer in use 创建非自动删除交换机
             * @param arguments other properties (construction arguments) for the exchange
             */
            // 创建类型为direct，持久化、非自动删除的交换机exchange_demo
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, null);
            /**
             * @param queue the name of the queue 队列名字
             * @param durable true if we are declaring a durable queue (the queue will survive a server restart) 创建持久化队列
             * @param exclusive true if we are declaring an exclusive queue (restricted to this connection) 创建非排他队列
             * @param autoDelete true if we are declaring an autodelete queue (server will delete it when no longer in use) 创建非自动删除队列
             * @param arguments other properties (construction arguments) for the queue
             */
            // 创建持久化、非自动删除、非排他的队列queue_demo
            channel.queueDeclare(queueName, true, false, false, null);
            // 将交换机和队列通过路由键进行绑定
            channel.queueBind(queueName, exchangeName, routingKey);


            channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
                @Override
                public void handleConsumeOk(String consumerTag) {
                    super.handleConsumeOk(consumerTag);
                }

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    super.handleDelivery(consumerTag, envelope, properties, body);
                    LOGGER.info("receive message : {}", new String(body, StandardCharsets.UTF_8));
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            });
            // 关闭资源
            channel.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
