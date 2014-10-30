package autumn;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by infinitu on 14. 10. 31..
 */
public class PathRouter {

    PathNode root = new PathNode();


    public interface PathAllocator{
        public PathAllocator addPath(String Path);
        public PathAllocator addAction(String pathAction, Method method);
    }
    protected static class PathNode implements PathAllocator{
        String nodeName;
        Map<String,PathNode> childNodes;
        PathNode childWildNode;

        protected PathNode(String node){
            this.nodeName = node;
            this.childNodes = new HashMap<>();
            this.childWildNode = null;
        }

        boolean invokeAction(Iterator<String> path, Iterator<String> Param) {
        }

        @Override
        public PathAllocator addPath(String Path) {
            return null;
        }

        @Override
        public PathAllocator addAction(String pathAction, Method method) {
            return null;
        }
    }
}
