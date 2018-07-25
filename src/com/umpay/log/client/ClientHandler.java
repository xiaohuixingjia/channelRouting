/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.umpay.log.client;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends SimpleChannelUpstreamHandler {

    private Logger log = LoggerFactory.getLogger("ClientHandler");
    private String key;
    private String pwd;

    public ClientHandler(String key,String pwd) {
        this.key = key;
        this.pwd = pwd;
    }

    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e instanceof ChannelStateEvent &&
                ((ChannelStateEvent) e).getState() != ChannelState.INTEREST_OPS) {
            log.info(e.toString());
        }
        super.handleUpstream(ctx, e);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        log.info("try to connect server , key = ["+key+"] , pwd = ["+pwd+"] . ");
        e.getChannel().write(key+","+pwd);
    }

    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        String a = (String)e.getMessage();
        String s[] = a.split(",");
        short returncode = Short.valueOf(s[0]);

        switch (returncode) {
            case ReturnCode.AUTH_SUCC:
                log.info("server authentication success !");
                break;
            case ReturnCode.AUTH_FAIL:
                log.info("server authentication failure !");
                break;
            case ReturnCode.EXE_SUCC:
                log.debug("execute success .");
                break;
            case ReturnCode.EXE_FAIL:
                log.debug("execute failure .");
                break;
            case ReturnCode.SPEED:
                log.info("control send speed:"+Integer.valueOf(s[1]));
                break;
            default:
                log.warn("Unrecognized Return Code ["+returncode+"] .");
                break;
        }
    }

    public void channelClosed(ChannelHandlerContext ctx,ChannelStateEvent e){
        log.info("close");
//        Entry.stop();
//        com.umpay.collect.client.ClientFactory.ping().start();
//        ctx.getChannel().close();
//        log.info("close 1");
    }

    public void channelDisconnected(ChannelHandlerContext ctx,ChannelStateEvent e){
//        log.info("channelDisconnected");
//        Entry.stop();
//        com.umpay.collect.client.ClientFactory.ping().start();
//        ctx.getChannel().close();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        log.error("==error:"+e.getCause().getMessage());
//        Entry.stop();
//        com.umpay.collect.client.ClientFactory.ping().start();
//        ctx.getChannel().close();
    }
}
