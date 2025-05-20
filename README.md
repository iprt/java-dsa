# Data Structures and Algorithms

> 数据结构与算法 Java 实现

## 排序

[冒泡排序](src/main/java/io/intellij/dsa/sort/impl/BubbleSort.java)

[插入排序](src/main/java/io/intellij/dsa/sort/impl/InsertSort.java)

[归并排序](src/main/java/io/intellij/dsa/sort/impl/MergeSort.java)

[快速排序](src/main/java/io/intellij/dsa/sort/impl/QuickSort.java)

## 查找

[堆的定义](src/main/java/io/intellij/dsa/tree/heap/Heap.java)

- [堆的实现](src/main/java/io/intellij/dsa/tree/heap/HeapImpl.java)

[基础二分搜索树](src/main/java/io/intellij/dsa/tree/bst/basic/BasicBST.java)

[AVL平衡树](src/main/java/io/intellij/dsa/tree/bst/avl/AvlTree.java)

- [旋转](src/main/java/io/intellij/dsa/tree/bst/avl/Rotate.java)

## 图

[图的定义](src/main/java/io/intellij/dsa/graph/Graph.java)

- [邻接矩阵，主要用于稠密图](src/main/java/io/intellij/dsa/graph/impl/DenseGraph.java)
- [邻接表，主要用于稀疏图](src/main/java/io/intellij/dsa/graph/impl/SparseGraph.java)

图中的算法

- [图的遍历](src/main/java/io/intellij/dsa/graph/algo/Traverse.java)
    - 深度遍历(`dfs`)
    - 广度遍历(`bfs`)
- [最小生成树](src/main/java/io/intellij/dsa/graph/algo/Mst.java)
- [无向图联通分量](src/main/java/io/intellij/dsa/graph/algo/Components.java)
- [单源最短路径](src/main/java/io/intellij/dsa/graph/algo/Dijkstra.java)
