package com.my.friends.dao.extend;

import com.my.friends.dao.Lb;
import com.my.friends.dao.LbItem;
import lombok.Data;

import java.util.List;

/**
 * @Atuhor: qin
 * @Create: 2021-02-27-17-01
 * @Time: 17:01
 * @Description:
 */
public class LbXm extends Lb {

    private List<LbItem> lbItem;

    public List<LbItem> getLbItem() {
        return lbItem;
    }

    public void setLbItem(List<LbItem> lbItem) {
        this.lbItem = lbItem;
    }
}
