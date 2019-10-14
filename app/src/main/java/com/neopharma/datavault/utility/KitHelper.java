package com.neopharma.datavault.utility;

import com.neopharma.datavault.model.kit.Kit;

import java.util.ArrayList;
import java.util.List;

public class KitHelper {
    public static List<String> getKitsNames(List<Kit> kits) {
        List<String> list = new ArrayList<>();
        if (kits != null)
            for (Kit kit : kits) {
                list.add(kit.serialNo);
            }
        return list;
    }
}
