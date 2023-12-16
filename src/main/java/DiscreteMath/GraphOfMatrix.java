package DiscreteMath;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author 郑悦
 * @Description: 记录生成图
 * @date 2023/12/16 15:59
 */
public class GraphOfMatrix {
    private boolean isDirect;
    private int[][] Matrix;
    char[] arrayV;
    int length;
    static int INF = 1000000;
    boolean hasMinTree = false;

    public GraphOfMatrix(int length, boolean b) {
        Matrix = new int[length][length];
        this.length = length;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                Matrix[i][j] = INF;
            }
        }
    }

    public void initArray(char[] array) {
        arrayV = array;
    }

    public void addEdge(char a, char b, int i) {
        Matrix[a-'a'][b-'a'] = i;
        //如果是无向图邻接矩阵对称位置也要添加
        if (!isDirect){
            Matrix[b-'a'][a-'a'] = i;
        }
    }

    //按照下标将边加入到最小生成树中
    public void addEdgeUseIndex(int srcIndex,int destIndex,int weight){
        Matrix[srcIndex][destIndex] = weight;
        //如果是无向图邻接矩阵对称位置也要添加
        if (!isDirect){
            Matrix[destIndex][srcIndex] = weight;
        }
    }

    public int kruskal(GraphOfMatrix minTree) {
        //1.定义一个优先级队列
        PriorityQueue<Edge> minQ = new PriorityQueue<Edge>(new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                return o1.weight - o2.weight;
            }
        });
        int n = arrayV.length;
        //2.遍历领接矩阵,将所有的边都放入优先级队列中
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i < j && Matrix[i][j] != Integer.MIN_VALUE) {
                    minQ.offer(new Edge(i, j, Matrix[i][j]));
                }
            }
        }
        //3.构造并查集将符合要求的边加入到最小生成树中
        UnionFindSet ufs = new UnionFindSet(n);

        int size = 0;//记录最小生成树中边的数量
        int totalWeight = 0;//记录权值
        while (size < n - 1 && !minQ.isEmpty()) {
            Edge edge = minQ.poll();
            int srcIndex = edge.srcIndex;
            int destIndex = edge.destIndex;
            //同一边的相邻顶点不能来自同一集合
            if (!ufs.isSameUnionFindSet(srcIndex, destIndex)) {
                //将符合条件的边加入到最小生成树中
                minTree.addEdgeUseIndex(srcIndex, destIndex, Matrix[srcIndex][destIndex]);
                System.out.println("选择的边" + arrayV[srcIndex] + " -> " + arrayV[destIndex] + ":" + Matrix[srcIndex][destIndex]);
                size++;
                totalWeight += Matrix[srcIndex][destIndex];
                //将添加过的边的相邻顶点放入同一集合,防止出现环.
                ufs.union(srcIndex, destIndex);
            }
        }
        if (size == n - 1) {
            hasMinTree = true;
            return totalWeight;
        } else {
            hasMinTree = false;
//            return -1;
            throw new RuntimeException("没有最小生成树");
        }
    }

    public void printGraph() {
        for (int i = 0; i < length; i++) {
            for(int j = 0; j < length; j++) {
                System.out.print(Matrix[i][j] + " ");
            }
            System.out.print('\n');
        }
    }

    public int[] From;
    public int[] To;
    public void getLineFromTo(List<Circle> circles) {
        From = new int[length];
        To = new int[length];
        PriorityQueue<Edge> minQ = new PriorityQueue<Edge>(new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                return o1.weight - o2.weight;
            }
        });
        int n = arrayV.length;
        //2.遍历领接矩阵,将所有的边都放入优先级队列中
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i < j && Matrix[i][j] != Integer.MIN_VALUE) {
                    minQ.offer(new Edge(i, j, Matrix[i][j]));
                }
            }
        }
        //3.构造并查集将符合要求的边加入到最小生成树中
        UnionFindSet ufs = new UnionFindSet(n);

        int size = 0;//记录最小生成树中边的数量
        while (size < n - 1 && !minQ.isEmpty()) {
            Edge edge = minQ.poll();
            int srcIndex = edge.srcIndex;
            int destIndex = edge.destIndex;
            //同一边的相邻顶点不能来自同一集合
            if (!ufs.isSameUnionFindSet(srcIndex, destIndex)) {
                From[size] = srcIndex;
                To[size] = destIndex;
                size++;
                //将添加过的边的相邻顶点放入同一集合,防止出现环.
                ufs.union(srcIndex, destIndex);
            }
        }
    }
}
