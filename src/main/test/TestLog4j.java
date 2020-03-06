import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public  class TestLog4j {
    private Logger logger = Logger.getLogger(SpringbootStudyLoggerApplicationTests.class);//创建Logger对象
    private long time;//记录时间

    //测试方法
    @Test
    public void show(){
        for(int i=0;i<100;i++)
            System.out.println(i+1);
        logger.info("循环一百遍");//写入到日志中
    }
    @Test
    public void print(){
        int num=0;
        for(int i=1;i<=1000;++i)
            num=num+i;
        System.out.println("1000以内的整数和："+num);
        logger.info("一千以内的整数和："+num);

    }
    //在每个方法执行前获得一次时间
    @Before
    public void before(){
        time=System.currentTimeMillis();//获得方法执行前的时间
    }
    //在每个方法执行后 获得一次时间，并作差  就得到了 方法运行的时间差
    @After
    public void after(){
        time=System.currentTimeMillis()-time;
        System.out.println("时间："+time);
        logger.info("时间："+time);
    }
}



