package DiscreteMath;

import java.util.Arrays;

public class UnionFindSet {
    public int[] elem;

    public UnionFindSet(int n){
        this.elem = new int[n];
        Arrays.fill(elem,-1);
    }

    /**
     * 查找数据x的根节点
     * @param x
     * @return
     */
    public int findRoot(int x){
        if (x < 0){
            throw new RuntimeException("下表不合法");
        }
        while (elem[x] >= 0){
            x = elem[x];
        }
        return x;
    }
    /**
     * 查询x1和x2是不是同一个集合
     * @param x1
     * @param x2
     * @return
     */
    public boolean isSameUnionFindSet(int x1 , int x2){
        int index1 = findRoot(x1);
        int index2 = findRoot(x2);
        if (index1 == index2){
            return true;
        }
        return false;
    }

    /**
     * 这是合并操作
     * @param x1
     * @param x2
     */
    public void union(int x1 , int x2){
        int index1 = findRoot(x1);
        int index2 = findRoot(x2);
        if (index1 == index2) return;
        elem[index1] = elem[index1] + elem[index2];
        elem[index2] = index1;
    }

    /**
     * 有几对关系
     * @return
     */
    public int getCount(){
        int count = 0;
        for (int x:elem) {
            if (x < 0){
                count++;
            }
        }
        return count;
    }
    public void Print(){
        for (int x:elem){
            System.out.print(x+" ");
        }
        System.out.println();
    }
}