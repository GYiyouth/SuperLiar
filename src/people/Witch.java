package people;

import act.Kill;

/**
 * Created by geyao on 16/7/5.
 */
public class Witch extends Player implements Kill{ // 女巫

    private int antidote = 1 ; // 一瓶解药
    private int poison = 1; // 一瓶毒药
    private boolean saveYourself = false; // 默认不可自救

    private Witch( int number ){ // 女巫构造函数,私有,单例
        super(number);
        this.setIdentity(2);
    }

    private static Witch witch = null; // 单例模式,唯一的女巫

    public synchronized static Witch WitchBorn(int number ){ // 女巫诞生
        if( witch == null ){
            witch = new Witch(number);
        }
        return witch;
    }

    public static Witch getWitch(){ // 返回当前女巫
        return witch;
    }

    public boolean getAntitode(){ // 查询解药数量
        if(this.antidote == 1 )
            return true;
        return false;
    }

    public boolean getPoison(){ // 查询毒药数量
        if( this.poison == 1 )
            return true;
        return false;
    }

    public boolean save(Player deadplayer){ // 拯救某玩家, 会查询目标玩家状态, 解药状态, 解救以后毒药减1, 该玩家重生
        if( deadplayer.getalive() == false && this.getAntitode() ){
            if(!saveYourself && deadplayer.equals(this)) {
                return false;
            }
            this.antidote--;
            deadplayer.reborn();
            return true;
        }
        return false;
    }

    public int kill( Player player ){ // 毒杀目标玩家
        if( player.getalive() && this.getPoison() ){
            player.die();
            this.poison--;
            return 2;
        }
        return 0;
    }


    public void print(String god){
        super.print();
        if (god.equals("god"))
        System.out.println("该玩家是女巫");
    }
}
