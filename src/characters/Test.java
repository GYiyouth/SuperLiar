package characters;


import OthersPlayers.God;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by geyao on 16/7/5.
 */

public class Test {

//    public static void main(String[] args){
//
//        System.out.println("TEST");
//
//        Map<Integer, Village> V = new HashMap<>();
//        Map<Integer, Village> G = new HashMap<>();
//        Map<Integer, Wolf > W = new HashMap<>();
//        Map<Integer, Village> AllPlayers = new HashMap<>();
//
//        Scanner in = new Scanner(System.in);
//        int maxPlayers = in.nextInt();
//        ArrayList<Integer> remainPlayers = new ArrayList<>();
//
//        for( int i = 1; i<= maxPlayers; i++)
//            remainPlayers.add(i);
//
//
//
//
//        Village prophet = new Prophet(1);
//        G.put(1,prophet);
//        Village villager1 = new Village(2);
//        V.put(2,villager1);
//        Village villager2 = new Village(3);
//        V.put(3,villager2);
//        Village witch = Witch.WitchBorn(4);
//        G.put(4,witch);
//        Wolf wolf1 = new Wolf(5);
//        W.put(5,wolf1);
//        Wolf wolf2 = new Wolf(6);
//        W.put(6,wolf2);
//        God god ;
//
//        AllPlayers.putAll(V);
//        AllPlayers.putAll(G);
//        AllPlayers.putAll(W);
//
//        System.out.println(V);
//
//        ArrayList<Integer> villagers = new ArrayList<>();
//        ArrayList<Integer> gods = new ArrayList<>();
//        ArrayList<Integer> wolves = new ArrayList<>();
//
//        villagers.add(2);
//        villagers.add(3);
//        gods.add(4);
//        gods.add(1);
//        wolves.add(5);
//        wolves.add(6);
//
//
//        int[] wfKill = new int[ maxPlayers+1 ]; // 下标是刀杀目标, 内容是杀他的狼人数目
//        int killedbyWf = 0; // 该座次号的玩家被杀害
//        for( int wolf : wolves){
//            System.out.println(wolves+"请选出要杀的玩家");
//            wfKill[W.get(wolf).wolfkill(remainPlayers)]++; // 被投票击杀的玩家会多一票
//        }
//        for( int i=1; i<=maxPlayers; i++ ){ // 循环找出被最多狼击杀的玩家
//            if( wfKill[i]>=wfKill[1]){
//                killedbyWf = i;
//                wfKill[1] = wfKill[i];
//            }
//        }
//
//        /*
//        * 这里应该还有女误判断,死亡的判断应该是两步
//        * */
//        AllPlayers.get( killedbyWf ).die(); // 该玩家死亡
//
//        if( V.containsValue(killedbyWf ) ){ // 如果是平民玩家死亡
//            for( int i = V.size()-1; i >= 0 ; i-- ){
//                if( villagers.get(i) == killedbyWf )
//                    villagers.remove( killedbyWf );
//            }
//        }
//
//        if( Witch.getWitch().save( AllPlayers.get( killedbyWf )) && Witch.getWitch().getAntitode() ){ // 如果女巫有解药救他
//
//            AllPlayers.get( killedbyWf ).reborn(); // 重生
//        }
//
//
//
//
//        //明天工作从这里开始
//
//
//
////        Witch c = Witch.creatWitch(2);
////        a.print();
////        Village b = c;
////        Witch d = Witch.getWitch();
////
////        if(d == b)
////            System.out.println(1);
////        Kill wf = new Wolf(4);
////
////        c.kill(a);
////        d.kill(c);
////        b.print();
////        b.die();
////        d.print("god");
//    }
}
