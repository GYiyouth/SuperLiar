package RuleAlgorithm;

import characters.*;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by geyao on 16/8/1.
 */
public class Group {
    private int tableNumber ;

    private int[] intPlayers ; // 所有玩家, 标志其存活状态, 1为生, 2为死
    private ArrayList<Integer> intAlivePlayers = new ArrayList<>(); // 存活玩家的数组,包含玩家座次号
    private ArrayList<Integer> WfKill = new ArrayList<>(); // 狼人刀杀的目标
    private ArrayList<Integer> VoteKill = new ArrayList<>(); // 票杀的目标组合
    private ArrayList<Integer> Leaving = new ArrayList<>(); // 即将离场的人



    private HashMap<Integer, Village> MapPlayers = new HashMap<>(); // 数字对应身份 排列的哈希表
    private HashMap<Integer, Village> MapVillagers = new HashMap<>();
    private HashMap<Integer, Village> MapGods = new HashMap<>();
    private HashMap<Integer, Village> MapWolves = new HashMap<>();
    private HashMap<Integer, Village> Players = new HashMap<>(); // 数字对应座次号 排列的哈希表



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

    public void Night(){ // 夜晚函数,主要阶段是狼人刀,女巫救,预言家查看
        WolfAct();
        WitchAct();
        ProphetAct();
        // 此时留下了Leaving数组给上帝宣布死亡
    }

    public int WolfAct(){ // 狼人行动, 返回最后确定击杀的那一个
        WfKill.clear(); // 上一轮刀杀目标残留在数组中,清空
        Leaving.clear(); // 将上一轮将离场玩家清除
        for (Village wolf : MapWolves.values()) { // 遍历狼表,将欲击杀目标座次号添加到WfKill数组中
            Wolf wf = (Wolf) wolf;
            wf.wolfkill(intAlivePlayers, WfKill);
        }
        int count[] = new int[intPlayers.length]; // 统计谁的票最多
        for (int i : WfKill){
            count [i] ++;
        }
        int dyingMan = -1;
        int max = 0;
        for (int i = 0; i < intPlayers.length; i++){ // 找出被最多狼刀杀的那一个
            if (count[i] > max ){
                max = count[i];
                dyingMan = i;
            }
        }

        Leaving.add(dyingMan); // 将最终击杀目标添加到Leaving数组中
        Players.get(dyingMan).die(); // 目标玩家进入死亡状态
        return dyingMan;
    }

    public void WitchAct(){ // 女巫行动
        if (getWitch().getalive()) { // 女巫须存活
            Scanner in = new Scanner(System.in);
            if (this.getWitch().getAntitode()) { // 如果解药还在, 将告知刀杀目标并决定是否营救
                System.out.println("今天晚上被击杀的玩家是" + Leaving.toString() + "\t 营救请输入1, 否则输入2");
                if (in.nextInt() == 1) {
                    getWitch().save(Players.get(Leaving.get(0)));
                    Leaving.remove(0); // 营救后将被刀杀溢出将离场数组
                }
            }
            if (this.getWitch().getPoison()) { // 如果毒药还在
                System.out.println("当前存货玩家为"+intAlivePlayers+" 号");
                System.out.println("你有一瓶毒药,你要用吗, 如果使用请输入欲毒玩家座次号,否则请输入-1");
                try {
                    int target = in.nextInt();
                    if (intAlivePlayers.contains(target)){
                        getWitch().kill(Players.get(target));
                        Leaving.add(target); // 将被毒杀玩家添加至离场玩家数组
                        in.close();
                    }
                }
                catch (Exception e){
                    return;
                }


            }
        }
    }

    public void ProphetAct(){ // 预言家行动
        if(getProphet().getalive()){ // 预言家存活
            System.out.println("目前存活的玩家有\n"+ intAlivePlayers);
            System.out.println("请选择要查看的玩家身份, 并输入数字");
            Scanner in = new Scanner(System.in);
            try{
                int target = in.nextInt();
                getProphet().getIdentity(Players.get(target));
            }
            catch (Exception e){
                //
            }
        }
    }

    public static void main(String[] args){
        new Group(12);
    }
}
