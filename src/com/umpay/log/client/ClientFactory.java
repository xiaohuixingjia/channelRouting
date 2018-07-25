package com.umpay.log.client;


import java.util.ArrayList;
import java.util.List;

import com.umpay.log.util.ConfigParams;

/**
 * @name ClientFactory
 * @description 通信模块单例方法
 * @author tyf
 * @company umpay
 * @date 2013-8-06
*/


public class ClientFactory {
	private static Client client;

	public static void main(String args[]){
		ClientFactory.client().start();

    	while(true){
        	long a = System.currentTimeMillis();
            for(int i=1;i<=2;i++){
                List<String> list = new ArrayList<String>();
            	for(int j=1;j<=1;j++){
            		list.add("mt,20130710094829,0101,18610919701,84416536292450,10,1307100948284812,0,0,18610919701,,,3,15,uafPsrvxtcMxMDAuMDDUqs/WvfDIr6OsyK/C6zk1NjMzMzQzMTOjrNPQ0KfG2tbBN9TCMzHI1aOstqmyzcrkyOvIr8Lrz+3K3Lz1w+KjrMDbu/3P1r3wyK+/yc/tw+K30dPDss2jrNTZvdPU2cD44Lihvrr0xfO7vdPRob8=,427498,-XXCX,01,000000");
            	}
            	if(!ClientFactory.client().write(list)){
            		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            	}
//            	channel.writeAndFlush("mt,20130710094829,0101,18610919701,84416536292450"+i+",10,1307100948284812,0,0,18610919701,,,3,15,uafPsrvxtcMxMDAuMDDUqs/WvfDIr6OsyK/C6zk1NjMzMzQzMTOjrNPQ0KfG2tbBN9TCMzHI1aOstqmyzcrkyOvIr8Lrz+3K3Lz1w+KjrMDbu/3P1r3wyK+/yc/tw+K30dPDss2jrNTZvdPU2cD44Lihvrr0xfO7vdPRob8=,427498,-XXCX,01,000000\n");

//            	if(!cf.isSuccess()){
//            		System.out.println("=============");
//            	}
            }
        	long b = System.currentTimeMillis();
        	System.out.println(b-a);

        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}

	synchronized public static Client client() {
		if(client == null){
			client = new Client();
			client.setHost(ConfigParams.getProp("client.host"));
			client.setPort(Integer.parseInt(ConfigParams.getProp("client.port")));
			client.setKey(ConfigParams.getProp("client.key"));
			client.setPwd(ConfigParams.getProp("client.pwd"));
		}
		return client;
	}

	
}
