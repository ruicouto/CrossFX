/*
 * RequestsHandler
 * ruicouto in 3/fev/2017
 */
package testfx.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import testfx.app.Adapter;

/**
 * The class which handles both the requests from the FX webview 
 * and from external webpages.
 * Changes on how to handle requests should be made here.
 * @author ruicouto
 */
public class FXRequestsHandler implements HttpHandler {

    /** The class which will process the requests */
    private Adapter adapter;

    /**
     * Keep a reference for the handler
     * @param adapter 
     */
    public FXRequestsHandler(Adapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Handle a request
     * @param t
     * @throws IOException 
     */
    @Override
    public void handle(HttpExchange t) throws IOException {
        Map<String, String> p = parseRequest(t.getRequestURI().getQuery());
        String response = "";
        String mn = p.get("method");

        Class c = adapter.getClass();
        Method[] methods = c.getDeclaredMethods();

        if (mn.equals("getMethods")) {
            int methodInd = 0;
            for (methodInd = 0; methodInd < methods.length - 1; methodInd++) {
                response += methods[methodInd].getName() + ",";
            }
            response += methods[methodInd].getName();

        } else {
            List<String> params = new ArrayList<>();
            for (Map.Entry<String, String> e : p.entrySet()) {
                if (e.getKey().startsWith("p")) {
                    params.add(e.getValue());
                }
            }

            try {
                for (Method m : methods) {
                    if (m.getName().equals(mn)) {
                        Object[] pars = new Object[params.size()];
                        int i = 0;
                        for (Class cls : m.getParameterTypes()) {
                            String tp = params.get(i);
                            System.out.println(cls.getTypeName());
                            if (cls.getTypeName().equals("int")) {
                                pars[i] = Integer.parseInt(tp);
                            } else {
                                pars[i] = tp;
                            }
                            i++;
                        }
                        response = m.invoke(adapter, pars).toString();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        //enable cross-origin HTTP requests
        t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        t.getResponseHeaders().set("Content-Type", "text/plain");

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    /**
     * Parse querystring parameters
     * @param request
     * @return 
     */
    private static Map<String, String> parseRequest(String request) {
        Map<String, String> r = new HashMap<>();
        String[] params = request.split("&");
        for (String s : params) {
            try {
                String k = s.split("=")[0];
                String v = s.split("=")[1];
                r.put(k, v);
            } catch (Exception e) {
            }
        }
        return r;
    }
}
