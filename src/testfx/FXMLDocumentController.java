/*
 * FXMLDocumentController
 * ruicouto in 12/jan/2017
 */
package testfx;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import testfx.app.Adapter;
import testfx.web.FXRequestsHandler;


/**
 *
 * @author ruicouto
 */
public class FXMLDocumentController implements Initializable {
    
    private static final boolean debug = true;
    @FXML private WebView container;
    private static Adapter adapter = new Adapter();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        container.getEngine().setJavaScriptEnabled(true);
        container.getEngine().load(getClass().getResource("/testfx/view/base.html").toExternalForm());           
        container.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
          @Override
          public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
              System.out.println(newState);
              if (newState == State.SUCCEEDED) {
                    JSObject win = (JSObject) container.getEngine().executeScript("window");
                    win.setMember("app", FXMLDocumentController.this);
                    win.setMember("adapter", adapter);
                    //enable firebug if in debug mode
                    if(debug) {
                        container.getEngine().executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}"); 
                    }
              }
          }
        });
        
        startServer();
    }
    
    
    /**
     * Start the webserver to handle external requests
     */
    public void startServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/FXStub", new FXRequestsHandler(adapter));
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
       
    /**
     * Invoke javascript from java
     * @param id
     * @param value 
     */
    public void setVal(String id, String value) {
        //invoke javacript
        container.getEngine().executeScript("setVal('"+id+"','"+value+"')");
    }   
}
