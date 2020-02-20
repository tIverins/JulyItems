package github.july_summer.julyitems.skills;

public class SkillData {

    public Object[] data;

    public SkillData(Object[] data){
        this.data = data;
    }

    public SkillData(int length){
        data = new Object[length];
    }

    public void setData(int length, Object vaule){
        data[length] = vaule;
    }

    public Object getData(int length){
        return data[length];
    }
}
