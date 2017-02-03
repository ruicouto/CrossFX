/*
 * Adapter
 * ruicouto in 20/jan/2017
 */
package testfx.app;

/**
 * The class which implements the methods available for the javascript in
 * both the webview and external pages.
 * @author ruicouto
 */
public class Adapter {
    
    public String salute(String name) {
        System.out.println("In java");
        return "Hello, "+name+"!";
    }
    
    public int sum(int a, int b) {
        System.out.println("In java");
        return a+b;
    }
    
    public String toUpper(String src) {
        System.out.println("In java");
        return src.toUpperCase();
    }
    
    public String source() {
        return "JavaFX app";
    }
    
    
    public int mult(int a, int b) {
        return a*b;
    }
    
    
}
