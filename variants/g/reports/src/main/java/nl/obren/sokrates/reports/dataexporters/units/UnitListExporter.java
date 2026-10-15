/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.dataexporters.units;

import nl.obren.sokrates.sourcecode.units.UnitInfo;

import java.util.ArrayList;
import java.util.List;

public class UnitListExporter {
    private List<UnitInfo> units = new ArrayList<>();

    public UnitListExporter(List<UnitInfo> units) {
        this.units = units;
    }

    public List<UnitInfoExport> getAllUnitsData() {
        List<UnitInfoExport> unitInfoExports = new ArrayList<>();

        units.forEach(unit -> {
            unitInfoExports.add(UnitInfoExport.from(unit));
        });

        return unitInfoExports;
    }
}
