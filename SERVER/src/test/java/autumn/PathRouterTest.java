package autumn;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by infinitu on 14. 10. 31..
 */
public class PathRouterTest {

    @Test
    public void testMakingPathTree(){

        // it should create child nodes in degree 1.
        PathRouter.PathNode root = new PathRouter.PathNode(null);

        root.addPath("/aaa");
        root.addPath("/bbb");

        Assert.assertTrue(root.childNodes.size() == 2);
        Assert.assertTrue(root.childWildNode == null);

        // it should not create duplicated nodes
        root.addPath("/aaa");
        Assert.assertTrue(root.childNodes.size() == 2);


        // it should create whildcard child when input is in case "{~~}".

        root.addPath("/{aaa}");
        Assert.assertTrue(root.childWildNode != null);


        // it should create child nodes continuously

        root.addPath("/ccc").addPath("/ddd");

        Assert.assertTrue(
                root.childNodes.getOrDefault("ccc",new PathRouter.PathNode(null))
                    .childNodes.get("ddd")
                != null
        );

        // it should create all child nodes in degree over 2

        root.addPath("/eee/fff/ggg");

        Assert.assertTrue(
                root.childNodes.getOrDefault("eee",new PathRouter.PathNode(null))
                        .childNodes.getOrDefault("fff",new PathRouter.PathNode(null))
                        .childNodes.get("ggg")
                        != null
        );

    }


    @Test
    public void testMakingActionTree(){
        
    }

}
