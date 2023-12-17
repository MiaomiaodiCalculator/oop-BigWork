package DiscreteMath;

/**
 * @author 郑悦
 * @Description:
 * @date 2023/12/16 15:57
 */
public class Edge {
    public int srcIndex;
    public int destIndex;
    int weight;

    public Edge(int i, int j, int w) {
        srcIndex = i;
        destIndex = j;
        weight = w;
    }
}
