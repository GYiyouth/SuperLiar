package act;

import characters.Player;

/**
 * Created by geyao on 16.
 */
public interface Kill {
    public int  kill(Player player);
    /**
     * 0代表错误
     * 1代表狼杀
     * 2代表毒杀
     * 3代表投票死
     * 4代表猎人带走
    * */
}
