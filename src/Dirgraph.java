import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class Dirgraph {
    /*有向图的类
     *属性：有向图Graph graphInfo
     *      文件保存路径String savepath
     *      文件保存名称String fileName
     *方法：构造Dirgraph(Graph graph,String path,String name)
     *      生成并且保存图片void genaPic()
     */
    public Graph graphInfo;
    public String savepath;
    public String fileName;
    public Dirgraph(Graph graph,String path,String name){
        /*
         *形式参数：图，文件保存路径，文件保存名称
         */
        graphInfo=graph;
        savepath=path;
        fileName=name;
    }
    public void genaPic(){
        Graphviz gviz=new Graphviz(savepath,fileName);
        AdjacentEdge curEdge;
        StringBuilder line;
        gviz.startGraph();
        for(int i=0;i<graphInfo.vertexNum;i++){
            curEdge=graphInfo.vertexArray[i].next;
            for(int j=0;j<graphInfo.vertexArray[i].edgeNum;j++){
                line=new StringBuilder();
                line.append(graphInfo.vertexArray[i].word+"->"+graphInfo.vertexArray[curEdge.edgeTailIndex].word);
                line.append(" [label=\""+curEdge.weight+"\"]");
                gviz.addLn(line.toString());
                curEdge=curEdge.next;
            }
        }
        gviz.endGraph();
        try{
            gviz.run();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void shortestPath(String word1,String word2){
        int MAX= 32676;
        int[][] pathInfo=graphInfo.shortestPath(word1,word2);
        if(pathInfo==null){
            //System.out.println("no word1 or word2 in the graph");
            return;
        }
        int word1Index = -1, word2Index = -1;
        for (int i = 0; i < graphInfo.vertexNum; i++) {
            if (graphInfo.vertexArray[i].word.equals(word1)) {
                word1Index = i;
            } else if (graphInfo.vertexArray[i].word.equals(word2)) {
                word2Index = i;
            } else if (word1Index < 0 || word2Index < 0) {
                continue;
            } else {
                break;
            }
        }
        if(pathInfo[0][word2Index]==MAX){
            //System.out.println("no path from word1 to word2");
            return;
        }
        boolean[][] edgeFlag=new boolean[graphInfo.vertexNum][graphInfo.vertexNum];
        int[] preNode=pathInfo[1];

        Graphviz gviz=new Graphviz(savepath,fileName);

        AdjacentEdge curEdge;
        StringBuilder line;
        int curIndex=word2Index;
        int weight=-1;
        gviz.startGraph();
        for(int i=0;i<preNode.length;i++){
            line=new StringBuilder();
            line.append(graphInfo.vertexArray[preNode[curIndex]].word+"->"+graphInfo.vertexArray[curIndex].word);
            curEdge=graphInfo.vertexArray[preNode[curIndex]].next;
            for(int j=0;j<graphInfo.vertexArray[preNode[curIndex]].edgeNum;j++){
                if(curEdge.edgeTailIndex==curIndex){
                    weight=curEdge.weight;
                    break;
                }
                curEdge=curEdge.next;
            }
            line.append(" [label=\""+weight+"\",color=\"red\"]");
            gviz.addLn(line.toString());
            edgeFlag[preNode[curIndex]][curIndex]=true;
            curIndex=preNode[curIndex];
            if(curIndex==word1Index){
                break;
            }
        }

        for(int i=0;i<graphInfo.vertexNum;i++){
            curEdge=graphInfo.vertexArray[i].next;
            for(int j=0;j<graphInfo.vertexArray[i].edgeNum;j++){
                if(!edgeFlag[i][curEdge.edgeTailIndex]){
                    line=new StringBuilder();
                    line.append(graphInfo.vertexArray[i].word+"->"+graphInfo.vertexArray[curEdge.edgeTailIndex].word);
                    line.append(" [label=\""+curEdge.weight+"\"]");
                    gviz.addLn(line.toString());
                }
                curEdge=curEdge.next;
            }
        }
        gviz.endGraph();
        try{
            gviz.run();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}


class  Graphviz{
    private String runPath = "";    //"D:\\TestDel"

    //  dot程序的路径
    private String dotPath = "D:\\WorkSpace\\SoftWare\\Graphviz2.38\\bin\\dot.exe";

    private String runCmd="";
    private String gvFile="";
    private String genaPic="";
    private StringBuilder gvText = new StringBuilder();

    Runtime runtime=Runtime.getRuntime();

    public Graphviz(String path,String fileName) {
        this.runPath=path;
        this.gvFile=fileName;
        this.genaPic=fileName;
    }

    public void genaCmd(){
        runCmd+=dotPath+" ";
        runCmd+=runPath;
        runCmd+="\\"+gvFile+".txt ";
        runCmd+="-T png ";
        runCmd+="-o ";
        runCmd+=runPath;
        runCmd+="\\"+genaPic+".png";
        //System.out.println(runCmd);
    }

    public void run() {
        File file=new File(runPath);
        if(!file.exists())
        {
            file.mkdirs();
        }
        writeGraphToFile(gvText.toString(), runPath);
        genaCmd();
        try {
            runtime.exec(runCmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeGraphToFile(String dotText, String dirName) {
        try {
            File file = new File(dirName+"\\"+gvFile+".txt");
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(dotText.getBytes());
            fos.close();
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void add(String line) {
        gvText.append("\t"+line);
    }

    public void addLn(String line) {
        gvText.append("\t"+line + "\n");
    }

    public void addLn() {
        gvText.append('\n');
    }

    public void startGraph() {
        gvText.append("digraph G {\n") ;
    }

    public void endGraph() {
        gvText.append("}") ;
    }
}