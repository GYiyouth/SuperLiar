package characters;

import act.Kill;

/**
 * Created by geyao on 16/7/5.
 */
public class Witch extends Village implements Kill{ // 女巫

    private int antidote = 1 ; // 一瓶解药
    private int poison = 1; // 一瓶毒药
    private boolean saveYourself = false; // 默认不可自救

    private Witch( ){ // 女巫构造函数,私有,单例
        super();
        this.setIdentity(4);
    }

    private static Witch witch = null; // 单例模式,唯一的女巫

    public synchronized static Witch WitchBorn( ){ // 女巫诞生
        if( witch == null ){
            witch = new Witch();
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

    public boolean useAntitode(){ // 使用解药
        if (getAntitode()){
            this.antidote = 0;
            return true;
        }
        return false;
    }

    public boolean usePoison(){
        if ((getPoison())){
            this.poison = 0;
            return true;
        }
        return false;
    }

    public boolean getPoison(){ // 查询毒药数量
        if( this.poison == 1 )
            return true;
        return false;
    }

    public boolean save(Village deadplayer){ // 拯救某玩家, 会查询目标玩家状态, 解药状态, 解救以后毒药减1, 该玩家重生
        if(  this.getAntitode() ){ // 如果有毒药
            if( deadplayer.equals(this) && !saveYourself ) { // 如果被击杀的是女巫,并且设定不能自救, 返回false
                return false;
            }

            this.antidote--;
            deadplayer.reborn();
            return true;
        }
        return false;
    }

    public int kill( Village village){ // 毒杀目标玩家
        if( village.getalive() && this.getPoison() ){
            village.die(1);
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
