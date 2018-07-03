package com.yuechuankeji.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuechuankeji.init.ServerConfig;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**通信端口主类*/
public class BootStrapMgr {
	
	public static Logger log = LoggerFactory.getLogger(BootStrapMgr.class);
	
	private static final EventLoopGroup bossGroup = new NioEventLoopGroup();
	private static final EventLoopGroup workerGroup = new NioEventLoopGroup();
	public static ServerBootstrap bootStrap = null;
	
	public static BootStrapMgr inst ;
	
	public static BootStrapMgr getInst(){
		if(inst == null ){
			inst  = new BootStrapMgr();
		}
		return inst ;
	}
	
	
	public void start() throws Exception{
		if(bootStrap != null){
			log.error("bootStrap is not null");
			return;
		}
		
		bootStrap = new ServerBootstrap();
		bootStrap.group(bossGroup, workerGroup);
		bootStrap.channel(NioServerSocketChannel.class);
		bootStrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel sc) throws Exception {
				ChannelPipeline cp = sc.pipeline();
				cp.addLast("keepAlive" , 
						new IdleStateHandler(0, 0, ServerConfig.keepAliveTimeOut));
				cp.addLast("encrypt" , new NettyEncryptEncoder());
				cp.addLast("encoder" , new NettyProtocolEncoder());
				cp.addLast("decrypt" , new NettyDecryptDecoder());
				cp.addLast("decoder" , new NettyProtocolDecoder());
				cp.addLast("exception" , new NettyExceptionHandler());
				cp.addLast("handler" , new NettyHandler());
			}
		})
		.option(ChannelOption.SO_BACKLOG, 1000)//socket标准参数，当服务器处理线程全满时等待队列的最大长度，不会遇到这种情况
		.childOption(ChannelOption.TCP_NODELAY, true)//socket标准参数，关闭Nagle算法，保证通信高实时性
		.childOption(ChannelOption.SO_KEEPALIVE, true)//socket标准参数，启用socket心跳保活机制，避免连接自行断开
		.childOption(ChannelOption.RCVBUF_ALLOCATOR, 
				new AdaptiveRecvByteBufAllocator(20, 1024*64, 1024*64))//单次接受内容大小,最小20byte,最大64kb
		;
		bootStrap.bind(ServerConfig.port).sync();
		log.info("开始监听通信端口{}" , ServerConfig.port);
	}
	
	public void shutDown(){
		workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("关闭通信端口完成");
	}
}
