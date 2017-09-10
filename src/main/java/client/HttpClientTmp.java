package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import util.LoggerUtil;

import java.net.URI;

/**
 * Created by zw on 17-9-9.
 */
public class HttpClientTmp {
    private String Http_Ip;
    private int Http_Port;
    private void connect(String host, int port, String msg) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(new HttpClientInboundHandler());
                }
            });
            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();
            URI uri = new URI("/message");
//            String msg ="{borg=aaaa,bdevicetype=aaaa,bfloor=1,room=001,devicestates=00}";
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                    uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));
            // 构建http请求
            request.headers().set(HttpHeaderNames.HOST,host);
//            request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
//            request.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
//            request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());
//            request.headers().set(HttpHeaderNames.CONTENT_LENGTH,HttpHeaderValueS.);
            request.headers().setInt(HttpHeaderNames.CONTENT_LENGTH,request.content().readableBytes());
            // 发送http请求
            f.channel().write(request);
            f.channel().flush();
            f.channel().close().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }
    public boolean Http_Send(String ip,int port,String Msg){
        this.Http_Ip = ip;
        this.Http_Port = port;
        try {
            this.connect(ip,port,Msg);
        } catch (Exception e) {
            LoggerUtil.log.error(e);
            return false;
        }
        return true;
    }
    public static void main(String[] args) throws Exception {
//        HttpClient client = new HttpClient();
//        client.connect("127.0.0.1", 8888);
    }
}
