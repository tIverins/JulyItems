package github.july_summer.julyitems.skills;


public enum SkillTrigger {

    LEFT_CLICK("左键"),
    RIGHT_CLICK("右键"),
    FIRST_HELD("首次手持"),
    LAST_HELD("持续手持"),
    ATTACK("攻击玩家和生物"),
    ATTACK_PLAYER("攻击玩家"),
    ATTACK_ENTITY("攻击生物"),
    GET_DAMAGE("受到伤害"),
    GET_ENTITY_DAMAGE("受到生物伤害"),
    GET_PLAYER_DAMAGE("受到玩家伤害"),
    BREAK_BLOCK("破坏方块时");


    private String str;

    private SkillTrigger(String str){
        this.str = str;
    }

    public String getDisplayName(){
        return str;
    }

    public static boolean contains(String trigger) {
        for (SkillTrigger t : SkillTrigger.values()) {
            if (t.name().equals(trigger)) {
                return true;
            }
        }
        return false;
    }

}
