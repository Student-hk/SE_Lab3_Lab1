import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;


class AdjacentEdge{
    /*有向图的邻接边
     *属性：邻接边的尾顶点的检索值，邻接表的权重，下一条邻接边的引用
     */
    public int edgeTailIndex=0;
    public int weight=0;
    public AdjacentEdge next;
}

class Vertex{
    /*有向图的顶点
     *属性：顶点单词，出度边的数目，下一条边的引用
     */
    public String word;
    public int edgeNum=0;
    public AdjacentEdge next;
}


class Graph {
    /*有向图
     *属性:有向图的顶点数组Vertex[] vertexArray
     *     顶点个数int vertexNum
     *方法：有向图的构造Graph(String[] wordSet, String[] wordArray)
     *      桥接词的搜索String[] bridgeWord(String word1, String word2)
     *      根据桥接词生成新文本String generateText(String text)
     *      两个单词的最短路径int[][] shortestPath(String word1,String word2)
     *      随机路径String randomGraph()
     */
    public Vertex[] vertexArray;
    int vertexNum;

    public Graph(String[] wordSet, String[] wordArray) {
        /*有向图的构造
         *形式参数：单词的集合数组，单词原来次序数组
         */
        vertexNum = wordSet.length;
        vertexArray = new Vertex[vertexNum];
        for (int i = 0; i < wordSet.length; i++) {
            vertexArray[i] = new Vertex();
            vertexArray[i].word = wordSet[i];
        }
        int vertexIndex = 0;
        int nextVertexIndex = 0;
        AdjacentEdge tmpEdge;
        for (int i = 0; i < wordArray.length - 1; i++) {
            //寻找邻接边的两个顶点信息
            for (int j = 0; j < wordSet.length; j++) {
                if (wordSet[j].equals(wordArray[i])) {
                    vertexIndex = j;
                    vertexArray[j].edgeNum++;
                    break;
                }
            }
            for (int j = 0; j < wordSet.length; j++) {
                if (wordSet[j].equals(wordArray[i + 1])) {
                    nextVertexIndex = j;
                    break;
                }
            }
            //添加邻接边
            AdjacentEdge curEdge;
            if (vertexArray[vertexIndex].next == null) {
                tmpEdge = new AdjacentEdge();
                tmpEdge.edgeTailIndex = nextVertexIndex;
                tmpEdge.weight++;
                vertexArray[vertexIndex].next = tmpEdge;
            } else {
                curEdge = vertexArray[vertexIndex].next;
                while (true) {
                    if (curEdge.edgeTailIndex == nextVertexIndex) {
                        vertexArray[vertexIndex].edgeNum--;
                        curEdge.weight++;
                        break;
                    } else {
                        if (curEdge.next == null) {
                            tmpEdge = new AdjacentEdge();
                            tmpEdge.edgeTailIndex = nextVertexIndex;
                            tmpEdge.weight++;
                            curEdge.next = tmpEdge;
                            break;
                        } else {
                            curEdge = curEdge.next;
                        }
                    }
                }
            }
        }
    }

    public String[] bridgeWord(String word1, String word2) {
        /*桥接词的搜索
         *形式参数：单词1，单词2
         * 返回：桥接词的数组
         */
        int word1Index = -1, word2Index = -1;
        String[] bridgeWords = null;
        //查找是否存在单词1和单词2
        for (int i = 0; i < vertexNum; i++) {
            if (vertexArray[i].word.equals(word1)) {
                word1Index = i;
            } else if (vertexArray[i].word.equals(word2)) {
                word2Index = i;
            } else if (word1Index < 0 || word2Index < 0) {
                continue;
            } else {
                break;
            }
        }
        if (word1Index < 0 || word2Index < 0) {
            //不存在返回null
            return bridgeWords;
        }
        bridgeWords = new String[vertexNum];
        AdjacentEdge bridgeEdge1 = vertexArray[word1Index].next, bridgeEdge2;
        int tmpBridge;
        int bridgeWordNum = 0;
        //查找桥接词
        while (bridgeEdge1 != null) {
            tmpBridge = bridgeEdge1.edgeTailIndex;
            bridgeEdge2 = vertexArray[tmpBridge].next;
            while (bridgeEdge2 != null) {
                if (bridgeEdge2.edgeTailIndex == word2Index) {
                    bridgeWords[bridgeWordNum++] = vertexArray[bridgeEdge1.edgeTailIndex].word;
                    break;
                } else {
                    bridgeEdge2 = bridgeEdge2.next;
                }
            }
            bridgeEdge1 = bridgeEdge1.next;
        }
        bridgeWords[bridgeWordNum] = null;
        return bridgeWords;
    }

