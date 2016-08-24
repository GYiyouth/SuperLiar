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
    public static volatile HashMap<Integer, Village> Players = new HashMap<>();
    public static volatile HashMap<Integer, Village> Villagers = new HashMap<>();
    public static volatile HashMap<Integer, Village> Gods = new HashMap<>();
    public static volatile HashMap<Integer, Village> Wolves = new HashMap<>();
}
