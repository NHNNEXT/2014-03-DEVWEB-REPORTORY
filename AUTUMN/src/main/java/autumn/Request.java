package autumn;

import autumn.route.PathRouter;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by infinitu on 14. 11. 2..
 */
public class Request {

    public int method;
    private String path;

    public Request(HttpServletRequest req){
        switch (req.getMethod()){
            case "GET":
                this.method= PathRouter.REST_METHOD_ID_GET;
            case "POST":
                this.method= PathRouter.REST_METHOD_ID_POST;
            case "PUT":
                this.method= PathRouter.REST_METHOD_ID_PUT;
            case "DELETE":
                this.method= PathRouter.REST_METHOD_ID_DELETE;
            case "UPDATE":
                this.method= PathRouter.REST_METHOD_ID_UPDATE;
        }
        this.path = req.getContextPath()+req.getPathInfo();
    }

    public Request(int method, String path) {
        this.method = method;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public int getMethod() {
        return method;
    }
}
