package com.xuzhiyong.comego.debug;

//import android.app.Activity;
//import android.content.Context;
//
//import com.duowan.fw.Module;
//import com.duowan.fw.ThreadBus;
//import com.duowan.fw.util.JNetworkUtil;
//import com.duowan.gamego.R;
//import com.duowan.gamego.module.DConst;
//import com.duowan.gamego.module.DModule;
//import com.duowan.gamego.module.analysis.AnalysisInterface;
//import com.duowan.gamego.module.analysis.LocalStatics;
//import com.duowan.gamego.module.datacenter.tables.ProtoStatistics;
//import com.duowan.gamego.module.login.LoginHelper;
//import com.duowan.gamego.module.login.LoginInterface;
//import com.duowan.gamego.module.net.NetClient;
//import com.duowan.gamego.module.net.NetDataChannel;
//import com.duowan.gamego.module.net.NetPing;
//import com.duowan.gamego.module.net.NetRequest;
//import com.duowan.gamego.module.net.Proto;
//import com.duowan.gamego.ui.base.GToast;
//import com.duowan.gamego.ui.main.MainActivity;
//import com.google.gson.Gson;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
//import protocol.ProtoBody;

public class DLocalCommands {


//    /**
//     * ********************************************************************************
//     */
//    public interface LocalCommandCallback {
//        public void dealWithCommand(DLocalCommandEnv env, String name, String[] args, Context context, Object result);
//    }
//
//    public interface LocalCommand {
//        public Object dealWithCommand(DLocalCommandEnv env, String[] args, Context context);
//    }
//
//    public static class LocalCommandMiddle {
//        public LocalCommand command;
//        public String input;
//        public Object result;
//    }
//    /************************************************************************************/
//
//
//    /************************************************************************************/
//    /**
//     * *
//     * 本地命令环境
//     */
//    public static class DLocalCommandEnv {
//        public ConcurrentHashMap<String, Object> allChild = new ConcurrentHashMap<String, Object>();
//        public ConcurrentHashMap<String, LocalCommand> allCommands = new ConcurrentHashMap<String, LocalCommand>();
//        public ConcurrentLinkedQueue<LocalCommandMiddle> allMiddles = new ConcurrentLinkedQueue<LocalCommandMiddle>();
//        public boolean open = false;
//
//        public DLocalCommandEnv() {
//            addLocalCommand("open", new LocalCommand() {
//
//                @Override
//                public Object dealWithCommand(DLocalCommandEnv env, String[] args, Context context) {
//                    env.open = true;
//                    GToast.show("OPEN LOCAL COMMAND SUCCESSFUL");
//                    return null;
//                }
//            });
//            addLocalCommand("close", new LocalCommand() {
//
//                @Override
//                public Object dealWithCommand(DLocalCommandEnv env, String[] args, Context context) {
//                    env.open = false;
//                    GToast.show("CLOSE LOCAL COMMAND SUCCESSFUL");
//                    return null;
//                }
//            });
//            addLocalCommand("alias", new LocalCommand() {
//
//                @Override
//                public Object dealWithCommand(DLocalCommandEnv env, String[] args,
//                                              Context context) {
//                    if (args.length == 3) {
//                        allChild.put(args[1], args[2]);
//                    }
//                    return null;
//                }
//            });
//        }
//
//        public boolean dealWithCommandPipe(String commandString, Context context, LocalCommandCallback callback) {
//            String[] commands = commandString.split("\\|");
//            if (commands.length >= 1) {
//                for (String exc : commands) {
//                    if (!dealWithCommand(exc, context, callback)) {
//                        return false;
//                    }
//                }
//                return true;
//            }
//
//            return false;
//        }
//
//        public boolean dealWithCommand(String commandString, Context context, LocalCommandCallback callback) {
//            //{{{ alias
//            Object alias = allChild.get(commandString);
//            if (alias != null && alias instanceof String) {
//                commandString = String.class.cast(alias);
//
//                return dealWithCommandPipe(commandString, context, callback);
//            }
//            //}}}
//
//            String[] commands = commandString.split("@| ");
//            if (commands.length >= 1) {
//                LocalCommand locCommand = allCommands.get(commands[0]);
//                if (locCommand != null) {
//                    Object dealed = locCommand.dealWithCommand(this, commands, context);
//                    if (callback != null) {
//                        callback.dealWithCommand(this, commands[0], commands, context, dealed);
//                    }
//                    enter(locCommand, commandString, dealed);
//
//                    return true;
//                } else {
//                    GToast.show("NOT FOUND COMMAND: " + commandString);
//                }
//            }
//            return false;
//        }
//
//        public boolean dealWithLocalCommand(String command, Context context, LocalCommandCallback callback) {
//            boolean strong = command.endsWith("#@@") && command.startsWith("@@#");
//            if (open && !strong) {
//                return dealWithCommandPipe(command, context, callback);
//            }
//            if (command.length() > 6 && strong) {
//                String commandString = command.substring(3, command.length() - 3);
//                return dealWithCommandPipe(commandString, context, callback);
//            }
//
//            return false;
//        }
//
//        public boolean addLocalCommand(String name, LocalCommand command) {
//            if (allCommands.containsKey(name)) {
//                return false;
//            }
//
//            allCommands.put(name, command);
//
//            return true;
//        }
//
//        private void enter(LocalCommand local, String comand, Object result) {
//            LocalCommandMiddle middle = new LocalCommandMiddle();
//            middle.command = local;
//            middle.input = comand;
//            middle.result = result;
//            allMiddles.add(middle);
//            while (allMiddles.size() > 3) {
//                allMiddles.poll();
//            }
//        }
//
//        public LocalCommandMiddle cur() {
//            return allMiddles.peek();
//        }
//    }
//
//    public static DLocalCommandEnv lcEnv = new DLocalCommandEnv();
//
//    public static DLocalCommandEnv env() {
//        return lcEnv;
//    }
//    /************************************************************************************/
//
//    /**
//     * ********************************************************************************
//     */
//    public static class SimpleDebugHttpServer extends NanoHTTPD {
//
//        public SimpleDebugHttpServer(int port) {
//            super(port);
//        }
//
//        @Override
//        public Response serve(IHTTPSession session) {
//            Method method = session.getMethod();
//            String uri = session.getUri();
//            System.out.println(method + " '" + uri + "' ");
//
//            String msg = "<html><body><h1>Local Command System</h1>\n";
//            Map<String, String> parms = session.getParms();
//            if (parms.size() == 0) {
//                msg +=
//                        "<form action='?' method='get'>\n" +
//                                "  <p>Your command: <input type='text' name='command'></p>\n" +
//                                "</form>\n";
//            } else if (parms.get("command") != null) {
//                boolean result = DLocalCommands.env().dealWithLocalCommand(parms.get("command"), Module.gMainContext, null);
//                if (result) {
//                    msg += "<p>Execute Command Successful: " + parms.get("command") + "!</p>";
//                } else {
//                    msg += "<p>Not Found Command Or Execute Failed: " + parms.get("command") + "!</p>";
//                }
//
//                if (parms.get("command").equals("proto")) {
//                    msg += checkProtoStatistic();
//                }
//            }
//            msg += "</body></html>\n";
//
//            return new NanoHTTPD.Response(msg);
//        }
//
//        private String checkProtoStatistic() {
//            List<ProtoStatistics> list = LocalStatics.ns().checkAll();
//            StringBuilder sb = new StringBuilder();
//            sb.append("<h3>Proto</h3><p><blockquote><style type=\"text/css\">th,td{text-align:left;}</style><table border=\"1\" >");
//            sb.append("<tr><th>proto</th><th>net</th><th>op</th><th>size</th><th>date</th></tr>");
//
//            for (ProtoStatistics ps : list) {
//                sb.append("<tr><th>").append(ps.tag).append("</th><th>")
//                        .append(ps.getNetWay()).append("</th><th>")
//                        .append(ps.getOpWay()).append("</th><th>")
//                        .append(ps.size).append("</th><th>")
//                        .append(ps.getDay()).append("</th></tr>");
//            }
//
//            sb.append("</table></blockquote></p>");
//
//            return sb.toString();
//        }
//    }
//
//    /************************************************************************************/
//    /************************************************************************************/
//    static {
//        // 切换到测试服务器
//        lcEnv.addLocalCommand("test", new LocalCommand() {
//
//            @Override
//            public Object dealWithCommand(DLocalCommandEnv env, String[] args, Context context) {
//                // @@#test#@@
////                DModule.ModuleLogin.cast(LoginInterface.class).logout();
//                ThreadBus.bus().post(ThreadBus.Working, new Runnable() {
//                    @Override
//                    public void run() {
//                        NetClient.switchServer(NetClient.Server.Server_Test);
//
////	                    LoginHelper.loginWithMacAddress();
//                    }
//                });
//                return true;
//            }
//        });
//
//        // 发送协议
//        lcEnv.addLocalCommand("send", new LocalCommand() {
//
//            @Override
//            public Object dealWithCommand(DLocalCommandEnv env, String[] args,
//                                          Context context) {
//                if (args.length >= 5) {
//                    StringBuilder sb = new StringBuilder();
//                    sb.append(args[4]);
//                    for (int i = 5; i < args.length; ++i) {
//                        sb.append(" ");
//                        sb.append(args[i]);
//                    }
//                    Gson gson = new Gson();
//                    try {
//                        int type = Integer.valueOf(args[1]);
//                        int sub = Integer.valueOf(args[2]);
//                        int res = Integer.valueOf(args[3]);
//                        ProtoBody body = gson.fromJson(sb.toString(), ProtoBody.class);
//                        NetRequest.newBuilder(type, sub, res, body).setHandler(new NetRequest.ProtoHandler() {
//
//                            @Override
//                            public void onTimeOut(Proto proto) {
//                                GToast.show("TIME OUT");
//                            }
//
//                            @Override
//                            public void onRespond(Proto proto) {
//                                GToast.show(proto.body.toString());
//                            }
//                        }).setTimeOut(DConst.KC_MaxNetOperatorTimeOut).request();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                return null;
//            }
//        });
//
//        // 启动http server
//        lcEnv.addLocalCommand("httpd", new LocalCommand() {
//
//            @Override
//            public Object dealWithCommand(DLocalCommandEnv env, String[] args,
//                                          Context context) {
//                Object __httpd = env.allChild.get("__httpd");
//                if (__httpd == null || !(__httpd instanceof SimpleDebugHttpServer)) {
//                    SimpleDebugHttpServer httpd = new SimpleDebugHttpServer(31234);
//                    try {
//                        httpd.start();
//                        env.allChild.put("__httpd", httpd);
//                        String ip = JNetworkUtil.getIpAddress(Module.gMainContext);
//                        GToast.show("Start Http Server At http://" + ip + ":" + 31234);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    String ip = JNetworkUtil.getIpAddress(Module.gMainContext);
//                    GToast.show("Already Start Http Server At http://" + ip + ":" + 31234);
//                }
//                return null;
//            }
//        });
//
//        // 登出
//        lcEnv.addLocalCommand("logout", new LocalCommand() {
//
//            @Override
//            public Object dealWithCommand(DLocalCommandEnv env, String[] args,
//                                          Context context) {
//                DModule.ModuleLogin.cast(LoginInterface.class).logout();
//	            GToast.show(R.string.has_logout);
//                return null;
//            }
//        });
//
//        // 切换到正式服务器
//        lcEnv.addLocalCommand("official", new LocalCommand() {
//
//            @Override
//            public Object dealWithCommand(DLocalCommandEnv env, String[] args, Context context) {
//                // @@#official#@@
////                DModule.ModuleLogin.cast(LoginInterface.class).logout();
//
//                ThreadBus.bus().post(ThreadBus.Working, new Runnable() {
//	                @Override
//	                public void run() {
//		                NetClient.switchServer(NetClient.Server.Server_Official);
//
////		                LoginHelper.loginWithMacAddress();
//	                }
//                });
//
////	            if (context != null && context instanceof Activity) {
////		            MainActivity.jumpMain((Activity) context, 3);
////	            }
//                return false;
//            }
//        });
//
//        // 切换到新新的服务器
//        lcEnv.addLocalCommand("xinxin", new LocalCommand() {
//
//            @Override
//            public Object dealWithCommand(DLocalCommandEnv env, String[] args, Context context) {
//                // @@#login 172.13.1.142 16888#@@
////                DModule.ModuleLogin.cast(LoginInterface.class).logout();
//                ThreadBus.bus().post(ThreadBus.Working, new Runnable() {
//	                @Override
//	                public void run() {
//		                NetClient.switchServerTo("58.215.183.168", "18501");
//
////		                LoginHelper.loginWithMacAddress();
//	                }
//                });
//                return true;
//            }
//        });
//
//        // 切换到新新的服务器
//        lcEnv.addLocalCommand("jerry", new LocalCommand() {
//
//            @Override
//            public Object dealWithCommand(DLocalCommandEnv env, String[] args, Context context) {
//                // @@#login 172.13.1.142 16888#@@
////                DModule.ModuleLogin.cast(LoginInterface.class).logout();
//                ThreadBus.bus().post(ThreadBus.Working, new Runnable() {
//	                @Override
//	                public void run() {
//		                NetClient.switchServerTo("172.19.146.112", "18501");
//
////		                LoginHelper.loginWithMacAddress();
//	                }
//                });
//                return true;
//            }
//        });
//
//        // 切换到指定的服务器登录
//        lcEnv.addLocalCommand("login", new LocalCommand() {
//
//            @Override
//            public Object dealWithCommand(DLocalCommandEnv env, final String[] args, Context context) {
//                if (args.length == 2 && args[1] != null) {
//                    // @@#login 172.13.1.142#@@
////                    DModule.ModuleLogin.cast(LoginInterface.class).logout();
//                    ThreadBus.bus().post(ThreadBus.Working, new Runnable() {
//	                    @Override
//	                    public void run() {
//		                    NetClient.switchServerTo(args[1], null);
//
////		                    LoginHelper.loginWithMacAddress();
//	                    }
//                    });
//                } else if (args.length == 3 && args[1] != null && args[2] != null) {
//                    // @@#login 172.13.1.142 16888#@@
////                    DModule.ModuleLogin.cast(LoginInterface.class).logout();
//
//                    ThreadBus.bus().post(ThreadBus.Working, new Runnable() {
//	                    @Override
//	                    public void run() {
//		                    NetClient.switchServerTo(args[1], args[2]);
//
////		                    LoginHelper.loginWithMacAddress();
//	                    }
//                    });
//                }
//
////	            if (context != null && context instanceof Activity) {
////		            MainActivity.jumpMain((Activity) context, 3);
////	            }
//                return true;
//            }
//        });
//
//        //上报友盟需要的注册测试设备的deviceId和mac地址
//        lcEnv.addLocalCommand("reportDebugInfo", new LocalCommand() {
//
//            @Override
//            public Object dealWithCommand(DLocalCommandEnv env, String[] args, Context context) {
//                // @@#reportDebugInfo#@@
//                DModule.ModuleAnalysis.cast(AnalysisInterface.class).reportDeviceIdAndMac();
//                GToast.show("report success thanks");
//                return true;
//            }
//        });
//
//        //模拟网络延时
//        lcEnv.addLocalCommand("net", new LocalCommand() {
//
//            @Override
//            public Object dealWithCommand(DLocalCommandEnv env, String[] args, Context context) {
//                // @@#reportDebugInfo#@@
//            	NetDataChannel.SimulateNetDelay = Long.parseLong(args[1]);
//                GToast.show("current ping :" + NetPing.np().ping());
//                return true;
//            }
//        });
//    }
}
