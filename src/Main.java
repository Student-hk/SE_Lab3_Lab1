import java.awt.*;
import java.util.Scanner;

public class Main {
    private Graph graph;
    private String runPath;
    private String fileName;
    public static void main(String[] args) throws InterruptedException {
        Main proG=new Main();
        String sourceText=ReadFromFile.readFileByChars(args[0]);
        //"D:\WorkSpace\Project\Java\SE_Lab1\out\production\SE_Lab1\test.txt"

        proG.graph=proG.createDirectedGraph(sourceText);
        System.out.println("----------------文本转换为有向图-------------------");
        Scanner in=new Scanner(System.in);
        System.out.println("请输入图片保存的目录路径");
        proG.runPath=in.next();//D:\TestDel
        System.out.println("请输入保存的文件名");
        proG.fileName=in.next();
        //proG.showDirectedGraph(proG.graph);
        proG.userCommand();
    }

    public Graph createDirectedGraph(String text){
        return StringToGraph.getGraph(text);
    }
    public void showDirectedGraph(Graph G) throws InterruptedException {
        Dirgraph gv = new Dirgraph(G,runPath,fileName);
        gv.genaPic();
        Thread.sleep(500);
        PicView.run(runPath,fileName);
    }
    public String queryBridgeWords(Graph G,String word1,String word2){
        String[] result=G.bridgeWord(word1,word2);
        if(result==null){
            return "No "+word1+" or "+word2+" in the graph!";
        }
        else if(result[0]==null){
            return "No bridge words from "+word1+" to "+word2+"!";
        }
        else{
            int bridgeWordNum=0;
            for(String str: result){
                if(str!=null){
                    bridgeWordNum++;
                    continue;
                }
                break;
            }
            StringBuilder rst=new StringBuilder();
            rst.append("The bridge words from "+word1+" to "+word2+" are: ");
            if(bridgeWordNum==1) {
                rst.append(result[0] + '.');
            }
            else{
                for(int i=0;i<bridgeWordNum-1;i++){
                    rst.append(result[i]+", ");
                }
             rst.append("and "+result[bridgeWordNum-1]+'.');
            }
                return rst.toString();
        }
    }

    public String generateNewText(Graph G,String inputText){
        return G.generateText(inputText);
    }

    public String calcShortestPath(Graph G,String word1,String word2) throws InterruptedException {
        int MAX= 32676;
        int[][] rst=graph.shortestPath(word1,word2);
        if(rst==null){
            return "no "+word1+" or no "+word2+" in the graph";
        }
        int word1Index = -1, word2Index = -1;
        for (int i = 0; i < graph.vertexNum; i++) {
            if (graph.vertexArray[i].word.equals(word1)) {
                word1Index = i;
            } else if (graph.vertexArray[i].word.equals(word2)) {
                word2Index = i;
            } else if (word1Index < 0 || word2Index < 0) {
                continue;
            } else {
                break;
            }
        }
        if(rst[0][word2Index]==MAX){
            return "no path from "+word1+" to "+word2;
        }
        int curIndex=word2Index;
        String pathString=graph.vertexArray[curIndex].word;
        for(int i=0;i<graph.vertexNum;i++){
            pathString=graph.vertexArray[rst[1][curIndex]].word+"-->"+pathString;
            if(rst[1][curIndex]!=word1Index){
                curIndex=rst[1][curIndex];
                continue;
            }
            break;
        }
        String result="The path from "+word1+" to "+word2+": "+pathString;

        Dirgraph gv = new Dirgraph(G,runPath,word1+"ShortestPathTo"+word2);
        gv.shortestPath(word1,word2);
        Thread.sleep(500);
        PicView.run(runPath,word1+"ShortestPathTo"+word2);
        return result;
    }
    public String randomWalk(Graph G){
        return graph.randomGraph();
    }


    public void shortestPath(Graph G,String word) throws InterruptedException {
        Scanner in=new Scanner(System.in);
        int wordIndex=-1;
        for(int i=0;i<graph.vertexNum;i++){
            if(word.equals(graph.vertexArray[i].word)){
                wordIndex=i;
                break;
            }
        }
        if(wordIndex<0){
            System.out.println("no "+word+" in the graph.");
            return;
        }
        String tmp;
        for(int i=0;i<graph.vertexNum;i++){
            if(i!=wordIndex){
                do {
                    System.out.println("请输入‘n'来得到下一条最短路径 or 输入'c'来撤销操作");
                    tmp = in.next();
                }while(!tmp.equals("n") && !tmp.equals("c"));
            if(tmp.equals("c")) {
                break;
            }
                System.out.println(calcShortestPath(graph,word,graph.vertexArray[i].word));
            }
        }
    }

    public void userCommand() throws InterruptedException {
        Scanner in = new Scanner(System.in);
        String enter;
        String word1,word2;
        String Text;
        do{
            System.out.println("\n*******请选择功能（输入选项的字母序号即可）*******");
            System.out.println("a.展示文本生成的有向图");
            System.out.println("b.查询桥接词");
            System.out.println("c.根据bridge word生成新文本");
            System.out.println("d.计算word1和word2之间的最短路径");
            System.out.println("e.随机游走");
            System.out.println("f.计算一个word与所有其他单词最短的路径");
            System.out.println("q.退出");
            System.out.println("**************************************************\n");
            enter=in.next();
            if(enter.equals("a")){
                showDirectedGraph(graph);
            }
            else if(enter.equals("b")){
                System.out.println("请依次输入word1和word2，用回车间隔");
                word1=in.next();
                word2=in.next();
                System.out.println(queryBridgeWords(graph,word1,word2));
            }
            else if(enter.equals("c")){
                System.out.println("请输入你的文本");
                in.nextLine();
                Text=in.nextLine();
                System.out.println(generateNewText(graph,Text));
            }
            else if(enter.equals("d")){
                System.out.println("请依次输入word1和word2，用回车间隔");
                word1=in.next();
                word2=in.next();
                System.out.println(calcShortestPath(graph,word1,word2));
            }
            else if(enter.equals("e")){
                System.out.println("随机游走");
                randomWalk(graph);
            }
            else if(enter.equals("f")){
                System.out.println("请输入word1");
                word1=in.next();
                shortestPath(graph,word1);
            }
            else if(enter.equals("q")){
                System.out.println("程序已经退出");
                System.exit(0);
            }
            else{
                System.out.println("请输入许可的字母序号");
            }
        } while(true);
    }
}