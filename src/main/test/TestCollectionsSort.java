import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Created By Zzyy
 **/
public class TestCollectionsSort {
    public static void main(String[] args) {


        List<Integer> list = new ArrayList<>();

        //用户输入10个整数
        System.out.println("请输入10个整数：");
        for(int i = 0; i < 10; i++)
        {
            list.add((int)(Math.random() * 20) + 1);
        }

        //输出排序前结果
        System.out.println(list);

        //逆序排序
        Collections.sort(list,Collections.reverseOrder());
        //顺序排序
        Collections.sort(list);

        //输出排序结果
        System.out.println(list);
    }
}
