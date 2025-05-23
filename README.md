# Data Structures and Algorithms

> 数据结构与算法 Java 实现

## 排序

[冒泡排序](src/main/java/io/intellij/dsa/sort/impl/BubbleSort.java)

[插入排序](src/main/java/io/intellij/dsa/sort/impl/InsertSort.java)

[归并排序](src/main/java/io/intellij/dsa/sort/impl/MergeSort.java)

[快速排序](src/main/java/io/intellij/dsa/sort/impl/QuickSort.java)

## 查找

[并查集定义](src/main/java/io/intellij/dsa/uf/UnionFind.java)：路径压缩，快速取并

- [基于索引的并查集](src/main/java/io/intellij/dsa/uf/IndexedUnionFind.java)
- [基于树的并查集](src/main/java/io/intellij/dsa/uf/TreeUnionFind.java)

[堆的定义](src/main/java/io/intellij/dsa/tree/heap/Heap.java)

- [堆的实现](src/main/java/io/intellij/dsa/tree/heap/HeapImpl.java)

[基础二分搜索树](src/main/java/io/intellij/dsa/tree/bst/basic/BasicBST.java)

[AVL平衡树](src/main/java/io/intellij/dsa/tree/bst/avl/AvlTree.java)

- [旋转](src/main/java/io/intellij/dsa/tree/bst/avl/Rotate.java)
-

## 图

[图的定义](src/main/java/io/intellij/dsa/graph/Graph.java)

- [稠密图 邻接矩阵](src/main/java/io/intellij/dsa/graph/impl/DenseGraph.java)
- [稀疏图 邻接表](src/main/java/io/intellij/dsa/graph/impl/SparseGraph.java)

[图中的算法](src/main/java/io/intellij/dsa/graph/compute)

- [图的遍历](src/main/java/io/intellij/dsa/graph/compute/Traverse.java)
    - 深度遍历(`dfs`)
    - 广度遍历(`bfs`)
- [最小生成树](src/main/java/io/intellij/dsa/graph/compute/Mst.java)
    - `LazyPrim`算法：深度遍历 + 切分
    - `Kruskal`算法：最小堆 + 并查集 + 切分
- [无向图联通分量](src/main/java/io/intellij/dsa/graph/compute/Components.java)
- [单源最短路径](src/main/java/io/intellij/dsa/graph/compute/Dijkstra.java)
    - 思路：局部最优更新到全局最优

- 环分析: 节点标记 + 深度遍历 + 递归回溯
    - [无向图寻找所有环](src/main/java/io/intellij/dsa/graph/compute/UndirectedCycles.java)
    - [有向图寻找所有环](src/main/java/io/intellij/dsa/graph/compute/DirectedCycles.java)
