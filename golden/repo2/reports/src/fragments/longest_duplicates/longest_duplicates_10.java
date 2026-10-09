repo1/src/main/java/nl/obren/sokrates/common/renderingutils/charts/BarChart.java [101:106]:
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        String functionId = "draw_" + id;
        return "<script type=\"text/javascript\">\n" +
                "      google.charts.load(\"current\", {packages:[\"corechart\"]});\n" +
                "      google.charts.setOnLoadCallback(" + functionId + ");\n" +
                "      function " + functionId + "() {\n" +
                "        var data = google.visualization.arrayToDataTable([\n" +
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -



repo1/src/main/java/nl/obren/sokrates/common/renderingutils/charts/PieChart.java [96:101]:
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        String functionId = "draw_" + id;
        return "<script type=\"text/javascript\">\n" +
                "      google.charts.load(\"current\", {packages:[\"corechart\"]});\n" +
                "      google.charts.setOnLoadCallback(" + functionId + ");\n" +
                "      function " + functionId + "() {\n" +
                "        var data = google.visualization.arrayToDataTable([\n" +
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -



