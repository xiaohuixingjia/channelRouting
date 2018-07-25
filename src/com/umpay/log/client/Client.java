package com.umpay.log.client;


import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Executors;

public class Client {

	public static Logger log = LoggerFactory.getLogger("Client");

    private ClientBootstrap bootstrap;
    private Channel channel;

	private String host;
    private int port;
	private String key;
	private String pwd;

    public void start() {
        bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(
                        new ObjectEncoder(),
                        new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())),
                        new ClientHandler(key,pwd));
            }
        });


        channel = bootstrap.connect(new InetSocketAddress(host, port)).getChannel();
    }

    public void stop(){
        channel.close().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
        bootstrap.shutdown();
    }

    public boolean write(String line){
        try {
            return channel.write(line+"\n").sync().isSuccess();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return false;
    }

    public boolean write(List<String> list){
        try {
            return channel.write(list).sync().isSuccess();
        } catch (Exception e) {
            return false;
        }
    }

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}


}
