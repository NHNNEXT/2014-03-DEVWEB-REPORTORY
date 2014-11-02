package autumn.route;

import autumn.Request;
import autumn.Result;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by infinitu on 14. 10. 31..
 */
public class PathRouter {

    protected static final int REST_METHOD_ID_GET = 0;
    protected static final int REST_METHOD_ID_POST = 1;
    protected static final int REST_METHOD_ID_PUT = 2;
    protected static final int REST_METHOD_ID_DELETE = 3;
    protected static final int REST_METHOD_ID_UPDATE = 4;
    protected static final int REST_METHOD_ID_OPTION = 5;
    protected static final int REST_METHOD_ID_INFO = 6;
    protected static final int REST_METHOD_ID_HEAD = 7;
    protected static final int REST_METHOD_ID_CUSTUM = -1; //todo 커스텀 메서드 대응.

    PathNode root = new PathNode(null);


    public interface PathAllocator{
        public PathAllocator path(String Path);
        public PathAllocator addAction(String actionPath, Method method, int RESTMethod);
        public Result doAct(String actionPath, Request req);
    }
    static class PathNode implements PathAllocator{
        String nodeName;
        Map<String,PathNode> childNodes;
        PathNode childWildNode;
        Method[] actions = {null,null,null,null,null,null,null,null};

        protected PathNode(String node){
            this.nodeName = node;
            this.childNodes = new HashMap<>();
            this.childWildNode = null;
        }

        private boolean invokeAction(Iterator<String> path, Iterator<String> Param) {
            return false;
        }

        @Override
        public PathAllocator path(String Path) {
            return null;
        }

        @Override
        public PathAllocator addAction(String pathAction, Method method, int RESTMethod) {
            return null;
        }

        @Override
        public Result doAct(String actionPath, Request req) {
            return null;
        }
    }
}
