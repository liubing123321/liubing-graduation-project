package com.example.service;

import com.example.controller.QuestionController;
import org.apache.commons.lang.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/16.
 * 敏感词过滤
 */
@Service
public class SensitiveService implements InitializingBean{
    private static final Logger logger= LoggerFactory.getLogger(SensitiveService.class);
    @Override
    public void afterPropertiesSet() throws Exception {
            try{
                InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
                InputStreamReader read=new InputStreamReader(is);
                BufferedReader bufferedReader=new BufferedReader(read);
                String lineTxt;
                while((lineTxt=bufferedReader.readLine())!=null){
                        adddWord(lineTxt.trim());
                }
                read.close();
            }catch(Exception e){
                logger.error("读取敏感词文件失败"+e.getMessage());
        }
    }
    //增加关键词
    private void adddWord(String lineTxt){
        TrieNode tempNode=rootNode;//指针指向根节点
        for(int i=0;i<lineTxt.length();++i){
            Character c=lineTxt.charAt(i);
            // 过滤空格
            if (isSymbol(c)) {
                continue;
            }

            TrieNode node=tempNode.getSubNode(c);
            if(node==null){
                node=new TrieNode();
                tempNode.addSubNode(c,node);
            }
            tempNode=node;
            if(i==lineTxt.length()-1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    private class TrieNode{/*前缀树节点*/
        //是不是关键词的结尾
        private boolean end=false;
        //当下所有的子节点
        private Map<Character,TrieNode> subNodes=new HashMap<Character,TrieNode>();

        public void addSubNode(Character key,TrieNode node){
                subNodes.put(key,node);
        }

        TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        boolean isKeywordEnd(){return end;}

        void setKeywordEnd(boolean end){
            this.end=end;
        }
    }
    private TrieNode rootNode= new TrieNode();

    /**
     * 判断是否是一个符号
     */
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    /**
     * 过滤敏感词
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        String replacement = "***";
        StringBuilder result = new StringBuilder();

        TrieNode tempNode = rootNode;//蓝色
        int begin = 0; // 回滚数   红色
        int position = 0; // 当前比较的位置   黑色

        while (position < text.length()) {
            char c = text.charAt(position);
            // 空格直接跳过
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
            tempNode = tempNode.getSubNode(c);

            // 当前位置的匹配结束
            if (tempNode == null) {
                // 以begin开始的字符串不存在敏感词
                result.append(text.charAt(begin));
                // 跳到下一个字符开始测试
                position = begin + 1;
                begin = position;
                // 回到树初始节点
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                // 发现敏感词， 从begin到position的位置用replacement替换掉
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
            }
        }

        result.append(text.substring(begin));

        return result.toString();
    }


    public static void main(String[] argv){
        SensitiveService s=new SensitiveService();
        s.adddWord("色情");
        s.adddWord("赌博");
        System.out.print(s.filter("你好色 情"));
    }
}
