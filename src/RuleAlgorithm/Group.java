package RuleAlgorithm;

import characters.*;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by geyao on 16/8/1.
 */
public class Group {
    private int tableNumber ;

    private int[] intPlayers ; // 所有玩家, 标志其存活状态, 1为生, 2为死
    private ArrayList<Integer> intAlivePlayers = new ArrayList<>();


    private HashMap<Integer, Village> MapPlayers = new HashMap<>();
    private HashMap<Integer, Village> MapVillagers = new HashMap<>();
    private HashMap<Integer, Village> MapGods = new HashMap<>();
    private HashMap<Integer, Village> MapWolves = new HashMap<>();
    private HashMap<Integer, Village> Players = new HashMap<>();



    public Group( int totalnumbers ){ // 构造函数
        tableNumber = totalnumbers;
        intPlayers = new int[tableNumber];
//        MapPlayers = new HashMap<>(tableNumber);
//        AryPlayers = new ArrayList<>(tableNumber);
        /**
         *  玩家  民 神 狼
         *   8   2  3  3
         *   9   3  3  3
         *  10   4  3  3
         *  11   3  4  4
         *  12   4  4  4
         *  注意,0预言家 1女巫 2猎人 9白痴(11人局以上有)
         * */

        MapGods.put(0, new Prophet());
        MapGods.put(1, Witch.WitchBorn());
        MapGods.put(2, new Hunter());
        for (int i = 3; i < 6; i++)
            MapWolves.put(i, new Wolf());
        for (int i = 6; i < 8; i++)
            MapVillagers.put(i, new Village());
        // 至此, 神3, 狼3, 民2
        if (tableNumber >= 9)
            MapVillagers.put(8, new Village());
        // 至此,神3, 狼3, 民3
        if (tableNumber >=10 )
            MapVillagers.put(9, new Village());
        // 至此, 神3, 狼3, 民3, 民1
        if (tableNumber >= 11 ){
            MapVillagers.remove(9);
            MapGods.put(9, new Fool());
            MapWolves.put(10, new Wolf());
        }
        // 至此, 神3, 狼3, 民3, 神1, 狼1
        if (tableNumber == 12 )
            MapVillagers.put(11, new Village());
        // 至此, 神3, 狼3, 民3, 神1, 狼1, 民1 ,可以合并了
        MapPlayers.putAll(MapGods);
        MapPlayers.putAll(MapVillagers);
        MapPlayers.putAll(MapWolves);
        // 合并完成


        for (int i = 0 ; i < tableNumber ; i ++ ) { // 将存活玩家的 【序号】 添加到数组中
            intPlayers[i] = 1; //
            intAlivePlayers.add(i);
        }


        for (int i = 0; i < intPlayers.length ; i++ ){ // 把玩家序号依次撒到MapPlayers上面
            boolean done = false;
            while (!done) {
                double random = Math.random();
                int num = (int) (random * tableNumber); // num 为0 ~ tableNumber-1 的整形数,刚好对应下标
                if (MapPlayers.get(num).getNumber() == -1){
                    MapPlayers.get(num).setNum(i);
                    done = true;
                }
            } // while()
        } // for() 至此, 每个Village都有了独一无二的座次号




        for (HashMap.Entry<Integer, Village> entry : MapPlayers.entrySet()) { // 创建有序新表
            Players.put(entry.getValue().getNumber(), entry.getValue());
        }
        for (HashMap.Entry<Integer, Village> entry : Players.entrySet()) { // 遍历新表,打印
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        {// 创建有序新表
            HashMap<Integer, Village> map = new HashMap<>();
            for (HashMap.Entry<Integer, Village> entry : MapGods.entrySet()) { // 神的新表
                map.put(entry.getValue().getNumber(), entry.getValue());
            }

            MapGods.clear();
            MapGods.putAll(map);
            map.clear();
            for (HashMap.Entry<Integer, Village> entry : MapVillagers.entrySet()) { // 民的新表
                map.put(entry.getValue().getNumber(), entry.getValue());
            }

            MapVillagers.clear();
            MapVillagers.putAll(map);
            map.clear();
            for (HashMap.Entry<Integer, Village> entry : MapWolves.entrySet()) { // 狼的新表
                map.put(entry.getValue().getNumber(), entry.getValue());
            }
            MapWolves.clear();
            MapWolves.putAll(map);
            map.clear();
        }
        for (HashMap.Entry<Integer, Village> entry : MapGods.entrySet()) { // 遍历新表,打印
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }for (HashMap.Entry<Integer, Village> entry : MapVillagers.entrySet()) { // 遍历新表,打印
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }for (HashMap.Entry<Integer, Village> entry : MapWolves.entrySet()) { // 遍历新表,打印
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    } // 构造函数

    public HashMap getHisMap(int num){ // 根据座位号返回玩家所在阵营的HashMap
        Village V = MapPlayers.get(num);
        if (MapGods.containsKey(V.getNumber()))
            return MapGods;
        if (MapVillagers.containsKey(V.getNumber()))
            return MapVillagers;
        if (MapWolves.containsKey(V.getNumber()))
            return MapWolves;
        else
            return Players;
    }

    public Village getPlayers(int num){ // 根据座位号返回玩家
        return Players.get(num);
    }

    public void kill(int num){ // 杀死某位玩家, 死亡函数
        if (Players.get(num).getalive()){
            Players.get(num).die();
        }

    }

    public void leave (int num){ // 离场
        if (!Players.get(num).getalive() && !Players.get(num).isLeave()){
            Players.get(num).leave();
            getHisMap(num).remove(num);
            if (isGameEndAndWhoLose() !=0 ){
                //调用游戏结束函数
            }
        }
    }

    public Prophet getProphet(){ // 获取预言家
        return (Prophet) MapPlayers.get(0);
    }

    public Witch getWitch(){ // 获取女巫
        return (Witch) MapPlayers.get(1);
    }

    public Hunter getHunter(){ // 获取猎人
        return (Hunter) MapPlayers.get(2);
    }

    public HashMap getWolvesMap(){ // 获取狼人的HashMap
        return MapWolves;
    }

    public HashMap getVillagersMap(){ // 获取村民的HashMap
        return MapVillagers;
    }

    public HashMap getGodsMap(){ // 获取神的HashMap
        return MapGods;
    }

    public int isGameEndAndWhoLose(){ // 判断游戏是否结束, 返回失败方
        if (MapWolves.isEmpty())
            return 3;
        if (MapGods.isEmpty())
            return 2;
        if (MapVillagers.isEmpty())
            return 1;
        return 0;
    }

    public static void main(String[] args){
        new Group(12);
    }
}
