package characters;

/**
 * Created by geyao on 16/8/1.
 */
public class Fool extends Village {
    private int sheild = 1; // 免票死次数
    public Fool(){ // 构造函数
        super();
        this.setIdentity(2);
    }

    public int getSheild(){ // 查询免死次数
        return sheild;
    }

    public boolean useSheild(){ // 使用免死权利
        if (sheild == 1){
            sheild--;
            return true;
        }
        return false;
    }

    @Override
    public int die(int way){
        switch (way){
            case 1 : return super.die(1); // 狼人第二天及以后刀杀, 无遗言死
            case 2 : return super.die(2);// 狼人第一天刀杀, 或者别的有遗言死情况
            case 3: {
                if (getSheild() == 1) { // 被票死 且有免死机会
                    useSheild();
                    return -1;
                }else
                    return super.die(3);
            }
        }
        return this.getIdentity();

    }

}
