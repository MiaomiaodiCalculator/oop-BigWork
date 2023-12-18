package DiscreteMath;

import java.util.Arrays;
/**
 * @author 郑悦
 * @Description: 并查集创建
 * @date 2023/12/16 15:59
 */
public class UnionFindSet {
    public int[] elem;
    /**
     * @Description 构造并查集
     * @param n
     * @return null
     * @author 郑悦
     * @date 2023/12/18 22:09
    **/
    public UnionFindSet(int n){
        this.elem = new int[n];
        Arrays.fill(elem,-1);
    }

    /**
     * @Description 查询根节点
     * @param x
     * @return int
     * @author 郑悦
     * @date 2023/12/18 22:10
    **/
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
     * @Description 查询
     * @param x1
     * @param x2
     * @return boolean
     * @author 郑悦
     * @date 2023/12/16 22:09
    **/
    public boolean isSameUnionFindSet(int x1 , int x2){
        int index1 = findRoot(x1);
        int index2 = findRoot(x2);
        if (index1 == index2){
            return true;
        }
        return false;
    }

    /**
     * @Description 合并
     * @param x1
     * @param x2
     * @author 郑悦
     * @date 2023/12/16 22:09
    **/
    public void union(int x1 , int x2){
        int index1 = findRoot(x1);
        int index2 = findRoot(x2);
        if (index1 == index2) return;
        elem[index1] = elem[index1] + elem[index2];
        elem[index2] = index1;
    }
}