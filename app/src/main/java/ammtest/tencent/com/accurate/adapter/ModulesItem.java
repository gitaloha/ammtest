package ammtest.tencent.com.accurate.adapter;

/**
 * Created by xiazeng on 2015/11/26.
 */
public class ModulesItem {
    public String getModulesName() {
        return modulesName;
    }

    public void setModulesName(String modulesName) {
        this.modulesName = modulesName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    String modulesName;
    int count;

    public ModulesItem(String modulesName, int count) {
        this.modulesName = modulesName;
        this.count = count;
    }
}
