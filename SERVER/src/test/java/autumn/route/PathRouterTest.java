package autumn.route;

import autumn.Request;
import autumn.Result;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;


/**
 * Created by infinitu on 14. 10. 31..
 */
public class PathRouterTest {

    @Test
    public void testMakingPathTree(){

        PathRouter.PathNode root = new PathRouter.PathNode(null);

        // it should create child nodes in degree 1.

        root.path("/aaa");
        root.path("/bbb");

        Assert.assertTrue(root.childNodes.size() == 2);
        Assert.assertTrue(root.childWildNode == null);

        // it should not create duplicated nodes
        root.path("/aaa");
        Assert.assertTrue(root.childNodes.size() == 2);


        // it should create whildcard child when input is in case "{~~}".

        root.path("/{aaa}");
        Assert.assertTrue(root.childWildNode != null);


        // it should create child nodes continuously

        root.path("/ccc").path("/ddd");

        Assert.assertTrue(
                root.childNodes.getOrDefault("ccc",new PathRouter.PathNode(null))
                    .childNodes.get("ddd")
                != null
        );

        // it should create all child nodes in degree over 2

        root.path("/eee/fff/ggg");

        Assert.assertTrue(
                root.childNodes.getOrDefault("eee",new PathRouter.PathNode(null))
                        .childNodes.getOrDefault("fff",new PathRouter.PathNode(null))
                        .childNodes.get("ggg")
                        != null
        );

    }

    @Test
    public void testMakingActionTree(){

        PathRouter.PathNode root = new PathRouter.PathNode(null);
        Method testMethod1=null,testMethod2=null;
        try {
            testMethod1 = this.getClass().getDeclaredMethod("dummyMethod_no_param");
            testMethod2 = this.getClass().getDeclaredMethod("dummyMethod_with_param", String.class, String.class);
        } catch (NoSuchMethodException e) {
            Assert.fail(e.getMessage());
        }

        // it should add action in route node.
        root.addAction("/aaa",testMethod1, PathRouter.REST_METHOD_ID_GET);

        Assert.assertTrue(
                ((PathRouter.PathNode) root.path("aaa")).actions[PathRouter.REST_METHOD_ID_GET]
                        == testMethod1
        );


        // it should call method and return result.
        try{
            Assert.assertTrue(
                    Result.class.isInstance(
                            root.doAct("/aaa", new Request(PathRouter.REST_METHOD_ID_GET))
                    )
            );
        }
        catch(Exception e){
            Assert.fail(e.getMessage());
        }

        // it should add action that has some parameters and can call that action and return result.
        root.addAction("/{a}/{b}/aaa",testMethod2, PathRouter.REST_METHOD_ID_POST);

        try{
            Assert.assertTrue(
                    Result.class.isInstance(
                            root.doAct(String.format("/{%s}/{%s}/aaa",testParam1,testParam2),
                                    new Request(PathRouter.REST_METHOD_ID_GET))
                    )
            );
        }
        catch(Exception e){
            Assert.fail(e.getMessage());
        }


        // it should recognize method parameter names.
        root.addAction("/{b}/{a}/aaa",testMethod2, PathRouter.REST_METHOD_ID_POST);

        try{
            Assert.assertTrue(
                    Result.class.isInstance(
                            root.doAct(String.format("/{%s}/{%s}/aaa",testParam2,testParam1),
                                    new Request(PathRouter.REST_METHOD_ID_GET))
                    )
            );
        }
        catch(Exception e){
            Assert.fail(e.getMessage());
        }

    }

    public static Result dummyMethod_no_param(){
        return new Result() {};
    }

//    @SuppressWarnings("FieldCanBeLocal")
    private String testParam1 = "param1";

//    @SuppressWarnings("FieldCanBeLocal")
    private String testParam2 = "param2";

    public static Result dummyMethod_with_param(String a, String b){
        Assert.assertEquals(a,"param1");
        Assert.assertEquals(a,"param2");
        return new Result() {};
    }

}
