repo1/src/main/java/nl/obren/sokrates/common/renderingutils/VisualizationTemplate.java [25:34]:
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        try {
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            content = content.replace("${data}", new JsonGenerator().generate(data));

            return content;
        } catch (IOException e) {
            LOG.error(e);
        }

        return null;
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -



repo1/src/main/java/nl/obren/sokrates/common/renderingutils/VisualizationTemplate.java [57:66]:
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        try {
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            content = content.replace("${data}", new JsonGenerator().generate(data));

            return content;
        } catch (IOException e) {
            LOG.error(e);
        }

        return null;
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -



