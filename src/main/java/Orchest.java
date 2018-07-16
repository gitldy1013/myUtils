/**
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @author ceshi
 * @date 2018/7/1218:06
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @Author: Liudongyang
 * @Description:
 * @Date: Created in 18:06 2018/7/12
 * @Modified By:
 * @Version: 1.0.0
 */
public class Orchest {
  /**
   * 测试主函数
   */
  public static void main(String[] args) {
    //  1,2,3
    //  4,5
    //  6,7,8
    //  9
    Map<String, List<String>> rawArrayMap = new HashMap<String, List<String>>();
    ArrayList<String> integers1 = new ArrayList<String>();
    integers1.add("1");
    integers1.add("2");
    integers1.add("3");
    ArrayList<String> integers2 = new ArrayList<String>();
    integers2.add("4");
    integers2.add("5");
    ArrayList<String> integers3 = new ArrayList<String>();
    integers3.add("6");
    integers3.add("7");
    integers3.add("8");
    ArrayList<String> integers4 = new ArrayList<String>();
    integers4.add("9");
    rawArrayMap.put("1", integers1);
    rawArrayMap.put("2", integers2);
    rawArrayMap.put("3", integers3);
    rawArrayMap.put("4", integers4);
//    List<Map<String, String>> orchest = orchest(rawArrayMap);
//    System.out.println(orchest);
//    List<Map<String, String>> orchestPro = orchestPro(rawArrayMap);
//    System.out.println(orchestPro);
//    List<Map<String, String>> orchestProPlus = orchestProPlus(rawArrayMap);
//    System.out.println(orchestProPlus);
    //==================================================================================================================
//    String result = "";
//    String separator = ",";
//    List<String> fn = fn(rawArrayMap, 0, result, separator);
//    System.out.println(fn);
    //==================================================================================================================
//    int[][] array = new int[][]{{1, 2, 3, 5}, {4, 5, 6}, {7, 8, 9, 10}};
//    int[] num = new int[array.length];
//    sort(array, array.length, 0, num);
    //==================================================================================================================
    Set<String> origin = new TreeSet<String>();
    origin.add("1");
    origin.add("2");
    origin.add("3");
    Long depth = 0L;
    Map<Long, Set<String>> res = new HashMap<Long, Set<String>>();
    res.put(0L, origin);
    fullComp(origin, depth, res);
    System.out.println(res);
  }

  /**
   * 维元编排
   */
  //[{1=1, 2=4, 3=6, 4=9}, {1=1, 2=4, 3=7, 4=9}, {1=1, 2=4, 3=8, 4=9},
  // {1=1, 2=5, 3=6, 4=9}, {1=1, 2=5, 3=7, 4=9}, {1=1, 2=5, 3=8, 4=9},
  // {1=2, 2=4, 3=6, 4=9}, {1=2, 2=4, 3=7, 4=9}, {1=2, 2=4, 3=8, 4=9},
  // {1=2, 2=5, 3=6, 4=9}, {1=2, 2=5, 3=7, 4=9}, {1=2, 2=5, 3=8, 4=9},
  // {1=3, 2=4, 3=6, 4=9}, {1=3, 2=4, 3=7, 4=9}, {1=3, 2=4, 3=8, 4=9},
  // {1=3, 2=5, 3=6, 4=9}, {1=3, 2=5, 3=7, 4=9}, {1=3, 2=5, 3=8, 4=9}]
  private static List<Map<String, String>> orchest(Map<String, List<String>> rawArrayMap) {
    //创建结果集合
    List<Map<String, String>> rawOrchestList = new ArrayList<Map<String, String>>();
    //初始化结果集的size
    int totalNum = 1;
    //循环整个map
    Set<String> keys = rawArrayMap.keySet();
    //得到结果集中存放的元素个数
    for (String key : keys)
      totalNum = totalNum * rawArrayMap.get(key).size();
    System.out.println("结果集中存放的元素个数：" + totalNum);
    //填充初始化数据
    for (int i = 0; i < totalNum; i++)
      rawOrchestList.add(new LinkedHashMap<String, String>());

    //计数器
    int count = 0;
    Integer loopSize = 1;
    for (String key : keys) {
      //单个子单元数组
      List<String> metaList = rawArrayMap.get(key);
      //子单元数组个数
      int metaSize = metaList.size();

      //结果集总数/外层循环数
      int singleLoopNum = totalNum / loopSize;
//      System.err.println("singleLoopNum:" + singleLoopNum);
      //结果集总数/每层子单元个数
      int singleUnitTotalNum = totalNum / metaSize;
//      System.err.println("singleUnitTotalNum:" + singleLoopNum);

      int singleUnitNumPerLoop = singleUnitTotalNum / loopSize;
//      System.err.println("singleUnitNumPerLoop:" + singleLoopNum);

      for (int i = 0; i < loopSize; i++)
        for (int j = 0; j < metaSize; j++) {
          //得到每层的每个子单元作为value 该层的key即是结果集的key
          String metaId = metaList.get(j);
          for (int h = 0; h < singleUnitNumPerLoop; h++) {
            //得到要填充数据的map
//            System.out.println("得到要填充数据的map:" + (i * singleLoopNum + j * singleUnitNumPerLoop + h));
            Map<String, String> row = rawOrchestList.get(i * singleLoopNum + j * singleUnitNumPerLoop + h);
            count++;
            //向每一层的每一个map中填入数据
            row.put(key, metaId);
          }
        }
      //
      loopSize *= metaSize;
      System.err.println(rawOrchestList);
    }

    System.err.println(count);
    return rawOrchestList;
  }

  //====================================================================================================================

  /**
   * 内部封装实体类
   */
  public static class Index {
    private int index;
    private String key;
    private int size;
    private int arrindex;

    //下一个元素
    private Index next;
    //上一个元素
    private Index pre;

    Index(int index, String key, int size, int arrindex) {
      this.index = index;
      this.key = key;
      this.size = size;
      this.arrindex = arrindex;
    }

    public Index getNext() {
      return next;
    }

    public void setNext(Index next) {
      this.next = next;
    }

    public Index getPre() {
      return pre;
    }

    public void setPre(Index pre) {
      this.pre = pre;
    }

    public int getArrindex() {
      return arrindex;
    }

    public void setArrindex(int arrindex) {
      this.arrindex = arrindex;
    }

    public int getSize() {
      return size;
    }

    public void setSize(int size) {
      this.size = size;
    }

    public int getIndex() {
      return index;
    }

    public void setIndex(int index) {
      if (index >= this.size) {
        if (this.next != null) {
          this.next.setIndex(this.next.getIndex() + 1);
          this.index = 0;
        }
      } else {
        this.index = index;
      }
    }

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public void setPreIndex(int index) {
      if (index >= this.size) {
        if (this.pre != null) {
          this.pre.setPreIndex(this.pre.getIndex() + 1);
          this.index = 0;
        }
      } else {
        this.index = index;
      }
    }
  }

  /**
   * 优化后的全编排算法
   */
  //[{1=1, 2=4, 3=6, 4=9}, {1=2, 2=4, 3=6, 4=9}, {1=3, 2=4, 3=6, 4=9},
  // {1=1, 2=5, 3=6, 4=9}, {1=2, 2=5, 3=6, 4=9}, {1=3, 2=5, 3=6, 4=9},
  // {1=1, 2=4, 3=7, 4=9}, {1=2, 2=4, 3=7, 4=9}, {1=3, 2=4, 3=7, 4=9},
  // {1=1, 2=5, 3=7, 4=9}, {1=2, 2=5, 3=7, 4=9}, {1=3, 2=5, 3=7, 4=9},
  // {1=1, 2=4, 3=8, 4=9}, {1=2, 2=4, 3=8, 4=9}, {1=3, 2=4, 3=8, 4=9},
  // {1=1, 2=5, 3=8, 4=9}, {1=2, 2=5, 3=8, 4=9}, {1=3, 2=5, 3=8, 4=9}]
  private static List<Map<String, String>> orchestPro(Map<String, List<String>> rawArrayMap) {
    //创建结果集合
    List<Map<String, String>> rawOrchestList = new LinkedList<Map<String, String>>();
    //循环整个map
    Set<String> keys = rawArrayMap.keySet();
    //初始化结果集的size
    int totalNum = 1;
    //记录各个元素的位置
    Index[] indexarr = new Index[keys.size()];
    //得到结果集中存放的元素个数
    int ax = 0;
    for (String key : keys) {
      totalNum = totalNum * rawArrayMap.get(key).size();
      Index index = new Index(0, key, rawArrayMap.get(key).size(), ax);
      indexarr[ax++] = index;
    }
    for (int i = 0; i < indexarr.length - 1; i++) {
      indexarr[i].next = indexarr[i + 1];
    }
    //计算时先用第一个元素加一
    indexarr[0].setIndex(-1);
    //计数器
    int count = 0;
    //外循环
    for (int j = 0; j < totalNum; j++) {
      //填充空MAP容器
      rawOrchestList.add(new LinkedHashMap<String, String>());
      //内循环
      for (String key : keys) {
        boolean flag = false;
        //数据填充循环
        for (int i = 0; i < indexarr.length; i++) {
          if (indexarr[i].getKey().equals(key)) {
            //每次内循环累加一次
            if (flag == false) {
              indexarr[i].setIndex(indexarr[i].getIndex() + 1);
              if (i < indexarr.length - 1) {
                indexarr[i + 1] = indexarr[i].next;
              }
            }
            //MAP容器填充数据
            rawOrchestList.get(j).put(key, rawArrayMap.get(indexarr[i].getKey()).get(indexarr[i].getIndex()));
            count++;
          }
          flag = true;
        }
        System.err.println(rawOrchestList);
      }
    }
    System.err.println(rawOrchestList.size());
    System.err.println(count);
    return rawOrchestList;
  }

  /**
   * 优化后的全编排算法 (倒排改造)
   */
  //[{1=1, 2=4, 3=6, 4=9}, {1=1, 2=4, 3=7, 4=9}, {1=1, 2=4, 3=8, 4=9},
  // {1=1, 2=5, 3=6, 4=9}, {1=1, 2=5, 3=7, 4=9}, {1=1, 2=5, 3=8, 4=9},
  // {1=2, 2=4, 3=6, 4=9}, {1=2, 2=4, 3=7, 4=9}, {1=2, 2=4, 3=8, 4=9},
  // {1=2, 2=5, 3=6, 4=9}, {1=2, 2=5, 3=7, 4=9}, {1=2, 2=5, 3=8, 4=9},
  // {1=3, 2=4, 3=6, 4=9}, {1=3, 2=4, 3=7, 4=9}, {1=3, 2=4, 3=8, 4=9},
  // {1=3, 2=5, 3=6, 4=9}, {1=3, 2=5, 3=7, 4=9}, {1=3, 2=5, 3=8, 4=9}]
  private static List<Map<String, String>> orchestProPlus(Map<String, List<String>> rawArrayMap) {
    //创建结果集合
    List<Map<String, String>> rawOrchestList = new LinkedList<Map<String, String>>();
    //循环整个map
    Set<String> keys = rawArrayMap.keySet();
    //初始化结果集的size
    int totalNum = 1;
    //记录各个元素的位置
    Index[] indexarr = new Index[keys.size()];
    //得到结果集中存放的元素个数
    int ax = 0;
    for (String key : keys) {
      totalNum = totalNum * rawArrayMap.get(key).size();
      Index index = new Index(0, key, rawArrayMap.get(key).size(), ax);
      indexarr[ax++] = index;
    }
    for (int i = 0; i < indexarr.length - 1; i++) {
      indexarr[i + 1].pre = indexarr[i];
    }
    //计数器
    int count = 0;
    //外循环
    for (int j = 0; j < totalNum; j++) {
      //填充空MAP容器
      rawOrchestList.add(new LinkedHashMap<String, String>());
      //内循环
      for (String key : keys) {
        //数据填充循环
        for (int i = 0; i < indexarr.length; i++) {
          if (indexarr[i].getKey().equals(key)) {
            //每次内循环累加一次
            if (indexarr[indexarr.length - 1].getKey().equals(key)) {
              indexarr[i].setPreIndex(indexarr[i].getIndex() + 1);
              if (i < indexarr.length - 1) {
                indexarr[i + 1].pre = indexarr[i];
              }
            }
            //MAP容器填充数据
            rawOrchestList.get(j).put(key, rawArrayMap.get(indexarr[i].getKey()).get(indexarr[i].getIndex()));
            count++;
          }
        }
        System.err.println(rawOrchestList);
      }
    }
    System.err.println(rawOrchestList.size());
    System.err.println(count);
    return rawOrchestList;
  }


  /**
   * 默认排列分隔符
   */
  private final static String ORCHEST_SEPERATOR = "#";

  /**
   * 递归全排列
   */
//{0=[#3#, #2#, #1#],
// 1=[#3#2#, #2#3#, #3#1#, #1#3#, #2#1#, #1#2#],
// 2=[#3#2#1#, #3#1#2#, #2#1#3#, #2#3#1#, #1#3#2#, #1#2#3#]}
  public static void fullComp(Set<String> originElements, Long depth, Map<Long, Set<String>> result) {
    //非空和逻辑的严谨性判断校验
    if (originElements == null || originElements.size() <= 0 || depth < 0)
      return;
    //递归的弹出必要条件:层级如果和集合数相同 不再循环(例子中元素个数为三 即代表每层最多三个元素 一共递归到第三层)
    if (depth == originElements.size()) {
      return;
    }
    //0层
    if (depth == 0) {
      Set<String> zeroLayerNodes = new HashSet<String>();
      //遍历初始元素集合
      for (String element : originElements)
        //通过遍历初始元素集合,SET中的每个元素即为一个生成的编排结果集的最小单元
        zeroLayerNodes.add(ORCHEST_SEPERATOR + element + ORCHEST_SEPERATOR);
      //将0层结果集合放入结果MAP
      result.put(depth, zeroLayerNodes);
      System.out.println(result);
      //递归到下一层 层数加1
      fullComp(originElements, depth + 1, result);
    } else {
      //大于0层
      long lastLayer = depth - 1;
      //获取上一层结果集合
      Set<String> lastLayerNodes = result.get(lastLayer);
      //创建新的结果集合
      Set<String> newLayerNodes = new HashSet<String>();
      //遍历上层结果集合
      for (String lastLayerNode : lastLayerNodes) {
        //遍历初始元素集合
        for (String element : originElements) {
          //判断每一个结果集合中的元素是否包含当前传入的初始元素
          if (!lastLayerNode.contains(ORCHEST_SEPERATOR + element + ORCHEST_SEPERATOR)) {
            //不包含即可组成新的当前层集合最小节点单元
            newLayerNodes.add(lastLayerNode + element + ORCHEST_SEPERATOR);
          }
        }
      }
      //将当前层的结果集合放入结果MAP
      result.put(depth, newLayerNodes);
      System.out.println(result);
      //继续下一层递归
      fullComp(originElements, depth + 1, result);
    }
  }


  //====================================================================================================================
  //以下为递归方式
  protected static List<String> fn(Map<String, List<String>> rawArrayMap, int x, String result, String separator) {
    List<String[]> list = new ArrayList<String[]>();

    Set<Map.Entry<String, List<String>>> entries = rawArrayMap.entrySet();
    for (Map.Entry<String, List<String>> entrie : entries) {
      String[] strings = new String[entrie.getValue().size()];
      for (int i = 0; i < entrie.getValue().size(); i++) {
        strings[i] = entrie.getKey() + "=" + entrie.getValue().get(i);
      }
      list.add(strings);
    }
    //迭代list
    List<String> li = new ArrayList<String>();
    Object[] arr = list.get(x);
    //取得当前的数组
    int i = list.indexOf(arr);
    //迭代数组
    for (Object st : arr) {
      if (null != result && !result.trim().equals("")) {
        st = result + separator + st;
      }
      if (i < list.size() - 1) {
        li.addAll(fn(rawArrayMap, i + 1, st.toString(), separator));
      } else if (i == list.size() - 1) {
        li.add(st.toString());
      }
    }

    return li;
  }

  public static void sort(int[][] array, int length, int index, int[] num) {
    if (index == length) {
      String s = Arrays.toString(num);
      System.out.println(s);
      return;
    }

    for (int j = 0; j < array[index].length; j++) {//数组中的每一位遍历一次
      num[index] = array[index][j];
      sort(array, length, index + 1, num);
    }
  }
}
