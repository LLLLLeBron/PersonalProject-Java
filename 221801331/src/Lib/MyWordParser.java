package Lib;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MyWordParser implements WordParser
{
  private Map<String,Integer> wordCountMap;   //单词频率统计

  private int letterNum=0;    //连续字母数量

  private boolean isWordReading=false; //单词读取位；判断是否正在读取单词

  private boolean isThisWordValid=true;   //单词合法判断位;判断当前单词是否合法

  private StringBuilder wordReader=new StringBuilder(); //单词读取器

  /**
   * 默认构造方法
   */
  public MyWordParser()
  {
    wordCountMap=new HashMap<>();
  }

  /**
   * 判断是否是字母
   * @param c
   * @return
   */
  private boolean isLetter(char c)
  {
    if((c>='A'&&c<='Z')||(c>='a'&&c<='z'))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * 判断是否是有效字符
   * @param c
   * @return
   */
  private boolean isValidChar(char c)
  {
    if((c>='A'&&c<='Z')||(c>='a'&&c<='z')||(c>='0'&&c<='9'))
    {
      return true;
    }
    else
    {
      return false;
    }
  }


  /**
   * 统计单词
   * @return
   */
  @Override
  public int countWord(String text)
  {
    char c;
    int num=0;
    for (int i=0;i<text.length();i++)
    {
      c=text.charAt(i);
      if(isWordReading)   //正在读取单词
      {
        if(isValidChar(c)) //若是有效单词
        {
          wordReader.append(c);   //加入单词读取器
        }
        else  //否则
        {
          isWordReading=false;    //单词读取结束，读取位改为false
          isThisWordValid=true;   //单词读取结束,重置合法位
          num++;   //单词数量+1
          Integer count;
          String word=wordReader.toString().toLowerCase();

          letterNum=0;  //清空连续字母数量
          wordReader.delete(0,wordReader.length());   //清空单词读取器
          wordCountMap.put(word,(count=wordCountMap.get(word))==null?1:count+1);  //更新map
        }
      }
      else   //若否
      {
        if(!isValidChar(c))   //若为分隔符
        {
          isThisWordValid=true;   //重置合法位；开始读取下一个单词
          letterNum=0;  //清空连续字母数量
          wordReader.delete(0,wordReader.length());   //清空单词读取器
          continue;
        }
        if(!isThisWordValid)    //若此单词已不合法
        {
          continue;
        }

        if(isLetter(c))     //若为字母
        {
          wordReader.append(c);  //加入单词读取器
          letterNum++;  //字母数量+1
        }
        else
        {
          isThisWordValid=false;    //此单词不合法;将合法位置false
        }
        if(letterNum>=4)    //连续字母数量超过4个
        {
          isWordReading=true;  //开始读取单词
        }
      }
    }
    if(isWordReading)
    {
      isWordReading=false;
      num++;   //单词数量+1
      Integer count;
      String word=wordReader.toString().toLowerCase();
      letterNum=0;  //清空连续字母数量
      wordReader.delete(0,wordReader.length());   //清空单词读取器
      wordCountMap.put(word,(count=wordCountMap.get(word))==null?1:count+1);  //更新map
    }
    return num;
  }

  /**
   * 获取单词统计map
   * @return
   */
  @Override
  public Map<String, Integer> getWordCountMapBySize(int size)
  {
    return wordCountMap
        .entrySet()
        .stream()
        .sorted(Map.Entry.<String, Integer> comparingByValue() //字母频率升序排序
            .reversed()//倒序
            .thenComparing(Map.Entry.comparingByKey()))//按照key排序
        .limit(size) //选择最前面的十个
        .collect(  //以map形式返回
            Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (oldVal, newVal) -> oldVal,
                LinkedHashMap::new
            )
        );
  }


}
