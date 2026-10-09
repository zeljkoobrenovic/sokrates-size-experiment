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



repo1/src/main/java/nl/obren/sokrates/common/renderingutils/VisualizationTemplate.java [73:82]:
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