    public String generateText(String text){
        /*生成新文本
         *形式参数：用户输入的String文本
         * 返回：新文本的String
         */
        String SEPARATOR=" |,|!|\\.|\\?|:|;|-|@";
        String[] textWords = text.split(SEPARATOR);
        String[] bridgeWords;
        int bridgeWordsNum;
        StringBuilder result = new StringBuilder();
        for(int i=0;i<textWords.length-1;i++){
            result.append(textWords[i]+" ");
            //两个单词的桥接词查询
            bridgeWords=bridgeWord(textWords[i],textWords[i+1]);
            if(bridgeWords==null){
                continue;
            }
            else{
                if(bridgeWords[0]==null){
                    continue;
                }
                bridgeWordsNum=0;
                for(String str:bridgeWords){
                    if(str!=null){
                        bridgeWordsNum++;
                        continue;
                    }
                    break;
                }
                //随机选取桥接词进行添加
                result.append(bridgeWords[RandomNum.randomNum(1,bridgeWordsNum)-1]+" ");
            }
        }
        result.append(textWords[textWords.length-1]+".");
        return result.toString();
    }

    public int[][] shortestPath(String word1,String word2){
        /*求两个单词的最短路径
         *形式参数：单词1，单词2
         * 返回：Dijkstra中的Dist[]（距离数组）和preNode[]（前顶点数组）两个数组
         * 利用Dijkstra算法进行最短路径的求解
         */
        int MAX= 32676;
        int word1Index = -1, word2Index = -1;
        //检索单词1和单词2
        for (int i = 0; i < vertexNum; i++) {
            if (vertexArray[i].word.equals(word1)) {
                word1Index = i;
            } else if (vertexArray[i].word.equals(word2)) {
                word2Index = i;
            } else if (word1Index < 0 || word2Index < 0) {
                continue;
            } else {
                break;
            }
        }
        if(word1Index<0||word2Index<0){
            return null;
        }
        //利用最小优先队列进行优化
        Queue<VertexDist> distQueue=new PriorityQueue<>(vertexNum,VertexDist.distComparator);
        int[] dist= new int[vertexNum];
        int[] preNode = new int[vertexNum];
        boolean[] inSet = new boolean[vertexNum];
        for(int i=0;i<vertexNum;i++){
            dist[i]=MAX;
            inSet[i]=false;
        }
        dist[word1Index]=0;
        AdjacentEdge curEdge;
        int curVertex=word1Index;
        for(int i=1;i<vertexNum;i++){
            if(distQueue.peek()!=null && !inSet[distQueue.peek().index]){
                curVertex=distQueue.poll().index;
            }
            curEdge=vertexArray[curVertex].next;
            inSet[curVertex]=true;
            while(curEdge!=null){
                if(!inSet[curEdge.edgeTailIndex] && dist[curVertex]+curEdge.weight<dist[curEdge.edgeTailIndex]){
                    dist[curEdge.edgeTailIndex]=dist[curVertex]+curEdge.weight;
                    preNode[curEdge.edgeTailIndex]=curVertex;
                    distQueue.add(new VertexDist(curEdge.edgeTailIndex,dist[curEdge.edgeTailIndex]));
                }
                curEdge=curEdge.next;
            }
        }
        //Dist[]和preNode[]合成一个二维数组
        int[][] rst=new int[2][vertexNum];
        for(int i=0;i<vertexNum;i++){
            rst[0][i]=dist[i];
            rst[1][i]=preNode[i];
        }
        return rst;
    }

    public String randomGraph(){
        /*随机路径
         *返回：最终完全的随机路径String
         */
        Scanner in=new Scanner(System.in);
        String input;
        do{
            System.out.println("输入 's' 开始");
            input = in.next();
        } while(!input.equals("s"));

        StringBuilder rst = new StringBuilder();
        int curIndex=RandomNum.randomNum(0,vertexNum-1);
        int nextIndex,randomEdge;
        AdjacentEdge curEdge;
        boolean[][] travelEdge=new boolean[vertexNum][vertexNum];
        while(true){
            rst.append(vertexArray[curIndex].word+" ");
            curEdge=vertexArray[curIndex].next;
            System.out.println(rst.toString());
            if(curEdge==null){
                break;
            }

            //随机路径生成时的控制
            if(!input.equals("f")){
                System.out.println("输入 'n' 来得到下一个单词 or 输入 'f' 来自动完成随机路径 or 输入 'c' 来取消操作");
                do{
                    input = in.next();
                }while(!input.equals("n") && !input.equals("f") && !input.equals("c"));
                if (input.equals("c")){
                    break;
                }
            }
            randomEdge=RandomNum.randomNum(1,vertexArray[curIndex].edgeNum);
            for(int i=1;i<randomEdge;i++){
                curEdge=curEdge.next;
            }
            nextIndex=curEdge.edgeTailIndex;
            if(travelEdge[curIndex][nextIndex]){
                //rst.append(vertexArray[curIndex].word+" ");
                rst.append(vertexArray[nextIndex].word);
                System.out.println(rst.toString());
                break;
            }
            travelEdge[curIndex][nextIndex]=true;
            curIndex=nextIndex;
        }
        System.out.println("\n随机行走已经结束");
        return rst.toString();
    }
}
