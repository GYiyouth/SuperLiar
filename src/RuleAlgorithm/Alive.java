package RuleAlgorithm;

import characters.Village;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by geyao on 16/8/19.
 */
public class Alive { // 全局变量类
    private Alive(){}

    public static volatile ArrayList<Integer> intPlayers = new ArrayList<>();
    public static volatile ArrayList<Integer> Leaving = new ArrayList<>();
    public static volatile ArrayList<Integer> intWolves = new ArrayList<>();
    public static volatile HashMap<Integer, Village> Players = new HashMap<>();
    public static volatile HashMap<Integer, Village> Villagers = new HashMap<>();
    public static volatile HashMap<Integer, Village> Gods = new HashMap<>();
    public static volatile HashMap<Integer, Village> Wolves = new HashMap<>();
    public static volatile Object[] talkKey; // 发言关键字
    public static volatile Object talkKeyAll = new Object(); // 发言总开关
    public static volatile boolean[] voteKey; // 投票关键字, 不能使用Object, 很麻烦, 因为主函数不常是拥有着
    public static volatile HashMap<Integer, ArrayList<Integer>> candidates = new HashMap<>(); // 给每个投票人的候选列表
    public static volatile boolean AllConfirm = false; // 全员投票, 上警投票
    public static volatile int[] voteResult ; // 用来各个玩家给主线程反应投票结果
    public static volatile boolean endGame = false; // 游戏结束锁
    public static volatile boolean dayKey = false; // 进入白天关键字
    public static volatile boolean nightKey = false; // 进入夜晚关键字
}
